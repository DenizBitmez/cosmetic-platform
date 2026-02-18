import axios from 'axios';

const api = axios.create({
    baseURL: '/api', // Proxy handles the target
    withCredentials: true,
    headers: {
        'Content-Type': 'application/json',
    },
});

// Global response interceptor: catch 401/403 → auto logout & redirect
api.interceptors.response.use(
    (response) => response,
    (error) => {
        if (error.response && (error.response.status === 401 || error.response.status === 403)) {
            // Avoid circular imports by accessing store lazily
            const userData = localStorage.getItem('user');
            if (userData) {
                // Clear auth state
                localStorage.removeItem('user');

                // Redirect to login page (only if not already there)
                if (!window.location.pathname.includes('/login')) {
                    window.location.href = '/login?session=expired';
                }
            }
        }
        return Promise.reject(error);
    }
);

export default api;
