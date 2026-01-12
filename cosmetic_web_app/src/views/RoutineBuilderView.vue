<template>
  <div class="min-h-screen bg-gray-50 pt-8 pb-16">
    <div class="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8">
      
      <!-- Header -->
      <div class="text-center mb-10">
        <h1 class="text-3xl font-serif text-brand-dark mb-2">My Skincare Routine</h1>
        <p class="text-gray-600">Build and track your daily rituals</p>
      </div>

      <!-- Routine Tabs -->
      <div class="bg-white rounded-2xl shadow-xl overflow-hidden min-h-[600px] flex flex-col">
          <div class="flex border-b">
              <button 
                @click="activeTab = 'MORNING'"
                :class="['flex-1 py-4 text-center text-sm font-bold uppercase tracking-widest transition-colors flex items-center justify-center gap-2', activeTab === 'MORNING' ? 'text-brand-gold border-b-2 border-brand-gold bg-brand-cream/10' : 'text-gray-400 hover:text-gray-600']"
              >
                  <PhSun :size="20" weight="fill" />
                  Morning
              </button>
              <button 
                @click="activeTab = 'EVENING'"
                :class="['flex-1 py-4 text-center text-sm font-bold uppercase tracking-widest transition-colors flex items-center justify-center gap-2', activeTab === 'EVENING' ? 'text-indigo-900 border-b-2 border-indigo-900 bg-indigo-50' : 'text-gray-400 hover:text-gray-600']"
              >
                  <PhMoon :size="20" weight="fill" />
                  Evening
              </button>
          </div>

          <!-- Content -->
          <div class="p-6 md:p-10 flex-1 relative bg-gradient-to-br from-white to-gray-50">
              
              <div v-if="routineStore.loading" class="absolute inset-0 flex items-center justify-center bg-white/80 z-10">
                  <div class="animate-spin rounded-full h-12 w-12 border-b-2 border-brand-gold"></div>
              </div>

              <!-- Steps List -->
              <div v-if="currentRoutine && currentRoutine.steps && currentRoutine.steps.length > 0" class="space-y-4">
                  <div 
                    v-for="(step, index) in currentRoutine.steps" 
                    :key="step.id"
                    class="bg-white p-4 rounded-xl border border-gray-100 shadow-sm flex items-center gap-4 transition-all hover:shadow-md group cursor-move relative"
                    :class="{ 'border-brand-gold bg-brand-cream/5 opacity-50': draggedStepId === step.id }"
                    draggable="true"
                    @dragstart="onDragStart($event, step, index)"
                    @dragend="onDragEnd"
                    @dragover.prevent
                    @dragenter.prevent="onDragEnter(index)"
                    @drop="onDrop(index)"
                  >
                      <!-- Drag Handle Indicator -->
                      <div class="absolute left-2 top-1/2 transform -translate-y-1/2 text-gray-300 opacity-0 group-hover:opacity-100 transition-opacity">
                         <div class="flex flex-col gap-0.5">
                            <div class="w-1 h-1 rounded-full bg-current"></div>
                            <div class="w-1 h-1 rounded-full bg-current"></div>
                            <div class="w-1 h-1 rounded-full bg-current"></div>
                         </div>
                      </div>

                      <!-- Step Number -->
                      <div class="w-8 h-8 rounded-full bg-brand-dark text-white flex items-center justify-center text-sm font-bold flex-shrink-0 ml-4 group-hover:ml-4 transition-all">
                          {{ index + 1 }}
                      </div>

                      <!-- Product Image -->
                      <div class="w-16 h-16 bg-gray-100 rounded-lg overflow-hidden flex-shrink-0 select-none">
                          <img :src="step.product?.image || 'https://images.unsplash.com/photo-1620916566398-39f1143ab7be?q=80&w=200'" class="w-full h-full object-cover">
                      </div>

                      <!-- Product Info -->
                      <div class="flex-1 min-w-0 select-none">
                          <h4 class="font-medium text-gray-900 truncate">{{ step.product?.name }}</h4>
                          <p class="text-xs text-gray-500 uppercase">{{ step.product?.category || 'Product' }}</p>
                          <p v-if="step.notes" class="text-xs text-gray-400 mt-1 italic">"{{ step.notes }}"</p>
                      </div>

                      <!-- Actions -->
                      <div class="flex items-center gap-2 opacity-0 group-hover:opacity-100 transition-opacity">
                          <button @click="removeStep(step.id)" class="p-2 text-gray-400 hover:text-red-500 transition-colors">
                              <PhTrash :size="18" />
                          </button>
                      </div>
                  </div>
              </div>

              <!-- Empty State -->
              <div v-else class="text-center py-20">
                  <div class="w-20 h-20 bg-gray-100 rounded-full flex items-center justify-center mx-auto mb-6 text-gray-400">
                      <component :is="activeTab === 'MORNING' ? PhSun : PhMoon" :size="40" weight="duotone" />
                  </div>
                  <h3 class="text-lg font-medium text-gray-900 mb-2">Build Your Routine</h3>
                  <p class="text-gray-500 max-w-sm mx-auto mb-8">Start adding steps to create your personalized {{ activeTab.toLowerCase() }} skincare ritual.</p>
              </div>

              <!-- Add Button -->
              <div class="mt-8 text-center">
                  <button 
                    @click="showSearchModal = true"
                    class="inline-flex items-center gap-2 px-6 py-3 bg-brand-gold text-white rounded-full font-bold hover:bg-brand-dark transition-all shadow-lg hover:shadow-xl transform hover:-translate-y-0.5"
                  >
                      <PhPlus :size="20" weight="bold" />
                      Add Product
                  </button>
              </div>

          </div>
      </div>
    </div>

    <!-- Modals -->
    <ProductSearchModal 
        :isOpen="showSearchModal" 
        @close="showSearchModal = false"
        @select="handleAddProduct"
    />
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import { useRoutineStore } from '@/stores/routine';
import { useAuthStore } from '@/stores/auth';
import { useUiStore } from '@/stores/ui';
import { PhSun, PhMoon, PhPlus, PhTrash } from '@phosphor-icons/vue';
import ProductSearchModal from '@/components/ProductSearchModal.vue';

const routineStore = useRoutineStore();
const authStore = useAuthStore();
const uiStore = useUiStore();

const activeTab = ref('MORNING');
const showSearchModal = ref(false);
const draggedStepId = ref(null);
const draggedStepIndex = ref(null);

const currentRoutine = computed(() => {
    return activeTab.value === 'MORNING' ? routineStore.morningRoutine : routineStore.eveningRoutine;
});

// Drag and Drop Handlers
const onDragStart = (event, step, index) => {
    draggedStepId.value = step.id;
    draggedStepIndex.value = index;
    // Set ghost image effect
    event.dataTransfer.effectAllowed = 'move';
    event.dataTransfer.dropEffect = 'move';
};

const onDragEnd = () => {
    draggedStepId.value = null;
    draggedStepIndex.value = null;
};

const onDragEnter = (targetIndex) => {
    if (draggedStepIndex.value === null || draggedStepIndex.value === targetIndex) return;

    // Optimistic reordering in UI
    const routine = currentRoutine.value;
    const steps = [...routine.steps];
    const draggedItem = steps[draggedStepIndex.value];
    
    // Remove from old position
    steps.splice(draggedStepIndex.value, 1);
    // Insert at new position
    steps.splice(targetIndex, 0, draggedItem);
    
    // Update store state (direct mutation for responsiveness)
    routine.steps = steps;
    draggedStepIndex.value = targetIndex;
};

const onDrop = async () => {
    if(!currentRoutine.value) return;
    
    // Persist new order
    const stepIds = currentRoutine.value.steps.map(s => s.id);
    try {
        await routineStore.reorderSteps(currentRoutine.value.id, stepIds);
    } catch (e) {
        uiStore.notify("Failed to save new order.", 'error');
        // Optionally revert here
    }
};

onMounted(async () => {
    if (authStore.user?.id) {
        await routineStore.fetchRoutines();
    }
});

const handleAddProduct = async (product) => {
    const userId = authStore.user?.id;
    if (!userId) return;

    try {
        let routine = currentRoutine.value;
        
        // If routine doesn't exist, create it first
        if (!routine) {
            const name = activeTab.value === 'MORNING' ? 'Morning Routine' : 'Evening Routine';
            routine = await routineStore.createRoutine(name, activeTab.value);
        }

        // Add step to routine
        await routineStore.addStep(routine.id, product.id);
        uiStore.notify(`${product.name} added to routine!`);
        
        // Refresh routines to update UI
        // Note: Store could handle optimistic updates better but fetching is safe
        await routineStore.fetchRoutines(); 
        
    } catch (e) {
        console.error(e);
        uiStore.notify("Failed to add product to routine.", 'error');
    }
};

const removeStep = async (stepId) => {
    if(!currentRoutine.value) return;
    
    try {
        await routineStore.removeStep(currentRoutine.value.id, stepId);
        uiStore.notify("Step removed.");
    } catch (e) {
        uiStore.notify("Failed to remove step.", 'error');
    }
};

</script>
