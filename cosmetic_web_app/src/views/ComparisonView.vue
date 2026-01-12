<template>
  <div class="min-h-screen bg-gray-50 py-8">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
      <!-- Header -->
      <div class="mb-8">
        <div class="flex justify-between items-center">
          <div>
            <h1 class="text-3xl font-bold text-gray-900">Product Comparison</h1>
            <p class="mt-2 text-sm text-gray-600">Compare up to {{ comparisonStore.maxItems }} products side by side</p>
          </div>
          <div class="flex gap-3">
            <router-link to="/categories" class="px-4 py-2 text-sm font-medium text-gray-700 bg-white border border-gray-300 rounded-lg hover:bg-gray-50 transition-colors">
              Add More Products
            </router-link>
            <button v-if="comparisonStore.itemCount > 0" @click="clearAll" class="px-4 py-2 text-sm font-medium text-white bg-red-600 rounded-lg hover:bg-red-700 transition-colors">
              Clear All
            </button>
          </div>
        </div>
      </div>

      <!-- Empty State -->
      <div v-if="comparisonStore.itemCount === 0" class="text-center py-20 bg-white rounded-2xl border-2 border-dashed border-gray-200">
        <PhScales class="mx-auto h-16 w-16 text-gray-300 mb-4" />
        <h3 class="text-lg font-serif text-gray-800">No Products to Compare</h3>
        <p class="text-sm text-gray-500 max-w-xs mx-auto mt-2">Start adding products from the categories page to compare them side by side.</p>
        <router-link to="/categories" class="inline-block mt-4 text-sm bg-brand-dark text-white px-6 py-2 rounded-lg hover:bg-black transition-colors">
          Browse Products
        </router-link>
      </div>

      <!-- Comparison Table -->
      <div v-else class="bg-white rounded-xl shadow-lg overflow-hidden">
        <div class="overflow-x-auto">
          <table class="w-full min-w-[800px] table-fixed">
            <colgroup>
              <col style="width: 200px">
              <col v-for="product in comparisonStore.products" :key="product.id" :style="{ width: `${(100 - 200/8)}%` }">
            </colgroup>
            <thead class="bg-gray-50 border-b border-gray-200">
              <tr>
                <th class="sticky left-0 z-10 bg-gray-50 px-6 py-4 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Feature
                </th>
                <th v-for="product in products" :key="product.id" class="px-6 py-4 text-center min-w-[250px]">
                  <div class="relative">
                    <button @click="removeProduct(product.id)" class="absolute top-0 right-0 p-1 text-gray-400 hover:text-red-500 transition-colors">
                      <PhX :size="20" />
                    </button>
                    <div class="h-32 w-32 mx-auto bg-gray-100 rounded-lg bg-cover bg-center mb-3" :style="{ backgroundImage: 'url(' + (product.image || 'https://images.unsplash.com/photo-1620916566398-39f1143ab7be?w=400') + ')' }"></div>
                    <h3 class="font-bold text-sm text-gray-900 line-clamp-2">{{ product.name }}</h3>
                    <p class="text-xs text-gray-500 mt-1">{{ product.brand || product.category }}</p>
                  </div>
                </th>
              </tr>
            </thead>
            <tbody class="divide-y divide-gray-200">
              <!-- Price -->
              <tr class="hover:bg-gray-50">
                <td class="sticky left-0 z-10 bg-white px-6 py-4 text-sm font-medium text-gray-900">
                  Price
                </td>
                <td v-for="product in products" :key="product.id" class="px-6 py-4 text-center">
                  <span class="text-lg font-bold text-brand-gold">${{ typeof product.price === 'number' ? product.price.toFixed(2) : parseFloat(product.price || 0).toFixed(2) }}</span>
                </td>
              </tr>

              <!-- Rating -->
              <tr class="hover:bg-gray-50">
                <td class="sticky left-0 z-10 bg-white px-6 py-4 text-sm font-medium text-gray-900">
                  Rating
                </td>
                <td v-for="product in products" :key="product.id" class="px-6 py-4 text-center">
                  <div class="flex items-center justify-center gap-1">
                    <PhStar :size="16" weight="fill" class="text-yellow-400" />
                    <span class="text-sm font-medium">{{ product.rating || 'N/A' }}</span>
                  </div>
                </td>
              </tr>

              <!-- Category -->
              <tr class="hover:bg-gray-50">
                <td class="sticky left-0 z-10 bg-white px-6 py-4 text-sm font-medium text-gray-900">
                  Category
                </td>
                <td v-for="product in products" :key="product.id" class="px-6 py-4 text-center">
                  <span class="text-sm text-gray-700">{{ product.category || 'N/A' }}</span>
                </td>
              </tr>

              <!-- Brand -->
              <tr class="hover:bg-gray-50">
                <td class="sticky left-0 z-10 bg-white px-6 py-4 text-sm font-medium text-gray-900">
                  Brand
                </td>
                <td v-for="product in products" :key="product.id" class="px-6 py-4 text-center">
                  <span class="text-sm text-gray-700">{{ product.brand || 'N/A' }}</span>
                </td>
              </tr>

              <!-- Stock -->
              <tr class="hover:bg-gray-50">
                <td class="sticky left-0 z-10 bg-white px-6 py-4 text-sm font-medium text-gray-900">
                  Availability
                </td>
                <td v-for="product in products" :key="product.id" class="px-6 py-4 text-center">
                  <span v-if="product.stock > 0" class="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium bg-green-100 text-green-800">
                    In Stock ({{ product.stock }})
                  </span>
                  <span v-else class="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium bg-red-100 text-red-800">
                    Out of Stock
                  </span>
                </td>
              </tr>

              <!-- PAO -->
              <tr class="hover:bg-gray-50">
                <td class="sticky left-0 z-10 bg-white px-6 py-4 text-sm font-medium text-gray-900">
                  Period After Opening
                </td>
                <td v-for="product in products" :key="product.id" class="px-6 py-4 text-center">
                  <span class="text-sm text-gray-700">{{ product.paoMonths ? product.paoMonths + ' months' : 'N/A' }}</span>
                </td>
              </tr>

              <!-- Ingredients Count -->
              <tr class="hover:bg-gray-50">
                <td class="sticky left-0 z-10 bg-white px-6 py-4 text-sm font-medium text-gray-900">
                  Ingredients
                </td>
                <td v-for="product in products" :key="product.id" class="px-6 py-4 text-center">
                  <span class="text-sm text-gray-700">{{ product.ingredients?.length || 0 }} ingredients</span>
                </td>
              </tr>

              <!-- Description -->
              <tr class="hover:bg-gray-50">
                <td class="sticky left-0 z-10 bg-white px-6 py-4 text-sm font-medium text-gray-900">
                  Description
                </td>
                <td v-for="product in products" :key="product.id" class="px-6 py-4">
                  <p class="text-xs text-gray-600 line-clamp-3">{{ product.description || 'No description available' }}</p>
                </td>
              </tr>

              <!-- Actions -->
              <tr class="bg-gray-50">
                <td class="sticky left-0 z-10 bg-gray-50 px-6 py-4 text-sm font-medium text-gray-900">
                  Actions
                </td>
                <td v-for="product in products" :key="product.id" class="px-6 py-4 text-center">
                  <div class="flex flex-col gap-2">
                    <button @click="addToCart(product)" class="w-full bg-brand-dark text-white px-4 py-2 rounded-lg text-sm font-medium hover:bg-black transition-colors">
                      Add to Cart
                    </button>
                    <button @click="viewDetails(product)" class="w-full bg-white text-brand-dark border border-brand-dark px-4 py-2 rounded-lg text-sm font-medium hover:bg-brand-cream transition-colors">
                      View Details
                    </button>
                  </div>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue';
import { useRouter } from 'vue-router';
import { useComparisonStore } from '@/stores/comparison';
import { useCartStore } from '@/stores/cart';
import { useUiStore } from '@/stores/ui';
import { PhScales, PhX, PhStar } from '@phosphor-icons/vue';

const router = useRouter();
const comparisonStore = useComparisonStore();
const cartStore = useCartStore();
const uiStore = useUiStore();

const products = computed(() => comparisonStore.getComparisonProducts);

const removeProduct = (productId) => {
    comparisonStore.removeFromComparison(productId);
};

const clearAll = async () => {
    const confirmed = await uiStore.confirm('Clear Comparison', 'Are you sure you want to remove all products from comparison?');
    if (confirmed) {
        comparisonStore.clearComparison();
    }
};

const addToCart = async (product) => {
    const success = await cartStore.addToCart(product.id, 1, product);
    if (success) {
        uiStore.notify("Product added to cart! 🛍️");
        cartStore.toggleDrawer(true);
    } else {
        uiStore.notify("Failed to add to cart", 'error');
    }
};

const viewDetails = (product) => {
    // Navigate to categories page and trigger product modal
    router.push({ 
        path: '/categories', 
        query: { productId: product.id }
    });
};
</script>
