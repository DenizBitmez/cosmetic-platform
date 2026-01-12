import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import LoginView from '../views/LoginView.vue'
import RegisterView from '../views/RegisterView.vue'
import CategoriesView from '../views/CategoriesView.vue'
import CommunityView from '../views/CommunityView.vue'
import AboutView from '../views/AboutView.vue'

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: [
        {
            path: '/',
            name: 'home',
            component: HomeView
        },
        {
            path: '/categories',
            name: 'categories',
            component: CategoriesView
        },
        {
            path: '/community',
            name: 'community',
            component: CommunityView
        },
        {
            path: '/about',
            name: 'about',
            component: AboutView
        },
        {
            path: '/login',
            name: 'login',
            component: LoginView
        },
        {
            path: '/register',
            name: 'register',
            component: RegisterView
        },
        {
            path: '/checkout',
            name: 'checkout',
            component: () => import('../views/CheckoutView.vue')
        },
        {
            path: '/forgot-password',
            name: 'forgot-password',
            component: () => import('../views/ForgotPasswordView.vue')
        },
        {
            path: '/reset-password',
            name: 'reset-password',
            component: () => import('../views/ResetPasswordView.vue')
        },
        {
            path: '/profile',
            name: 'profile',
            component: () => import('../views/ProfileView.vue')
        },
        {
            path: '/product/:id',
            name: 'product-detail',
            component: () => import('../views/ProductDetailView.vue')
        },
        {
            path: '/blog',
            name: 'blog-list',
            component: () => import('../views/BlogListView.vue')
        },
        {
            path: '/blog/:id',
            name: 'blog-detail',
            component: () => import('../views/BlogDetailView.vue')
        },
        {
            path: '/blog/submit',
            name: 'blog-submit',
            component: () => import('../views/BlogSubmissionView.vue')
        },
        {
            path: '/comparison',
            name: 'comparison',
            component: () => import('../views/ComparisonView.vue')
        },
        {
            path: '/skin-quiz',
            name: 'skin-quiz',
            component: () => import('../views/SkinQuizView.vue')
        },
        {
            path: '/routine-builder',
            name: 'routine-builder',
            component: () => import('../views/RoutineBuilderView.vue')
        },
        {
            path: '/admin/blog',
            name: 'blog-admin',
            component: () => import('../views/BlogAdminView.vue')
        }
    ]
})

export default router
