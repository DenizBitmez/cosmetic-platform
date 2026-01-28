<template>
  <Teleport to="body">
    <div v-if="isOpen" class="fixed inset-0 z-[100] overflow-y-auto" aria-labelledby="modal-title" role="dialog" aria-modal="true">
      <div class="flex items-end justify-center min-h-screen pt-4 px-4 pb-20 text-center sm:block sm:p-0">
        
        <div class="fixed inset-0 bg-gray-500 bg-opacity-75 transition-opacity" aria-hidden="true" @click="$emit('close')"></div>

        <span class="hidden sm:inline-block sm:align-middle sm:h-screen" aria-hidden="true">&#8203;</span>

        <div class="inline-block align-bottom bg-white rounded-lg text-left overflow-hidden shadow-xl transform transition-all sm:my-8 sm:align-middle sm:max-w-lg sm:w-full relative z-10">
          <div class="bg-white px-4 pt-5 pb-4 sm:p-6 sm:pb-4">
            <h3 class="text-lg leading-6 font-medium text-gray-900 font-serif mb-4" id="modal-title">
              Share Your Experience
            </h3>

            <!-- File Input -->
            <div class="mb-4">
                <label class="block text-sm font-medium text-gray-700 mb-2">Photo</label>
                <div 
                    class="mt-1 flex justify-center px-6 pt-5 pb-6 border-2 border-gray-300 border-dashed rounded-md cursor-pointer hover:border-brand-gold transition-colors"
                    @click="triggerFileInput"
                    @drop.prevent="handleDrop"
                    @dragover.prevent
                >
                    <div v-if="!previewUrl" class="space-y-1 text-center">
                        <svg class="mx-auto h-12 w-12 text-gray-400" stroke="currentColor" fill="none" viewBox="0 0 48 48" aria-hidden="true">
                            <path d="M28 8H12a4 4 0 00-4 4v20m32-12v8m0 0v8a4 4 0 01-4 4H12a4 4 0 01-4-4v-4m32-4l-3.172-3.172a4 4 0 00-5.656 0L28 28M8 32l9.172-9.172a4 4 0 015.656 0L28 28m0 0l4 4m4-24h8m-4-4v8m-12 4h.02" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" />
                        </svg>
                        <div class="flex text-sm text-gray-600">
                            <span class="relative cursor-pointer bg-white rounded-md font-medium text-brand-gold hover:text-brand-dark focus-within:outline-none focus-within:ring-2 focus-within:ring-offset-2 focus-within:ring-brand-gold">
                                Upload a file
                            </span>
                            <p class="pl-1">or drag and drop</p>
                        </div>
                        <p class="text-xs text-gray-500">PNG, JPG, GIF up to 5MB</p>
                    </div>
                    <div v-else class="relative w-full h-48">
                        <img :src="previewUrl" class="w-full h-full object-contain rounded-md">
                        <button @click.stop="removeFile" class="absolute top-0 right-0 -mt-2 -mr-2 bg-red-500 text-white rounded-full p-1 shadow hover:bg-red-600">
                            <svg class="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12"></path></svg>
                        </button>
                    </div>
                </div>
                <input type="file" ref="fileInput" class="hidden" accept="image/*" @change="handleFileChange">
            </div>

            <!-- Description -->
            <div class="mb-4">
                <label class="block text-sm font-medium text-gray-700 mb-2">Description (Optional)</label>
                <textarea 
                    v-model="description" 
                    rows="3" 
                    class="shadow-sm focus:ring-brand-gold focus:border-brand-gold mt-1 block w-full sm:text-sm border border-gray-300 rounded-md p-2" 
                    placeholder="Tell us about your results..."
                ></textarea>
            </div>

          </div>
          <div class="bg-gray-50 px-4 py-3 sm:px-6 sm:flex sm:flex-row-reverse">
            <button 
                type="button" 
                class="w-full inline-flex justify-center rounded-md border border-transparent shadow-sm px-4 py-2 bg-brand-gold text-base font-medium text-white hover:bg-brand-dark focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-brand-gold sm:ml-3 sm:w-auto sm:text-sm disabled:opacity-50 disabled:cursor-not-allowed"
                :disabled="!selectedFile || loading"
                @click="handleSubmit"
            >
                {{ loading ? 'Uploading...' : 'Share Photo' }}
            </button>
            <button 
                type="button" 
                class="mt-3 w-full inline-flex justify-center rounded-md border border-gray-300 shadow-sm px-4 py-2 bg-white text-base font-medium text-gray-700 hover:bg-gray-50 focus:outline-none sm:mt-0 sm:ml-3 sm:w-auto sm:text-sm" 
                @click="$emit('close')"
            >
                Cancel
            </button>
          </div>
        </div>
      </div>
    </div>
  </Teleport>
</template>

<script setup>
import { ref } from 'vue';
import { useCommunityStore } from '@/stores/community';
import { useAuthStore } from '@/stores/auth';

const props = defineProps({
    isOpen: Boolean,
    product: Object
});

const emit = defineEmits(['close']);

const communityStore = useCommunityStore();
const authStore = useAuthStore();

const fileInput = ref(null);
const selectedFile = ref(null);
const previewUrl = ref(null);
const description = ref('');
const loading = ref(false);

const triggerFileInput = () => {
    fileInput.value.click();
};

const handleFileChange = (event) => {
    const file = event.target.files[0];
    if (file) processFile(file);
};

const handleDrop = (event) => {
    const file = event.dataTransfer.files[0];
    if (file) processFile(file);
};

const processFile = (file) => {
    if (!file.type.startsWith('image/')) return;
    selectedFile.value = file;
    previewUrl.value = URL.createObjectURL(file);
};

const removeFile = () => {
    selectedFile.value = null;
    previewUrl.value = null;
    if (fileInput.value) fileInput.value.value = '';
};

const handleSubmit = async () => {
    if (!selectedFile.value || !props.product || !authStore.user) return;

    loading.value = true;
    try {
        const success = await communityStore.uploadPhoto(
            selectedFile.value,
            props.product.id,
            authStore.user.id,
            description.value
        );
        
        if (success) {
            emit('close');
            // Reset form
            removeFile();
            description.value = '';
        }
    } finally {
        loading.value = false;
    }
};
</script>
