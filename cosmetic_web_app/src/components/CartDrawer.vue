<template>
  <div v-if="cartStore.drawerOpen" class="relative z-[100]" aria-labelledby="slide-over-title" role="dialog" aria-modal="true">
    <!-- Background backdrop -->
    <div class="fixed inset-0 bg-black bg-opacity-40 transition-opacity"></div>

    <div class="fixed inset-0 overflow-hidden">
      <div class="absolute inset-0 overflow-hidden">
        <div class="pointer-events-none fixed inset-y-0 right-0 flex max-w-full pl-10">
          <div class="pointer-events-auto w-screen max-w-md">
            <div class="flex h-full flex-col overflow-y-scroll bg-white shadow-xl">
              <div class="flex-1 overflow-y-auto px-4 py-6 sm:px-6">
                <div class="flex items-start justify-between">
                  <h2 class="text-lg font-medium text-gray-900 font-serif" id="slide-over-title">Shopping Cart</h2>
                  <div class="ml-3 flex h-7 items-center">
                    <button @click="cartStore.toggleDrawer(false)" type="button" class="-m-2 p-2 text-gray-400 hover:text-gray-500">
                      <span class="sr-only">Close panel</span>
                      <svg class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" aria-hidden="true">
                        <path stroke-linecap="round" stroke-linejoin="round" d="M6 18L18 6M6 6l12 12" />
                      </svg>
                    </button>
                  </div>
                </div>

                <div class="mt-8">
                  <div class="flow-root">
                    <ul role="list" class="-my-6 divide-y divide-gray-200">
                      <li v-if="!cartStore.cart || cartStore.cart.cartItems.length === 0" class="py-6 text-center text-gray-500">
                        Your cart is empty.
                      </li>
                      <li v-else v-for="item in cartStore.cart.cartItems" :key="item.id" class="flex py-6">
                        <div class="h-24 w-24 flex-shrink-0 overflow-hidden rounded-md border border-gray-200">
                          <img src="https://images.unsplash.com/photo-1620916566398-39f1143ab7be?q=80&w=1887&auto=format&fit=crop" class="h-full w-full object-cover object-center">
                        </div>

                        <div class="ml-4 flex flex-1 flex-col">
                          <div>
                            <div class="flex justify-between text-base font-medium text-gray-900">
                              <h3>
                                <a href="#">{{ item.product.name }}</a>
                              </h3>
                              <p class="ml-4 text-brand-gold">${{ item.product.price }}</p>
                            </div>
                            <p class="mt-1 text-sm text-gray-500">{{ item.product.category }}</p>
                          </div>
                          <div class="flex flex-1 items-end justify-between text-sm">
                            <p class="text-gray-500">Qty {{ item.quantity }}</p>

                            <div class="flex">
                              <button @click="cartStore.removeFromCart(item.id)" type="button" class="font-medium text-red-600 hover:text-red-500">Remove</button>
                            </div>
                          </div>
                        </div>
                      </li>
                    </ul>
                  </div>
                </div>
              </div>

              <div class="border-t border-gray-200 px-4 py-6 sm:px-6">
                <div class="flex justify-between text-base font-medium text-gray-900">
                  <p>Subtotal</p>
                  <p>${{ cartStore.totalAmount }}</p>
                </div>
                <p class="mt-0.5 text-sm text-gray-500">Shipping and taxes calculated at checkout.</p>
                <div class="mt-6">
                  <router-link to="/checkout" @click="cartStore.toggleDrawer(false)" class="flex items-center justify-center rounded-md border border-transparent bg-brand-dark px-6 py-3 text-base font-medium text-white shadow-sm hover:bg-brand-gold transition-colors uppercase tracking-widest">
                      Checkout
                  </router-link>
                </div>
                <div class="mt-6 flex justify-center text-center text-sm text-gray-500">
                  <p>
                    or
                    <button @click="cartStore.toggleDrawer(false)" type="button" class="font-medium text-brand-dark hover:text-brand-gold">
                      Continue Shopping
                      <span aria-hidden="true"> &rarr;</span>
                    </button>
                  </p>
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
import { useCartStore } from '@/stores/cart';
const cartStore = useCartStore();
</script>
