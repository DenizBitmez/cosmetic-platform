import { defineStore } from 'pinia';
import api from '@/services/api';
import { useAuthStore } from './auth';

export const useRecommendationStore = defineStore('recommendation', {
    state: () => ({
        recommendations: [],
        skinProfile: null,
        loading: false,
        error: null,
    }),

    getters: {
        hasCompletedQuiz: (state) => state.skinProfile?.hasCompletedQuiz || false,
        recommendationCount: (state) => state.recommendations.length,
    },

    actions: {
        async fetchRecommendations(limit = 12) {
            const auth = useAuthStore();
            if (!auth.user || !auth.user.id) {
                console.log('User not logged in, skipping recommendations');
                return;
            }

            this.loading = true;
            try {
                const response = await api.get('/recommendations/personalized', {
                    params: {
                        userId: auth.user.id,
                        limit
                    }
                });
                this.recommendations = response.data;
                console.log('Fetched recommendations:', this.recommendations.length);
            } catch (err) {
                console.error('Failed to fetch recommendations:', err);
                this.error = 'Failed to load recommendations';
            } finally {
                this.loading = false;
            }
        },

        async fetchSkinProfile() {
            const auth = useAuthStore();
            if (!auth.user || !auth.user.id) {
                console.log('User not logged in, skipping skin profile fetch');
                return;
            }

            try {
                const response = await api.get(`/recommendations/skin-profile/${auth.user.id}`);
                this.skinProfile = response.data;
                console.log('Skin profile:', this.skinProfile);
            } catch (err) {
                console.error('Failed to fetch skin profile:', err);
            }
        },

        async saveSkinProfile(profileData) {
            const auth = useAuthStore();
            if (!auth.user) {
                return { success: false, message: 'Please login first' };
            }

            this.loading = true;
            try {
                const response = await api.post('/recommendations/skin-profile', null, {
                    params: {
                        userId: auth.user.id,
                        ...profileData
                    }
                });

                // Refresh skin profile and recommendations
                await this.fetchSkinProfile();
                await this.fetchRecommendations();

                return { success: true, message: 'Skin profile saved!' };
            } catch (err) {
                console.error('Failed to save skin profile:', err);
                return { success: false, message: 'Failed to save skin profile' };
            } finally {
                this.loading = false;
            }
        },

        async getSimilarProducts(productId, limit = 6) {
            try {
                const response = await api.get(`/recommendations/similar/${productId}`, {
                    params: { limit }
                });
                return response.data;
            } catch (err) {
                console.error('Failed to fetch similar products:', err);
                return [];
            }
        }
    }
});
