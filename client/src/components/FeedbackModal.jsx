import React, { useState, useEffect, useRef } from 'react';
import { useTranslation } from 'react-i18next';
import './FeedbackModal.css';

const FeedbackModal = ({ isOpen, onClose }) => {
  const { t } = useTranslation();
  const [formData, setFormData] = useState({
    type: 'suggestion',
    message: '',
    email: ''
  });
  const [submitStatus, setSubmitStatus] = useState(null);
  const [isSubmitting, setIsSubmitting] = useState(false);
  const modalRef = useRef(null);
  
  useEffect(() => {
    const handleClickOutside = (event) => {
      if (modalRef.current && !modalRef.current.contains(event.target)) {
        handleClose();
      }
    };
    
    if (isOpen) {
      document.addEventListener('mousedown', handleClickOutside);
      // Prevent scrolling of body when modal is open
      document.body.style.overflow = 'hidden';
    }
    
    return () => {
      document.removeEventListener('mousedown', handleClickOutside);
      document.body.style.overflow = 'auto';
    };
  }, [isOpen]);
  
  const handleClose = () => {
    // Only close if not submitting
    if (!isSubmitting) {
      onClose();
      // Reset form after closing
      setTimeout(() => {
        setFormData({
          type: 'suggestion',
          message: '',
          email: ''
        });
        setSubmitStatus(null);
      }, 300);
    }
  };
  
  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value
    }));
  };
  
  const handleSubmit = (e) => {
    e.preventDefault();
    setIsSubmitting(true);
    
    // Simulate API call
    setTimeout(() => {
      // 90% chance of success
      const success = Math.random() < 0.9;
      
      if (success) {
        setSubmitStatus('success');
        // Auto close on success after delay
        setTimeout(() => {
          handleClose();
        }, 2000);
      } else {
        setSubmitStatus('error');
      }
      
      setIsSubmitting(false);
    }, 1500);
  };
  
  if (!isOpen) return null;
  
  return (
    <div className="feedback-modal-container">
      <div className="feedback-modal-backdrop" aria-hidden="true" onClick={handleClose}></div>
      <div 
        className="feedback-modal" 
        ref={modalRef}
        role="dialog"
        aria-labelledby="feedback-title"
        aria-modal="true"
      >
        <div className="modal-header">
          <h3 id="feedback-title">{t('feedback.title')}</h3>
          <button 
            className="close-modal" 
            onClick={handleClose}
            aria-label={t('feedback.close')}
            disabled={isSubmitting}
          >
            ×
          </button>
        </div>
        
        {submitStatus === 'success' ? (
          <div className="feedback-success">
            <div className="success-icon">✓</div>
            <p>{t('feedback.thankYou')}</p>
          </div>
        ) : (
          <form className="feedback-form" onSubmit={handleSubmit}>
            <div className="form-group">
              <label htmlFor="feedback-type">{t('feedback.type')}</label>
              <select 
                id="feedback-type" 
                name="type" 
                value={formData.type}
                onChange={handleChange}
                disabled={isSubmitting}
              >
                <option value="suggestion">{t('feedback.suggestion')}</option>
                <option value="bug">{t('feedback.bug')}</option>
                <option value="question">{t('feedback.question')}</option>
                <option value="other">{t('feedback.other')}</option>
              </select>
            </div>
            
            <div className="form-group">
              <label htmlFor="feedback-message">{t('feedback.message')}</label>
              <textarea 
                id="feedback-message" 
                name="message" 
                rows="4" 
                placeholder={t('feedback.placeholder')}
                value={formData.message}
                onChange={handleChange}
                required
                disabled={isSubmitting}
              ></textarea>
            </div>
            
            <div className="form-group">
              <label htmlFor="feedback-email" className="optional-label">
                Email (Optional)
              </label>
              <input
                type="email"
                id="feedback-email"
                name="email"
                placeholder="your@email.com"
                value={formData.email}
                onChange={handleChange}
                disabled={isSubmitting}
              />
              <small>We'll only use this to follow up if needed</small>
            </div>
            
            {submitStatus === 'error' && (
              <div className="error-message">
                {t('feedback.error')}
              </div>
            )}
            
            <div className="form-actions">
              <button 
                type="button" 
                className="cancel-btn"
                onClick={handleClose}
                disabled={isSubmitting}
              >
                {t('feedback.cancel')}
              </button>
              <button 
                type="submit" 
                className={`submit-btn ${isSubmitting ? 'loading' : ''}`}
                disabled={isSubmitting || !formData.message.trim()}
              >
                {isSubmitting ? (
                  <span className="loading-spinner"></span>
                ) : (
                  t('feedback.submit')
                )}
              </button>
            </div>
          </form>
        )}
      </div>
    </div>
  );
};

export default FeedbackModal;