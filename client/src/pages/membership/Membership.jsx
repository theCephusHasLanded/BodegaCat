import React, { useState, useEffect } from 'react';
import { Container, Row, Col, Card, Button, Badge, Alert } from 'react-bootstrap';
import MembershipService from '../../services/membership.service';
import { useAuth } from '../../context/AuthContext';
import LoadingSpinner from '../../components/LoadingSpinner';
import './Membership.css';

const Membership = () => {
  const [membership, setMembership] = useState(null);
  const [benefits, setBenefits] = useState({});
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');
  const { currentUser } = useAuth();

  useEffect(() => {
    const fetchData = async () => {
      try {
        setLoading(true);
        const [membershipData, benefitsData] = await Promise.all([
          currentUser ? MembershipService.getCurrentMembership() : Promise.resolve(null),
          MembershipService.getMembershipBenefits()
        ]);
        
        setMembership(membershipData);
        setBenefits(benefitsData);
      } catch (err) {
        setError('Failed to load membership data. Please try again later.');
        console.error(err);
      } finally {
        setLoading(false);
      }
    };

    fetchData();
  }, [currentUser]);

  const handleSubscribe = async (tier) => {
    try {
      setLoading(true);
      setError('');
      
      if (tier === 'REGULAR') {
        await MembershipService.subscribeRegular();
      } else if (tier === 'CHAMPION') {
        await MembershipService.subscribeChampion();
      }
      
      const updatedMembership = await MembershipService.getCurrentMembership();
      setMembership(updatedMembership);
      setSuccess(`Successfully subscribed to ${tier.toLowerCase()} membership!`);
    } catch (err) {
      setError('Failed to process subscription. Please try again later.');
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  const handleCancelMembership = async () => {
    if (window.confirm('Are you sure you want to cancel your membership?')) {
      try {
        setLoading(true);
        setError('');
        
        await MembershipService.cancelMembership();
        setMembership(null);
        setSuccess('Membership successfully cancelled.');
      } catch (err) {
        setError('Failed to cancel membership. Please try again later.');
        console.error(err);
      } finally {
        setLoading(false);
      }
    }
  };

  if (loading) return <LoadingSpinner />;

  return (
    <Container className="membership-container">
      <h1 className="text-center mb-4">Bodega Cat Membership</h1>
      
      {error && <Alert variant="danger">{error}</Alert>}
      {success && <Alert variant="success">{success}</Alert>}
      
      {currentUser && membership && membership.tier && (
        <Alert variant="info" className="text-center">
          You are currently a <strong>{membership.tier.toLowerCase()}</strong> member!
          Your membership is active until {new Date(membership.endDate).toLocaleDateString()}.
        </Alert>
      )}
      
      <Row className="mb-5">
        <Col md={4}>
          <Card className="membership-card">
            <Card.Header as="h5">Casual</Card.Header>
            <Card.Body>
              <Card.Title className="price">Free</Card.Title>
              <div className="benefits">
                <ul>
                  {benefits.CASUAL && benefits.CASUAL.map((benefit, index) => (
                    <li key={index}>{benefit}</li>
                  ))}
                </ul>
              </div>
              <div className="text-center">
                <Badge bg="secondary">Current</Badge>
              </div>
            </Card.Body>
          </Card>
        </Col>
        
        <Col md={4}>
          <Card className="membership-card">
            <Card.Header as="h5">Regular</Card.Header>
            <Card.Body>
              <Card.Title className="price">$5/month</Card.Title>
              <div className="benefits">
                <ul>
                  {benefits.REGULAR && benefits.REGULAR.map((benefit, index) => (
                    <li key={index}>{benefit}</li>
                  ))}
                </ul>
              </div>
              {currentUser ? (
                membership && membership.tier === 'REGULAR' ? (
                  <Button variant="outline-secondary" onClick={handleCancelMembership}>Cancel</Button>
                ) : (
                  <Button 
                    variant="primary" 
                    onClick={() => handleSubscribe('REGULAR')}
                    disabled={membership && membership.tier === 'CHAMPION'}
                  >
                    Subscribe
                  </Button>
                )
              ) : (
                <Button variant="primary" disabled>Sign in to subscribe</Button>
              )}
            </Card.Body>
          </Card>
        </Col>
        
        <Col md={4}>
          <Card className="membership-card champion">
            <Card.Header as="h5">Champion</Card.Header>
            <Card.Body>
              <Card.Title className="price">$15/month</Card.Title>
              <div className="benefits">
                <ul>
                  {benefits.CHAMPION && benefits.CHAMPION.map((benefit, index) => (
                    <li key={index}>{benefit}</li>
                  ))}
                </ul>
              </div>
              {currentUser ? (
                membership && membership.tier === 'CHAMPION' ? (
                  <Button variant="outline-secondary" onClick={handleCancelMembership}>Cancel</Button>
                ) : (
                  <Button 
                    variant="success" 
                    onClick={() => handleSubscribe('CHAMPION')}
                  >
                    Subscribe
                  </Button>
                )
              ) : (
                <Button variant="success" disabled>Sign in to subscribe</Button>
              )}
            </Card.Body>
          </Card>
        </Col>
      </Row>
      
      <div className="text-center membership-info">
        <h3>Why become a member?</h3>
        <p>
          Your membership directly supports our mission to celebrate and preserve the cultural 
          heritage of bodega cats across neighborhoods. Join our community of bodega 
          cat enthusiasts and help us make a difference!
        </p>
      </div>
    </Container>
  );
};

export default Membership;