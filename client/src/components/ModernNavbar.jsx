import React, { useState, useEffect, useRef } from 'react';
import { Link, useNavigate, useLocation } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { useTranslation } from 'react-i18next';
import './ModernNavbar.css';

// Language selector component
const LanguageSelector = () => {
  const { i18n } = useTranslation();
  const [showLangMenu, setShowLangMenu] = useState(false);
  const langMenuRef = useRef(null);
  
  const languages = [
    { code: 'en', name: 'English', flag: '🇺🇸' },
    { code: 'es', name: 'Español', flag: '🇪🇸' }
  ];

  useEffect(() => {
    const handleClickOutside = (event) => {
      if (langMenuRef.current && !langMenuRef.current.contains(event.target)) {
        setShowLangMenu(false);
      }
    };
    
    document.addEventListener('mousedown', handleClickOutside);
    return () => document.removeEventListener('mousedown', handleClickOutside);
  }, []);

  const changeLanguage = (langCode) => {
    i18n.changeLanguage(langCode);
    setShowLangMenu(false);
    localStorage.setItem('preferredLanguage', langCode);
  };

  const currentLang = languages.find(lang => lang.code === i18n.language) || languages[0];

  return (
    <div className="language-selector" ref={langMenuRef}>
      <button 
        className="lang-button" 
        onClick={() => setShowLangMenu(!showLangMenu)}
        aria-label="Select language"
      >
        <span className="lang-flag">{currentLang.flag}</span>
        <span className="lang-code">{currentLang.code.toUpperCase()}</span>
      </button>
      {showLangMenu && (
        <div className="lang-dropdown">
          {languages.map((lang) => (
            <button
              key={lang.code}
              className={`lang-option ${i18n.language === lang.code ? 'active' : ''}`}
              onClick={() => changeLanguage(lang.code)}
            >
              <span className="lang-flag">{lang.flag}</span>
              <span className="lang-name">{lang.name}</span>
            </button>
          ))}
        </div>
      )}
    </div>
  );
};

const ModernNavbar = ({ 
  onChatbotToggle, 
  onFeedbackToggle,
  isChatbotOpen = false,
  isFeedbackOpen = false
}) => {
  const { t } = useTranslation();
  const { currentUser, logout } = useAuth();
  const navigate = useNavigate();
  const location = useLocation();
  const [isMenuOpen, setIsMenuOpen] = useState(false);
  const [isScrolled, setIsScrolled] = useState(false);
  const [isDarkMode, setIsDarkMode] = useState(
    localStorage.getItem('darkMode') === 'true' || 
    window.matchMedia('(prefers-color-scheme: dark)').matches
  );
  
  // Handle scroll events for context-aware header
  useEffect(() => {
    const handleScroll = () => {
      setIsScrolled(window.scrollY > 20);
    };
    
    window.addEventListener('scroll', handleScroll);
    return () => window.removeEventListener('scroll', handleScroll);
  }, []);

  // Apply dark mode
  useEffect(() => {
    if (isDarkMode) {
      document.body.classList.add('dark-mode');
    } else {
      document.body.classList.remove('dark-mode');
    }
    localStorage.setItem('darkMode', isDarkMode);
  }, [isDarkMode]);

  const toggleMenu = () => {
    setIsMenuOpen(!isMenuOpen);
  };

  const toggleTheme = () => {
    setIsDarkMode(!isDarkMode);
  };

  const handleLogout = () => {
    logout();
    navigate('/');
    setIsMenuOpen(false);
  };

  // Generate navigation items with active state tracking
  const navItems = [
    { path: '/', label: t('navbar.home') },
    { path: '/about', label: t('navbar.about') },
    { path: '/bodegas', label: t('navbar.bodegas') },
    { path: '/fundraising', label: t('navbar.fundraising') },
    { path: '/membership', label: t('navbar.membership') },
  ];

  const authenticatedItems = [
    { path: '/cats', label: t('navbar.cats') },
    { path: '/passport', label: t('navbar.passport') },
    { path: '/profile', label: t('navbar.profile') },
  ];

  // Close menu when clicking outside
  useEffect(() => {
    const handleClickOutside = (event) => {
      if (isMenuOpen && !event.target.closest('.navbar-container') && !event.target.closest('.menu-icon')) {
        setIsMenuOpen(false);
      }
    };
    
    document.addEventListener('mousedown', handleClickOutside);
    return () => document.removeEventListener('mousedown', handleClickOutside);
  }, [isMenuOpen]);

  // Close menu when route changes
  useEffect(() => {
    setIsMenuOpen(false);
  }, [location]);

  return (
    <>
      <nav className={`modern-navbar ${isScrolled ? 'scrolled' : ''}`}>
        <div className="navbar-container">
          <Link to="/" className="navbar-logo" aria-label="BodegaCat Home">
            <div className="logo-icon">🐱</div>
            <span className="logo-text">BodegaCat</span>
          </Link>

          <div className="desktop-nav">
            {navItems.map((item) => (
              <Link 
                key={item.path} 
                to={item.path}
                className={`nav-link ${location.pathname === item.path ? 'active' : ''}`}
              >
                {item.label}
                <span className="nav-indicator"></span>
              </Link>
            ))}
            
            {currentUser && authenticatedItems.map((item) => (
              <Link 
                key={item.path} 
                to={item.path}
                className={`nav-link ${location.pathname === item.path ? 'active' : ''}`}
              >
                {item.label}
                <span className="nav-indicator"></span>
              </Link>
            ))}
          </div>

          <div className="navbar-controls">
            <LanguageSelector />
            
            <button 
              className="theme-toggle" 
              onClick={toggleTheme} 
              aria-label={isDarkMode ? t('navbar.lightMode') : t('navbar.darkMode')}
            >
              {isDarkMode ? '☀️' : '🌙'}
            </button>
            
            <button 
              className={`feedback-button ${isFeedbackOpen ? 'active' : ''}`} 
              onClick={() => onFeedbackToggle(!isFeedbackOpen)}
              aria-label={t('navbar.feedback')}
            >
              <span role="img" aria-hidden="true">💬</span>
            </button>
            
            <button 
              className={`chatbot-button ${isChatbotOpen ? 'active' : ''}`} 
              onClick={() => onChatbotToggle(!isChatbotOpen)}
              aria-label={t('navbar.chatbot')}
            >
              <span role="img" aria-hidden="true">🤖</span>
            </button>

            {currentUser ? (
              <div className="user-controls">
                <div className="user-avatar">
                  {currentUser.username?.charAt(0).toUpperCase() || '👤'}
                </div>
                <div className="dropdown-menu">
                  <Link to="/profile" className="dropdown-item">{t('navbar.profile')}</Link>
                  <button onClick={handleLogout} className="dropdown-item logout">
                    {t('navbar.logout')}
                  </button>
                </div>
              </div>
            ) : (
              <div className="auth-buttons">
                <Link to="/login" className="auth-button login">
                  {t('navbar.login')}
                </Link>
                <Link to="/register" className="auth-button register">
                  {t('navbar.register')}
                </Link>
              </div>
            )}

            <button 
              className={`menu-icon ${isMenuOpen ? 'active' : ''}`} 
              onClick={toggleMenu}
              aria-expanded={isMenuOpen}
              aria-label={isMenuOpen ? t('navbar.closeMenu') : t('navbar.openMenu')}
            >
              <span className="bar"></span>
              <span className="bar"></span>
              <span className="bar"></span>
            </button>
          </div>
        </div>

        <div className={`mobile-nav ${isMenuOpen ? 'active' : ''}`}>
          {navItems.map((item) => (
            <Link 
              key={item.path} 
              to={item.path}
              className={`mobile-nav-link ${location.pathname === item.path ? 'active' : ''}`}
            >
              {item.label}
            </Link>
          ))}
          
          {currentUser ? (
            <>
              {authenticatedItems.map((item) => (
                <Link 
                  key={item.path} 
                  to={item.path}
                  className={`mobile-nav-link ${location.pathname === item.path ? 'active' : ''}`}
                >
                  {item.label}
                </Link>
              ))}
              <button onClick={handleLogout} className="mobile-nav-link logout">
                {t('navbar.logout')}
              </button>
            </>
          ) : (
            <>
              <Link to="/login" className="mobile-nav-link">
                {t('navbar.login')}
              </Link>
              <Link to="/register" className="mobile-nav-link register-mobile">
                {t('navbar.register')}
              </Link>
            </>
          )}
        </div>
      </nav>

    </>
  );
};

export default ModernNavbar;