import { defineStore } from 'pinia';
import api from '@/services/api';
import { useAuthStore } from './auth';

export const usePriceAlertStore = defineStore('priceAlert', {
    state: () => ({
        alerts: [],
        loading: false,
        error: null,
    }),

    getters: {
        alertCount: (state) => state.alerts.length,

        activeAlerts: (state) => state.alerts.filter(alert => alert.isActive),

        hasAlertForProduct: (state) => (productId) => {
            return state.alerts.some(alert =>
                alert.productId === productId && alert.isActive
            );
        },

        priceDroppedAlerts: (state) => state.alerts.filter(alert => alert.isPriceDropped),
    },

    actions: {
        async fetchAlerts() {
            const auth = useAuthStore();
            if (!auth.user || !auth.user.id) return;

            this.loading = true;
            this.error = null;

            try {
                const response = await api.get(`/price-alert/user/${auth.user.id}`);
                this.alerts = response.data;
            } catch (err) {
                console.error('Failed to fetch price alerts:', err);
                this.error = 'Failed to load price alerts';
            } finally {
                this.loading = false;
            }
        },

        async createAlert(productId, targetPrice, productData = null) {
            const auth = useAuthStore();
            if (!auth.isAuthenticated) {
                return { success: false, message: 'Please login first' };
            }

            this.loading = true;
            try {
                const params = {
                    userId: auth.user.id,
                    productId: productId,
                    targetPrice: targetPrice
                };

                // If product data is provided, include external product fields
                if (productData) {
                    params.externalName = productData.name;
                    params.externalImage = productData.image;
                    params.externalBrand = productData.brand;
                }

                const response = await api.post('/price-alert/create', null, { params });

                // Refresh alerts
                await this.fetchAlerts();

                return { success: true, message: response.data.message || 'Price alert created!' };
            } catch (err) {
                console.error('Failed to create price alert:', err);
                const message = err.response?.data?.message || 'Failed to create price alert';
                return { success: false, message };
            } finally {
                this.loading = false;
            }
        },

        async deleteAlert(alertId) {
            try {
                await api.delete(`/price-alert/${alertId}`);

                // Update local state
                this.alerts = this.alerts.filter(alert => alert.id !== alertId);

                return { success: true, message: 'Price alert deleted' };
            } catch (err) {
                console.error('Failed to delete price alert:', err);
                return { success: false, message: 'Failed to delete alert' };
            }
        },

        async checkAlert(productId) {
            const auth = useAuthStore();
            if (!auth.user) return false;

            try {
                const response = await api.get('/price-alert/check', {
                    params: {
                        userId: auth.user.id,
                        productId: productId
                    }
                });
                return response.data.hasAlert;
            } catch (err) {
                console.error('Failed to check price alert:', err);
                return false;
            }
        },

        async getAlertCount() {
            const auth = useAuthStore();
            if (!auth.user) return 0;

            try {
                const response = await api.get(`/price-alert/user/${auth.user.id}/count`);
                return response.data.count;
            } catch (err) {
                console.error('Failed to get alert count:', err);
                return 0;
            }
        }
    }
});
