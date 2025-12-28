/** @type {import('tailwindcss').Config} */
export default {
    content: [
        "./index.html",
        "./src/**/*.{vue,js,ts,jsx,tsx}",
    ],
    theme: {
        extend: {
            colors: {
                'brand-gold': '#D4AF37',       // Metallic Gold
                'brand-rose': '#E0B0FF',       // Soft Mauve/Rose
                'brand-dark': '#1A1A1A',       // Rich Black
                'brand-cream': '#F9F7F2',      // Off-white cream
                'brand-accent': '#C5A089',     // Bronze/Earth tone
            },
            fontFamily: {
                'serif': ['"Playfair Display"', 'serif'],
                'sans': ['"Lato"', 'sans-serif'],
            }
        },
    },
    plugins: [],
}
