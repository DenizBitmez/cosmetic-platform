package com.cosmeticPlatform.CosmeticPlatform.repository;

import com.cosmeticPlatform.CosmeticPlatform.model.RoutineStep;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoutineStepRepository extends JpaRepository<RoutineStep, Long> {
    void deleteByRoutineId(Long routineId);
}
