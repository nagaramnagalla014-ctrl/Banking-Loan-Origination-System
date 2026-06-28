package com.banking.loan.service;

import com.banking.loan.dto.ApprovalRequest;
import com.banking.loan.exception.LoanException;
import com.banking.loan.messaging.LoanEventPublisher;
import com.banking.loan.model.LoanApproval;
import com.banking.loan.model.LoanApplication;
import com.banking.loan.model.User;
import com.banking.loan.repository.LoanApprovalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;

@Service
@Transactional
public class ApprovalService {

    @Autowired private LoanApprovalRepository loanApprovalRepository;
    @Autowired private LoanApplicationService loanApplicationService;
    @Autowired private AuditService auditService;
    @Autowired private LoanEventPublisher eventPublisher;

    @Transactional(readOnly = true)
    public List<LoanApplication> getPendingApprovals() {
        return loanApplicationService.getByStatus("PENDING_APPROVAL");
    }

    public LoanApproval processApproval(Long applicationId, ApprovalRequest req, User manager) {
        LoanApplication app = loanApplicationService.getById(applicationId);
        if (app.getStatus() != LoanApplication.ApplicationStatus.PENDING_APPROVAL) {
            throw new LoanException("Application must be in PENDING_APPROVAL status");
        }

        String decision = req.getDecision().toUpperCase();
        if (!"APPROVED".equals(decision) && !"REJECTED".equals(decision)) {
            throw new LoanException("Decision must be APPROVED or REJECTED");
        }

        LoanApproval approval = new LoanApproval();
        approval.setLoanApplication(app);
        approval.setApprovedBy(manager);
        approval.setDecision(decision);
        approval.setConditions(req.getConditions());

        if ("APPROVED".equals(decision)) {
            BigDecimal sanctionedAmount = req.getSanctionedAmount() != null ?
                req.getSanctionedAmount() : app.getRequestedAmount();
            BigDecimal sanctionedRate = req.getSanctionedRate() != null ?
                req.getSanctionedRate() : app.getInterestRate();
            int tenure = req.getSanctionedTenure() != null ?
                req.getSanctionedTenure() : app.getTenureMonths();

            BigDecimal emi = LoanApplicationService.calculateEmi(sanctionedAmount, sanctionedRate, tenure);

            approval.setSanctionedAmount(sanctionedAmount);
            approval.setSanctionedRate(sanctionedRate);
            approval.setSanctionedTenure(tenure);
            approval.setEmi(emi);
            approval.setSanctionLetterNumber("SL" + System.currentTimeMillis());

            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DAY_OF_MONTH, 30);
            approval.setSanctionExpiryDate(cal.getTime());

            app.setApprovedAmount(sanctionedAmount);
            app.setInterestRate(sanctionedRate);
            app.setTenureMonths(tenure);
            app.setEmi(emi);

            loanApplicationService.updateStatus(applicationId,
                LoanApplication.ApplicationStatus.APPROVED, manager);
        } else {
            approval.setRejectionReason(req.getRejectionReason());
            loanApplicationService.updateStatus(applicationId,
                LoanApplication.ApplicationStatus.REJECTED, manager);
        }

        LoanApproval saved = loanApprovalRepository.save(approval);
        auditService.log("LOAN_APPLICATION", applicationId, "APPROVAL_DECISION",
            "PENDING_APPROVAL", decision, manager,
            "Approval decision: " + decision + " by " + manager.getFullName());
        eventPublisher.publishApprovalDecision(app, decision);
        return saved;
    }

    @Transactional(readOnly = true)
    public LoanApproval getApprovalForApplication(Long applicationId) {
        LoanApplication app = loanApplicationService.getById(applicationId);
        return loanApprovalRepository.findByLoanApplication(app)
            .orElseThrow(() -> new LoanException("Approval record not found for application: " + applicationId));
    }
}
