<template>
  <div class="min-h-screen bg-[#FDFBF7] pt-12 pb-20">
    <div class="max-w-6xl mx-auto px-4 sm:px-6">
      <div class="flex items-center justify-between mb-12">
          <div>
              <h1 class="text-4xl font-serif text-brand-dark mb-2">Admin: Blog Review</h1>
              <p class="text-gray-500 font-light italic">Moderating the community wisdom.</p>
          </div>
          <router-link to="/" class="text-xs font-bold text-gray-400 hover:text-brand-dark uppercase tracking-widest">Back to Site</router-link>
      </div>

      <div v-if="loading" class="flex justify-center py-20">
          <div class="animate-spin rounded-full h-8 w-8 border-b-2 border-brand-gold"></div>
      </div>

      <div v-else-if="pendingPosts.length === 0" class="text-center py-24 bg-white rounded-3xl border border-dashed border-gray-200">
          <p class="text-gray-400 font-light">All clear! No pending posts to review. ✨</p>
      </div>

      <div v-else class="space-y-6">
          <div v-for="post in pendingPosts" :key="post.id" class="bg-white rounded-3xl shadow-sm border border-gray-100 overflow-hidden p-8 flex flex-col md:flex-row gap-8">
              <div class="md:w-1/3 aspect-[16/10] bg-gray-100 rounded-2xl overflow-hidden">
                  <img :src="post.imageUrl || 'https://images.unsplash.com/photo-1596462502278-27bfdc4033c8?q=80&w=1760&auto=format&fit=crop'" 
                       class="w-full h-full object-cover" alt="Blog Image">
              </div>
              <div class="md:w-2/3 space-y-4">
                  <div class="flex flex-wrap items-center gap-3">
                      <span class="text-[10px] bg-brand-cream text-brand-gold px-2 py-0.5 rounded-full font-bold uppercase tracking-wider">
                          {{ post.expertStatus ? 'Expert Applicant' : 'Community Submission' }}
                      </span>
                      <span class="text-[10px] text-gray-400 font-bold uppercase tracking-widest">by {{ post.authorName }}</span>
                      <span class="text-[10px] text-gray-400 uppercase tracking-widest ml-auto">{{ formatDate(post.createdAt) }}</span>
                  </div>
                  <h3 class="text-2xl font-serif text-brand-dark">{{ post.title }}</h3>
                  <div class="text-sm text-gray-600 line-clamp-4 font-light leading-relaxed h-[100px] bg-gray-50 p-4 rounded-xl">
                      {{ post.content }}
                  </div>
                  
                  <div class="flex gap-4 pt-4">
                      <button @click="approve(post.id)" class="bg-green-600 text-white px-8 py-2.5 rounded-full text-[10px] font-bold uppercase tracking-widest hover:bg-green-700 transition-all flex items-center gap-2">
                          Approve and Publish
                      </button>
                      <button @click="reject(post.id)" class="border border-red-200 text-red-500 px-8 py-2.5 rounded-full text-[10px] font-bold uppercase tracking-widest hover:bg-red-50 transition-all">
                          Reject
                      </button>
                      <router-link :to="`/blog/${post.id}`" target="_blank" class="ml-auto text-[10px] font-bold text-gray-400 hover:text-brand-dark uppercase tracking-widest flex items-center">
                          Preview →
                      </router-link>
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

const pendingPosts = ref([]);
const loading = ref(true);

const fetchPending = async () => {
    try {
        const res = await api.get('/blog/pending');
        pendingPosts.value = res.data;
    } catch (e) {
        console.error("Failed to fetch pending", e);
    } finally {
        loading.value = false;
    }
};

const approve = async (id) => {
    try {
        await api.put(`/blog/${id}/approve`);
        await fetchPending();
    } catch (e) {
        alert("Approval failed.");
    }
};

const reject = async (id) => {
    if (!confirm("Are you sure you want to reject/delete this post?")) return;
    try {
        await api.delete(`/blog/${id}`);
        await fetchPending();
    } catch (e) {
        alert("Rejection failed.");
    }
};

const formatDate = (dateStr) => {
    const date = new Date(dateStr);
    return date.toLocaleString();
};

onMounted(fetchPending);
</script>
