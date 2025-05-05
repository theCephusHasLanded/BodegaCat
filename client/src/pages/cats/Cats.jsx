import React, { useState, useEffect } from 'react';
import { useAuth } from '../../context/AuthContext';
import './Cats.css';

const Cats = () => {
  const { currentUser } = useAuth();
  const [cats, setCats] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    // This would be replaced with an actual API call in a real application
    const fetchCats = async () => {
      try {
        setLoading(true);
        // Simulate API call
        setTimeout(() => {
          const mockCats = [
            {
              id: 1,
              name: 'Oliver',
              store: 'Corner Deli',
              neighborhood: 'East Village',
              description: 'Orange tabby who loves to nap on the newspaper stack',
              imageUrl: 'https://placekitten.com/300/200?image=1'
            },
            {
              id: 2,
              name: 'Luna',
              store: 'Sunshine Market',
              neighborhood: 'Williamsburg',
              description: 'Black cat with green eyes, guards the snack aisle',
              imageUrl: 'https://placekitten.com/300/200?image=2'
            },
            {
              id: 3,
              name: 'Max',
              store: '7th Ave Grocery',
              neighborhood: 'Park Slope',
              description: 'Gray and white, friendly to all customers',
              imageUrl: 'https://placekitten.com/300/200?image=3'
            }
          ];
          setCats(mockCats);
          setLoading(false);
        }, 1000);
      } catch (err) {
        setError('Failed to fetch cats. Please try again later.');
        setLoading(false);
      }
    };

    fetchCats();
  }, []);

  if (loading) {
    return (
      <div className="cats-container">
        <div className="loading">Loading cats...</div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="cats-container">
        <div className="error">{error}</div>
      </div>
    );
  }

  return (
    <div className="cats-container">
      <h1>Bodega Cats of NYC</h1>
      <p className="cats-intro">
        Meet the feline friends who keep watch over New York City's corner stores.
      </p>

      <div className="cats-grid">
        {cats.map(cat => (
          <div key={cat.id} className="cat-card">
            <img src={cat.imageUrl} alt={cat.name} className="cat-image" />
            <div className="cat-info">
              <h3>{cat.name}</h3>
              <p className="cat-location">{cat.store} • {cat.neighborhood}</p>
              <p className="cat-description">{cat.description}</p>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
};

export default Cats;
