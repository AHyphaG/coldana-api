package com.coldana.coldana.repositories;

import com.coldana.coldana.models.OtherExpense;

import com.coldana.coldana.config.DatabaseConfig;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class OtherExpenseRepository {
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
}
