package com.cosmeticPlatform.CosmeticPlatform.service;

import com.cosmeticPlatform.CosmeticPlatform.model.Product;
import com.cosmeticPlatform.CosmeticPlatform.model.Routine;
import com.cosmeticPlatform.CosmeticPlatform.model.RoutineStep;
import com.cosmeticPlatform.CosmeticPlatform.repository.ProductRepository;
import com.cosmeticPlatform.CosmeticPlatform.repository.RoutineRepository;
import com.cosmeticPlatform.CosmeticPlatform.repository.RoutineStepRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class RoutineService {

    @Autowired
    private RoutineRepository routineRepository;

    @Autowired
    private RoutineStepRepository routineStepRepository;

    @Autowired
    private ProductRepository productRepository;

    public List<Routine> getUserRoutines(Integer userId) {
        return routineRepository.findByUserId(userId);
    }

    public Routine createRoutine(Integer userId, String name, String timeOfDay) {
        Routine routine = new Routine(userId, name, timeOfDay);
        return routineRepository.save(routine);
    }

    public Routine updateRoutine(Long id, String name, String timeOfDay) {
        Optional<Routine> routineOpt = routineRepository.findById(id);
        if (routineOpt.isPresent()) {
            Routine routine = routineOpt.get();
            routine.setName(name);
            routine.setTimeOfDay(timeOfDay);
            return routineRepository.save(routine);
        }
        return null;
    }

    public void deleteRoutine(Long id) {
        routineRepository.deleteById(id);
    }

    @Transactional
    public RoutineStep addStep(Long routineId, Integer productId, String notes) {
        Routine routine = routineRepository.findById(routineId)
                .orElseThrow(() -> new RuntimeException("Routine not found"));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        int order = routine.getSteps().size() + 1;
        RoutineStep step = new RoutineStep(routine, product, order, notes);

        return routineStepRepository.save(step);
    }

    public void removeStep(Long stepId) {
        routineStepRepository.deleteById(stepId);
    }

    @Transactional
    public void reorderSteps(Long routineId, List<Long> stepIds) {
        Routine routine = routineRepository.findById(routineId)
                .orElseThrow(() -> new RuntimeException("Routine not found"));

        List<RoutineStep> steps = routine.getSteps();

        for (int i = 0; i < stepIds.size(); i++) {
            Long stepId = stepIds.get(i);
            int newOrder = i + 1;

            steps.stream()
                    .filter(s -> s.getId().equals(stepId))
                    .findFirst()
                    .ifPresent(s -> {
                        s.setStepOrder(newOrder);
                        routineStepRepository.save(s);
                    });
        }
    }
}
