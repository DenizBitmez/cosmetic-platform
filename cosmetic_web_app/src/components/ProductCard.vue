<template>
  <div class="group">
    <div class="w-full aspect-[3/4] bg-gray-200 overflow-hidden relative mb-4">
        <!-- Placeholder Image Logic -->
        <img src="https://images.unsplash.com/photo-1620916566398-39f1143ab7be?q=80&w=1887&auto=format&fit=crop" 
             alt="Crs product"
             class="w-full h-full object-cover object-center group-hover:scale-105 transition-transform duration-500 ease-in-out" />
        
    </div>
    
    <div class="p-2">
        <h3 class="text-lg font-medium text-brand-dark truncate font-serif">{{ product.name }}</h3>
        <p class="mt-1 text-sm text-gray-500 uppercase tracking-wide">{{ product.category }}</p>
        <div class="flex items-center justify-between mt-2">
             <p class="text-md font-bold text-brand-gold">${{ product.price || '0.00' }}</p>
             <button @click="addToCart" :disabled="loading" class="bg-gray-900 text-white py-2 px-4 text-xs font-bold uppercase tracking-widest hover:bg-gray-700 transition-colors disabled:opacity-50 shadow-md">
                {{ loading ? '...' : 'Add' }}
            </button>
        </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { useCartStore } from '@/stores/cart';
import { useAuthStore } from '@/stores/auth';
import { useRouter } from 'vue-router';

const props = defineProps({
  product: {
    type: Object,
    required: true
  }
});

const cartStore = useCartStore();
const authStore = useAuthStore();
const router = useRouter();
const loading = ref(false);

const addToCart = async () => {
    if (!authStore.isAuthenticated) {
        router.push('/login');
        return;
    }

    loading.value = true;
    const success = await cartStore.addToCart(props.product.id, 1);
    
    if (success) {
         cartStore.toggleDrawer(true);
    } else {
        alert("Failed to add to cart. Please try again.");
    }
    
    loading.value = false;
};
</script>
