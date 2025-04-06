package com.coldana.coldana.models;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.util.List;

public class CalendarDay {
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
    private List<Category> categories;
    private boolean hasExpenses;

    public CalendarDay(LocalDate date, List<Category> categories, boolean hasExpenses) {
        this.date = date;
        this.categories = categories;
        this.hasExpenses = hasExpenses;
    }

    public LocalDate getDate() {
        return date;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public boolean isHasExpenses() {
        return hasExpenses;
    }

}
