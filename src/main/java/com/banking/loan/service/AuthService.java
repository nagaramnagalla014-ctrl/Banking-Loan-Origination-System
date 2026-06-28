package com.banking.loan.service;

import com.banking.loan.config.JwtTokenProvider;
import com.banking.loan.dto.JwtResponse;
import com.banking.loan.dto.LoginRequest;
import com.banking.loan.dto.RegisterRequest;
import com.banking.loan.exception.LoanException;
import com.banking.loan.model.User;
import com.banking.loan.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AuthService {

    @Autowired private AuthenticationManager authenticationManager;
    @Autowired private JwtTokenProvider tokenProvider;
    @Autowired private UserRepository userRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    public JwtResponse login(LoginRequest request) {
        Authentication auth = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(auth);
        String token = tokenProvider.generateToken(auth);

        User user = userRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new LoanException("User not found"));
        return new JwtResponse(token, user.getUserId(), user.getEmail(),
            user.getRole().name(), user.getFullName());
    }

    public User register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail()))
            throw new LoanException("Email already registered: " + request.getEmail());

        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setPhone(request.getPhone());
        user.setPanNumber(request.getPanNumber());
        user.setAadharNumber(request.getAadharNumber());
        user.setAddress(request.getAddress());
        user.setRole(User.Role.CUSTOMER);
        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public User getCurrentUser(String email) {
        return userRepository.findByEmail(email)
            .orElseThrow(() -> new LoanException("User not found: " + email));
    }
}
