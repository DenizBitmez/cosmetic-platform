import { defineStore } from 'pinia';

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

        async fetchByCategory(category) {
            if (this.categoriesCache[category]) return;

            this.loading = true;
            try {
                const url = `https://makeup-api.herokuapp.com/api/v1/products.json?product_type=${category}`;
                const res = await fetch(url);
                if (!res.ok) throw new Error('API Error');

                const data = await res.json();
                if (!data || data.length === 0) throw new Error('No data from API');

                const validProducts = data.filter(p =>
                    p.price && parseFloat(p.price) > 0 &&
                    p.image_link && p.image_link.trim().length > 10
                );

                this.categoriesCache[category] = validProducts.map(p => ({
                    ...p,
                    image: p.image_link.replace('http://', 'https://'),
                    price: p.price
                }));
            } catch (e) {
                console.error(`Failed to fetch products for category: ${category}`, e);
                // We keep the cache empty or handle error
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
