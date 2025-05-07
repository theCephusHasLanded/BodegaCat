import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { AuthProvider } from './context/AuthContext';
import ProtectedRoute from './components/ProtectedRoute';
import Navbar from './components/Navbar';
import Footer from './components/Footer';
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

function App() {
  return (
    <Router>
      <AuthProvider>
        <div className="app">
          <Navbar />
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
        </div>
      </AuthProvider>
    </Router>
  );
}

export default App;
