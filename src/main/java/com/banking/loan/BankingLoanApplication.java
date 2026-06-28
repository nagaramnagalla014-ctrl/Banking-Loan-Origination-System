package com.banking.loan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class BankingLoanApplication {
    public static void main(String[] args) {
        SpringApplication.run(BankingLoanApplication.class, args);
    }
}
