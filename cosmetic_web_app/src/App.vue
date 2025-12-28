<script setup>
import { RouterView, useRoute } from 'vue-router'
import NavBar from '@/components/NavBar.vue'
import CartDrawer from '@/components/CartDrawer.vue'

const route = useRoute();
</script>

<template>
  <div class="min-h-screen bg-gray-50 font-sans text-gray-900">
    <NavBar v-if="route.name !== 'login'" />
    <CartDrawer />
    <RouterView />
  </div>
</template>
