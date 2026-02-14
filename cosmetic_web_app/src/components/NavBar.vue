<template>
  <nav class="bg-white/95 backdrop-blur-sm shadow-sm border-b border-gray-200 sticky top-0 z-50">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
      <div class="flex justify-between h-20">
        <div class="flex">
          <div class="flex-shrink-0 flex items-center">
            <h1 class="text-3xl font-serif text-brand-dark tracking-tighter">CP.</h1>
          </div>
          <div class="hidden sm:ml-12 sm:flex sm:space-x-8">
            <router-link to="/" class="border-transparent text-gray-500 hover:text-brand-gold inline-flex items-center px-1 pt-1 border-b-2 text-sm font-medium uppercase tracking-wide transition-colors">
              Home
            </router-link>
             <router-link to="/categories" class="border-transparent text-gray-500 hover:text-brand-gold inline-flex items-center px-1 pt-1 border-b-2 text-sm font-medium uppercase tracking-wide transition-colors">
              Categories
            </router-link>
             <router-link to="/community" class="border-transparent text-gray-500 hover:text-brand-gold inline-flex items-center px-1 pt-1 border-b-2 text-sm font-medium uppercase tracking-wide transition-colors">
              Community
            </router-link>
             <router-link to="/about" class="border-transparent text-gray-500 hover:text-brand-gold inline-flex items-center px-1 pt-1 border-b-2 text-sm font-medium uppercase tracking-wide transition-colors">
              About
            </router-link>
            <router-link to="/blog" class="border-transparent text-gray-500 hover:text-brand-gold inline-flex items-center px-1 pt-1 border-b-2 text-sm font-medium uppercase tracking-wide transition-colors">
              Blog
            </router-link>
            <router-link to="/ingredients" class="border-transparent text-gray-500 hover:text-brand-gold inline-flex items-center px-1 pt-1 border-b-2 text-sm font-medium uppercase tracking-wide transition-colors">
              Ingredients
            </router-link>
          </div>
        </div>
        <div class="flex items-center space-x-6">
            <template v-if="authStore.isAuthenticated">
                <button @click="cartStore.toggleDrawer()" class="text-gray-500 hover:text-brand-gold relative mr-4">
                  <PhShoppingCart :size="24" />
                  <span v-if="cartStore.itemCount > 0" class="absolute -top-2 -right-2 bg-brand-gold text-white text-xs font-bold rounded-full h-5 w-5 flex items-center justify-center">
                    {{ cartStore.itemCount }}
                  </span>
                </button>
                <router-link to="/profile" class="text-sm font-medium text-gray-900 mr-4 hover:text-brand-gold transition-colors">
                    Hello, {{ authStore.user?.username || 'Beauty' }}
                </router-link>
                <button @click="handleLogout" class="text-sm font-medium text-gray-500 hover:text-red-500 uppercase tracking-wider">Logout</button>
            </template>
            <template v-else>
                 <router-link to="/login" class="text-sm font-medium text-brand-dark hover:text-brand-gold uppercase tracking-wider">Log In</router-link>
                 <router-link to="/register" class="bg-brand-dark text-white px-5 py-2 text-xs font-bold uppercase tracking-widest hover:bg-brand-gold transition-colors">Sign Up</router-link>
            </template>
        </div>
      </div>
    </div>
  </nav>
</template>

<script setup>
import { useAuthStore } from '@/stores/auth';
import { useCartStore } from '@/stores/cart';
import { useRouter } from 'vue-router';
import { PhShoppingCart } from '@phosphor-icons/vue';

const authStore = useAuthStore();
const cartStore = useCartStore();
const router = useRouter();

const handleLogout = () => {
    authStore.logout();
    router.push('/login');
};

import { onMounted } from 'vue';
onMounted(() => {
    if (authStore.isAuthenticated) {
        cartStore.fetchCart();
    }
});
</script>
