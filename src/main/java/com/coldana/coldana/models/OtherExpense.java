package com.coldana.coldana.models;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class OtherExpense {

    private String id, user_id,description;
    private int amount;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime create_At;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime update_At;

    public OtherExpense(){};

    public OtherExpense(String id, String user_id, String description, int amount, LocalDate date, LocalDateTime create_dAt, LocalDateTime update_dAt) {
        this.id = id;
        this.user_id = user_id;
        this.description = description;
        this.amount = amount;
        this.date = date;
        this.create_At = create_dAt;
        this.update_At = update_dAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalDateTime getCreate_At() {
        return create_At;
    }

    public void setCreate_At(LocalDateTime create_dAt) {
        this.create_At = create_dAt;
    }

    public LocalDateTime getUpdate_At() {
        return update_At;
    }

    public void setUpdate_At(LocalDateTime update_dAt) {
        this.update_At = update_dAt;
    }
}
