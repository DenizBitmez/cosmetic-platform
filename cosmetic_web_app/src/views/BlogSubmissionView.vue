<template>
  <div class="min-h-screen bg-[#FDFBF7] pt-12 pb-20">
    <div class="max-w-3xl mx-auto px-4 sm:px-6">
      <div class="text-center mb-12">
          <h1 class="text-4xl font-serif text-brand-dark mb-4">Share Your Wisdom</h1>
          <p class="text-gray-500 font-light italic">Write an article, inspire the community. Your post will be published after expert/admin review.</p>
      </div>

      <div class="bg-white rounded-3xl shadow-sm border border-gray-100 p-8 md:p-12">
          <form @submit.prevent="submitPost" class="space-y-8">
              <div class="space-y-4">
                  <label class="block text-xs font-bold text-gray-400 uppercase tracking-widest">Article Title</label>
                  <input v-model="form.title" type="text" required placeholder="A catchy title for your beauty guide..."
                    class="w-full bg-gray-50 border-none focus:ring-1 focus:ring-brand-gold rounded-xl px-6 py-4 text-lg font-serif">
              </div>

              <div class="space-y-4">
                  <label class="block text-xs font-bold text-gray-400 uppercase tracking-widest">Cover Image URL</label>
                  <input v-model="form.imageUrl" type="text" placeholder="https://images.unsplash.com/..."
                    class="w-full bg-gray-50 border-none focus:ring-1 focus:ring-brand-gold rounded-xl px-6 py-3 text-sm">
                  <p class="text-[10px] text-gray-400 italic">Pro-tip: Use high-quality photography for more engagement.</p>
              </div>

              <div class="space-y-4">
                  <label class="block text-xs font-bold text-gray-400 uppercase tracking-widest">Your Article</label>
                  <textarea v-model="form.content" rows="12" required placeholder="Once upon a time in my skincare routine..."
                    class="w-full bg-gray-50 border-none focus:ring-1 focus:ring-brand-gold rounded-2xl px-6 py-4 text-sm resize-none"></textarea>
              </div>

              <div class="pt-6 border-t border-gray-50 flex items-center justify-between">
                  <div class="flex items-center gap-2">
                       <input type="checkbox" v-model="form.expertStatus" id="expert" class="text-brand-gold focus:ring-brand-gold rounded">
                       <label for="expert" class="text-xs text-gray-500">I am a beauty professional/expert</label>
                  </div>
                  <button type="submit" :disabled="submitting" 
                    class="bg-brand-dark text-white px-10 py-4 rounded-full text-xs font-bold uppercase tracking-widest hover:bg-black transition-all shadow-xl disabled:opacity-50">
                    {{ submitting ? 'Sending for Review...' : 'Submit for Approval' }}
                  </button>
              </div>
          </form>
      </div>

      <div class="mt-8 text-center">
          <button @click="router.back()" class="text-xs font-bold text-gray-400 hover:text-brand-dark uppercase tracking-widest">Cancel and Go Back</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue';
import { useRouter } from 'vue-router';
import api from '@/services/api';
import { useAuthStore } from '@/stores/auth';

const router = useRouter();
const authStore = useAuthStore();
const submitting = ref(false);

const form = reactive({
    title: '',
    content: '',
    imageUrl: '',
    expertStatus: false
});

const submitPost = async () => {
    if (!authStore.isAuthenticated) {
        alert("Please login to submit a post.");
        return;
    }
    
    submitting.value = true;
    try {
        const payload = {
            ...form,
            authorName: authStore.user.username,
            authorId: authStore.user.id,
            isApproved: false
        };
        await api.post('/blog/submit', payload);
        alert("Thank you! Your post has been submitted for review.");
        router.push('/');
    } catch (e) {
        alert("Failed to submit post.");
    } finally {
        submitting.value = false;
    }
};
</script>
