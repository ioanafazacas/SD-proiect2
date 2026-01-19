import React from 'react';
import ReactDOM from 'react-dom/client';
import App from './App.jsx';
import './styles/main.css';
import { AuthProvider } from './context/AuthContext.jsx';
import { createRoot } from 'react-dom/client';

window.global = window;

createRoot(document.getElementById('root')).render(<App />);

ReactDOM.createRoot(document.getElementById('root')).render(
  <React.StrictMode>
    <AuthProvider>
      <App />
    </AuthProvider>
  </React.StrictMode>,
);
