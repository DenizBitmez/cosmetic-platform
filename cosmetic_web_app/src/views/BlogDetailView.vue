<template>
  <div class="min-h-screen bg-white pb-20">
    <div v-if="loading" class="flex justify-center py-40">
        <div class="animate-spin rounded-full h-8 w-8 border-b-2 border-brand-gold"></div>
    </div>

    <div v-else-if="post" class="animate-fadeIn">
      <!-- Article Hero -->
      <div class="relative h-[60vh] bg-gray-900">
          <img :src="post.imageUrl || 'https://images.unsplash.com/photo-1596462502278-27bfdc4033c8?q=80&w=1760&auto=format&fit=crop'" 
               class="w-full h-full object-cover opacity-60" alt="Hero">
          <div class="absolute inset-0 flex items-center justify-center">
              <div class="max-w-4xl px-4 text-center">
                  <div class="flex justify-center gap-3 mb-6">
                      <span class="text-xs bg-brand-gold text-white px-3 py-1 rounded-full font-bold uppercase tracking-widest">
                          {{ post.expertStatus ? 'Expert Article' : 'Community Story' }}
                      </span>
                  </div>
                  <h1 class="text-4xl md:text-6xl font-serif text-white mb-6 leading-tight">{{ post.title }}</h1>
                  <div class="flex items-center justify-center gap-3 text-white">
                      <div class="h-8 w-8 rounded-full bg-brand-cream text-brand-gold flex items-center justify-center font-bold">
                          {{ post.authorName.charAt(0).toUpperCase() }}
                      </div>
                      <span class="text-sm font-light italic">by {{ post.authorName }}</span>
                      <span class="mx-2 opacity-50">•</span>
                      <span class="text-xs font-bold uppercase tracking-widest">{{ formatDate(post.createdAt) }}</span>
                  </div>
              </div>
          </div>
      </div>

      <!-- Article Content -->
      <div class="max-w-3xl mx-auto px-4 sm:px-6 mt-16">
          <div class="prose prose-brand lg:prose-xl text-gray-700 leading-relaxed font-light">
              {{ post.content }}
          </div>
          
          <div class="mt-16 pt-8 border-t border-gray-100 flex items-center justify-between">
              <button @click="router.back()" class="text-xs font-bold uppercase tracking-widest text-brand-gold hover:text-brand-dark transition-colors flex items-center gap-2">
                  ← Back to List
              </button>
              <div class="flex gap-4">
                  <!-- Social placeholders -->
                  <span class="text-xs text-gray-400 font-bold uppercase tracking-tighter">Share:</span>
                  <div class="flex gap-2 text-gray-400">
                      <span class="hover:text-brand-gold cursor-pointer transition-colors text-sm">Twitter</span>
                      <span class="hover:text-brand-gold cursor-pointer transition-colors text-sm">Pinterest</span>
                  </div>
              </div>
          </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import api from '@/services/api';

const route = useRoute();
const router = useRouter();
const post = ref(null);
const loading = ref(true);

const fetchPost = async () => {
    try {
        const res = await api.get(`/blog/${route.params.id}`);
        post.value = res.data;
    } catch (e) {
        console.error("Failed to fetch blog post", e);
    } finally {
        loading.value = false;
    }
};

const formatDate = (dateStr) => {
    const date = new Date(dateStr);
    return date.toLocaleDateString('en-US', { 
        month: 'long', 
        day: 'numeric',
        year: 'numeric'
    });
};

onMounted(fetchPost);
</script>

<style scoped>
.prose {
    white-space: pre-wrap;
}
.animate-fadeIn {
    animation: fadeIn 0.5s ease-out;
}
@keyframes fadeIn {
    from { opacity: 0; transform: translateY(10px); }
    to { opacity: 1; transform: translateY(0); }
}
</style>
