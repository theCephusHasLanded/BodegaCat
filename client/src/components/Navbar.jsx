import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import './Navbar.css';

const Navbar = () => {
  const { currentUser, logout } = useAuth();
  const [isMenuOpen, setIsMenuOpen] = useState(false);
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate('/login');
    setIsMenuOpen(false);
  };

  const toggleMenu = () => {
    setIsMenuOpen(!isMenuOpen);
  };

  return (
    <nav className="navbar">
      <div className="navbar-container">
        <Link to="/" className="navbar-logo">
          Bodega Cat
        </Link>

        {/* Mobile menu button */}
        <div className="menu-icon" onClick={toggleMenu}>
          <span className={`menu-icon-bar ${isMenuOpen ? 'open' : ''}`}></span>
          <span className={`menu-icon-bar ${isMenuOpen ? 'open' : ''}`}></span>
          <span className={`menu-icon-bar ${isMenuOpen ? 'open' : ''}`}></span>
        </div>

        {/* Navigation links */}
        <ul className={`nav-menu ${isMenuOpen ? 'active' : ''}`}>
          <li className="nav-item">
            <Link to="/" className="nav-link" onClick={() => setIsMenuOpen(false)}>
              Home
            </Link>
          </li>
          <li className="nav-item">
            <Link to="/cats" className="nav-link" onClick={() => setIsMenuOpen(false)}>
              Cats
            </Link>
          </li>
          <li className="nav-item">
            <Link to="/about" className="nav-link" onClick={() => setIsMenuOpen(false)}>
              About
            </Link>
          </li>

          {/* Conditional rendering based on authentication state */}
          {currentUser ? (
            <>
              <li className="nav-item">
                <Link to="/profile" className="nav-link" onClick={() => setIsMenuOpen(false)}>
                  Profile
                </Link>
              </li>
              <li className="nav-item">
                <button onClick={handleLogout} className="nav-button logout-button">
                  Logout
                </button>
              </li>
            </>
          ) : (
            <>
              <li className="nav-item">
                <Link to="/login" className="nav-link" onClick={() => setIsMenuOpen(false)}>
                  Login
                </Link>
              </li>
              <li className="nav-item">
                <Link to="/register" className="nav-button register-button" onClick={() => setIsMenuOpen(false)}>
                  Register
                </Link>
              </li>
            </>
          )}
        </ul>
      </div>
    </nav>
  );
};

export default Navbar;
