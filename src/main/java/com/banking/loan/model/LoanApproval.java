package com.banking.loan.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "loan_approvals")
public class LoanApproval {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "approval_seq")
    @SequenceGenerator(name = "approval_seq", sequenceName = "approval_seq", allocationSize = 1)
    private Long approvalId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "application_id", nullable = false, unique = true)
    private LoanApplication loanApplication;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "approved_by", nullable = false)
    private User approvedBy;

    @Column(nullable = false, length = 20)
    private String decision;

    @Column(precision = 14, scale = 2)
    private BigDecimal sanctionedAmount;

    @Column(precision = 5, scale = 2)
    private BigDecimal sanctionedRate;

    @Column
    private Integer sanctionedTenure;

    @Column(precision = 14, scale = 2)
    private BigDecimal emi;

    @Column(columnDefinition = "CLOB")
    private String conditions;

    @Column(columnDefinition = "CLOB")
    private String rejectionReason;

    @Column(length = 50)
    private String sanctionLetterNumber;

    @Temporal(TemporalType.DATE)
    private Date sanctionExpiryDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable = false)
    private Date decidedAt;

    @PrePersist
    protected void onCreate() { decidedAt = new Date(); }

    public Long getApprovalId() { return approvalId; }
    public void setApprovalId(Long approvalId) { this.approvalId = approvalId; }
    public LoanApplication getLoanApplication() { return loanApplication; }
    public void setLoanApplication(LoanApplication loanApplication) { this.loanApplication = loanApplication; }
    public User getApprovedBy() { return approvedBy; }
    public void setApprovedBy(User approvedBy) { this.approvedBy = approvedBy; }
    public String getDecision() { return decision; }
    public void setDecision(String decision) { this.decision = decision; }
    public BigDecimal getSanctionedAmount() { return sanctionedAmount; }
    public void setSanctionedAmount(BigDecimal sanctionedAmount) { this.sanctionedAmount = sanctionedAmount; }
    public BigDecimal getSanctionedRate() { return sanctionedRate; }
    public void setSanctionedRate(BigDecimal sanctionedRate) { this.sanctionedRate = sanctionedRate; }
    public Integer getSanctionedTenure() { return sanctionedTenure; }
    public void setSanctionedTenure(Integer sanctionedTenure) { this.sanctionedTenure = sanctionedTenure; }
    public BigDecimal getEmi() { return emi; }
    public void setEmi(BigDecimal emi) { this.emi = emi; }
    public String getConditions() { return conditions; }
    public void setConditions(String conditions) { this.conditions = conditions; }
    public String getRejectionReason() { return rejectionReason; }
    public void setRejectionReason(String rejectionReason) { this.rejectionReason = rejectionReason; }
    public String getSanctionLetterNumber() { return sanctionLetterNumber; }
    public void setSanctionLetterNumber(String sanctionLetterNumber) { this.sanctionLetterNumber = sanctionLetterNumber; }
    public Date getSanctionExpiryDate() { return sanctionExpiryDate; }
    public void setSanctionExpiryDate(Date sanctionExpiryDate) { this.sanctionExpiryDate = sanctionExpiryDate; }
    public Date getDecidedAt() { return decidedAt; }
}
