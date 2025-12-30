<template>
  <div class="bg-white min-h-screen pt-20 pb-16">
    <div v-if="loading" class="flex justify-center items-center h-64">
        <div class="animate-spin rounded-full h-12 w-12 border-b-2 border-brand-gold"></div>
    </div>
    <div v-else-if="product" class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
      <div class="lg:grid lg:grid-cols-2 lg:gap-x-8 lg:items-start">
        
        <!-- Image Gallery -->
        <div class="flex flex-col-reverse">
          <div class="w-full aspect-w-1 aspect-h-1 bg-gray-100 rounded-lg overflow-hidden sm:aspect-w-2 sm:aspect-h-3">
             <img :src="product.image || 'https://images.unsplash.com/photo-1620916566398-39f1143ab7be?q=80&w=1000&auto=format&fit=crop'" 
                  :alt="product.name" 
                  class="w-full h-full object-center object-cover hover:scale-105 transition-transform duration-500" />
          </div>
        </div>

        <!-- Product Info -->
        <div class="mt-10 px-4 sm:px-0 sm:mt-16 lg:mt-0">
          <h1 class="text-3xl font-extrabold tracking-tight text-brand-dark font-serif">{{ product.name }}</h1>
          
          <div class="mt-3">
            <h2 class="sr-only">Product information</h2>
            <p class="text-3xl text-brand-gold">${{ product.price }}</p>
          </div>

          <div class="mt-6">
            <h3 class="sr-only">Description</h3>
            <div class="text-base text-gray-700 space-y-6">
              <p>{{ product.description || 'Experience the essence of beauty with this premium product. Carefully formulated to enhance your natural glow.' }}</p>
            </div>
          </div>

          <div class="mt-6">
             <div class="flex items-center">
                <h4 class="text-sm font-medium text-gray-900 mr-4">Category:</h4>
                <span class="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium bg-brand-cream text-brand-dark">
                  {{ product.category }}
                </span>
             </div>
          </div>

          <div class="mt-10 flex sm:flex-col1">
            <button @click="addToCart" :disabled="adding" class="max-w-xs flex-1 bg-brand-dark border border-transparent rounded-md py-3 px-8 flex items-center justify-center text-base font-medium text-white hover:bg-black focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-offset-gray-50 focus:ring-brand-gold sm:w-full transition-colors uppercase tracking-widest">
               {{ adding ? 'Adding...' : 'Add to Cart' }}
            </button>
          </div>
          
          <div class="mt-8 border-t border-gray-200 pt-8">
               <h3 class="text-sm font-medium text-gray-900">Highlights</h3>
               <ul role="list" class="mt-4 pl-4 list-disc text-sm space-y-2 text-gray-500">
                   <li>Cruelty-free and vegan</li>
                   <li>Dermatologist tested</li>
                   <li>Sustainably sourced ingredients</li>
                   <li>Premium recyclable packaging</li>
               </ul>
          </div>
        </div>
      </div>
    </div>
    <div v-else class="text-center py-20">
        <h2 class="text-2xl font-bold text-gray-900">Product not found</h2>
        <router-link to="/" class="mt-4 inline-block text-brand-gold hover:underline">Return to Shop</router-link>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import api from '@/services/api';
import { useCartStore } from '@/stores/cart';
import { useAuthStore } from '@/stores/auth';

const route = useRoute();
const router = useRouter();
const cartStore = useCartStore();
const authStore = useAuthStore();

const product = ref(null);
const loading = ref(true);
const adding = ref(false);

const addToCart = async () => {
    if (!authStore.isAuthenticated) {
        router.push('/login');
        return;
    }
    adding.value = true;
    const success = await cartStore.addToCart(product.value.id, 1);
    if (success) {
        cartStore.toggleDrawer(true);
    }
    adding.value = false;
};

onMounted(async () => {
    try {
        const id = route.params.id;
        const res = await api.get(`/product/find/${id}`);
        product.value = res.data;

        // --- TRACKING: RECENTLY VIEWED ---
        saveToRecentlyViewed(product.value);

    } catch (e) {
        console.error("Failed to fetch product", e);
    } finally {
        loading.value = false;
    }
});

const saveToRecentlyViewed = (prod) => {
    let viewed = JSON.parse(localStorage.getItem('recentlyViewed') || '[]');
    
    // Remove if already exists (to push to top)
    viewed = viewed.filter(p => p.id !== prod.id);
    
    // Add to beginning
    viewed.unshift({
        id: prod.id,
        name: prod.name,
        price: prod.price,
        image: prod.image || 'https://images.unsplash.com/photo-1620916566398-39f1143ab7be?q=80&w=200'
    });
    
    // Limit to 4 items
    if (viewed.length > 4) {
        viewed = viewed.slice(0, 4);
    }
    
    localStorage.setItem('recentlyViewed', JSON.stringify(viewed));
};
</script>
