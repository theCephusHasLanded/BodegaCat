import React, { useState, useEffect } from 'react';
import { Container, Row, Col, Card, Button, Form, Tabs, Tab, Modal, Badge } from 'react-bootstrap';
import { useAuth } from '../../context/AuthContext';
import BodegaService from '../../services/bodega.service';
import LoadingSpinner from '../../components/LoadingSpinner';
import './BodegaExplorer.css';

const BodegaExplorer = () => {
  const [bodegaStores, setBodegaStores] = useState([]);
  const [neighborhoods, setNeighborhoods] = useState([]);
  const [selectedNeighborhood, setSelectedNeighborhood] = useState('');
  const [selectedStore, setSelectedStore] = useState(null);
  const [storeCats, setStoreCats] = useState([]);
  const [storeVisits, setStoreVisits] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [showVisitModal, setShowVisitModal] = useState(false);
  const [visitRating, setVisitRating] = useState(5);
  const [visitReview, setVisitReview] = useState('');
  const [visitPhotoUrl, setVisitPhotoUrl] = useState('');
  const [visitSuccess, setVisitSuccess] = useState(false);
  const { currentUser } = useAuth();

  useEffect(() => {
    const fetchInitialData = async () => {
      try {
        setLoading(true);
        const [stores, neighborhoods] = await Promise.all([
          BodegaService.getAllBodegaStores(),
          BodegaService.getAllNeighborhoods()
        ]);
        
        setBodegaStores(stores);
        setNeighborhoods(neighborhoods);
      } catch (err) {
        setError('Failed to load bodega data. Please try again later.');
        console.error(err);
      } finally {
        setLoading(false);
      }
    };

    fetchInitialData();
  }, []);

  useEffect(() => {
    const fetchNeighborhoodStores = async () => {
      if (!selectedNeighborhood) return;
      
      try {
        setLoading(true);
        const stores = await BodegaService.getBodegaStoresByNeighborhood(selectedNeighborhood);
        setBodegaStores(stores);
      } catch (err) {
        setError('Failed to load stores for this neighborhood.');
        console.error(err);
      } finally {
        setLoading(false);
      }
    };

    fetchNeighborhoodStores();
  }, [selectedNeighborhood]);

  const handleViewStore = async (storeId) => {
    try {
      setLoading(true);
      const [store, cats, visits] = await Promise.all([
        BodegaService.getBodegaStoreById(storeId),
        BodegaService.getBodegaStoreCats(storeId),
        BodegaService.getBodegaVisits(storeId)
      ]);
      
      setSelectedStore(store);
      setStoreCats(cats);
      setStoreVisits(visits);
    } catch (err) {
      setError('Failed to load store details. Please try again later.');
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  const handleBackToList = () => {
    setSelectedStore(null);
    setStoreCats([]);
    setStoreVisits([]);
  };

  const handleRecordVisit = () => {
    setShowVisitModal(true);
  };

  const handleVisitSubmit = async (e) => {
    e.preventDefault();
    
    try {
      setLoading(true);
      
      await BodegaService.recordBodegaVisit(selectedStore.id, {
        rating: visitRating,
        review: visitReview,
        photoUrl: visitPhotoUrl
      });
      
      // Refresh store visits
      const updatedVisits = await BodegaService.getBodegaVisits(selectedStore.id);
      setStoreVisits(updatedVisits);
      
      // Reset form
      setVisitRating(5);
      setVisitReview('');
      setVisitPhotoUrl('');
      setVisitSuccess(true);
      
      // Close modal after a short delay
      setTimeout(() => {
        setShowVisitModal(false);
        setVisitSuccess(false);
      }, 2000);
      
    } catch (err) {
      setError('Failed to record visit. Please try again.');
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  if (loading && !selectedStore && bodegaStores.length === 0) {
    return <LoadingSpinner />;
  }

  // Calculate average rating for a store
  const getAverageRating = (visits) => {
    if (!visits || visits.length === 0) return 0;
    const sum = visits.reduce((total, visit) => total + visit.rating, 0);
    return (sum / visits.length).toFixed(1);
  };

  return (
    <Container className="bodega-explorer-container">
      <h1 className="text-center mb-4">Explore NYC Bodegas</h1>
      
      {error && <div className="alert alert-danger">{error}</div>}
      
      {!selectedStore ? (
        <>
          <div className="neighborhood-filter mb-4">
            <Form.Group>
              <Form.Label>Filter by Neighborhood</Form.Label>
              <Form.Select
                value={selectedNeighborhood}
                onChange={(e) => setSelectedNeighborhood(e.target.value)}
              >
                <option value="">All Neighborhoods</option>
                {neighborhoods.map((neighborhood) => (
                  <option key={neighborhood} value={neighborhood}>
                    {neighborhood}
                  </option>
                ))}
              </Form.Select>
            </Form.Group>
          </div>
          
          <Row>
            {bodegaStores.map((store) => (
              <Col key={store.id} lg={4} md={6} className="mb-4">
                <Card className="bodega-card h-100">
                  {store.imageUrl ? (
                    <Card.Img variant="top" src={store.imageUrl} className="bodega-card-image" />
                  ) : (
                    <div className="bodega-card-placeholder">
                      <span>{store.name.charAt(0)}</span>
                    </div>
                  )}
                  <Card.Body>
                    <Card.Title>{store.name}</Card.Title>
                    <div className="bodega-location mb-2">
                      <Badge bg="info">{store.neighborhood}</Badge>
                    </div>
                    <Card.Text>{store.description || 'A neighborhood bodega with character.'}</Card.Text>
                    
                    <div className="bodega-stats">
                      <div>
                        <span className="cats-count">{store.cats ? store.cats.length : 0}</span> Cats
                      </div>
                      <div>
                        <span className="visits-count">{store.visits ? store.visits.length : 0}</span> Visits
                      </div>
                    </div>
                    
                    <div className="mt-3 d-grid">
                      <Button 
                        variant="primary" 
                        onClick={() => handleViewStore(store.id)}
                      >
                        View Details
                      </Button>
                    </div>
                  </Card.Body>
                </Card>
              </Col>
            ))}
          </Row>
        </>
      ) : (
        <div className="bodega-detail">
          <Button 
            variant="outline-secondary" 
            onClick={handleBackToList}
            className="mb-3"
          >
            &larr; Back to all bodegas
          </Button>
          
          <Card className="mb-4">
            {selectedStore.imageUrl ? (
              <Card.Img 
                variant="top" 
                src={selectedStore.imageUrl} 
                className="bodega-detail-image" 
              />
            ) : (
              <div className="bodega-detail-placeholder">
                <span>{selectedStore.name.charAt(0)}</span>
              </div>
            )}
            <Card.Body>
              <Card.Title className="h2">{selectedStore.name}</Card.Title>
              
              <div className="bodega-detail-meta mb-3">
                <Badge bg="info" className="me-2">{selectedStore.neighborhood}</Badge>
                <div className="rating-display">
                  <span className="rating-stars">
                    {"★".repeat(Math.round(getAverageRating(storeVisits)))}
                    {"☆".repeat(5 - Math.round(getAverageRating(storeVisits)))}
                  </span>
                  <span className="rating-value">
                    {getAverageRating(storeVisits)} ({storeVisits.length} {storeVisits.length === 1 ? 'review' : 'reviews'})
                  </span>
                </div>
              </div>
              
              <Card.Text className="bodega-description">
                {selectedStore.description || 'A neighborhood bodega with character.'}
              </Card.Text>
              
              <div className="bodega-address mb-3">
                <strong>Address:</strong> {selectedStore.address}
              </div>
              
              <div className="text-center mt-4">
                <Button 
                  variant="success" 
                  size="lg" 
                  onClick={handleRecordVisit}
                  disabled={!currentUser}
                >
                  I Visited This Bodega!
                </Button>
                {!currentUser && (
                  <div className="text-muted mt-2">Sign in to record your visit</div>
                )}
              </div>
            </Card.Body>
          </Card>
          
          <Tabs defaultActiveKey="cats" className="mb-4">
            <Tab eventKey="cats" title="Bodega Cats">
              <div className="p-3">
                {storeCats.length > 0 ? (
                  <Row>
                    {storeCats.map((cat) => (
                      <Col key={cat.id} md={4} className="mb-4">
                        <Card className="cat-card">
                          {cat.imageUrl ? (
                            <Card.Img variant="top" src={cat.imageUrl} className="cat-image" />
                          ) : (
                            <div className="cat-placeholder">
                              <span>🐱</span>
                            </div>
                          )}
                          <Card.Body>
                            <Card.Title>{cat.name}</Card.Title>
                            <Card.Text>{cat.description || 'A friendly bodega cat.'}</Card.Text>
                          </Card.Body>
                        </Card>
                      </Col>
                    ))}
                  </Row>
                ) : (
                  <div className="alert alert-info">
                    No cats have been added for this bodega yet.
                  </div>
                )}
              </div>
            </Tab>
            <Tab eventKey="visits" title="Recent Visits">
              <div className="p-3">
                {storeVisits.length > 0 ? (
                  <div className="visits-list">
                    {storeVisits.map((visit) => (
                      <Card key={visit.id} className="mb-3 visit-card">
                        <Card.Body>
                          <div className="d-flex justify-content-between align-items-start">
                            <div>
                              <div className="visit-user">{visit.user.username}</div>
                              <div className="visit-date">
                                Visited on {new Date(visit.visitDate).toLocaleDateString()}
                              </div>
                            </div>
                            <div className="visit-rating">
                              {"★".repeat(visit.rating)}
                              {"☆".repeat(5 - visit.rating)}
                            </div>
                          </div>
                          
                          {visit.review && (
                            <div className="visit-review mt-3">
                              {visit.review}
                            </div>
                          )}
                          
                          {visit.photoUrl && (
                            <div className="visit-photo mt-3">
                              <img src={visit.photoUrl} alt="Visit" className="img-fluid rounded" />
                            </div>
                          )}
                        </Card.Body>
                      </Card>
                    ))}
                  </div>
                ) : (
                  <div className="alert alert-info">
                    No visits have been recorded for this bodega yet. Be the first!
                  </div>
                )}
              </div>
            </Tab>
          </Tabs>
        </div>
      )}
      
      {/* Visit Modal */}
      <Modal show={showVisitModal} onHide={() => setShowVisitModal(false)} centered>
        <Modal.Header closeButton>
          <Modal.Title>Record Your Visit to {selectedStore?.name}</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          {visitSuccess ? (
            <div className="text-center py-4">
              <h4 className="text-success">Visit recorded successfully!</h4>
              <p>Thanks for sharing your experience.</p>
            </div>
          ) : (
            <Form onSubmit={handleVisitSubmit}>
              <Form.Group className="mb-3">
                <Form.Label>Rating</Form.Label>
                <div className="rating-input">
                  {[1, 2, 3, 4, 5].map((star) => (
                    <span 
                      key={star}
                      className={`star ${star <= visitRating ? 'selected' : ''}`}
                      onClick={() => setVisitRating(star)}
                    >
                      ★
                    </span>
                  ))}
                </div>
              </Form.Group>
              
              <Form.Group className="mb-3">
                <Form.Label>Review (Optional)</Form.Label>
                <Form.Control 
                  as="textarea" 
                  rows={3}
                  value={visitReview} 
                  onChange={(e) => setVisitReview(e.target.value)}
                  placeholder="Share your experience at this bodega..."
                />
              </Form.Group>
              
              <Form.Group className="mb-4">
                <Form.Label>Photo URL (Optional)</Form.Label>
                <Form.Control 
                  type="url"
                  value={visitPhotoUrl} 
                  onChange={(e) => setVisitPhotoUrl(e.target.value)}
                  placeholder="Add a link to your photo"
                />
              </Form.Group>
              
              <div className="d-grid">
                <Button variant="success" type="submit" disabled={loading}>
                  {loading ? 'Saving...' : 'Record Visit'}
                </Button>
              </div>
            </Form>
          )}
        </Modal.Body>
      </Modal>
    </Container>
  );
};

export default BodegaExplorer;