package com.banking.loan.config;

import com.banking.loan.model.User;
import com.banking.loan.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired private UserRepository userRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (userRepository.count() > 0) return;

        createUser("Rajesh", "Kumar", "customer@bank.com", "password123",
            User.Role.CUSTOMER, "9876543210", "ABCDE1234F", "111222333444", "12 MG Road, Bangalore");

        createUser("Priya", "Sharma", "customer2@bank.com", "password123",
            User.Role.CUSTOMER, "9123456780", "FGHIJ5678K", "555666777888", "45 Park Street, Mumbai");

        createUser("Arun", "Mehta", "underwriter@bank.com", "admin123",
            User.Role.UNDERWRITER, "9988776655", "UVWXY9876Z", "999000111222", "Bank of India, Hyderabad");

        createUser("Sunita", "Patel", "manager@bank.com", "admin123",
            User.Role.MANAGER, "9000001111", "LMNOP2345Q", "333444555666", "State Bank HO, Mumbai");

        createUser("Admin", "User", "admin@bank.com", "admin123",
            User.Role.ADMIN, "9111112222", "QRSTU6789V", "777888999000", "Bank Admin Office, Delhi");
    }

    private void createUser(String first, String last, String email, String pass,
                             User.Role role, String phone, String pan, String aadhar, String addr) {
        User user = new User();
        user.setFirstName(first);
        user.setLastName(last);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(pass));
        user.setRole(role);
        user.setPhone(phone);
        user.setPanNumber(pan);
        user.setAadharNumber(aadhar);
        user.setAddress(addr);
        userRepository.save(user);
    }
}
