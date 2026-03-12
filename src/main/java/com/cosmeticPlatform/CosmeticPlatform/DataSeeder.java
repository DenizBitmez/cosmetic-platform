package com.cosmeticPlatform.CosmeticPlatform;

import com.cosmeticPlatform.CosmeticPlatform.model.Ingredient;
import com.cosmeticPlatform.CosmeticPlatform.model.Product;
import com.cosmeticPlatform.CosmeticPlatform.repository.IngredientRepository;
import com.cosmeticPlatform.CosmeticPlatform.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class DataSeeder implements CommandLineRunner {

        private final ProductRepository productRepository;
        private final IngredientRepository ingredientRepository;
        private final com.cosmeticPlatform.CosmeticPlatform.repository.UserRepository userRepository;
        private final CacheManager cacheManager;

        public DataSeeder(ProductRepository productRepository, 
                          IngredientRepository ingredientRepository, 
                          com.cosmeticPlatform.CosmeticPlatform.repository.UserRepository userRepository,
                          CacheManager cacheManager) {
                this.productRepository = productRepository;
                this.ingredientRepository = ingredientRepository;
                this.userRepository = userRepository;
                this.cacheManager = cacheManager;
        }

        private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(DataSeeder.class);

        @Override
        public void run(String... args) throws Exception {
                log.info("Starting Data Seeding process...");
                
                // Seed demo user for cart functionality
                if (userRepository.count() == 0) {
                        log.info("User table empty, seeding demo user...");
                        com.cosmeticPlatform.CosmeticPlatform.model.User demoUser = new com.cosmeticPlatform.CosmeticPlatform.model.User();
                        demoUser.setUsername("demo");
                        demoUser.setEmail("demo@example.com");
                        demoUser.setPassword("password"); // In real app, this should be encoded
                        demoUser.setUserType(com.cosmeticPlatform.CosmeticPlatform.model.UserType.CLIENT);
                        userRepository.save(demoUser);
                        log.info("Demo user seeded.");
                }

                log.info("Current ingredient count: {}", ingredientRepository.count());
                
                seedIngredients();
                log.info("Ingredient seeding finished. New count: {}", ingredientRepository.count());

                if (productRepository.count() == 0) {
                        log.info("Product table empty, seeding products...");
                        seedProducts();
                } else {
                        log.info("Product table not empty, checking for missing demo products...");
                        seedDemoProductsIfMissing();
                }

                // Evict caches after seeding to ensure new data is visible
                try {
                        log.info("Attempting to clear Redis caches...");
                        if (cacheManager.getCache("ingredients") != null) {
                                cacheManager.getCache("ingredients").clear();
                                log.info("Ingredients cache cleared.");
                        }
                        if (cacheManager.getCache("products") != null) {
                                cacheManager.getCache("products").clear();
                                log.info("Products cache cleared.");
                        }
                } catch (Exception e) {
                        log.error("Failed to clear cache during seeding (Redis may be unreachable): {}", e.getMessage());
                }
                log.info("Data Seeding Completed!");
        }

        private void seedIngredients() {
                log.info("Seeding ingredients dictionary...");
                // A
                createIngredient("Alpha-Arbutin", "Skin brightener used to reduce hyperpigmentation.",
                                "Skin Brightening", 2, 0, "Reduces dark spots, evens skin tone.", "safe");
                createIngredient("Allantoin", "Soothing ingredient that promotes healing.", "Soothing", 1, 0,
                                "Soothes irritation, promotes healing.", "safe");
                createIngredient("Alcohol Denat", "Can dry out skin and damage the barrier.", "Solvent, Astringent", 5,
                                0, "Quick drying, antibacterial.", "warning");
                createIngredient("Ascorbic Acid", "Potent antioxidant, brightens skin and boosts collagen.",
                                "Antioxidant", 1, 0, "Brightens skin, boosts collagen, fights free radicals.", "safe");

                // B
                createIngredient("Benzoyl Peroxide", "Effective acne treatment that kills bacteria.", "Anti-acne", 4, 0,
                                "Kills acne bacteria, unclogs pores.", "warning");
                createIngredient("Beta-Glucan", "Soothing agent and antioxidant.", "Soothing", 1, 0,
                                "Soothes redness, hydrates, anti-aging.", "safe");
                createIngredient("Beeswax", "Natural wax produced by honey bees.", "Emollient", 1, 2,
                                "Protects skin, locks in moisture.", "safe", false);

                // C
                createIngredient("Caffeine", "Constricts blood vessels, reducing puffiness.", "Antioxidant", 2, 0,
                                "Reduces puffiness, antioxidant protection.", "safe");
                createIngredient("Centella Asiatica", "Soothes sensitive skin and promotes wound healing.", "Soothing",
                                1, 0, "Calms inflammation, stimulates collagen.", "safe");
                createIngredient("Ceramide NP", "Repairs the skin barrier and prevents moisture loss.",
                                "Skin Conditioning", 1, 0, "Restores barrier, hydrates.", "safe");

                // D
                createIngredient("Dimethicone", "Silicone that forms a protective barrier.", "Emollient", 3, 1,
                                "Smooths texture, locks in moisture.", "safe");

                // E
                createIngredient("Ethylhexylglycerin", "Skin conditioning agent and preservative booster.",
                                "Preservative", 2, 0, "Skin conditioning, deodorizing.", "safe");

                // F
                createIngredient("Ferulic Acid", "Antioxidant that boosts effectiveness of Vitamins C and E.",
                                "Antioxidant", 2, 0, "Stabilizes Vitamin C, fights free radicals.", "safe");

                // G
                createIngredient("Glycerin", "Humectant that draws moisture into the skin.", "Humectant", 1, 0,
                                "Hydrates, strengthens barrier.", "safe");
                createIngredient("Glycolic Acid", "AHA - Renews and smooths the skin surface.", "Exfoliant", 3, 0,
                                "Exfoliates, brightens, smooths texture.", "safe");

                // H
                createIngredient("Hyaluronic Acid", "Intensely hydrates and plumps the skin.", "Humectant", 1, 0,
                                "Deep hydration, plumping.", "safe");

                // I
                createIngredient("Idebenone", "Potent antioxidant.", "Antioxidant", 2, 0,
                                "Prevents environmental damage, reduces fine lines.", "safe");

                // J
                createIngredient("Jojoba Oil", "Natural oil effectively mimics skin sebum.", "Emollient", 1, 2,
                                "Moisturizes, balances oil production.", "safe");

                // K
                createIngredient("Kaolin", "Clay that absorbs excess oil.", "Absorbent", 1, 0,
                                "Absorbs oil, detoxifies pores.", "safe");

                // L
                createIngredient("Lactic Acid", "Gentle AHA exfoliant.", "Exfoliant", 2, 0,
                                "Gentle exfoliation, hydration.", "safe");
                createIngredient("Lanolin", "Derived from sheep's wool. Deeply moisturizes.", "Emollient", 1, 4,
                                "Intense hydration, barrier repair.", "safe", false);

                // M
                createIngredient("Mandelic Acid", "Gentle AHA suitable for sensitive skin.", "Exfoliant", 2, 0,
                                "Exfoliates sensitive skin, anti-bacterial.", "safe");

                // N
                createIngredient("Niacinamide", "Strengthens skin barrier and tightens pores.", "Skin Conditioning", 1,
                                0, "Regulates oil, minimizes pores, brightens.", "safe");

                // O
                createIngredient("Oxybenzone", "Chemical sunscreen filter.", "Sunscreen", 8, 0, "UV protection.",
                                "danger");

                // P
                createIngredient("Panthenol", "Pro-Vitamin B5, soothes and hydrates.", "Soothing", 1, 0,
                                "Soothes, hydrates, repairs barrier.", "safe");
                createIngredient("Paraben", "Controversial preservative. May have hormone-disrupting effects.",
                                "Preservative", 7, 0, "Preserves product shelf life.", "warning");
                createIngredient("Peptides", "Building blocks of proteins like collagen.", "Anti-aging", 1, 0,
                                "Stimulates collagen, firms skin.", "safe");
                createIngredient("Potassium Sorbate", "Potassium salt of sorbic acid used as a preservative.",
                                "Preservative", 2, 0, "Preserves product safety.", "safe");

                // Q
                createIngredient("Quercetin", "Plant pigment with antioxidant properties.", "Antioxidant", 1, 0,
                                "Soothing, antioxidant.", "safe");

                // R
                createIngredient("Retinol", "Anti-aging gold standard. Accelerates cell turnover.", "Anti-aging", 3, 0,
                                "Reduces wrinkles, treats acne, renews skin.", "safe");
                createIngredient("Resveratrol", "Antioxidant found in grapes.", "Antioxidant", 1, 0,
                                "Protects against UV damage, anti-aging.", "safe");

                // S
                createIngredient("Salicylic Acid", "BHA - Deeply cleanses pores, anti-acne.", "Exfoliant", 3, 0,
                                "Unclogs pores, treats blackheads and acne.", "safe");
                createIngredient("Shea Butter", "Rich moisturizer.", "Emollient", 1, 0, "Deeply moisturizes, soothes.",
                                "safe");
                createIngredient("Sodium Lauryl Sulfate (SLS)", "Strong harsh surfactant.", "Surfactant", 5, 3,
                                "Cleanses, creates foam (can be drying).", "warning");
                createIngredient("Squalane", "Hydrates and softens skin, does not clog pores.", "Emollient", 1, 1,
                                "Moisturizes, compatible with all skin types.", "safe");

                // T
                createIngredient("Titanium Dioxide", "Physical sunscreen filter.", "Sunscreen", 2, 0,
                                "Broad spectrum UV protection.", "safe");
                createIngredient("Tocopherol", "Vitamin E, strong antioxidant.", "Antioxidant", 1, 2,
                                "Moisturizes, protects against free radicals.", "safe");
                createIngredient("Tea Tree Oil", "Natural antibacterial oil.", "Anti-acne", 3, 0,
                                "Treats acne, antibacterial.", "safe");

                // U
                createIngredient("Urea", "Humectant and exfoliant.", "Humectant", 2, 0, "Hydrates, gently exfoliates.",
                                "safe");

                // V
                createIngredient("Vitamin C", "Potent antioxidant, brightens the skin.", "Antioxidant", 1, 0,
                                "Brightens, boosts collagen.", "safe");

                // W
                createIngredient("Witch Hazel", "Natural astringent.", "Astringent", 2, 0,
                                "Tightens pores, controls oil.", "safe");

                // X
                createIngredient("Xanthan Gum", "Texture enhancer and thickener.", "Texture Enhancer", 1, 0,
                                "Improves texture.", "safe");

                // Y
                createIngredient("Ylang Ylang Oil", "Fragrant essential oil.", "Fragrance", 4, 0, "Fragrance.",
                                "warning");

                // Z
                createIngredient("Zinc Oxide", "Physical sunscreen filter and skin protectant.", "Sunscreen", 1, 1,
                                "UV protection, soothing.", "safe");
        }

        private void seedProducts() {
                List<Ingredient> ingredients = ingredientRepository.findAll();

                Ingredient retinol = ingredients.stream().filter(i -> i.getName().equals("Retinol")).findFirst()
                                .orElse(null);
                Ingredient vitC = ingredients.stream().filter(i -> i.getName().equals("Ascorbic Acid")).findFirst()
                                .orElse(null);
                Ingredient alcohol = ingredients.stream().filter(i -> i.getName().equals("Alcohol Denat"))
                                .findFirst()
                                .orElse(null);
                Ingredient paraben = ingredients.stream().filter(i -> i.getName().equals("Paraben")).findFirst()
                                .orElse(null);
                Ingredient sa = ingredients.stream().filter(i -> i.getName().equals("Salicylic Acid")).findFirst()
                                .orElse(null);
                Ingredient ha = ingredients.stream().filter(i -> i.getName().equals("Hyaluronic Acid")).findFirst()
                                .orElse(null);
                Ingredient nia = ingredients.stream().filter(i -> i.getName().equals("Niacinamide")).findFirst()
                                .orElse(null);
                Ingredient cer = ingredients.stream().filter(i -> i.getName().equals("Ceramide NP")).findFirst()
                                .orElse(null);
                Ingredient cen = ingredients.stream().filter(i -> i.getName().equals("Centella Asiatica"))
                                .findFirst()
                                .orElse(null);
                Ingredient beeswax = ingredients.stream().filter(i -> i.getName().equals("Beeswax"))
                                .findFirst()
                                .orElse(null);
                Ingredient lanolin = ingredients.stream().filter(i -> i.getName().equals("Lanolin"))
                                .findFirst()
                                .orElse(null);

                // --- DERMOKOZMETIK / SERUMS ---
                saveProductWithCategory("Retinol Rejuvenating Serum", "dermokozmetik", "serum", 59.99, 100, 6,
                                "https://images.unsplash.com/photo-1620916566398-39f1143ab7be?auto=format&fit=crop&q=80&w=1000",
                                "Powerful retinol serum for skin renewal and anti-aging.",
                                retinol != null ? Arrays.asList(retinol) : null);

                saveProductWithCategory("Vitamin C Glow Booster", "dermokozmetik", "serum", 45.00, 150, 3,
                                "https://images.unsplash.com/photo-1594125350485-255060855ce0?auto=format&fit=crop&q=80&w=1000",
                                "High concentration of Vitamin C for radiant and even skin tone.",
                                vitC != null ? Arrays.asList(vitC) : null);

                saveProductWithCategory("Niacinamide 10% + Zinc 1%", "dermokozmetik", "serum", 19.99, 300, 12,
                                "https://images.unsplash.com/photo-1620916566398-39f1143ab7be?auto=format&fit=crop&q=80&w=1000",
                                "Blemish and oil control serum with high-strength vitamin B3.",
                                nia != null ? Arrays.asList(nia) : null);

                saveProductWithCategory("Hyaluronic Acid Hydrating Serum", "dermokozmetik", "serum", 34.00, 80, 6,
                                "https://images.unsplash.com/photo-1620917670397-dc71bce6d1f1?auto=format&fit=crop&q=80&w=1000",
                                "Ultra-hydrating serum with multi-molecular weight Hyaluronic Acid.",
                                ha != null ? Arrays.asList(ha) : null);

                // --- CLEANSERS ---
                saveProductWithCategory("Salicylic Acid Blemish Cleanser", "dermokozmetik", "cleanser", 22.50, 120, 12,
                                "https://images.unsplash.com/photo-1612817288484-6f916006741a?auto=format&fit=crop&q=80&w=1000",
                                "Deep cleansing gel with 2% Salicylic Acid for oily and acne-prone skin.",
                                sa != null ? Arrays.asList(sa) : null);

                saveProductWithCategory("Ceramide Foaming Cleanser", "dermokozmetik", "cleanser", 19.00, 300, 12,
                                "https://images.unsplash.com/photo-1556228578-0d85b1a4d571?auto=format&fit=crop&q=80&w=1000",
                                "Hydrating cleanser that respects the skin's natural barrier.",
                                cer != null ? Arrays.asList(cer) : null);

                // --- CREAMS ---
                saveProductWithCategory("Ceramide Barrier Cream", "dermokozmetik", "cream", 38.00, 100, 12,
                                "https://images.unsplash.com/photo-1570194065650-d99fb4b8ccb0?auto=format&fit=crop&q=80&w=1000",
                                "Deeply hydrating moisturizer designed for barrier repair and protection.",
                                (cer != null && lanolin != null && beeswax != null)
                                                ? Arrays.asList(cer, lanolin, beeswax)
                                                : (cer != null ? Arrays.asList(cer) : null));

                saveProductWithCategory("Centella Soothing Gel", "dermokozmetik", "cream", 26.00, 120, 12,
                                "https://images.unsplash.com/photo-1556229162-5c63ed9c4ffb?auto=format&fit=crop&q=80&w=1000",
                                "Lightweight calming gel for irritated, red, or sensitive skin.",
                                cen != null ? Arrays.asList(cen) : null);

                saveProductWithCategory("Anti-Aging Retinol Night Cream", "dermokozmetik", "cream", 48.00, 60, 6,
                                "https://images.unsplash.com/photo-1620917670397-dc71bce6d1f1?auto=format&fit=crop&q=80&w=1000",
                                "Targets fine lines and rejuvenates skin while you sleep.",
                                retinol != null ? Arrays.asList(retinol) : null);

                // Fallback / Other products
                saveProductIfMissing("Basic Face Wash", "cleanser", 15.99, 200, 12,
                                "https://images.unsplash.com/photo-1556228578-0d85b1a4d571?auto=format&fit=crop&q=80&w=1000",
                                "Simple daily cleanser for all skin types.",
                                (alcohol != null && paraben != null) ? Arrays.asList(alcohol, paraben) : null);

                saveProductIfMissing("Aloe Vera Soothing Gel", "cream", 14.00, 500, 24,
                                "https://images.unsplash.com/photo-1570194065650-d99fb4b8ccb0?auto=format&fit=crop&q=80&w=1000",
                                "Pure aloe relief for sun-exposed, dry, or itchy skin.", null);

                log.info("Dermokozmetik Product Seeding Finished.");
        }

        private void saveProductWithCategory(String name, String category, String type, double price, int stock, int pao,
                        String image, String desc, List<Ingredient> ings) {
                // Use type if needed for sub-categorization or just category
                saveProductIfMissing(name, category, price, stock, pao, image, desc, ings);
        }

        private void saveProductIfMissing(String name, String category, double price, int stock, int pao, String image,
                        String desc, List<Ingredient> ings) {
                List<Product> existing = productRepository.findByNameContainingIgnoreCase(name);
                if (existing.isEmpty()) {
                        Product p = new Product();
                        p.setName(name);
                        p.setCategory(category);
                        p.setPrice(price);
                        p.setStock(stock);
                        p.setPaoMonths(pao);
                        p.setImage(image);
                        p.setDescription(desc);
                        p.setIsCrueltyFree(!name.contains("Basic"));
                        setSustainabilityMetrics(p, category);
                        if (ings != null) p.setIngredients(ings);
                        productRepository.save(p);
                        log.info("Created new product: {} in category: {}", name, category);
                } else {
                        Product p = existing.get(0);
                        boolean updated = false;

                        // FORCE update category if it's different (to support re-categorization to 'dermokozmetik')
                        if (!category.equals(p.getCategory())) {
                                p.setCategory(category);
                                updated = true;
                        }

                        if (p.getIsCrueltyFree() == null || p.getEcoPackagingScore() == null) {
                                p.setIsCrueltyFree(!name.contains("Basic"));
                                setSustainabilityMetrics(p, category);
                                updated = true;
                        }

                        if ((p.getIngredients() == null || p.getIngredients().isEmpty()) && ings != null) {
                                p.setIngredients(ings);
                                updated = true;
                        }

                        if (updated) {
                                productRepository.save(p);
                                log.info("Updated product: {} with new category/metrics", name);
                        }
                }
        }

        private void setSustainabilityMetrics(Product p, String category) {
                if (category.contains("serum") || category.equals("dermokozmetik")) {
                        p.setEcoPackagingScore(9); 
                        p.setCarbonFootprintRating("A");
                } else if (category.contains("cream")) {
                        p.setEcoPackagingScore(6);
                        p.setCarbonFootprintRating("C");
                } else {
                        p.setEcoPackagingScore(5);
                        p.setCarbonFootprintRating("C");
                }
        }

        private void seedDemoProductsIfMissing() {
                List<Product> products = productRepository.findByNameContainingIgnoreCase("Aloe Vera Soothing Gel");
                if (products.isEmpty()) {
                        System.out.println("New heavy skincare products missing, seeding...");
                        seedProducts();
                }
        }

        private void createIngredient(String name, String desc, String func, int safety, int comedogenic,
                        String benefits, String alert, boolean isVegan) {
                Ingredient i = ingredientRepository.findByName(name).orElse(new Ingredient());
                i.setName(name);
                i.setDescription(desc);
                i.setFunctionality(func);
                i.setSafetyRating(safety);
                i.setComedogenicRating(comedogenic);
                i.setBenefits(benefits);
                i.setAlertType(alert);
                i.setIsVegan(isVegan);
                ingredientRepository.save(i);
                log.trace("Saved ingredient: {}", name);
        }

        private void createIngredient(String name, String desc, String func, int safety, int comedogenic,
                        String benefits, String alert) {
                createIngredient(name, desc, func, safety, comedogenic, benefits, alert, true);
        }
}
