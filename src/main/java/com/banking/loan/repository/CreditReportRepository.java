package com.banking.loan.repository;

import com.banking.loan.model.CreditReport;
import com.banking.loan.model.LoanApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface CreditReportRepository extends JpaRepository<CreditReport, Long> {
    Optional<CreditReport> findByLoanApplication(LoanApplication loanApplication);
}
