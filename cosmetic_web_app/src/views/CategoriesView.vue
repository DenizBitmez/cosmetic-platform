<template>
  <div class="min-h-screen bg-gray-50 pt-8 pb-16">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
      <div class="text-center mb-12">
        <h1 class="text-3xl font-serif text-brand-dark mb-4">Product Categories</h1>
        <p class="text-gray-600 max-w-2xl mx-auto">Explore our curated collection of premium cosmetic products.</p>
      </div>

      <!-- Category Tabs (Existing) -->
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
              <option value="rating">Top Rated</option>
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
                 <img :src="product.image_link" @error="$event.target.src='https://via.placeholder.com/150?text=No+Image'" alt="Product Image" class="object-contain h-full w-full group-hover:scale-105 transition-transform duration-500">
             </div>
             <div>
                 <p class="text-xs text-brand-gold font-bold uppercase mb-1">{{ product.brand || 'Generic' }}</p>
                 <h3 class="text-sm font-bold text-gray-900 mb-1 truncate" :title="product.name">{{ product.name }}</h3>
                 <p class="text-xs text-gray-500 mb-3 truncate">{{ product.category || 'Beauty' }}</p>
                 <div class="flex justify-between items-center">
                     <span class="font-bold text-gray-900">{{ product.price_sign }}{{ product.price || '0.00' }}</span>
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
      <div v-if="selectedProduct" class="fixed inset-0 z-[70] overflow-y-auto" aria-labelledby="modal-title" role="dialog" aria-modal="true">
          <div class="flex items-center justify-center min-h-screen px-4 pt-4 pb-20 text-center sm:p-0">
             <div class="fixed inset-0 bg-gray-900 bg-opacity-80 transition-opacity backdrop-blur-sm" @click="selectedProduct = null"></div>

             <div class="relative inline-block align-bottom bg-white rounded-2xl text-left overflow-hidden shadow-2xl transform transition-all sm:my-8 sm:align-middle sm:max-w-4xl sm:w-full">
                 <button @click="selectedProduct = null" class="absolute top-4 right-4 z-10 bg-white/50 rounded-full p-2 hover:bg-white text-gray-600 transition-colors">
                     <svg class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" /></svg>
                 </button>

                 <div class="grid grid-cols-1 md:grid-cols-2 h-full">
                     <!-- Image Section -->
                     <div class="bg-gray-50 p-8 flex items-center justify-center border-r border-gray-100">
                         <img :src="selectedProduct.image_link" @error="$event.target.src='https://via.placeholder.com/300?text=No+Image'" class="max-h-[400px] object-contain drop-shadow-xl" />
                     </div>

                     <!-- Info Section -->
                     <div class="p-6 md:p-8 flex flex-col h-full overflow-hidden">
                         <!-- Header -->
                         <div class="mb-4">
                            <div class="flex items-center justify-between mb-2">
                                <span class="bg-black text-white text-[10px] font-bold px-2 py-1 rounded uppercase tracking-widest">
                                    {{ selectedProduct.brand || 'Premium' }}
                                </span>
                                <div class="flex items-center gap-1 bg-gray-50 px-2 py-1 rounded border border-gray-100">
                                   <template v-if="selectedProduct.rating">
                                       <span class="text-xs font-bold text-gray-900">{{ selectedProduct.rating }}</span>
                                       <svg class="w-3 h-3 text-yellow-400" fill="currentColor" viewBox="0 0 20 20"><path d="M9.049 2.927c.3-.921 1.603-.921 1.902 0l1.07 3.292a1 1 0 00.95.69h3.462c.969 0 1.371 1.24.588 1.81l-2.8 2.034a1 1 0 00-.364 1.118l1.07 3.292c.3.921-.755 1.688-1.54 1.118l-2.8-2.034a1 1 0 00-1.175 0l-2.8 2.034c-.784.57-1.838-.197-1.539-1.118l1.07-3.292a1 1 0 00-.364-1.118L2.98 8.72c-.783-.57-.38-1.81.588-1.81h3.461a1 1 0 00.951-.69l1.07-3.292z"></path></svg>
                                   </template>
                                   <span v-else class="text-[10px] text-gray-400 font-medium">No Ratings</span>
                                </div>
                            </div>
                            <h2 class="text-2xl font-serif text-gray-900 leading-tight mb-2">{{ selectedProduct.name }}</h2>
                            <span class="text-xl font-bold text-gray-900">{{ selectedProduct.price_sign }}{{ selectedProduct.price || '0.00' }}</span>
                         </div>

                         <!-- Tabs Navigation -->
                         <div class="flex border-b border-gray-100 mb-6">
                            <button @click="activeTab = 'analysis'" :class="['flex-1 pb-3 text-sm font-bold transition-colors border-b-2', activeTab === 'analysis' ? 'border-green-500 text-green-700' : 'border-transparent text-gray-400 hover:text-gray-600']">
                                🌿 Content Analysis
                            </button>
                            <button @click="activeTab = 'overview'" :class="['flex-1 pb-3 text-sm font-bold transition-colors border-b-2', activeTab === 'overview' ? 'border-brand-dark text-brand-dark' : 'border-transparent text-gray-400 hover:text-gray-600']">
                                Product Details
                            </button>
                         </div>

                         <!-- TAB CONTENT: ANALYSIS (Ecomercek Style) -->
                         <div v-show="activeTab === 'analysis'" class="overflow-y-auto pr-2 custom-scrollbar flex-1 pb-20">
                            <!-- Health Score Card -->
                            <div class="bg-gradient-to-br from-green-50 to-white border border-green-100 rounded-2xl p-6 mb-6 flex items-center justify-between relative overflow-hidden">
                                <div class="relative z-10 w-2/3">
                                   <h3 class="text-lg font-bold text-gray-900 mb-1">Clean Beauty Score</h3>
                                   <p class="text-xs text-gray-600 mb-3">Calculated based on ingredient safety, allergy risks, and environmental impact.</p>
                                   <div class="flex gap-2">
                                       <span v-if="calculateScore(selectedProduct) >= 80" class="bg-green-100 text-green-700 text-[10px] font-bold px-2 py-1 rounded">EXCELLENT</span>
                                       <span v-else-if="calculateScore(selectedProduct) >= 50" class="bg-yellow-100 text-yellow-700 text-[10px] font-bold px-2 py-1 rounded">MODERATE</span>
                                       <span v-else class="bg-red-100 text-red-700 text-[10px] font-bold px-2 py-1 rounded">CONCERN</span>
                                   </div>
                                </div>
                                <!-- Score Gauge -->
                                <div class="relative flex items-center justify-center w-20 h-20">
                                    <svg class="transform -rotate-90 w-20 h-20">
                                        <circle cx="40" cy="40" r="36" stroke="currentColor" stroke-width="8" fill="transparent" class="text-gray-200" />
                                        <circle cx="40" cy="40" r="36" stroke="currentColor" stroke-width="8" fill="transparent" 
                                            :class="calculateScore(selectedProduct) >= 50 ? 'text-green-500' : 'text-orange-500'" 
                                            :stroke-dasharray="226" 
                                            :stroke-dashoffset="226 - (226 * calculateScore(selectedProduct) / 100)" 
                                            stroke-linecap="round" />
                                    </svg>
                                    <span class="absolute text-xl font-bold text-gray-800">{{ calculateScore(selectedProduct) }}</span>
                                </div>
                            </div>
                            
                            <!-- Detailed Breakdown -->
                            <h4 class="text-sm font-bold text-gray-900 mb-3 flex items-center gap-2">
                                <span class="w-1.5 h-1.5 rounded-full bg-green-500"></span> Positive Ingredients
                            </h4>
                            <div class="space-y-3 mb-6">
                                <div v-for="(tag, index) in getAnalysis(selectedProduct).good" :key="index" class="flex items-start gap-3 p-3 bg-white border border-gray-100 rounded-xl shadow-sm hover:shadow-md transition-shadow">
                                    <div class="bg-green-100 p-2 rounded-full text-green-600 shrink-0">
                                        <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z"></path></svg>
                                    </div>
                                    <div>
                                        <p class="text-sm font-bold text-gray-800 capitalize">{{ tag }}</p>
                                        <p class="text-[11px] text-gray-500 leading-tight mt-0.5">Identified as a safe and beneficial component for skin health.</p>
                                    </div>
                                </div>
                                <div v-if="getAnalysis(selectedProduct).good.length === 0" class="text-center p-4 bg-gray-50 rounded-xl">
                                    <p class="text-xs text-gray-400">Standard formulation detected. No specific 'Clean Beauty' tags found.</p>
                                </div>
                            </div>

                             <h4 class="text-sm font-bold text-gray-900 mb-3 flex items-center gap-2">
                                <span class="w-1.5 h-1.5 rounded-full bg-blue-500"></span> Other Features
                            </h4>
                            <div class="flex flex-wrap gap-2 mb-6">
                                 <span v-for="tag in getAnalysis(selectedProduct).features" :key="tag" class="px-3 py-1 bg-gray-50 border border-gray-200 text-gray-600 text-xs font-medium rounded-full flex items-center gap-1">
                                    <svg class="w-3 h-3 text-blue-400" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 16h-1v-4h-1m1-4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z"></path></svg>
                                    {{ tag }}
                                 </span>
                            </div>
                         </div>

                         <!-- TAB CONTENT: OVERVIEW -->
                         <div v-show="activeTab === 'overview'" class="overflow-y-auto pr-2 custom-scrollbar flex-1 pb-20">
                             <div class="mb-6">
                                <h3 class="text-sm font-bold text-gray-900 mb-2">Description</h3>
                                <div class="text-xs text-gray-600 leading-relaxed space-y-2" v-html="selectedProduct.description || 'No detailed description available.'"></div>
                             </div>
                             
                             <!-- Colors -->
                             <div v-if="selectedProduct.product_colors && selectedProduct.product_colors.length > 0">
                                <h3 class="text-sm font-bold text-gray-900 mb-3">Available Shades ({{ selectedProduct.product_colors.length }})</h3>
                                <div class="grid grid-cols-4 gap-2">
                                     <div v-for="color in selectedProduct.product_colors.slice(0, 12)" :key="color.hex_value" class="flex flex-col items-center gap-1">
                                         <div class="w-8 h-8 rounded-full border border-gray-200 shadow-sm" :style="{ backgroundColor: color.hex_value }"></div>
                                         <span class="text-[10px] text-gray-500 truncate w-full text-center" :title="color.colour_name">{{ color.colour_name }}</span>
                                     </div>
                                </div>
                             </div>
                         </div>
                         
                         <!-- Sticky Footer (Buttons) -->
                         <div class="absolute bottom-0 left-0 right-0 p-6 md:p-8 bg-white border-t border-gray-100 flex gap-3">
                             <a :href="selectedProduct.product_link" target="_blank" class="flex-1 bg-black text-white text-center py-3 rounded-lg text-sm font-bold hover:bg-gray-800 transition-colors shadow-lg flex items-center justify-center gap-2">
                                 <span>Buy Now</span>
                                 <svg class="w-4 h-4 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M10 6H6a2 2 0 00-2 2v10a2 2 0 002 2h10a2 2 0 002-2v-4M14 4h6m0 0v6m0-6L10 14"></path></svg>
                             </a>
                             <button class="px-4 py-3 border border-gray-200 rounded-lg hover:bg-gray-50 text-gray-600 transition-colors">
                                 <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4.318 6.318a4.5 4.5 0 000 6.364L12 20.364l7.682-7.682a4.5 4.5 0 00-6.364-6.364L12 7.636l-1.318-1.318a4.5 4.5 0 00-6.364 0z"></path></svg>
                             </button>
                         </div>

                     </div>
                 </div>
             </div>
          </div>
      </div>

    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue';
import axios from 'axios';

const categories = ['lipstick', 'mascara', 'foundation', 'eyeliner', 'eyeshadow', 'blush', 'bronzer', 'eyebrow'];
const selectedCategory = ref('lipstick');
const products = ref([]);
const loading = ref(false);
const selectedProduct = ref(null);
const searchQuery = ref('');
const sortBy = ref('featured');
const activeTab = ref('analysis');

const fetchProducts = async (category) => {
    selectedCategory.value = category;
    loading.value = true;
    products.value = [];
    try {
        const response = await fetch(`http://makeup-api.herokuapp.com/api/v1/products.json?product_type=${category}`);
        const data = await response.json();
        // Filter out items without images strictly
        products.value = data.filter(p => p.image_link && p.price && parseFloat(p.price) > 0).slice(0, 40); 
    } catch (e) {
        console.error("Failed to fetch products", e);
    } finally {
        loading.value = false;
    }
};

const filteredProducts = computed(() => {
    let result = products.value;

    if (searchQuery.value) {
        const query = searchQuery.value.toLowerCase();
        result = result.filter(p => 
            p.name.toLowerCase().includes(query) || 
            (p.brand && p.brand.toLowerCase().includes(query))
        );
    }

    if (sortBy.value === 'price-low') {
        result = [...result].sort((a, b) => parseFloat(a.price) - parseFloat(b.price));
    } else if (sortBy.value === 'price-high') {
        result = [...result].sort((a, b) => parseFloat(b.price) - parseFloat(a.price));
    } else if (sortBy.value === 'rating') {
        result = [...result].sort((a, b) => (b.rating || 0) - (a.rating || 0));
    }

    return result;
});

const openProduct = (product) => {
    selectedProduct.value = product;
    activeTab.value = 'analysis';
};

// Analysis Logic
const calculateScore = (product) => {
    let score = 75; // Base score
    const tags = product.tag_list || [];
    if (tags.includes('Vegan')) score += 5;
    if (tags.includes('Organic')) score += 5;
    if (tags.includes('Gluten Free')) score += 3;
    if (tags.includes('Natural')) score += 5;
    if (tags.includes('EWG Verified')) score += 10;
    return Math.min(score, 100);
};

const capitalize = (s) => s.charAt(0).toUpperCase() + s.slice(1);

const getAnalysis = (product) => {
    const tags = product.tag_list || [];
    const good = tags.filter(t => ['Vegan', 'Organic', 'Natural', 'Gluten Free', 'EWG Verified', 'Hypoallergenic'].includes(t));
    const features = tags.filter(t => !['Vegan', 'Organic', 'Natural', 'Gluten Free', 'EWG Verified', 'Hypoallergenic'].includes(t)).slice(0, 4);
    
    // Capitalize for display
    return {
        good: good.map(t => t.replace(/_/g, ' ').replace(/\b\w/g, l => l.toUpperCase())),
        features: features.map(t => t.replace(/_/g, ' ').replace(/\b\w/g, l => l.toUpperCase()))
    };
};

onMounted(() => {
    fetchProducts(selectedCategory.value);
});
</script>
