<template>
  <div class="min-h-screen bg-brand-cream pb-20">
    <!-- Hero Section -->
    <div class="relative bg-gray-900 h-[60vh] overflow-hidden">
        <div class="absolute inset-0">
             <img class="w-full h-full object-cover opacity-60" src="https://images.unsplash.com/photo-1596462502278-27bfdc403348?q=80&w=2670&auto=format&fit=crop" alt="Cosmetics Background">
        </div>
        <div class="relative max-w-7xl mx-auto py-24 px-4 sm:py-32 sm:px-6 lg:px-8 h-full flex items-center">
             <div class="max-w-xl">
                <h1 class="text-4xl font-extrabold tracking-tight text-white sm:text-5xl lg:text-6xl font-serif mb-6">
                    Redefine Your Glow
                </h1>
                <p class="text-xl text-gray-100 mb-8 max-w-2xl mx-auto">
                    Curated collection of premium skincare and cosmetics for the modern individual.
                </p>
                
                <!-- Skin Quiz CTA -->
                <div class="mb-8">
                    <router-link to="/skin-quiz" class="inline-flex items-center gap-2 bg-brand-gold text-white px-8 py-4 rounded-full text-lg font-semibold hover:bg-brand-gold/90 transition-all shadow-lg hover:shadow-xl">
                        <PhSparkle :size="24" weight="fill" />
                        Take Our Skin Quiz
                        <PhArrowRight :size="20" weight="bold" />
                    </router-link>
                    <p class="text-sm text-gray-100 mt-3">Get personalized product recommendations in 2 minutes</p>
                </div>
             </div>
        </div>
    </div>

    <!-- Features Section -->
    <div class="py-16 bg-white">
        <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 grid grid-cols-1 md:grid-cols-3 gap-8 text-center">
            <div class="p-6">
                <div class="text-brand-gold mb-4 flex justify-center"><PhSparkle :size="32" /></div>
                <h3 class="text-lg font-bold uppercase tracking-wide mb-2">Premium Quality</h3>
                <p class="text-gray-500 font-light">Sourced from the finest ingredients worldwide.</p>
            </div>
            <div class="p-6 border-l border-r border-gray-100">
                <div class="text-brand-gold mb-4 flex justify-center"><PhPlant :size="32" /></div>
                <h3 class="text-lg font-bold uppercase tracking-wide mb-2">Cruelty Free</h3>
                <p class="text-gray-500 font-light">Ethical beauty that cares for the planet.</p>
            </div>
            <div class="p-6">
                 <div class="text-brand-gold mb-4 flex justify-center"><PhTruck :size="32" /></div>
                <h3 class="text-lg font-bold uppercase tracking-wide mb-2">Fast Delivery</h3>
                <p class="text-gray-500 font-light">Luxury delivered to your doorstep in days.</p>
            </div>
        </div>
    </div>

    <!-- Recommended for You Section -->
    <div v-if="recommendationStore.hasCompletedQuiz && recommendations.length > 0" class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 mt-16">
        <div class="flex items-center justify-between mb-12">
            <div>
                <h2 class="text-3xl font-serif text-brand-dark mb-2">Recommended for You</h2>
                <p class="text-gray-500 font-light italic">Personalized picks based on your skin profile</p>
            </div>
            <router-link to="/skin-quiz" class="text-xs font-bold uppercase tracking-widest text-brand-gold hover:text-brand-dark transition-colors border-b border-brand-gold">
                Retake Quiz
            </router-link>
        </div>
        
        <div class="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 xl:grid-cols-5 2xl:grid-cols-6 gap-y-12 gap-x-8">
            <ProductCard 
                v-for="product in recommendations" 
                :key="product.id" 
                :product="product" 
                @click="selectedProduct = product"
                class="transition-opacity duration-300"
            />
        </div>
    </div>

    <!-- Product Grid -->
    <div id="products" class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 mt-16">
        <div class="flex items-center justify-between mb-12">
            <h2 class="text-3xl font-serif text-brand-dark">Latest Arrivals</h2>
             <a href="#" class="text-brand-gold hover:text-brand-dark transition-colors border-b border-brand-gold pb-1 text-sm uppercase tracking-wide">View All</a>
        </div>
        
        <div v-if="loading" class="text-center py-24">
             <div class="animate-pulse flex flex-col items-center">
                <div class="h-4 bg-gray-200 rounded w-1/4 mb-4"></div>
                <div class="h-4 bg-gray-200 rounded w-1/6"></div>
             </div>
        </div>

        <div v-else-if="error" class="text-center py-12 text-red-500 bg-red-50 rounded p-8">
            <p class="font-bold">Something went wrong.</p>
            <p>{{ error }}</p>
        </div>

        <div v-else class="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 xl:grid-cols-5 2xl:grid-cols-6 gap-y-12 gap-x-8">
            <ProductCard 
                v-for="product in filteredProducts" 
                :key="product.id" 
                :product="product" 
                @remove="handleRemove(product)"
                @click="selectedProduct = product"
                class="transition-opacity duration-300"
            />
        </div>

        <!-- Blog Section -->
        <div class="mt-32">
            <div class="flex items-center justify-between mb-12">
                <div>
                    <h2 class="text-3xl font-serif text-brand-dark mb-2">Expert Insights</h2>
                    <p class="text-gray-500 font-light italic">Beauty guidance from the experts you trust.</p>
                </div>
                <router-link to="/blog" class="text-xs font-bold uppercase tracking-widest text-brand-gold hover:text-brand-dark transition-colors border-b border-brand-gold">Read All Articles</router-link>
            </div>

            <div v-if="loading && blogPosts.length === 0" class="grid grid-cols-1 md:grid-cols-3 gap-8">
                <div v-for="i in 3" :key="i" class="animate-pulse">
                    <div class="aspect-[16/10] bg-gray-100 rounded-2xl mb-6"></div>
                    <div class="h-4 bg-gray-100 rounded w-1/4 mb-4"></div>
                    <div class="h-6 bg-gray-100 rounded w-3/4 mb-4"></div>
                    <div class="h-4 bg-gray-100 rounded w-full"></div>
                </div>
            </div>

            <div v-else-if="blogPosts.length === 0" class="text-center py-20 bg-white/50 rounded-3xl border border-dashed border-gray-200">
                <p class="text-gray-400 font-light italic">More expert insights coming soon.</p>
            </div>

            <div v-else class="grid grid-cols-1 md:grid-cols-3 gap-8">
                <div v-for="post in blogPosts" :key="post.id" class="group cursor-pointer" @click="router.push(`/blog/${post.id}`)">
                    <div class="aspect-[16/10] overflow-hidden rounded-2xl mb-6">
                        <img :src="post.imageUrl || 'https://images.unsplash.com/photo-1596462502278-27bfdc4033c8?q=80&w=1760&auto=format&fit=crop'" 
                             class="w-full h-full object-cover group-hover:scale-105 transition-transform duration-700" alt="Blog Image">
                    </div>
                    <div class="space-y-3">
                        <div class="flex items-center gap-2">
                            <span class="text-[10px] bg-brand-cream text-brand-gold px-2 py-0.5 rounded-full font-bold uppercase tracking-wider">Expert Advice</span>
                            <span class="text-[10px] text-gray-400 capitalize">{{ post.authorName }}</span>
                        </div>
                        <h3 class="text-xl font-serif text-brand-dark group-hover:text-brand-gold transition-colors">{{ post.title }}</h3>
                        <p class="text-sm text-gray-500 line-clamp-2 font-light leading-relaxed">{{ post.content }}</p>
                    </div>
                </div>
            </div>
        </div>

        <!-- Product Detail Modal -->
        <ProductDetailModal 
            :product="selectedProduct" 
            @close="selectedProduct = null"
        />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue';
import api from '@/services/api';
import ProductCard from '@/components/ProductCard.vue';
import ProductDetailModal from '@/components/ProductDetailModal.vue';
import { PhSparkle, PhPlant, PhTruck, PhArrowRight } from '@phosphor-icons/vue';
import { useProductStore } from '@/stores/product';
import { useRecommendationStore } from '@/stores/recommendation';
import { useAuthStore } from '@/stores/auth';
import { useRouter } from 'vue-router';

const router = useRouter();
const productStore = useProductStore();
const recommendationStore = useRecommendationStore();
const authStore = useAuthStore();

const products = computed(() => productStore.latestArrivals);
const recommendations = computed(() => recommendationStore.recommendations);
const blogPosts = ref([]);
const loading = computed(() => productStore.loading);
const error = computed(() => productStore.error);
const selectedProduct = ref(null);

const fetchBlogPosts = async () => {
    try {
        const res = await api.get('/blog');
        blogPosts.value = res.data.slice(0, 3);
    } catch (e) {
        console.error("Failed to fetch blog posts", e);
    }
};

const filteredProducts = computed(() => {
    return products.value.slice(0, 6);
});

const handleRemove = (productToRemove) => {
    productStore.latestArrivals = productStore.latestArrivals.filter(p => p.id !== productToRemove.id);
};

onMounted(async () => {
    console.log('HomeView mounted, auth user:', authStore.user);
    await productStore.fetchLatestArrivals();
    
    // Only fetch recommendations if user is logged in
    if (authStore.user && authStore.user.id) {
        await recommendationStore.fetchSkinProfile();
        if (recommendationStore.hasCompletedQuiz) {
            await recommendationStore.fetchRecommendations(6);
        }
    }
    
    await fetchBlogPosts();
});
</script>
