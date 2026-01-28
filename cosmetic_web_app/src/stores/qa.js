import { defineStore } from 'pinia';
import { ref } from 'vue';
import api from '@/services/api';
import { useUiStore } from './ui';

export const useQAStore = defineStore('qa', () => {
    const questions = ref([]);
    const loading = ref(false);
    const uiStore = useUiStore();

    // Fetch questions for a specific product
    const fetchQuestions = async (productId) => {
        loading.value = true;
        try {
            const res = await api.get(`/qa/product/${productId}`);
            questions.value = res.data;
        } catch (error) {
            console.error('Failed to fetch questions:', error);
            uiStore.notify('Failed to load Q&A.', 'error');
        } finally {
            loading.value = false;
        }
    };

    // Submit a new question
    const askQuestion = async (userId, product, content) => {
        if (!content.trim()) return false;

        // Handle both ID only (old way) or full object
        let productId = product;
        let productDetails = {};

        if (typeof product === 'object') {
            productId = product.id;
            productDetails = {
                productName: product.name,
                productBrand: product.brand || product.product_type || product.category,
                productImage: product.image || product.api_featured_image,
                productPrice: product.price,
                description: product.description
            };
        }

        try {
            const res = await api.post('/qa/question', {
                userId,
                productId,
                content,
                ...productDetails
            });
            // Add to list immediately
            questions.value.unshift({ ...res.data, answers: [] });
            uiStore.notify('Question submitted!');
            return true;
        } catch (error) {
            console.error('Failed to ask question:', error);
            uiStore.notify('Failed to submit question.', 'error');
            return false;
        }
    };

    // Answer a question
    const answerQuestion = async (userId, questionId, content) => {
        if (!content.trim()) return false;

        try {
            const res = await api.post('/qa/answer', { userId, questionId, content });

            // Find question and add answer
            const q = questions.value.find(q => q.id === questionId);
            if (q) {
                if (!q.answers) q.answers = [];
                q.answers.push(res.data);
            }

            uiStore.notify('Answer submitted!');
            return true;
        } catch (error) {
            console.error('Failed to submit answer:', error);
            uiStore.notify('Failed to submit answer.', 'error');
            return false;
        }
    };

    // Vote on a question
    const voteQuestion = async (questionId) => {
        try {
            const res = await api.post(`/qa/question/${questionId}/vote`);
            const index = questions.value.findIndex(q => q.id === questionId);
            if (index !== -1) questions.value[index].upvotes = res.data.upvotes;
        } catch (error) {
            console.error('Failed to vote:', error);
        }
    };

    // Vote on an answer
    const voteAnswer = async (questionId, answerId) => {
        try {
            const res = await api.post(`/qa/answer/${answerId}/vote`);
            const q = questions.value.find(q => q.id === questionId);
            if (q && q.answers) {
                const ans = q.answers.find(a => a.id === answerId);
                if (ans) ans.upvotes = res.data.upvotes;
            }
        } catch (error) {
            console.error('Failed to vote:', error);
        }
    };

    return {
        questions,
        loading,
        fetchQuestions,
        askQuestion,
        answerQuestion,
        voteQuestion,
        voteAnswer
    };
});
