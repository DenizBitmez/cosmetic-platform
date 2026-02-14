<template>
  <div class="min-h-screen bg-brand-cream py-12 px-4 sm:px-6 lg:px-8">
    <div class="max-w-7xl mx-auto">
      <h1 class="text-3xl font-serif text-brand-dark mb-8">Checkout</h1>

      <div class="lg:grid lg:grid-cols-12 lg:gap-x-12 lg:items-start">
        <!-- Left Column: Address Selection & Payment -->
        <section class="lg:col-span-7 space-y-8">
          <!-- Shipping Address -->
          <div class="bg-white p-8 shadow-sm">
            <h2 class="text-xl font-medium text-gray-900 mb-6 font-serif">Shipping Address</h2>
            
            <div v-if="addressStore.loading" class="text-gray-500">Loading addresses...</div>
            
            <div v-else class="space-y-4">
               <!-- Address List -->
                <div v-for="address in addressStore.addresses" :key="address.id" 
                    class="relative flex items-center p-5 border-2 rounded-lg cursor-pointer transition-all duration-300 transform"
                    :class="selectedAddressId == address.id ? 'border-brand-gold bg-yellow-50/70 scale-[1.02] shadow-md ring-2 ring-brand-gold' : 'border-gray-100 hover:border-gray-200 bg-white'"
                    @click="selectAddress(address.id)">
                    
                    <div class="min-w-0 flex-1">
                      <div class="flex items-center justify-between mb-2">
                        <span class="text-xs font-bold uppercase tracking-wider text-brand-gold bg-yellow-100 px-2 py-1 rounded">
                          {{ address.title }}
                        </span>
                        <div v-if="selectedAddressId == address.id" class="flex items-center text-brand-gold">
                           <PhCheckCircle :size="20" weight="fill" />
                        </div>
                      </div>
                      <div class="text-gray-800 font-medium line-clamp-2 mb-1">{{ address.fullAddress }}</div>
                      <div class="text-gray-500 text-sm font-light">{{ address.district }}, {{ address.city }}</div>
                    </div>
                    <button @click.stop="addressStore.deleteAddress(address.id)" class="ml-4 p-2 text-red-500 hover:text-red-700 transition-all bg-red-50 rounded-full border border-red-100 shadow-sm opacity-100">
                      <PhTrash :size="18" />
                    </button>
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
          </div>

          <!-- Payment -->
          <div class="bg-white p-8 shadow-sm">
            <h2 class="text-xl font-medium text-gray-900 mb-6 font-serif">Payment Method</h2>
            <div v-if="!stripeLoaded && !paymentError" class="mb-4 p-4 bg-blue-50 border border-blue-100 rounded text-center">
                <p class="text-[10px] text-blue-600 animate-pulse font-bold uppercase">
                  Initializing Payment System ({{ stripeStep }})...
                </p>
            </div>
            <div id="payment-element" v-show="stripeLoaded" class="min-h-[250px]">
              <!-- Stripe Elements mounted here -->
            </div>
            
            <!-- Amazon Pay / Cash App Premium Help Section -->
            <transition name="fade">
              <div v-if="showAmazonHelp" class="mt-3 p-6 bg-yellow-50 rounded-2xl border-2 border-brand-gold/20 shadow-sm animate-fade-in relative overflow-hidden">
                <div class="absolute top-0 right-0 p-4 opacity-5 text-brand-gold">
                   <PhQuestion :size="80" />
                </div>
                <div class="relative z-10 flex items-start gap-5">
                    <div class="bg-white p-3 rounded-xl shadow-md border border-brand-gold/30">
                       <PhQuestion :size="28" class="text-brand-gold" />
                    </div>
                    <div>
                       <h3 class="text-base font-bold text-brand-dark mb-2">Amazon Pay Assistance</h3>
                       <p class="text-sm text-gray-600 leading-relaxed mb-4">
                          If the Amazon Pay window didn't open or appeared as a blank area, please ensure pop-ups are allowed or click the button below to open it manually.
                       </p>
                       <a href="https://pay.amazon.com" target="_blank" class="inline-flex items-center gap-3 text-sm font-bold text-white bg-gray-900 px-5 py-2.5 rounded-xl shadow-lg hover:bg-black transition-all hover:scale-[1.02] active:scale-[0.98]">
                          Open Amazon Pay Portal 
                          <PhArrowSquareUpRight :size="10" />
                       </a>
                    </div>
                </div>
              </div>
            </transition>
            <div v-if="paymentError" class="mt-4 p-4 bg-red-50 border border-red-100 rounded-md">
              <p class="text-sm text-red-600 mb-3">{{ paymentError }}</p>
              <button @click="useMockPayment" class="text-xs font-bold text-red-700 underline uppercase tracking-widest hover:text-red-800">
                Skip Payment (Demo Mode)
              </button>
            </div>
          </div>
        </section>

        <!-- Right Column: Order Summary -->
        <section class="lg:col-span-5 mt-16 lg:mt-0 bg-white p-8 shadow-sm border border-gray-100 rounded-sm sticky top-28">
          <h2 class="text-xl font-medium text-gray-900 mb-6 font-serif">Order Summary</h2>

          <div v-if="cartStore.loading && !cartStore.cart" class="flex flex-col items-center py-12">
             <div class="animate-spin rounded-full h-8 w-8 border-b-2 border-brand-gold"></div>
             <p class="mt-4 text-sm text-gray-500">Loading your cart...</p>
          </div>

          <div v-else-if="!cartStore.cart || cartStore.cart.cartItems.length === 0">
             <div class="text-center py-8">
               <PhShoppingCart :size="48" class="mx-auto text-gray-200 mb-4" />
               <p class="text-gray-500">Your cart is empty.</p>
               <router-link to="/" class="block mt-4 text-brand-gold hover:underline font-medium">Continue Shopping</router-link>
             </div>
          </div>

          <div v-else class="flow-root">
            <ul role="list" class="-my-4 divide-y divide-gray-200">
              <li v-for="item in cartStore.cart.cartItems" :key="item.id" class="flex items-center py-4">
                <div class="h-20 w-20 flex-shrink-0 overflow-hidden rounded-md border border-gray-100 bg-gray-50 flex items-center justify-center">
                  <img :src="item.product.image" 
                       @error="(e) => e.target.src = 'https://images.unsplash.com/photo-1620916566398-39f1143ab7be?q=80&w=400&auto=format&fit=crop'"
                       class="h-full w-full object-contain p-1 transition-opacity duration-300">
                </div>
                <div class="ml-4 flex-1">
                  <div class="flex justify-between text-base font-medium text-gray-900">
                    <h3 class="line-clamp-1 pr-4">{{ item.product.name }}</h3>
                    <p class="ml-4">${{ item.product.price }}</p>
                  </div>
                  <p class="mt-1 text-sm text-gray-500">Qty {{ item.quantity }}</p>
                </div>
              </li>
            </ul>

            <!-- Loyalty Redemption -->
             <div class="border-t border-gray-200 mt-6 pt-6 mb-6">
                <div class="flex items-center justify-between mb-2">
                    <h3 class="text-sm font-medium text-gray-900 flex items-center gap-2">
                        <PhCoin class="text-brand-gold" weight="fill" />
                        Redeem Points
                    </h3>
                    <span class="text-xs text-gray-500">Balance: {{ loyaltyStore.pointsBalance }} points</span>
                </div>
                
                <div v-if="loyaltyStore.pointsBalance > 0" class="flex gap-2">
                    <input 
                        v-model.number="redeemAmount" 
                        type="number" 
                        :max="Math.min(loyaltyStore.pointsBalance, cartStore.totalAmount * 10)" 
                        min="0"
                        class="block w-full border-gray-300 rounded-md shadow-sm focus:ring-brand-gold focus:border-brand-gold sm:text-sm"
                        placeholder="Points to use"
                    >
                    <button 
                        @click="redeemAmount = Math.min(loyaltyStore.pointsBalance, Math.floor(cartStore.totalAmount))"
                        class="text-xs font-bold text-brand-gold uppercase tracking-wider px-2 hover:text-brand-dark"
                    >
                        Max
                    </button>
                </div>
                <p v-else class="text-xs text-gray-500 italic">You don't have any points to redeem yet.</p>
                <p class="text-xs text-gray-400 mt-1" v-if="redeemAmount > 0">
                    Converting {{ redeemAmount }} points to ${{ redeemAmount.toFixed(2) }} discount
                </p>
             </div>

            <div class="border-t border-gray-200 mt-6 pt-6">
              <div class="flex justify-between text-base font-medium text-gray-900">
                <p>Subtotal</p>
                <p>${{ cartStore.totalAmount.toFixed(2) }}</p>
              </div>
              <div class="flex justify-between text-base font-medium text-gray-500 mt-2 text-sm">
                <p>Shipping</p>
                <p>Free</p>
              </div>
              <div v-if="redeemAmount > 0" class="flex justify-between text-base font-medium text-green-600 mt-2 text-sm">
                <p>Points Discount</p>
                <p>-${{ redeemAmount.toFixed(2) }}</p>
              </div>
               <div class="flex justify-between text-lg font-serif font-bold text-gray-900 mt-4 pt-4 border-t">
                <p>Total</p>
                <p>${{ (cartStore.totalAmount - redeemAmount).toFixed(2) }}</p>
              </div>

              <div class="mt-6">
                <button @click="handleSubmitPayment" :disabled="!isAddressSelected || processing || !stripeLoaded" 
                        class="w-full bg-brand-gold border border-transparent py-4 px-4 text-sm font-medium text-white shadow-sm hover:bg-yellow-600 focus:outline-none transition-all uppercase tracking-widest disabled:opacity-50 disabled:grayscale disabled:cursor-not-allowed flex items-center justify-center">
                  <div v-if="processing" class="animate-spin rounded-full h-4 w-4 border-b-2 border-white mr-2"></div>
                  {{ processing ? 'Processing...' : 'Confirm & Pay' }}
                </button>
              </div>
              
              <div v-if="!isAddressSelected && cartStore.itemCount > 0" class="mt-3 p-3 bg-red-50 border border-red-100 rounded text-center">
                  <p class="text-xs text-red-600 font-bold uppercase tracking-tight">
                    <PhWarning class="inline-block mr-1" />
                    Please add or select a shipping address
                  </p>
              </div>
            </div>
          </div>
        </section>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, reactive, nextTick, computed } from 'vue';
import { useCartStore } from '@/stores/cart';
import { useAddressStore } from '@/stores/address';
import { useAuthStore } from '@/stores/auth';
import { useRouter } from 'vue-router';
import api from '@/services/api';
import { loadStripe } from '@stripe/stripe-js';
import { PhShoppingCart, PhCheckCircle, PhTrash, PhWarning, PhQuestion, PhArrowSquareUpRight, PhCoin } from '@phosphor-icons/vue';
import { useLoyaltyStore } from '@/stores/loyalty';

const cartStore = useCartStore();
const addressStore = useAddressStore();
const authStore = useAuthStore();
const loyaltyStore = useLoyaltyStore();
const router = useRouter();

const selectedAddressId = ref(null);
const showAddForm = ref(false);
const processing = ref(false);
const stripeLoaded = ref(false);
const initializing = ref(false);
const stripeStep = ref('IDLE');
const paymentError = ref(null);
const publicKeySnapshot = ref(null);
const showAmazonHelp = ref(false);
const redeemAmount = ref(0);

const isAddressSelected = computed(() => {
    if (!selectedAddressId.value) return false;
    return addressStore.addresses.some(a => a.id == selectedAddressId.value);
});

let stripe = null;
let elements = null;
let paymentElement = null;

const newAddress = reactive({
    title: '',
    city: '',
    district: '',
    fullAddress: ''
});

onMounted(async () => {
    console.log("CheckoutView mounted, checking auth...");
    if (!authStore.isAuthenticated) {
        router.push('/login');
        return;
    }
    
    // Start address fetch
    console.log("Fetching addresses...");
    await addressStore.fetchAddresses();
    if (addressStore.addresses.length > 0) {
        if (!selectedAddressId.value) {
            selectedAddressId.value = addressStore.addresses[0].id;
        }
    } else {
        console.log("No addresses found, showing add form...");
        showAddForm.value = true;
    }

    // Ensure cart is loaded
    if (!cartStore.cart || cartStore.cart.cartItems.length === 0) {
        console.log("Fetching cart...");
        await cartStore.fetchCart();
    }
    
    console.log("Cart total:", cartStore.totalAmount);
    // Initialize Stripe only after cart total is known and > 0
    if (cartStore.totalAmount > 0) {
        await initStripe();
    }
    
    // Fetch loyalty points
    await loyaltyStore.fetchBalance();
});

import { watch } from 'vue';
watch(() => cartStore.totalAmount, async (newTotal) => {
    if (newTotal > 0 && !stripe) {
        await initStripe();
    }
});

const initStripe = async () => {
    if (initializing.value || stripeLoaded.value || !cartStore.cart || cartStore.totalAmount <= 0) {
        console.log("Stripe init skipped", { initializing: initializing.value, loaded: stripeLoaded.value, total: cartStore.totalAmount });
        return;
    }
    
    initializing.value = true;
    stripeStep.value = 'STARTING';
    
    // Add a safety timeout
    const timeout = setTimeout(() => {
        if (!stripeLoaded.value && !paymentError.value) {
            stripeStep.value = 'TIMEOUT';
            paymentError.value = "Stripe initialization is taking too long. Step: " + stripeStep.value;
        }
    }, 15000);

    try {
        stripeStep.value = 'FETCH_CONFIG';
        const { data } = await api.get('/payment/config');
        publicKeySnapshot.value = data.publicKey;
        
        stripeStep.value = 'LOAD_STRIPE_JS';
        stripe = await loadStripe(data.publicKey);
        if (!stripe) throw new Error("Stripe.js failed to load.");

        stripeStep.value = 'CREATE_PAYMENT_INTENT';
        const response = await api.post('/payment/create-payment-intent', {
            amount: cartStore.totalAmount
        });
        const clientSecret = response.data.clientSecret;
        
        stripeStep.value = 'CREATE_ELEMENTS';
        elements = stripe.elements({ clientSecret, locale: 'en' });
        paymentElement = elements.create('payment');
        
        stripeStep.value = 'MOUNT_ELEMENT';
        await nextTick();
        const el = document.getElementById('payment-element');
        if (el) {
            paymentElement.mount('#payment-element');
            
            paymentElement.on('change', (event) => {
                // The correct property is identified as event.value.type from debug session
                showAmazonHelp.value = event.value?.type === 'amazon_pay';
            });

            stripeLoaded.value = true;
            stripeStep.value = 'SUCCESS';
        } else {
            throw new Error("Target element #payment-element not found.");
        }
    } catch (err) {
        stripeStep.value = 'ERROR';
        console.error("Stripe initialization failed", err);
        paymentError.value = err.response?.data?.message || err.message || "Failed to load payment system.";
    } finally {
        initializing.value = false;
        clearTimeout(timeout);
    }
};

const useMockPayment = () => {
    alert("DEMO MODE: Simulating payment success...");
    handlePlaceOrder();
};

const selectAddress = (id) => {
    console.log("Selecting address:", id);
    selectedAddressId.value = id;
};

const handleAddressSubmit = async () => {
    const success = await addressStore.addAddress(newAddress);
    if (success) {
        showAddForm.value = false;
        newAddress.title = '';
        newAddress.city = '';
        newAddress.district = '';
        newAddress.fullAddress = '';
        await addressStore.fetchAddresses(); // Refresh to get the new id
        if (addressStore.addresses.length > 0) {
            selectedAddressId.value = addressStore.addresses[addressStore.addresses.length - 1].id;
        }
    }
};

const handleSubmitPayment = async () => {
    if (!selectedAddressId.value || processing.value) return;
    
    processing.value = true;
    paymentError.value = null;

    try {
        // If redeeming points, handle that first
        if (redeemAmount.value > 0) {
             const success = await loyaltyStore.redeemPoints(redeemAmount.value);
             if (!success) {
                 throw new Error("Failed to redeem points. Please try again or clear redemption.");
             }
        }

        const { error } = await stripe.confirmPayment({
            elements,
            confirmParams: {
                return_url: `${window.location.origin}/`,
            },
            redirect: 'if_required'
        });

        if (error) {
            paymentError.value = error.message;
            processing.value = false;
        } else {
            // Payment successful, now create the order
            await handlePlaceOrder();
        }
    } catch (err) {
        console.error("Payment failed", err);
        paymentError.value = "An unexpected error occurred.";
        processing.value = false;
    }
};

const handlePlaceOrder = async () => {
    try {
        const userId = authStore.user.id;
        const addressId = selectedAddressId.value;
        
        await api.post(`/order/create/${userId}`, null, {
            params: { addressId }
        });
        
        alert("Payment successful! Your order has been placed.");
        await cartStore.fetchCart(); 
        router.push('/');
    } catch (err) {
        console.error("Order completion failed", err);
        alert("Payment was successful but we failed to record your order. Please contact support.");
    } finally {
        processing.value = false;
    }
};
</script>

<style scoped>
.fade-enter-active,
.fade-leave-active {
  transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
  transform: translateY(-10px) scale(0.98);
}

.animate-fade-in {
  animation: fadeIn 0.5s ease-out forwards;
}

@keyframes fadeIn {
  from { opacity: 0; transform: translateY(10px); }
  to { opacity: 1; transform: translateY(0); }
}
</style>
