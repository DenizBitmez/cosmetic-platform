<template>
  <div class="min-h-screen bg-[#FDFBF7] pt-8 pb-16">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
      <div class="flex items-center justify-between mb-12">
        <div>
            <h1 class="text-4xl font-serif text-brand-dark mb-2">Expert Insights</h1>
            <p class="text-gray-500 font-light italic">Deep dives into beauty, science, and self-care.</p>
        </div>
        <div>
            <router-link to="/blog/submit" class="bg-brand-gold text-white px-8 py-3 rounded-full text-[10px] font-bold uppercase tracking-widest hover:bg-brand-dark transition-all shadow-xl">Write an Article</router-link>
        </div>
      </div>

      <div v-if="loading" class="flex justify-center py-20">
          <div class="animate-spin rounded-full h-8 w-8 border-b-2 border-brand-gold"></div>
      </div>

      <div v-else class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-10">
          <div v-for="post in posts" :key="post.id" class="group cursor-pointer bg-white rounded-3xl overflow-hidden shadow-sm hover:shadow-xl transition-all duration-500 border border-gray-100" @click="router.push(`/blog/${post.id}`)">
              <div class="aspect-[16/10] overflow-hidden">
                  <img :src="post.imageUrl || 'https://images.unsplash.com/photo-1596462502278-27bfdc4033c8?q=80&w=1760&auto=format&fit=crop'" 
                       class="w-full h-full object-cover group-hover:scale-105 transition-transform duration-700" alt="Blog Image">
              </div>
              <div class="p-8">
                  <div class="flex items-center gap-3 mb-4">
                      <span class="text-[10px] bg-brand-cream text-brand-gold px-2 py-0.5 rounded-full font-bold uppercase tracking-wider">
                          {{ post.expertStatus ? 'Expert Advice' : 'Community Voice' }}
                      </span>
                      <span class="text-[10px] text-gray-400 capitalize">{{ post.authorName }}</span>
                  </div>
                  <h3 class="text-2xl font-serif text-brand-dark mb-4 group-hover:text-brand-gold transition-colors">{{ post.title }}</h3>
                  <p class="text-sm text-gray-500 line-clamp-3 font-light leading-relaxed mb-6">{{ post.content }}</p>
                  <div class="flex items-center justify-between pt-6 border-t border-gray-50">
                      <span class="text-[10px] text-gray-400 uppercase tracking-widest font-bold">{{ formatDate(post.createdAt) }}</span>
                      <span class="text-xs text-brand-gold font-bold uppercase tracking-widest group-hover:translate-x-2 transition-transform">Read More →</span>
                  </div>
              </div>
          </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import api from '@/services/api';
import { useRouter } from 'vue-router';
import { useAuthStore } from '@/stores/auth';

const router = useRouter();
const authStore = useAuthStore();
const posts = ref([]);
const loading = ref(true);

const fetchPosts = async () => {
    try {
        const res = await api.get('/blog');
        posts.value = res.data;
    } catch (e) {
        console.error("Failed to fetch blog posts", e);
    } finally {
        loading.value = false;
    }
};

const formatDate = (dateStr) => {
    const date = new Date(dateStr);
    return date.toLocaleDateString('en-US', { 
        month: 'short', 
        day: 'numeric',
        year: 'numeric'
    });
};

onMounted(fetchPosts);
</script>
