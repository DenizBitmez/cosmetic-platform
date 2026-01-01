<template>
  <footer class="bg-brand-dark text-white pt-16 pb-8">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
      <div class="grid grid-cols-1 md:grid-cols-4 gap-12">
        <!-- Brand Info -->
        <div class="col-span-1 md:col-span-1">
          <h2 class="text-2xl font-serif text-brand-gold mb-6 tracking-widest uppercase">Glow & Grace</h2>
          <p class="text-gray-400 text-sm leading-relaxed mb-6">
            Elevating your natural beauty with clean, curated, and high-performance cosmetic essentials. Designed for the modern individual who values both science and soul.
          </p>
          <div class="flex space-x-4">
             <a href="#" class="text-gray-400 hover:text-brand-gold transition-colors">
                <span class="sr-only">Instagram</span>
                <svg class="h-6 w-6" fill="currentColor" viewBox="0 0 24 24"><path d="M12 2.163c3.204 0 3.584.012 4.85.07 3.252.148 4.771 1.691 4.919 4.919.058 1.265.069 1.645.069 4.849 0 3.205-.012 3.584-.069 4.849-.149 3.225-1.664 4.771-4.919 4.919-1.266.058-1.644.07-4.85.07-3.204 0-3.584-.012-4.849-.07-3.26-.149-4.771-1.699-4.919-4.92-.058-1.265-.07-1.644-.07-4.849 0-3.204.013-3.583.07-4.849.149-3.227 1.664-4.771 4.919-4.919 1.266-.057 1.645-.069 4.849-.069zm0-2.163c-3.259 0-3.667.014-4.947.072-4.358.2-6.78 2.618-6.98 6.98-.059 1.281-.073 1.689-.073 4.948 0 3.259.014 3.668.072 4.948.2 4.358 2.618 6.78 6.98 6.98 1.281.058 1.689.072 4.948.072 3.259 0 3.668-.014 4.948-.072 4.354-.2 6.782-2.618 6.979-6.98.059-1.28.073-1.689.073-4.948 0-3.259-.014-3.667-.072-4.947-.196-4.354-2.617-6.78-6.979-6.98-1.281-.059-1.69-.073-4.949-.073zm0 5.838c-3.403 0-6.162 2.759-6.162 6.162s2.759 6.163 6.162 6.163 6.162-2.759 6.162-6.163c0-3.403-2.759-6.162-6.162-6.162zm0 10.162c-2.209 0-4-1.79-4-4 0-2.209 1.791-4 4-4s4 1.791 4 4c0 2.21-1.791 4-4 4zm6.406-11.845c-.796 0-1.441.645-1.441 1.44s.645 1.44 1.441 1.44c.795 0 1.439-.645 1.439-1.44s-.644-1.44-1.439-1.44z"/></svg>
             </a>
             <a href="#" class="text-gray-400 hover:text-brand-gold transition-colors">
                <span class="sr-only">Pinterest</span>
                <svg class="h-6 w-6" fill="currentColor" viewBox="0 0 24 24"><path d="M12 0c-6.627 0-12 5.372-12 12 0 5.084 3.163 9.426 7.627 11.174-.105-.949-.199-2.403.041-3.439.219-.937 1.406-5.965 1.406-5.965s-.359-.719-.359-1.782c0-1.668.967-2.914 2.171-2.914 1.023 0 1.518.769 1.518 1.69 0 1.029-.655 2.568-.994 3.995-.283 1.194.599 2.169 1.777 2.169 2.133 0 3.772-2.249 3.772-5.495 0-2.873-2.064-4.882-5.012-4.882-3.414 0-5.418 2.561-5.418 5.207 0 1.031.397 2.138.893 2.738.098.119.112.224.083.345-.091.377-.293 1.189-.333 1.353-.053.213-.173.258-.399.153-1.487-.692-2.417-2.864-2.417-4.61 0-3.752 2.727-7.202 7.863-7.202 4.127 0 7.335 2.941 7.335 6.872 0 4.102-2.586 7.402-6.174 7.402-1.206 0-2.34-.626-2.729-1.37l-.744 2.834c-.269 1.026-1.002 2.309-1.493 3.111 1.124.347 2.316.537 3.551.537 6.627 0 12-5.373 12-12 0-6.628-5.373-12-12-12z"/></svg>
             </a>
          </div>
        </div>

        <!-- Links Section 1 -->
        <div>
          <h3 class="text-xs font-bold uppercase tracking-widest text-brand-gold mb-6">Collections</h3>
          <ul class="space-y-4 text-sm text-gray-400">
            <li><router-link to="/categories" class="hover:text-white transition-colors">Face</router-link></li>
            <li><router-link to="/categories" class="hover:text-white transition-colors">Eyes</router-link></li>
            <li><router-link to="/categories" class="hover:text-white transition-colors">Lips</router-link></li>
            <li><router-link to="/categories" class="hover:text-white transition-colors">Skincare</router-link></li>
          </ul>
        </div>

        <!-- Links Section 2 -->
        <div>
          <h3 class="text-xs font-bold uppercase tracking-widest text-brand-gold mb-6">Company</h3>
          <ul class="space-y-4 text-sm text-gray-400">
            <li><router-link to="/blog" class="hover:text-white transition-colors">Our Blog</router-link></li>
            <li><a href="#" class="hover:text-white transition-colors">Philosophy</a></li>
            <li><a href="#" class="hover:text-white transition-colors">Ingredients</a></li>
            <li><a href="#" class="hover:text-white transition-colors">Sustainability</a></li>
          </ul>
        </div>

        <!-- Newsletter -->
        <div>
          <h3 class="text-xs font-bold uppercase tracking-widest text-brand-gold mb-6">Join the Glow Circle</h3>
          <p class="text-sm text-gray-400 mb-6">Subscribe for exclusive offers and clean beauty tips.</p>
          <form class="flex flex-col space-y-3">
             <input type="email" placeholder="Email address" class="bg-gray-800 border-none px-4 py-3 text-sm focus:ring-1 focus:ring-brand-gold text-white outline-none">
             <button class="bg-brand-gold text-brand-dark px-4 py-3 text-xs font-bold uppercase tracking-widest hover:bg-yellow-500 transition-colors">Subscribe</button>
          </form>
        </div>
      </div>

      <div class="mt-16 pt-8 border-t border-gray-800 flex flex-col md:flex-row justify-between items-center text-xs text-gray-500 uppercase tracking-widest">
        <p>&copy; 2026 Glow & Grace. All rights reserved.</p>
        <div class="flex space-x-6 mt-4 md:mt-0">
          <a href="#" class="hover:text-white transition-colors">Privacy Policy</a>
          <a href="#" class="hover:text-white transition-colors">Terms of Service</a>
          <a href="#" class="hover:text-white transition-colors">Shipping & Returns</a>
        </div>
      </div>
    </div>
  </footer>
</template>

<script setup>
</script>

<style scoped>
footer {
  border-top: 1px solid rgba(212, 175, 55, 0.1); /* Subtle gold border at top */
}
</style>
