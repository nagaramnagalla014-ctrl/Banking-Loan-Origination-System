package com.banking.loan.service;

import com.banking.loan.dto.LoanApplicationRequest;
import com.banking.loan.exception.LoanException;
import com.banking.loan.messaging.LoanEventPublisher;
import com.banking.loan.model.LoanApplication;
import com.banking.loan.model.User;
import com.banking.loan.repository.LoanApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class LoanApplicationService {

    @Autowired private LoanApplicationRepository loanApplicationRepository;
    @Autowired private AuditService auditService;
    @Autowired private LoanEventPublisher eventPublisher;

    @Value("${app.loan.auto-approve-credit-score:750}")
    private int autoApproveCreditScore;

    public LoanApplication submitApplication(LoanApplicationRequest req, User applicant) {
        LoanApplication app = new LoanApplication();
        app.setApplicationNumber("LN" + System.currentTimeMillis());
        app.setApplicant(applicant);
        app.setLoanType(LoanApplication.LoanType.valueOf(req.getLoanType()));
        app.setRequestedAmount(req.getRequestedAmount());
        app.setTenureMonths(req.getTenureMonths());
        app.setPurpose(req.getPurpose());
        app.setAnnualIncome(req.getAnnualIncome());
        app.setEmploymentType(req.getEmploymentType());
        app.setEmployerName(req.getEmployerName());
        app.setExistingEmi(req.getExistingEmi() != null ? req.getExistingEmi() : BigDecimal.ZERO);
        app.setInterestRate(determineDefaultRate(req.getLoanType()));
        app.setStatus(LoanApplication.ApplicationStatus.SUBMITTED);

        LoanApplication saved = loanApplicationRepository.save(app);
        auditService.log("LOAN_APPLICATION", saved.getApplicationId(), "APPLICATION_SUBMITTED",
            null, "SUBMITTED", applicant, "Loan application submitted by " + applicant.getEmail());
        eventPublisher.publishApplicationSubmitted(saved);
        return saved;
    }

    @Transactional(readOnly = true)
    public LoanApplication getById(Long id) {
        return loanApplicationRepository.findById(id)
            .orElseThrow(() -> new LoanException("Application not found: " + id));
    }

    @Transactional(readOnly = true)
    public LoanApplication getByApplicationNumber(String appNumber) {
        return loanApplicationRepository.findByApplicationNumber(appNumber)
            .orElseThrow(() -> new LoanException("Application not found: " + appNumber));
    }

    @Transactional(readOnly = true)
    public List<LoanApplication> getMyApplications(User applicant) {
        return loanApplicationRepository.findByApplicantOrderBySubmittedAtDesc(applicant);
    }

    @Transactional(readOnly = true)
    public List<LoanApplication> getByStatus(String status) {
        return loanApplicationRepository.findByStatusOrderBySubmittedAtDesc(
            LoanApplication.ApplicationStatus.valueOf(status));
    }

    @Transactional(readOnly = true)
    public List<LoanApplication> getAllApplications() {
        return loanApplicationRepository.findAllByOrderBySubmittedAtDesc();
    }

    public LoanApplication updateStatus(Long id, LoanApplication.ApplicationStatus newStatus, User actor) {
        LoanApplication app = getById(id);
        String from = app.getStatus().name();
        app.setStatus(newStatus);
        LoanApplication saved = loanApplicationRepository.save(app);
        auditService.log("LOAN_APPLICATION", id, "STATUS_CHANGE", from, newStatus.name(), actor,
            "Status changed from " + from + " to " + newStatus.name());
        return saved;
    }

    public static BigDecimal calculateEmi(BigDecimal principal, BigDecimal annualRate, int tenureMonths) {
        BigDecimal monthlyRate = annualRate.divide(BigDecimal.valueOf(1200), 10, RoundingMode.HALF_UP);
        if (monthlyRate.compareTo(BigDecimal.ZERO) == 0) {
            return principal.divide(BigDecimal.valueOf(tenureMonths), 2, RoundingMode.HALF_UP);
        }
        double r = monthlyRate.doubleValue();
        double n = tenureMonths;
        double p = principal.doubleValue();
        double emi = p * r * Math.pow(1 + r, n) / (Math.pow(1 + r, n) - 1);
        return BigDecimal.valueOf(emi).setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal determineDefaultRate(String loanType) {
        switch (loanType) {
            case "HOME_LOAN":      return new BigDecimal("8.50");
            case "AUTO_LOAN":      return new BigDecimal("9.00");
            case "EDUCATION_LOAN": return new BigDecimal("10.00");
            case "BUSINESS_LOAN":  return new BigDecimal("12.00");
            default:               return new BigDecimal("13.50");
        }
    }
}
