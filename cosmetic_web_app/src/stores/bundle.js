import { defineStore } from 'pinia';
import api from '@/services/api';

export const useBundleStore = defineStore('bundle', {
    state: () => ({
        bundles: [],
        loading: false
    }),
    actions: {
        async fetchBundles(productId, productDetails = {}) {
            this.loading = true;
            try {
                const params = {
                    category: productDetails.category,
                    name: productDetails.name,
                    price: productDetails.price,
                    image: productDetails.image
                };
                const response = await api.get(`/bundles/product/${productId}`, { params });
                this.bundles = response.data;
            } catch (error) {
                console.error('Error fetching bundles:', error);
                this.bundles = [];
            } finally {
                this.loading = false;
            }
        }
    }
});
