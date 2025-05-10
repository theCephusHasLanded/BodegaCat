import React, { useState, useEffect } from 'react';
import { Container, Row, Col, Card, ProgressBar, Badge, ListGroup, Table } from 'react-bootstrap';
import { useAuth } from '../../context/AuthContext';
import AchievementService from '../../services/achievement.service';
import BodegaService from '../../services/bodega.service';
import LoadingSpinner from '../../components/LoadingSpinner';
import './Passport.css';

const Passport = () => {
  const [achievements, setAchievements] = useState([]);
  const [userAchievements, setUserAchievements] = useState([]);
  const [progress, setProgress] = useState({});
  const [passportPoints, setPassportPoints] = useState(0);
  const [leaderboard, setLeaderboard] = useState([]);
  const [recentVisits, setRecentVisits] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const { currentUser } = useAuth();

  useEffect(() => {
    const fetchData = async () => {
      try {
        setLoading(true);
        
        // Fetch achievements data
        const [allAchievements, leaderboardData] = await Promise.all([
          AchievementService.getAllAchievements(),
          AchievementService.getAchievementLeaderboard()
        ]);
        
        setAchievements(allAchievements);
        setLeaderboard(leaderboardData);
        
        // Fetch user-specific data if logged in
        if (currentUser) {
          const [userAchievementsData, progressData, pointsData, visitsData] = await Promise.all([
            AchievementService.getUserAchievements(),
            AchievementService.getUserAchievementProgress(),
            AchievementService.getUserPassportPoints(),
            BodegaService.getUserBodegaVisits()
          ]);
          
          setUserAchievements(userAchievementsData);
          setProgress(progressData);
          setPassportPoints(pointsData.passportPoints);
          setRecentVisits(visitsData.slice(0, 5)); // Get 5 most recent visits
        }
      } catch (err) {
        setError('Failed to load passport data. Please try again later.');
        console.error(err);
      } finally {
        setLoading(false);
      }
    };

    fetchData();
  }, [currentUser]);

  const groupAchievementsByCategory = (achievements) => {
    return achievements.reduce((groups, achievement) => {
      const category = achievement.category || 'MISC';
      if (!groups[category]) {
        groups[category] = [];
      }
      groups[category].push(achievement);
      return groups;
    }, {});
  };

  const isAchievementEarned = (achievementId) => {
    return userAchievements.some(a => a.id === achievementId);
  };

  if (loading) return <LoadingSpinner />;

  // Group achievements by category
  const groupedAchievements = groupAchievementsByCategory(achievements);
  
  // Format category names for display
  const formatCategoryName = (category) => {
    return category.charAt(0) + category.slice(1).toLowerCase();
  };

  return (
    <Container className="passport-container">
      <h1 className="text-center mb-4">Bodega Cat Passport</h1>
      
      {!currentUser && (
        <Card className="mb-4 text-center">
          <Card.Body>
            <Card.Title>Sign in to track your achievements!</Card.Title>
            <Card.Text>
              Create an account or sign in to start collecting stamps in your Bodega Cat Passport.
            </Card.Text>
          </Card.Body>
        </Card>
      )}
      
      {currentUser && (
        <Row className="mb-4">
          <Col lg={6} className="mb-3">
            <Card className="passport-summary">
              <Card.Body>
                <Card.Title>Your Passport Summary</Card.Title>
                <div className="passport-points">
                  <span className="points-value">{passportPoints}</span>
                  <span className="points-label">Passport Points</span>
                </div>
                <div className="achievements-summary">
                  <div>
                    <span className="summary-value">{userAchievements.length}</span>
                    <span className="summary-label">Achievements Earned</span>
                  </div>
                  <div>
                    <span className="summary-value">{achievements.length - userAchievements.length}</span>
                    <span className="summary-label">Achievements Left</span>
                  </div>
                </div>
                <ProgressBar 
                  now={(userAchievements.length / achievements.length) * 100} 
                  label={`${Math.round((userAchievements.length / achievements.length) * 100)}%`} 
                  className="mt-3" 
                  variant="success"
                />
              </Card.Body>
            </Card>
          </Col>
          
          <Col lg={6} className="mb-3">
            <Card className="recent-visits">
              <Card.Body>
                <Card.Title>Recent Bodega Visits</Card.Title>
                {recentVisits.length > 0 ? (
                  <ListGroup variant="flush">
                    {recentVisits.map((visit) => (
                      <ListGroup.Item key={visit.id} className="recent-visit-item">
                        <div className="visit-store">{visit.bodegaStore.name}</div>
                        <div className="visit-date">
                          {new Date(visit.visitDate).toLocaleDateString()}
                        </div>
                        <div className="visit-rating">
                          {"★".repeat(visit.rating)} {"☆".repeat(5 - visit.rating)}
                        </div>
                      </ListGroup.Item>
                    ))}
                  </ListGroup>
                ) : (
                  <div className="text-center p-3">
                    No bodega visits recorded yet. Start your adventure!
                  </div>
                )}
              </Card.Body>
            </Card>
          </Col>
        </Row>
      )}
      
      <h2 className="text-center mb-3">Achievement Stamps</h2>
      
      {Object.keys(groupedAchievements).map((category) => (
        <Card key={category} className="mb-4 achievement-category">
          <Card.Header>
            <h3>{formatCategoryName(category)} Achievements</h3>
          </Card.Header>
          <Card.Body>
            <Row>
              {groupedAchievements[category].map((achievement) => {
                const earned = isAchievementEarned(achievement.id);
                return (
                  <Col key={achievement.id} md={4} sm={6} className="mb-3">
                    <Card 
                      className={`achievement-card ${earned ? 'earned' : 'locked'}`}
                    >
                      <Card.Body>
                        <div className="achievement-header">
                          <Card.Title>{achievement.name}</Card.Title>
                          <Badge bg={earned ? "success" : "secondary"}>
                            {earned ? "Earned" : "Locked"}
                          </Badge>
                        </div>
                        <Card.Text>{achievement.description}</Card.Text>
                        <div className="achievement-points">
                          {achievement.pointsValue} points
                        </div>
                        {!earned && progress[achievement.id] && (
                          <ProgressBar 
                            now={progress[achievement.id].progress} 
                            max={progress[achievement.id].target} 
                            label={`${progress[achievement.id].progress}/${progress[achievement.id].target}`}
                            className="mt-2"
                          />
                        )}
                      </Card.Body>
                    </Card>
                  </Col>
                );
              })}
            </Row>
          </Card.Body>
        </Card>
      ))}
      
      <h2 className="text-center mb-3">Passport Leaderboard</h2>
      <Card>
        <Card.Body>
          <Table striped responsive>
            <thead>
              <tr>
                <th>#</th>
                <th>Username</th>
                <th>Passport Points</th>
                <th>Achievements</th>
              </tr>
            </thead>
            <tbody>
              {leaderboard.map((user, index) => (
                <tr key={user.id} className={user.id === (currentUser?.id || -1) ? 'current-user' : ''}>
                  <td>{index + 1}</td>
                  <td>{user.username}</td>
                  <td>{user.passportPoints}</td>
                  <td>{user.achievementCount}</td>
                </tr>
              ))}
            </tbody>
          </Table>
        </Card.Body>
      </Card>
    </Container>
  );
};

export default Passport;