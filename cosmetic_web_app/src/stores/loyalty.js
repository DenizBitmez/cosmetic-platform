import { defineStore } from 'pinia';
import api from '@/services/api';
import { useAuthStore } from '@/stores/auth';

export const useLoyaltyStore = defineStore('loyalty', {
    state: () => ({
        pointsBalance: 0,
        totalEarned: 0,
        transactions: [],
        loading: false,
        error: null
    }),
    actions: {
        async fetchBalance() {
            const authStore = useAuthStore();
            if (!authStore.user) return;

            this.loading = true;
            try {
                const response = await api.get(`/loyalty/balance/${authStore.user.id}`);
                this.pointsBalance = response.data.pointsBalance;
                this.totalEarned = response.data.totalPointsEarned;
            } catch (error) {
                console.error('Error fetching points balance:', error);
                this.error = "Failed to load points.";
            } finally {
                this.loading = false;
            }
        },

        async fetchHistory() {
            const authStore = useAuthStore();
            if (!authStore.user) return;

            this.loading = true;
            try {
                const response = await api.get(`/loyalty/history/${authStore.user.id}`);
                this.transactions = response.data;
            } catch (error) {
                console.error('Error fetching points history:', error);
            } finally {
                this.loading = false;
            }
        },

        async redeemPoints(amount) {
            const authStore = useAuthStore();
            if (!authStore.user) return false;

            try {
                const response = await api.post('/loyalty/redeem', {
                    userId: authStore.user.id,
                    amount: amount,
                    description: "Discount Redemption"
                });
                if (response.data.success) {
                    this.pointsBalance -= amount; // Optimistic update
                    await this.fetchhistory(); // Refresh history
                    return true;
                }
                return false;
            } catch (error) {
                console.error('Error redeeming points:', error);
                return false;
            }
        }
    }
});
