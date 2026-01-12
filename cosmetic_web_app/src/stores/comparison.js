import { defineStore } from 'pinia';
import { useUiStore } from './ui';

export const useComparisonStore = defineStore('comparison', {
    state: () => ({
        comparisonList: JSON.parse(localStorage.getItem('comparisonList') || '[]'),
        maxItems: 4,
    }),

    getters: {
        itemCount: (state) => state.comparisonList.length,

        isFull: (state) => state.comparisonList.length >= state.maxItems,

        isInComparison: (state) => (productId) => {
            return state.comparisonList.some(item => item.id === productId);
        },

        canAddMore: (state) => state.comparisonList.length < state.maxItems,

        getComparisonProducts: (state) => state.comparisonList,
    },

    actions: {
        addToComparison(product) {
            const uiStore = useUiStore();

            // Check if already in comparison
            if (this.isInComparison(product.id)) {
                uiStore.notify('Product already in comparison', 'info');
                return { success: false, message: 'Already in comparison' };
            }

            // Check if comparison is full
            if (this.isFull) {
                uiStore.notify(`Maximum ${this.maxItems} products can be compared`, 'warning');
                return { success: false, message: 'Comparison list is full' };
            }

            // Add to comparison
            const comparisonItem = {
                id: product.id,
                name: product.name,
                image: product.image || product.api_featured_image,
                price: product.price,
                category: product.category || product.product_type,
                brand: product.brand,
                rating: product.rating,
                stock: product.stock,
                ingredients: product.ingredients || [],
                description: product.description,
                paoMonths: product.paoMonths,
                addedAt: new Date().toISOString()
            };

            this.comparisonList.push(comparisonItem);
            this.saveToLocalStorage();

            uiStore.notify('Added to comparison!', 'success');
            return { success: true, message: 'Added to comparison' };
        },

        removeFromComparison(productId) {
            const uiStore = useUiStore();

            this.comparisonList = this.comparisonList.filter(item => item.id !== productId);
            this.saveToLocalStorage();

            uiStore.notify('Removed from comparison', 'success');
            return { success: true, message: 'Removed from comparison' };
        },

        clearComparison() {
            this.comparisonList = [];
            this.saveToLocalStorage();

            const uiStore = useUiStore();
            uiStore.notify('Comparison cleared', 'success');
            return { success: true, message: 'Comparison cleared' };
        },

        toggleComparison(product) {
            if (this.isInComparison(product.id)) {
                return this.removeFromComparison(product.id);
            } else {
                return this.addToComparison(product);
            }
        },

        saveToLocalStorage() {
            localStorage.setItem('comparisonList', JSON.stringify(this.comparisonList));
        },

        loadFromLocalStorage() {
            const stored = localStorage.getItem('comparisonList');
            if (stored) {
                try {
                    this.comparisonList = JSON.parse(stored);
                } catch (e) {
                    console.error('Failed to parse comparison list from localStorage', e);
                    this.comparisonList = [];
                }
            }
        }
    }
});
