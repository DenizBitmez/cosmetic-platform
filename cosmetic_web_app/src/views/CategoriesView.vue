<template>
  <div class="min-h-screen bg-gray-50 pt-8 pb-16">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
      <div class="text-center mb-12">
        <h1 class="text-3xl font-serif text-brand-dark mb-4">Product Categories</h1>
        <p class="text-gray-600 max-w-2xl mx-auto">Explore our curated collection of premium cosmetic products.</p>
      </div>

      <!-- Category Tabs -->
      <div class="flex overflow-x-auto pb-4 gap-4 mb-8 scrollbar-hide">
          <button v-for="category in categories" :key="category"
              @click="fetchProducts(category)"
              :class="['px-6 py-2 rounded-full whitespace-nowrap text-sm font-bold transition-all',
              selectedCategory === category ? 'bg-black text-white shadow-lg' : 'bg-white text-gray-600 border hover:bg-gray-50']">
              {{ category.charAt(0).toUpperCase() + category.slice(1) }}
          </button>
      </div>

      <!-- Search and Filter Bar -->
      <div class="flex flex-col sm:flex-row gap-4 mb-8">
          <div class="relative flex-grow">
              <span class="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                  <svg class="h-5 w-5 text-gray-400" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                      <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z" />
                  </svg>
              </span>
              <input v-model="searchQuery" type="text" placeholder="Search products, brands..." 
                  class="w-full pl-10 pr-4 py-2 border border-gray-200 rounded-lg focus:outline-none focus:ring-2 focus:ring-black focus:border-transparent transition-shadow">
          </div>
          <select v-model="sortBy" class="px-4 py-2 border border-gray-200 rounded-lg focus:outline-none focus:ring-2 focus:ring-black bg-white cursor-pointer">
              <option value="featured">Featured</option>
              <option value="price-low">Price: Low to High</option>
              <option value="price-high">Price: High to Low</option>
          </select>
      </div>

      <!-- Loading State -->
      <div v-if="loading" class="text-center py-20">
          <div class="animate-spin rounded-full h-12 w-12 border-b-2 border-brand-gold mx-auto"></div>
          <p class="mt-4 text-gray-500">Loading premium products...</p>
      </div>

      <!-- Product Grid -->
      <div v-else class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-6">
         <div v-for="product in filteredProducts" :key="product.id" @click="openProduct(product)" class="bg-white border rounded-lg p-4 group hover:shadow-lg transition-all duration-300 cursor-pointer">
             <div class="h-48 relative mb-4 overflow-hidden rounded-md bg-white flex items-center justify-center">
                 <img :src="product.image" @error="handleImageError(product)" alt="Product Image" class="object-contain h-full w-full group-hover:scale-105 transition-transform duration-500">
             </div>
             <div>
                 <p class="text-xs text-brand-gold font-bold uppercase mb-1">{{ product.brand || 'Generic' }}</p>
                 <h3 class="text-sm font-bold text-gray-900 mb-1 truncate" :title="product.name">{{ product.name }}</h3>
                 <p class="text-xs text-gray-500 mb-3 truncate">{{ product.category || 'Beauty' }}</p>
                 <div class="flex justify-between items-center">
                     <span class="font-bold text-gray-900">${{ product.price || '0.00' }}</span>
                     <button class="text-xs bg-brand-dark text-white px-3 py-1.5 rounded hover:bg-black transition-colors">View Analysis</button>
                 </div>
             </div>
         </div>
      </div>
      
      <!-- Empty State -->
      <div v-if="!loading && filteredProducts.length === 0" class="text-center py-20 text-gray-500">
          No products found matching your criteria.
      </div>

      <!-- PRODUCT DETAIL MODAL -->
      <ProductDetailModal 
          :product="selectedProduct" 
          @close="selectedProduct = null"
      />

    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed, watch } from 'vue';
import { useProductStore } from '@/stores/product';
import ProductDetailModal from '@/components/ProductDetailModal.vue';

const productStore = useProductStore();

const categories = ['serum', 'cleanser', 'toner', 'cream', 'lipstick', 'mascara', 'foundation', 'eyeliner', 'eyeshadow', 'blush', 'bronzer', 'eyebrow', 'lip_liner', 'nail_polish'];
const selectedCategory = ref('serum'); // Default category is now Serum to show new items
const products = computed(() => productStore.categoriesCache[selectedCategory.value] || []);
const loading = computed(() => productStore.loading);
const selectedProduct = ref(null);
const searchQuery = ref('');
const sortBy = ref('featured');
const activeTab = ref('overview'); // Default tab changed to overview

const fetchProducts = async (category) => {
    selectedCategory.value = category;
    try {
        await productStore.fetchByCategory(category);
    } catch (e) {
        console.error("Failed to fetch products", e);
        // Fallback or error handled by store/view
    }
};

const handleImageError = (productToRemove) => {
    if (productStore.categoriesCache[selectedCategory.value]) {
        productStore.categoriesCache[selectedCategory.value] = 
            productStore.categoriesCache[selectedCategory.value].filter(p => p.id !== productToRemove.id);
    }
};

const filteredProducts = computed(() => {
    let result = products.value;

    if (searchQuery.value) {
        const query = searchQuery.value.toLowerCase();
        result = result.filter(p => 
            (p.name && p.name.toLowerCase().includes(query)) || 
            (p.brand && p.brand.toLowerCase().includes(query))
        );
    }

    if (sortBy.value === 'price-low') {
        result = [...result].sort((a, b) => parseFloat(a.price) - parseFloat(b.price));
    } else if (sortBy.value === 'price-high') {
        result = [...result].sort((a, b) => parseFloat(b.price) - parseFloat(a.price));
    } 

    return result;
});

const openProduct = (product) => {
    selectedProduct.value = product;
};

watch(() => selectedCategory.value, (newCat) => {
    // Watcher logic if needed
});

onMounted(() => {
    fetchProducts(selectedCategory.value);
});
</script>
