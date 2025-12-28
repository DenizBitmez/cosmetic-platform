import { defineStore } from 'pinia';
import api from '@/services/api';
import { useAuthStore } from './auth';

export const useCartStore = defineStore('cart', {
    state: () => ({
        cart: null,
        loading: false,
        error: null,
        drawerOpen: false,
    }),
    getters: {
        itemCount: (state) => state.cart?.cartItems?.reduce((total, item) => total + item.quantity, 0) || 0,
        totalAmount: (state) => state.cart?.totalAmount || 0,
    },
    actions: {
        toggleDrawer(isOpen) {
            this.drawerOpen = isOpen !== undefined ? isOpen : !this.drawerOpen;
        },
        async fetchCart() {
            const auth = useAuthStore();
            if (!auth.user || !auth.user.id) return;

            this.loading = true;
            try {
                const response = await api.get(`/cart/${auth.user.id}`);
                this.cart = response.data;
            } catch (err) {
                console.error("Failed to fetch cart", err);
            } finally {
                this.loading = false;
            }
        },

        async addToCart(productId, quantity = 1) {
            const auth = useAuthStore();
            if (!auth.isAuthenticated) {
                // Redirect to login or show notification
                return false;
            }

            this.loading = true;
            try {
                // API expects query params: ?productId=...&quantity=...
                const response = await api.post(`/cart/${auth.user.id}/add`, null, {
                    params: { productId, quantity }
                });
                this.cart = response.data;
                return true;
            } catch (err) {
                this.error = "Failed to add to cart";
                console.error(err);
                return false;
            } finally {
                this.loading = false;
            }
        },

        async removeFromCart(cartItemId) {
            const auth = useAuthStore();
            if (!auth.user) return;

            try {
                const response = await api.delete(`/cart/${auth.user.id}/remove/${cartItemId}`);
                this.cart = response.data;
            } catch (err) {
                console.error(err);
            }
        },

        async clearCart() {
            const auth = useAuthStore();
            if (!auth.user) return;
            try {
                await api.delete(`/cart/${auth.user.id}/clear`);
                this.cart = null;
            } catch (err) {
                console.error(err);
            }
        }
    }
});
