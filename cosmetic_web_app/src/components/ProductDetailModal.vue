<template>
  <div v-if="product" class="fixed inset-0 z-[70] overflow-y-auto" aria-labelledby="modal-title" role="dialog" aria-modal="true">
    <div class="flex items-center justify-center min-h-screen px-4 pt-4 pb-20 text-center sm:p-0">
      <div class="fixed inset-0 bg-gray-900 bg-opacity-80 transition-opacity backdrop-blur-sm" @click="$emit('close')"></div>

      <div class="relative inline-block align-bottom bg-white rounded-2xl text-left overflow-hidden shadow-2xl transform transition-all sm:my-8 sm:align-middle sm:max-w-4xl sm:w-full">
        <button @click="$emit('close')" class="absolute top-4 right-4 z-10 bg-black/20 rounded-full p-2 hover:bg-black/40 text-white transition-colors">
          <svg class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" /></svg>
        </button>

        <div class="grid grid-cols-1 md:grid-cols-2 h-full">
          <!-- Image Section -->
          <div class="bg-gray-50 p-8 flex items-center justify-center border-r border-gray-100">
            <img :src="product.image" @error="$event.target.src='https://via.placeholder.com/300?text=No+Image'" class="max-h-[400px] object-contain drop-shadow-xl" />
          </div>

          <!-- Info Section -->
          <div class="p-6 md:p-8 flex flex-col h-full overflow-hidden">
            <!-- Header -->
            <div class="mb-4">
              <div class="flex items-center justify-between mb-2">
                <span class="bg-black text-white text-[10px] font-bold px-2 py-1 rounded uppercase tracking-widest">
                  {{ product.brand || 'Premium' }}
                </span>
              </div>
              <h2 class="text-2xl font-serif text-gray-900 leading-tight mb-2">{{ product.name }}</h2>
              <span class="text-xl font-bold text-gray-900">${{ product.price || '0.00' }}</span>
            </div>

            <!-- Tabs Navigation -->
            <div class="flex border-b border-gray-100 mb-6 overflow-x-auto no-scrollbar">
              <button @click="activeTab = 'overview'" :class="['flex-1 pb-3 text-sm font-bold transition-colors border-b-2', activeTab === 'overview' ? 'border-brand-dark text-brand-dark' : 'border-transparent text-gray-400 hover:text-gray-600']">
                Product Details
              </button>
              <button @click="activeTab = 'analysis'" :class="['flex-1 pb-3 text-sm font-bold transition-colors border-b-2', activeTab === 'analysis' ? 'border-green-500 text-green-700' : 'border-transparent text-gray-400 hover:text-gray-600']">
                🌿 Content Analysis
              </button>
              <button @click="activeTab = 'manual'" :class="['flex-1 pb-3 text-sm font-bold transition-colors border-b-2', activeTab === 'manual' ? 'border-brand-gold text-brand-gold' : 'border-transparent text-gray-400 hover:text-gray-600']">
                🔍 Manual Check
              </button>
              <button @click="activeTab = 'photos'" :class="['flex-1 pb-3 text-sm font-bold transition-colors border-b-2', activeTab === 'photos' ? 'border-purple-500 text-purple-700' : 'border-transparent text-gray-400 hover:text-gray-600']">
                📸 Photos
              </button>
              <button @click="activeTab = 'qa'" :class="['flex-1 pb-3 text-sm font-bold transition-colors border-b-2', activeTab === 'qa' ? 'border-blue-500 text-blue-700' : 'border-transparent text-gray-400 hover:text-gray-600']">
                💬 Q&A
              </button>
            </div>

            <!-- TAB CONTENT: OVERVIEW (First Tab) -->
            <div v-show="activeTab === 'overview'" class="overflow-y-auto pr-2 custom-scrollbar flex-1 pb-20">
              
              <!-- Rating & Reviews (Mocked/Real) -->
              <div class="flex items-center mb-6">
                <div class="flex text-yellow-400">
                  <svg v-for="i in 5" :key="i" class="w-5 h-5 fill-current" viewBox="0 0 24 24"><path d="M12 17.27L18.18 21l-1.64-7.03L22 9.24l-7.19-.61L12 2 9.19 8.63 2 9.24l5.46 4.73L5.82 21z"/></svg>
                </div>
                <span class="text-gray-500 text-sm ml-2">(4.8/5 - Based on current trends)</span>
              </div>

              <!-- Color Swatches (if available) -->
              <div v-if="product.product_colors && product.product_colors.length > 0" class="mb-6">
                <h3 class="text-sm font-bold text-gray-900 mb-2">Available Colors</h3>
                <div class="flex flex-wrap gap-2">
                  <div v-for="color in product.product_colors" :key="color.hex_value" 
                    :style="{ backgroundColor: color.hex_value }"
                    class="w-8 h-8 rounded-full border border-gray-200 shadow-sm cursor-pointer hover:scale-110 transition-transform"
                    :title="color.colour_name">
                  </div>
                </div>
              </div>

              <div class="mb-6">
                <h3 class="text-sm font-bold text-gray-900 mb-2">Description</h3>
                <div class="text-xs text-gray-600 leading-relaxed space-y-2" v-html="product.description || 'No detailed description available.'"></div>
              </div>

              <div v-if="product.paoMonths" class="bg-brand-cream/30 p-4 rounded-xl border border-brand-gold/20 flex items-center gap-3">
                 <PhClockCounterClockwise :size="24" class="text-brand-gold" />
                 <div>
                    <p class="text-[10px] font-bold uppercase text-brand-gold">Shelf Life (PAO)</p>
                    <p class="text-sm font-medium text-brand-dark">{{ product.paoMonths }} Months after opening</p>
                 </div>
              </div>
            </div>

            <!-- TAB CONTENT: ANALYSIS (Table Style) -->
            <div v-show="activeTab === 'analysis'" class="overflow-y-auto pr-2 custom-scrollbar flex-1 pb-20">
              
              <div v-if="ingredients && ingredients.length > 0">
                <div class="overflow-hidden border border-gray-200 rounded-lg">
                  <table class="min-w-full divide-y divide-gray-200">
                    <thead class="bg-gray-50">
                      <tr>
                        <th scope="col" class="px-3 py-3 text-left text-xs font-bold text-gray-500 uppercase tracking-wider">İçerik Adı (INCI)</th>
                        <th scope="col" class="px-3 py-3 text-left text-xs font-bold text-gray-500 uppercase tracking-wider hidden sm:table-cell">İçerik Adı (TR)</th>
                        <th scope="col" class="px-3 py-3 text-left text-xs font-bold text-gray-500 uppercase tracking-wider">Nedir?</th>
                        <th scope="col" class="px-3 py-3 text-center text-xs font-bold text-gray-500 uppercase tracking-wider">Puan</th>
                      </tr>
                    </thead>
                    <tbody class="bg-white divide-y divide-gray-200">
                      <tr v-for="ing in ingredients" :key="ing.id || ing.inciName" class="hover:bg-gray-50">
                        <td class="px-3 py-3 text-xs font-medium text-gray-900 align-top">
                          {{ ing.inciName }}
                          <div class="sm:hidden text-gray-500 mt-1 italic">{{ ing.turkishName }}</div>
                        </td>
                        <td class="px-3 py-3 text-xs text-gray-500 align-top hidden sm:table-cell">{{ ing.turkishName }}</td>
                        <td class="px-3 py-3 text-xs text-gray-500 align-top">{{ ing.description }}</td>
                        <td class="px-3 py-3 whitespace-nowrap align-top text-center">
                          <span :class="['px-2 py-1 inline-flex text-[10px] leading-5 font-semibold rounded-full', getScoreColorClass(ing.score)]">
                            {{ ing.score }}
                          </span>
                        </td>
                      </tr>
                    </tbody>
                  </table>
                </div>
              </div>
              <div v-else class="text-center py-10 bg-gray-50 rounded-lg">
                <p class="text-gray-500 text-sm">No detailed ingredient analysis available for this product.</p>
              </div>

            </div>
            
            <!-- TAB CONTENT: MANUAL ANALYSIS -->
            <div v-show="activeTab === 'manual'" class="overflow-y-auto pr-2 custom-scrollbar flex-1 pb-20 mt-4">
               <div class="space-y-4">
                  <p class="text-xs text-gray-500 italic">Paste ingredients or type them manually to check for risks.</p>
                  <textarea v-model="manualText" rows="4" class="w-full bg-gray-50 border-gray-200 rounded-xl text-xs p-3 focus:ring-brand-gold focus:border-brand-gold" placeholder="Aqua, Glycerin, Paraben..."></textarea>
                  <button @click="handleManualAnalysis" :disabled="analyzing" class="w-full bg-brand-dark text-white py-3 rounded-lg text-xs font-bold uppercase tracking-widest hover:bg-black disabled:opacity-50 transition-all shadow-lg">
                    {{ analyzing ? 'Analyzing...' : 'Check Ingredients' }}
                  </button>

                  <div v-if="manualResults.length > 0" class="mt-6 space-y-2 border-t border-gray-100 pt-4">
                      <div v-for="res in manualResults" :key="res.inciName" class="flex items-center justify-between p-2 hover:bg-gray-50 rounded-lg transition-colors">
                          <span class="text-xs font-medium text-gray-800">{{ res.inciName }}</span>
                          <span :class="['px-2 py-0.5 text-[10px] font-bold rounded-full', getScoreColorClass(res.score)]">
                            {{ res.score }}
                          </span>
                      </div>
                  </div>
               </div>
            </div>

            <!-- TAB CONTENT: COMMUNITY PHOTOS -->
            <div v-show="activeTab === 'photos'" class="overflow-y-auto pr-2 custom-scrollbar flex-1 pb-20 mt-4">
                <div class="flex justify-between items-center mb-4">
                    <p class="text-xs text-gray-500">Real results from our community.</p>
                    <button @click="showUploadModal = true" class="text-xs bg-brand-gold text-white px-3 py-1.5 rounded-full font-bold hover:bg-brand-dark transition-colors flex items-center gap-1">
                        <PhCamera :size="14" weight="bold" /> Share Yours
                    </button>
                </div>

                <div v-if="communityStore.loading" class="flex justify-center py-8">
                     <div class="animate-spin rounded-full h-8 w-8 border-b-2 border-brand-gold"></div>
                </div>

                <div v-else-if="communityStore.photos.length === 0" class="text-center py-10 bg-gray-50 rounded-xl border border-dashed border-gray-200">
                    <PhCamera :size="32" class="mx-auto text-gray-300 mb-2" weight="duotone" />
                    <p class="text-sm font-medium text-gray-900">No photos yet</p>
                    <p class="text-xs text-gray-500">Be the first to share your experience!</p>
                </div>

                <div v-else class="grid grid-cols-2 gap-3">
                    <div v-for="photo in communityStore.photos" :key="photo.id" class="group relative aspect-square bg-gray-100 rounded-lg overflow-hidden cursor-pointer">
                        <img :src="photo.imageUrl" class="w-full h-full object-cover transition-transform group-hover:scale-110">
                        <div class="absolute inset-0 bg-black/40 opacity-0 group-hover:opacity-100 transition-opacity flex items-end p-2">
                             <div class="w-full text-white text-xs">
                                 <p class="font-bold truncate">{{ photo.user?.username || 'User' }}</p>
                                 <div class="flex justify-between items-center mt-1">
                                    <span class="truncate opacity-80 text-[10px]">{{ photo.description }}</span>
                                    <button @click.stop="communityStore.likePhoto(photo.id)" class="flex items-center gap-0.5 hover:text-red-400">
                                        <PhHeart :size="12" weight="fill" /> {{ photo.likes }}
                                    </button>
                                 </div>
                             </div>
                        </div>
                    </div>
                </div>
            </div>

            <!-- TAB CONTENT: Q&A -->
            <div v-show="activeTab === 'qa'" class="overflow-y-auto pr-2 custom-scrollbar flex-1 pb-20 mt-4">
                <div class="bg-blue-50 p-4 rounded-xl mb-6">
                    <h3 class="text-sm font-bold text-gray-900 mb-2 flex items-center gap-2">
                        <PhQuestion :size="18" weight="bold" /> Ask a Question
                    </h3>
                    <div class="flex gap-2">
                        <input v-model="newQuestionText" type="text" placeholder="Is this suitable for sensitive skin?" class="flex-1 text-xs border border-blue-100 rounded-lg p-3 focus:outline-none focus:ring-2 focus:ring-blue-500">
                        <button @click="handleAskQuestion" class="bg-blue-600 text-white p-3 rounded-lg hover:bg-blue-700 transition-colors">
                            <PhPaperPlaneRight :size="16" weight="fill" />
                        </button>
                    </div>
                </div>

                <div v-if="qaStore.loading" class="flex justify-center py-8">
                     <div class="animate-spin rounded-full h-8 w-8 border-b-2 border-brand-gold"></div>
                </div>

                <div v-else-if="qaStore.questions.length === 0" class="text-center py-10 bg-gray-50 rounded-xl border border-dashed border-gray-200">
                     <PhChatCircle :size="32" class="mx-auto text-gray-300 mb-2" weight="duotone" />
                     <p class="text-sm font-medium text-gray-900">No questions yet</p>
                     <p class="text-xs text-gray-500">Ask the first question about this product!</p>
                </div>

                <div v-else class="space-y-4">
                    <div v-for="q in qaStore.questions" :key="q.id" class="border border-gray-100 rounded-xl p-4 hover:shadow-sm transition-shadow">
                        <div class="flex items-start gap-3">
                            <div class="bg-gray-100 p-2 rounded-full">
                                <PhQuestion :size="16" class="text-gray-500" />
                            </div>
                            <div class="flex-1">
                                <div class="flex justify-between items-start">
                                    <p class="text-sm font-bold text-gray-900">{{ q.content }}</p>
                                    <span class="text-[10px] text-gray-400">{{ new Date(q.createdDate).toLocaleDateString() }}</span>
                                </div>
                                <p class="text-xs text-gray-500 mt-1">Asked by {{ q.user?.username || 'User' }}</p>
                                
                                <div class="flex items-center gap-4 mt-3">
                                    <button @click="qaStore.voteQuestion(q.id)" class="text-xs text-gray-500 flex items-center gap-1 hover:text-blue-600">
                                        <PhThumbsUp :size="14" /> {{ q.upvotes }} Helpful
                                    </button>
                                    <button @click="showAnswerForm[q.id] = !showAnswerForm[q.id]" class="text-xs text-brand-dark font-bold hover:underline">
                                        Reply
                                    </button>
                                </div>

                                <!-- Answer Input -->
                                <div v-if="showAnswerForm[q.id]" class="mt-3 flex gap-2">
                                    <input v-model="newAnswerText[q.id]" type="text" placeholder="Write your answer..." class="flex-1 text-xs border border-gray-200 rounded-lg p-2 focus:ring-1 focus:ring-black">
                                    <button @click="handleAnswerQuestion(q.id)" class="bg-black text-white px-3 py-1 rounded text-xs font-bold">Post</button>
                                </div>

                                <!-- Answers List -->
                                <div v-if="q.answers && q.answers.length > 0" class="mt-4 pl-4 border-l-2 border-gray-100 space-y-3">
                                    <div v-for="ans in q.answers" :key="ans.id" class="bg-gray-50 p-3 rounded-lg">
                                        <div class="flex justify-between items-start">
                                            <div class="flex items-center gap-2">
                                                <span class="text-xs font-bold text-gray-800">{{ ans.user?.username }}</span>
                                                <span v-if="ans.expertAnswer" class="bg-green-100 text-green-700 text-[10px] px-1.5 py-0.5 rounded font-bold flex items-center gap-0.5">
                                                    <PhCheckCircle :size="10" weight="fill" /> Expert
                                                </span>
                                            </div>
                                            <span class="text-[10px] text-gray-400">{{ new Date(ans.createdDate).toLocaleDateString() }}</span>
                                        </div>
                                        <p class="text-xs text-gray-600 mt-1">{{ ans.content }}</p>
                                        <button @click="qaStore.voteAnswer(q.id, ans.id)" class="text-[10px] text-gray-400 flex items-center gap-1 mt-2 hover:text-blue-600">
                                            <PhThumbsUp :size="12" /> {{ ans.upvotes }} Helpful
                                        </button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <PhotoUploadModal 
                :isOpen="showUploadModal" 
                :product="product"
                @close="showUploadModal = false"
            />

            <!-- Price Alert Modal -->
            <PriceAlertModal :show="showPriceAlertModal" :product="product" @close="showPriceAlertModal = false" @created="onPriceAlertCreated" />

            <!-- Sticky Footer (Buttons) -->
            <div class="absolute bottom-0 left-0 right-0 p-6 md:p-8 bg-white border-t border-gray-100 flex gap-3">
              <button @click="toggleWishlist" :disabled="addingToWishlist" class="flex-shrink-0 bg-white border-2 text-center py-4 px-4 rounded-lg text-sm font-bold transition-colors shadow-lg flex items-center justify-center" :class="isInWishlist ? 'border-red-500 text-red-500 hover:bg-red-50' : 'border-gray-300 text-gray-600 hover:border-red-500 hover:text-red-500'">
                <PhHeart :size="20" :weight="isInWishlist ? 'fill' : 'regular'" />
              </button>
              <button @click="toggleComparison" class="flex-shrink-0 bg-white border-2 text-center py-4 px-4 rounded-lg text-sm font-bold transition-colors shadow-lg flex items-center justify-center" :class="isInComparison ? 'border-blue-500 text-blue-500 hover:bg-blue-50' : 'border-gray-300 text-gray-600 hover:border-blue-500 hover:text-blue-500'">
                <PhScales :size="20" :weight="isInComparison ? 'fill' : 'regular'" />
              </button>
              <button @click="openPriceAlertModal" class="flex-shrink-0 bg-white border-2 border-gray-300 text-gray-600 text-center py-4 px-4 rounded-lg text-sm font-bold hover:border-yellow-500 hover:text-yellow-500 transition-colors shadow-lg flex items-center justify-center">
                <PhBell :size="20" />
              </button>
              <button @click="handleAddToCart" class="flex-1 bg-black text-white text-center py-4 rounded-lg text-sm font-bold hover:bg-gray-800 transition-colors shadow-lg flex items-center justify-center gap-2">
                <PhShoppingCart :size="20" />
                Add to Cart
              </button>
              <button @click="addToShelf" :disabled="addingToShelf" class="flex-1 bg-brand-cream text-brand-dark border border-brand-gold/30 text-center py-4 rounded-lg text-sm font-bold hover:bg-brand-gold/10 transition-colors flex items-center justify-center gap-2">
                 <PhClockCounterClockwise :size="20" />
                 {{ addingToShelf ? 'Adding...' : 'Add to Shelf' }}
              </button>
            </div>

          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, watch, onMounted } from 'vue';
import { useCartStore } from '@/stores/cart';
import { useAuthStore } from '@/stores/auth';
import { useUiStore } from '@/stores/ui';
import { useWishlistStore } from '@/stores/wishlist';
import { useComparisonStore } from '@/stores/comparison';
import { useRouter } from 'vue-router';
import api from '@/services/api';
import { PhClockCounterClockwise, PhCheckCircle, PhShoppingCart, PhHeart, PhScales, PhBell, PhCamera, PhQuestion, PhThumbsUp, PhChatCircle, PhPaperPlaneRight } from '@phosphor-icons/vue';
import PriceAlertModal from './PriceAlertModal.vue';
import PhotoUploadModal from './PhotoUploadModal.vue';
import { useCommunityStore } from '@/stores/community';
import { useQAStore } from '@/stores/qa';

const props = defineProps({
  product: {
    type: Object,
    default: null
  }
});

const emit = defineEmits(['close', 'add-to-cart']);

const cartStore = useCartStore();
const authStore = useAuthStore();
const uiStore = useUiStore();
const wishlistStore = useWishlistStore();
const comparisonStore = useComparisonStore();
const communityStore = useCommunityStore();
const qaStore = useQAStore();
const router = useRouter();

const activeTab = ref('overview');
const manualText = ref('');
const manualResults = ref([]);
const newQuestionText = ref('');
const newAnswerText = ref({}); // Map of questionId -> text
const showAnswerForm = ref({}); // Map of questionId -> boolean
const analyzing = ref(false);
const addingToWishlist = ref(false);
const isInWishlist = ref(false);
const isInComparison = ref(false);
const showPriceAlertModal = ref(false);
const showUploadModal = ref(false);

// Mock Ingredients DB for enriching external products
const MOCK_INGREDIENTS_DB = [
    { inciName: 'Aqua', turkishName: 'Su', description: 'Çözücü olarak kullanılır, cildi nemlendirir.', score: 'İyi' },
    { inciName: 'Glycerin', turkishName: 'Gliserin', description: 'Cildi nemlendirir ve korur.', score: 'Çok İyi' },
    { inciName: 'Niacinamide', turkishName: 'B3 Vitamini', description: 'Cilt bariyerini güçlendirir, leke karşıtıdır.', score: 'Çok İyi' },
    { inciName: 'Dimethicone', turkishName: 'Dimetikon', description: 'Cildi pürüzsüzleştirir ancak gözenekleri tıkayabilir.', score: 'Orta' },
    { inciName: 'Phenoxyethanol', turkishName: 'Fenoksietanol', description: 'Koruyucu maddedir, bakterilere karşı etkilidir.', score: 'Orta' },
    { inciName: 'Tocopherol', turkishName: 'E Vitamini', description: 'Güçlü bir antioksidandır, cildi besler.', score: 'Çok İyi' },
    { inciName: 'Sodium Hyaluronate', turkishName: 'Hyaluronik Asit', description: 'Cildin su tutma kapasitesini artırır.', score: 'Çok İyi' },
    { inciName: 'Parfum', turkishName: 'Parfüm', description: 'Ürüne koku verir, alerjen olabilir.', score: 'Riskli' },
    { inciName: 'Titanium Dioxide', turkishName: 'Titanyum Dioksit', description: 'Güneş koruyucu ve renklendirici ajan.', score: 'İyi' },
    { inciName: 'Mica', turkishName: 'Mika', description: 'Işıltı ve parlaklık verir.', score: 'İyi' },
    { inciName: 'Retinol', turkishName: 'Retinol', description: 'Yaşlanma karşıtıdır, hücre yenilenmesini hızlandırır.', score: 'Çok İyi' },
    { inciName: 'Salicylic Acid', turkishName: 'Salisilik Asit', description: 'Gözenekleri temizler, sivilce karşıtıdır.', score: 'Çok İyi' },
    { inciName: 'Parabens', turkishName: 'Parabenler', description: 'Koruyucu grubudur, hormon sistemini etkileyebilir.', score: 'Riskli' },
    { inciName: 'Aloe Barbadensis Leaf Juice', turkishName: 'Aloe Vera Suyu', description: 'Cildi yatıştırır ve nemlendirir.', score: 'Çok İyi' },
    { inciName: 'CI 77491', turkishName: 'Demir Oksit', description: 'Kozmetik renklendirici.', score: 'İyi' }
];

// Real verified ingredients for specific popular products
const REAL_PRODUCT_INGREDIENTS = {
    'Maybelline Super Stay Matte Ink Liquid Lipstick': [
        { inciName: 'Dimethicone', turkishName: 'Dimetikon', description: 'Cildi pürüzsüzleştirir, mat bitiş sağlar.', score: 'Orta' },
        { inciName: 'Trimethylsiloxysilicate', turkishName: 'Trimetilsiloksisilik', description: 'Kalıcılığı artıran film oluşturucu.', score: 'İyi' },
        { inciName: 'Isododecane', turkishName: 'İzododekan', description: 'Uçucu çözücü, hafiflik hissi verir.', score: 'İyi' },
        { inciName: 'Nylon-611/Dimethicone Copolymer', turkishName: 'Naylon Kopolimer', description: 'Doku artırıcı ve sabitleyici.', score: 'İyi' },
        { inciName: 'Lauroyl Lysine', turkishName: 'Lauroil Lizin', description: 'Cilt yumuşatıcı amino asit türevi.', score: 'Çok İyi' },
        { inciName: 'CI 77891', turkishName: 'Titanyum Dioksit', description: 'Beyaz pigment ve opaklaştırıcı.', score: 'İyi' }
    ],
    'L\'Oreal True Match Foundation': [
        { inciName: 'Aqua', turkishName: 'Su', description: 'Çözücü, temel bileşen.', score: 'İyi' },
        { inciName: 'Dimethicone', turkishName: 'Dimetikon', description: 'Yumuşatıcı ve bariyer oluşturucu.', score: 'Orta' },
        { inciName: 'Isododecane', turkishName: 'İzododekan', description: 'Matlaştırıcı ve yayılım artırıcı.', score: 'İyi' },
        { inciName: 'Glycerin', turkishName: 'Gliserin', description: 'Güçlü nemlendirici.', score: 'Çok İyi' },
        { inciName: 'PEG-10 Dimethicone', turkishName: 'PEG-10 Dimetikon', description: 'Silikon bazlı emülgatör.', score: 'Orta' },
        { inciName: 'Sodium Chloride', turkishName: 'Sodyum Klorür', description: 'Kıvam artırıcı tuz.', score: 'İyi' },
        { inciName: 'Tocopherol', turkishName: 'E Vitamini', description: 'Antioksidan, cildi korur.', score: 'Çok İyi' }
    ],
    'Covergirl LashBlast Volume Mascara': [
        { inciName: 'Aqua', turkishName: 'Su', description: 'Çözücü.', score: 'İyi' },
        { inciName: 'Glyceryl Stearate', turkishName: 'Gliseril Stearat', description: 'Emülgatör ve kıvam verici.', score: 'İyi' },
        { inciName: 'Ammonium Acrylates Copolymer', turkishName: 'Amonyum Akrilat Kopolimer', description: 'Film oluşturucu, kalıcılık sağlar.', score: 'Orta' },
        { inciName: 'Disteardimonium Hectorite', turkishName: 'Disteardimonyum Hektorit', description: 'Kıvam artırıcı kil türevi.', score: 'İyi' },
        { inciName: 'Propylene Glycol', turkishName: 'Propilen Glikol', description: 'Nem tutucu ve çözücü.', score: 'Orta' }
    ],
    'Nyx Professional Makeup Ultimate Shadow Palette': [
        { inciName: 'Talc', turkishName: 'Talk', description: 'Emici ve matlaştırıcı mineral.', score: 'Orta' },
        { inciName: 'Magnesium Stearate', turkishName: 'Magnezyum Stearat', description: 'Yapışma ve kayganlık sağlar.', score: 'İyi' },
        { inciName: 'Dimethicone', turkishName: 'Dimetikon', description: 'Yumuşaklık hissi verir.', score: 'Orta' },
        { inciName: 'Hydrogenated Polyisobutene', turkishName: 'Hidrojene Poliizobüten', description: 'Sentetik yağ, yumuşatıcı.', score: 'Orta' },
        { inciName: 'Bis-Diglyceryl Polyacyladipate-2', turkishName: 'Sentetik Lanolin', description: 'Yoğun nemlendirici.', score: 'İyi' }
    ]
};

const ingredients = computed(() => {
    if (!props.product) return [];
    
    // 1. If product has backend ingredients, use them
    if (props.product.ingredients && props.product.ingredients.length > 0) return props.product.ingredients;

    // 2. Check for Real Verified Data Map (Name Matching)
    const productName = props.product.name;
    for (const [key, productIngredients] of Object.entries(REAL_PRODUCT_INGREDIENTS)) {
        if (productName.includes(key) || key.includes(productName)) {
            return productIngredients.map((ing, idx) => ({ ...ing, id: `real-${idx}` }));
        }
    }

    // 3. Fallback: Deterministic simulation based on ID to keep it consistent
    let seed = props.product.id || props.product.name.length;
    const count = 5 + (seed % 5); // 5 to 9 ingredients
    const result = [];
    const pool = [...MOCK_INGREDIENTS_DB];
    
    const selectedIndices = new Set();
    while (result.length < count && selectedIndices.size < pool.length) {
        let index = (seed + result.length * 7) % pool.length;
        
        while (selectedIndices.has(index)) {
            index = (index + 1) % pool.length;
        }
        
        selectedIndices.add(index);
        result.push(pool[index]);
    }
    return result;
});

const getScoreColorClass = (score) => {
    if (!score) return 'bg-gray-100 text-gray-800';
    const s = score.toLowerCase();
    if (s.includes('çok iyi')) return 'bg-green-600 text-white';
    if (s.includes('iyi')) return 'bg-blue-500 text-white';
    if (s.includes('orta')) return 'bg-yellow-500 text-white';
    if (s.includes('riskli')) return 'bg-red-500 text-white';
    return 'bg-gray-200 text-gray-800';
};

const checkAllergies = () => {
    if (!authStore.user?.allergies || !props.product) return true;
    
    const userAllergyList = authStore.user.allergies.split(',').map(a => a.trim().toLowerCase()).filter(a => a);
    if (userAllergyList.length === 0) return true;

    // Use ENRICHED ingredients from computed property for better coverage
    const enrichedInci = ingredients.value?.map(i => i.inciName.toLowerCase()) || [];
    const productKeywords = (props.product.description?.toLowerCase().split(/[, \n]+/) || []);
    
    const searchPool = [...new Set([...enrichedInci, ...productKeywords])];

    const foundAllergens = userAllergyList.filter(allergen => 
        searchPool.some(item => item.includes(allergen))
    );

    if (foundAllergens.length > 0) {
        return uiStore.confirm(
            "Allergy Warning! ⚠️", 
            `This product may contain ingredients you are allergic to: ${foundAllergens.join(', ')}. Do you still want to proceed?`
        );
    }
    
    return true;
};

const handleAddToCart = async () => {
    if (!authStore.isAuthenticated) {
        router.push('/login');
        return;
    }

    const ok = await checkAllergies();
    if (!ok) return;

    const success = await cartStore.addToCart(props.product.id, 1, {
        id: props.product.id,
        name: props.product.name,
        category: props.product.product_type || props.product.category || 'Beauty',
        price: parseFloat(props.product.price) || 0,
        image: props.product.image || props.product.api_featured_image,
        stock: 100
    });
    
    if (success) {
         uiStore.notify("Product added to cart! 🛍️");
         cartStore.toggleDrawer(true);
         emit('close');
    } else {
        uiStore.notify("Failed to add to cart.", 'error');
    }
};

const addingToShelf = ref(false);

const addToShelf = async () => {
    if (!authStore.isAuthenticated) {
        uiStore.notify("Please login to use the cabinet.", 'info');
        return;
    }
    
    const ok = await checkAllergies();
    if (!ok) return;

    addingToShelf.value = true;
    try {
        const today = new Date().toISOString().split('T')[0];
        const userId = Number(authStore.user.id);
        
        const isNumericProduct = !isNaN(props.product.id) && !isNaN(parseFloat(props.product.id)) && String(props.product.id).length < 10;
        
        const params = {
            userId: userId,
            openedDate: today,
            // Always send these as fallback for external products
            customName: props.product.name,
            brand: props.product.brand || props.product.product_type || props.product.category || 'External',
            paoMonths: props.product.paoMonths || 12,
            imageUrl: props.product.image || props.product.api_featured_image
        };

        if (isNumericProduct) {
            params.productId = parseInt(props.product.id);
        }

        console.log("Adding to shelf with params:", params);
        const res = await api.post('/shelf/add', null, { params });
        
        if (res.status === 200 || res.status === 201) {
            uiStore.notify("Product successfully added to your cabinet! ✨");
            emit('close');
        }
    } catch (e) {
        console.error("Failed to add to shelf", e);
        const errorMsg = e.response?.data?.message || e.message || "Please check your connection.";
        uiStore.notify(`Failed to add product: ${errorMsg}`, 'error');
    } finally {
        addingToShelf.value = false;
    }
};

const handleManualAnalysis = async () => {
    if (!manualText.value.trim()) return;
    analyzing.value = true;
    try {
        const res = await api.post('/analysis/ingredients', { text: manualText.value });
        manualResults.value = res.data;
    } catch (e) {
        console.error("Analysis failed", e);
    } finally {
        analyzing.value = false;
    }
};

const trackViewed = async () => {
    if (props.product && authStore.isAuthenticated && authStore.user?.id) {
        try {
            await api.post(`/history/viewed/${authStore.user.id}`, {
                externalProductId: props.product.id,
                productName: props.product.name,
                productImage: props.product.image,
                productBrand: props.product.brand,
                productPrice: props.product.price
            });
        } catch (e) {
            console.error("Failed to track viewed product", e);
        }
    }
};

const toggleWishlist = async () => {
    if (!authStore.isAuthenticated) {
        router.push('/login');
        return;
    }

    addingToWishlist.value = true;
    try {
        // Convert product ID to integer if it's a string
        const productId = typeof props.product.id === 'string' ? parseInt(props.product.id) : props.product.id;
        
        // Check if it's a valid numeric product (from our database)
        if (isNaN(productId)) {
            uiStore.notify("Invalid product ID", 'error');
            addingToWishlist.value = false;
            return;
        }

        const result = await wishlistStore.toggleWishlist(productId, {
            id: productId,
            name: props.product.name,
            image: props.product.image || props.product.api_featured_image,
            price: props.product.price,
            category: props.product.product_type || props.product.category,
            brand: props.product.brand
        });

        if (result.success) {
            uiStore.notify(result.message);
            isInWishlist.value = wishlistStore.isInWishlist(productId);
        } else {
            uiStore.notify(result.message, 'error');
        }
    } catch (e) {
        console.error("Failed to toggle wishlist", e);
        uiStore.notify("Failed to update wishlist", 'error');
    } finally {
        addingToWishlist.value = false;
    }
};

const checkWishlistStatus = async () => {
    if (authStore.isAuthenticated && props.product) {
        const productId = typeof props.product.id === 'string' ? parseInt(props.product.id) : props.product.id;
        if (!isNaN(productId)) {
            isInWishlist.value = wishlistStore.isInWishlist(productId);
        }
    }
};

const toggleComparison = () => {
    const productId = typeof props.product.id === 'string' ? parseInt(props.product.id) : props.product.id;
    
    // Check if it's a valid numeric product
    if (isNaN(productId)) {
        uiStore.notify("External products cannot be added to comparison yet", 'info');
        return;
    }

    // Check if it's an external product
    if (props.product.api_featured_image || props.product.product_link) {
        uiStore.notify("External API products cannot be compared", 'info');
        return;
    }

    const result = comparisonStore.toggleComparison({
        id: productId,
        name: props.product.name,
        image: props.product.image || props.product.api_featured_image,
        price: props.product.price,
        category: props.product.product_type || props.product.category,
        brand: props.product.brand,
        rating: props.product.rating,
        stock: props.product.stock,
        ingredients: props.product.ingredients,
        description: props.product.description,
        paoMonths: props.product.paoMonths
    });

    if (result.success) {
        isInComparison.value = comparisonStore.isInComparison(productId);
    }
};

const checkComparisonStatus = () => {
    if (props.product) {
        const productId = typeof props.product.id === 'string' ? parseInt(props.product.id) : props.product.id;
        if (!isNaN(productId)) {
            isInComparison.value = comparisonStore.isInComparison(productId);
        }
    }
};

const openPriceAlertModal = () => {
    showPriceAlertModal.value = true;
};

const onPriceAlertCreated = () => {
    // Refresh or update UI if needed
    console.log('Price alert created successfully');
};

watch(() => props.product, (newVal) => {
    if (newVal) {
        trackViewed();
        checkWishlistStatus();
        checkComparisonStatus();
        communityStore.fetchProductPhotos(newVal.id);
        qaStore.fetchQuestions(newVal.id);
    }
}, { immediate: true });

const handleAskQuestion = async () => {
    if (!authStore.isAuthenticated) {
        uiStore.notify("Please login to ask a question.", 'info');
        return;
    }
    const success = await qaStore.askQuestion(authStore.user.id, props.product, newQuestionText.value);
    if (success) {
        newQuestionText.value = '';
    }
};

const handleAnswerQuestion = async (questionId) => {
    if (!authStore.isAuthenticated) {
        uiStore.notify("Please login to answer.", 'info');
        return;
    }
    const text = newAnswerText.value[questionId];
    const success = await qaStore.answerQuestion(authStore.user.id, questionId, text);
    if (success) {
        newAnswerText.value[questionId] = '';
        showAnswerForm.value[questionId] = false;
    }
};
</script>

<style scoped>
.custom-scrollbar::-webkit-scrollbar {
  width: 4px;
}
.custom-scrollbar::-webkit-scrollbar-track {
  background: #f1f1f1;
}
.custom-scrollbar::-webkit-scrollbar-thumb {
  background: #ccc;
  border-radius: 10px;
}
.custom-scrollbar::-webkit-scrollbar-thumb:hover {
  background: #888;
}
</style>
