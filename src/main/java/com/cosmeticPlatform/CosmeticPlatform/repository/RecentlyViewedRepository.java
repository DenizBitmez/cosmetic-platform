package com.cosmeticPlatform.CosmeticPlatform.repository;

import com.cosmeticPlatform.CosmeticPlatform.model.RecentlyViewed;
import com.cosmeticPlatform.CosmeticPlatform.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface RecentlyViewedRepository extends JpaRepository<RecentlyViewed, Integer> {
    List<RecentlyViewed> findByUserOrderByViewedAtDesc(User user);

    Optional<RecentlyViewed> findByUserAndExternalProductId(User user, int externalProductId);
}
