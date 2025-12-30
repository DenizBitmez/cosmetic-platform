package com.cosmeticPlatform.CosmeticPlatform.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.core.annotation.Order;

@Configuration
public class SchemaCleanupConfig {

    @Bean
    @Order(0) // Run BEFORE the data seeder
    public CommandLineRunner cleanupSchema(JdbcTemplate jdbcTemplate) {
        return args -> {
            try {
                System.out.println("Attempting to drop stray column 'approved' if it exists...");
                jdbcTemplate.execute("ALTER TABLE blog_posts DROP COLUMN approved");
                System.out.println("Successfully dropped column 'approved'.");
            } catch (Exception e) {
                System.out.println("Column 'approved' did not exist or could not be dropped: " + e.getMessage());
            }

            try {
                // Also handle the reverse if is_approved is the stray one (unlikely but safe)
                // jdbcTemplate.execute("ALTER TABLE blog_posts DROP COLUMN is_approved");
            } catch (Exception e) {
            }
        };
    }
}
