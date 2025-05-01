import React from 'react';
import './About.css';

const About = () => {
  return (
    <div className="about-container">
      <div className="about-header">
        <h1>About Bodega Cat</h1>
        <p>Supporting NYC's Beloved Feline Guardians</p>
      </div>

      <div className="about-section">
        <h2>Why Bodega Cats Are a NYC Staple</h2>
        <p>
          Bodega cats are more than just adorable fixtures in New York City's corner stores—they're
          icons woven into the fabric of urban life. Their presence is both practical and symbolic,
          making them beloved by New Yorkers and visitors alike.
        </p>

        <div className="feature-grid">
          <div className="feature-card">
            <h3>Natural Pest Control</h3>
            <p>
              Bodega cats are expert hunters, keeping stores free from mice, rats, and other pests.
              This eco-friendly pest control means cleaner aisles and fresher food, without the need
              for harsh chemicals or traps.
            </p>
          </div>

          <div className="feature-card">
            <h3>Ambassadors of Community</h3>
            <p>
              These cats become familiar faces to regulars, greeting customers and forging connections.
              Over time, they develop unique personalities and routines, making each visit to the bodega
              feel like coming home.
            </p>
          </div>

          <div className="feature-card">
            <h3>Emotional Comfort</h3>
            <p>
              In a fast-paced city, bodega cats offer moments of calm and joy. Their gentle presence
              provides comfort to both customers and staff, turning quick errands into memorable experiences.
            </p>
          </div>

          <div className="feature-card">
            <h3>Symbols of Local Culture</h3>
            <p>
              Bodega cats embody the spirit of New York—resourceful, resilient, and full of character.
              They represent the quirky, communal bonds that flourish in the city's diverse neighborhoods.
            </p>
          </div>
        </div>
      </div>

      <div className="about-section">
        <h2>More Than Just Pest Control</h2>
        <p>
          The impact of bodega cats extends far beyond their practical role in keeping stores clean.
        </p>

        <div className="impact-grid">
          <div className="impact-item">
            <h3>Fostering Connections</h3>
            <p>
              Customers often stop by just to see the cat, sparking conversations and building friendships.
              Viral stories—like Mimi, the Greenpoint bodega cat who became a social media sensation—show
              how these felines can unite communities and even bring fame to their stores.
            </p>
          </div>

          <div className="impact-item">
            <h3>Enhancing Store Ambiance</h3>
            <p>
              Whether lounging in a sunny window or weaving through snack aisles, bodega cats add warmth
              and charm, making each store unique and inviting.
            </p>
          </div>

          <div className="impact-item">
            <h3>Economic and Emotional Value</h3>
            <p>
              Store owners note that cats not only keep pests away but also attract customers, boosting
              business and creating a welcoming atmosphere.
            </p>
          </div>
        </div>

        <div className="quote-box">
          <p className="quote">
            "Ultimately, the cats are a symbol of community building and the special, unique type of
            connection that happens in a city like New York."
          </p>
          <p className="quote-author">— Sydney Miller, poet and digital content creator</p>
        </div>
      </div>

      <div className="about-section">
        <h2>A Paradoxical Place in the City</h2>
        <p>
          Despite their popularity, bodega cats occupy a legally gray area. New York State regulations
          prohibit most animals in food-selling establishments, so store owners risk fines for keeping
          cats—even though their presence actually helps maintain cleanliness. This contradiction has
          sparked petitions and public outcry, with thousands advocating for legal protections for these
          feline workers.
        </p>
      </div>

      <div className="about-section">
        <h2>Our Mission</h2>
        <p>
          At Bodega Cat, we celebrate and support the cats that call New York's corner stores home.
          These remarkable felines keep our bodegas pest-free, bring smiles to customers, and embody
          the spirit of community that defines our city.
        </p>
      </div>

      <div className="about-section">
        <h2>What We Do</h2>
        <div className="services-grid">
          <div className="service-card">
            <h3>Veterinary Care</h3>
            <p>
              We provide free veterinary services to bodega cats, including vaccinations,
              spay/neuter procedures, and treatment for injuries or illnesses.
            </p>
          </div>

          <div className="service-card">
            <h3>Food & Supplies</h3>
            <p>
              We deliver high-quality cat food and essential supplies to participating
              bodegas to ensure the cats are well-fed and comfortable.
            </p>
          </div>

          <div className="service-card">
            <h3>Education</h3>
            <p>
              We educate store owners and the public about the vital role these cats play in urban life
              and proper care techniques.
            </p>
          </div>

          <div className="service-card">
            <h3>Adoption Services</h3>
            <p>
              When a bodega cat is ready for retirement or a store closes, we help find
              loving forever homes through our adoption network.
            </p>
          </div>
        </div>
      </div>

      <div className="about-section">
        <h2>Join the Movement</h2>
        <div className="help-options">
          <div className="help-option">
            <h3>Donate</h3>
            <p>
              Your financial contributions help us provide medical care, food, and supplies
              to bodega cats in need.
            </p>
            <button className="help-button">Make a Donation</button>
          </div>

          <div className="help-option">
            <h3>Volunteer</h3>
            <p>
              We're always looking for volunteers to help with cat care, transportation,
              adoption events, and more.
            </p>
            <button className="help-button">Join Our Team</button>
          </div>

          <div className="help-option">
            <h3>Adopt</h3>
            <p>
              Consider adopting a retired bodega cat and giving them a comfortable home
              for their golden years.
            </p>
            <button className="help-button">View Available Cats</button>
          </div>
        </div>

        <p className="closing-message">
          Bodega cats aren't just store mascots—they're cherished members of our neighborhoods.
          With your help, we can ensure they receive the love and care they deserve, keeping the
          unique spirit of New York alive, one purr at a time.
        </p>
      </div>

      <div className="about-section contact-section">
        <h2>Contact Us</h2>
        <p>
          Questions? Want to get involved? Reach out—let's make NYC a better place for bodega cats
          and the communities they serve!
        </p>
        <div className="contact-info">
          <div className="contact-item">
            <strong>Email:</strong> info@bodegacat.org
          </div>
          <div className="contact-item">
            <strong>Phone:</strong> (555) 123-4567
          </div>
          <div className="contact-item">
            <strong>Address:</strong> 123 Cat Street, New York, NY 10001
          </div>
        </div>
      </div>
    </div>
  );
};

export default About;
