<template>
  <Teleport to="body">
    <div v-if="isOpen" class="fixed inset-0 z-[60] overflow-y-auto" aria-labelledby="modal-title" role="dialog" aria-modal="true">
      <div class="flex items-end justify-center min-h-screen pt-4 px-4 pb-20 text-center sm:block sm:p-0">
        
        <!-- Backdrop -->
        <div class="fixed inset-0 bg-gray-500 bg-opacity-75 transition-opacity" aria-hidden="true" @click="$emit('close')"></div>
  
        <span class="hidden sm:inline-block sm:align-middle sm:h-screen" aria-hidden="true">&#8203;</span>
  
        <div class="inline-block align-bottom bg-white rounded-lg text-left overflow-hidden shadow-xl transform transition-all sm:my-8 sm:align-middle sm:max-w-2xl sm:w-full relative z-10">
          <div class="bg-white px-4 pt-5 pb-4 sm:p-6 sm:pb-4">
            <div class="sm:flex sm:items-start">
              <div class="mt-3 text-center sm:mt-0 sm:ml-4 sm:text-left w-full">
                <h3 class="text-lg leading-6 font-medium text-gray-900 font-serif mb-4" id="modal-title">
                  Add Product to Routine
                </h3>
                
                <!-- Search Input -->
                <div class="relative mb-6">
                  <input 
                      v-model="searchQuery" 
                      @input="handleSearch"
                      type="text" 
                      placeholder="Search for products..." 
                      class="w-full border border-gray-300 rounded-lg pl-10 pr-4 py-3 focus:ring-2 focus:ring-brand-gold focus:border-brand-gold outline-none"
                      autofocus
                  >
                  <div class="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                      <svg class="h-5 w-5 text-gray-400" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20" fill="currentColor">
                          <path fill-rule="evenodd" d="M8 4a4 4 0 100 8 4 4 0 000-8zM2 8a6 6 0 1110.89 3.476l4.817 4.817a1 1 0 01-1.414 1.414l-4.816-4.816A6 6 0 012 8z" clip-rule="evenodd" />
                      </svg>
                  </div>
                </div>
  
                <!-- Results -->
                 <div class="bg-gray-50 rounded-lg min-h-[300px] max-h-[400px] overflow-y-auto p-4">
                      <div v-if="loading" class="flex justify-center py-8">
                          <div class="animate-spin rounded-full h-8 w-8 border-b-2 border-brand-gold"></div>
                      </div>
                      
                      <div v-else-if="products.length === 0" class="text-center py-8 text-gray-500">
                          {{ searchQuery ? 'No products found.' : 'Start typing to search products.' }}
                      </div>
  
                      <div v-else class="space-y-3">
                          <div v-for="product in products" :key="product.id" class="bg-white p-3 rounded-lg border border-gray-200 hover:border-brand-gold transition-colors flex items-center gap-4 group">
                               <div class="w-12 h-12 bg-gray-100 rounded-md flex-shrink-0 overflow-hidden">
                                  <img :src="product.image || 'https://images.unsplash.com/photo-1620916566398-39f1143ab7be?q=80&w=200'" class="w-full h-full object-cover">
                               </div>
                               <div class="flex-1 min-w-0">
                                   <h4 class="text-sm font-medium text-gray-900 truncate">{{ product.name }}</h4>
                                   <p class="text-xs text-brand-gold">{{ product.category || 'Beauty' }}</p>
                               </div>
                               <button @click="selectProduct(product)" class="bg-gray-100 text-gray-600 hover:bg-brand-dark hover:text-white px-3 py-1.5 rounded text-xs font-bold uppercase transition-colors">
                                  Add
                               </button>
                          </div>
                      </div>
                 </div>
              </div>
            </div>
          </div>
          <div class="bg-gray-50 px-4 py-3 sm:px-6 sm:flex sm:flex-row-reverse">
            <button type="button" class="mt-3 w-full inline-flex justify-center rounded-md border border-gray-300 shadow-sm px-4 py-2 bg-white text-base font-medium text-gray-700 hover:bg-gray-50 focus:outline-none sm:mt-0 sm:ml-3 sm:w-auto sm:text-sm" @click="$emit('close')">
              Cancel
            </button>
          </div>
        </div>
      </div>
    </div>
  </Teleport>
</template>

<script setup>
import { ref, watch } from 'vue';
import api from '@/services/api'; // Direct API ensuring search capability
// We can assume api is configured with axios

const props = defineProps({
  isOpen: Boolean
});

const emit = defineEmits(['close', 'select']);

const searchQuery = ref('');
const products = ref([]);
const loading = ref(false);
let debounceTimeout = null;

const handleSearch = () => {
    if (debounceTimeout) clearTimeout(debounceTimeout);
    
    debounceTimeout = setTimeout(async () => {
        if (searchQuery.value.length < 2) {
            products.value = [];
            return;
        }
        
        loading.value = true;
        console.log('Searching for:', searchQuery.value);
        try {
            const res = await api.get('/product/search', {
                params: { name: searchQuery.value }
            });
            console.log('Search results:', res.data);
            products.value = Array.isArray(res.data) ? res.data : [];
        } catch (e) {
            console.error("Search failed:", e);
            if (e.response) {
                 console.error("Response data:", e.response.data);
                 console.error("Response status:", e.response.status);
            }
            products.value = [];
        } finally {
            loading.value = false;
        }
    }, 300);
};

const selectProduct = (product) => {
    emit('select', product);
    emit('close');
};

watch(() => props.isOpen, (newVal) => {
    if (newVal) {
        searchQuery.value = '';
        products.value = [];
        // Optional: Load popular products initially?
    }
});
</script>
