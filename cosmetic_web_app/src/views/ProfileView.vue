<template>
  <div class="min-h-screen bg-gray-50 pt-8 pb-16">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
      
      <!-- Header -->
      <div class="mb-8 flex items-center justify-between">
         <div>
             <h1 class="text-3xl font-serif text-brand-dark">My Account</h1>
             <p class="text-gray-500 mt-1">Welcome back, {{ authStore.user?.username }}</p>
         </div>
         <button @click="authStore.logout(); router.push('/login')" class="text-sm text-red-600 hover:text-red-800 font-medium underline">
             Sign Out
         </button>
      </div>

      <div class="flex flex-col lg:flex-row gap-8">
        
        <!-- Sidebar Navigation -->
        <div class="w-full lg:w-64 flex-shrink-0">
          <nav class="bg-white shadow rounded-lg p-4 space-y-1 sticky top-24">
            <button 
                v-for="item in navItems" 
                :key="item.id"
                @click="currentTab = item.id"
                :class="[
                  currentTab === item.id 
                    ? 'bg-brand-cream text-brand-dark border-l-4 border-brand-gold font-semibold' 
                    : 'text-gray-600 hover:bg-gray-50 hover:text-gray-900',
                  'group flex items-center px-4 py-3 text-sm font-medium rounded-r-md transition-colors w-full'
                ]"
            >
              <component :is="item.icon" :class="[currentTab === item.id ? 'text-brand-gold' : 'text-gray-400 group-hover:text-gray-500', 'mr-3 flex-shrink-0 h-5 w-5']" />
              {{ item.name }}
            </button>
          </nav>
        </div>

        <!-- Main Content Area -->
        <div class="flex-1 bg-white shadow rounded-lg min-h-[500px] p-6 lg:p-10">
            
            <!-- OVERVIEW TAB -->
            <div v-if="currentTab === 'overview'" class="space-y-8">
                <h2 class="text-xl font-bold text-gray-900 border-b pb-4">Account Overview</h2>
                <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
                    <div class="bg-brand-cream p-6 rounded-lg">
                        <h3 class="font-serif text-lg text-brand-dark mb-2">My Profile</h3>
                        <p class="text-sm text-gray-600 mb-1"><strong class="font-medium text-gray-900">Name:</strong> {{ authStore.user?.username }}</p>
                        <p class="text-sm text-gray-600"><strong class="font-medium text-gray-900">Email:</strong> {{ authStore.user?.email }}</p>
                    </div>
                     <div class="border border-gray-200 p-6 rounded-lg flex flex-col justify-center items-center text-center">
                        <template v-if="orders.length > 0">
                             <div class="w-full text-left">
                                <h4 class="font-bold text-gray-900 mb-2">Latest Order</h4>
                                <div class="flex justify-between items-center mb-1">
                                    <span class="text-sm text-gray-600">Order #{{ orders[0].id }}</span>
                                    <span class="text-xs font-medium bg-green-100 text-green-800 px-2 py-0.5 rounded-full">{{ orders[0].status }}</span>
                                </div>
                                <p class="text-xs text-gray-500 mb-3">{{ new Date(orders[0].orderDate).toLocaleDateString() }}</p>
                                <p class="text-sm font-bold text-brand-dark mb-4">${{ orders[0].totalAmount }}</p>
                                <button @click="currentTab = 'orders'" class="w-full bg-white border border-gray-300 text-gray-700 text-sm py-2 rounded-md hover:bg-gray-50 transition-colors">
                                    View All Orders
                                </button>
                             </div>
                        </template>
                        <template v-else>
                            <PhPackage class="h-10 w-10 text-brand-gold mb-3 mx-auto" />
                            <p class="text-sm text-gray-500">You have no active orders in progress.</p>
                            <button @click="currentTab = 'orders'" class="mt-2 text-brand-dark font-medium text-sm hover:underline">View Order History</button>
                        </template>
                    </div>
                </div>
            </div>

            <!-- ORDERS TAB -->
            <div v-else-if="currentTab === 'orders'" class="space-y-6">
                <h2 class="text-xl font-bold text-gray-900 border-b pb-4">My Orders</h2>
                
                <div v-if="orders.length === 0" class="text-center py-12">
                     <PhPackage class="mx-auto h-12 w-12 text-gray-300" />
                     <h3 class="mt-2 text-sm font-medium text-gray-900">No orders yet</h3>
                     <p class="mt-1 text-sm text-gray-500">Start shopping to fill your beauty cabinet.</p>
                     <div class="mt-6">
                        <router-link to="/" class="inline-flex items-center px-4 py-2 border border-transparent shadow-sm text-sm font-medium rounded-md text-white bg-brand-dark hover:bg-brand-gold transition-colors">
                          Start Shopping
                        </router-link>
                      </div>
                </div>

                <div v-else class="space-y-4">
                    <div v-for="order in orders" :key="order.id" class="border rounded-lg overflow-hidden">
                        <div class="bg-gray-50 px-4 py-4 sm:px-6 flex justify-between items-center">
                            <div>
                                <p class="text-sm font-medium text-gray-900">Order #{{ order.id }}</p>
                                <p class="text-xs text-gray-500">{{ new Date(order.orderDate).toLocaleDateString() }}</p>
                            </div>
                            <div class="flex items-center space-x-4">
                                <span class="px-2.5 py-0.5 rounded-full text-xs font-medium bg-yellow-100 text-yellow-800">
                                    {{ order.status }}
                                </span>
                                <p class="text-sm font-bold text-gray-900">${{ order.totalAmount }}</p>
                            </div>
                        </div>
                        <div class="px-4 py-4 sm:px-6">
                            <h4 class="text-xs font-semibold uppercase tracking-wide text-gray-500 mb-2">Items</h4>
                            <ul class="divide-y divide-gray-200">
                                <li v-for="(item, idx) in order.orderItems" :key="idx" class="py-2 flex justify-between text-sm">
                                   <div class="flex items-center">
                                       <span class="text-gray-900 font-medium">{{ item.productName }}</span>
                                       <span class="ml-2 text-gray-400 text-xs">x{{ item.quantity }}</span>
                                   </div>
                                   <span class="text-gray-600">${{ item.price }}</span>
                                </li>
                            </ul>
                        </div>
                    </div>
                </div>
            </div>

            <!-- ADDRESSES TAB -->
            <div v-else-if="currentTab === 'addresses'" class="space-y-6">
                <div class="flex justify-between items-center border-b pb-4">
                     <h2 class="text-xl font-bold text-gray-900">My Addresses</h2>
                     <button @click="showAddressModal = true" class="text-sm bg-brand-dark text-white px-3 py-1.5 rounded hover:bg-black transition-colors">Add New</button>
                </div>
                
                <div v-if="addresses.length === 0" class="text-center py-10 text-gray-500">
                    You have no saved addresses.
                </div>

                 <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
                    <div v-for="addr in addresses" :key="addr.id" class="border rounded-lg p-4 relative group hover:border-brand-gold transition-colors">
                        <button @click="deleteAddress(addr.id)" class="absolute top-4 right-4 text-gray-400 hover:text-red-500 opacity-0 group-hover:opacity-100 transition-opacity">
                            <PhTrash :size="20"/>
                        </button>
                        <h4 class="font-bold text-sm mb-1">{{ addr.title }}</h4>
                        <p class="text-sm text-gray-600">{{ addr.fullAddress }}</p>
                        <p class="text-sm text-gray-600 mt-1">{{ addr.district }} / {{ addr.city }}</p>
                    </div>
                 </div>
            </div>

            <!-- BUY AGAIN TAB -->
            <div v-else-if="currentTab === 'buy-again'" class="space-y-6">
                <h2 class="text-xl font-bold text-gray-900 border-b pb-4">Buy Again</h2>
                <div v-if="buyAgain.length === 0" class="text-center py-10 text-gray-500">
                    No purchases yet to repeat.
                </div>
                <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6">
                    <div v-for="product in buyAgain" :key="product.id" class="border rounded-lg p-4 hover:shadow-md transition-shadow">
                        <div class="h-32 w-full bg-gray-200 rounded-md mb-4 bg-cover bg-center" :style="{ backgroundImage: 'url(' + product.image + ')' }"></div>
                        <h3 class="text-sm font-bold text-gray-900">{{ product.name }}</h3>
                        <p class="text-xs text-gray-500 mb-2">{{ product.category }}</p>
                        <div class="flex justify-between items-center">
                            <span class="font-medium text-brand-gold">${{ product.price }}</span>
                            <button class="text-xs bg-brand-dark text-white px-2 py-1 rounded hover:bg-black">Add to Cart</button>
                        </div>
                    </div>
                </div>
            </div>

            <!-- SAVED CARDS TAB -->
            <div v-else-if="currentTab === 'wallet'" class="space-y-6">
                <div class="flex justify-between items-center border-b pb-4">
                     <h2 class="text-xl font-bold text-gray-900">Saved Cards</h2>
                     <button class="text-sm bg-brand-dark text-white px-3 py-1.5 rounded hover:bg-black transition-colors">Add New Card</button>
                </div>
                <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
                    <div v-for="card in mockCards" :key="card.id" class="border rounded-lg p-4 bg-gradient-to-br from-gray-800 to-gray-900 text-white relative overflow-hidden">
                        <div class="absolute top-0 right-0 p-4 opacity-20"><PhCreditCard size="64" /></div>
                        <div class="relative z-10">
                            <p class="text-xs text-gray-400 uppercase tracking-widest mb-4">{{ card.brand }}</p>
                            <p class="text-xl font-mono tracking-wider mb-4">•••• •••• •••• {{ card.last4 }}</p>
                            <div class="flex justify-between text-xs text-gray-400">
                                <div>
                                    <p class="uppercase text-[10px]">Card Holder</p>
                                    <p class="text-white">{{ authStore.user?.username || 'DENIZ BITMEZ' }}</p>
                                </div>
                                <div>
                                    <p class="uppercase text-[10px]">Expires</p>
                                    <p class="text-white">{{ card.expiry }}</p>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <!-- COUPONS TAB -->
            <div v-else-if="currentTab === 'coupons'" class="space-y-6">
                 <h2 class="text-xl font-bold text-gray-900 border-b pb-4">My Coupons</h2>
                 <div class="space-y-4">
                    <div v-for="coupon in mockCoupons" :key="coupon.code" class="border border-brand-gold/30 bg-brand-cream/30 rounded-lg p-4 flex flex-col sm:flex-row justify-between items-center gap-4">
                        <div class="flex items-center gap-4">
                            <div class="bg-brand-gold/10 p-3 rounded-full text-brand-gold">
                                <PhTag size="24" weight="fill" />
                            </div>
                            <div>
                                <h4 class="font-bold text-brand-dark">{{ coupon.description }}</h4>
                                <p class="text-xs text-gray-500">Expires: {{ coupon.expiry }}</p>
                            </div>
                        </div>
                        <div class="flex items-center gap-4">
                            <span class="font-mono bg-white border border-dashed border-gray-300 px-3 py-1 rounded text-sm text-gray-600">{{ coupon.code }}</span>
                            <button class="text-xs font-bold text-brand-gold hover:text-brand-dark uppercase">Copy</button>
                        </div>
                    </div>
                 </div>
            </div>

            <!-- RECENTLY VIEWED TAB -->
            <div v-else-if="currentTab === 'viewed'" class="space-y-6">
                <h2 class="text-xl font-bold text-gray-900 border-b pb-4">Recently Viewed</h2>
                 <div v-if="recentlyViewed.length === 0" class="text-center py-10 text-gray-500">
                    You haven't viewed any products recently.
                </div>
                 <div class="grid grid-cols-2 md:grid-cols-4 gap-4">
                    <div v-for="product in recentlyViewed" :key="product.id" class="group cursor-pointer">
                        <div class="aspect-w-1 aspect-h-1 w-full overflow-hidden rounded-lg bg-gray-200 xl:aspect-w-7 xl:aspect-h-8">
                             <div class="h-40 bg-cover bg-center group-hover:opacity-75" :style="{ backgroundImage: 'url(' + (product.image || 'https://images.unsplash.com/photo-1620916566398-39f1143ab7be?q=80&w=200') + ')' }"></div>
                        </div>
                        <h3 class="mt-2 text-sm text-gray-700 truncate">{{ product.name }}</h3>
                        <p class="mt-1 text-lg font-medium text-brand-gold">${{ product.price }}</p>
                    </div>
                 </div>
            </div>

            <!-- HELP & FAQ TAB -->
            <div v-else-if="currentTab === 'help'" class="space-y-6">
                <h2 class="text-xl font-bold text-gray-900 border-b pb-4">Help & FAQ</h2>
                <div class="space-y-4">
                    <div v-for="(faq, index) in mockFaqs" :key="index" class="border-b border-gray-100 pb-4 last:border-0">
                        <button class="flex justify-between items-center w-full text-left focus:outline-none group">
                            <span class="font-medium text-gray-900 group-hover:text-brand-gold transition-colors">{{ faq.question }}</span>
                        </button>
                        <p class="mt-2 text-sm text-gray-600 leading-relaxed">{{ faq.answer }}</p>
                    </div>
                </div>
                <div class="bg-gray-50 p-6 rounded-lg text-center mt-8">
                    <p class="text-sm text-gray-600 mb-4">Still need help?</p>
                    <button class="bg-brand-dark text-white px-6 py-2 rounded-full text-sm hover:bg-black transition-colors">Contact Support</button>
                </div>
            </div>

            <!-- ASSISTANT TAB (Placeholder) -->
            <div v-else class="space-y-6">
                 <h2 class="text-xl font-bold text-gray-900 border-b pb-4">{{ navItems.find(i => i.id === currentTab)?.name }}</h2>
                 <div class="text-center py-12 bg-gray-50 rounded-lg border-2 border-dashed border-gray-200">
                     <component :is="navItems.find(i => i.id === currentTab)?.icon" class="mx-auto h-12 w-12 text-gray-400" />
                     <h3 class="mt-2 text-sm font-medium text-gray-900">AI Assistant Integration</h3>
                     <p class="mt-1 text-sm text-gray-500">Integration with open-source LLM is pending configuration.</p>
                 </div>
            </div>

        </div>
      </div>

      <!-- Add Address Modal -->
      <div v-if="showAddressModal" class="fixed inset-0 z-50 overflow-y-auto" aria-labelledby="modal-title" role="dialog" aria-modal="true">
        <div class="flex items-end justify-center min-h-screen pt-4 px-4 pb-20 text-center sm:block sm:p-0">
          <div class="fixed inset-0 bg-gray-500 bg-opacity-75 transition-opacity" aria-hidden="true" @click="showAddressModal = false"></div>
          <span class="hidden sm:inline-block sm:align-middle sm:h-screen" aria-hidden="true">&#8203;</span>
          <div class="inline-block align-bottom bg-white rounded-lg px-4 pt-5 pb-4 text-left overflow-hidden shadow-xl transform transition-all sm:my-8 sm:align-middle sm:max-w-lg sm:w-full sm:p-6">
            <div>
              <div class="mt-3 text-center sm:mt-5">
                <h3 class="text-lg leading-6 font-medium text-gray-900" id="modal-title">
                  Add New Address
                </h3>
                <div class="mt-2">
                  <form @submit.prevent="saveAddress" class="space-y-4 text-left">
                      <div>
                          <label class="block text-sm font-medium text-gray-700">Title (e.g., Home)</label>
                          <input v-model="newAddress.title" type="text" required class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-brand-gold focus:border-brand-gold sm:text-sm">
                      </div>
                      <div class="grid grid-cols-2 gap-4">
                          <div>
                              <label class="block text-sm font-medium text-gray-700">City</label>
                              <input v-model="newAddress.city" type="text" required class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-brand-gold focus:border-brand-gold sm:text-sm">
                          </div>
                           <div>
                              <label class="block text-sm font-medium text-gray-700">District</label>
                              <input v-model="newAddress.district" type="text" required class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-brand-gold focus:border-brand-gold sm:text-sm">
                          </div>
                      </div>
                      <div>
                          <label class="block text-sm font-medium text-gray-700">Full Address</label>
                          <textarea v-model="newAddress.fullAddress" rows="3" required class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-brand-gold focus:border-brand-gold sm:text-sm"></textarea>
                      </div>
                      <div class="mt-5 sm:mt-6 sm:grid sm:grid-cols-2 sm:gap-3 sm:grid-flow-row-dense">
                        <button type="submit" class="w-full inline-flex justify-center rounded-md border border-transparent shadow-sm px-4 py-2 bg-brand-dark text-base font-medium text-white hover:bg-black focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-brand-gold sm:col-start-2 sm:text-sm">
                          Save
                        </button>
                        <button type="button" @click="showAddressModal = false" class="mt-3 w-full inline-flex justify-center rounded-md border border-gray-300 shadow-sm px-4 py-2 bg-white text-base font-medium text-gray-700 hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-brand-gold sm:mt-0 sm:col-start-1 sm:text-sm">
                          Cancel
                        </button>
                      </div>
                  </form>
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
import { ref, reactive, onMounted } from 'vue';
import { useAuthStore } from '@/stores/auth';
import { useRouter } from 'vue-router';
import api from '@/services/api';
import { 
    PhUser, PhPackage, PhMapPin, PhArrowsClockwise, 
    PhCreditCard, PhTag, PhClockCounterClockwise, 
    PhQuestion, PhRobot, PhStar, PhTrash
} from '@phosphor-icons/vue';

const authStore = useAuthStore();
const router = useRouter();

const currentTab = ref('overview');
const showAddressModal = ref(false);

const navItems = [
    { id: 'overview', name: 'Overview', icon: PhUser },
    { id: 'orders', name: 'My Orders', icon: PhPackage },
    { id: 'addresses', name: 'My Addresses', icon: PhMapPin },
    { id: 'buy-again', name: 'Buy Again', icon: PhArrowsClockwise },
    { id: 'wallet', name: 'Saved Cards', icon: PhCreditCard },
    { id: 'coupons', name: 'Discount Coupons', icon: PhTag },
    { id: 'viewed', name: 'Recently Viewed', icon: PhClockCounterClockwise },
    { id: 'help', name: 'Help & FAQ', icon: PhQuestion },
    { id: 'assistant', name: 'Assistant', icon: PhRobot },
];

// Mock Data (Restored)
const mockCards = [
    { id: 1, brand: 'Mastercard', last4: '4242', expiry: '12/28' },
    { id: 2, brand: 'Visa', last4: '8888', expiry: '05/26' },
];

const mockCoupons = [
    { code: 'WELCOME20', description: '20% Off Your Next Order', expiry: 'Dec 31, 2025' },
    { code: 'FREESHIP', description: 'Free Shipping on Orders Over $50', expiry: 'N/A' },
];

const mockFaqs = [
    { question: 'How do I track my order?', answer: 'You can track your order status in the "My Orders" tab. Once shipped, a tracking number will be provided.' },
    { question: 'What is your return policy?', answer: 'We accept returns within 30 days of purchase. Products must be unopened and in original packaging.' },
    { question: 'Do you ship internationally?', answer: 'Yes, we ship to over 100 countries including Turkey, UK, Germany, and USA.' },
];

// Data Refs
const orders = ref([]);
const addresses = ref([]);
const buyAgain = ref([]);
const recentlyViewed = ref(JSON.parse(localStorage.getItem('recentlyViewed') || '[]'));
const wallet = ref(mockCards);
const coupons = ref(mockCoupons);
const faqs = ref(mockFaqs);

const newAddress = reactive({
    title: '',
    city: '',
    district: '',
    fullAddress: ''
});

// Address Functions
const fetchAddresses = async () => {
    if (authStore.user?.id) {
         try {
            const res = await api.get(`/address/user/${authStore.user.id}`);
            addresses.value = res.data;
        } catch (e) {
            console.error("Failed to fetch addresses", e);
        }
    }
};

const saveAddress = async () => {
     if (authStore.user?.id) {
        try {
            const payload = {
                ...newAddress,
                userId: authStore.user.id
            };
            await api.post('/address/add', payload);
            showAddressModal.value = false;
            // Reset form
            newAddress.title = '';
            newAddress.city = '';
            newAddress.district = '';
            newAddress.fullAddress = '';
            // Refresh list
            await fetchAddresses();
        } catch (e) {
            console.error("Failed to add address", e);
            alert("Failed to add address");
        }
    }
};

const deleteAddress = async (id) => {
    if(confirm("Are you sure you want to delete this address?")){
        try {
            await api.delete(`/address/${id}`);
            await fetchAddresses();
        } catch (e) {
            console.error("Failed to delete address", e);
        }
    }
};

onMounted(async () => {
    if (authStore.user?.id) {
        try {
            const res = await api.get(`/order/user/${authStore.user.id}`);
            orders.value = res.data;
            
            // Process Buy Again
            const allItems = orders.value.flatMap(o => o.orderItems);
            const uniqueItems = [];
            const map = new Map();
            for (const item of allItems) {
                if(!map.has(item.productName)){
                    map.set(item.productName, true);
                    uniqueItems.push({
                        id: Math.random(),
                        name: item.productName,
                        price: item.price,
                        category: item.category,
                        image: 'https://images.unsplash.com/photo-1620916566398-39f1143ab7be?q=80&w=200' 
                    });
                }
            }
            buyAgain.value = uniqueItems;

        } catch (e) {
            console.error("Failed to fetch orders", e);
        }
        
        await fetchAddresses();
    }
});
</script>
