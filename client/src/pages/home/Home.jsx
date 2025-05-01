import React from 'react';
import { Link } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import './Home.css';
import bodegaCatImage from '../../assets/bodegaCat.png'; // Import the image

const Home = () => {
  const { currentUser } = useAuth();

  return (
    <div className="home-container">
      <div className="hero-section">
        <div className="hero-content">
          <h1>Welcome to Bodega Cat</h1>
          <p>Support your local bodega cats and help them find forever homes.</p>

          {currentUser ? (
            <Link to="/cats" className="cta-button">
              Browse Cats
            </Link>
          ) : (
            <div className="cta-buttons">
              <Link to="/login" className="cta-button secondary">
                Login
              </Link>
              <Link to="/register" className="cta-button primary">
                Sign Up
              </Link>
            </div>
          )}
        </div>
      </div>

      <div className="features-section">
        <div className="feature">
          <div className="feature-icon">🐱</div>
          <h2>Meet the Cats</h2>
          <p>Browse our collection of adorable bodega cats looking for support and homes.</p>
        </div>

        <div className="feature">
          <div className="feature-icon">💰</div>
          <h2>Donate</h2>
          <p>Contribute to the well-being of these furry friends with one-time or recurring donations.</p>
        </div>

        <div className="feature">
          <div className="feature-icon">🏠</div>
          <h2>Adopt</h2>
          <p>Give a bodega cat a forever home and make a new best friend.</p>
        </div>
      </div>

      <div className="about-section">
        <h2>About Bodega Cats</h2>
        <p>
          Bodega cats are the beloved feline residents of neighborhood corner stores,
          delis, and bodegas across urban areas. They help keep the stores free of pests
          and bring joy to customers. Our mission is to support these working cats and
          help them find loving homes when they're ready to retire from bodega life.
        </p>
        <Link to="/about" className="learn-more">
          Learn More About Our Mission
        </Link>
      </div>
    </div>
  );
};

export default Home;
