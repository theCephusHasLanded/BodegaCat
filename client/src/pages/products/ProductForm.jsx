import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import ProductService from '../../services/product.service';
import { useAuth } from '../../context/AuthContext';
import LoadingSpinner from '../../components/LoadingSpinner';
import './ProductForm.css';

const initialFormState = {
  name: '',
  description: '',
  price: '',
  imageUrl: '',
  stockQuantity: '',
  snapEligible: false,
  benefitsCategory: '',
  discountPrograms: []
};

const ProductForm = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const { currentUser } = useAuth();
  const isAdmin = currentUser?.role === 'ADMIN';
  
  const [formData, setFormData] = useState(initialFormState);
  const [errors, setErrors] = useState({});
  const [loading, setLoading] = useState(false);
  const [saving, setSaving] = useState(false);
  const [imageFile, setImageFile] = useState(null);
  const [imagePreview, setImagePreview] = useState('');
  const [availableDiscountPrograms, setAvailableDiscountPrograms] = useState([]);
  
  const isEditMode = !!id;
  
  useEffect(() => {
    if (!isAdmin) {
      navigate('/products');
      return;
    }
    
    if (isEditMode) {
      loadProduct();
    }
    
    // Load available discount programs
    // This is a mock since we don't have the actual endpoint yet
    setAvailableDiscountPrograms([
      { code: 'SENIOR', name: 'Senior Discount' },
      { code: 'STUDENT', name: 'Student Discount' },
      { code: 'VETERAN', name: 'Veteran Discount' },
      { code: 'LOWINCOME', name: 'Low Income Assistance' }
    ]);
  }, [id, isAdmin]);
  
  const loadProduct = async () => {
    try {
      setLoading(true);
      const response = await ProductService.getProductById(id);
      const productData = response.data;
      
      setFormData({
        name: productData.name || '',
        description: productData.description || '',
        price: productData.price?.toString() || '',
        imageUrl: productData.imageUrl || '',
        stockQuantity: productData.stockQuantity?.toString() || '',
        snapEligible: productData.snapEligible || false,
        benefitsCategory: productData.benefitsCategory || '',
        discountPrograms: productData.discountPrograms || []
      });
      
      if (productData.imageUrl) {
        setImagePreview(productData.imageUrl);
      }
    } catch (err) {
      console.error('Error loading product:', err);
      setErrors({ general: 'Failed to load product. Please try again.' });
    } finally {
      setLoading(false);
    }
  };
  
  const handleChange = (e) => {
    const { name, value, type, checked } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: type === 'checkbox' ? checked : value
    }));
    
    // Clear field error when user starts typing
    if (errors[name]) {
      setErrors(prev => ({ ...prev, [name]: undefined }));
    }
  };
  
  const handleDiscountProgramChange = (e) => {
    const programCode = e.target.value;
    const isChecked = e.target.checked;
    
    setFormData(prev => {
      if (isChecked) {
        return {
          ...prev,
          discountPrograms: [...prev.discountPrograms, programCode]
        };
      } else {
        return {
          ...prev,
          discountPrograms: prev.discountPrograms.filter(code => code !== programCode)
        };
      }
    });
  };
  
  const handleImageChange = (e) => {
    const file = e.target.files[0];
    if (file) {
      setImageFile(file);
      const previewUrl = URL.createObjectURL(file);
      setImagePreview(previewUrl);
      
      setFormData(prev => ({
        ...prev,
        imageUrl: 'pending-upload' // Placeholder until upload completes
      }));
    }
  };
  
  const validateForm = () => {
    const newErrors = {};
    
    if (!formData.name.trim()) {
      newErrors.name = 'Product name is required';
    }
    
    if (!formData.description.trim()) {
      newErrors.description = 'Description is required';
    }
    
    if (!formData.price.trim()) {
      newErrors.price = 'Price is required';
    } else if (isNaN(parseFloat(formData.price)) || parseFloat(formData.price) <= 0) {
      newErrors.price = 'Price must be a positive number';
    }
    
    if (!formData.stockQuantity.trim()) {
      newErrors.stockQuantity = 'Stock quantity is required';
    } else if (
      isNaN(parseInt(formData.stockQuantity)) || 
      parseInt(formData.stockQuantity) < 0 ||
      !Number.isInteger(parseFloat(formData.stockQuantity))
    ) {
      newErrors.stockQuantity = 'Stock quantity must be a non-negative integer';
    }
    
    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };
  
  const uploadImage = async () => {
    if (!imageFile) return null;
    
    try {
      const response = await ProductService.uploadProductImage(imageFile);
      return response.data.imageUrl; // Assuming the API returns the image URL
    } catch (err) {
      console.error('Error uploading image:', err);
      throw new Error('Failed to upload product image');
    }
  };
  
  const handleSubmit = async (e) => {
    e.preventDefault();
    
    if (!validateForm()) {
      return;
    }
    
    try {
      setSaving(true);
      
      // Upload image if a new one was selected
      let finalImageUrl = formData.imageUrl;
      if (imageFile) {
        finalImageUrl = await uploadImage();
      }
      
      // Prepare product data
      const productData = {
        ...formData,
        price: parseFloat(formData.price),
        stockQuantity: parseInt(formData.stockQuantity),
        imageUrl: finalImageUrl === 'pending-upload' ? '' : finalImageUrl
      };
      
      if (isEditMode) {
        await ProductService.updateProduct(id, productData);
      } else {
        await ProductService.createProduct(productData);
      }
      
      navigate('/products');
    } catch (err) {
      console.error('Error saving product:', err);
      setErrors({ general: 'Failed to save product. Please try again.' });
    } finally {
      setSaving(false);
    }
  };
  
  if (loading) {
    return <LoadingSpinner />;
  }
  
  return (
    <div className="product-form-container">
      <h1>{isEditMode ? 'Edit Product' : 'Create New Product'}</h1>
      
      {errors.general && (
        <div className="error-message general-error">{errors.general}</div>
      )}
      
      <form onSubmit={handleSubmit} className="product-form">
        <div className="form-section">
          <h2>Basic Information</h2>
          
          <div className="form-group">
            <label htmlFor="name">Product Name *</label>
            <input
              type="text"
              id="name"
              name="name"
              value={formData.name}
              onChange={handleChange}
              className={errors.name ? 'error' : ''}
            />
            {errors.name && <div className="error-text">{errors.name}</div>}
          </div>
          
          <div className="form-group">
            <label htmlFor="description">Description *</label>
            <textarea
              id="description"
              name="description"
              value={formData.description}
              onChange={handleChange}
              rows="5"
              className={errors.description ? 'error' : ''}
            ></textarea>
            {errors.description && <div className="error-text">{errors.description}</div>}
          </div>
          
          <div className="form-row">
            <div className="form-group">
              <label htmlFor="price">Price ($) *</label>
              <input
                type="number"
                id="price"
                name="price"
                value={formData.price}
                onChange={handleChange}
                step="0.01"
                min="0"
                className={errors.price ? 'error' : ''}
              />
              {errors.price && <div className="error-text">{errors.price}</div>}
            </div>
            
            <div className="form-group">
              <label htmlFor="stockQuantity">Stock Quantity *</label>
              <input
                type="number"
                id="stockQuantity"
                name="stockQuantity"
                value={formData.stockQuantity}
                onChange={handleChange}
                min="0"
                className={errors.stockQuantity ? 'error' : ''}
              />
              {errors.stockQuantity && <div className="error-text">{errors.stockQuantity}</div>}
            </div>
          </div>
        </div>
        
        <div className="form-section">
          <h2>Product Image</h2>
          
          <div className="image-upload-container">
            <div className="image-preview">
              {imagePreview ? (
                <img 
                  src={imagePreview} 
                  alt="Product Preview"
                  onError={() => setImagePreview('')}
                />
              ) : (
                <div className="no-image">No image selected</div>
              )}
            </div>
            
            <div className="image-upload">
              <label htmlFor="imageUpload" className="upload-btn">
                {imagePreview ? 'Change Image' : 'Upload Image'}
              </label>
              <input
                type="file"
                id="imageUpload"
                accept="image/*"
                onChange={handleImageChange}
                style={{ display: 'none' }}
              />
              <p className="upload-hint">
                Recommended: Square image, at least 500x500px
              </p>
            </div>
          </div>
        </div>
        
        <div className="form-section">
          <h2>Benefits Information</h2>
          
          <div className="form-group checkbox-group">
            <label>
              <input
                type="checkbox"
                name="snapEligible"
                checked={formData.snapEligible}
                onChange={handleChange}
              />
              SNAP Eligible
            </label>
          </div>
          
          <div className="form-group">
            <label htmlFor="benefitsCategory">Benefits Category</label>
            <select
              id="benefitsCategory"
              name="benefitsCategory"
              value={formData.benefitsCategory}
              onChange={handleChange}
            >
              <option value="">None</option>
              <option value="FOOD">Food</option>
              <option value="HOUSING">Housing</option>
              <option value="HEALTHCARE">Healthcare</option>
              <option value="TRANSPORTATION">Transportation</option>
              <option value="EDUCATION">Education</option>
              <option value="OTHER">Other</option>
            </select>
          </div>
          
          <div className="form-group">
            <label>Discount Programs</label>
            <div className="checkbox-list">
              {availableDiscountPrograms.map(program => (
                <label key={program.code}>
                  <input
                    type="checkbox"
                    value={program.code}
                    checked={formData.discountPrograms.includes(program.code)}
                    onChange={handleDiscountProgramChange}
                  />
                  {program.name}
                </label>
              ))}
            </div>
          </div>
        </div>
        
        <div className="form-actions">
          <button 
            type="button" 
            className="cancel-btn"
            onClick={() => navigate('/products')}
          >
            Cancel
          </button>
          <button 
            type="submit" 
            className="save-btn"
            disabled={saving}
          >
            {saving ? 'Saving...' : (isEditMode ? 'Update Product' : 'Create Product')}
          </button>
        </div>
      </form>
    </div>
  );
};

export default ProductForm;