<template>
  <div class="min-h-screen bg-brand-cream py-12 px-4 sm:px-6 lg:px-8">
    <div class="max-w-7xl mx-auto">
      <h1 class="text-3xl font-serif text-brand-dark mb-8">Checkout</h1>

      <div class="lg:grid lg:grid-cols-12 lg:gap-x-12 lg:items-start">
        <!-- Left Column: Address Selection -->
        <section class="lg:col-span-7 bg-white p-8 shadow-sm">
          <h2 class="text-xl font-medium text-gray-900 mb-6 font-serif">Shipping Address</h2>
          
          <div v-if="addressStore.loading" class="text-gray-500">Loading addresses...</div>
          
          <div v-else class="space-y-4">
             <!-- Address List -->
             <div v-for="address in addressStore.addresses" :key="address.id" 
                  class="relative flex items-center p-4 border rounded-sm cursor-pointer transition-colors"
                  :class="selectedAddressId === address.id ? 'border-brand-gold bg-yellow-50/50' : 'border-gray-200 hover:border-gray-300'"
                  @click="selectedAddressId = address.id">
                  
                  <div class="min-w-0 flex-1 text-sm">
                    <div class="font-medium text-gray-900 block">{{ address.title }}</div>
                    <div class="text-gray-500 mt-1 block">{{ address.fullAddress }}</div>
                    <div class="text-gray-500 block">{{ address.district }}, {{ address.city }}</div>
                  </div>
                  <div class="ml-4 h-5 w-5 flex items-center justify-center border-gray-300">
                     <div v-if="selectedAddressId === address.id" class="h-2.5 w-2.5 rounded-full bg-brand-gold"></div>
                  </div>
                  <button @click.stop="addressStore.deleteAddress(address.id)" class="ml-4 text-xs text-red-500 hover:text-red-700 underline">Delete</button>
             </div>

             <!-- Add Address Form Toggle -->
             <button @click="showAddForm = !showAddForm" class="mt-4 flex items-center text-sm text-brand-dark hover:text-brand-gold font-medium">
                <span v-if="!showAddForm">+ Add New Address</span>
                <span v-else>- Cancel</span>
             </button>

             <!-- Add Address Form -->
             <form v-if="showAddForm" @submit.prevent="handleAddressSubmit" class="mt-6 grid grid-cols-1 gap-y-6 sm:grid-cols-2 sm:gap-x-4 border-t border-gray-100 pt-6">
                 <div class="sm:col-span-2">
                   <label class="block text-sm font-medium text-gray-700">Address Title (e.g. Home)</label>
                   <input v-model="newAddress.title" type="text" required class="mt-1 block w-full border-gray-300 rounded-none shadow-sm focus:ring-brand-gold focus:border-brand-gold sm:text-sm p-3 border">
                 </div>
                 
                 <div>
                   <label class="block text-sm font-medium text-gray-700">City</label>
                   <input v-model="newAddress.city" type="text" required class="mt-1 block w-full border-gray-300 rounded-none shadow-sm focus:ring-brand-gold focus:border-brand-gold sm:text-sm p-3 border">
                 </div>
                 
                 <div>
                   <label class="block text-sm font-medium text-gray-700">District</label>
                   <input v-model="newAddress.district" type="text" required class="mt-1 block w-full border-gray-300 rounded-none shadow-sm focus:ring-brand-gold focus:border-brand-gold sm:text-sm p-3 border">
                 </div>

                 <div class="sm:col-span-2">
                   <label class="block text-sm font-medium text-gray-700">Full Address</label>
                   <textarea v-model="newAddress.fullAddress" rows="3" required class="mt-1 block w-full border-gray-300 rounded-none shadow-sm focus:ring-brand-gold focus:border-brand-gold sm:text-sm p-3 border"></textarea>
                 </div>

                 <div class="sm:col-span-2">
                    <button type="submit" class="w-full bg-gray-900 text-white py-3 px-4 uppercase tracking-widest text-xs font-bold hover:bg-gray-800 transition-colors">
                        Save Address
                    </button>
                 </div>
             </form>
          </div>
        </section>

        <!-- Right Column: Order Summary -->
        <section class="lg:col-span-5 mt-16 lg:mt-0 bg-white p-8 shadow-sm">
          <h2 class="text-xl font-medium text-gray-900 mb-6 font-serif">Order Summary</h2>

          <div v-if="!cartStore.cart || cartStore.cart.cartItems.length === 0">
             <p class="text-gray-500">Your cart is empty.</p>
             <router-link to="/" class="block mt-4 text-brand-gold hover:underline">Go shopping</router-link>
          </div>

          <div v-else class="flow-root">
            <ul role="list" class="-my-4 divide-y divide-gray-200">
              <li v-for="item in cartStore.cart.cartItems" :key="item.id" class="flex items-center py-4">
                <div class="h-16 w-16 flex-shrink-0 overflow-hidden rounded-md border border-gray-200">
                   <!-- Fallback Image -->
                   <img src="https://images.unsplash.com/photo-1620916566398-39f1143ab7be?q=80&w=1887&auto=format&fit=crop" class="h-full w-full object-cover object-center">
                </div>
                <div class="ml-4 flex-1">
                  <div class="flex justify-between text-base font-medium text-gray-900">
                    <h3>{{ item.product.name }}</h3>
                    <p class="ml-4">${{ item.product.price }}</p>
                  </div>
                  <p class="mt-1 text-sm text-gray-500">Qty {{ item.quantity }}</p>
                </div>
              </li>
            </ul>

            <div class="border-t border-gray-200 mt-6 pt-6">
              <div class="flex justify-between text-base font-medium text-gray-900">
                <p>Subtotal</p>
                <p>${{ cartStore.totalAmount.toFixed(2) }}</p>
              </div>
              <div class="flex justify-between text-base font-medium text-gray-500 mt-2 text-sm">
                <p>Shipping</p>
                <p>Free</p>
              </div>
               <div class="flex justify-between text-lg font-serif font-bold text-gray-900 mt-4 pt-4 border-t">
                <p>Total</p>
                <p>${{ cartStore.totalAmount.toFixed(2) }}</p>
              </div>

              <div class="mt-6">
                <button @click="handlePlaceOrder" :disabled="!selectedAddressId || processing" 
                        class="w-full bg-brand-gold border border-transparent py-4 px-4 text-sm font-medium text-white shadow-sm hover:bg-yellow-600 focus:outline-none transition-colors uppercase tracking-widest disabled:opacity-50 disabled:cursor-not-allowed">
                  {{ processing ? 'Processing...' : 'Confirm Order' }}
                </button>
              </div>
              <p v-if="!selectedAddressId && cartStore.itemCount > 0" class="mt-2 text-sm text-red-500 text-center">
                  Please select a shipping address.
              </p>
            </div>
          </div>
        </section>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, reactive } from 'vue';
import { useCartStore } from '@/stores/cart';
import { useAddressStore } from '@/stores/address';
import { useAuthStore } from '@/stores/auth';
import { useRouter } from 'vue-router';
import api from '@/services/api';

const cartStore = useCartStore();
const addressStore = useAddressStore();
const authStore = useAuthStore();
const router = useRouter();

const selectedAddressId = ref(null);
const showAddForm = ref(false);
const processing = ref(false);

const newAddress = reactive({
    title: '',
    city: '',
    district: '',
    fullAddress: ''
});

onMounted(() => {
    if (!authStore.isAuthenticated) {
        router.push('/login');
        return;
    }
    addressStore.fetchAddresses();
    if (!cartStore.cart) {
        cartStore.fetchCart();
    }
});

const handleAddressSubmit = async () => {
    const success = await addressStore.addAddress(newAddress);
    if (success) {
        showAddForm.value = false;
        // Reset form
        newAddress.title = '';
        newAddress.city = '';
        newAddress.district = '';
        newAddress.fullAddress = '';
        // Select the newly added address (optimized: last one)
        if (addressStore.addresses.length > 0) {
            selectedAddressId.value = addressStore.addresses[addressStore.addresses.length - 1].id;
        }
    }
};

const handlePlaceOrder = async () => {
    if (!selectedAddressId.value) return;
    
    processing.value = true;
    try {
        // API: /api/order/create/{userId}?addressId={addressId}
        const userId = authStore.user.id;
        const addressId = selectedAddressId.value;
        
        await api.post(`/order/create/${userId}`, null, {
            params: { addressId }
        });
        
        alert("Order placed successfully! Thank you for your purchase.");
        await cartStore.fetchCart(); // Clear cart in UI (backend should have cleared it)
        router.push('/'); // Redirect home
    } catch (err) {
        console.error("Order failed", err);
        alert("Failed to place order. " + (err.response?.data?.message || ''));
    } finally {
        processing.value = false;
    }
};
</script>
