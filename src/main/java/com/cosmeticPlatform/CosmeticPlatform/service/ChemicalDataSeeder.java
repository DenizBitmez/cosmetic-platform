package com.cosmeticPlatform.CosmeticPlatform.service;

import com.cosmeticPlatform.CosmeticPlatform.model.ChemicalRisk;
import com.cosmeticPlatform.CosmeticPlatform.model.Ingredient;
import com.cosmeticPlatform.CosmeticPlatform.repository.ChemicalRiskRepository;
import com.cosmeticPlatform.CosmeticPlatform.repository.IngredientRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
public class ChemicalDataSeeder implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(ChemicalDataSeeder.class);
    private final ChemicalRiskRepository repository;
    private final IngredientRepository ingredientRepository;

    public ChemicalDataSeeder(ChemicalRiskRepository repository, IngredientRepository ingredientRepository) {
        this.repository = repository;
        this.ingredientRepository = ingredientRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (repository.count() > 0) {
            logger.info("Chemical risk data already loaded. Skipping seeder.");
            return;
        }

        logger.info("Starting to import California Safe Cosmetics Program dataset...");
        try {
            ClassPathResource resource = new ClassPathResource("data/chemicals-in-cosmetics.csv");
            if (!resource.exists()) {
                logger.warn(
                        "Dataset not found at src/main/resources/data/chemicals-in-cosmetics.csv. Please download it from Kaggle.");
                return;
            }

            BufferedReader fileReader = new BufferedReader(
                    new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8));

            // Fixed deprecated CSVFormat methods
            CSVFormat csvFormat = CSVFormat.Builder.create(CSVFormat.DEFAULT)
                    .setHeader()
                    .setIgnoreHeaderCase(true)
                    .setTrim(true)
                    .build();

            CSVParser csvParser = new CSVParser(fileReader, csvFormat);

            List<ChemicalRisk> risks = new ArrayList<>();
            List<Ingredient> newIngredients = new ArrayList<>();

            int count = 0;

            for (CSVRecord record : csvParser) {
                String rawChemicalName = record.get("ChemicalName");
                if (rawChemicalName == null || rawChemicalName.trim().isEmpty()) {
                    continue;
                }

                String truncatedName = truncate(rawChemicalName, 500);

                ChemicalRisk risk = new ChemicalRisk();
                risk.setChemicalName(truncatedName);
                risk.setProductName(truncate(record.get("ProductName"), 500));
                risk.setBrandName(truncate(record.get("BrandName"), 255));
                risk.setCompanyName(truncate(record.get("CompanyName"), 255));

                risks.add(risk);

                // Add to standard ingredients table as well
                if (!ingredientRepository.existsByNameIgnoreCase(truncatedName)) {
                    Ingredient ing = new Ingredient();
                    ing.setName(truncatedName);
                    ing.setDescription("WARNING: Listed by California Safe Cosmetics Program (Kaggle).");
                    ing.setSafetyRating(9);
                    ing.setAlertType("danger");
                    ing.setFunctionality("Risk marked from Kaggle Dataset");
                    newIngredients.add(ing);
                }

                count++;

                // Batch save to prevent memory overflow
                if (count % 5000 == 0) {
                    repository.saveAll(risks);
                    if (!newIngredients.isEmpty()) {
                        ingredientRepository.saveAll(newIngredients);
                        newIngredients.clear();
                    }
                    risks.clear();
                    logger.info("Loaded {} records...", count);
                }
            }

            if (!risks.isEmpty()) {
                repository.saveAll(risks);
            }
            if (!newIngredients.isEmpty()) {
                ingredientRepository.saveAll(newIngredients);
            }

            logger.info("Successfully imported {} chemical risk records into the database.", count);
            csvParser.close();
            fileReader.close();

        } catch (Exception e) {
            logger.error("Failed to parse chemical risk CSV: ", e);
        }
    }

    private String truncate(String val, int maxLen) {
        if (val == null)
            return null;
        if (val.length() > maxLen)
            return val.substring(0, maxLen);
        return val;
    }
}
