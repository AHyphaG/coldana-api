package com.coldana.coldana.services;

import com.coldana.coldana.models.OtherExpense;
import com.coldana.coldana.models.OtherExpense;
import com.coldana.coldana.repositories.OtherExpenseRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@ApplicationScoped
public class OtherExpenseService {
    private final OtherExpenseRepository otherExpenseRepository;

    public OtherExpenseService() {
        this.otherExpenseRepository = new OtherExpenseRepository();
    }

    public void addOtherExpense(OtherExpense otherExpense) {
        System.out.println("Adding other expense: " + otherExpense.getDescription());
        System.out.println("Adding other expense: " + otherExpense.getAmount());

        otherExpenseRepository.save(otherExpense);
    }

    public void updateOtherExpense(String id,String userId, String description, int amount, LocalDate date) {
        // Check if an expense already exists for this date and category
        OtherExpense existingOtherExpense = otherExpenseRepository.findById(id);
        if (existingOtherExpense != null) {
            // Sistem akan merubah bagian yang dibawah line ini
            existingOtherExpense.setDescription(description);
            existingOtherExpense.setAmount(amount);
            existingOtherExpense.setUpdate_At(LocalDateTime.now());

            otherExpenseRepository.update(existingOtherExpense);
        } else {
            // Create new expense
            OtherExpense newExpense = new OtherExpense(
                    null, // ID will be generated in the repository
                    userId,
                    description,
                    amount,
                    date,
                    LocalDateTime.now(),
                    LocalDateTime.now()
            );
            otherExpenseRepository.save(newExpense);
        }
    }
}
