
import axios from 'axios';

export const monitoringApi = axios.create({
  baseURL: 'http://monitoring.localhost',
});
