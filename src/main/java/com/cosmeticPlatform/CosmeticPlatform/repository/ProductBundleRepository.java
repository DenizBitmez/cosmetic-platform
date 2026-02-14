package com.cosmeticPlatform.CosmeticPlatform.repository;

import com.cosmeticPlatform.CosmeticPlatform.model.ProductBundle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductBundleRepository extends JpaRepository<ProductBundle, Long> {

    // Find bundles that contain a specific product
    @Query("SELECT b FROM ProductBundle b JOIN b.products p WHERE p.id = :productId")
    List<ProductBundle> findBundlesByProductId(Integer productId);
}
