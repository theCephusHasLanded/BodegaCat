import React, { useState, useEffect } from 'react';
import { useParams, Link, useNavigate } from 'react-router-dom';
import ProductService from '../../services/product.service';
import LoadingSpinner from '../../components/LoadingSpinner';
import { useAuth } from '../../context/AuthContext';
import './ProductDetail.css';

const ProductDetail = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const [product, setProduct] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [quantity, setQuantity] = useState(1);

  const { currentUser } = useAuth();
  const isAdmin = currentUser?.role === 'ADMIN';

  useEffect(() => {
    loadProduct();
  }, [id]);

  const loadProduct = async () => {
    try {
      setLoading(true);
      const response = await ProductService.getProductById(id);
      setProduct(response.data);
    } catch (err) {
      console.error('Error loading product:', err);
      setError('Failed to load product details. Please try again later.');
    } finally {
      setLoading(false);
    }
  };

  const handleQuantityChange = (e) => {
    const value = parseInt(e.target.value);
    if (value > 0 && value <= (product?.stockQuantity || 1)) {
      setQuantity(value);
    }
  };

  const incrementQuantity = () => {
    if (quantity < (product?.stockQuantity || 1)) {
      setQuantity(prev => prev + 1);
    }
  };

  const decrementQuantity = () => {
    if (quantity > 1) {
      setQuantity(prev => prev - 1);
    }
  };

  const handleAddToCart = () => {
    // This will be implemented in Week 4
    alert(`Added ${quantity} ${product.name} to cart`);
  };

  const handleDeleteProduct = async () => {
    if (window.confirm('Are you sure you want to delete this product?')) {
      try {
        await ProductService.deleteProduct(id);
        navigate('/products');
      } catch (err) {
        console.error('Error deleting product:', err);
        alert('Failed to delete product. Please try again.');
      }
    }
  };

  if (loading) {
    return <LoadingSpinner />;
  }

  if (error) {
    return <div className="error-message">{error}</div>;
  }

  if (!product) {
    return <div className="product-not-found">Product not found</div>;
  }

  return (
    <div className="product-detail-container">
      <div className="breadcrumbs">
        <Link to="/products">Products</Link> &gt; {product.name}
      </div>

      <div className="product-detail-content">
        <div className="product-detail-image">
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
              <span>No Image Available</span>
            </div>
          )}
          {product.snapEligible && (
            <div className="snap-badge">SNAP Eligible</div>
          )}
        </div>

        <div className="product-detail-info">
          <h1 className="product-title">{product.name}</h1>
          <p className="product-price">${product.price?.toFixed(2)}</p>
          
          {product.stockQuantity <= 5 && product.stockQuantity > 0 && (
            <div className="stock-info low-stock">
              Only {product.stockQuantity} left in stock!
            </div>
          )}
          
          {product.stockQuantity === 0 ? (
            <div className="stock-info out-of-stock">
              Out of Stock
            </div>
          ) : (
            <div className="stock-info in-stock">
              In Stock
            </div>
          )}

          <div className="product-description">
            <h3>Description</h3>
            <p>{product.description}</p>
          </div>

          {product.benefitsCategory && (
            <div className="benefits-info">
              <h3>Benefits Category</h3>
              <p>{product.benefitsCategory}</p>
            </div>
          )}

          {product.discountPrograms && product.discountPrograms.length > 0 && (
            <div className="discount-programs">
              <h3>Eligible for Discount Programs</h3>
              <ul>
                {product.discountPrograms.map((program, index) => (
                  <li key={index}>{program}</li>
                ))}
              </ul>
            </div>
          )}

          {product.stockQuantity > 0 && (
            <div className="quantity-selector">
              <h3>Quantity</h3>
              <div className="quantity-controls">
                <button onClick={decrementQuantity} disabled={quantity <= 1}>-</button>
                <input 
                  type="number" 
                  min="1" 
                  max={product.stockQuantity} 
                  value={quantity} 
                  onChange={handleQuantityChange}
                />
                <button onClick={incrementQuantity} disabled={quantity >= product.stockQuantity}>+</button>
              </div>
            </div>
          )}

          {product.stockQuantity > 0 && (
            <button className="add-to-cart-btn" onClick={handleAddToCart}>
              Add to Cart
            </button>
          )}

          {isAdmin && (
            <div className="admin-actions">
              <Link to={`/products/edit/${product.id}`} className="edit-btn">
                Edit Product
              </Link>
              <button onClick={handleDeleteProduct} className="delete-btn">
                Delete Product
              </button>
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default ProductDetail;