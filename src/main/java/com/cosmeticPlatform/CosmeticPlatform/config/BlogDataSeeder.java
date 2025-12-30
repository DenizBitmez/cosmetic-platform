package com.cosmeticPlatform.CosmeticPlatform.config;

import com.cosmeticPlatform.CosmeticPlatform.model.BlogPost;
import com.cosmeticPlatform.CosmeticPlatform.repository.BlogPostRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.util.Arrays;

@Configuration
public class BlogDataSeeder {

        @Bean
        @Order(1)
        CommandLineRunner initBlogData(BlogPostRepository repository) {
                return args -> {
                        // If we have fewer than 3 approved posts, reset and re-seed
                        if (repository.count() < 3) {
                                repository.deleteAll();
                                BlogPost post1 = new BlogPost();
                                post1.setTitle("The Ultimate Guide to Vitamin C Serums");
                                post1.setContent(
                                                "Vitamin C is often heralded as a magic bullet for skincare, and for good reason. As a powerful antioxidant, it neutralizes free radicals caused by UV exposure and pollution, which can lead to premature aging. But not all Vitamin C serums are created equal.\n\n"
                                                                +
                                                                "When shopping for a serum, the most important thing to look for is L-ascorbic acid. This is the most studied and effective form of Vitamin C. However, it can be unstable and irritating for some. If you have sensitive skin, consider derivatives like Sodium Ascorbyl Phosphate or Magnesium Ascorbyl Phosphate, which are gentler though perhaps slightly less potent.\n\n"
                                                                +
                                                                "Consistency is key. Apply your serum in the morning after cleansing and before moisturizing. This provides an extra layer of protection against the environmental stressors you'll face throughout the day. Remember, Vitamin C and Sunscreen are the ultimate power couple!");
                                post1.setAuthorName("Dr. Sarah Glow");
                                post1.setAuthorId(1);
                                post1.setExpertStatus(true);
                                post1.setApproved(true);
                                post1.setImageUrl(
                                                "https://images.unsplash.com/photo-1620916566398-39f1143ab7be?q=80&w=1974&auto=format&fit=crop");

                                BlogPost post2 = new BlogPost();
                                post2.setTitle("Hydration vs. Moisture: What's the Difference?");
                                post2.setContent(
                                                "It's a common misconception in the beauty world that hydration and moisture are the same thing. In reality, they address two very different skin needs, and understanding the difference is the first step to a healthy skin barrier.\n\n"
                                                                +
                                                                "Hydration refers to the water content within the cells. Ingredients like Hyaluronic Acid and Glycerin are humectants, meaning they pull water into the skin. If your skin feels tight and looks dull, it's likely dehydrated and needs water.\n\n"
                                                                +
                                                                "Moisture, on the other hand, is about the oil content and the skin's ability to retain that water. Emollients and occlusives, such as ceramides, plant oils, and even petrolatum, create a seal to prevent water loss (known as Trans-Epidermal Water Loss or TEWL). If your skin is flaky and rough, it's dry and needs oil.\n\n"
                                                                +
                                                                "A perfect routine balances both. Use a hydrating serum on damp skin, followed by a nourishing moisturizer to lock all that goodness in.");
                                post2.setAuthorName("Expert Emma");
                                post2.setAuthorId(2);
                                post2.setExpertStatus(true);
                                post2.setApproved(true);
                                post2.setImageUrl(
                                                "https://images.unsplash.com/photo-1598440947619-2c35fc9aa908?q=80&w=1935&auto=format&fit=crop");

                                BlogPost post3 = new BlogPost();
                                post3.setTitle("Night Routine for Glowing Skin");
                                post3.setContent(
                                                "While you sleep, your skin goes into repair mode, making your evening routine arguably the most important part of your day. It’s the time to undo the damage from environmental stressors and provide your skin with the nutrients it needs to regenerate.\n\n"
                                                                +
                                                                "Step 1: Double Cleanse. Use an oil-based cleanser to break down makeup and SPF, followed by a gentle water-based cleanser to actually clean the skin. This ensures your pores are clear and ready to absorb the next steps.\n\n"
                                                                +
                                                                "Step 2: Targeted Treatment. This is when you application of actives like Retinol, Peptides, or AHA/BHAs. Since these can increase sun sensitivity, the night is the perfect time for them to work their magic.\n\n"
                                                                +
                                                                "Step 3: Seal the Deal. Finish with a richer moisturizer or a sleeping mask. Look for ingredients like Niacinamide to soothe and Ceramides to strengthen the skin barrier while you rest. Wake up to skin that's refreshed, plump, and ready to glow!");
                                post3.setAuthorName("Beauty Guru Ben");
                                post3.setAuthorId(3);
                                post3.setExpertStatus(true);
                                post3.setApproved(true);
                                post3.setImageUrl(
                                                "https://images.unsplash.com/photo-1552046122-03184de85e08?q=80&w=1974&auto=format&fit=crop");

                                repository.saveAll(Arrays.asList(post1, post2, post3));
                                System.out.println("Seeded 3 expert blog posts.");
                        }
                };
        }
}
