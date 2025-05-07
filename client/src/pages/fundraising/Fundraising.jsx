import React, { useState, useEffect } from 'react';
import { Container, Row, Col, Card, Button, ProgressBar, Form, Modal } from 'react-bootstrap';
import { useAuth } from '../../context/AuthContext';
import FundraiserService from '../../services/fundraiser.service';
import LoadingSpinner from '../../components/LoadingSpinner';
import './Fundraising.css';

const Fundraising = () => {
  const [fundraisers, setFundraisers] = useState([]);
  const [selectedFundraiser, setSelectedFundraiser] = useState(null);
  const [donations, setDonations] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [showDonateModal, setShowDonateModal] = useState(false);
  const [donationAmount, setDonationAmount] = useState('');
  const [donationMessage, setDonationMessage] = useState('');
  const [anonymous, setAnonymous] = useState(false);
  const [donationSuccess, setDonationSuccess] = useState(false);
  const { currentUser } = useAuth();

  useEffect(() => {
    const fetchFundraisers = async () => {
      try {
        setLoading(true);
        const data = await FundraiserService.getAllFundraisers();
        setFundraisers(data);
      } catch (err) {
        setError('Failed to load fundraisers. Please try again later.');
        console.error(err);
      } finally {
        setLoading(false);
      }
    };

    fetchFundraisers();
  }, []);

  const handleViewFundraiser = async (fundraiserId) => {
    try {
      setLoading(true);
      const [fundraiser, donations] = await Promise.all([
        FundraiserService.getFundraiserById(fundraiserId),
        FundraiserService.getFundraiserDonations(fundraiserId)
      ]);
      
      setSelectedFundraiser(fundraiser);
      setDonations(donations);
    } catch (err) {
      setError('Failed to load fundraiser details. Please try again later.');
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  const handleDonate = (fundraiser) => {
    setSelectedFundraiser(fundraiser);
    setShowDonateModal(true);
  };

  const handleDonationSubmit = async (e) => {
    e.preventDefault();
    
    try {
      setLoading(true);
      
      await FundraiserService.makeDonation(selectedFundraiser.id, {
        amount: Number(donationAmount),
        message: donationMessage,
        anonymous: anonymous
      });
      
      // Refresh fundraiser data
      const [updatedFundraiser, updatedDonations, allFundraisers] = await Promise.all([
        FundraiserService.getFundraiserById(selectedFundraiser.id),
        FundraiserService.getFundraiserDonations(selectedFundraiser.id),
        FundraiserService.getAllFundraisers()
      ]);
      
      setSelectedFundraiser(updatedFundraiser);
      setDonations(updatedDonations);
      setFundraisers(allFundraisers);
      
      // Reset form
      setDonationAmount('');
      setDonationMessage('');
      setAnonymous(false);
      setDonationSuccess(true);
      
      // Close modal after a short delay
      setTimeout(() => {
        setShowDonateModal(false);
        setDonationSuccess(false);
      }, 2000);
      
    } catch (err) {
      setError('Failed to process donation. Please try again.');
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  const handleBackToList = () => {
    setSelectedFundraiser(null);
    setDonations([]);
  };

  if (loading && !selectedFundraiser && fundraisers.length === 0) {
    return <LoadingSpinner />;
  }

  // Calculate progress percentage
  const getProgressPercentage = (current, goal) => {
    return Math.min(Math.round((current / goal) * 100), 100);
  };

  return (
    <Container className="fundraising-container">
      <h1 className="text-center mb-4">Support Bodega Cats & Owners</h1>
      
      {error && <div className="alert alert-danger">{error}</div>}
      
      {!selectedFundraiser ? (
        <>
          <div className="fundraising-intro mb-4">
            <p>
              Help support bodega cats and the small businesses that house them. Your donations 
              directly impact the wellbeing of these beloved community members and the cultural 
              institutions that are bodegas in our neighborhoods.
            </p>
          </div>
          
          <Row>
            {fundraisers.map((fundraiser) => (
              <Col key={fundraiser.id} lg={4} md={6} className="mb-4">
                <Card className="fundraiser-card h-100">
                  {fundraiser.imageUrl && (
                    <Card.Img variant="top" src={fundraiser.imageUrl} />
                  )}
                  <Card.Body>
                    <Card.Title>{fundraiser.title}</Card.Title>
                    <Card.Text>{fundraiser.description}</Card.Text>
                    
                    <div className="fundraiser-stats">
                      <div className="goal-amount">
                        Goal: ${fundraiser.goalAmount.toFixed(2)}
                      </div>
                      <div className="raised-amount">
                        Raised: ${fundraiser.currentAmount.toFixed(2)}
                      </div>
                    </div>
                    
                    <ProgressBar 
                      now={getProgressPercentage(fundraiser.currentAmount, fundraiser.goalAmount)} 
                      label={`${getProgressPercentage(fundraiser.currentAmount, fundraiser.goalAmount)}%`}
                      variant="success"
                      className="mb-3"
                    />
                    
                    <div className="d-flex justify-content-between">
                      <Button 
                        variant="outline-primary" 
                        onClick={() => handleViewFundraiser(fundraiser.id)}
                      >
                        View Details
                      </Button>
                      <Button 
                        variant="success" 
                        onClick={() => handleDonate(fundraiser)}
                        disabled={!currentUser}
                      >
                        Donate
                      </Button>
                    </div>
                  </Card.Body>
                </Card>
              </Col>
            ))}
          </Row>
        </>
      ) : (
        <div className="fundraiser-detail">
          <Button 
            variant="outline-secondary" 
            onClick={handleBackToList}
            className="mb-3"
          >
            &larr; Back to all fundraisers
          </Button>
          
          <Card className="mb-4">
            {selectedFundraiser.imageUrl && (
              <Card.Img 
                variant="top" 
                src={selectedFundraiser.imageUrl} 
                className="fundraiser-detail-image" 
              />
            )}
            <Card.Body>
              <Card.Title className="h2">{selectedFundraiser.title}</Card.Title>
              
              <div className="fundraiser-detail-stats mb-3">
                <Row>
                  <Col md={8}>
                    <ProgressBar 
                      now={getProgressPercentage(selectedFundraiser.currentAmount, selectedFundraiser.goalAmount)} 
                      label={`${getProgressPercentage(selectedFundraiser.currentAmount, selectedFundraiser.goalAmount)}%`}
                      variant="success"
                      className="progress-large"
                    />
                  </Col>
                  <Col md={4} className="text-right">
                    <div className="current-amount">
                      ${selectedFundraiser.currentAmount.toFixed(2)}
                    </div>
                    <div className="goal-label">
                      of ${selectedFundraiser.goalAmount.toFixed(2)} goal
                    </div>
                  </Col>
                </Row>
              </div>
              
              <Card.Text className="fundraiser-description">
                {selectedFundraiser.description}
              </Card.Text>
              
              {selectedFundraiser.bodegaStore && (
                <div className="fundraiser-store">
                  <h5>Supporting:</h5>
                  <div>{selectedFundraiser.bodegaStore.name}</div>
                  <div className="text-muted">{selectedFundraiser.bodegaStore.neighborhood}</div>
                </div>
              )}
              
              <div className="text-center mt-4">
                <Button 
                  variant="success" 
                  size="lg" 
                  onClick={() => handleDonate(selectedFundraiser)}
                  disabled={!currentUser}
                >
                  Make a Donation
                </Button>
                {!currentUser && (
                  <div className="text-muted mt-2">Sign in to donate</div>
                )}
              </div>
            </Card.Body>
          </Card>
          
          <h3 className="mb-3">Recent Donations</h3>
          {donations.length > 0 ? (
            <div className="donations-list">
              {donations.map((donation) => (
                <Card key={donation.id} className="mb-2 donation-card">
                  <Card.Body>
                    <div className="d-flex justify-content-between">
                      <div>
                        <div className="donation-name">
                          {donation.anonymous ? 'Anonymous' : donation.user.username}
                        </div>
                        {donation.message && (
                          <div className="donation-message">{donation.message}</div>
                        )}
                      </div>
                      <div className="donation-amount">
                        ${donation.amount.toFixed(2)}
                      </div>
                    </div>
                    <div className="donation-date text-muted">
                      {new Date(donation.donationDate).toLocaleDateString()}
                    </div>
                  </Card.Body>
                </Card>
              ))}
            </div>
          ) : (
            <div className="alert alert-info">
              No donations yet. Be the first to contribute!
            </div>
          )}
        </div>
      )}
      
      {/* Donation Modal */}
      <Modal show={showDonateModal} onHide={() => setShowDonateModal(false)} centered>
        <Modal.Header closeButton>
          <Modal.Title>Donate to {selectedFundraiser?.title}</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          {donationSuccess ? (
            <div className="text-center py-4">
              <h4 className="text-success">Thank you for your donation!</h4>
              <p>Your support makes a difference.</p>
            </div>
          ) : (
            <Form onSubmit={handleDonationSubmit}>
              <Form.Group className="mb-3">
                <Form.Label>Donation Amount ($)</Form.Label>
                <Form.Control 
                  type="number" 
                  min="1" 
                  step="0.01" 
                  value={donationAmount} 
                  onChange={(e) => setDonationAmount(e.target.value)}
                  required
                />
              </Form.Group>
              
              <Form.Group className="mb-3">
                <Form.Label>Message (Optional)</Form.Label>
                <Form.Control 
                  as="textarea" 
                  rows={3}
                  value={donationMessage} 
                  onChange={(e) => setDonationMessage(e.target.value)}
                />
              </Form.Group>
              
              <Form.Group className="mb-4">
                <Form.Check 
                  type="checkbox"
                  id="anonymous-checkbox"
                  label="Make donation anonymous"
                  checked={anonymous}
                  onChange={(e) => setAnonymous(e.target.checked)}
                />
              </Form.Group>
              
              <div className="d-grid">
                <Button variant="success" type="submit" disabled={loading}>
                  {loading ? 'Processing...' : 'Complete Donation'}
                </Button>
              </div>
            </Form>
          )}
        </Modal.Body>
      </Modal>
    </Container>
  );
};

export default Fundraising;