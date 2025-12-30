<template>
  <div class="min-h-screen bg-[#FDFBF7] pt-8 pb-16">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
      <!-- Header -->
      <div class="mb-12">
        <h1 class="text-4xl font-serif text-brand-dark mb-2">Community & Reviews</h1>
        <p class="text-gray-500 font-light italic">Join the conversation, share your glow.</p>
      </div>

      <div class="grid grid-cols-1 lg:grid-cols-4 gap-8">
        <!-- Main Feed -->
        <div class="lg:col-span-3 space-y-8">
          <!-- Post Box -->
          <div class="bg-white rounded-2xl shadow-sm border border-gray-100 p-6">
            <div class="flex items-start gap-4">
              <div class="h-10 w-10 rounded-full bg-brand-cream border border-brand-gold flex items-center justify-center flex-shrink-0 text-brand-gold font-bold">
                {{ authStore.user?.username ? authStore.user.username.charAt(0).toUpperCase() : '?' }}
              </div>
              <div class="flex-grow">
                <div v-if="!isPosting" @click="isPosting = true" class="bg-gray-50 hover:bg-gray-100 transition-colors rounded-xl px-4 py-3 text-gray-400 cursor-pointer border border-transparent hover:border-gray-200">
                    What's your latest discovery, {{ authStore.user?.username || 'Guest' }}?
                </div>
                
                <div v-else class="space-y-4 animate-fadeIn">
                   <input v-model="newReview.productName" type="text" placeholder="Product Name (e.g. Silk Foundation)" 
                      class="w-full bg-gray-50 border-none focus:ring-1 focus:ring-brand-gold rounded-lg px-4 py-2 text-sm">
                   
                   <div class="flex items-center gap-2">
                       <span class="text-xs font-bold text-gray-500 uppercase tracking-wider">Rating:</span>
                       <div class="flex gap-1">
                           <button v-for="i in 5" :key="i" @click="newReview.rating = i" class="text-xl transition-transform hover:scale-120"
                               :class="i <= newReview.rating ? 'text-yellow-400' : 'text-gray-200'">
                               ★
                           </button>
                       </div>
                   </div>

                   <textarea v-model="newReview.comment" rows="3" placeholder="Tell us everything..." 
                      class="w-full bg-gray-50 border-none focus:ring-1 focus:ring-brand-gold rounded-xl px-4 py-3 text-sm resize-none"></textarea>
                   
                   <div class="flex justify-end gap-2">
                       <button @click="isPosting = false" class="px-4 py-2 text-xs font-bold text-gray-400 hover:text-gray-600 uppercase tracking-widest">Cancel</button>
                       <button @click="submitReview" :disabled="submitting || !newReview.comment" 
                          class="bg-brand-dark text-white px-6 py-2 rounded-lg text-xs font-bold uppercase tracking-widest hover:bg-black transition-all disabled:opacity-50">
                          {{ submitting ? 'Posting...' : 'Post Review' }}
                       </button>
                   </div>
                </div>
              </div>
            </div>
          </div>

          <!-- Feed -->
          <div class="space-y-8">
            <div v-if="loading" class="flex flex-col items-center py-20">
                <div class="animate-spin rounded-full h-8 w-8 border-b-2 border-brand-gold mb-4"></div>
                <p class="text-xs text-gray-400 uppercase tracking-widest">Gathering thoughts...</p>
            </div>

            <div v-else-if="reviews.length === 0" class="text-center py-20 bg-white rounded-2xl border border-dashed border-gray-200">
                <p class="text-gray-400 font-light">No reviews yet. Be the first to share! ✨</p>
            </div>

            <div v-else v-for="review in reviews" :key="review.id" class="bg-white rounded-2xl shadow-sm border border-gray-100 overflow-hidden group">
                <div class="p-6">
                    <!-- User Header -->
                    <div class="flex items-start justify-between mb-4">
                        <div class="flex items-center gap-3">
                            <div class="h-10 w-10 rounded-full bg-brand-cream border border-brand-gold flex items-center justify-center text-brand-gold font-bold">
                                {{ review.username.charAt(0).toUpperCase() }}
                            </div>
                            <div>
                                <h4 class="text-sm font-bold text-gray-900 capitalize">{{ review.username }}</h4>
                                <div class="flex items-center gap-2">
                                    <span class="text-[10px] text-gray-400">{{ formatDate(review.createdAt) }}</span>
                                    <span v-if="review.productName" class="text-[10px] bg-brand-cream text-brand-dark px-2 rounded-full font-medium">#{{ review.productName }}</span>
                                </div>
                            </div>
                        </div>
                        
                        <!-- Actions Menu -->
                        <div v-if="isOwner(review)" class="flex gap-2 opacity-0 group-hover:opacity-100 transition-opacity">
                             <button @click="startEdit(review)" class="p-1.5 text-gray-400 hover:text-blue-500 hover:bg-blue-50 rounded-lg transition-colors">
                                 <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15.232 5.232l3.536 3.536m-2.036-5.036a2.5 2.5 0 113.536 3.536L6.5 21.036H3v-3.572L16.732 3.732z"/></svg>
                             </button>
                             <button @click="deleteReview(review.id)" class="p-1.5 text-gray-400 hover:text-red-500 hover:bg-red-50 rounded-lg transition-colors">
                                 <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16"/></svg>
                             </button>
                        </div>
                    </div>

                    <!-- Rating -->
                    <div class="flex text-yellow-400 text-xs mb-3">
                        <span v-for="n in 5" :key="n">{{ n <= review.rating ? '★' : '☆' }}</span>
                    </div>

                    <!-- Content -->
                    <div v-if="editingId === review.id" class="mb-4 space-y-3">
                        <textarea v-model="editForm.comment" rows="3" class="w-full bg-gray-50 border border-gray-200 rounded-xl px-4 py-3 text-sm focus:ring-1 focus:ring-brand-gold outline-none"></textarea>
                        <div class="flex justify-end gap-2">
                            <button @click="editingId = null" class="text-xs font-bold text-gray-400 px-3 py-1">Cancel</button>
                            <button @click="saveEdit(review.id)" class="bg-brand-dark text-white text-xs font-bold px-4 py-1.5 rounded-lg shadow-sm hover:bg-black">Save Changes</button>
                        </div>
                    </div>
                    <p v-else class="text-gray-700 text-sm leading-relaxed mb-6">{{ review.comment }}</p>

                    <!-- Interactions -->
                    <div class="flex items-center gap-4 pt-4 border-t border-gray-50">
                        <div class="flex items-center gap-2">
                            <div class="flex -space-x-1">
                                <span v-for="(count, emoji) in aggregateReactions(review.reactions)" :key="emoji" 
                                    class="bg-gray-50 border border-white text-xs rounded-full px-2 py-0.5" :title="emoji">
                                    {{ emoji }} {{ count }}
                                </span>
                            </div>
                            <div class="relative group/emoji">
                                <button class="text-gray-400 hover:text-brand-gold transition-colors p-1">
                                    <svg class="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M14.828 14.828a4 4 0 01-5.656 0M9 10h.01M15 10h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z"/></svg>
                                </button>
                                <div class="absolute bottom-full left-0 mb-2 bg-white rounded-full shadow-xl border border-gray-100 p-2 opacity-0 invisible group-hover/emoji:opacity-100 group-hover/emoji:visible transition-all flex gap-1 z-10">
                                    <button v-for="e in ['✨', '💄', '💖', '🔥', '🌸']" :key="e" @click="react(review.id, e)" class="hover:scale-125 transition-transform p-1">{{ e }}</button>
                                </div>
                            </div>
                        </div>

                        <button @click="showReplyInput(review.id)" class="text-xs font-bold text-gray-400 hover:text-brand-dark transition-colors uppercase tracking-widest flex items-center gap-1">
                            <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M8 12h.01M12 12h.01M16 12h.01M21 12c0 4.418-4.03 8-9 8a9.863 9.863 0 01-4.255-.949L3 20l1.395-3.72C3.512 15.042 3 13.574 3 12c0-4.418 4.03-8 9-8s9 3.582 9 8z"/></svg>
                            Reply ({{ review.replies?.length || 0 }})
                        </button>
                    </div>

                    <!-- Replies -->
                    <div v-if="review.replies?.length" class="mt-6 space-y-4 pl-6 border-l-2 border-gray-50">
                        <div v-for="reply in review.replies" :key="reply.id">
                            <div class="flex items-center gap-2 mb-1">
                                <span class="text-xs font-bold text-gray-900">{{ reply.username }}</span>
                                <span class="text-[10px] text-gray-400">{{ formatDate(reply.createdAt) }}</span>
                            </div>
                            <p class="text-sm text-gray-600 leading-relaxed">{{ reply.comment }}</p>
                        </div>
                    </div>

                    <!-- Reply Input -->
                    <div v-if="replyingTo === review.id" class="mt-4 pl-6 border-l-2 border-brand-gold animate-slideDown">
                        <textarea v-model="replyForm.comment" rows="2" placeholder="Write a reply..." 
                            class="w-full bg-gray-50 border-none focus:ring-1 focus:ring-brand-gold rounded-xl px-4 py-3 text-sm resize-none mb-2"></textarea>
                        <div class="flex justify-end gap-2">
                            <button @click="replyingTo = null" class="text-xs font-bold text-gray-400 uppercase tracking-widest">Cancel</button>
                            <button @click="submitReply(review.id)" class="bg-brand-gold text-white px-4 py-1.5 rounded-lg text-xs font-bold uppercase tracking-widest hover:opacity-90">Reply</button>
                        </div>
                    </div>
                </div>
            </div>
          </div>
        </div>

        <!-- Sidebar -->
        <div class="space-y-6">
          <!-- Community Stats -->
          <div class="bg-white rounded-2xl shadow-sm border border-gray-100 p-6">
            <h3 class="text-sm font-bold text-gray-900 uppercase tracking-widest mb-4">Community Stats</h3>
            <div class="grid grid-cols-2 gap-4">
              <div>
                <p class="text-2xl font-serif text-brand-gold">{{ reviews.length }}</p>
                <p class="text-[10px] text-gray-400 uppercase font-bold tracking-tighter">Reviews</p>
              </div>
              <div>
                <p class="text-2xl font-serif text-brand-gold">{{ activeUsersCount }}</p>
                <p class="text-[10px] text-gray-400 uppercase font-bold tracking-tighter">Active Glowers</p>
              </div>
            </div>
          </div>

          <!-- Trending Products -->
          <div class="bg-white rounded-2xl shadow-sm border border-gray-100 p-6 sticky top-24">
            <h3 class="text-sm font-bold text-gray-900 uppercase tracking-widest mb-4 flex items-center gap-2">
              <span class="text-brand-gold">🔥</span> Trending Now
            </h3>
            <div class="space-y-4">
              <div v-for="(item, index) in trendingProducts" :key="item.name" 
                class="flex items-center gap-3 group/item cursor-pointer">
                <div class="h-8 w-8 rounded-lg bg-brand-cream border border-brand-gold flex items-center justify-center text-xs font-bold text-brand-gold group-hover/item:bg-brand-gold group-hover/item:text-white transition-colors">
                  #{{ index + 1 }}
                </div>
                <div class="flex-grow min-w-0">
                  <h4 class="text-sm font-bold text-gray-900 truncate group-hover/item:text-brand-gold transition-colors">{{ item.name }}</h4>
                  <p class="text-[10px] text-gray-400 uppercase tracking-wide">{{ item.count }} reviews</p>
                </div>
              </div>

              <div v-if="trendingProducts.length === 0" class="text-center py-4">
                <p class="text-xs text-gray-400 italic">No trends yet...</p>
              </div>
            </div>

            <!-- Guidelines -->
            <div class="mt-8 pt-8 border-t border-gray-50">
              <h3 class="text-[10px] font-bold text-gray-400 uppercase tracking-widest mb-3">Community Rules</h3>
              <ul class="text-[11px] text-gray-500 space-y-2">
                <li class="flex items-center gap-2">✨ Be respectful & kind</li>
                <li class="flex items-center gap-2">🌿 Share honest feedback</li>
                <li class="flex items-center gap-2">🚫 No spam or ads</li>
              </ul>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue';
import api from '@/services/api';
import { useAuthStore } from '@/stores/auth';

const authStore = useAuthStore();
const reviews = ref([]);
const loading = ref(false);
const submitting = ref(false);
const isPosting = ref(false);

const newReview = reactive({
    productName: '',
    rating: 5,
    comment: ''
});

const editingId = ref(null);
const editForm = reactive({ comment: '', rating: 5 });

const replyingTo = ref(null);
const replyForm = reactive({ comment: '' });

// Computed stats
const trendingProducts = computed(() => {
    const counts = reviews.value.reduce((acc, curr) => {
        if (curr.productName) {
            acc[curr.productName] = (acc[curr.productName] || 0) + 1;
        }
        return acc;
    }, {});
    
    return Object.entries(counts)
        .map(([name, count]) => ({ name, count }))
        .sort((a, b) => b.count - a.count)
        .slice(0, 5);
});

const activeUsersCount = computed(() => {
    return new Set(reviews.value.map(r => r.userId)).size;
});

const fetchReviews = async () => {
    loading.value = true;
    try {
        const res = await api.get('/reviews');
        reviews.value = res.data;
    } catch (e) {
        console.error("Failed to fetch reviews", e);
    } finally {
        loading.value = false;
    }
};

const submitReview = async () => {
    if (!validateAuth()) return;
    submitting.value = true;
    try {
        const payload = {
            ...newReview,
            username: authStore.user.username,
            userId: authStore.user.id
        };
        await api.post('/reviews/add', payload);
        resetNewReview();
        await fetchReviews();
    } catch (e) {
        alert("Failed to post review.");
    } finally {
        submitting.value = false;
        isPosting.value = false;
    }
};

const startEdit = (review) => {
    editingId.value = review.id;
    editForm.comment = review.comment;
    editForm.rating = review.rating;
};

const saveEdit = async (id) => {
    try {
        await api.put(`/reviews/${id}`, editForm);
        editingId.value = null;
        await fetchReviews();
    } catch (e) {
        alert("Update failed.");
    }
};

const deleteReview = async (id) => {
    if (!confirm("Are you sure you want to delete this review?")) return;
    try {
        await api.delete(`/reviews/${id}`);
        await fetchReviews();
    } catch (e) {
        alert("Delete failed.");
    }
};

const showReplyInput = (id) => {
    if (!validateAuth()) return;
    replyingTo.value = id;
    replyForm.comment = '';
};

const submitReply = async (reviewId) => {
    if (!replyForm.comment) return;
    try {
        const payload = {
            comment: replyForm.comment,
            username: authStore.user.username,
            userId: authStore.user.id
        };
        await api.post(`/reviews/${reviewId}/reply`, payload);
        replyingTo.value = null;
        await fetchReviews();
    } catch (e) {
        alert("Failed to reply.");
    }
};

const react = async (reviewId, emoji) => {
    if (!validateAuth()) return;
    try {
        const payload = {
            emoji,
            username: authStore.user.username,
            userId: authStore.user.id
        };
        await api.post(`/reviews/${reviewId}/react`, payload);
        await fetchReviews();
    } catch (e) {
        // Silent fall
    }
};

const aggregateReactions = (reactions) => {
    if (!reactions) return {};
    return reactions.reduce((acc, curr) => {
        acc[curr.emoji] = (acc[curr.emoji] || 0) + 1;
        return acc;
    }, {});
};

const isOwner = (review) => {
    return authStore.user && authStore.user.id === review.userId;
};

const validateAuth = () => {
    if (!authStore.isAuthenticated) {
        alert("Please login for this action.");
        return false;
    }
    return true;
};

const resetNewReview = () => {
    newReview.productName = '';
    newReview.comment = '';
    newReview.rating = 5;
};

const formatDate = (dateStr) => {
    const date = new Date(dateStr);
    return date.toLocaleDateString('en-US', { 
        month: 'short', 
        day: 'numeric',
        year: 'numeric'
    });
};

onMounted(() => {
    fetchReviews();
});
</script>

<style scoped>
.animate-fadeIn {
  animation: fadeIn 0.3s ease-out;
}
.animate-slideDown {
  animation: slideDown 0.3s ease-out;
}
@keyframes fadeIn {
  from { opacity: 0; }
  to { opacity: 1; }
}
@keyframes slideDown {
  from { transform: translateY(-10px); opacity: 0; }
  to { transform: translateY(0); opacity: 1; }
}
</style>
