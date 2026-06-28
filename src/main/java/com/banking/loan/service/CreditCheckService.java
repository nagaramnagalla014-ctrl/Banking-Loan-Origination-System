package com.banking.loan.service;

import com.banking.loan.exception.LoanException;
import com.banking.loan.messaging.LoanEventPublisher;
import com.banking.loan.model.CreditReport;
import com.banking.loan.model.LoanApplication;
import com.banking.loan.model.User;
import com.banking.loan.repository.CreditReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;
import java.util.Random;

@Service
@Transactional
public class CreditCheckService {

    @Autowired private CreditReportRepository creditReportRepository;
    @Autowired private LoanApplicationService loanApplicationService;
    @Autowired private AuditService auditService;
    @Autowired private LoanEventPublisher eventPublisher;

    @Value("${app.loan.auto-approve-credit-score:750}")
    private int autoApproveCreditScore;

    @Value("${app.loan.auto-reject-credit-score:580}")
    private int autoRejectCreditScore;

    public CreditReport initiateCreditCheck(Long applicationId, User initiator) {
        LoanApplication app = loanApplicationService.getById(applicationId);
        if (app.getStatus() != LoanApplication.ApplicationStatus.SUBMITTED) {
            throw new LoanException("Credit check can only be initiated for SUBMITTED applications");
        }

        loanApplicationService.updateStatus(applicationId, LoanApplication.ApplicationStatus.CREDIT_CHECK, initiator);

        // Simulate credit bureau lookup
        int creditScore = simulateCreditScore(app.getApplicant().getPanNumber());
        CreditReport report = buildCreditReport(app, creditScore);
        CreditReport saved = creditReportRepository.save(report);

        loanApplicationService.updateStatus(applicationId, LoanApplication.ApplicationStatus.CREDIT_CHECKED, initiator);
        auditService.log("LOAN_APPLICATION", applicationId, "CREDIT_CHECK_COMPLETED",
            "CREDIT_CHECK", "CREDIT_CHECKED", initiator,
            "Credit check done. Score: " + creditScore + " | Rating: " + report.getCreditRating());
        eventPublisher.publishCreditCheckCompleted(app, creditScore);
        return saved;
    }

    @Transactional(readOnly = true)
    public CreditReport getReportForApplication(Long applicationId) {
        LoanApplication app = loanApplicationService.getById(applicationId);
        return creditReportRepository.findByLoanApplication(app)
            .orElseThrow(() -> new LoanException("Credit report not found for application: " + applicationId));
    }

    private CreditReport buildCreditReport(LoanApplication app, int score) {
        CreditReport report = new CreditReport();
        report.setLoanApplication(app);
        report.setCreditScore(score);
        report.setCreditRating(determineCreditRating(score));
        report.setCreditSummary(buildSummary(score));
        report.setTotalAccounts(5 + new Random().nextInt(10));
        report.setActiveAccounts(3 + new Random().nextInt(5));
        report.setOverdueAccounts(score < 650 ? new Random().nextInt(3) : 0);
        report.setPaymentHistory(score >= 700 ? "EXCELLENT" : score >= 600 ? "GOOD" : "POOR");
        report.setCheckStatus("COMPLETED");
        report.setCheckedAt(new Date());
        return report;
    }

    private int simulateCreditScore(String panNumber) {
        if (panNumber == null || panNumber.trim().isEmpty()) return 650;
        int hash = Math.abs(panNumber.hashCode()) % 350;
        return 550 + hash;
    }

    private String determineCreditRating(int score) {
        if (score >= 750) return "EXCELLENT";
        if (score >= 700) return "GOOD";
        if (score >= 650) return "FAIR";
        if (score >= 600) return "BELOW_AVERAGE";
        return "POOR";
    }

    private String buildSummary(int score) {
        if (score >= 750) return "Excellent credit profile. Low risk borrower with strong repayment history.";
        if (score >= 700) return "Good credit profile. Minor delinquencies noted. Eligible for standard terms.";
        if (score >= 650) return "Fair credit profile. Moderate risk. Enhanced review recommended.";
        if (score >= 600) return "Below average credit. High risk borrower. Collateral required.";
        return "Poor credit profile. Multiple defaults observed. Loan not recommended.";
    }
}
