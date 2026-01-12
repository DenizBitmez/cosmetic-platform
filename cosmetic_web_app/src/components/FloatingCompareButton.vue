<template>
  <!-- Floating Comparison Button -->
  <transition name="slide-up">
    <div v-if="comparisonStore.itemCount > 0" class="fixed bottom-6 right-6 z-50">
      <router-link to="/comparison" class="flex items-center gap-3 bg-blue-600 text-white px-6 py-4 rounded-full shadow-2xl hover:bg-blue-700 transition-all hover:scale-105">
        <PhScales :size="24" weight="fill" />
        <div class="flex flex-col items-start">
          <span class="text-sm font-bold">Compare Products</span>
          <span class="text-xs opacity-90">{{ comparisonStore.itemCount }} / {{ comparisonStore.maxItems }} items</span>
        </div>
        <div class="bg-white text-blue-600 rounded-full w-6 h-6 flex items-center justify-center text-xs font-bold">
          {{ comparisonStore.itemCount }}
        </div>
      </router-link>
    </div>
  </transition>
</template>

<script setup>
import { useComparisonStore } from '@/stores/comparison';
import { PhScales } from '@phosphor-icons/vue';

const comparisonStore = useComparisonStore();
</script>

<style scoped>
.slide-up-enter-active,
.slide-up-leave-active {
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

.slide-up-enter-from {
  transform: translateY(100px);
  opacity: 0;
}

.slide-up-leave-to {
  transform: translateY(100px);
  opacity: 0;
}
</style>
