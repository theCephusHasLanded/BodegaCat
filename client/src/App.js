import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import { AuthProvider } from './context/AuthContext';
import Navbar from './components/Navbar';
import Login from './pages/auth/Login';
import Register from './pages/auth/Register';
import ProtectedRoute from './components/ProtectedRoute';
import './App.css';

// Placeholder components for now
const Home = () => <div className="container"><h1>Welcome to Bodega Cat</h1></div>;
const Profile = () => <div className="container"><h1>User Profile</h1></div>;
const Cats = () => <div className="container"><h1>Bodega Cats</h1></div>;
const About = () => <div className="container"><h1>About Us</h1></div>;

function App() {
  return (
    <AuthProvider>
      <Router>
        <div className="app">
          <Navbar />
          <main>
            <Routes>
              <Route path="/" element={<Home />} />
              <Route path="/login" element={<Login />} />
              <Route path="/register" element={<Register />} />
              <Route path="/cats" element={<Cats />} />
              <Route path="/about" element={<About />} />
              <Route path="/profile" element={
                <ProtectedRoute>
                  <Profile />
                </ProtectedRoute>
              } />
            </Routes>
          </main>
        </div>
      </Router>
    </AuthProvider>
  );
}

export default App;
