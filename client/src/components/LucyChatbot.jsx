import React, { useState, useEffect, useRef } from 'react';
import { useTranslation } from 'react-i18next';
import { useAuth } from '../context/AuthContext';
import './LucyChatbot.css';

const LucyChatbot = ({ isOpen, onClose }) => {
  const { t } = useTranslation();
  const { currentUser } = useAuth();
  const [messages, setMessages] = useState([]);
  const [inputValue, setInputValue] = useState('');
  const [isTyping, setIsTyping] = useState(false);
  const messagesEndRef = useRef(null);
  const inputRef = useRef(null);
  
  // Predefined suggestions
  const suggestions = [
    { id: 'findBodega', text: t('chatbot.suggestions.findBodega') },
    { id: 'catOfTheDay', text: t('chatbot.suggestions.catOfTheDay') },
    { id: 'fundraiser', text: t('chatbot.suggestions.fundraiser') },
    { id: 'passport', text: t('chatbot.suggestions.passport') }
  ];

  // Initial greeting messages
  useEffect(() => {
    if (isOpen) {
      const username = currentUser?.username || t('chatbot.guest');
      
      // Clear messages when reopening the chat
      setMessages([
        { 
          id: Date.now(), 
          text: t('chatbot.greeting', { username }), 
          sender: 'bot',
          timestamp: new Date()
        },
        { 
          id: Date.now() + 1, 
          text: t('chatbot.help'), 
          sender: 'bot',
          timestamp: new Date()
        }
      ]);
      
      // Focus on input field when chat opens
      setTimeout(() => {
        inputRef.current?.focus();
      }, 300);
    }
  }, [isOpen, currentUser, t]);

  // Auto-scroll to the bottom when new messages are added
  useEffect(() => {
    messagesEndRef.current?.scrollIntoView({ behavior: 'smooth' });
  }, [messages]);

  // Get response based on user input
  const getBotResponse = (userMessage) => {
    const normalizedMessage = userMessage.toLowerCase();
    
    // Check for predefined responses
    if (normalizedMessage.includes('hello') || normalizedMessage.includes('hi')) {
      return t('chatbot.responses.greeting');
    }
    
    if (normalizedMessage.includes('bodega') && 
       (normalizedMessage.includes('find') || normalizedMessage.includes('near'))) {
      return t('chatbot.responses.findBodega');
    }
    
    if (normalizedMessage.includes('cat') && 
       (normalizedMessage.includes('day') || normalizedMessage.includes('featured'))) {
      return t('chatbot.responses.catOfTheDay');
    }
    
    if (normalizedMessage.includes('donate') || normalizedMessage.includes('fundraiser')) {
      return t('chatbot.responses.fundraiser');
    }
    
    if (normalizedMessage.includes('passport') || normalizedMessage.includes('reward')) {
      return t('chatbot.responses.passport');
    }
    
    // Fallback response if no match is found
    return t('chatbot.fallback');
  };

  // Handle form submission
  const handleSubmit = (e) => {
    e.preventDefault();
    
    if (!inputValue.trim()) return;
    
    // Add user message
    const userMessage = {
      id: Date.now(),
      text: inputValue.trim(),
      sender: 'user',
      timestamp: new Date()
    };
    
    setMessages(prevMessages => [...prevMessages, userMessage]);
    setInputValue('');
    
    // Simulate bot typing
    setIsTyping(true);
    
    // Simulate response delay (500-1500ms)
    setTimeout(() => {
      const botResponse = {
        id: Date.now() + 1,
        text: getBotResponse(inputValue),
        sender: 'bot',
        timestamp: new Date()
      };
      
      setMessages(prevMessages => [...prevMessages, botResponse]);
      setIsTyping(false);
    }, Math.floor(Math.random() * 1000) + 500);
  };
  
  // Handle suggestion click
  const handleSuggestionClick = (suggestionId) => {
    const suggestion = suggestions.find(s => s.id === suggestionId);
    if (suggestion) {
      setInputValue(suggestion.text);
      
      // Add user message
      const userMessage = {
        id: Date.now(),
        text: suggestion.text,
        sender: 'user',
        timestamp: new Date()
      };
      
      setMessages(prevMessages => [...prevMessages, userMessage]);
      
      // Simulate bot typing
      setIsTyping(true);
      
      // Simulate response delay (500-1500ms)
      setTimeout(() => {
        const botResponse = {
          id: Date.now() + 1,
          text: t(`chatbot.responses.${suggestionId}`),
          sender: 'bot',
          timestamp: new Date()
        };
        
        setMessages(prevMessages => [...prevMessages, botResponse]);
        setIsTyping(false);
        setInputValue('');
      }, Math.floor(Math.random() * 1000) + 500);
    }
  };

  // Format timestamp
  const formatTime = (date) => {
    return date.toLocaleTimeString([], {
      hour: '2-digit',
      minute: '2-digit'
    });
  };

  if (!isOpen) return null;

  return (
    <div className="lucy-chatbot" dir={document.documentElement.dir}>
      <div className="chatbot-header">
        <div className="chatbot-header-info">
          <div className="chatbot-avatar">🤖</div>
          <h3>Lucy</h3>
        </div>
        <button 
          className="close-chatbot" 
          onClick={onClose}
          aria-label={t('chatbot.close')}
        >
          ×
        </button>
      </div>
      
      <div className="chatbot-messages">
        {messages.map((message) => (
          <div 
            key={message.id} 
            className={`message ${message.sender === 'bot' ? 'bot-message' : 'user-message'}`}
          >
            <div className="message-content">{message.text}</div>
            <div className="message-time">{formatTime(message.timestamp)}</div>
          </div>
        ))}
        
        {isTyping && (
          <div className="message bot-message typing">
            <div className="typing-indicator">
              <span></span>
              <span></span>
              <span></span>
            </div>
          </div>
        )}
        
        <div ref={messagesEndRef} />
      </div>
      
      {messages.length <= 2 && (
        <div className="chatbot-suggestions">
          {suggestions.map((suggestion) => (
            <button
              key={suggestion.id}
              className="suggestion-chip"
              onClick={() => handleSuggestionClick(suggestion.id)}
            >
              {suggestion.text}
            </button>
          ))}
        </div>
      )}
      
      <form className="chatbot-input" onSubmit={handleSubmit}>
        <input 
          type="text" 
          value={inputValue}
          onChange={(e) => setInputValue(e.target.value)}
          placeholder={t('chatbot.placeholder')} 
          aria-label={t('chatbot.inputLabel')}
          ref={inputRef}
        />
        <button 
          type="submit" 
          aria-label={t('chatbot.send')}
          disabled={!inputValue.trim()}
        >
          <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
            <path d="M22 2L11 13" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"/>
            <path d="M22 2L15 22L11 13L2 9L22 2Z" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"/>
          </svg>
        </button>
      </form>
    </div>
  );
};

export default LucyChatbot;