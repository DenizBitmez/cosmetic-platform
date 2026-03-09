package com.cosmeticPlatform.CosmeticPlatform.repository;

import com.cosmeticPlatform.CosmeticPlatform.model.ChemicalRisk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChemicalRiskRepository extends JpaRepository<ChemicalRisk, Long> {
    List<ChemicalRisk> findByChemicalNameContainingIgnoreCase(String chemicalName);

    boolean existsByChemicalNameIgnoreCase(String chemicalName);
}
