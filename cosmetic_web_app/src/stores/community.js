import { defineStore } from 'pinia';
import { ref } from 'vue';
import api from '@/services/api';
import { useUiStore } from './ui';

export const useCommunityStore = defineStore('community', () => {
    const photos = ref([]);
    const loading = ref(false);
    const uiStore = useUiStore();

    // Fetch photos for a specific product
    const fetchProductPhotos = async (productId) => {
        loading.value = true;
        try {
            const res = await api.get(`/photos/product/${productId}`);
            photos.value = res.data;
        } catch (error) {
            console.error('Failed to fetch photos:', error);
            uiStore.notify('Failed to load community photos.', 'error');
        } finally {
            loading.value = false;
        }
    };

    // Upload a new photo
    const uploadPhoto = async (file, productId, userId, description) => {
        const formData = new FormData();
        formData.append('file', file);
        formData.append('productId', productId);
        formData.append('userId', userId);
        if (description) {
            formData.append('description', description);
        }

        try {
            const res = await api.post('/photos/upload', formData, {
                headers: {
                    'Content-Type': 'multipart/form-data'
                }
            });
            // Add new photo to the list immediately
            photos.value.unshift(res.data);
            uiStore.notify('Photo uploaded successfully!');
            return true;
        } catch (error) {
            console.error('Upload failed:', error);
            uiStore.notify('Failed to upload photo.', 'error');
            return false;
        }
    };

    // Like a photo
    const likePhoto = async (photoId) => {
        try {
            const res = await api.post(`/photos/${photoId}/like`);
            // Update the photo in the list
            const index = photos.value.findIndex(p => p.id === photoId);
            if (index !== -1) {
                photos.value[index] = res.data;
            }
        } catch (error) {
            console.error('Failed to like photo:', error);
        }
    };

    return {
        photos,
        loading,
        fetchProductPhotos,
        uploadPhoto,
        likePhoto
    };
});
