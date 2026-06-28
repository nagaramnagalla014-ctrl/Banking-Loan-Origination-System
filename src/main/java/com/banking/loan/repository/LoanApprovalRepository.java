package com.banking.loan.repository;

import com.banking.loan.model.LoanApplication;
import com.banking.loan.model.LoanApproval;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface LoanApprovalRepository extends JpaRepository<LoanApproval, Long> {
    Optional<LoanApproval> findByLoanApplication(LoanApplication loanApplication);
}
