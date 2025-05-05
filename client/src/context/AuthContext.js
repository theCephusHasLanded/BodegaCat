import React, { createContext, useState, useContext, useEffect } from 'react';
import axios from 'axios';

const AuthContext = createContext();

export function useAuth() {
  return useContext(AuthContext);
}

export function AuthProvider({ children }) {
  const [currentUser, setCurrentUser] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    // Check if user is already logged in (token exists in localStorage)
    const token = localStorage.getItem('token');
    if (token) {
      const userData = JSON.parse(localStorage.getItem('user'));
      setCurrentUser(userData);
    }
    setLoading(false);
  }, []);

  // Register a new user
  const register = async (username, email, password, firstName, lastName) => {
    try {
      setError('');
      const response = await axios.post('/api/auth/register', {
        username,
        email,
        password,
        firstName,
        lastName
      });
      return response.data;
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to register');
      throw err;
    }
  };

  // Login user
  const login = async (username, password) => {
    try {
      setError('');
      const response = await axios.post('/api/auth/login', {
        username,
        password
      });

      const { token, id, username: user, email, role } = response.data;

      // Store user data and token in localStorage
      const userData = { id, username: user, email, role };
      localStorage.setItem('token', token);
      localStorage.setItem('user', JSON.stringify(userData));

      // Set axios default headers for future requests
      axios.defaults.headers.common['Authorization'] = `Bearer ${token}`;

      setCurrentUser(userData);
      return response.data;
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to login');
      throw err;
    }
  };

  // Logout user
  const logout = () => {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
    delete axios.defaults.headers.common['Authorization'];
    setCurrentUser(null);
  };

  // Get current user profile
  const getCurrentUser = async () => {
    try {
      const response = await axios.get('/api/users/me');
      return response.data;
    } catch (err) {
      if (err.response?.status === 401) {
        // Token expired or invalid, logout user
        logout();
      }
      throw err;
    }
  };

  const value = {
    currentUser,
    loading,
    error,
    register,
    login,
    logout,
    getCurrentUser
  };

  return (
    <AuthContext.Provider value={value}>
      {!loading && children}
    </AuthContext.Provider>
  );
}
