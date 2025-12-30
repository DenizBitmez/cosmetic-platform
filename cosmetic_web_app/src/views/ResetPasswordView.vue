<template>
  <div class="flex min-h-screen bg-brand-cream items-center justify-center">
    <div class="w-full max-w-md p-8 bg-white shadow-lg space-y-8 border border-gray-100">
      <div class="text-center">
        <h2 class="text-sm font-semibold text-brand-gold tracking-widest uppercase mb-2">Account Recovery</h2>
        <h1 class="text-3xl font-bold text-brand-dark font-serif">Reset Password</h1>
        <p class="mt-2 text-gray-600 font-light">Enter your new password below.</p>
      </div>

      <form class="mt-8 space-y-6" @submit.prevent="handleSubmit">
        <div>
          <label for="password" class="sr-only">New Password</label>
          <input v-model="password" id="password" name="password" type="password" required class="appearance-none rounded-sm relative block w-full px-4 py-4 border border-gray-300 placeholder-gray-500 text-gray-900 focus:outline-none focus:ring-brand-gold focus:border-brand-gold focus:z-10 sm:text-sm" placeholder="New Password">
        </div>
        
        <div>
          <label for="confirmPassword" class="sr-only">Confirm Password</label>
          <input v-model="confirmPassword" id="confirmPassword" name="confirmPassword" type="password" required class="appearance-none rounded-sm relative block w-full px-4 py-4 border border-gray-300 placeholder-gray-500 text-gray-900 focus:outline-none focus:ring-brand-gold focus:border-brand-gold focus:z-10 sm:text-sm" placeholder="Confirm Password">
        </div>

        <div v-if="successMessage" class="bg-green-50 border-l-4 border-green-500 p-4">
             <p class="text-sm text-green-700">{{ successMessage }}</p>
             <router-link to="/login" class="block mt-2 font-bold underline">Go to Login</router-link>
        </div>

        <div v-if="errorMessage" class="bg-red-50 border-l-4 border-red-500 p-4">
             <p class="text-sm text-red-700">{{ errorMessage }}</p>
        </div>

        <div>
          <button type="submit" class="group relative w-full flex justify-center py-4 px-4 border border-transparent text-sm font-medium rounded-sm text-white bg-brand-dark hover:bg-black focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-brand-gold transition-all duration-300 uppercase tracking-widest">
            Reset Password
          </button>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { useAuthStore } from '@/stores/auth';

const password = ref('');
const confirmPassword = ref('');
const route = useRoute();
const router = useRouter();
const authStore = useAuthStore();
const successMessage = ref('');
const errorMessage = ref('');

const handleSubmit = async () => {
    if (!password.value || !confirmPassword.value) {
        errorMessage.value = "Please fill in all fields.";
        return;
    }
    if (password.value !== confirmPassword.value) {
        errorMessage.value = "Passwords do not match.";
        return;
    }
    
    const token = route.query.token;
    if (!token) {
        errorMessage.value = "Invalid or missing token.";
        return;
    }

    errorMessage.value = '';
    
    // Note: authStore call
    // We update authStore to use raw text body or json? 
    // Backend expects RequestBody String. Axios might send JSON by default if we pass object.
    // In store I added { headers: { 'Content-Type': 'text/plain' } }
    
    const success = await authStore.resetPassword(token, password.value);

    if (success) {
        successMessage.value = "Password reset successfully.";
        // Optional: redirect after delay
        setTimeout(() => router.push('/login'), 3000);
    } else {
        errorMessage.value = authStore.error || "Failed to reset password.";
    }
};
</script>
