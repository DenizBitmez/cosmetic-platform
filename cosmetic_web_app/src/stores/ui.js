import { defineStore } from 'pinia';
import { ref } from 'vue';

export const useUiStore = defineStore('ui', () => {
    const notifications = ref([]);

    const notify = (message, type = 'success', duration = 4000) => {
        const id = Date.now();
        notifications.value.push({
            id,
            message,
            type, // 'success', 'error', 'warning', 'info'
            duration
        });

        if (duration > 0) {
            setTimeout(() => {
                removeNotification(id);
            }, duration);
        }
        return id;
    };

    const removeNotification = (id) => {
        const index = notifications.value.findIndex(n => n.id === id);
        if (index !== -1) {
            notifications.value.splice(index, 1);
        }
    };

    // For confirmations
    const confirmModal = ref({
        show: false,
        title: '',
        message: '',
        resolve: null,
        reject: null
    });

    const confirm = (title, message) => {
        confirmModal.value.show = true;
        confirmModal.value.title = title;
        confirmModal.value.message = message;

        return new Promise((resolve, reject) => {
            confirmModal.value.resolve = resolve;
            confirmModal.value.reject = reject;
        });
    };

    const handleConfirm = (value) => {
        confirmModal.value.show = false;
        if (value && confirmModal.value.resolve) {
            confirmModal.value.resolve(true);
        } else if (!value && confirmModal.value.resolve) {
            confirmModal.value.resolve(false);
        }
    };

    return {
        notifications,
        notify,
        removeNotification,
        confirmModal,
        confirm,
        handleConfirm
    };
});
