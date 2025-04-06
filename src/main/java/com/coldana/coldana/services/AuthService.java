package com.coldana.coldana.services;

import com.coldana.coldana.models.Users;
import com.coldana.coldana.repositories.UsersRepository;
import com.coldana.coldana.utils.HashUtil;

import java.util.Optional;

public class AuthService {
    private final UsersRepository usersRepository;

    public AuthService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    public boolean login(String username, String password) {
        password = HashUtil.sha256(password);
        System.out.println("Input Password: " + password);
        Optional<Users> usersOpt = usersRepository.findByUsername(username);
        if (usersOpt.isPresent()) {
            Users users = usersOpt.get();
            System.out.println("User Password: " + users.getPassword());
            return users.getPassword().equals(password);
        }

        return false;
    }
}
