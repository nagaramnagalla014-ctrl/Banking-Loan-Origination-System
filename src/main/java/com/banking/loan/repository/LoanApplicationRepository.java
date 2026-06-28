package com.banking.loan.repository;

import com.banking.loan.model.LoanApplication;
import com.banking.loan.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface LoanApplicationRepository extends JpaRepository<LoanApplication, Long> {
    Optional<LoanApplication> findByApplicationNumber(String applicationNumber);
    List<LoanApplication> findByApplicantOrderBySubmittedAtDesc(User applicant);
    List<LoanApplication> findByStatusOrderBySubmittedAtDesc(LoanApplication.ApplicationStatus status);
    List<LoanApplication> findAllByOrderBySubmittedAtDesc();

    @Query("SELECT COUNT(a) FROM LoanApplication a WHERE a.status = :status")
    long countByStatus(LoanApplication.ApplicationStatus status);

    @Query("SELECT SUM(a.requestedAmount) FROM LoanApplication a WHERE a.status = 'APPROVED' OR a.status = 'DISBURSED'")
    java.math.BigDecimal sumApprovedLoanAmount();
}
