import { defineStore } from 'pinia';
import api from '@/services/api';
import { useAuthStore } from './auth';

export const useWishlistStore = defineStore('wishlist', {
    state: () => ({
        wishlistItems: [],
        loading: false,
        error: null,
    }),

    getters: {
        itemCount: (state) => state.wishlistItems.length,

        isInWishlist: (state) => (productId) => {
            return state.wishlistItems.some(item => item.productId === productId);
        },

        getWishlistProducts: (state) => state.wishlistItems,
    },

    actions: {
        async fetchWishlist() {
            const auth = useAuthStore();
            if (!auth.user || !auth.user.id) return;

            this.loading = true;
            this.error = null;

            try {
                const response = await api.get(`/wishlist/${auth.user.id}`);
                this.wishlistItems = response.data;
            } catch (err) {
                console.error('Failed to fetch wishlist:', err);
                this.error = 'Failed to load wishlist';
            } finally {
                this.loading = false;
            }
        },

        async addToWishlist(productId, productData = null) {
            const auth = useAuthStore();
            if (!auth.isAuthenticated) {
                return { success: false, message: 'Please login first' };
            }

            this.loading = true;
            try {
                const params = {
                    userId: auth.user.id,
                    productId: productId
                };

                // If product data is provided, include external product fields
                if (productData) {
                    params.externalName = productData.name;
                    params.externalImage = productData.image;
                    params.externalPrice = productData.price;
                    params.externalBrand = productData.brand;
                    params.externalCategory = productData.category;
                }

                const response = await api.post('/wishlist/add', null, { params });

                // Refresh wishlist
                await this.fetchWishlist();

                return { success: true, message: 'Added to wishlist!' };
            } catch (err) {
                console.error('Failed to add to wishlist:', err);
                console.error('Error details:', {
                    message: err.message,
                    status: err.response?.status,
                    data: err.response?.data,
                    config: err.config
                });
                const message = err.response?.data?.message || err.message || 'Failed to add to wishlist';
                return { success: false, message };
            } finally {
                this.loading = false;
            }
        },

        async removeFromWishlist(productId) {
            const auth = useAuthStore();
            if (!auth.user) return;

            try {
                await api.delete('/wishlist/remove', {
                    params: {
                        userId: auth.user.id,
                        productId: productId
                    }
                });

                // Update local state
                this.wishlistItems = this.wishlistItems.filter(
                    item => item.productId !== productId
                );

                return { success: true, message: 'Removed from wishlist' };
            } catch (err) {
                console.error('Failed to remove from wishlist:', err);
                return { success: false, message: 'Failed to remove from wishlist' };
            }
        },

        async removeById(wishlistId) {
            try {
                await api.delete(`/wishlist/${wishlistId}`);

                // Update local state
                this.wishlistItems = this.wishlistItems.filter(
                    item => item.id !== wishlistId
                );

                return { success: true, message: 'Removed from wishlist' };
            } catch (err) {
                console.error('Failed to remove item:', err);
                return { success: false, message: 'Failed to remove item' };
            }
        },

        async checkWishlist(productId) {
            const auth = useAuthStore();
            if (!auth.user) return false;

            try {
                const response = await api.get('/wishlist/check', {
                    params: {
                        userId: auth.user.id,
                        productId: productId
                    }
                });
                return response.data.inWishlist;
            } catch (err) {
                console.error('Failed to check wishlist:', err);
                return false;
            }
        },

        async getWishlistCount() {
            const auth = useAuthStore();
            if (!auth.user) return 0;

            try {
                const response = await api.get(`/wishlist/${auth.user.id}/count`);
                return response.data.count;
            } catch (err) {
                console.error('Failed to get wishlist count:', err);
                return 0;
            }
        },

        async generateShareLink() {
            const auth = useAuthStore();
            if (!auth.user) return null;

            try {
                const response = await api.post(`/wishlist/${auth.user.id}/share`);
                return response.data;
            } catch (err) {
                console.error('Failed to generate share link:', err);
                return null;
            }
        },

        toggleWishlist(productId, productData = null) {
            if (this.isInWishlist(productId)) {
                return this.removeFromWishlist(productId);
            } else {
                return this.addToWishlist(productId, productData);
            }
        }
    }
});
