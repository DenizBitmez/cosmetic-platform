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

                // Fetch extra data
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
        async handleOAuthLogin(email, name, id) {
            // ...
            this.user = { id: id, email: email, userType: 'CLIENT', username: name || 'Google User' }; // Simplified with ID
            this.isAuthenticated = true;
            localStorage.setItem('user', JSON.stringify(this.user));

            // Fetch cart immediately after login
            const { useCartStore } = await import('./cart');
            const cartStore = useCartStore();
            cartStore.fetchCart();

            return true;
        },
        async forgotPassword(email) {
            try {
                await api.post('/auth/forgot-password', null, { params: { email } });
                return true;
            } catch (err) {
                this.error = err.response?.data || 'Failed to send reset link';
                return false;
            }
        },
        async resetPassword(token, newPassword) {
            try {
                await api.post('/auth/reset-password', newPassword, { params: { token }, headers: { 'Content-Type': 'text/plain' } });
                return true;
            } catch (err) {
                this.error = err.response?.data || 'Failed to reset password';
                return false;
            }
        },
        async checkSession() {
            if (!this.isAuthenticated) return;
            try {
                const response = await api.get(`/user/${this.user.id}`);
                this.user = response.data;
                localStorage.setItem('user', JSON.stringify(this.user));
            } catch (err) {
                console.error("Session verification failed", err);
                if (err.response?.status === 401 || err.response?.status === 403) {
                    this.logout();
                }
            }
        },
        async updateAllergies(allergies) {
            if (!this.user?.id) return false;
            try {
                const res = await api.put(`/user/${this.user.id}/allergies`, { allergies });
                this.user = res.data;
                localStorage.setItem('user', JSON.stringify(this.user));
                return true;
            } catch (err) {
                console.error("Failed to update allergies", err);
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
