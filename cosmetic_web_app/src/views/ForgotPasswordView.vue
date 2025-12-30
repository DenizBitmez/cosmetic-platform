<template>
  <div class="flex min-h-screen bg-brand-cream items-center justify-center">
    <div class="w-full max-w-md p-8 bg-white shadow-lg space-y-8 border border-gray-100">
      <div class="text-center">
        <h2 class="text-sm font-semibold text-brand-gold tracking-widest uppercase mb-2">Account Recovery</h2>
        <h1 class="text-3xl font-bold text-brand-dark font-serif">Forgot Password?</h1>
        <p class="mt-2 text-gray-600 font-light">Enter your email to receive a reset link.</p>
      </div>

      <form class="mt-8 space-y-6" @submit.prevent="handleSubmit">
        <div>
          <label for="email" class="sr-only">Email address</label>
          <input v-model="email" id="email" name="email" type="email" autocomplete="email" required class="appearance-none rounded-sm relative block w-full px-4 py-4 border border-gray-300 placeholder-gray-500 text-gray-900 focus:outline-none focus:ring-brand-gold focus:border-brand-gold focus:z-10 sm:text-sm" placeholder="Email address">
        </div>

        <div v-if="successMessage" class="bg-green-50 border-l-4 border-green-500 p-4">
             <p class="text-sm text-green-700">{{ successMessage }}</p>
        </div>

        <div v-if="authStore.error" class="bg-red-50 border-l-4 border-red-500 p-4">
             <p class="text-sm text-red-700">{{ authStore.error }}</p>
        </div>

        <div>
          <button type="submit" class="group relative w-full flex justify-center py-4 px-4 border border-transparent text-sm font-medium rounded-sm text-white bg-brand-dark hover:bg-black focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-brand-gold transition-all duration-300 uppercase tracking-widest">
            Send Reset Link
          </button>
        </div>
        
         <div class="text-center mt-4">
            <router-link to="/login" class="font-medium text-gray-500 hover:text-brand-dark transition-colors">
                Back to Login
            </router-link>
          </div>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { useAuthStore } from '@/stores/auth';

const email = ref('');
const authStore = useAuthStore();
const successMessage = ref('');

const handleSubmit = async () => {
    if (!email.value) return;
    
    authStore.error = null;
    successMessage.value = '';

    const success = await authStore.forgotPassword(email.value);

    if (success) {
        successMessage.value = "If an account exists, a reset link has been sent.";
    }
};
</script>
