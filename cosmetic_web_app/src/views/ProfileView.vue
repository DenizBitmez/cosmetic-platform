<template>
  <div class="min-h-screen bg-gray-50 pt-8 pb-16">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
      
      <!-- Header -->
      <div class="mb-8 flex items-center justify-between">
         <div>
             <h1 class="text-3xl font-serif text-brand-dark">My Account</h1>
             <p class="text-gray-500 mt-1">Welcome back, {{ authStore.user?.username }}</p>
         </div>
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
                        <p class="text-sm text-gray-600 mb-4"><strong class="font-medium text-gray-900">Email:</strong> {{ authStore.user?.email }}</p>
                        
                        <div class="border-t border-brand-gold/20 pt-4 mt-2">
                           <h4 class="text-xs font-bold uppercase tracking-wider text-brand-dark mb-2 flex items-center gap-2">
                               <PhWarning :size="14" class="text-red-500" />
                               My Allergies
                           </h4>
                           <p class="text-[10px] text-gray-500 mb-3 italic">Enter ingredients you are allergic to (comma separated).</p>
                           <div class="flex gap-2">
                              <input 
                                 v-model="userAllergies" 
                                 type="text" 
                                 placeholder="e.g. Paraben, Alcohol" 
                                 class="flex-1 bg-white border border-brand-gold/20 rounded-lg px-3 py-1.5 text-xs focus:ring-1 focus:ring-brand-gold outline-none"
                              >
                              <button 
                                 @click="saveAllergies" 
                                 :disabled="savingAllergies"
                                 class="bg-brand-dark text-white px-4 py-1.5 rounded-lg text-xs font-bold hover:bg-black disabled:opacity-50 transition-all"
                              >
                                 {{ savingAllergies ? '...' : 'Save' }}
                              </button>
                           </div>
                        </div>
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
                                <button @click="currentTab = 'orders'" class="w-full bg-brand-dark border border-gray-300 text-white text-sm py-2 rounded-md hover:bg-black transition-colors">
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
                     <button @click="showCardModal = true" class="text-sm bg-brand-dark text-white px-3 py-1.5 rounded hover:bg-black transition-colors">Add New Card</button>
                </div>
                <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
                    <div v-for="card in wallet" :key="card.id" class="border rounded-lg p-4 bg-gradient-to-br from-gray-800 to-gray-900 text-white relative overflow-hidden">
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

            <!-- MY BEAUTY SHELF TAB -->
            <div v-else-if="currentTab === 'shelf'" class="space-y-6">
                <div class="flex justify-between items-center border-b pb-4">
                     <h2 class="text-xl font-bold text-gray-900">My Cabinet</h2>
                     <div class="flex items-center gap-3">
                        <button @click="checkConflicts" :disabled="checkingConflicts" class="text-[10px] font-bold uppercase tracking-widest bg-brand-gold/10 text-brand-gold px-3 py-1.5 rounded-full hover:bg-brand-gold hover:text-white transition-all border border-brand-gold/20">
                           {{ checkingConflicts ? 'Checking...' : 'Check Routine Conflicts' }}
                        </button>
                        <p class="text-[10px] text-brand-dark italic">Track expiry and product life</p>
                     </div>
                </div>

                <!-- Combination Warning UI -->
                <transition name="fade">
                    <div v-if="conflictWarnings.length > 0" class="bg-red-50 border-l-4 border-red-500 p-5 rounded-r-xl shadow-sm mb-8">
                        <div class="flex items-start gap-4">
                            <div class="bg-red-100 p-2 rounded-full text-red-600 flex-shrink-0">
                                <PhWarning :size="24" weight="bold" />
                            </div>
                            <div class="flex-1">
                                <h3 class="text-sm font-bold text-red-800 uppercase tracking-wider mb-1">Routine Conflict Detected!</h3>
                                <p class="text-xs text-red-600 mb-3 font-medium">Using these together can cause irritation or cancel out benefits:</p>
                                <ul class="space-y-2">
                                    <li v-for="(warn, idx) in conflictWarnings" :key="idx" class="text-[11px] text-red-700 bg-white/50 p-2 rounded border border-red-100 shadow-sm leading-relaxed">
                                        ✨ {{ warn }}
                                    </li>
                                </ul>
                            </div>
                            <button @click="conflictWarnings = []" class="text-red-400 hover:text-red-600">
                                <PhTrash :size="18" />
                            </button>
                        </div>
                    </div>
                </transition>
                
                <div v-if="shelfProducts.length === 0" class="text-center py-20 bg-gray-50 rounded-2xl border-2 border-dashed border-gray-200">
                     <PhClockCounterClockwise class="mx-auto h-16 w-16 text-gray-300 mb-4" />
                     <h3 class="text-lg font-serif text-gray-800">Your Beauty Cabinet is Empty</h3>
                     <p class="text-sm text-gray-500 max-w-xs mx-auto mt-2">Add products to track their shelf life and detect active ingredient conflicts.</p>
                </div>

                <div v-else class="grid grid-cols-1 md:grid-cols-2 gap-8">
                    <div v-for="up in shelfProducts" :key="up.id" class="relative bg-white border rounded-3xl p-6 hover:shadow-xl transition-all group overflow-hidden" :class="up.expired ? 'border-red-200 bg-red-50/10' : 'border-gray-100'">
                        <!-- Progress Background -->
                        <div class="absolute bottom-0 left-0 h-1 bg-gray-100 w-full overflow-hidden">
                            <div 
                                class="h-full transition-all duration-1000" 
                                :class="getStatusColor(calculateRemainingLife(up.openedDate, up.expiryDate))"
                                :style="{ width: calculateRemainingLife(up.openedDate, up.expiryDate) + '%' }"
                            ></div>
                        </div>

                        <div class="flex gap-5">
                            <div class="w-24 h-24 bg-gray-100 rounded-2xl bg-cover bg-center shadow-sm flex-shrink-0" :style="{ backgroundImage: 'url(' + (up.imageUrl || up.product?.image || 'https://images.unsplash.com/photo-1556228578-0d85b1a4d571?w=200') + ')' }"></div>
                            <div class="flex-1 min-w-0">
                                <div class="flex justify-between items-start">
                                    <div>
                                        <h4 class="font-bold text-gray-900 text-base leading-tight truncate pr-6">{{ up.customName }}</h4>
                                        <p class="text-[10px] text-brand-gold font-bold uppercase tracking-widest mt-1">{{ up.brand }}</p>
                                    </div>
                                    <button @click="removeFromShelf(up.id)" class="text-gray-300 hover:text-red-500 transition-colors p-1">
                                        <PhTrash :size="18" />
                                    </button>
                                </div>
                                
                                <div class="mt-4 grid grid-cols-2 gap-3">
                                    <div class="bg-gray-50 p-2 rounded-xl text-center">
                                        <p class="text-[9px] text-gray-400 uppercase font-bold">Opened</p>
                                        <p class="text-[11px] font-bold text-gray-700">{{ up.openedDate }}</p>
                                    </div>
                                    <div class="bg-gray-50 p-2 rounded-xl text-center" :class="up.expired ? 'bg-red-50 text-red-700' : ''">
                                        <p class="text-[9px] text-gray-400 uppercase font-bold">Expires</p>
                                        <p class="text-[11px] font-bold" :class="up.expired ? 'text-red-600' : 'text-green-600'">{{ up.expiryDate }}</p>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <!-- Remaining Days Overlay -->
                        <div class="mt-4 flex items-center justify-between">
                            <span class="text-[10px] font-bold text-gray-400 uppercase tracking-wider">Product Status</span>
                            <span v-if="!up.expired" class="text-[10px] font-bold text-gray-600 bg-gray-100 px-2 py-0.5 rounded-full">
                                {{ Math.round(calculateRemainingLife(up.openedDate, up.expiryDate)) }}% Ready
                            </span>
                            <span v-else class="text-[10px] font-bold text-red-600 bg-red-100 px-2 py-0.5 rounded-full">
                                EXPIRED
                            </span>
                        </div>
                        
                        <div v-if="up.expired" class="mt-3 bg-red-600 text-white p-2 rounded-xl text-center text-[11px] font-bold animate-pulse">
                           ⚠️ REPLACE IMMEDIATELY
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
                             <div class="h-40 bg-cover bg-center group-hover:opacity-75" :style="{ backgroundImage: 'url(' + (product.productImage || product.image || 'https://images.unsplash.com/photo-1620916566398-39f1143ab7be?q=80&w=200') + ')' }"></div>
                        </div>
                        <h3 class="mt-2 text-sm text-gray-700 truncate font-medium">{{ product.productName || product.name }}</h3>
                        <p class="mt-1 text-lg font-bold text-brand-gold">${{ product.productPrice || product.price }}</p>
                        <p class="text-[10px] text-gray-400 uppercase tracking-tighter">{{ product.productBrand || 'Premium' }}</p>
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

      <!-- Add Address Modal (Teleport to Body for better stacking) -->
      <teleport to="body">
        <transition name="fade">
            <div v-if="showAddressModal" class="fixed inset-0 z-[60] flex items-center justify-center p-4 bg-black/60 backdrop-blur-sm overflow-y-auto">
                <div class="bg-white rounded-3xl shadow-2xl max-w-lg w-full p-8 relative animate-in fade-in zoom-in duration-300">
                    <button @click="showAddressModal = false" class="absolute top-6 right-6 text-gray-400 hover:text-gray-600">
                        <PhX :size="24" />
                    </button>
                    
                    <h3 class="text-2xl font-serif text-gray-900 mb-6">Add New Address</h3>
                    
                    <form @submit.prevent="saveAddress" class="space-y-5">
                        <div class="space-y-1">
                            <label class="block text-xs font-bold text-gray-400 uppercase tracking-widest">Address Title</label>
                            <input v-model="newAddress.title" type="text" placeholder="e.g. Home, Office" required class="w-full bg-gray-50 border border-gray-100 rounded-xl px-4 py-3 text-sm focus:ring-2 focus:ring-brand-gold outline-none transition-all">
                        </div>
                        
                        <div class="grid grid-cols-2 gap-4">
                            <div class="space-y-1">
                                <label class="block text-xs font-bold text-gray-400 uppercase tracking-widest">City</label>
                                <input v-model="newAddress.city" type="text" required class="w-full bg-gray-50 border border-gray-100 rounded-xl px-4 py-3 text-sm focus:ring-2 focus:ring-brand-gold outline-none transition-all">
                            </div>
                            <div class="space-y-1">
                                <label class="block text-xs font-bold text-gray-400 uppercase tracking-widest">District</label>
                                <input v-model="newAddress.district" type="text" required class="w-full bg-gray-50 border border-gray-100 rounded-xl px-4 py-3 text-sm focus:ring-2 focus:ring-brand-gold outline-none transition-all">
                            </div>
                        </div>

                        <div class="space-y-1">
                            <label class="block text-xs font-bold text-gray-400 uppercase tracking-widest">Full Address Details</label>
                            <textarea v-model="newAddress.fullAddress" rows="4" required placeholder="Street, Apartment No, Floor..." class="w-full bg-gray-50 border border-gray-100 rounded-xl px-4 py-3 text-sm focus:ring-2 focus:ring-brand-gold outline-none transition-all resize-none"></textarea>
                        </div>

                        <div class="flex gap-4 pt-4">
                            <button type="button" @click="showAddressModal = false" class="flex-1 px-6 py-3 rounded-xl text-sm font-bold text-gray-500 bg-gray-50 hover:bg-gray-100 transition-all">
                                Cancel
                            </button>
                            <button type="submit" class="flex-1 px-6 py-3 rounded-xl text-sm font-bold text-white bg-black hover:bg-gray-800 transition-all shadow-lg">
                                Save Address
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </transition>
      </teleport>

      <!-- Add Card Modal -->
      <teleport to="body">
        <transition name="fade">
            <div v-if="showCardModal" class="fixed inset-0 z-[60] flex items-center justify-center p-4 bg-black/60 backdrop-blur-sm overflow-y-auto">
                <div class="bg-white rounded-3xl shadow-2xl max-w-sm w-full p-8 relative animate-in fade-in zoom-in duration-300">
                    <button @click="showCardModal = false" class="absolute top-6 right-6 text-gray-400 hover:text-gray-600">
                        <PhX :size="24" />
                    </button>
                    
                    <h3 class="text-2xl font-serif text-gray-900 mb-6 text-center">Add New Card</h3>
                    
                    <!-- Interactive Card Preview -->
                    <div class="perspective-1000 mb-8 mx-auto w-full max-w-[300px] h-[180px]">
                        <div class="relative w-full h-full transition-transform duration-700 preserve-3d" :class="{ 'rotate-y-180': isCardFlipped }">
                            <!-- Front -->
                            <div class="absolute inset-0 backface-hidden bg-gradient-to-br from-gray-800 to-gray-900 rounded-2xl p-6 text-white shadow-xl flex flex-col justify-between overflow-hidden">
                                <div class="absolute top-0 right-0 p-4 opacity-10"><PhCreditCard size="120" /></div>
                                <div class="flex justify-between items-start relative z-10">
                                    <div class="w-10 h-8 bg-brand-gold/20 rounded-md border border-brand-gold/30 flex items-center justify-center">
                                         <div class="w-6 h-4 bg-brand-gold/40 rounded-sm"></div>
                                    </div>
                                    <span class="text-xs font-bold italic tracking-widest text-brand-gold/70 uppercase">{{ getCardBrand(newCard.number) }}</span>
                                </div>
                                <div class="text-lg font-mono tracking-[0.2em] relative z-10">{{ formatCardNumber(newCard.number) }}</div>
                                <div class="flex justify-between items-end relative z-10">
                                    <div class="flex flex-col">
                                        <span class="text-[8px] uppercase text-gray-400">Card Holder</span>
                                        <span class="text-xs font-bold truncate max-w-[140px] uppercase">{{ newCard.holder || 'YOUR NAME' }}</span>
                                    </div>
                                    <div class="flex flex-col items-end">
                                        <span class="text-[8px] uppercase text-gray-400">Expires</span>
                                        <span class="text-xs font-bold">{{ newCard.expiry || 'MM/YY' }}</span>
                                    </div>
                                </div>
                            </div>
                            <!-- Back -->
                            <div class="absolute inset-0 backface-hidden rotate-y-180 bg-gradient-to-br from-gray-700 to-gray-800 rounded-2xl text-white shadow-xl overflow-hidden py-4">
                                <div class="bg-gray-900 h-10 w-full mb-6"></div>
                                <div class="px-6">
                                    <div class="text-[8px] uppercase text-gray-400 mb-1 text-right">CVV</div>
                                    <div class="bg-white text-gray-900 h-8 rounded-md flex items-center justify-end px-3 font-mono font-bold tracking-widest text-sm italic shadow-inner">
                                        {{ newCard.cvv || '***' }}
                                    </div>
                                </div>
                                <div class="mt-4 px-6 opacity-20"><PhCreditCard size="48" /></div>
                            </div>
                        </div>
                    </div>
                    
                    <form @submit.prevent="saveCard" class="space-y-5">
                        <div class="space-y-1">
                            <label class="block text-xs font-bold text-gray-400 uppercase tracking-widest">Card Holder</label>
                            <input v-model="newCard.holder" type="text" placeholder="NAME SURNAME" required class="w-full bg-gray-50 border border-gray-100 rounded-xl px-4 py-3 text-sm focus:ring-2 focus:ring-brand-gold outline-none transition-all uppercase">
                        </div>
                        
                        <div class="space-y-1">
                            <label class="block text-xs font-bold text-gray-400 uppercase tracking-widest">Card Number</label>
                            <input v-model="newCard.number" type="text" placeholder="0000 0000 0000 0000" maxlength="16" required @input="handleNumberInput" class="w-full bg-gray-50 border border-gray-100 rounded-xl px-4 py-3 text-sm focus:ring-2 focus:ring-brand-gold outline-none transition-all font-mono">
                        </div>
                        
                        <div class="grid grid-cols-2 gap-4">
                            <div class="space-y-1">
                                <label class="block text-xs font-bold text-gray-400 uppercase tracking-widest">Expiry</label>
                                <input v-model="newCard.expiry" type="text" placeholder="MM/YY" maxlength="5" required @input="handleExpiryInput" class="w-full bg-gray-50 border border-gray-100 rounded-xl px-4 py-3 text-sm focus:ring-2 focus:ring-brand-gold outline-none transition-all">
                            </div>
                            <div class="space-y-1">
                                <label class="block text-xs font-bold text-gray-400 uppercase tracking-widest">CVV</label>
                                <input v-model="newCard.cvv" type="password" placeholder="***" maxlength="3" required @input="handleCvvInput" @focus="isCardFlipped = true" @blur="isCardFlipped = false" class="w-full bg-gray-50 border border-gray-100 rounded-xl px-4 py-3 text-sm focus:ring-2 focus:ring-brand-gold outline-none transition-all">
                            </div>
                        </div>

                        <div class="flex gap-4 pt-4">
                            <button type="button" @click="showCardModal = false" class="flex-1 px-6 py-3 rounded-xl text-sm font-bold text-gray-500 bg-gray-50 hover:bg-gray-100 transition-all">
                                Cancel
                            </button>
                            <button type="submit" class="flex-1 px-6 py-3 rounded-xl text-sm font-bold text-white bg-black hover:bg-gray-800 transition-all shadow-lg">
                                Save Card
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </transition>
      </teleport>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue';
import { useAuthStore } from '@/stores/auth';
import { useUiStore } from '@/stores/ui';
import { useRouter } from 'vue-router';
import api from '@/services/api';
import { 
    PhUser, PhPackage, PhMapPin, PhArrowsClockwise, 
    PhCreditCard, PhTag, PhClockCounterClockwise, 
    PhQuestion, PhRobot, PhStar, PhTrash, PhWarning, PhX
} from '@phosphor-icons/vue';

const authStore = useAuthStore();
const uiStore = useUiStore();
const router = useRouter();

const currentTab = ref('overview');
const showAddressModal = ref(false);
const showCardModal = ref(false);
const isCardFlipped = ref(false);
const userAllergies = ref(authStore.user?.allergies || '');
const savingAllergies = ref(false);

const saveAllergies = async () => {
    savingAllergies.value = true;
    const success = await authStore.updateAllergies(userAllergies.value);
    if (success) {
        uiStore.notify("Allergies updated successfully!");
    } else {
        uiStore.notify("Failed to update allergies.", 'error');
    }
    savingAllergies.value = false;
};

const navItems = [
    { id: 'overview', name: 'Overview', icon: PhUser },
    { id: 'shelf', name: 'My Cabinet', icon: PhClockCounterClockwise },
    { id: 'orders', name: 'My Orders', icon: PhPackage },
    { id: 'addresses', name: 'My Addresses', icon: PhMapPin },
    { id: 'wallet', name: 'Saved Cards', icon: PhCreditCard },
    { id: 'coupons', name: 'My Coupons', icon: PhTag },
    { id: 'viewed', name: 'Recently Viewed', icon: PhArrowsClockwise },
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
const recentlyViewed = ref([]);
const wallet = ref(mockCards);
const coupons = ref(mockCoupons);
const shelfProducts = ref([]);
const faqs = ref(mockFaqs);

const newAddress = reactive({
    title: '',
    city: '',
    district: '',
    fullAddress: ''
});

const newCard = reactive({
    number: '',
    expiry: '',
    cvv: '',
    holder: ''
});

const saveCard = () => {
    if (newCard.number.length < 16) {
        uiStore.notify("Please enter a complete 16-digit card number.", 'error');
        return;
    }

    // Expiry Validation
    if (newCard.expiry.includes('/')) {
        const parts = newCard.expiry.split('/');
        const month = parseInt(parts[0]);
        const yearStr = parts[1] || '';
        const year = parseInt(yearStr);
        
        if (isNaN(month) || month < 1 || month > 12) {
            uiStore.notify("Invalid month. Use 01-12.", 'warning');
            return;
        }

        if (yearStr.length < 2) {
            uiStore.notify("Please enter a 2-digit year.", 'warning');
            return;
        }

        if (isNaN(year) || year < 25) {
            uiStore.notify("Expiry year must be 2025 or later.", 'error');
            return;
        }
    } else {
        uiStore.notify("Please enter a complete expiry date (MM/YY).", 'error');
        return;
    }

    if (newCard.cvv.length < 3) {
        uiStore.notify("Please enter a complete CVV.", 'error');
        return;
    }
    
    // Simple simulation
    const last4 = newCard.number.slice(-4);
    const brand = newCard.number.startsWith('4') ? 'Visa' : 'Mastercard';
    
    wallet.value.push({
        id: Date.now(),
        brand: brand,
        last4: last4,
        expiry: newCard.expiry || '12/29'
    });
    
    showCardModal.value = false;
    uiStore.notify("Card successfully added to your wallet! ✨");
    
    // Reset
    newCard.number = '';
    newCard.expiry = '';
    newCard.cvv = '';
    newCard.holder = '';
};

const handleExpiryInput = (e) => {
    let val = e.target.value.replace(/\D/g, ''); // Remove non-digits
    
    // Month validation
    if (val.length >= 2) {
        let month = parseInt(val.substring(0, 2));
        if (month > 12) {
            uiStore.notify("Month cannot be greater than 12", 'warning');
            val = '12' + val.substring(2);
        } else if (month === 0 && val.length === 2) {
             uiStore.notify("Invalid month", 'warning');
             val = '01';
        }
    }

    if (val.length >= 2) {
        val = val.substring(0, 2) + '/' + val.substring(2, 4);
    }
    newCard.expiry = val;
};

const handleNumberInput = (e) => {
    let val = e.target.value;
    // Strictly numeric only
    const numeric = val.replace(/\D/g, '');
    
    if (val !== numeric) {
        uiStore.notify("Card number must contain digits only.", 'warning');
    }
    newCard.number = numeric;
};

const handleCvvInput = (e) => {
    let val = e.target.value;
    const numeric = val.replace(/\D/g, '');
    if (val !== numeric) {
        uiStore.notify("CVV must contain digits only.", 'warning');
    }
    newCard.cvv = numeric;
};

const formatCardNumber = (val) => {
    let res = val.replace(/\s?/g, '').replace(/(\d{4})/g, '$1 ').trim();
    return res || '•••• •••• •••• ••••';
};

const getCardBrand = (val) => {
    if (val.startsWith('4')) return 'Visa';
    if (val.startsWith('5')) return 'Mastercard';
    return '';
};

const calculateRemainingLife = (openedDate, expiryDate) => {
    if (!openedDate || !expiryDate) return 0;
    const start = new Date(openedDate).getTime();
    const end = new Date(expiryDate).getTime();
    const now = new Date().getTime();
    
    const total = end - start;
    const remaining = end - now;
    
    if (remaining <= 0) return 0;
    return Math.max(0, Math.min(100, (remaining / total) * 100));
};

const getStatusColor = (percent) => {
    if (percent > 50) return 'bg-green-500';
    if (percent > 20) return 'bg-yellow-500';
    return 'bg-red-500';
};
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
            uiStore.notify("Failed to add address", 'error');
        }
    }
};

const conflictWarnings = ref([]);
const checkingConflicts = ref(false);

const checkConflicts = async () => {
    if (shelfProducts.value.length < 2) {
        uiStore.notify("Add at least 2 products to your cabinet for conflict check.", 'info');
        return;
    }
    checkingConflicts.value = true;
    conflictWarnings.value = [];
    try {
        const ingredients = shelfProducts.value.flatMap(p => {
            const fromProduct = p.product?.ingredients?.map(i => i.inciName || i.inci_name) || [];
            
            // Fallback to parsing description if ingredients list is empty
            const fromDesc = (p.product?.description || '')
                .split(/[, \n.()]+/)
                .filter(word => word.length > 3 && /^[A-Z][a-z]/.test(word)); // Rough heuristic for chemical names

            const fromText = p.ingredientsText ? p.ingredientsText.split(/[, \n]+/).filter(i => i.trim().length > 0) : [];
            
            return [...new Set([...fromProduct, ...fromDesc, ...fromText])];
        });

        const activeIngredients = ingredients.filter(i => i && i.length > 2);

        if (activeIngredients.length === 0) {
            uiStore.notify("No ingredient signatures detected. Provide a description or use catalog products.", 'warning');
            return;
        }
        
        const res = await api.post('/analysis/check-conflicts', { ingredients: activeIngredients });
        conflictWarnings.value = res.data;
        if (conflictWarnings.value.length === 0) {
            uiStore.notify("No conflicts detected! Your current routine looks safe. ✨");
        }
    } catch (e) {
        console.error("Conflict check failed", e);
        uiStore.notify("Failed to perform conflict check.", 'error');
    } finally {
        checkingConflicts.value = false;
    }
};

const fetchShelf = async () => {
    if (authStore.user?.id) {
        try {
            const res = await api.get(`/shelf/user/${authStore.user.id}`);
            shelfProducts.value = res.data;
        } catch (e) {
            console.error("Failed to fetch shelf", e);
        }
    }
};

const removeFromShelf = async (id) => {
    const ok = await uiStore.confirm("Remove Product", "Remove this product from your cabinet?");
    if (ok) {
        try {
            await api.delete(`/shelf/${id}`);
            await fetchShelf();
            uiStore.notify("Product removed from cabinet.");
        } catch (e) {
             console.error("Failed to remove from shelf", e);
             uiStore.notify("Failed to remove product.", 'error');
        }
    }
}

const deleteAddress = async (id) => {
    const ok = await uiStore.confirm("Delete Address", "Are you sure you want to delete this address?");
    if(ok){
        try {
            await api.delete(`/address/${id}`);
            await fetchAddresses();
            uiStore.notify("Address deleted successfully.");
        } catch (e) {
            console.error("Failed to delete address", e);
            uiStore.notify("Failed to delete address.", 'error');
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

        // Fetch Recently Viewed
        try {
            const res = await api.get(`/history/viewed/${authStore.user.id}`);
            recentlyViewed.value = res.data;
        } catch (e) {
            console.error("Failed to fetch recently viewed", e);
        }
        await fetchShelf();
    }
});
</script>

<style scoped>
.perspective-1000 {
    perspective: 1000px;
}
.preserve-3d {
    transform-style: preserve-3d;
}
.backface-hidden {
    backface-visibility: hidden;
}
.rotate-y-180 {
    transform: rotateY(180deg);
}

.fade-enter-active,
.fade-leave-active {
    transition: opacity 0.3s ease;
}
.fade-enter-from,
.fade-leave-to {
    opacity: 0;
}
</style>
