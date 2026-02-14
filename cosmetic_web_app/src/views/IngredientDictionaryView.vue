<template>
  <div class="min-h-screen bg-brand-cream py-12 px-4 sm:px-6 lg:px-8">
    <div class="max-w-7xl mx-auto">
      <div class="text-center mb-12">
          <h1 class="text-4xl font-serif text-brand-dark mb-4">Cosmetic Ingredient Dictionary</h1>
          <p class="text-gray-600 max-w-2xl mx-auto">
              Decide what goes on your skin with confidence. Explore our comprehensive guide to cosmetic ingredients, understanding their benefits, functions, and safety profiles.
          </p>
      </div>

      <!-- Search Section -->
      <div class="max-w-3xl mx-auto mb-16 relative">
          <div class="relative">
              <input 
                v-model="searchQuery"
                @input="handleSearch"
                type="text" 
                placeholder="Search for an ingredient (e.g. Retinol, Glycerin)..." 
                class="w-full pl-12 pr-4 py-4 rounded-full border-2 border-brand-gold/30 focus:border-brand-gold focus:ring-4 focus:ring-brand-gold/10 shadow-lg text-lg transition-all"
              >
              <PhMagnifyingGlass :size="24" class="absolute left-4 top-1/2 -translate-y-1/2 text-gray-400" />
          </div>

          <!-- Search Results Dropdown -->
          <div v-if="searchQuery && ingredientStore.searchResults.length > 0" class="absolute top-full left-0 right-0 mt-2 bg-white rounded-xl shadow-xl z-20 overflow-hidden border border-gray-100">
              <div v-for="result in ingredientStore.searchResults" :key="result.id" 
                   @click="openModal(result)"
                   class="p-4 hover:bg-gray-50 cursor-pointer border-b last:border-0 flex justify-between items-center transition-colors">
                  <span class="font-bold text-brand-dark">{{ result.name }}</span>
                  <span :class="getSafetyClass(result.alertType)" class="text-xs px-2 py-1 rounded-full font-bold uppercase tracking-wider">
                      {{ result.alertType }}
                  </span>
              </div>
          </div>
      </div>

      <!-- A-Z Navigation -->
      <div class="flex flex-wrap justify-center gap-2 mb-12">
          <a v-for="letter in alphabet" :key="letter" 
             :href="`#letter-${letter}`"
             class="w-8 h-8 flex items-center justify-center rounded-full text-sm font-bold text-gray-500 hover:text-brand-gold hover:bg-white transition-all">
             {{ letter }}
          </a>
      </div>

      <!-- Ingredient List -->
      <div v-if="ingredientStore.loading && !ingredientStore.ingredients.length" class="text-center py-20">
          <div class="inline-block animate-spin rounded-full h-12 w-12 border-b-2 border-brand-gold"></div>
          <p class="mt-4 text-gray-500">Loading library...</p>
      </div>

      <div v-else class="space-y-12">
          <div v-for="(group, letter) in ingredientStore.groupedByLetter" :key="letter" :id="`letter-${letter}`" class="scroll-mt-24">
              <h2 class="text-6xl font-serif text-brand-gold/20 font-bold mb-6 border-b border-brand-gold/10">{{ letter }}</h2>
              <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                  <div v-for="ing in group" :key="ing.id" 
                       @click="openModal(ing)"
                       class="bg-white p-6 rounded-xl shadow-sm hover:shadow-md transition-all cursor-pointer border border-transparent hover:border-brand-gold/30 group">
                      <div class="flex justify-between items-start mb-2">
                          <h3 class="font-bold text-lg text-brand-dark group-hover:text-brand-gold transition-colors">{{ ing.name }}</h3>
                          <PhInfo :size="20" class="text-gray-300 group-hover:text-brand-gold" />
                      </div>
                      <p class="text-sm text-gray-500 line-clamp-2 mb-3">{{ ing.description }}</p>
                      <div class="flex gap-2">
                          <span class="text-[10px] bg-gray-100 text-gray-600 px-2 py-1 rounded uppercase tracking-wider font-medium">{{ ing.functionality }}</span>
                          <span v-if="ing.safetyRating" :class="getSafetyColor(ing.safetyRating)" class="text-[10px] px-2 py-1 rounded uppercase tracking-wider font-bold">
                              Safety: {{ ing.safetyRating }}/10
                          </span>
                      </div>
                  </div>
              </div>
          </div>
      </div>

      <!-- Detail Modal -->
      <TransitionRoot appear :show="isModalOpen" as="template">
        <Dialog as="div" @close="closeModal" class="relative z-50">
          <TransitionChild as="template" enter="duration-300 ease-out" enter-from="opacity-0" enter-to="opacity-100" leave="duration-200 ease-in" leave-from="opacity-100" leave-to="opacity-0">
            <div class="fixed inset-0 bg-black/60 backdrop-blur-sm" />
          </TransitionChild>

          <div class="fixed inset-0 overflow-y-auto">
            <div class="flex min-h-full items-center justify-center p-4 text-center">
              <TransitionChild as="template" enter="duration-300 ease-out" enter-from="opacity-0 scale-95" enter-to="opacity-100 scale-100" leave="duration-200 ease-in" leave-from="opacity-100 scale-100" leave-to="opacity-0 scale-95">
                <DialogPanel class="w-full max-w-2xl transform overflow-hidden rounded-2xl bg-white p-8 text-left align-middle shadow-2xl transition-all relative">
                  
                  <button @click="closeModal" class="absolute top-4 right-4 p-2 rounded-full hover:bg-gray-100 text-gray-400 hover:text-gray-600">
                      <PhX :size="24" />
                  </button>

                  <div v-if="selectedIngredient" class="space-y-6">
                      <!-- Header -->
                      <div class="border-b pb-4">
                          <div class="flex items-center gap-3 mb-2">
                              <h3 class="text-3xl font-serif text-brand-dark">{{ selectedIngredient.name }}</h3>
                              <span :class="getSafetyClass(selectedIngredient.alertType)" class="px-3 py-1 rounded-full text-xs font-bold uppercase tracking-wider">
                                  {{ selectedIngredient.alertType }}
                              </span>
                          </div>
                          <p class="text-gray-500 italic">{{ selectedIngredient.functionality }}</p>
                      </div>

                      <!-- Ratings Grid -->
                      <div class="grid grid-cols-2 gap-4">
                          <div class="bg-gray-50 p-4 rounded-xl text-center">
                              <p class="text-xs text-gray-500 uppercase tracking-widest mb-1">Safety Rating</p>
                              <div class="text-2xl font-bold" :class="getSafetyTextColor(selectedIngredient.safetyRating)">
                                  {{ selectedIngredient.safetyRating }}<span class="text-sm text-gray-400">/10</span>
                              </div>
                              <p class="text-xs text-gray-400 mt-1">
                                  {{ selectedIngredient.safetyRating <= 2 ? 'Low Hazard' : selectedIngredient.safetyRating <= 6 ? 'Moderate Hazard' : 'High Hazard' }}
                              </p>
                          </div>
                          <div class="bg-gray-50 p-4 rounded-xl text-center">
                              <p class="text-xs text-gray-500 uppercase tracking-widest mb-1">Comedogenic Rating</p>
                              <div class="text-2xl font-bold text-gray-700">
                                  {{ selectedIngredient.comedogenicRating }}<span class="text-sm text-gray-400">/5</span>
                              </div>
                              <p class="text-xs text-gray-400 mt-1">
                                  {{ selectedIngredient.comedogenicRating === 0 ? 'Won\'t Clog Pores' : 'May Clog Pores' }}
                              </p>
                          </div>
                      </div>

                      <!-- Description & Benefits -->
                      <div class="prose prose-sm max-w-none text-gray-600 bg-brand-cream/30 p-6 rounded-xl border border-brand-gold/10">
                          <h4 class="text-brand-dark font-bold mb-2">What is it?</h4>
                          <p class="mb-4">{{ selectedIngredient.description }}</p>
                          
                          <h4 class="text-brand-dark font-bold mb-2">Key Benefits</h4>
                          <p>{{ selectedIngredient.benefits || 'No specific benefits listed yet.' }}</p>
                      </div>
                  </div>

                </DialogPanel>
              </TransitionChild>
            </div>
          </div>
        </Dialog>
      </TransitionRoot>

    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue';
import { useIngredientStore } from '@/stores/ingredient';
import { PhMagnifyingGlass, PhInfo, PhX } from '@phosphor-icons/vue';
import { Dialog, DialogPanel, TransitionRoot, TransitionChild } from '@headlessui/vue';

const ingredientStore = useIngredientStore();
const searchQuery = ref('');
const isModalOpen = ref(false);
const selectedIngredient = ref(null);
const alphabet = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ'.split('');

onMounted(async () => {
    await ingredientStore.fetchAllIngredients();
});

const handleSearch = () => {
    ingredientStore.searchIngredients(searchQuery.value);
};

const openModal = (ingredient) => {
    selectedIngredient.value = ingredient;
    isModalOpen.value = true;
    searchQuery.value = ''; // Clear search on selection
};

const closeModal = () => {
    isModalOpen.value = false;
    setTimeout(() => selectedIngredient.value = null, 300);
};

// Styling Helpers
const getSafetyClass = (type) => {
    switch(type) {
        case 'safe': return 'bg-green-100 text-green-700';
        case 'warning': return 'bg-yellow-100 text-yellow-700';
        case 'danger': return 'bg-red-100 text-red-700';
        default: return 'bg-gray-100 text-gray-700';
    }
};

const getSafetyColor = (rating) => {
     if (rating <= 2) return 'bg-green-50 text-green-600';
     if (rating <= 5) return 'bg-yellow-50 text-yellow-600';
     return 'bg-red-50 text-red-600';
};

const getSafetyTextColor = (rating) => {
     if (rating <= 2) return 'text-green-600';
     if (rating <= 5) return 'text-yellow-600';
     return 'text-red-600';
};
</script>

<style scoped>
html {
    scroll-behavior: smooth;
}
</style>
