import { defineStore } from 'pinia';
import api from '@/services/api';
import { useAuthStore } from './auth';

export const useRoutineStore = defineStore('routine', {
    state: () => ({
        routines: [],
        loading: false,
        error: null
    }),

    getters: {
        morningRoutine: (state) => state.routines.find(r => r.timeOfDay === 'MORNING'),
        eveningRoutine: (state) => state.routines.find(r => r.timeOfDay === 'EVENING'),
    },

    actions: {
        async fetchRoutines() {
            const auth = useAuthStore();
            if (!auth.user?.id) return;

            this.loading = true;
            try {
                const response = await api.get(`/routines/user/${auth.user.id}`);
                this.routines = response.data;
            } catch (err) {
                console.error('Failed to fetch routines:', err);
                this.error = 'Failed to load routines';
            } finally {
                this.loading = false;
            }
        },

        async createRoutine(name, timeOfDay) {
            const auth = useAuthStore();
            if (!auth.user?.id) return;

            try {
                const response = await api.post('/routines', {
                    userId: auth.user.id,
                    name,
                    timeOfDay
                });
                this.routines.push(response.data);
                return response.data;
            } catch (err) {
                console.error('Failed to create routine:', err);
                throw err;
            }
        },

        async addStep(routineId, productId, notes = '') {
            try {
                const response = await api.post(`/routines/${routineId}/steps`, {
                    productId,
                    notes
                });

                // Update local state
                const routine = this.routines.find(r => r.id === routineId);
                if (routine) {
                    if (!routine.steps) routine.steps = [];
                    routine.steps.push(response.data);
                    // Sort by order just in case
                    routine.steps.sort((a, b) => a.stepOrder - b.stepOrder);
                }

                return response.data;
            } catch (err) {
                console.error('Failed to add step:', err);
                throw err;
            }
        },

        async removeStep(routineId, stepId) {
            try {
                await api.delete(`/routines/steps/${stepId}`);

                // Update local state
                const routine = this.routines.find(r => r.id === routineId);
                if (routine && routine.steps) {
                    routine.steps = routine.steps.filter(s => s.id !== stepId);
                }
            } catch (err) {
                console.error('Failed to remove step:', err);
                throw err;
            }
        },

        async reorderSteps(routineId, stepIds) {
            try {
                // Optimistic update
                const routine = this.routines.find(r => r.id === routineId);
                if (routine && routine.steps) {
                    // Create a map for quick lookup
                    const stepMap = new Map(routine.steps.map(s => [s.id, s]));

                    // Reconstruct steps array based on new ID order
                    const newSteps = [];
                    stepIds.forEach((id, index) => {
                        const step = stepMap.get(id);
                        if (step) {
                            step.stepOrder = index + 1;
                            newSteps.push(step);
                        }
                    });

                    routine.steps = newSteps;
                }

                await api.put(`/routines/${routineId}/reorder`, stepIds);
            } catch (err) {
                console.error('Failed to reorder steps:', err);
                // Revert on failure (could fetch fresh data)
                await this.fetchRoutines();
                throw err;
            }
        }
    }
});
