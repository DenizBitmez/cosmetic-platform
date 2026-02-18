package com.cosmeticPlatform.CosmeticPlatform;

import com.cosmeticPlatform.CosmeticPlatform.model.Ingredient;
import com.cosmeticPlatform.CosmeticPlatform.model.Product;
import com.cosmeticPlatform.CosmeticPlatform.repository.IngredientRepository;
import com.cosmeticPlatform.CosmeticPlatform.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class DataSeeder implements CommandLineRunner {

        private final ProductRepository productRepository;
        private final IngredientRepository ingredientRepository;

        public DataSeeder(ProductRepository productRepository, IngredientRepository ingredientRepository) {
                this.productRepository = productRepository;
                this.ingredientRepository = ingredientRepository;
        }

        @Override
        public void run(String... args) throws Exception {
                seedIngredients();

                if (productRepository.count() == 0) {
                        seedProducts();
                } else {
                        // Ensure demo products exist even if DB is not empty
                        seedDemoProductsIfMissing();
                }
        }

        private void seedIngredients() {
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
                Ingredient ga = ingredients.stream().filter(i -> i.getName().equals("Glycolic Acid")).findFirst()
                                .orElse(null);
                Ingredient nia = ingredients.stream().filter(i -> i.getName().equals("Niacinamide")).findFirst()
                                .orElse(null);
                Ingredient sq = ingredients.stream().filter(i -> i.getName().equals("Squalane")).findFirst()
                                .orElse(null);
                Ingredient cer = ingredients.stream().filter(i -> i.getName().equals("Ceramide NP")).findFirst()
                                .orElse(null);
                Ingredient cen = ingredients.stream().filter(i -> i.getName().equals("Centella Asiatica"))
                                .findFirst()
                                .orElse(null);

                // --- SERUMS (NEW & IMPROVED) ---
                saveProductIfMissing("Retinol Rejuvenating Serum", "serum", 59.99, 100, 6,
                                "https://images.unsplash.com/photo-1620916566398-39f1143ab7be?auto=format&fit=crop&q=80&w=1000",
                                "Powerful retinol serum for skin renewal and anti-aging.",
                                retinol != null ? Arrays.asList(retinol) : null);

                saveProductIfMissing("Vitamin C Glow Booster", "serum", 45.00, 150, 3,
                                "https://images.unsplash.com/photo-1594125350485-255060855ce0?auto=format&fit=crop&q=80&w=1000",
                                "High concentration of Vitamin C for radiant and even skin tone.",
                                vitC != null ? Arrays.asList(vitC) : null);

                saveProductIfMissing("Niacinamide 10% + Zinc 1%", "serum", 19.99, 300, 12,
                                "https://images.unsplash.com/photo-1620916566398-39f1143ab7be?auto=format&fit=crop&q=80&w=1000",
                                "Blemish and oil control serum with high-strength vitamin B3.",
                                nia != null ? Arrays.asList(nia) : null);

                saveProductIfMissing("Multi-Peptide + HA Serum", "serum", 28.50, 150, 12,
                                "https://images.unsplash.com/photo-1620916566398-39f1143ab7be?auto=format&fit=crop&q=80&w=1000",
                                "Universal formula targeting signs of aging with peptides and hydration.",
                                ha != null ? Arrays.asList(ha) : null);

                saveProductIfMissing("Hyaluronic Acid Hydrating Serum", "serum", 34.00, 80, 6,
                                "https://images.unsplash.com/photo-1620917670397-dc71bce6d1f1?auto=format&fit=crop&q=80&w=1000",
                                "Ultra-hydrating serum with multi-molecular weight Hyaluronic Acid.",
                                ha != null ? Arrays.asList(ha) : null);

                // --- CLEANSERS ---
                saveProductIfMissing("Salicylic Acid Blemish Cleanser", "cleanser", 22.50, 120, 12,
                                "https://images.unsplash.com/photo-1612817288484-6f916006741a?auto=format&fit=crop&q=80&w=1000",
                                "Deep cleansing gel with 2% Salicylic Acid for oily and acne-prone skin.",
                                sa != null ? Arrays.asList(sa) : null);

                saveProductIfMissing("Squalane Cleanser", "cleanser", 24.50, 150, 6,
                                "https://images.unsplash.com/photo-1556228578-0d85b1a4d571?auto=format&fit=crop&q=80&w=1000",
                                "Gentle oil-like cleanser for effective makeup removal and skin softness.",
                                sq != null ? Arrays.asList(sq) : null);

                saveProductIfMissing("Ceramide Foaming Cleanser", "cleanser", 19.00, 300, 12,
                                "https://images.unsplash.com/photo-1556228578-0d85b1a4d571?auto=format&fit=crop&q=80&w=1000",
                                "Hydrating cleanser that respects the skin's natural barrier.",
                                cer != null ? Arrays.asList(cer) : null);

                saveProductIfMissing("Basic Face Wash", "cleanser", 15.99, 200, 12,
                                "https://images.unsplash.com/photo-1556228578-0d85b1a4d571?auto=format&fit=crop&q=80&w=1000",
                                "Simple daily cleanser for all skin types.",
                                (alcohol != null && paraben != null) ? Arrays.asList(alcohol, paraben) : null);

                // --- TONERS ---
                saveProductIfMissing("Glycolic Acid Exfoliating Toner", "toner", 28.00, 90, 12,
                                "https://images.unsplash.com/photo-1601049541289-9b1b7bbbfe19?auto=format&fit=crop&q=80&w=1000",
                                "AHA toner for a smoother, brighter complexion and gentle exfoliation.",
                                ga != null ? Arrays.asList(ga) : null);

                saveProductIfMissing("BHA Blackhead Power Liquid", "toner", 32.00, 80, 12,
                                "https://images.unsplash.com/photo-1612817288484-6f916006741a?auto=format&fit=crop&q=80&w=1000",
                                "Liquid exfoliator that deep cleans pores and fights blackheads.",
                                sa != null ? Arrays.asList(sa) : null);

                saveProductIfMissing("Rose Water Calming Toner", "toner", 18.00, 200, 12,
                                "https://images.unsplash.com/photo-1601049541289-9b1b7bbbfe19?auto=format&fit=crop&q=80&w=1000",
                                "Natural rose water toner to refresh and soothe sensitive skin.", null);

                // --- CREAMS ---
                saveProductIfMissing("Ceramide Barrier Cream", "cream", 38.00, 100, 12,
                                "https://images.unsplash.com/photo-1570194065650-d99fb4b8ccb0?auto=format&fit=crop&q=80&w=1000",
                                "Deeply hydrating moisturizer designed for barrier repair and protection.",
                                cer != null ? Arrays.asList(cer) : null);

                saveProductIfMissing("Centella Soothing Gel", "cream", 26.00, 120, 12,
                                "https://images.unsplash.com/photo-1556229162-5c63ed9c4ffb?auto=format&fit=crop&q=80&w=1000",
                                "Lightweight calming gel for irritated, red, or sensitive skin.",
                                cen != null ? Arrays.asList(cen) : null);

                saveProductIfMissing("Daily Moisturizing Lotion", "cream", 14.99, 500, 12,
                                "https://images.unsplash.com/photo-1570194065650-d99fb4b8ccb0?auto=format&fit=crop&q=80&w=1000",
                                "Non-greasy daily moisturizer for all-day hydration.",
                                sq != null ? Arrays.asList(sq) : null);

                saveProductIfMissing("Anti-Aging Retinol Night Cream", "cream", 48.00, 60, 6,
                                "https://images.unsplash.com/photo-1620917670397-dc71bce6d1f1?auto=format&fit=crop&q=80&w=1000",
                                "Targets fine lines and rejuvenates skin while you sleep.",
                                retinol != null ? Arrays.asList(retinol) : null);

                saveProductIfMissing("Aloe Vera Soothing Gel", "cream", 14.00, 500, 24,
                                "https://images.unsplash.com/photo-1570194065650-d99fb4b8ccb0?auto=format&fit=crop&q=80&w=1000",
                                "Pure aloe relief for sun-exposed, dry, or itchy skin.", null);

                System.out.println("Data Seeding Completed!");
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
                        if (ings != null)
                                p.setIngredients(ings);
                        productRepository.save(p);
                } else {
                        // If already exists but has no ingredients (due to older seeder run), update it
                        Product p = existing.get(0);
                        if ((p.getIngredients() == null || p.getIngredients().isEmpty()) && ings != null) {
                                p.setIngredients(ings);
                                productRepository.save(p);
                        }
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
                        String benefits, String alert) {
                Ingredient i = ingredientRepository.findByName(name).orElse(new Ingredient());
                i.setName(name);
                i.setDescription(desc);
                i.setFunctionality(func);
                i.setSafetyRating(safety);
                i.setComedogenicRating(comedogenic);
                i.setBenefits(benefits);
                i.setAlertType(alert);
                ingredientRepository.save(i);
        }
}
