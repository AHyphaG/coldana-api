package com.coldana.coldana.repositories;

import com.coldana.coldana.config.DatabaseConfig;
import com.coldana.coldana.models.Expense;
import jakarta.enterprise.context.ApplicationScoped;


import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class ExpenseRepository {

    public List<Expense> findByMonth(String userId, int year, int month) {
        String query = "SELECT * FROM expenses WHERE user_id = ? AND YEAR(date) = ? AND MONTH(date) = ?";

        List<Expense> expensesList = new ArrayList<>();

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, userId);
            statement.setInt(2, year);
            statement.setInt(3, month);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Expense expense = new Expense(
                            resultSet.getString("expense_id"),
                            resultSet.getString("user_id"),
                            resultSet.getString("category_id"),
                            resultSet.getInt("amount"),
                            resultSet.getDate("date").toLocalDate(),
                            resultSet.getTimestamp("created_at").toLocalDateTime(),
                            resultSet.getTimestamp("updated_at").toLocalDateTime()
                    );
                    expensesList.add(expense);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return expensesList;
    }

    public List<Expense> findByUserIdAndDate(String userId, LocalDate date) {
        String query = "SELECT * FROM expenses WHERE user_id = ? AND DATE(`date`) = ?";
        List<Expense> expenses = new ArrayList<>();

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, userId);
            stmt.setDate(2, Date.valueOf(date));

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Expense expense = new Expense(
                            rs.getString("expense_id"),
                            rs.getString("user_id"),
                            rs.getString("category_id"),
                            rs.getInt("amount"),
                            rs.getDate("date").toLocalDate(),
                            rs.getTimestamp("created_at").toLocalDateTime(),
                            rs.getTimestamp("updated_at").toLocalDateTime()
                    );
                    expenses.add(expense);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return expenses;
    }

    public List<Expense> findByUserIdAndDateBetween(String userId, LocalDate start, LocalDate end){
        String query = "SELECT * FROM expenses WHERE user_id = ? AND date BETWEEN ? AND ?";
        List<Expense> expenses = new ArrayList<>();

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, userId);
            stmt.setDate(2, Date.valueOf(start));
            stmt.setDate(3, Date.valueOf(end));

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Expense expense = new Expense(
                            rs.getString("expense_id"),
                            rs.getString("user_id"),
                            rs.getString("category_id"),
                            rs.getInt("amount"),
                            rs.getDate("date").toLocalDate(),
                            rs.getTimestamp("created_at").toLocalDateTime(),
                            rs.getTimestamp("updated_at").toLocalDateTime()
                    );
                    expenses.add(expense);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return expenses;
    }

    public void save(Expense expense) {
        String query = "INSERT INTO expenses (user_id, category_id, amount, date, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, expense.getUserId());
            stmt.setString(2, expense.getCategoryId());
            stmt.setInt(3, expense.getAmount());
            stmt.setDate(4, Date.valueOf(expense.getDate()));
            stmt.setTimestamp(5, Timestamp.valueOf(expense.getCreatedAt()));
            stmt.setTimestamp(6, Timestamp.valueOf(expense.getUpdatedAt()));

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating expense failed, no rows affected.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    expense.setExpensesId(generatedKeys.getString(1));
                } else {
                    throw new SQLException("Creating expense failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Optional<Expense> findByUserIdCategoryIdAndDate(String userId, String categoryId, LocalDate date) {
        String query = "SELECT * FROM expenses WHERE user_id = ? AND category_id = ? AND date = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, userId);
            stmt.setString(2, categoryId);
            stmt.setDate(3, Date.valueOf(date));

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Expense expense = new Expense(
                            rs.getString("expense_id"),
                            rs.getString("user_id"),
                            rs.getString("category_id"),
                            rs.getInt("amount"),
                            rs.getDate("date").toLocalDate(),
                            rs.getTimestamp("created_at").toLocalDateTime(),
                            rs.getTimestamp("updated_at").toLocalDateTime()
                    );
                    return Optional.of(expense);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    public void update(Expense expense) {
        String query = "UPDATE expenses SET amount = ?, updated_at = ? WHERE expense_id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, expense.getAmount());
            stmt.setTimestamp(2, Timestamp.valueOf(expense.getUpdatedAt()));
            stmt.setString(3, expense.getExpensesId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
