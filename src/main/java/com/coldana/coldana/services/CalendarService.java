package com.coldana.coldana.services;

import com.coldana.coldana.models.CalendarDay;
import com.coldana.coldana.models.Category;
import com.coldana.coldana.models.Expense;
import com.coldana.coldana.repositories.CategoryRepository;
import com.coldana.coldana.repositories.ExpenseRepository;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

public class CalendarService {

    private final ExpenseService expenseService;
    private final CategoryService categoryService;

    public CalendarService() {
        this.expenseService = new ExpenseService();
        this.categoryService = new CategoryService();
    }

    public List<CalendarDay> generateCalendar(String userId, int year, int month) {
        List<CalendarDay> calendar = new ArrayList<>();

        // 1. Ambil semua expenses di bulan itu
        List<Expense> monthlyExpenses = expenseService.getExpensesByMonth(userId, year, month);
        System.out.println("DEBUG: Expenses bulan ini = " + monthlyExpenses.size());
        monthlyExpenses.forEach(e -> System.out.println("→ " + e.getCategoryId() + " on " + e.getDate()));

        // 2. Map expense by date
        Map<LocalDate, List<Expense>> expenseByDate = monthlyExpenses.stream()
                .collect(Collectors.groupingBy(Expense::getDate));

        // 3. Ambil kategori aktif user
        List<Category> activeCategories = categoryService.getActiveCategoriesByUser(userId);

        // 4. Loop per tanggal
        YearMonth yearMonth = YearMonth.of(year, month);
        for (int day = 1; day <= yearMonth.lengthOfMonth(); day++) {
            LocalDate currentDate = LocalDate.of(year, month, day);

            List<Category> categoriesForThisDate;

            if (expenseByDate.containsKey(currentDate)) {
                // Jika sudah ada expense, gunakan kategori dari histori
                List<Expense> expensesToday = expenseByDate.get(currentDate);
                Set<String> usedCategoryIds = expensesToday.stream()
                        .map(Expense::getCategoryId)
                        .collect(Collectors.toSet());

                // Ambil info kategori berdasarkan ID
                categoriesForThisDate = usedCategoryIds.stream()
                        .map(categoryId -> categoryService.getCategoryById(userId, categoryId))
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());

            } else {
                DayOfWeek dayOfWeek = currentDate.getDayOfWeek();
                String dayName = dayOfWeek.name(); // e.g., "MONDAY"

                categoriesForThisDate = activeCategories.stream()
                        .filter(cat -> {
                            // Tampilkan hanya kategori daily yang punya hari aktif
                            if (cat.isDaily()) {
                                String days = cat.getActiveDays();
                                if (days == null || days.isBlank()) return false;

                                String[] activeDays = days.split(",");
                                return Arrays.asList(activeDays).contains(dayName);
                            }
                            // Kalau bukan daily, jangan tampilkan (kecuali ada expense di bagian atas)
                            return false;
                        })
                        .collect(Collectors.toList());
            }

            calendar.add(new CalendarDay(currentDate, categoriesForThisDate, expenseByDate.containsKey(currentDate)));
        }

        return calendar;
    }
//    public Map<String, Object> generateDailyExpenses(LocalDate date, String userId) {
//        Map<String, Object> response = new HashMap<>();
//        List<Map<String, Object>> expenseList = new ArrayList<>();
//
//        // Step 1: Ambil kategori harian aktif
//        CategoryRepository categoryRepository = new CategoryRepository();
//        List<Category> activeCategories = categoryRepository.findActiveByUserId(userId)
//                .stream()
//                .filter(Category::isDaily)
//                .filter(cat -> {
//                    if (!cat.isDaily()) return false;
//
//                    // Konversi nama hari ke format "Mon", "Tue", dll
//                    String dayName = date.getDayOfWeek()
//                            .getDisplayName(TextStyle.SHORT, Locale.ENGLISH); // hasil: "Mon", "Tue", dst
//
//                    if (cat.getActiveDays() == null) return true; // <- buat sarapan, dll yg setiap hari
//
//                    List<String> days = Arrays.asList(cat.getActiveDays().split(","));
//                    return days.contains(dayName);
//                })
//                .collect(Collectors.toList());
//        System.out.println("DEBUG: Active categories untuk " + date + " = " + activeCategories.size());
//        for (Category c : activeCategories) {
//            System.out.println("  -> " + c.getName() + " (" + c.getActiveDays() + ")");
//        }
//        // Step 2: Ambil semua expenses untuk tanggal itu
//        ExpenseRepository expenseRepository = new ExpenseRepository();
//        List<Expense> expenses = expenseRepository.findByUserIdAndDate(userId, date);
//        System.out.println("DEBUG: Expenses untuk tanggal " + date + " = " + expenses.size());
//        // Step 3: Cocokkan kategori dengan expense
//        for (Category cat : activeCategories) {
//            Optional<Expense> matched = expenses.stream()
//                    .filter(exp -> exp.getCategoryId().equals(cat.getCategoryId()))
//                    .findFirst();
//
//            Map<String, Object> expMap = new HashMap<>();
//            expMap.put("categoryId", cat.getCategoryId());
//            expMap.put("categoryName", cat.getName());
//
//            if (matched.isPresent()) {
//                expMap.put("hasExpensed", true);
//                expMap.put("amount", matched.get().getAmount());
//            } else {
//                expMap.put("hasExpensed", false);
//                expMap.put("amount", null); // atau 0 kalau kamu mau default 0
//            }
//
//            expenseList.add(expMap);
//        }
//        // Step 4: Tambahkan expenses dari kategori non-daily (isDaily = false) yang sudah dicatat
//        List<String> dailyCategoryIds = activeCategories.stream()
//                .map(Category::getCategoryId)
//                .collect(Collectors.toList());
//
//        for (Expense exp : expenses) {
//            if (!dailyCategoryIds.contains(exp.getCategoryId())) {
//                Category category = categoryRepository.findById(exp.getCategoryId());
//
//                if (category != null && !category.isDaily()) {
//                    Map<String, Object> expMap = new HashMap<>();
//                    expMap.put("categoryId", category.getCategoryId());
//                    expMap.put("categoryName", category.getName());
//                    expMap.put("hasExpensed", true);
//                    expMap.put("amount", exp.getAmount());
//
//                    expenseList.add(expMap);
//                }
//            }
//        }
//
//        response.put("date", date.toString());
//        response.put("expenses", expenseList);
//        return response;
//    }
public Map<String, Object> generateDailyExpenses(LocalDate date, String userId) {
    Map<String, Object> response = new HashMap<>();
    List<Map<String, Object>> expenseList = new ArrayList<>();

    CategoryRepository categoryRepository = new CategoryRepository();
    ExpenseRepository expenseRepository = new ExpenseRepository();

    // Step 1: Ambil semua kategori user
    List<Category> allCategories = categoryRepository.findByUserId(userId);

    // Step 2: Ambil kategori harian aktif berdasarkan hari
    String dayName = date.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.ENGLISH); // Mon, Tue, dst
    List<Category> activeCategories = allCategories.stream()
            .filter(Category::isDaily)
            .filter(cat -> {
                String activeDays = cat.getActiveDays();
                if (activeDays == null || activeDays.isBlank()) return true; // kategori yang aktif tiap hari
                List<String> days = Arrays.asList(activeDays.split(","));
                return days.contains(dayName);
            })
            .collect(Collectors.toList());

    System.out.println("DEBUG: Active categories untuk " + date + " = " + activeCategories.size());
    for (Category c : activeCategories) {
        System.out.println("  -> " + c.getName() + " (" + c.getActiveDays() + ")");
    }

    // Step 3: Ambil semua expenses untuk tanggal tersebut
    List<Expense> expenses = expenseRepository.findByUserIdAndDate(userId, date);
    System.out.println("DEBUG: Expenses untuk tanggal " + date + " = " + expenses.size());

    // Step 4: Cocokkan expenses dengan kategori harian aktif
    for (Category cat : activeCategories) {
        Optional<Expense> matched = expenses.stream()
                .filter(exp -> exp.getCategoryId().equals(cat.getCategoryId()))
                .findFirst();

        Map<String, Object> expMap = new HashMap<>();
        expMap.put("categoryId", cat.getCategoryId());
        expMap.put("categoryName", cat.getName());

        if (matched.isPresent()) {
            expMap.put("hasExpensed", true);
            expMap.put("amount", matched.get().getAmount());
        } else {
            expMap.put("hasExpensed", false);
            expMap.put("amount", null); // bisa diganti 0 kalau mau
        }

        expenseList.add(expMap);
    }

    // Step 5: Tambahkan expenses dari kategori non-daily (isDaily = false)
    List<String> dailyCategoryIds = activeCategories.stream()
            .map(Category::getCategoryId)
            .collect(Collectors.toList());

    for (Expense exp : expenses) {
        if (!dailyCategoryIds.contains(exp.getCategoryId())) {
            Category category = allCategories.stream()
                    .filter(c -> c.getCategoryId().equals(exp.getCategoryId()))
                    .findFirst()
                    .orElse(null);

            if (category != null && !category.isDaily()) {
                Map<String, Object> expMap = new HashMap<>();
                expMap.put("categoryId", category.getCategoryId());
                expMap.put("categoryName", category.getName());
                expMap.put("hasExpensed", true);
                expMap.put("amount", exp.getAmount());

                expenseList.add(expMap);
            }
        }
    }

    response.put("date", date.toString());
    response.put("expenses", expenseList);
    return response;
}

    public List<Map<String, Object>> generateCalendar(LocalDate start, LocalDate end, String userId) {
        List<Map<String, Object>> calendar = new ArrayList<>();
        for (LocalDate date = start; !date.isAfter(end); date = date.plusDays(1)) {
            calendar.add(generateDailyExpenses(date, userId));
        }
        return calendar;
    }

}
