<template>
  <div class="min-h-screen bg-white">
    <!-- Hero Section -->
    <div class="bg-brand-cream py-16">
        <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 text-center">
            <h1 class="text-4xl font-serif text-brand-dark mb-4">About Cosmetic Platform</h1>
            <p class="text-lg text-gray-600 max-w-3xl mx-auto">We are dedicated to bringing you the finest cosmetic products with transparency and community-driven insights.</p>
        </div>
    </div>

    <!-- Content -->
    <div class="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8 py-16 space-y-20">
        <section class="animate-fadeIn">
            <h2 class="text-3xl font-serif text-brand-dark mb-6">Our Story</h2>
            <p class="text-gray-600 leading-relaxed mb-4">
                Founded in 2023, Cosmetic Platform emerged from a simple observation: the beauty industry was filled with noise, but lacked a central hub for genuine, science-backed guidance. We started as a small community of skincare enthusiasts who were tired of "miracle cures" and wanted to understand what went into the products they used every day.
            </p>
            <p class="text-gray-600 leading-relaxed">
                Today, we've grown into a comprehensive ecosystem where data meets passion. Our platform bridge the gap between complex ingredient lists and the simple desire for healthy, glowing skin.
            </p>
        </section>

        <section class="bg-gray-50 -mx-4 sm:-mx-6 lg:-mx-8 px-4 sm:px-6 lg:px-8 py-16 border-y border-gray-100">
            <h2 class="text-3xl font-serif text-brand-dark mb-6">Our Mission</h2>
            <div class="prose prose-brand text-gray-600 max-w-none">
                <p class="text-xl font-light italic text-brand-dark mb-6">"Empowering every individual to make informed choices for their unique beauty journey."</p>
                <p>
                    We believe transparency is the highest form of luxury. That's why every product on our platform undergoes a rigorous analysis. We don't just show you the brand; we show you the science, the community's voice, and the true value of what you're buying.
                </p>
            </div>
        </section>

        <section>
            <h2 class="text-3xl font-serif text-brand-dark mb-8">The Clean Beauty Commitment</h2>
            <div class="grid grid-cols-1 md:grid-cols-3 gap-8">
                <div class="text-center group">
                    <div class="h-16 w-16 bg-brand-cream rounded-full flex items-center justify-center mx-auto mb-4 group-hover:bg-brand-gold transition-colors duration-300">
                        <span class="text-2xl text-brand-gold group-hover:text-white">🌿</span>
                    </div>
                    <h3 class="font-bold text-gray-900 mb-2">Sustainable Sourcing</h3>
                    <p class="text-xs text-gray-500 leading-relaxed">We prioritize brands that respect the planet as much as they respect your skin.</p>
                </div>
                <div class="text-center group">
                    <div class="h-16 w-16 bg-brand-cream rounded-full flex items-center justify-center mx-auto mb-4 group-hover:bg-brand-gold transition-colors duration-300">
                        <span class="text-2xl text-brand-gold group-hover:text-white">🔬</span>
                    </div>
                    <h3 class="font-bold text-gray-900 mb-2">Science First</h3>
                    <p class="text-xs text-gray-500 leading-relaxed">Every ingredient is cross-referenced with global dermatological databases.</p>
                </div>
                <div class="text-center group">
                    <div class="h-16 w-16 bg-brand-cream rounded-full flex items-center justify-center mx-auto mb-4 group-hover:bg-brand-gold transition-colors duration-300">
                        <span class="text-2xl text-brand-gold group-hover:text-white">🤝</span>
                    </div>
                    <h3 class="font-bold text-gray-900 mb-2">Total Transparency</h3>
                    <p class="text-xs text-gray-500 leading-relaxed">No hidden additives. No vague marketing labels. Just the honest truth.</p>
                </div>
            </div>
        </section>

        <section class="text-center py-12 border-t border-gray-100">
            <h2 class="text-3xl font-serif text-brand-dark mb-4">Join the Movement</h2>
            <p class="text-gray-600 mb-8 max-w-2xl mx-auto">
                Whether you're a skincare novice or a beauty veteran, there's a place for you here. Share your reviews, explore expert insights, and help us build a more transparent beauty world.
            </p>
            <div class="flex justify-center gap-4">
                <router-link to="/categories" class="bg-brand-dark text-white px-8 py-3 rounded-full text-xs font-bold uppercase tracking-widest hover:bg-black transition-colors shadow-lg">Explore Products</router-link>
                <router-link to="/community" class="border border-brand-dark text-brand-dark px-8 py-3 rounded-full text-xs font-bold uppercase tracking-widest hover:bg-gray-50 transition-colors">Join Community</router-link>
            </div>
        </section>
    </div>
  </div>
</template>

<script setup>
</script>
