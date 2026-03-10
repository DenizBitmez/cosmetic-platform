import { defineStore } from 'pinia';
import api from '@/services/api';

export const useProductStore = defineStore('product', {
    state: () => ({
        latestArrivals: [],
        categoriesCache: {}, // { categoryName: [products] }
        loading: false,
        error: null
    }),
    actions: {
        async fetchLatestArrivals() {
            if (this.latestArrivals.length > 0) return;

            this.loading = true;
            try {
                const response = await fetch('https://makeup-api.herokuapp.com/api/v1/products.json?product_type=nail_polish');
                if (!response.ok) throw new Error('API Error');
                const data = await response.json();

                this.latestArrivals = data
                    .filter(p => p.price && parseFloat(p.price) > 0 && p.image_link && p.image_link.length > 10)
                    .map(p => ({
                        ...p,
                        image: p.image_link.replace('http://', 'https://'),
                        price: p.price
                    }));
            } catch (err) {
                console.error('Failed to fetch latest arrivals', err);
                this.error = "Unable to load catalogue.";
            } finally {
                this.loading = false;
            }
        },

        async fetchByCategory(category, isVegan = false, isCrueltyFree = false) {
            // Include filters in cache key to force refetch when filters change
            const cacheKey = `${category}_vegan${isVegan}_crueltyFree${isCrueltyFree}`;
            if (this.categoriesCache[cacheKey]) return;

            this.loading = true;
            try {
                // 1. Fetch from External API
                const url = `https://makeup-api.herokuapp.com/api/v1/products.json?product_type=${category}`;
                let externalProducts = [];
                try {
                    const res = await fetch(url);
                    if (res.ok) {
                        const data = await res.json();
                        externalProducts = data.filter(p =>
                            p.price && parseFloat(p.price) > 0 &&
                            p.image_link && p.image_link.trim().length > 10
                        ).map(p => {
                            // Deterministically mock sustainability properties based on ID
                            const mockVegan = p.id % 2 === 0;
                            const mockCrueltyFree = p.id % 3 !== 0;

                            return {
                                ...p,
                                image: p.image_link.replace('http://', 'https://'),
                                price: p.price,
                                isVegan: mockVegan,
                                isCrueltyFree: mockCrueltyFree,
                                ecoPackagingScore: (p.id % 10) + 1,
                                carbonFootprintRating: ['A', 'B', 'C', 'D', 'E'][p.id % 5],
                                overallSustainabilityScore: ((p.id % 5) * 10 + 50)
                            };
                        });

                        // Apply filters to external products locally
                        if (isVegan) {
                            externalProducts = externalProducts.filter(p => p.isVegan);
                        }
                        if (isCrueltyFree) {
                            externalProducts = externalProducts.filter(p => p.isCrueltyFree);
                        }
                    }
                } catch (err) {
                    console.warn(`External API failed for ${category}`, err);
                }

                // 2. Fetch from Local Backend
                let localProducts = [];
                try {
                    const params = {};
                    if (isVegan) params.isVegan = true;
                    if (isCrueltyFree) params.isCrueltyFree = true;

                    const res = await api.get(`/product/category/${category}`, { params });
                    if (res.status === 200 && res.data) {
                        localProducts = res.data.map(p => ({
                            ...p,
                            brand: p.brand || 'Dermacosmetic',
                            image: p.image || 'https://via.placeholder.com/300?text=Beauty+Product'
                        }));
                    }
                } catch (localErr) {
                    console.warn(`Local API failed for ${category}`, localErr.message);
                }

                // Combine results
                this.categoriesCache[cacheKey] = [...localProducts, ...externalProducts];

                if (this.categoriesCache[cacheKey].length === 0) {
                    throw new Error('No products found in this category');
                }

            } catch (e) {
                console.error(`Failed to fetch products for category: ${category}`, e);
                throw e;
            } finally {
                this.loading = false;
            }
        },

        clearCache() {
            this.latestArrivals = [];
            this.categoriesCache = {};
        }
    }
});
