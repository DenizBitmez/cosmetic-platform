import { defineStore } from 'pinia';
import api from '@/services/api';

export const useAuthStore = defineStore('auth', {
    state: () => ({
        user: JSON.parse(localStorage.getItem('user')) || null,
        isAuthenticated: !!localStorage.getItem('user'),
        error: null,
    }),
    actions: {
        async login(email, password) {
            this.error = null;
            try {
                const response = await api.post('/auth/login', { email, password });
                this.user = response.data;
                this.isAuthenticated = true;
                localStorage.setItem('user', JSON.stringify(this.user));

                // Fetch cart immediately after login
                const { useCartStore } = await import('./cart');
                const cartStore = useCartStore();
                cartStore.fetchCart();

                return true;
            } catch (err) {
                this.error = err.response?.data?.message || 'Login failed';
                this.isAuthenticated = false;
                return false;
            }
        },
        logout() {
            this.user = null;
            this.isAuthenticated = false;
            localStorage.removeItem('user');
        }
    }
});
