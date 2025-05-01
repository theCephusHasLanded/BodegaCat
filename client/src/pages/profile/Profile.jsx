import React from 'react';
import { useAuth } from '../../context/AuthContext';
import './Profile.css';

const Profile = () => {
  const { currentUser } = useAuth();

  if (!currentUser) {
    return <div className="loading">Loading user data...</div>;
  }

  return (
    <div className="profile-container">
      <div className="profile-card">
        <div className="profile-header">
          <div className="profile-avatar">
            {currentUser.username.charAt(0).toUpperCase()}
          </div>
          <h1>{currentUser.username}</h1>
          <p className="profile-email">{currentUser.email}</p>
        </div>

        <div className="profile-details">
          <div className="profile-section">
            <h2>Account Details</h2>
            <div className="profile-info">
              <div className="profile-info-item">
                <span className="label">Username:</span>
                <span className="value">{currentUser.username}</span>
              </div>
              <div className="profile-info-item">
                <span className="label">Email:</span>
                <span className="value">{currentUser.email}</span>
              </div>
              <div className="profile-info-item">
                <span className="label">Role:</span>
                <span className="value">{currentUser.role || 'User'}</span>
              </div>
              <div className="profile-info-item">
                <span className="label">Member Since:</span>
                <span className="value">
                  {currentUser.createdAt
                    ? new Date(currentUser.createdAt).toLocaleDateString()
                    : 'N/A'}
                </span>
              </div>
            </div>
          </div>
        </div>

        <div className="profile-actions">
          <button className="profile-button edit-button">Edit Profile</button>
          <button className="profile-button password-button">Change Password</button>
        </div>
      </div>
    </div>
  );
};

export default Profile;
