<template>
    <div class="fixed bottom-8 right-8 z-[100] flex flex-col gap-3 pointer-events-none">
        <transition-group name="toast">
            <div 
                v-for="notif in notifications" 
                :key="notif.id"
                class="pointer-events-auto min-w-[320px] max-w-md bg-white rounded-2xl shadow-2xl border border-gray-100 p-4 flex items-center gap-4 animate-in slide-in-from-right duration-300"
            >
                <!-- Icon -->
                <div :class="['p-2 rounded-full shrink-0', getIconBg(notif.type)]">
                    <component 
                        :is="getIcon(notif.type)" 
                        :size="20" 
                        :class="getIconColor(notif.type)" 
                        weight="bold" 
                    />
                </div>

                <!-- Content -->
                <div class="flex-1">
                    <p class="text-xs font-bold text-gray-900 leading-tight">{{ notif.message }}</p>
                </div>

                <!-- Close -->
                <button @click="uiStore.removeNotification(notif.id)" class="text-gray-300 hover:text-gray-500 transition-colors">
                    <PhX :size="16" />
                </button>
            </div>
        </transition-group>
    </div>

    <!-- Global Confirm Modal -->
    <div v-if="confirmModal.show" class="fixed inset-0 z-[110] flex items-center justify-center p-4 bg-black/60 backdrop-blur-sm">
        <div class="bg-white rounded-3xl shadow-2xl max-w-sm w-full p-8 text-center">
            <div class="w-16 h-16 bg-yellow-50 text-yellow-500 rounded-full flex items-center justify-center mx-auto mb-6">
                <PhWarning :size="32" weight="bold" />
            </div>
            <h3 class="text-xl font-serif text-gray-900 mb-2">{{ confirmModal.title }}</h3>
            <p class="text-sm text-gray-500 leading-relaxed mb-8">{{ confirmModal.message }}</p>
            <div class="flex gap-3">
                <button 
                    @click="uiStore.handleConfirm(false)"
                    class="flex-1 px-6 py-3 rounded-xl text-sm font-bold text-gray-500 bg-gray-50 hover:bg-gray-100 transition-all border border-gray-100"
                >
                    Cancel
                </button>
                <button 
                    @click="uiStore.handleConfirm(true)"
                    class="flex-1 px-6 py-3 rounded-xl text-sm font-bold text-white bg-black hover:bg-gray-800 transition-all shadow-lg"
                >
                    Proceed
                </button>
            </div>
        </div>
    </div>
</template>

<script setup>
import { useUiStore } from '@/stores/ui';
import { storeToRefs } from 'pinia';
import { PhCheckCircle, PhWarning, PhInfo, PhXCircle, PhX, PhWarningCircle } from '@phosphor-icons/vue';

const uiStore = useUiStore();
const { notifications, confirmModal } = storeToRefs(uiStore);

const getIcon = (type) => {
    switch (type) {
        case 'success': return PhCheckCircle;
        case 'error': return PhXCircle;
        case 'warning': return PhWarningCircle;
        case 'info': return PhInfo;
        default: return PhInfo;
    }
};

const getIconBg = (type) => {
    switch (type) {
        case 'success': return 'bg-green-50';
        case 'error': return 'bg-red-50';
        case 'warning': return 'bg-yellow-50';
        case 'info': return 'bg-blue-50';
        default: return 'bg-gray-50';
    }
};

const getIconColor = (type) => {
    switch (type) {
        case 'success': return 'text-green-600';
        case 'error': return 'text-red-600';
        case 'warning': return 'text-yellow-600';
        case 'info': return 'text-blue-600';
        default: return 'text-gray-600';
    }
};
</script>

<style scoped>
.toast-enter-active,
.toast-leave-active {
    transition: all 0.3s ease;
}
.toast-enter-from {
    opacity: 0;
    transform: translateX(30px);
}
.toast-leave-to {
    opacity: 0;
    transform: scale(0.9);
}
</style>
