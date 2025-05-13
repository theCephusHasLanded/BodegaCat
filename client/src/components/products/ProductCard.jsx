import React from 'react';
import { Link } from 'react-router-dom';
import './ProductCard.css';

const ProductCard = ({ product }) => {
  return (
    <div className="product-card">
      <div className="product-image">
        {product.imageUrl ? (
          <img 
            src={product.imageUrl} 
            alt={product.name} 
            onError={(e) => {
              e.target.onerror = null;
              e.target.src = '/placeholder-product.jpg';
            }}
          />
        ) : (
          <div className="placeholder-image">
            <span>No Image</span>
          </div>
        )}
        {product.snapEligible && (
          <div className="snap-badge">SNAP Eligible</div>
        )}
      </div>
      
      <div className="product-info">
        <h3 className="product-name">{product.name}</h3>
        <p className="product-price">${product.price?.toFixed(2)}</p>
        
        <div className="product-description">
          {product.description?.length > 100 
            ? `${product.description.substring(0, 100)}...` 
            : product.description}
        </div>
        
        {product.stockQuantity <= 5 && product.stockQuantity > 0 && (
          <div className="low-stock-warning">
            Only {product.stockQuantity} left!
          </div>
        )}
        
        {product.stockQuantity === 0 && (
          <div className="out-of-stock">
            Out of Stock
          </div>
        )}
      </div>
      
      <div className="product-actions">
        <Link to={`/products/${product.id}`} className="view-details-btn">
          View Details
        </Link>
        {product.stockQuantity > 0 && (
          <button className="add-to-cart-btn">
            Add to Cart
          </button>
        )}
      </div>
    </div>
  );
};

export default ProductCard;