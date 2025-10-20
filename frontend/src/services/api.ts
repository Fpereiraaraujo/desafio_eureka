import axios from 'axios';

const api = axios.create({
    baseURL: 'http://localhost:18080/v1', // backend
});

export default api;
