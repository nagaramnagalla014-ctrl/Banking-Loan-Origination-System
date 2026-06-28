package com.banking.loan.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "loan_applications")
public class LoanApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "loan_app_seq")
    @SequenceGenerator(name = "loan_app_seq", sequenceName = "loan_app_seq", allocationSize = 1)
    private Long applicationId;

    @Column(unique = true, nullable = false, length = 20)
    private String applicationNumber;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "applicant_id", nullable = false)
    private User applicant;

    @Column(nullable = false, length = 30)
    @Enumerated(EnumType.STRING)
    private LoanType loanType;

    @Column(nullable = false, precision = 14, scale = 2)
    private BigDecimal requestedAmount;

    @Column(nullable = false)
    private Integer tenureMonths;

    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal interestRate;

    @Column(precision = 14, scale = 2)
    private BigDecimal approvedAmount;

    @Column(precision = 14, scale = 2)
    private BigDecimal emi;

    @Column(nullable = false, length = 30)
    @Enumerated(EnumType.STRING)
    private ApplicationStatus status = ApplicationStatus.SUBMITTED;

    @Column(columnDefinition = "CLOB")
    private String purpose;

    @Column(precision = 14, scale = 2)
    private BigDecimal annualIncome;

    @Column(length = 50)
    private String employmentType;

    @Column(length = 100)
    private String employerName;

    @Column(precision = 14, scale = 2)
    private BigDecimal existingEmi;

    @Column(columnDefinition = "CLOB")
    private String remarks;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable = false)
    private Date submittedAt;

    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    @JsonIgnore
    @OneToOne(mappedBy = "loanApplication", fetch = FetchType.LAZY)
    private CreditReport creditReport;

    @JsonIgnore
    @OneToMany(mappedBy = "loanApplication", fetch = FetchType.LAZY)
    private List<Document> documents;

    @JsonIgnore
    @OneToOne(mappedBy = "loanApplication", fetch = FetchType.LAZY)
    private UnderwritingReport underwritingReport;

    @JsonIgnore
    @OneToOne(mappedBy = "loanApplication", fetch = FetchType.LAZY)
    private LoanApproval loanApproval;

    @JsonIgnore
    @OneToOne(mappedBy = "loanApplication", fetch = FetchType.LAZY)
    private LoanDisbursement loanDisbursement;

    @PrePersist
    protected void onCreate() { submittedAt = new Date(); updatedAt = new Date(); }

    @PreUpdate
    protected void onUpdate() { updatedAt = new Date(); }

    public enum LoanType { HOME_LOAN, PERSONAL_LOAN, AUTO_LOAN, BUSINESS_LOAN, EDUCATION_LOAN }

    public enum ApplicationStatus {
        SUBMITTED, CREDIT_CHECK, CREDIT_CHECKED, DOCUMENT_PENDING, DOCUMENTS_VERIFIED,
        UNDERWRITING, PENDING_APPROVAL, APPROVED, REJECTED, DISBURSED, ACTIVE, CLOSED
    }

    public Long getApplicationId() { return applicationId; }
    public void setApplicationId(Long applicationId) { this.applicationId = applicationId; }
    public String getApplicationNumber() { return applicationNumber; }
    public void setApplicationNumber(String applicationNumber) { this.applicationNumber = applicationNumber; }
    public User getApplicant() { return applicant; }
    public void setApplicant(User applicant) { this.applicant = applicant; }
    public LoanType getLoanType() { return loanType; }
    public void setLoanType(LoanType loanType) { this.loanType = loanType; }
    public BigDecimal getRequestedAmount() { return requestedAmount; }
    public void setRequestedAmount(BigDecimal requestedAmount) { this.requestedAmount = requestedAmount; }
    public Integer getTenureMonths() { return tenureMonths; }
    public void setTenureMonths(Integer tenureMonths) { this.tenureMonths = tenureMonths; }
    public BigDecimal getInterestRate() { return interestRate; }
    public void setInterestRate(BigDecimal interestRate) { this.interestRate = interestRate; }
    public BigDecimal getApprovedAmount() { return approvedAmount; }
    public void setApprovedAmount(BigDecimal approvedAmount) { this.approvedAmount = approvedAmount; }
    public BigDecimal getEmi() { return emi; }
    public void setEmi(BigDecimal emi) { this.emi = emi; }
    public ApplicationStatus getStatus() { return status; }
    public void setStatus(ApplicationStatus status) { this.status = status; }
    public String getPurpose() { return purpose; }
    public void setPurpose(String purpose) { this.purpose = purpose; }
    public BigDecimal getAnnualIncome() { return annualIncome; }
    public void setAnnualIncome(BigDecimal annualIncome) { this.annualIncome = annualIncome; }
    public String getEmploymentType() { return employmentType; }
    public void setEmploymentType(String employmentType) { this.employmentType = employmentType; }
    public String getEmployerName() { return employerName; }
    public void setEmployerName(String employerName) { this.employerName = employerName; }
    public BigDecimal getExistingEmi() { return existingEmi; }
    public void setExistingEmi(BigDecimal existingEmi) { this.existingEmi = existingEmi; }
    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }
    public Date getSubmittedAt() { return submittedAt; }
    public Date getUpdatedAt() { return updatedAt; }
    public CreditReport getCreditReport() { return creditReport; }
    public List<Document> getDocuments() { return documents; }
    public UnderwritingReport getUnderwritingReport() { return underwritingReport; }
    public LoanApproval getLoanApproval() { return loanApproval; }
    public LoanDisbursement getLoanDisbursement() { return loanDisbursement; }
}
