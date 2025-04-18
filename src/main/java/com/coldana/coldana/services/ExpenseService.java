package com.coldana.coldana.services;

import com.coldana.coldana.models.Category;
import com.coldana.coldana.models.Expense;
import com.coldana.coldana.repositories.CategoryRepository;
import com.coldana.coldana.repositories.ExpenseRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@ApplicationScoped
public class ExpenseService {

    private final ExpenseRepository expenseRepository;

    public ExpenseService() {
        this.expenseRepository = new ExpenseRepository();
    }

    public List<Expense> getExpensesByMonth(String userId, int year, int month) {
        return expenseRepository.findByMonth(userId, year, month);
    }

    public void addExpense(Expense expense) {
        expenseRepository.save(expense);
    }

    public void updateExpense(String userId, String categoryId, int amount, LocalDate date) {
        // Check if an expense already exists for this date and category
        Optional<Expense> existingExpense = expenseRepository.findByUserIdCategoryIdAndDate(userId, categoryId, date);

        if (existingExpense.isPresent()) {
            // Update existing expense
            Expense expense = existingExpense.get();
            expense.setAmount(amount);
            expense.setUpdatedAt(LocalDateTime.now());
            expenseRepository.update(expense);
        } else {
            // Create new expense
            Expense newExpense = new Expense(
                    null, // ID will be generated in the repository
                    userId,
                    categoryId,
                    amount,
                    date,
                    LocalDateTime.now(),
                    LocalDateTime.now()
            );
            expenseRepository.save(newExpense);
        }
    }
//
//    public void deleteExpense(String expenseId) {
//        expenseRepository.delete(expenseId);
//    }
//
//    public Expense getExpenseById(String expenseId) {
//        return expenseRepository.findById(expenseId);
//    }
}

