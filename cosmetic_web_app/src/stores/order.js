import { defineStore } from 'pinia';
import api from '@/services/api';
import { useAuthStore } from './auth';

export const useOrderStore = defineStore('order', {
    state: () => ({
        orders: [],
        loading: false,
        error: null,
    }),
    actions: {
        async fetchUserOrders() {
            const auth = useAuthStore();
            console.log('OrderStore: Fetching orders for user:', auth.user);
            if (!auth.user?.id) {
                console.warn('OrderStore: No user ID found, aborting.');
                return;
            }

            this.loading = true;
            try {
                const response = await api.get(`/order/user/${auth.user.id}`);
                this.orders = response.data;
            } catch (err) {
                this.error = "Failed to load orders.";
                console.error(err);
            } finally {
                this.loading = false;
            }
        }
    }
});
