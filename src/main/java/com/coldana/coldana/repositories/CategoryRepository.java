package com.coldana.coldana.repositories;
import com.coldana.coldana.config.DatabaseConfig;
import com.coldana.coldana.models.Category;
import jakarta.enterprise.context.ApplicationScoped;


import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@ApplicationScoped
public class CategoryRepository {

    public Category findById(String userId, String categoryId) {
        String query = "SELECT * FROM categories WHERE user_id = ? AND category_id = ? AND isActive = TRUE";

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, userId);
            statement.setString(2, categoryId);

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return new Category(
                            rs.getString("category_id"),
                            rs.getString("user_id"),
                            rs.getString("category_name"),
                            rs.getInt("budget_amount"),
                            rs.getTimestamp("created_at").toLocalDateTime(),
                            (rs.getTimestamp("updated_at") != null) ? rs.getTimestamp("updated_at").toLocalDateTime() : null,
                            rs.getBoolean("is_daily"),
                            rs.getInt("daily_budget"),
                            rs.getString("active_days"),
                            rs.getBoolean("isActive")
                    );
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<Category> findActiveByUserId(String userId) {
        String query = "SELECT * FROM categories WHERE user_id = ? AND isActive = TRUE";
        List<Category> categories = new ArrayList<>();

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, userId);

            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    // Tangani nilai nullable terlebih dahulu
                    Timestamp updatedAtTs = rs.getTimestamp("updated_at");
                    LocalDateTime updatedAt = (updatedAtTs != null) ? updatedAtTs.toLocalDateTime() : null;

                    int dailyBudgetRaw = rs.getInt("daily_budget");
                    Integer dailyBudget = rs.wasNull() ? null : dailyBudgetRaw;

                    // Buat objek Category
                    Category category = new Category(
                            rs.getString("category_id"),
                            rs.getString("user_id"),
                            rs.getString("category_name"),
                            rs.getInt("budget_amount"),
                            rs.getTimestamp("created_at").toLocalDateTime(),
                            updatedAt,
                            rs.getBoolean("is_daily"),
                            dailyBudget,
                            rs.getString("active_days"),
                            rs.getBoolean("isActive")
                    );

                    categories.add(category);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return categories;
    }

    public List<Category> findByUserIdAndType(String userId, Boolean isDaily) {
        String query = "SELECT * FROM categories WHERE user_id = ? AND is_daily= ? AND isActive = TRUE";
        List<Category> categories = new ArrayList<>();

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, userId);
            statement.setBoolean(2, isDaily);

            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    // Tangani nilai nullable terlebih dahulu
                    Timestamp updatedAtTs = rs.getTimestamp("updated_at");
                    LocalDateTime updatedAt = (updatedAtTs != null) ? updatedAtTs.toLocalDateTime() : null;

                    int dailyBudgetRaw = rs.getInt("daily_budget");
                    Integer dailyBudget = rs.wasNull() ? null : dailyBudgetRaw;

                    // Buat objek Category
                    Category category = new Category(
                            rs.getString("category_id"),
                            rs.getString("user_id"),
                            rs.getString("category_name"),
                            rs.getInt("budget_amount"),
                            rs.getTimestamp("created_at").toLocalDateTime(),
                            updatedAt,
                            rs.getBoolean("is_daily"),
                            dailyBudget,
                            rs.getString("active_days"),
                            rs.getBoolean("isActive")
                    );

                    categories.add(category);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return categories;
    }
    public List<Category> findByUserId(String userId) {
        String query = "SELECT * FROM categories WHERE user_id = ?";
        List<Category> categories = new ArrayList<>();

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, userId);

            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    Category category = new Category(
                            rs.getString("category_id"),
                            rs.getString("user_id"),
                            rs.getString("category_name"),
                            rs.getInt("budget_amount"),
                            rs.getTimestamp("created_at").toLocalDateTime(),
                            (rs.getTimestamp("updated_at") != null) ? rs.getTimestamp("updated_at").toLocalDateTime() : null,
                            rs.getBoolean("is_daily"),
                            rs.getInt("daily_budget"),
                            rs.getString("active_days"),
                            rs.getBoolean("isActive")
                    );
                    categories.add(category);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return categories;
    }

    public void save(Category category) {
        String query = "INSERT INTO categories (category_id, user_id, name, budget_amount, created_at, updated_at, is_daily, daily_budget, active_days, is_active) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, category.getCategoryId());
            statement.setString(2, category.getUserId());
            statement.setString(3, category.getName());
            statement.setInt(4, category.getBudgetAmount());
            statement.setTimestamp(5, Timestamp.valueOf(category.getCreatedAt()));
            statement.setTimestamp(6, Timestamp.valueOf(category.getUpdatedAt()));
            statement.setBoolean(7, category.isDaily());
            statement.setInt(8, category.getDailyBudget());
            statement.setString(9, category.getActiveDays());
            statement.setBoolean(10, category.isActive());

            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deactivate(String categoryId) {
        String query = "UPDATE categories SET is_active = FALSE, updated_at = ? WHERE category_id = ?";

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
            statement.setString(2, categoryId);

            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Kamu bisa tambahkan findById, update, delete sesuai kebutuhan.
}
