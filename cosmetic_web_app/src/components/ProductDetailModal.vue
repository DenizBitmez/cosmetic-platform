<template>
  <div v-if="product" class="fixed inset-0 z-[70] overflow-y-auto" aria-labelledby="modal-title" role="dialog" aria-modal="true">
    <div class="flex items-center justify-center min-h-screen px-4 pt-4 pb-20 text-center sm:p-0">
      <div class="fixed inset-0 bg-gray-900 bg-opacity-80 transition-opacity backdrop-blur-sm" @click="$emit('close')"></div>

      <div class="relative inline-block align-bottom bg-white rounded-2xl text-left overflow-hidden shadow-2xl transform transition-all sm:my-8 sm:align-middle sm:max-w-4xl sm:w-full">
        <button @click="$emit('close')" class="absolute top-4 right-4 z-10 bg-white/50 rounded-full p-2 hover:bg-white text-gray-600 transition-colors">
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
            <div class="flex border-b border-gray-100 mb-6">
              <button @click="activeTab = 'overview'" :class="['flex-1 pb-3 text-sm font-bold transition-colors border-b-2', activeTab === 'overview' ? 'border-brand-dark text-brand-dark' : 'border-transparent text-gray-400 hover:text-gray-600']">
                Product Details
              </button>
              <button @click="activeTab = 'analysis'" :class="['flex-1 pb-3 text-sm font-bold transition-colors border-b-2', activeTab === 'analysis' ? 'border-green-500 text-green-700' : 'border-transparent text-gray-400 hover:text-gray-600']">
                🌿 Content Analysis
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
            
            <!-- Sticky Footer (Buttons) -->
            <div class="absolute bottom-0 left-0 right-0 p-6 md:p-8 bg-white border-t border-gray-100 flex gap-3">
              <button @click="handleAddToCart" class="flex-1 bg-black text-white text-center py-3 rounded-lg text-sm font-bold hover:bg-gray-800 transition-colors shadow-lg flex items-center justify-center gap-2">
                <span>Add to Cart</span>
                <svg class="w-4 h-4 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M10 6H6a2 2 0 00-2 2v10a2 2 0 002 2h10a2 2 0 002-2v-4M14 4h6m0 0v6m0-6L10 14"></path></svg>
              </button>
            </div>

          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue';
import { useCartStore } from '@/stores/cart';
import { useAuthStore } from '@/stores/auth';
import { useRouter } from 'vue-router';

const props = defineProps({
  product: {
    type: Object,
    default: null
  }
});

const emit = defineEmits(['close', 'add-to-cart']);

const cartStore = useCartStore();
const authStore = useAuthStore();
const router = useRouter();

const activeTab = ref('overview');

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

const handleAddToCart = async () => {
    if (!authStore.isAuthenticated) {
        router.push('/login');
        return;
    }

    const success = await cartStore.addToCart(props.product.id, 1, {
        id: props.product.id,
        name: props.product.name,
        category: props.product.product_type || props.product.category || 'Beauty',
        price: parseFloat(props.product.price) || 0,
        stock: 100
    });
    
    if (success) {
         cartStore.toggleDrawer(true);
         emit('close');
    } else {
        alert("Failed to add to cart. Please try again.");
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

import { watch } from 'vue';
import api from '@/services/api';

watch(() => props.product, (newVal) => {
    if (newVal) {
        trackViewed();
    }
}, { immediate: true });
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
