import axios from 'axios';

export const authAPI = axios.create({
  baseURL: 'http://auth.localhost',
});

// opțional: intercepțiune pentru token automat
authAPI.interceptors.request.use((config) => {
  const token = localStorage.getItem('token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});