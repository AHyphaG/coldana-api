package com.coldana.coldana.repositories;

import com.coldana.coldana.models.Expense;
import com.coldana.coldana.models.OtherExpense;

import com.coldana.coldana.config.DatabaseConfig;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class OtherExpenseRepository {

    public OtherExpense findById(String id) {
        String sql = "SELECT * FROM other_expenses WHERE id = ?";
        OtherExpense otherExpense = null;

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                otherExpense = new OtherExpense();
                otherExpense.setId(rs.getString("id"));
                otherExpense.setUser_id(rs.getString("user_id"));
                otherExpense.setDate(rs.getDate("date").toLocalDate());
                otherExpense.setDescription(rs.getString("description"));
                otherExpense.setAmount(rs.getInt("amount"));
                otherExpense.setCreate_At(rs.getTimestamp("created_at").toLocalDateTime());
                otherExpense.setUpdate_At(rs.getTimestamp("updated_at").toLocalDateTime());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return otherExpense;
    }

    public List<OtherExpense> findByUserIdAndDate(String userId, LocalDate date) {
        List<OtherExpense> list = new ArrayList<>();

        String sql = "SELECT * FROM other_expenses WHERE user_id = ? AND date = ?";
        try (Connection conn = DatabaseConfig.getConnection(); // asumsi kamu sudah punya helper koneksi
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, userId);
            stmt.setDate(2, Date.valueOf(date));

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                OtherExpense exp = new OtherExpense();
                exp.setId(rs.getString("id"));
                exp.setUser_id(rs.getString("user_id"));
                exp.setDate(rs.getDate("date").toLocalDate());
                exp.setDescription(rs.getString("description"));
                exp.setAmount(rs.getInt("amount"));
                exp.setCreate_At(rs.getTimestamp("created_at").toLocalDateTime());
                exp.setUpdate_At(rs.getTimestamp("updated_at").toLocalDateTime());
                list.add(exp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public List<OtherExpense> findByUserIdAndDateBetween(String userId, LocalDate start, LocalDate end){
        List<OtherExpense> list = new ArrayList<>();

        String sql = "SELECT * FROM other_expenses WHERE user_id = ? AND date BETWEEN ? AND ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, userId);
            stmt.setDate(2, Date.valueOf(start));
            stmt.setDate(3, Date.valueOf(end));

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                OtherExpense exp = new OtherExpense();
                exp.setId(rs.getString("id"));
                exp.setUser_id(rs.getString("user_id"));
                exp.setDate(rs.getDate("date").toLocalDate());
                exp.setDescription(rs.getString("description"));
                exp.setAmount(rs.getInt("amount"));
                exp.setCreate_At(rs.getTimestamp("created_at").toLocalDateTime());
                exp.setUpdate_At(rs.getTimestamp("updated_at").toLocalDateTime());
                list.add(exp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public void save(OtherExpense otherExpense) {
        String query = "INSERT INTO other_expenses (user_id, date, description, amount, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, otherExpense.getUser_id());
            stmt.setDate(2, Date.valueOf(otherExpense.getDate()));
            stmt.setString(3, otherExpense.getDescription());
            stmt.setInt(4, otherExpense.getAmount());
            stmt.setTimestamp(5, Timestamp.valueOf(otherExpense.getCreate_At()));
            stmt.setTimestamp(6, Timestamp.valueOf(otherExpense.getUpdate_At()));

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating otherExpense failed, no rows affected.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    otherExpense.setId(generatedKeys.getString(1));
                } else {
                    throw new SQLException("Creating Other Expense failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(OtherExpense otherExpense){
        String query = "UPDATE other_expenses SET amount = ?, updated_at = ? WHERE id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, otherExpense.getAmount());
            stmt.setTimestamp(2, Timestamp.valueOf(otherExpense.getUpdate_At()));
            stmt.setString(3, otherExpense.getId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
