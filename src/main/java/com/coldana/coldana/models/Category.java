package com.coldana.coldana.models;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public class Category {
    private String categoryId;
    private String userId;
    private String name;
    private int budgetAmount;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;
    private boolean isDaily;
    private Integer dailyBudget;
    private String activeDays;
    private boolean isActive;


    // Constructor

    public Category(String categoryId, String userId, String name, int budgetAmount, LocalDateTime createdAt, LocalDateTime updatedAt, boolean isDaily, Integer dailyBudget, String activeDays, boolean isActive) {
        this.categoryId = categoryId;
        this.userId = userId;
        this.name = name;
        this.budgetAmount = budgetAmount;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.isDaily = isDaily;
        this.dailyBudget = dailyBudget;
        this.activeDays = activeDays;
        this.isActive = isActive;
    }


    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getBudgetAmount() {
        return budgetAmount;
    }

    public void setBudgetAmount(int budgetAmount) {
        this.budgetAmount = budgetAmount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public boolean isDaily() {
        return isDaily;
    }

    public void setDaily(boolean daily) {
        isDaily = daily;
    }

    public Integer getDailyBudget() {
        return dailyBudget;
    }

    public void setDailyBudget(int dailyBudget) {
        this.dailyBudget = dailyBudget;
    }

    public String getActiveDays() {
        return activeDays;
    }

    public void setActiveDays(String activeDays) {
        this.activeDays = activeDays;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}

