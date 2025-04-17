package com.coldana.coldana.services;

import com.coldana.coldana.models.CalendarDay;
import com.coldana.coldana.models.Category;
import com.coldana.coldana.models.Expense;
import com.coldana.coldana.models.OtherExpense;
import com.coldana.coldana.repositories.CategoryRepository;
import com.coldana.coldana.repositories.ExpenseRepository;
import com.coldana.coldana.repositories.OtherExpenseRepository;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

public class CalendarService {

//// Start Complete Version of GPT
//public Map<String, Object> generateDailyExpenses(LocalDate date, String userId) {
//    Map<String, Object> response = new HashMap<>();
//    List<Map<String, Object>> expenseList = new ArrayList<>();
//
//    CategoryRepository categoryRepository = new CategoryRepository();
//    ExpenseRepository expenseRepository = new ExpenseRepository();
//
//    // Step 1: Ambil semua kategori user
//    List<Category> allCategories = categoryRepository.findByUserId(userId);
//
//    // Step 2: Ambil kategori harian aktif berdasarkan hari
//    String dayName = date.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.ENGLISH); // Mon, Tue, dst
//    List<Category> activeCategories = allCategories.stream()
//            .filter(Category::isDaily)
//            .filter(cat -> {
//                String activeDays = cat.getActiveDays();
//                if (activeDays == null || activeDays.isBlank()) return true; // kategori yang aktif tiap hari
//                List<String> days = Arrays.asList(activeDays.split(","));
//                return days.contains(dayName);
//            })
//            .collect(Collectors.toList());
//
////    System.out.println("DEBUG: Active categories untuk " + date + " = " + activeCategories.size());
////    for (Category c : activeCategories) {
////        System.out.println("  -> " + c.getName() + " (" + c.getActiveDays() + ")");
////    }
//
//    // Step 3: Ambil semua expenses untuk tanggal tersebut
//    List<Expense> expenses = expenseRepository.findByUserIdAndDate(userId, date);
////    System.out.println("DEBUG: Expenses untuk tanggal " + date + " = " + expenses.size());
//
//    // Step 4: Cocokkan expenses dengan kategori harian aktif
//    for (Category cat : activeCategories) {
//        Optional<Expense> matched = expenses.stream()
//                .filter(exp -> exp.getCategoryId().equals(cat.getCategoryId()))
//                .findFirst();
//
//        Map<String, Object> expMap = new HashMap<>();
//        expMap.put("categoryId", cat.getCategoryId());
//        expMap.put("categoryName", cat.getName());
//
//        if (matched.isPresent()) {
//            expMap.put("hasExpensed", true);
//            expMap.put("amount", matched.get().getAmount());
//        } else {
//            expMap.put("hasExpensed", false);
//            expMap.put("amount", null); // bisa diganti 0 kalau mau
//        }
//
//        expenseList.add(expMap);
//    }
//
//    // Step 5: Tambahkan expenses dari kategori non-daily (isDaily = false)
//    List<String> dailyCategoryIds = activeCategories.stream()
//            .map(Category::getCategoryId)
//            .collect(Collectors.toList());
//
//    for (Expense exp : expenses) {
//        if (!dailyCategoryIds.contains(exp.getCategoryId())) {
//            Category category = allCategories.stream()
//                    .filter(c -> c.getCategoryId().equals(exp.getCategoryId()))
//                    .findFirst()
//                    .orElse(null);
//
//            if (category != null && !category.isDaily()) {
//                Map<String, Object> expMap = new HashMap<>();
//                expMap.put("categoryId", category.getCategoryId());
//                expMap.put("categoryName", category.getName());
//                expMap.put("hasExpensed", true);
//                expMap.put("amount", exp.getAmount());
//
//                expenseList.add(expMap);
//            }
//        }
//    }
//
//    OtherExpenseRepository otherExpenseRepository = new OtherExpenseRepository();
//    List<OtherExpense> otherExpenses = otherExpenseRepository.findByUserIdAndDate(userId, date);
//
//    List<Map<String, Object>> otherExpenseList = new ArrayList<>();
//    for (OtherExpense oe : otherExpenses) {
//        Map<String, Object> oeMap = new HashMap<>();
//        oeMap.put("description", oe.getDescription());
//        oeMap.put("amount", oe.getAmount());
//        otherExpenseList.add(oeMap);
//    }
//
//    response.put("date", date.toString());
//    response.put("expenses", expenseList);
//    response.put("other_expenses", otherExpenseList);
//    return response;
//}
//
//    public List<Map<String, Object>> generateCalendar(LocalDate start, LocalDate end, String userId) {
//        List<Map<String, Object>> calendar = new ArrayList<>();
//        for (LocalDate date = start; !date.isAfter(end); date = date.plusDays(1)) {
//            calendar.add(generateDailyExpenses(date, userId));
//        }
//        return calendar;
//    }
//
//// End Complete Version Of GPT


// Start Complete Optimized Version
public List<Map<String, Object>> generateCalendar(LocalDate start, LocalDate end, String userId) {
    // Pre-fetch semua data yang dibutuhkan untuk keseluruhan rentang waktu
    CategoryRepository categoryRepository = new CategoryRepository();
    ExpenseRepository expenseRepository = new ExpenseRepository();
    OtherExpenseRepository otherExpenseRepository = new OtherExpenseRepository();

    // 1. Ambil semua kategori user sekali saja
    List<Category> allCategories = categoryRepository.findByUserId(userId);
    Map<String, Category> categoryMap = allCategories.stream()
            .collect(Collectors.toMap(Category::getCategoryId, c -> c));

    // 2. Ambil semua expenses untuk seluruh rentang tanggal
    List<Expense> allExpenses = expenseRepository.findByUserIdAndDateBetween(userId, start, end);

    // 3. Kelompokkan expenses berdasarkan tanggal
    Map<LocalDate, List<Expense>> expensesByDate = allExpenses.stream()
            .collect(Collectors.groupingBy(Expense::getDate));

    // 4. Ambil semua other expenses untuk seluruh rentang tanggal
    List<OtherExpense> allOtherExpenses = otherExpenseRepository.findByUserIdAndDateBetween(userId, start, end);

    // 5. Kelompokkan other expenses berdasarkan tanggal
    Map<LocalDate, List<OtherExpense>> otherExpensesByDate = allOtherExpenses.stream()
            .collect(Collectors.groupingBy(OtherExpense::getDate));

    // 6. Pisahkan kategori daily dan non-daily untuk efisiensi
    List<Category> dailyCategories = allCategories.stream()
            .filter(Category::isDaily)
            .collect(Collectors.toList());

    // 7. Buat hasil calendar
    List<Map<String, Object>> calendar = new ArrayList<>();

    for (LocalDate date = start; !date.isAfter(end); date = date.plusDays(1)) {
        calendar.add(generateDailyExpensesOptimized(
                date,
                userId,
                dailyCategories,
                categoryMap,
                expensesByDate.getOrDefault(date, Collections.emptyList()),
                otherExpensesByDate.getOrDefault(date, Collections.emptyList())
        ));
    }

    return calendar;
}

    private Map<String, Object> generateDailyExpensesOptimized(
            LocalDate date,
            String userId,
            List<Category> dailyCategories,
            Map<String, Category> categoryMap,
            List<Expense> expensesForDay,
            List<OtherExpense> otherExpensesForDay) {

        Map<String, Object> response = new HashMap<>();
        List<Map<String, Object>> expenseList = new ArrayList<>();

        // 1. Dapatkan hari dalam format singkat (Mon, Tue, dll)
        String dayName = date.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.ENGLISH);

        // 2. Filter kategori daily yang aktif untuk hari ini
        List<Category> activeDailyCategories = dailyCategories.stream()
                .filter(cat -> {
                    String activeDays = cat.getActiveDays();
                    if (activeDays == null || activeDays.isBlank()) return true; // kategori yang aktif tiap hari
                    List<String> days = Arrays.asList(activeDays.split(","));
                    return days.contains(dayName);
                })
                .collect(Collectors.toList());

        // 3. Buat map expenses untuk pencarian yang lebih cepat
        Map<String, Expense> expenseMap = expensesForDay.stream()
                .collect(Collectors.toMap(Expense::getCategoryId, e -> e, (e1, e2) -> e1));

        // 4. Proses kategori daily aktif
        for (Category cat : activeDailyCategories) {
            Map<String, Object> expMap = new HashMap<>();
            expMap.put("categoryId", cat.getCategoryId());
            expMap.put("categoryName", cat.getName());

            Expense expense = expenseMap.get(cat.getCategoryId());
            if (expense != null) {
                expMap.put("hasExpensed", true);
                expMap.put("amount", expense.getAmount());
            } else {
                expMap.put("hasExpensed", false);
                expMap.put("amount", null);
            }

            expenseList.add(expMap);
        }

        // 5. Dapatkan ID kategori daily aktif untuk filter nantinya
        Set<String> activeDailyCategoryIds = activeDailyCategories.stream()
                .map(Category::getCategoryId)
                .collect(Collectors.toSet());

        // 6. Proses expenses untuk kategori non-daily
        for (Expense expense : expensesForDay) {
            if (!activeDailyCategoryIds.contains(expense.getCategoryId())) {
                Category category = categoryMap.get(expense.getCategoryId());

                if (category != null && !category.isDaily()) {
                    Map<String, Object> expMap = new HashMap<>();
                    expMap.put("categoryId", category.getCategoryId());
                    expMap.put("categoryName", category.getName());
                    expMap.put("hasExpensed", true);
                    expMap.put("amount", expense.getAmount());

                    expenseList.add(expMap);
                }
            }
        }

        // 7. Proses other expenses
        List<Map<String, Object>> otherExpenseList = new ArrayList<>();
        for (OtherExpense oe : otherExpensesForDay) {
            Map<String, Object> oeMap = new HashMap<>();
            oeMap.put("description", oe.getDescription());
            oeMap.put("amount", oe.getAmount());
            otherExpenseList.add(oeMap);
        }

        response.put("date", date.toString());
        response.put("expenses", expenseList);
        response.put("other_expenses", otherExpenseList);

        return response;
    }
// End Complete Optimized Version

}




