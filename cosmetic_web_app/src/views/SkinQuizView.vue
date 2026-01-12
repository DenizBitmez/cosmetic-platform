<template>
  <div class="min-h-screen bg-gradient-to-br from-brand-cream via-white to-brand-cream/50 py-12 px-4">
    <div class="max-w-2xl mx-auto">
      <!-- Header -->
      <div class="text-center mb-8">
        <h1 class="text-4xl font-bold text-brand-dark mb-2">Skin Type Quiz</h1>
        <p class="text-gray-600">Discover your perfect skincare routine in 6 simple steps</p>
      </div>

      <!-- Progress Bar -->
      <div class="mb-8">
        <div class="flex justify-between items-center mb-2">
          <span class="text-sm font-medium text-gray-700">Step {{ currentStep }} of 6</span>
          <span class="text-sm text-gray-500">{{ Math.round((currentStep / 6) * 100) }}% Complete</span>
        </div>
        <div class="w-full bg-gray-200 rounded-full h-2">
          <div class="bg-brand-gold h-2 rounded-full transition-all duration-300" :style="{ width: `${(currentStep / 6) * 100}%` }"></div>
        </div>
      </div>

      <!-- Quiz Card -->
      <div class="bg-white rounded-2xl shadow-xl p-6 md:p-8">
        <!-- Step 1: Skin Type -->
        <div v-if="currentStep === 1" class="space-y-6">
          <h2 class="text-2xl font-bold text-gray-900 mb-4">What's your skin type?</h2>
          <p class="text-gray-600 mb-6">How does your skin feel by midday?</p>
          
          <div class="space-y-3">
            <button v-for="option in skinTypeOptions" :key="option.value" @click="selectSkinType(option.value)" :class="['w-full p-4 border-2 rounded-lg text-left transition-all', quizData.skinType === option.value ? 'border-brand-gold bg-brand-cream/30' : 'border-gray-200 hover:border-brand-gold/50']">
              <div class="font-semibold text-gray-900">{{ option.label }}</div>
              <div class="text-sm text-gray-500 mt-1">{{ option.description }}</div>
            </button>
          </div>
        </div>

        <!-- Step 2: Skin Concerns -->
        <div v-else-if="currentStep === 2" class="space-y-6">
          <h2 class="text-2xl font-bold text-gray-900 mb-4">What are your main skin concerns?</h2>
          <p class="text-gray-600 mb-6">Select all that apply</p>
          
          <div class="grid grid-cols-2 gap-3">
            <button v-for="concern in concernOptions" :key="concern.value" @click="toggleConcern(concern.value)" :class="['p-4 border-2 rounded-lg text-center transition-all', quizData.concerns.includes(concern.value) ? 'border-brand-gold bg-brand-cream/30' : 'border-gray-200 hover:border-brand-gold/50']">
              <div class="text-2xl mb-2">{{ concern.icon }}</div>
              <div class="font-medium text-sm">{{ concern.label }}</div>
            </button>
          </div>
        </div>

        <!-- Step 3: Skin Tone -->
        <div v-else-if="currentStep === 3" class="space-y-6">
          <h2 class="text-2xl font-bold text-gray-900 mb-4">What's your skin tone?</h2>
          <p class="text-gray-600 mb-6">This helps us recommend the right products</p>
          
          <div class="grid grid-cols-5 gap-3">
            <button v-for="tone in skinToneOptions" :key="tone.value" @click="selectSkinTone(tone.value)" :class="['aspect-square rounded-full border-4 transition-all', quizData.skinTone === tone.value ? 'border-brand-gold scale-110' : 'border-gray-300 hover:border-brand-gold/50']" :style="{ backgroundColor: tone.color }">
              <span class="sr-only">{{ tone.label }}</span>
            </button>
          </div>
          <div class="text-center text-sm text-gray-600 mt-4">
            {{ quizData.skinTone ? skinToneOptions.find(t => t.value === quizData.skinTone)?.label : 'Select your skin tone' }}
          </div>
        </div>

        <!-- Step 4: Age -->
        <div v-else-if="currentStep === 4" class="space-y-6">
          <h2 class="text-2xl font-bold text-gray-900 mb-4">What's your age range?</h2>
          <p class="text-gray-600 mb-6">Different ages have different skincare needs</p>
          
          <div class="space-y-3">
            <button v-for="range in ageRanges" :key="range.value" @click="selectAge(range.value)" :class="['w-full p-4 border-2 rounded-lg text-center transition-all', quizData.age === range.value ? 'border-brand-gold bg-brand-cream/30' : 'border-gray-200 hover:border-brand-gold/50']">
              <div class="font-semibold">{{ range.label }}</div>
            </button>
          </div>
        </div>

        <!-- Step 5: Allergies -->
        <div v-else-if="currentStep === 5" class="space-y-6">
          <h2 class="text-2xl font-bold text-gray-900 mb-4">Do you have any allergies?</h2>
          <p class="text-gray-600 mb-6">Optional - helps us filter out products you can't use</p>
          
          <textarea v-model="quizData.allergies" placeholder="e.g., fragrance, parabens, sulfates..." class="w-full p-4 border-2 border-gray-200 rounded-lg focus:border-brand-gold focus:ring-2 focus:ring-brand-gold/20 outline-none transition-all" rows="4"></textarea>
          
          <button @click="nextStep" class="text-brand-gold hover:underline text-sm">
            Skip this step →
          </button>
        </div>

        <!-- Step 6: Results -->
        <div v-else-if="currentStep === 6" class="space-y-6">
          <div class="text-center mb-6">
            <div class="inline-flex items-center justify-center w-16 h-16 bg-green-100 rounded-full mb-4">
              <PhCheck :size="32" weight="bold" class="text-green-600" />
            </div>
            <h2 class="text-2xl font-bold text-gray-900 mb-2">Your Skin Profile</h2>
            <p class="text-gray-600">Here's what we learned about your skin</p>
          </div>

          <div class="space-y-4 bg-gray-50 rounded-lg p-6">
            <div class="flex justify-between items-center">
              <span class="font-medium text-gray-700">Skin Type:</span>
              <span class="text-brand-dark font-semibold">{{ getSkinTypeLabel(quizData.skinType) }}</span>
            </div>
            <div class="flex justify-between items-center">
              <span class="font-medium text-gray-700">Main Concerns:</span>
              <span class="text-brand-dark font-semibold">{{ quizData.concerns.length }} selected</span>
            </div>
            <div class="flex justify-between items-center">
              <span class="font-medium text-gray-700">Skin Tone:</span>
              <span class="text-brand-dark font-semibold">{{ getSkinToneLabel(quizData.skinTone) }}</span>
            </div>
            <div class="flex justify-between items-center">
              <span class="font-medium text-gray-700">Age Range:</span>
              <span class="text-brand-dark font-semibold">{{ getAgeLabel(quizData.age) }}</span>
            </div>
          </div>

          <div v-if="saving" class="text-center py-4">
            <div class="inline-block animate-spin rounded-full h-8 w-8 border-b-2 border-brand-dark"></div>
          </div>
        </div>

        <!-- Navigation Buttons -->
        <div class="flex justify-between mt-8 pt-6 border-t">
          <!-- Back Button -->
          <button 
            v-if="currentStep > 1 && currentStep < 6" 
            @click="prevStep" 
            class="px-6 py-3 border-2 border-gray-300 rounded-lg font-medium text-gray-700 hover:border-gray-400 transition-colors"
          >
            ← Back
          </button>
          <div v-else></div>

          <!-- Next/Submit Buttons -->
          <div>
            <button 
              v-if="currentStep < 5" 
              @click="nextStep" 
              :disabled="!canProceed" 
              :class="['px-6 py-3 rounded-lg font-medium transition-colors', canProceed ? 'bg-brand-dark text-white hover:bg-black' : 'bg-gray-300 text-gray-500 cursor-not-allowed']"
            >
              Next →
            </button>
            <button 
              v-if="currentStep === 5" 
              @click="nextStep" 
              class="px-6 py-3 bg-brand-dark text-white rounded-lg font-medium hover:bg-black transition-colors"
            >
              See Results →
            </button>
            <button 
              v-if="currentStep === 6" 
              @click="saveAndFinish" 
              :disabled="saving" 
              class="px-6 py-3 bg-brand-gold text-white rounded-lg font-medium hover:bg-brand-gold/90 transition-colors disabled:opacity-50"
            >
              {{ saving ? 'Saving...' : 'Get My Recommendations' }}
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue';
import { useRouter } from 'vue-router';
import { useRecommendationStore } from '@/stores/recommendation';
import { useUiStore } from '@/stores/ui';
import { PhCheck } from '@phosphor-icons/vue';

const router = useRouter();
const recommendationStore = useRecommendationStore();
const uiStore = useUiStore();

const currentStep = ref(1);
const saving = ref(false);

const quizData = ref({
  skinType: null,
  concerns: [],
  skinTone: null,
  age: null,
  allergies: ''
});

const skinTypeOptions = [
  { value: 'OILY', label: 'Oily', description: 'Shiny, enlarged pores, prone to breakouts' },
  { value: 'DRY', label: 'Dry', description: 'Tight, flaky, rough texture' },
  { value: 'COMBINATION', label: 'Combination', description: 'Oily T-zone, dry cheeks' },
  { value: 'NORMAL', label: 'Normal', description: 'Balanced, not too oily or dry' },
  { value: 'SENSITIVE', label: 'Sensitive', description: 'Easily irritated, redness-prone' }
];

const concernOptions = [
  { value: 'acne', label: 'Acne & Breakouts', icon: '🔴' },
  { value: 'wrinkles', label: 'Fine Lines', icon: '📏' },
  { value: 'dark_spots', label: 'Dark Spots', icon: '⚫' },
  { value: 'pores', label: 'Large Pores', icon: '🕳️' },
  { value: 'redness', label: 'Redness', icon: '🔴' },
  { value: 'dullness', label: 'Dullness', icon: '💤' },
  { value: 'dryness', label: 'Dryness', icon: '🏜️' },
  { value: 'oiliness', label: 'Excess Oil', icon: '💧' }
];

const skinToneOptions = [
  { value: 'FAIR', label: 'Fair', color: '#FFDFC4' },
  { value: 'LIGHT', label: 'Light', color: '#F0C8A0' },
  { value: 'MEDIUM', label: 'Medium', color: '#D4A574' },
  { value: 'TAN', label: 'Tan', color: '#B68655' },
  { value: 'DEEP', label: 'Deep', color: '#8D5524' }
];

const ageRanges = [
  { value: 20, label: 'Under 18' },
  { value: 21, label: '18-24' },
  { value: 29, label: '25-34' },
  { value: 39, label: '35-44' },
  { value: 49, label: '45-54' },
  { value: 60, label: '55+' }
];

const canProceed = computed(() => {
  if (currentStep.value === 1) return quizData.value.skinType !== null;
  if (currentStep.value === 2) return quizData.value.concerns.length > 0;
  if (currentStep.value === 3) return quizData.value.skinTone !== null;
  if (currentStep.value === 4) return quizData.value.age !== null;
  return true;
});

const selectSkinType = (type) => {
  quizData.value.skinType = type;
};

const toggleConcern = (concern) => {
  const index = quizData.value.concerns.indexOf(concern);
  if (index > -1) {
    quizData.value.concerns.splice(index, 1);
  } else {
    quizData.value.concerns.push(concern);
  }
};

const selectSkinTone = (tone) => {
  quizData.value.skinTone = tone;
};

const selectAge = (age) => {
  quizData.value.age = age;
};

const nextStep = () => {
  if (currentStep.value < 6) {
    currentStep.value++;
  }
};

const prevStep = () => {
  if (currentStep.value > 1) {
    currentStep.value--;
  }
};

const getSkinTypeLabel = (value) => {
  return skinTypeOptions.find(o => o.value === value)?.label || value;
};

const getSkinToneLabel = (value) => {
  return skinToneOptions.find(o => o.value === value)?.label || value;
};

const getAgeLabel = (value) => {
  return ageRanges.find(o => o.value === value)?.label || value;
};

const saveAndFinish = async () => {
  saving.value = true;

  const profileData = {
    skinType: quizData.value.skinType,
    skinConcerns: JSON.stringify(quizData.value.concerns),
    skinTone: quizData.value.skinTone,
    age: quizData.value.age
  };

  const result = await recommendationStore.saveSkinProfile(profileData);

  saving.value = false;

  if (result.success) {
    uiStore.notify('Skin profile saved! Check out your personalized recommendations', 'success');
    router.push('/');
  } else {
    uiStore.notify(result.message, 'error');
  }
};
</script>

<style scoped>
.sr-only {
  position: absolute;
  width: 1px;
  height: 1px;
  padding: 0;
  margin: -1px;
  overflow: hidden;
  clip: rect(0, 0, 0, 0);
  white-space: nowrap;
  border-width: 0;
}
</style>
