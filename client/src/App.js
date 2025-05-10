import React, { useState, useEffect } from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { AuthProvider } from './context/AuthContext';
import ProtectedRoute from './components/ProtectedRoute';
import ModernNavbar from './components/ModernNavbar';
import Footer from './components/Footer';
import MatrixLoading from './components/MatrixLoading';
import LucyChatbot from './components/LucyChatbot';
import FeedbackModal from './components/FeedbackModal';
import Home from './pages/home/Home';
import About from './pages/about/About';
import Login from './pages/auth/Login';
import Register from './pages/auth/Register';
import Profile from './pages/profile/Profile';
import Cats from './pages/cats/Cats';
import Membership from './pages/membership/Membership';
import Passport from './pages/passport/Passport';
import Fundraising from './pages/fundraising/Fundraising';
import BodegaExplorer from './pages/bodegas/BodegaExplorer';
import './App.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import './i18n/i18n'; // Import i18n configuration

function App() {
  const [loading, setLoading] = useState(true);
  const [showChatbot, setShowChatbot] = useState(false);
  const [showFeedbackModal, setShowFeedbackModal] = useState(false);
  
  useEffect(() => {
    // Simulate loading time
    const timer = setTimeout(() => {
      setLoading(false);
    }, 2500);
    
    return () => clearTimeout(timer);
  }, []);
  
  // Handler for toggling the chatbot
  const handleChatbotToggle = (isVisible) => {
    setShowChatbot(isVisible);
    // Close feedback modal if chatbot is opened
    if (isVisible) {
      setShowFeedbackModal(false);
    }
  };
  
  // Handler for toggling the feedback modal
  const handleFeedbackToggle = (isVisible) => {
    setShowFeedbackModal(isVisible);
    // Close chatbot if feedback modal is opened
    if (isVisible) {
      setShowChatbot(false);
    }
  };
  
  return (
    <Router>
      <AuthProvider>
        {loading ? (
          <MatrixLoading message="Connecting to NYC Bodega Network..." />
        ) : (
          <div className="app">
            <ModernNavbar 
              onChatbotToggle={handleChatbotToggle}
              onFeedbackToggle={handleFeedbackToggle}
              isChatbotOpen={showChatbot}
              isFeedbackOpen={showFeedbackModal}
            />
            <main className="main-content">
              <Routes>
                <Route path="/" element={<Home />} />
                <Route path="/about" element={<About />} />
                <Route path="/login" element={<Login />} />
                <Route path="/register" element={<Register />} />
                <Route
                  path="/profile"
                  element={
                    <ProtectedRoute>
                      <Profile />
                    </ProtectedRoute>
                  }
                />
                <Route
                  path="/cats"
                  element={
                    <ProtectedRoute>
                      <Cats />
                    </ProtectedRoute>
                  }
                />
                <Route path="/membership" element={<Membership />} />
                <Route path="/passport" element={<Passport />} />
                <Route path="/fundraising" element={<Fundraising />} />
                <Route path="/bodegas" element={<BodegaExplorer />} />
                <Route path="*" element={<Navigate to="/" />} />
              </Routes>
            </main>
            <Footer />
            
            {/* Lucy Chatbot */}
            <LucyChatbot 
              isOpen={showChatbot} 
              onClose={() => setShowChatbot(false)} 
            />
            
            {/* Feedback Modal */}
            <FeedbackModal 
              isOpen={showFeedbackModal} 
              onClose={() => setShowFeedbackModal(false)}
            />
          </div>
        )}
      </AuthProvider>
    </Router>
  );
}

export default App;
