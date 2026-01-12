<template>
  <transition name="fade">
    <div v-if="show" class="fixed inset-0 z-[80] overflow-y-auto" @click.self="closeModal">
      <div class="flex items-center justify-center min-h-screen px-4">
        <div class="fixed inset-0 bg-black/50 backdrop-blur-sm"></div>
        
        <div class="relative bg-white rounded-2xl shadow-2xl max-w-md w-full p-6 animate-scale-in">
          <!-- Header -->
          <div class="flex items-center justify-between mb-4">
            <h3 class="text-xl font-bold text-gray-900">Set Price Alert</h3>
            <button @click="closeModal" class="p-1 text-gray-400 hover:text-gray-600 transition-colors">
              <PhX :size="24" />
            </button>
          </div>

          <!-- Product Info -->
          <div class="flex gap-3 mb-6 p-3 bg-gray-50 rounded-lg">
            <div class="h-16 w-16 bg-gray-200 rounded-lg bg-cover bg-center flex-shrink-0" :style="{ backgroundImage: 'url(' + (product.image || product.api_featured_image || 'https://images.unsplash.com/photo-1620916566398-39f1143ab7be?w=400') + ')' }"></div>
            <div class="flex-1 min-w-0">
              <h4 class="font-semibold text-sm text-gray-900 line-clamp-2">{{ product.name }}</h4>
              <p class="text-xs text-gray-500 mt-1">{{ product.brand || product.category }}</p>
              <p class="text-lg font-bold text-brand-gold mt-1">${{ typeof product.price === 'number' ? product.price.toFixed(2) : parseFloat(product.price || 0).toFixed(2) }}</p>
            </div>
          </div>

          <!-- Form -->
          <form @submit.prevent="handleSubmit">
            <div class="mb-6">
              <label class="block text-sm font-medium text-gray-700 mb-2">
                Target Price
              </label>
              <div class="relative">
                <span class="absolute left-3 top-1/2 -translate-y-1/2 text-gray-500">$</span>
                <input
                  v-model.number="targetPrice"
                  type="number"
                  step="0.01"
                  min="0.01"
                  :max="product.price - 0.01"
                  required
                  class="w-full pl-8 pr-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-brand-gold focus:border-transparent"
                  placeholder="0.00"
                />
              </div>
              <p class="text-xs text-gray-500 mt-2">
                Enter a price lower than the current price (${{ typeof product.price === 'number' ? product.price.toFixed(2) : parseFloat(product.price || 0).toFixed(2) }})
              </p>
            </div>

            <!-- Savings Preview -->
            <div v-if="targetPrice && targetPrice < product.price" class="mb-6 p-4 bg-green-50 border border-green-200 rounded-lg">
              <div class="flex items-center gap-2 text-green-700">
                <PhBell :size="20" weight="fill" />
                <span class="text-sm font-medium">You'll save ${{ (product.price - targetPrice).toFixed(2) }} ({{ savingsPercentage }}%)</span>
              </div>
              <p class="text-xs text-green-600 mt-1">We'll notify you when the price drops to ${{ targetPrice?.toFixed(2) }} or below</p>
            </div>

            <!-- Error Message -->
            <div v-if="errorMessage" class="mb-4 p-3 bg-red-50 border border-red-200 rounded-lg">
              <p class="text-sm text-red-700">{{ errorMessage }}</p>
            </div>

            <!-- Actions -->
            <div class="flex gap-3">
              <button type="button" @click="closeModal" class="flex-1 px-4 py-3 border border-gray-300 rounded-lg text-sm font-medium text-gray-700 hover:bg-gray-50 transition-colors">
                Cancel
              </button>
              <button type="submit" :disabled="submitting || !targetPrice || targetPrice >= product.price" class="flex-1 px-4 py-3 bg-brand-dark text-white rounded-lg text-sm font-medium hover:bg-black transition-colors disabled:opacity-50 disabled:cursor-not-allowed">
                {{ submitting ? 'Creating...' : 'Create Alert' }}
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  </transition>
</template>

<script setup>
import { ref, computed } from 'vue';
import { usePriceAlertStore } from '@/stores/priceAlert';
import { useUiStore } from '@/stores/ui';
import { PhX, PhBell } from '@phosphor-icons/vue';

const props = defineProps({
  show: {
    type: Boolean,
    required: true
  },
  product: {
    type: Object,
    required: true
  }
});

const emit = defineEmits(['close', 'created']);

const priceAlertStore = usePriceAlertStore();
const uiStore = useUiStore();

const targetPrice = ref(null);
const submitting = ref(false);
const errorMessage = ref('');

const savingsPercentage = computed(() => {
  if (!targetPrice.value || !props.product.price) return 0;
  return Math.round(((props.product.price - targetPrice.value) / props.product.price) * 100);
});

const closeModal = () => {
  targetPrice.value = null;
  errorMessage.value = '';
  emit('close');
};

const handleSubmit = async () => {
  errorMessage.value = '';
  
  if (!targetPrice.value || targetPrice.value >= props.product.price) {
    errorMessage.value = 'Target price must be lower than current price';
    return;
  }
  
  submitting.value = true;
  
  const productId = typeof props.product.id === 'string' ? parseInt(props.product.id) : props.product.id;
  
  const result = await priceAlertStore.createAlert(productId, targetPrice.value, props.product);
  
  submitting.value = false;
  
  if (result.success) {
    uiStore.notify(result.message, 'success');
    emit('created');
    closeModal();
  } else {
    errorMessage.value = result.message;
  }
};
</script>

<style scoped>
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}

.animate-scale-in {
  animation: scaleIn 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

@keyframes scaleIn {
  from {
    transform: scale(0.95);
    opacity: 0;
  }
  to {
    transform: scale(1);
    opacity: 1;
  }
}
</style>
