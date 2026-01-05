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
        if (ingredientRepository.count() == 0) {
            seedIngredients();
        }

        if (productRepository.count() == 0) {
            seedProducts();
        } else {
            // Ensure demo products exist even if DB is not empty
            seedDemoProductsIfMissing();
        }
    }

    private void seedIngredients() {
        Ingredient i1 = new Ingredient();
        i1.setInciName("Potassium Sorbate");
        i1.setTurkishName("Potasyum Sorbat");
        i1.setDescription("Potasyum sorbat, kozmetikte koruyucu olarak kullanılan sorbik asidin potasyum tuzudur.");
        i1.setScore("İyi");

        Ingredient i2 = new Ingredient();
        i2.setInciName("Retinol");
        i2.setTurkishName("A Vitamini");
        i2.setDescription("Yaşlanma karşıtı altın standart. Hücre yenilenmesini hızlandırır.");
        i2.setScore("Çok İyi");

        Ingredient i3 = new Ingredient();
        i3.setInciName("Ascorbic Acid");
        i3.setTurkishName("C Vitamini");
        i3.setDescription("Güçlü antioksidan, cildi aydınlatır ve kolajen üretimini destekler.");
        i3.setScore("Çok İyi");

        Ingredient i4 = new Ingredient();
        i4.setInciName("Paraben");
        i4.setTurkishName("Paraben");
        i4.setDescription("Tartışmalı bir koruyucu. Hormon bozucu etkileri olabilir.");
        i4.setScore("Riskli");

        Ingredient i5 = new Ingredient();
        i5.setInciName("Alcohol Denat");
        i5.setTurkishName("Alkol");
        i5.setDescription("Cildi kurutabilir ve bariyerine zarar verebilir.");
        i5.setScore("Orta");

        Ingredient i6 = new Ingredient();
        i6.setInciName("Niacinamide");
        i6.setTurkishName("B3 Vitamini");
        i6.setDescription("Cilt bariyerini güçlendirir, gözenekleri sıkılaştırır.");
        i6.setScore("Çok İyi");

        Ingredient i7 = new Ingredient();
        i7.setInciName("Salicylic Acid");
        i7.setTurkishName("Salisilik Asit");
        i7.setDescription("BHA - Gözenekleri derinlemesine temizler, akne karşıtıdır.");
        i7.setScore("Çok İyi");

        Ingredient i8 = new Ingredient();
        i8.setInciName("Hyaluronic Acid");
        i8.setTurkishName("Hyaluronik Asit");
        i8.setDescription("Cildi yoğun nemlendirir, dolgunlaştırır.");
        i8.setScore("Çok İyi");

        Ingredient i9 = new Ingredient();
        i9.setInciName("Glycolic Acid");
        i9.setTurkishName("Glikolik Asit");
        i9.setDescription("AHA - Cilt yüzeyini yeniler, pürüzsüzleştirir.");
        i9.setScore("Çok İyi");

        Ingredient i10 = new Ingredient();
        i10.setInciName("Squalane");
        i10.setTurkishName("Skualen");
        i10.setDescription("Cildi nemlendirir ve yumuşatır, gözenekleri tıkamaz.");
        i10.setScore("Çok İyi");

        Ingredient i11 = new Ingredient();
        i11.setInciName("Ceramide NP");
        i11.setTurkishName("Seramid");
        i11.setDescription("Cilt bariyerini onarır ve nem kaybını önler.");
        i11.setScore("Çok İyi");

        Ingredient i12 = new Ingredient();
        i12.setInciName("Centella Asiatica");
        i12.setTurkishName("Centella");
        i12.setDescription("Hassas cildi yatıştırır, yara iyileşmesini destekler.");
        i12.setScore("Çok İyi");

        ingredientRepository.saveAll(Arrays.asList(i1, i2, i3, i4, i5, i6, i7, i8, i9, i10, i11, i12));
    }

    private void seedProducts() {
        List<Ingredient> ingredients = ingredientRepository.findAll();

        Ingredient retinol = ingredients.stream().filter(i -> i.getInciName().equals("Retinol")).findFirst()
                .orElse(null);
        Ingredient vitC = ingredients.stream().filter(i -> i.getInciName().equals("Ascorbic Acid")).findFirst()
                .orElse(null);
        Ingredient alcohol = ingredients.stream().filter(i -> i.getInciName().equals("Alcohol Denat")).findFirst()
                .orElse(null);
        Ingredient paraben = ingredients.stream().filter(i -> i.getInciName().equals("Paraben")).findFirst()
                .orElse(null);
        Ingredient sa = ingredients.stream().filter(i -> i.getInciName().equals("Salicylic Acid")).findFirst()
                .orElse(null);
        Ingredient ha = ingredients.stream().filter(i -> i.getInciName().equals("Hyaluronic Acid")).findFirst()
                .orElse(null);
        Ingredient ga = ingredients.stream().filter(i -> i.getInciName().equals("Glycolic Acid")).findFirst()
                .orElse(null);
        Ingredient nia = ingredients.stream().filter(i -> i.getInciName().equals("Niacinamide")).findFirst()
                .orElse(null);
        Ingredient sq = ingredients.stream().filter(i -> i.getInciName().equals("Squalane")).findFirst().orElse(null);
        Ingredient cer = ingredients.stream().filter(i -> i.getInciName().equals("Ceramide NP")).findFirst()
                .orElse(null);
        Ingredient cen = ingredients.stream().filter(i -> i.getInciName().equals("Centella Asiatica")).findFirst()
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
                "Non-greasy daily moisturizer for all-day hydration.", sq != null ? Arrays.asList(sq) : null);

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
}
