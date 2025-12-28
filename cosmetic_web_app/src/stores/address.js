import { defineStore } from 'pinia';
import api from '@/services/api';
import { useAuthStore } from './auth';

export const useAddressStore = defineStore('address', {
    state: () => ({
        addresses: [],
        loading: false,
        error: null,
    }),
    actions: {
        async fetchAddresses() {
            const auth = useAuthStore();
            if (!auth.user?.id) return;

            this.loading = true;
            try {
                const response = await api.get(`/address/user/${auth.user.id}`);
                this.addresses = response.data;
            } catch (err) {
                this.error = "Failed to load addresses.";
                console.error(err);
            } finally {
                this.loading = false;
            }
        },

        async addAddress(addressData) {
            const auth = useAuthStore();
            if (!auth.user?.id) return false;

            this.loading = true;
            try {
                // Ensure userId is in DTO
                const payload = { ...addressData, userId: auth.user.id };
                const response = await api.post('/address/add', payload);
                this.addresses.push(response.data);
                return true;
            } catch (err) {
                this.error = "Failed to add address.";
                console.error(err);
                return false;
            } finally {
                this.loading = false;
            }
        },

        async deleteAddress(addressId) {
            try {
                await api.delete(`/address/${addressId}`);
                this.addresses = this.addresses.filter(a => a.id !== addressId);
            } catch (err) {
                console.error(err);
            }
        }
    }
});
