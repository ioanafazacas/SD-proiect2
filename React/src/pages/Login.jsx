import React, { useState, useContext } from 'react';
import { authAPI } from '../api/auth.js';
import { useNavigate } from 'react-router-dom';
import { AuthContext } from '../context/AuthContext';

const Login = () => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const { login } = useContext(AuthContext);
  const navigate = useNavigate();

  const handleLogin = async (e) => {
    e.preventDefault();

    try {
      // 🔹 Trimite cererea către backend
      const response = await authAPI.post('/auth/login', { username, password });
      console.log("✅ Răspuns primit:", response.data);

      // 🔹 Extrage tokenul și userul
      const { token, user } = response.data;

      // 🔹 Salvează în context + localStorage
      login({ user, token });

      // 🔹 Redirecționează după rol
      if (user.role === 'ADMIN') navigate('/admin');
      else navigate('/user');

    } catch (err) {
      console.error('❌ Eroare la login:', err.response?.data || err.message);
      alert('Login failed. Check credentials.');
    }
  };

  return (
    <div className="flex flex-col items-center justify-center h-screen bg-gray-100">
      <form onSubmit={handleLogin} className="bg-white p-6 rounded shadow-md w-80">
        <h2 className="text-2xl mb-4 text-center font-bold">Login</h2>
        <input
          type="text"
          placeholder="Username"
          className="border p-2 w-full mb-3"
          value={username}
          onChange={(e) => setUsername(e.target.value)}
        />
        <input
          type="password"
          placeholder="Password"
          className="border p-2 w-full mb-3"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
        />
        <button className="bg-blue-500 text-white w-full py-2 rounded">
          Login
        </button>
      </form>
    </div>
  );
};

export default Login;
