<template>
  <div class="flex min-h-screen bg-brand-cream">
    <!-- Left: Image Section -->
    <div class="hidden lg:flex w-1/2 bg-cover bg-center relative" style="background-image: url('https://images.unsplash.com/photo-1612817288484-92913477491d?q=80&w=2574&auto=format&fit=crop');">
      <div class="absolute inset-0 bg-black bg-opacity-30 flex items-center justify-center">
        <div class="text-white text-center p-12">
          <h1 class="text-5xl font-bold mb-4 font-serif">Essence of Beauty</h1>
          <p class="text-xl font-light">Discover the luxury you deserve.</p>
        </div>
      </div>
    </div>

    <!-- Right: Login Form -->
    <div class="w-full lg:w-1/2 flex items-center justify-center p-8 lg:p-24">
      <div class="w-full max-w-md space-y-8">
        <div class="text-center">
             <h2 class="text-sm font-semibold text-brand-gold tracking-widest uppercase mb-2">Welcome Back</h2>
             <h1 class="text-4xl font-bold text-brand-dark font-serif">Member Login</h1>
        </div>

        <form class="mt-8 space-y-6" @submit.prevent="handleLogin">
          <div class="space-y-4">
            <div>
              <label for="email" class="sr-only">Email address</label>
              <input v-model="email" id="email" name="email" type="email" autocomplete="email" required class="appearance-none rounded-none relative block w-full px-4 py-4 border border-gray-300 placeholder-gray-500 text-gray-900 focus:outline-none focus:ring-brand-gold focus:border-brand-gold focus:z-10 sm:text-sm bg-transparent" placeholder="Email address">
            </div>
            <div>
              <label for="password" class="sr-only">Password</label>
              <input v-model="password" id="password" name="password" type="password" autocomplete="current-password" required class="appearance-none rounded-none relative block w-full px-4 py-4 border border-gray-300 placeholder-gray-500 text-gray-900 focus:outline-none focus:ring-brand-gold focus:border-brand-gold focus:z-10 sm:text-sm bg-transparent" placeholder="Password">
            </div>
          </div>

          <div class="flex items-center justify-between">
            <div class="flex items-center">
              <input id="remember-me" name="remember-me" type="checkbox" class="h-4 w-4 text-brand-gold focus:ring-brand-gold border-gray-300 rounded">
              <label for="remember-me" class="ml-2 block text-sm text-gray-900"> Remember me </label>
            </div>
          </div>

          <!-- Error Message Display -->
          <div v-if="authStore.error" class="bg-red-50 border-l-4 border-red-500 p-4">
             <div class="flex">
                <div class="ml-3">
                  <p class="text-sm text-red-700 font-bold">Login Failed</p>
                  <p class="text-sm text-red-700">{{ authStore.error }}</p>
                </div>
             </div>
          </div>

          <div>
            <button type="submit" class="group relative w-full flex justify-center py-4 px-4 border border-transparent text-sm font-medium rounded-sm text-white bg-brand-dark hover:bg-black focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-brand-gold transition-all duration-300 uppercase tracking-widest">
              Sign In
            </button>
          </div>
          
           <div class="text-center mt-4">
              <router-link to="/register" class="font-medium text-brand-dark hover:text-brand-gold border-b border-brand-dark pb-1 hover:border-brand-gold transition-colors">
                  Create an Account
              </router-link>
            </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import { useAuthStore } from '@/stores/auth';

const email = ref('');
const password = ref('');
const router = useRouter();
const authStore = useAuthStore();

const handleLogin = async () => {
    if (!email.value || !password.value) return;
    
    // Logging for debugging
    console.log("Attempting login with:", email.value);
    
    const success = await authStore.login(email.value, password.value);

    if (success) {
        console.log("Login success, redirecting...");
        router.push('/');
    } else {
        console.error("Login failed:", authStore.error);
    }
};
</script>
