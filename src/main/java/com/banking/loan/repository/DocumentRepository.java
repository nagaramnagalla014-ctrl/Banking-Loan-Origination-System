package com.banking.loan.repository;

import com.banking.loan.model.Document;
import com.banking.loan.model.LoanApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {
    List<Document> findByLoanApplication(LoanApplication loanApplication);
    List<Document> findByLoanApplicationAndVerificationStatus(LoanApplication loanApplication, String status);
    boolean existsByLoanApplicationAndDocumentType(LoanApplication loanApplication, String documentType);
}
