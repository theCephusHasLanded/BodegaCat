import React from 'react';
import { Link } from 'react-router-dom';
import './Home.css'; 

const Home = () => {
  return (
    <div className="home-container">
      <div className="hero-section">
        <div className="hero-content">
          <h1>Welcome to Bodega Cat</h1>
          <p>Celebrating the beloved feline guardians of New York City's corner stores.</p>
          <div className="hero-buttons">
            <Link to="/cats" className="primary-button">Meet the Cats</Link>
            <Link to="/about" className="secondary-button">Learn More</Link>
          </div>
        </div>
      </div>

      <div className="features-section">
        <div className="container">
          <h2>Why Bodega Cats Matter</h2>
          <div className="features-grid">
            <div className="feature-card">
              <div className="feature-icon">🏪</div>
              <h3>Store Guardians</h3>
              <p>Bodega cats keep stores free of pests and bring good fortune to their owners.</p>
            </div>
            <div className="feature-card">
              <div className="feature-icon">❤️</div>
              <h3>Community Mascots</h3>
              <p>These cats become beloved fixtures in their neighborhoods, known by locals.</p>
            </div>
            <div className="feature-card">
              <div className="feature-icon">📸</div>
              <h3>Instagram Famous</h3>
              <p>Many bodega cats have their own social media followings and fan clubs.</p>
            </div>
          </div>
        </div>
      </div>

      <div className="cta-section">
        <div className="container">
          <h2>Join Our Community</h2>
          <p>Sign up to share your favorite bodega cat sightings and stories.</p>
          <Link to="/register" className="cta-button">Sign Up Now</Link>
        </div>
      </div>
    </div>
  );
};

export default Home;
