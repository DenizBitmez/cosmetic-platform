<template>
  <div class="min-h-screen bg-gray-50 pt-8 pb-16">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
      <div class="text-center mb-12">
        <h1 class="text-3xl font-serif text-brand-dark mb-4">Community & Reviews</h1>
        <p class="text-gray-600 max-w-2xl mx-auto">Join the conversation. Share your experiences, rate products, and help others find their perfect match.</p>
        <button @click="showReviewModal = true" class="mt-6 bg-gray-900 text-white px-6 py-3 rounded-full text-sm font-bold uppercase tracking-widest hover:bg-yellow-600 transition-colors shadow-lg">
            Write a Review
        </button>
      </div>

      <div class="grid grid-cols-1 lg:grid-cols-3 gap-8">
          <!-- Latest Discussions / Reviews -->
          <div class="lg:col-span-2 space-y-6">
             <h2 class="text-xl font-bold text-gray-900 border-b pb-2 flex justify-between items-center">
                 Latest Reviews
                 <span class="text-xs font-normal text-gray-500">{{ reviews.length }} reviews</span>
             </h2>
             
             <div v-if="loading" class="text-center py-10">
                 <div class="animate-spin rounded-full h-8 w-8 border-b-2 border-yellow-600 mx-auto"></div>
             </div>

             <div v-else-if="reviews.length === 0" class="text-center py-10 bg-white rounded-lg border border-dashed">
                 <p class="text-gray-500">No reviews yet. Be the first to share your thoughts!</p>
             </div>

             <div v-else v-for="review in reviews" :key="review.id" class="bg-white p-6 rounded-lg shadow-sm border border-gray-100 transition-all hover:shadow-md">
                <div class="flex items-center mb-4">
                    <div class="h-10 w-10 rounded-full bg-yellow-50 text-yellow-800 flex items-center justify-center font-bold mr-3 border border-yellow-200">
                        {{ review.username ? review.username.charAt(0).toUpperCase() : 'U' }}
                    </div>
                    <div>
                        <h4 class="text-sm font-bold text-gray-900">{{ review.username || 'Anonymous' }}</h4>
                        <div class="flex text-yellow-400 text-xs">
                            <span v-for="n in 5" :key="n">{{ n <= review.rating ? '★' : '☆' }}</span>
                        </div>
                    </div>
                    <span class="ml-auto text-xs text-gray-400">{{ new Date(review.createdAt).toLocaleDateString() }}</span>
                </div>
                <h3 class="font-bold text-gray-800 mb-2">{{ review.productName }}</h3>
                <p class="text-sm text-gray-600 leading-relaxed">{{ review.comment }}</p>
             </div>
          </div>

          <!-- Sidebar / Trending -->
          <div class="space-y-6">
              <div class="bg-white p-6 rounded-lg shadow-sm border border-gray-100 sticky top-24">
                  <h3 class="text-lg font-bold text-gray-900 mb-4">Community Guidelines</h3>
                  <ul class="text-sm text-gray-600 space-y-3 list-disc pl-4">
                      <li>Be respectful and kind.</li>
                      <li>Share honest feedback.</li>
                      <li>Focus on the product experience.</li>
                      <li>Avoid spam or promotional content.</li>
                  </ul>
              </div>
          </div>
      </div>

      <!-- Add Review Modal -->
      <div v-if="showReviewModal" class="fixed inset-0 z-[60] overflow-y-auto" aria-labelledby="modal-title" role="dialog" aria-modal="true">
        <div class="flex items-center justify-center min-h-screen px-4 pt-4 pb-20 text-center sm:p-0">
          
          <!-- Overlay -->
          <div class="fixed inset-0 bg-gray-500 bg-opacity-75 transition-opacity" aria-hidden="true" @click="showReviewModal = false"></div>

          <!-- Modal Panel -->
          <div class="relative inline-block align-bottom bg-white rounded-lg px-4 pt-5 pb-4 text-left overflow-hidden shadow-xl transform transition-all sm:my-8 sm:align-middle sm:max-w-lg sm:w-full sm:p-6">
            <div class="absolute top-0 right-0 pt-4 pr-4">
                <button @click="showReviewModal = false" class="bg-white rounded-md text-gray-400 hover:text-gray-500 focus:outline-none">
                <span class="sr-only">Close</span>
                <svg class="h-6 w-6" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke="currentColor" aria-hidden="true">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
                </svg>
                </button>
            </div>
            <div>
              <div class="mt-3 text-center sm:mt-5">
                <h3 class="text-lg leading-6 font-medium text-gray-900" id="modal-title">Write a Review</h3>
                <div class="mt-2">
                  <form @submit.prevent="submitReview" class="space-y-4 text-left">
                      <div>
                          <label class="block text-sm font-medium text-gray-700">Product Name</label>
                          <input v-model="newReview.productName" type="text" required placeholder="e.g. L'Oreal Lipstick" class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-yellow-500 focus:border-yellow-500 sm:text-sm">
                      </div>
                      <div>
                          <label class="block text-sm font-medium text-gray-700">Rating (1-5)</label>
                          <select v-model="newReview.rating" required class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-yellow-500 focus:border-yellow-500 sm:text-sm">
                              <option value="5">5 - Excellent</option>
                              <option value="4">4 - Very Good</option>
                              <option value="3">3 - Average</option>
                              <option value="2">2 - Poor</option>
                              <option value="1">1 - Terrible</option>
                          </select>
                      </div>
                      <div>
                          <label class="block text-sm font-medium text-gray-700">Your Review</label>
                          <textarea v-model="newReview.comment" rows="4" required placeholder="Share your thoughts..." class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-yellow-500 focus:border-yellow-500 sm:text-sm"></textarea>
                      </div>
                      <div class="mt-5 sm:mt-6 sm:grid sm:grid-cols-2 sm:gap-3 sm:grid-flow-row-dense">
                        <button type="submit" class="w-full inline-flex justify-center rounded-md border border-transparent shadow-sm px-4 py-2 bg-gray-900 text-base font-medium text-white hover:bg-black focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-yellow-500 sm:col-start-2 sm:text-sm">
                          Submit Review
                        </button>
                        <button type="button" @click="showReviewModal = false" class="mt-3 w-full inline-flex justify-center rounded-md border border-gray-300 shadow-sm px-4 py-2 bg-white text-base font-medium text-gray-700 hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-yellow-500 sm:mt-0 sm:col-start-1 sm:text-sm">
                          Cancel
                        </button>
                      </div>
                  </form>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue';
import api from '@/services/api'; // Using our configured axios instance
import { useAuthStore } from '@/stores/auth';

const authStore = useAuthStore();
const reviews = ref([]);
const loading = ref(false);
const showReviewModal = ref(false);

const newReview = reactive({
    productName: '',
    rating: 5,
    comment: ''
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
    if (!authStore.isAuthenticated) {
        alert("Please login to post a review.");
        return;
    }
    
    try {
        const payload = {
            ...newReview,
            username: authStore.user?.username || 'Anonymous',
            // userId could be added if relations are needed strictly
        };
        await api.post('/reviews/add', payload);
        showReviewModal.value = false;
        newReview.productName = '';
        newReview.comment = '';
        newReview.rating = 5;
        await fetchReviews();
    } catch (e) {
        console.error("Failed to submit review", e);
        alert("Failed to submit review.");
    }
};

onMounted(() => {
    fetchReviews();
});
</script>
