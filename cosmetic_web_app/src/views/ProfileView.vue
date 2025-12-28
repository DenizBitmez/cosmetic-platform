<template>
  <div class="min-h-screen bg-brand-cream py-12 px-4 sm:px-6 lg:px-8">
    <div class="max-w-6xl mx-auto">
       <div class="bg-white shadow-xl overflow-hidden">
         <div class="md:flex">
            <!-- Sidebar -->
            <div class="md:w-1/4 bg-gray-900 p-8 text-white">
                <div class="mb-10 text-center">
                    <div class="h-24 w-24 rounded-full bg-brand-gold mx-auto flex items-center justify-center text-3xl font-serif text-white mb-4">
                        {{ authStore.user?.username?.charAt(0).toUpperCase() }}
                    </div>
                    <h2 class="text-xl font-bold">{{ authStore.user?.username }}</h2>
                    <p class="text-sm text-gray-400 capitalize">{{ authStore.user?.userType?.toLowerCase() }}</p>
                </div>

                <nav class="space-y-2">
                    <button @click="activeTab = 'orders'" :class="activeTab === 'orders' ? 'bg-brand-gold text-white' : 'text-gray-300 hover:bg-gray-800'" class="w-full text-left px-4 py-3 rounded transition-colors uppercase tracking-widest text-xs font-bold">
                        My Orders
                    </button>
                    <button @click="activeTab = 'addresses'" :class="activeTab === 'addresses' ? 'bg-brand-gold text-white' : 'text-gray-300 hover:bg-gray-800'" class="w-full text-left px-4 py-3 rounded transition-colors uppercase tracking-widest text-xs font-bold">
                        Addresses
                    </button>
                    <button @click="handleLogout" class="w-full text-left px-4 py-3 text-red-400 hover:bg-gray-800 rounded transition-colors uppercase tracking-widest text-xs font-bold mt-8">
                        Logout
                    </button>
                </nav>
            </div>

            <!-- Content Area -->
            <div class="md:w-3/4 p-8">
                <!-- Orders Tab -->
                <div v-if="activeTab === 'orders'">
                    <h1 class="text-2xl font-serif text-brand-dark mb-6">Order History</h1>
                    
                    <div v-if="orderStore.loading" class="text-gray-500">Loading orders...</div>
                    <div v-if="orderStore.error" class="text-red-500 mb-4">{{ orderStore.error }}</div>
                    
                    <!-- DEBUG: Remove before production -->
                    <div class="bg-gray-100 p-2 mb-4 text-xs font-mono text-gray-600">
                        Debug: Orders Count: {{ orderStore.orders ? orderStore.orders.length : 'null' }} <br>
                        User ID: {{ authStore.user?.id }}
                        <button @click="orderStore.fetchUserOrders()" class="ml-4 bg-blue-500 text-white px-2 py-1 rounded">Force Reload</button>
                    </div>

                    <div v-if="!orderStore.orders || orderStore.orders.length === 0" class="text-gray-500">
                        You have no orders yet. <router-link to="/" class="text-brand-gold hover:underline">Start shopping</router-link>.
                    </div>
                    
                    <div v-else class="space-y-6">
                        <div v-for="order in orderStore.orders" :key="order.id" class="border border-gray-200 rounded-sm overflow-hidden">
                            <div class="bg-gray-50 px-6 py-4 border-b border-gray-200 flex justify-between items-center">
                                <div>
                                    <span class="text-sm text-gray-500">Order #{{ order.id }}</span>
                                    <!-- Date handling optional here if backend sends date -->
                                </div>
                                <div class="font-bold text-gray-900">${{ order.totalAmount }}</div>
                            </div>
                            <div class="p-6">
                                <ul class="space-y-4">
                                    <li v-for="item in order.orderItems" :key="item.id" class="flex items-center">
                                         <div class="h-12 w-12 flex-shrink-0 overflow-hidden rounded-md border border-gray-200">
                                            <img src="https://images.unsplash.com/photo-1620916566398-39f1143ab7be?q=80&w=1887&auto=format&fit=crop" class="h-full w-full object-cover object-center">
                                         </div>
                                         <div class="ml-4">
                                             <p class="text-sm font-medium text-gray-900">{{ item.productName }}</p>
                                             <p class="text-sm text-gray-500">Qty: {{ item.quantity }} | ${{ item.price }}</p>
                                         </div>
                                    </li>
                                </ul>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Addresses Tab (Reusing logic from Checkout roughly, simplified) -->
                <div v-if="activeTab === 'addresses'">
                    <h1 class="text-2xl font-serif text-brand-dark mb-6">My Addresses</h1>
                     <div v-if="addressStore.loading" class="text-gray-500">Loading...</div>
                     <div v-else class="grid grid-cols-1 md:grid-cols-2 gap-6">
                         <div v-for="address in addressStore.addresses" :key="address.id" class="border border-gray-200 p-6 rounded-sm relative group">
                              <h3 class="font-bold text-gray-900">{{ address.title }}</h3>
                              <p class="text-gray-600 mt-2 text-sm">{{ address.fullAddress }}</p>
                              <p class="text-gray-500 text-sm">{{ address.district }}, {{ address.city }}</p>
                              
                              <button @click="addressStore.deleteAddress(address.id)" class="absolute top-4 right-4 text-red-500 opacity-0 group-hover:opacity-100 transition-opacity text-sm hover:underline">
                                  Delete
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
import { ref, onMounted } from 'vue';
import { useAuthStore } from '@/stores/auth';
import { useOrderStore } from '@/stores/order';
import { useAddressStore } from '@/stores/address';
import { useRouter } from 'vue-router';

const authStore = useAuthStore();
const orderStore = useOrderStore();
const addressStore = useAddressStore();
const router = useRouter();

const activeTab = ref('orders');

onMounted(() => {
    if (!authStore.isAuthenticated) {
        router.push('/login');
        return;
    }
    orderStore.fetchUserOrders();
    addressStore.fetchAddresses();
});

const handleLogout = () => {
    authStore.logout();
    router.push('/login');
};
</script>
