import React, { useState, useEffect } from 'react';
import { useAuth } from '../../context/AuthContext';
import './Profile.css';

const Profile = () => {
  const { currentUser, logout } = useAuth();
  const [userData, setUserData] = useState({
    firstName: '',
    lastName: '',
    email: '',
    username: ''
  });

  useEffect(() => {
    if (currentUser) {
      // In a real app, you would fetch the complete user profile from the API
      setUserData({
        firstName: currentUser.firstName || '',
        lastName: currentUser.lastName || '',
        email: currentUser.email || '',
        username: currentUser.username || ''
      });
    }
  }, [currentUser]);

  const handleLogout = () => {
    logout();
    // Redirect will happen automatically via ProtectedRoute
  };

  if (!currentUser) {
    return <div>Loading...</div>;
  }

  return (
    <div className="profile-container">
      <div className="profile-card">
        <div className="profile-header">
          <h2>User Profile</h2>
          <button className="logout-button" onClick={handleLogout}>
            Logout
          </button>
        </div>

        <div className="profile-info">
          <div className="info-group">
            <label>Username</label>
            <p>{userData.username}</p>
          </div>

          <div className="info-group">
            <label>Email</label>
            <p>{userData.email}</p>
          </div>

          <div className="info-row">
            <div className="info-group">
              <label>First Name</label>
              <p>{userData.firstName || 'Not provided'}</p>
            </div>

            <div className="info-group">
              <label>Last Name</label>
              <p>{userData.lastName || 'Not provided'}</p>
            </div>
          </div>
        </div>

        <div className="profile-actions">
          <button className="edit-button">Edit Profile</button>
        </div>
      </div>
    </div>
  );
};

export default Profile;
