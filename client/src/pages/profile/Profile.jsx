import React, { useState, useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import LoadingSpinner from '../../components/LoadingSpinner';
import './Profile.css';

const Profile = () => {
  const { currentUser, logout } = useAuth();
  const navigate = useNavigate();
  const [userData, setUserData] = useState({
    firstName: '',
    lastName: '',
    email: '',
    username: ''
  });
  const [loading, setLoading] = useState(true);
  
  // These would come from API in a real app
  const [orders, setOrders] = useState([]);
  const [visits, setVisits] = useState([]);
  const [achievements, setAchievements] = useState([]);

  useEffect(() => {
    if (currentUser) {
      // In a real app, you would fetch the complete user profile from the API
      setUserData({
        firstName: currentUser.firstName || '',
        lastName: currentUser.lastName || '',
        email: currentUser.email || '',
        username: currentUser.username || ''
      });
      
      // Mock data for orders, visits, and achievements
      setOrders([
        { id: 1, date: '2023-05-15', total: 35.99, status: 'Delivered' },
        { id: 2, date: '2023-04-20', total: 42.50, status: 'Delivered' }
      ]);
      
      setVisits([
        { id: 1, bodega: 'Sunny Corner Deli', date: '2023-05-10', cat: 'Whiskers' },
        { id: 2, bodega: 'Brooklyn Finest', date: '2023-04-12', cat: 'Shadow' }
      ]);
      
      setAchievements([
        { id: 1, name: 'First Visit', description: 'Visited your first bodega', earnedDate: '2023-04-12' },
        { id: 2, name: 'Cat Lover', description: 'Met 5 different bodega cats', earnedDate: '2023-05-10' }
      ]);
      
      setLoading(false);
    }
  }, [currentUser]);

  const handleLogout = () => {
    logout();
    // Redirect will happen automatically via ProtectedRoute
  };

  if (loading) {
    return <LoadingSpinner />;
  }

  if (!currentUser) {
    return <div>Please log in to view your profile.</div>;
  }

  return (
    <div className="profile-container">
      <div className="profile-header-section">
        <div className="profile-intro">
          <div className="profile-avatar">
            {userData.firstName?.charAt(0) || userData.username?.charAt(0) || '?'}
          </div>
          <div className="profile-name">
            <h1>
              {userData.firstName && userData.lastName 
                ? `${userData.firstName} ${userData.lastName}` 
                : userData.username}
            </h1>
            <p className="member-since">Member since May 2023</p>
          </div>
        </div>
        
        <div className="profile-actions">
          <Link to="/profile/edit" className="edit-profile-btn">
            Edit Profile
          </Link>
          <button className="logout-btn" onClick={handleLogout}>
            Logout
          </button>
        </div>
      </div>

      <div className="profile-content">
        <div className="profile-sidebar">
          <div className="profile-card user-info">
            <h2>Account Information</h2>
            <div className="info-group">
              <label>Username</label>
              <p>{userData.username}</p>
            </div>

            <div className="info-group">
              <label>Email</label>
              <p>{userData.email}</p>
            </div>

            <div className="info-group">
              <label>First Name</label>
              <p>{userData.firstName || 'Not provided'}</p>
            </div>

            <div className="info-group">
              <label>Last Name</label>
              <p>{userData.lastName || 'Not provided'}</p>
            </div>
          </div>
          
          <div className="profile-card">
            <h2>Program Stats</h2>
            <div className="stats-group">
              <div className="stat-item">
                <span className="stat-value">5</span>
                <label>Bodegas Visited</label>
              </div>
              <div className="stat-item">
                <span className="stat-value">8</span>
                <label>Cats Met</label>
              </div>
              <div className="stat-item">
                <span className="stat-value">120</span>
                <label>Stamps Collected</label>
              </div>
            </div>
            
            <Link to="/passport" className="view-all-link">View Passport</Link>
          </div>
        </div>
        
        <div className="profile-main">
          <div className="profile-card">
            <div className="card-header">
              <h2>Recent Orders</h2>
              <Link to="/orders" className="view-all-link">View All</Link>
            </div>
            
            {orders.length === 0 ? (
              <div className="empty-state">
                <p>You haven't placed any orders yet.</p>
                <Link to="/products" className="action-btn">Browse Products</Link>
              </div>
            ) : (
              <div className="orders-list">
                {orders.map(order => (
                  <div key={order.id} className="order-item">
                    <div className="order-info">
                      <div className="order-date">{order.date}</div>
                      <div className="order-id">Order #{order.id}</div>
                      <div className="order-status">{order.status}</div>
                    </div>
                    <div className="order-total">${order.total.toFixed(2)}</div>
                    <Link to={`/orders/${order.id}`} className="view-details-link">
                      View Details
                    </Link>
                  </div>
                ))}
              </div>
            )}
          </div>
          
          <div className="profile-card">
            <div className="card-header">
              <h2>Recent Bodega Visits</h2>
              <Link to="/visits" className="view-all-link">View All</Link>
            </div>
            
            {visits.length === 0 ? (
              <div className="empty-state">
                <p>You haven't logged any bodega visits yet.</p>
                <Link to="/bodegas" className="action-btn">Find Bodegas</Link>
              </div>
            ) : (
              <div className="visits-list">
                {visits.map(visit => (
                  <div key={visit.id} className="visit-item">
                    <div className="visit-info">
                      <div className="visit-bodega">{visit.bodega}</div>
                      <div className="visit-date">{visit.date}</div>
                    </div>
                    <div className="visit-cat">
                      Met <span className="cat-name">{visit.cat}</span>
                    </div>
                    <Link to={`/bodegas/${visit.id}`} className="view-details-link">
                      View Bodega
                    </Link>
                  </div>
                ))}
              </div>
            )}
          </div>
          
          <div className="profile-card">
            <div className="card-header">
              <h2>Achievements</h2>
              <Link to="/achievements" className="view-all-link">View All</Link>
            </div>
            
            {achievements.length === 0 ? (
              <div className="empty-state">
                <p>You haven't earned any achievements yet.</p>
                <Link to="/bodegas" className="action-btn">Start Exploring</Link>
              </div>
            ) : (
              <div className="achievements-list">
                {achievements.map(achievement => (
                  <div key={achievement.id} className="achievement-item">
                    <div className="achievement-badge">🏆</div>
                    <div className="achievement-info">
                      <div className="achievement-name">{achievement.name}</div>
                      <div className="achievement-description">{achievement.description}</div>
                      <div className="achievement-date">Earned on {achievement.earnedDate}</div>
                    </div>
                  </div>
                ))}
              </div>
            )}
          </div>
        </div>
      </div>
    </div>
  );
};

export default Profile;
