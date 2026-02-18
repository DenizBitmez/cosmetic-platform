package com.cosmeticPlatform.CosmeticPlatform.controller;

import com.cosmeticPlatform.CosmeticPlatform.model.Routine;
import com.cosmeticPlatform.CosmeticPlatform.model.RoutineStep;
import com.cosmeticPlatform.CosmeticPlatform.service.RoutineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/routines")
@CrossOrigin(origins = "http://localhost:5173")
public class RoutineController {

    @Autowired
    private RoutineService routineService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Routine>> getUserRoutines(@PathVariable Integer userId) {
        return ResponseEntity.ok(routineService.getUserRoutines(userId));
    }

    @PostMapping
    public ResponseEntity<Routine> createRoutine(@RequestBody Map<String, Object> payload) {
        Object userIdObj = payload.get("userId");
        Integer userId = null;
        if (userIdObj instanceof Number) {
            userId = ((Number) userIdObj).intValue();
        } else if (userIdObj instanceof String) {
            userId = Integer.parseInt((String) userIdObj);
        }

        String name = (String) payload.get("name");
        String timeOfDay = (String) payload.get("timeOfDay");

        return ResponseEntity.ok(routineService.createRoutine(userId, name, timeOfDay));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Routine> updateRoutine(@PathVariable Long id, @RequestBody Map<String, String> payload) {
        String name = payload.get("name");
        String timeOfDay = payload.get("timeOfDay");
        Routine updated = routineService.updateRoutine(id, name, timeOfDay);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoutine(@PathVariable Long id) {
        routineService.deleteRoutine(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/steps")
    public ResponseEntity<RoutineStep> addStep(@PathVariable Long id, @RequestBody Map<String, Object> payload) {
        Object productIdObj = payload.get("productId");
        Integer productId = null;
        if (productIdObj instanceof Number) {
            productId = ((Number) productIdObj).intValue();
        } else if (productIdObj instanceof String) {
            productId = Integer.parseInt((String) productIdObj);
        }

        String notes = (String) payload.get("notes");

        return ResponseEntity.ok(routineService.addStep(id, productId, notes));
    }

    @DeleteMapping("/steps/{stepId}")
    public ResponseEntity<Void> removeStep(@PathVariable Long stepId) {
        routineService.removeStep(stepId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/reorder")
    public ResponseEntity<Void> reorderSteps(@PathVariable Long id, @RequestBody List<Long> stepIds) {
        routineService.reorderSteps(id, stepIds);
        return ResponseEntity.ok().build();
    }
}
