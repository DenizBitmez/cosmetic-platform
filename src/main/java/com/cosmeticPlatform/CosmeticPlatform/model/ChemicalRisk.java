package com.cosmeticPlatform.CosmeticPlatform.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "chemical_risks")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChemicalRisk {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 500)
    private String chemicalName;

    @Column(length = 500)
    private String productName;

    @Column(length = 255)
    private String brandName;

    @Column(length = 255)
    private String companyName;
}
