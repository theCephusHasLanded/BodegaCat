import React, { useEffect, useState } from 'react';

const MatrixLoading = ({ message = 'Loading BodegaCat...' }) => {
  const [raindrops, setRaindrops] = useState([]);
  
  // Generate characters for the matrix rain effect
  useEffect(() => {
    const symbols = [
      '🐱', '🏪', '🗽', '🌆', '🌇', '🚕', '🚇', 
      '🏙️', '🌉', '🌃', '🚖', '🏢', '🛒', '🛍️',
      ...Array.from({ length: 26 }, (_, i) => String.fromCharCode(65 + i)),
      ...Array.from({ length: 26 }, (_, i) => String.fromCharCode(97 + i)),
      ...Array.from({ length: 10 }, (_, i) => i.toString())
    ];
    
    const drops = [];
    
    for (let i = 0; i < 50; i++) {
      const x = Math.floor(Math.random() * window.innerWidth);
      const y = Math.floor(Math.random() * window.innerHeight);
      const delay = Math.random() * 2;
      const symbol = symbols[Math.floor(Math.random() * symbols.length)];
      
      drops.push({ id: i, x, y, delay, symbol });
    }
    
    setRaindrops(drops);
  }, []);
  
  return (
    <div className="matrix-loading">
      <div className="matrix-rain">
        {raindrops.map((drop) => (
          <div
            key={drop.id}
            className="matrix-drop"
            style={{
              left: `${drop.x}px`,
              top: `${drop.y}px`,
              animationDelay: `${drop.delay}s`,
            }}
          >
            {drop.symbol}
          </div>
        ))}
      </div>
      <div className="matrix-text">{message}</div>
    </div>
  );
};

export default MatrixLoading;