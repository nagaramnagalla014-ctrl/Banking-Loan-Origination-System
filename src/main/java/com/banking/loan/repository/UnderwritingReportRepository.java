package com.banking.loan.repository;

import com.banking.loan.model.LoanApplication;
import com.banking.loan.model.UnderwritingReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UnderwritingReportRepository extends JpaRepository<UnderwritingReport, Long> {
    Optional<UnderwritingReport> findByLoanApplication(LoanApplication loanApplication);
}
