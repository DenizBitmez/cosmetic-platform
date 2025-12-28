<template>
  <div class="flex min-h-screen bg-brand-cream">
     <!-- Left: Image Section -->
    <div class="hidden lg:flex w-1/2 bg-cover bg-center relative" style="background-image: url('https://images.unsplash.com/photo-1571781535246-83563a6a3d0d?q=80&w=2574&auto=format&fit=crop');">
      <div class="absolute inset-0 bg-black bg-opacity-20 flex items-center justify-center">
        <div class="text-white text-center p-12">
          <h1 class="text-5xl font-bold mb-4 font-serif">Join the Circle</h1>
          <p class="text-xl font-light">Unlock exclusive benefits and curated beauty.</p>
        </div>
      </div>
    </div>

    <!-- Right: Register Form -->
    <div class="w-full lg:w-1/2 flex items-center justify-center p-8 lg:p-24 overflow-y-auto">
      <div class="w-full max-w-md space-y-8">
        <div class="text-center">
             <h2 class="text-sm font-semibold text-brand-gold tracking-widest uppercase mb-2">New Here?</h2>
             <h1 class="text-4xl font-bold text-brand-dark font-serif">Create Account</h1>
        </div>

        <form class="mt-8 space-y-6" @submit.prevent="handleRegister">
          <div class="space-y-4">
            <div>
              <label class="sr-only">Username</label>
              <input v-model="form.username" type="text" required class="appearance-none rounded-none relative block w-full px-4 py-4 border border-gray-300 placeholder-gray-500 text-gray-900 focus:outline-none focus:ring-brand-gold focus:border-brand-gold focus:z-10 sm:text-sm bg-transparent" placeholder="Username">
            </div>
            <div>
              <label class="sr-only">Email address</label>
              <input v-model="form.email" type="email" required class="appearance-none rounded-none relative block w-full px-4 py-4 border border-gray-300 placeholder-gray-500 text-gray-900 focus:outline-none focus:ring-brand-gold focus:border-brand-gold focus:z-10 sm:text-sm bg-transparent" placeholder="Email address">
            </div>
            <div>
              <label class="sr-only">Password</label>
              <input v-model="form.password" type="password" required class="appearance-none rounded-none relative block w-full px-4 py-4 border border-gray-300 placeholder-gray-500 text-gray-900 focus:outline-none focus:ring-brand-gold focus:border-brand-gold focus:z-10 sm:text-sm bg-transparent" placeholder="Password">
            </div>
          </div>

           <div v-if="error" class="bg-red-50 border-l-4 border-red-500 p-4">
            <p class="text-sm text-red-700">{{ error }}</p>
          </div>

          <div>
            <button type="submit" class="group relative w-full flex justify-center py-4 px-4 border border-transparent text-sm font-medium rounded-sm text-white bg-brand-dark hover:bg-black focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-brand-gold transition-all duration-300 uppercase tracking-widest">
              Join Now
            </button>
          </div>
          
           <div class="text-center mt-4">
              <span class="text-gray-500 text-sm">Already a member? </span>
              <router-link to="/login" class="font-medium text-brand-dark hover:text-brand-gold border-b border-brand-dark pb-1 hover:border-brand-gold transition-colors">
                  Sign In
              </router-link>
            </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue';
import { useRouter } from 'vue-router';
import api from '@/services/api';

const form = reactive({
    username: '',
    email: '',
    password: '',
    userType: 'CLIENT'
});
const error = ref(null);
const router = useRouter();

const handleRegister = async () => {
  try {
    await api.post('/register', form);
    router.push('/login');
  } catch (err) {
    error.value = "Registration failed. Please make sure email is unique.";
    console.error(err);
  }
};
</script>
