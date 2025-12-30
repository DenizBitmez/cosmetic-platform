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
        i1.setDescription(
                "Potasyum sorbat, kozmetikte koruyucu olarak kullanılan sorbik asidin potasyum tuzudur. Zayıf aktivitesi nedeniyle başka koruyucularla birlikte kullanılır.");
        i1.setScore("İyi");

        Ingredient i2 = new Ingredient();
        i2.setInciName("Sodium Benzoate");
        i2.setTurkishName("Sodyum Benzoat");
        i2.setDescription(
                "Sodyum benzoat, kozmetikte koruyucu olarak ve koku bileşenlerinin formüllerinde kullanılır. Özellikle mantarlara ve bakterilere karşı etkilidir.");
        i2.setScore("İyi");

        Ingredient i3 = new Ingredient();
        i3.setInciName("Sambucus Nigra Seed Oil");
        i3.setTurkishName("Kara Mürver Tohum Yağı");
        i3.setDescription(
                "Sambucus nigra bitkisinin tohumlarının sıkılmasıyla elde edilen yağdır. Karakteristik bir aroması vardır. Linoleik asit ve diğer asitleri içerir.");
        i3.setScore("Çok İyi");

        Ingredient i4 = new Ingredient();
        i4.setInciName("Ananas Sativus Fruit Extract");
        i4.setTurkishName("Ananas Ekstresi");
        i4.setDescription(
                "Ananas meyvesi olan Ananas sativus'un bir özüdür. Cildi yenilemek ve canlandırmak için kullanılır.");
        i4.setScore("Çok İyi");

        Ingredient i5 = new Ingredient();
        i5.setInciName("Sodium Fluoride");
        i5.setTurkishName("Sodyum Florür");
        i5.setDescription("Sodyum florür, ağız bakım ürünlerinde hijyen amacıyla kullanılan inorganik bir tuzdur.");
        i5.setScore("Orta");

        ingredientRepository.saveAll(Arrays.asList(i1, i2, i3, i4, i5));
    }

    private void seedProducts() {
        // Fetch ingredients to ensure we have references (assuming they were just
        // saved)
        List<Ingredient> ingredients = ingredientRepository.findAll();
        // Simple mapping for demo (in a real app, use IDs or find by name)
        Ingredient i1 = ingredients.stream().filter(i -> i.getInciName().equals("Potassium Sorbate")).findFirst()
                .orElse(null);
        Ingredient i2 = ingredients.stream().filter(i -> i.getInciName().equals("Sodium Benzoate")).findFirst()
                .orElse(null);
        Ingredient i3 = ingredients.stream().filter(i -> i.getInciName().equals("Sambucus Nigra Seed Oil")).findFirst()
                .orElse(null);
        Ingredient i4 = ingredients.stream().filter(i -> i.getInciName().equals("Ananas Sativus Fruit Extract"))
                .findFirst().orElse(null);
        Ingredient i5 = ingredients.stream().filter(i -> i.getInciName().equals("Sodium Fluoride")).findFirst()
                .orElse(null);

        Product p1 = new Product();
        p1.setName("Premium Natural Face Cream");
        p1.setCategory("foundation");
        p1.setPrice(49.99);
        p1.setStock(100);
        p1.setImage("https://images.unsplash.com/photo-1620916566398-39f1143ab7be?q=80&w=1000&auto=format&fit=crop");
        p1.setDescription("A luxurious face cream enriched with natural extracts.");
        if (i1 != null)
            p1.setIngredients(Arrays.asList(i1, i2, i3, i4));

        Product p2 = new Product();
        p2.setName("Herbal Toothpaste");
        p2.setCategory("other");
        p2.setPrice(12.50);
        p2.setStock(200);
        p2.setImage("https://images.unsplash.com/photo-1556228720-1957be979c29?q=80&w=1000&auto=format&fit=crop");
        p2.setDescription("Natural toothpaste for a bright smile.");
        if (i5 != null)
            p2.setIngredients(Arrays.asList(i5, i1));

        Product p3 = new Product();
        p3.setName("Velvet Lipstick Red");
        p3.setCategory("lipstick");
        p3.setPrice(29.99);
        p3.setStock(50);
        p3.setImage("https://images.unsplash.com/photo-1586495777744-4413f21062fa?q=80&w=1000&auto=format&fit=crop");
        p3.setDescription("Long-lasting matte lipstick in a stunning red shade.");
        if (i3 != null)
            p3.setIngredients(Arrays.asList(i3, i1));

        productRepository.saveAll(Arrays.asList(p1, p2, p3));
        System.out.println("Data Seeding Completed!");
    }

    private void seedDemoProductsIfMissing() {
        List<Product> products = productRepository.findByNameContainingIgnoreCase("Premium Natural Face Cream");
        if (products.isEmpty()) {
            System.out.println("Demo products missing, seeding...");
            seedProducts();
        }
    }
}
