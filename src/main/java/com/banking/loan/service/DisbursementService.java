package com.banking.loan.service;

import com.banking.loan.dto.DisbursementRequest;
import com.banking.loan.exception.LoanException;
import com.banking.loan.messaging.LoanEventPublisher;
import com.banking.loan.model.*;
import com.banking.loan.repository.EMIScheduleRepository;
import com.banking.loan.repository.LoanDisbursementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Service
@Transactional
public class DisbursementService {

    @Autowired private LoanDisbursementRepository disbursementRepository;
    @Autowired private EMIScheduleRepository emiScheduleRepository;
    @Autowired private LoanApplicationService loanApplicationService;
    @Autowired private AuditService auditService;
    @Autowired private LoanEventPublisher eventPublisher;

    @Transactional(readOnly = true)
    public List<LoanApplication> getApprovedApplications() {
        return loanApplicationService.getByStatus("APPROVED");
    }

    public LoanDisbursement disburse(Long applicationId, DisbursementRequest req, User processor) {
        LoanApplication app = loanApplicationService.getById(applicationId);
        if (app.getStatus() != LoanApplication.ApplicationStatus.APPROVED) {
            throw new LoanException("Application must be APPROVED for disbursement");
        }

        LoanDisbursement disbursement = new LoanDisbursement();
        disbursement.setLoanApplication(app);
        disbursement.setLoanAccountNumber("LA" + System.currentTimeMillis());
        disbursement.setTransactionReference("TXN" + UUID.randomUUID().toString().replace("-", "").substring(0, 12).toUpperCase());
        disbursement.setDisbursedAmount(req.getDisbursedAmount() != null ?
            req.getDisbursedAmount() : app.getApprovedAmount());
        disbursement.setBeneficiaryName(req.getBeneficiaryName() != null ?
            req.getBeneficiaryName() : app.getApplicant().getFullName());
        disbursement.setBeneficiaryAccountNumber(req.getBeneficiaryAccountNumber());
        disbursement.setBeneficiaryIfsc(req.getBeneficiaryIfsc());
        disbursement.setDisbursementMode(req.getDisbursementMode());
        disbursement.setProcessedBy(processor);
        disbursement.setStatus("COMPLETED");
        disbursement.setCompletedAt(new Date());

        Calendar firstEmi = Calendar.getInstance();
        firstEmi.add(Calendar.MONTH, 1);
        disbursement.setFirstEmiDate(firstEmi.getTime());

        Calendar maturity = Calendar.getInstance();
        maturity.add(Calendar.MONTH, app.getTenureMonths());
        disbursement.setMaturityDate(maturity.getTime());

        LoanDisbursement saved = disbursementRepository.save(disbursement);
        generateEmiSchedule(saved, app);

        loanApplicationService.updateStatus(applicationId, LoanApplication.ApplicationStatus.DISBURSED, processor);
        auditService.log("LOAN_APPLICATION", applicationId, "LOAN_DISBURSED",
            "APPROVED", "DISBURSED", processor,
            "Disbursed: " + disbursement.getDisbursedAmount() + " | Account: " + disbursement.getLoanAccountNumber());
        eventPublisher.publishDisbursementCompleted(app, disbursement.getLoanAccountNumber());
        return saved;
    }

    private void generateEmiSchedule(LoanDisbursement disbursement, LoanApplication app) {
        BigDecimal principal = disbursement.getDisbursedAmount();
        BigDecimal annualRate = app.getInterestRate();
        int tenure = app.getTenureMonths();
        BigDecimal emiAmount = LoanApplicationService.calculateEmi(principal, annualRate, tenure);
        BigDecimal monthlyRate = annualRate.divide(BigDecimal.valueOf(1200), 10, RoundingMode.HALF_UP);

        BigDecimal outstanding = principal;
        Calendar cal = Calendar.getInstance();
        cal.setTime(disbursement.getFirstEmiDate());

        for (int i = 1; i <= tenure; i++) {
            EMISchedule emi = new EMISchedule();
            emi.setDisbursement(disbursement);
            emi.setInstallmentNumber(i);
            emi.setDueDate(cal.getTime());
            emi.setEmiAmount(emiAmount);

            BigDecimal interest = outstanding.multiply(monthlyRate).setScale(2, RoundingMode.HALF_UP);
            BigDecimal principalComp = emiAmount.subtract(interest).setScale(2, RoundingMode.HALF_UP);
            if (i == tenure) principalComp = outstanding;
            outstanding = outstanding.subtract(principalComp);
            if (outstanding.compareTo(BigDecimal.ZERO) < 0) outstanding = BigDecimal.ZERO;

            emi.setInterestComponent(interest);
            emi.setPrincipalComponent(principalComp);
            emi.setOutstandingBalance(outstanding);
            emiScheduleRepository.save(emi);

            cal.add(Calendar.MONTH, 1);
        }
    }

    @Transactional(readOnly = true)
    public LoanDisbursement getDisbursementForApplication(Long applicationId) {
        LoanApplication app = loanApplicationService.getById(applicationId);
        return disbursementRepository.findByLoanApplication(app)
            .orElseThrow(() -> new LoanException("Disbursement not found for application: " + applicationId));
    }

    @Transactional(readOnly = true)
    public List<EMISchedule> getEmiSchedule(Long applicationId) {
        LoanDisbursement disbursement = getDisbursementForApplication(applicationId);
        return emiScheduleRepository.findByDisbursementOrderByInstallmentNumber(disbursement);
    }
}
