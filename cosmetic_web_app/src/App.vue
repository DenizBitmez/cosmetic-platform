<script setup>
import { onMounted } from 'vue';
import { RouterView, useRoute } from 'vue-router';
import { useAuthStore } from '@/stores/auth';
import { useCartStore } from '@/stores/cart';
import NavBar from '@/components/NavBar.vue';
import CartDrawer from '@/components/CartDrawer.vue';
import Footer from '@/components/Footer.vue';

const route = useRoute();
const authStore = useAuthStore();
const cartStore = useCartStore();

onMounted(async () => {
    // Always check session first to ensure we have the latest user state
    await authStore.checkSession();
    
    // Then fetch cart if we are authenticated
    if (authStore.isAuthenticated) {
        console.log("Session verified, fetching cart...");
        await cartStore.fetchCart();
    }
});
</script>

<template>
  <div class="min-h-screen flex flex-col bg-gray-50 font-sans text-gray-900">
    <NavBar v-if="route.name !== 'login'" />
    <CartDrawer />
    <main class="flex-grow">
        <RouterView />
    </main>
    <Footer v-if="route.name !== 'login'" />
  </div>
</template>
