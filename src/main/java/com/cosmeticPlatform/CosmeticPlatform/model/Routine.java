package com.cosmeticPlatform.CosmeticPlatform.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "routine")
public class Routine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(nullable = false)
    private String name; // e.g., "Morning Routine", "Evening Routine"

    @Column(name = "time_of_day")
    private String timeOfDay; // MORNING, EVENING, WEEKLY

    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;

    @OneToMany(mappedBy = "routine", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("stepOrder ASC")
    private List<RoutineStep> steps;

    public Routine() {
    }

    public Routine(Integer userId, String name, String timeOfDay) {
        this.userId = userId;
        this.name = name;
        this.timeOfDay = timeOfDay;
        this.createdDate = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTimeOfDay() {
        return timeOfDay;
    }

    public void setTimeOfDay(String timeOfDay) {
        this.timeOfDay = timeOfDay;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public List<RoutineStep> getSteps() {
        return steps;
    }

    public void setSteps(List<RoutineStep> steps) {
        this.steps = steps;
    }
}
