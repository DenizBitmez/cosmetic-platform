package com.cosmeticPlatform.CosmeticPlatform.repository;

import com.cosmeticPlatform.CosmeticPlatform.model.Routine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoutineRepository extends JpaRepository<Routine, Long> {
    List<Routine> findByUserId(Integer userId);
}
