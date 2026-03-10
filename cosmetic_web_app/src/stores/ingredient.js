import { defineStore } from 'pinia';
import api from '@/services/api';

export const useIngredientStore = defineStore('ingredient', {
    state: () => ({
        ingredients: [],
        searchResults: [],
        currentIngredient: null,
        loading: false,
        error: null,
        currentPage: 0,
        totalPages: 0,
        totalElements: 0,
        currentSearchPage: 0,
        totalSearchPages: 0
    }),

    getters: {
        safeIngredients: (state) => state.ingredients.filter(i => i.alertType === 'safe'),
        warningIngredients: (state) => state.ingredients.filter(i => i.alertType === 'warning'),
        groupedByLetter: (state) => {
            const groups = {};
            state.ingredients.forEach(ing => {
                const firstLetter = ing.name.charAt(0).toUpperCase();
                if (!groups[firstLetter]) groups[firstLetter] = [];
                groups[firstLetter].push(ing);
            });
            // Sort keys
            return Object.keys(groups).sort().reduce((acc, key) => {
                acc[key] = groups[key].sort((a, b) => a.name.localeCompare(b.name));
                return acc;
            }, {});
        }
    },

    actions: {
        async fetchAllIngredients(page = 0, size = 30, reset = true) {
            this.loading = true;
            try {
                const response = await api.get('/ingredients', { params: { page, size } });
                if (reset) {
                    this.ingredients = response.data.content;
                } else {
                    this.ingredients = [...this.ingredients, ...response.data.content];
                }
                this.currentPage = response.data.number;
                this.totalPages = response.data.totalPages;
                this.totalElements = response.data.totalElements;
            } catch (err) {
                this.error = 'Failed to load ingredients';
                console.error(err);
            } finally {
                this.loading = false;
            }
        },

        async searchIngredients(query, page = 0, size = 30) {
            if (!query) {
                this.searchResults = [];
                return;
            }
            this.loading = true;
            try {
                const response = await api.get('/ingredients/search', { params: { query, page, size } });
                this.searchResults = response.data.content;
                this.currentSearchPage = response.data.number;
                this.totalSearchPages = response.data.totalPages;
            } catch (err) {
                console.error(err);
            } finally {
                this.loading = false;
            }
        },

        async getIngredientById(id) {
            this.loading = true;
            try {
                const response = await api.get(`/ingredients/${id}`);
                this.currentIngredient = response.data;
                return response.data;
            } catch (err) {
                this.error = 'Failed to fetch ingredient details';
                console.error(err);
            } finally {
                this.loading = false;
            }
        }
    }
});
