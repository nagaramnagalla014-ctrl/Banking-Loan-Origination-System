package com.banking.loan.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "loan_disbursements")
public class LoanDisbursement {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "disb_seq")
    @SequenceGenerator(name = "disb_seq", sequenceName = "disb_seq", allocationSize = 1)
    private Long disbursementId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "application_id", nullable = false, unique = true)
    private LoanApplication loanApplication;

    @Column(unique = true, nullable = false, length = 30)
    private String loanAccountNumber;

    @Column(unique = true, nullable = false, length = 50)
    private String transactionReference;

    @Column(nullable = false, precision = 14, scale = 2)
    private BigDecimal disbursedAmount;

    @Column(length = 100)
    private String beneficiaryName;

    @Column(length = 30)
    private String beneficiaryAccountNumber;

    @Column(length = 15)
    private String beneficiaryIfsc;

    @Column(nullable = false, length = 30)
    private String disbursementMode;

    @Column(nullable = false, length = 20)
    private String status = "INITIATED";

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "processed_by")
    private User processedBy;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable = false)
    private Date initiatedAt;

    @Temporal(TemporalType.TIMESTAMP)
    private Date completedAt;

    @Temporal(TemporalType.DATE)
    private Date firstEmiDate;

    @Temporal(TemporalType.DATE)
    private Date maturityDate;

    @PrePersist
    protected void onCreate() { initiatedAt = new Date(); }

    public Long getDisbursementId() { return disbursementId; }
    public void setDisbursementId(Long disbursementId) { this.disbursementId = disbursementId; }
    public LoanApplication getLoanApplication() { return loanApplication; }
    public void setLoanApplication(LoanApplication loanApplication) { this.loanApplication = loanApplication; }
    public String getLoanAccountNumber() { return loanAccountNumber; }
    public void setLoanAccountNumber(String loanAccountNumber) { this.loanAccountNumber = loanAccountNumber; }
    public String getTransactionReference() { return transactionReference; }
    public void setTransactionReference(String transactionReference) { this.transactionReference = transactionReference; }
    public BigDecimal getDisbursedAmount() { return disbursedAmount; }
    public void setDisbursedAmount(BigDecimal disbursedAmount) { this.disbursedAmount = disbursedAmount; }
    public String getBeneficiaryName() { return beneficiaryName; }
    public void setBeneficiaryName(String beneficiaryName) { this.beneficiaryName = beneficiaryName; }
    public String getBeneficiaryAccountNumber() { return beneficiaryAccountNumber; }
    public void setBeneficiaryAccountNumber(String beneficiaryAccountNumber) { this.beneficiaryAccountNumber = beneficiaryAccountNumber; }
    public String getBeneficiaryIfsc() { return beneficiaryIfsc; }
    public void setBeneficiaryIfsc(String beneficiaryIfsc) { this.beneficiaryIfsc = beneficiaryIfsc; }
    public String getDisbursementMode() { return disbursementMode; }
    public void setDisbursementMode(String disbursementMode) { this.disbursementMode = disbursementMode; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public User getProcessedBy() { return processedBy; }
    public void setProcessedBy(User processedBy) { this.processedBy = processedBy; }
    public Date getInitiatedAt() { return initiatedAt; }
    public Date getCompletedAt() { return completedAt; }
    public void setCompletedAt(Date completedAt) { this.completedAt = completedAt; }
    public Date getFirstEmiDate() { return firstEmiDate; }
    public void setFirstEmiDate(Date firstEmiDate) { this.firstEmiDate = firstEmiDate; }
    public Date getMaturityDate() { return maturityDate; }
    public void setMaturityDate(Date maturityDate) { this.maturityDate = maturityDate; }
}
