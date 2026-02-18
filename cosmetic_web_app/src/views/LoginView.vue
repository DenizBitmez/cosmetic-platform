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
            
            <div class="text-sm">
              <router-link to="/forgot-password" class="font-medium text-brand-gold hover:text-brand-dark transition-colors">
                Forgot your password?
              </router-link>
            </div>
          </div>

          <!-- Session Expired Warning -->
          <div v-if="sessionExpired" class="bg-amber-50 border-l-4 border-amber-500 p-4">
             <div class="flex">
                <div class="ml-3">
                  <p class="text-sm text-amber-700 font-bold">Session Expired</p>
                  <p class="text-sm text-amber-700">Your session has expired. Please log in again.</p>
                </div>
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
 <a href="http://localhost:8081/oauth2/authorization/google" class="group relative w-full flex justify-center py-4 px-4 border border-gray-300 text-sm font-medium rounded-sm text-white bg-brand-dark hover:bg-black focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-brand-gold transition-all duration-300 uppercase tracking-widest">
                <svg class="h-5 w-5 mr-2" viewBox="0 0 24 24" width="24" height="24" xmlns="http://www.w3.org/2000/svg">
                    <g transform="matrix(1, 0, 0, 1, 27.009001, -39.238998)">
                    <path fill="#4285F4" d="M -3.264 51.509 C -3.264 50.719 -3.334 49.969 -3.454 49.239 L -14.754 49.239 L -14.754 53.749 L -8.284 53.749 C -8.574 55.229 -9.424 56.479 -10.684 57.329 L -10.684 60.329 L -6.824 60.329 C -4.564 58.239 -3.264 55.159 -3.264 51.509 Z"/>
                    <path fill="#34A853" d="M -14.754 63.239 C -11.514 63.239 -8.804 62.159 -6.824 60.329 L -10.684 57.329 C -11.764 58.049 -13.134 58.489 -14.754 58.489 C -17.884 58.489 -20.534 56.379 -21.484 53.529 L -25.464 53.529 L -25.464 56.619 C -23.494 60.539 -19.444 63.239 -14.754 63.239 Z"/>
                    <path fill="#FBBC05" d="M -21.484 53.529 C -21.734 52.809 -21.864 52.039 -21.864 51.239 C -21.864 50.439 -21.724 49.669 -21.484 48.949 L -21.484 45.859 L -21.484 45.859 C -26.284 47.479 -26.754 49.299 -26.754 51.239 C -26.754 53.179 -26.284 54.999 -25.464 56.619 L -21.484 53.529 Z"/>
                    <path fill="#EA4335" d="M -14.754 43.989 C -12.984 43.989 -11.404 44.599 -10.154 45.789 L -6.734 42.369 C -8.804 40.429 -11.514 39.239 -14.754 39.239 C -19.444 39.239 -23.494 41.939 -25.464 45.859 L -21.484 48.949 C -20.534 46.099 -17.884 43.989 -14.754 43.989 Z"/>
                    </g>
                </svg>
                Sign in with Google
            </a>
          <div class="space-y-4">
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
import { ref, onMounted } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import { useAuthStore } from '@/stores/auth';

const email = ref('');
const password = ref('');
const router = useRouter();
const route = useRoute();
const authStore = useAuthStore();

const sessionExpired = ref(route.query.session === 'expired');

onMounted(async () => {
    // If session expired, ensure auth state is fully cleared
    if (sessionExpired.value) {
        authStore.logout();
    }

    if (route.query.oauth_email) {
        console.log("Found OAuth Email:", route.query.oauth_email);
        const name = route.query.oauth_name || 'Google User';
        const id = route.query.oauth_id;
        await authStore.handleOAuthLogin(route.query.oauth_email, name, id);
        router.push('/');
    }
});

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
