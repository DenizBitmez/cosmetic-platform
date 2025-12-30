import axios from 'axios';

const api = axios.create({
    baseURL: '/api', // Proxy handles the target
    withCredentials: true,
    headers: {
        'Content-Type': 'application/json',
    },
});

export default api;
