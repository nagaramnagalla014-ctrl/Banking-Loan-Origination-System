package com.banking.loan.repository;

import com.banking.loan.model.LoanApplication;
import com.banking.loan.model.LoanDisbursement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface LoanDisbursementRepository extends JpaRepository<LoanDisbursement, Long> {
    Optional<LoanDisbursement> findByLoanApplication(LoanApplication loanApplication);
    Optional<LoanDisbursement> findByLoanAccountNumber(String loanAccountNumber);
}
