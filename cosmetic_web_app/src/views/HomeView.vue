<template>
  <div class="min-h-screen bg-brand-cream pb-20">
    <!-- Hero Section -->
    <div class="relative bg-gray-900 h-[60vh] overflow-hidden">
        <div class="absolute inset-0">
             <img class="w-full h-full object-cover opacity-60" src="https://images.unsplash.com/photo-1596462502278-27bfdd403348?q=80&w=2670&auto=format&fit=crop" alt="Cosmetics Background">
        </div>
        <div class="relative max-w-7xl mx-auto py-24 px-4 sm:py-32 sm:px-6 lg:px-8 h-full flex items-center">
             <div class="max-w-xl">
                <h1 class="text-4xl font-extrabold tracking-tight text-white sm:text-5xl lg:text-6xl font-serif mb-6">
                    Redefine Your Glow
                </h1>
                <p class="mt-4 text-xl text-gray-100 max-w-lg mb-8">
                    Curated collection of premium skincare and cosmetics for the modern individual.
                </p>
                <a href="#products" class="inline-block bg-brand-gold border border-transparent py-3 px-8 text-base font-medium text-white hover:bg-white hover:text-brand-gold transition-all duration-300 uppercase tracking-widest">
                    Shop Collection
                </a>
             </div>
        </div>
    </div>

    <!-- Features Section -->
    <div class="py-16 bg-white">
        <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 grid grid-cols-1 md:grid-cols-3 gap-8 text-center">
            <div class="p-6">
                <div class="text-brand-gold mb-4 flex justify-center"><PhSparkle :size="32" /></div>
                <h3 class="text-lg font-bold uppercase tracking-wide mb-2">Premium Quality</h3>
                <p class="text-gray-500 font-light">Sourced from the finest ingredients worldwide.</p>
            </div>
            <div class="p-6 border-l border-r border-gray-100">
                <div class="text-brand-gold mb-4 flex justify-center"><PhPlant :size="32" /></div>
                <h3 class="text-lg font-bold uppercase tracking-wide mb-2">Cruelty Free</h3>
                <p class="text-gray-500 font-light">Ethical beauty that cares for the planet.</p>
            </div>
            <div class="p-6">
                 <div class="text-brand-gold mb-4 flex justify-center"><PhTruck :size="32" /></div>
                <h3 class="text-lg font-bold uppercase tracking-wide mb-2">Fast Delivery</h3>
                <p class="text-gray-500 font-light">Luxury delivered to your doorstep in days.</p>
            </div>
        </div>
    </div>

    <!-- Product Grid -->
    <div id="products" class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 mt-16">
        <div class="flex items-center justify-between mb-12">
            <h2 class="text-3xl font-serif text-brand-dark">Latest Arrivals</h2>
             <a href="#" class="text-brand-gold hover:text-brand-dark transition-colors border-b border-brand-gold pb-1 text-sm uppercase tracking-wide">View All</a>
        </div>
        
        <div v-if="loading" class="text-center py-24">
             <div class="animate-pulse flex flex-col items-center">
                <div class="h-4 bg-gray-200 rounded w-1/4 mb-4"></div>
                <div class="h-4 bg-gray-200 rounded w-1/6"></div>
             </div>
        </div>

        <div v-else-if="error" class="text-center py-12 text-red-500 bg-red-50 rounded p-8">
            <p class="font-bold">Something went wrong.</p>
            <p>{{ error }}</p>
        </div>

        <div v-else class="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-y-12 gap-x-8">
            <ProductCard 
                v-for="product in filteredProducts" 
                :key="product.id" 
                :product="product" 
            />
        </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue';
import api from '@/services/api';
import ProductCard from '@/components/ProductCard.vue';
import { PhSparkle, PhPlant, PhTruck } from '@phosphor-icons/vue';

const products = ref([]);
const loading = ref(true);
const error = ref(null);

const filteredProducts = computed(() => {
    return products.value.filter(p => p.price > 0);
});

onMounted(async () => {
    try {
        const response = await api.get('/product/all');
        products.value = response.data;
    } catch (err) {
        console.error('Failed to fetch products', err);
        if (err.response && err.response.status === 204) {
            products.value = [];
        } else {
             error.value = "Unable to load catalogue.";
        }
    } finally {
        loading.value = false;
    }
});
</script>
