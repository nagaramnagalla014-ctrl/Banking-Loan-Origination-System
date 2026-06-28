package com.banking.loan.service;

import com.banking.loan.dto.UnderwritingRequest;
import com.banking.loan.exception.LoanException;
import com.banking.loan.model.LoanApplication;
import com.banking.loan.model.UnderwritingReport;
import com.banking.loan.model.User;
import com.banking.loan.repository.UnderwritingReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class UnderwritingService {

    @Autowired private UnderwritingReportRepository underwritingReportRepository;
    @Autowired private LoanApplicationService loanApplicationService;
    @Autowired private AuditService auditService;

    @Transactional(readOnly = true)
    public List<LoanApplication> getApplicationsForUnderwriting() {
        return loanApplicationService.getByStatus("DOCUMENTS_VERIFIED");
    }

    public LoanApplication assignToUnderwriting(Long applicationId, User underwriter) {
        LoanApplication app = loanApplicationService.getById(applicationId);
        if (app.getStatus() != LoanApplication.ApplicationStatus.DOCUMENTS_VERIFIED) {
            throw new LoanException("Application must be in DOCUMENTS_VERIFIED status for underwriting");
        }
        return loanApplicationService.updateStatus(applicationId,
            LoanApplication.ApplicationStatus.UNDERWRITING, underwriter);
    }

    public UnderwritingReport submitUnderwritingReport(Long applicationId,
                                                        UnderwritingRequest req,
                                                        User underwriter) {
        LoanApplication app = loanApplicationService.getById(applicationId);
        if (app.getStatus() != LoanApplication.ApplicationStatus.UNDERWRITING) {
            throw new LoanException("Application must be in UNDERWRITING status");
        }

        UnderwritingReport report = new UnderwritingReport();
        report.setLoanApplication(app);
        report.setUnderwriter(underwriter);
        report.setRiskCategory(req.getRiskCategory());
        report.setRiskScore(req.getRiskScore());
        report.setDebtToIncomeRatio(req.getDebtToIncomeRatio());
        report.setLoanToValueRatio(req.getLoanToValueRatio());
        report.setRiskAssessment(req.getRiskAssessment());
        report.setRecommendation(req.getRecommendation());
        report.setUnderwritingDecision(req.getUnderwritingDecision());
        report.setRecommendedAmount(req.getRecommendedAmount() != null ?
            req.getRecommendedAmount() : app.getRequestedAmount());
        report.setRecommendedRate(req.getRecommendedRate() != null ?
            req.getRecommendedRate() : app.getInterestRate());
        report.setCompletedAt(new Date());

        UnderwritingReport saved = underwritingReportRepository.save(report);
        loanApplicationService.updateStatus(applicationId,
            LoanApplication.ApplicationStatus.PENDING_APPROVAL, underwriter);

        auditService.log("LOAN_APPLICATION", applicationId, "UNDERWRITING_COMPLETED",
            "UNDERWRITING", "PENDING_APPROVAL", underwriter,
            "Underwriting done. Decision: " + req.getUnderwritingDecision() +
            " | Risk: " + req.getRiskCategory());
        return saved;
    }

    @Transactional(readOnly = true)
    public UnderwritingReport getReportForApplication(Long applicationId) {
        LoanApplication app = loanApplicationService.getById(applicationId);
        return underwritingReportRepository.findByLoanApplication(app)
            .orElseThrow(() -> new LoanException("Underwriting report not found"));
    }
}
