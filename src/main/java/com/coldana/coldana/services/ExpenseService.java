package com.coldana.coldana.services;

import com.coldana.coldana.models.Category;
import com.coldana.coldana.models.Expense;
import com.coldana.coldana.repositories.CategoryRepository;
import com.coldana.coldana.repositories.ExpenseRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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

//    public void addExpense(Expense expense) {
//        expenseRepository.save(expense);
//    }
//
//    public void updateExpense(Expense expense) {
//        expenseRepository.update(expense);
//    }
//
//    public void deleteExpense(String expenseId) {
//        expenseRepository.delete(expenseId);
//    }
//
//    public Expense getExpenseById(String expenseId) {
//        return expenseRepository.findById(expenseId);
//    }
}

