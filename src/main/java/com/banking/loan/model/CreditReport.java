package com.banking.loan.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "credit_reports")
public class CreditReport {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "credit_seq")
    @SequenceGenerator(name = "credit_seq", sequenceName = "credit_seq", allocationSize = 1)
    private Long reportId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "application_id", nullable = false, unique = true)
    private LoanApplication loanApplication;

    @Column(nullable = false)
    private Integer creditScore;

    @Column(length = 20)
    private String creditRating;

    @Column(length = 500)
    private String creditSummary;

    @Column
    private Integer totalAccounts;

    @Column
    private Integer activeAccounts;

    @Column
    private Integer overdueAccounts;

    @Column(length = 20)
    private String paymentHistory;

    @Column(nullable = false, length = 20)
    private String checkStatus = "PENDING";

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "checked_by")
    private User checkedBy;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable = false)
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    private Date checkedAt;

    @PrePersist
    protected void onCreate() { createdAt = new Date(); }

    public Long getReportId() { return reportId; }
    public void setReportId(Long reportId) { this.reportId = reportId; }
    public LoanApplication getLoanApplication() { return loanApplication; }
    public void setLoanApplication(LoanApplication loanApplication) { this.loanApplication = loanApplication; }
    public Integer getCreditScore() { return creditScore; }
    public void setCreditScore(Integer creditScore) { this.creditScore = creditScore; }
    public String getCreditRating() { return creditRating; }
    public void setCreditRating(String creditRating) { this.creditRating = creditRating; }
    public String getCreditSummary() { return creditSummary; }
    public void setCreditSummary(String creditSummary) { this.creditSummary = creditSummary; }
    public Integer getTotalAccounts() { return totalAccounts; }
    public void setTotalAccounts(Integer totalAccounts) { this.totalAccounts = totalAccounts; }
    public Integer getActiveAccounts() { return activeAccounts; }
    public void setActiveAccounts(Integer activeAccounts) { this.activeAccounts = activeAccounts; }
    public Integer getOverdueAccounts() { return overdueAccounts; }
    public void setOverdueAccounts(Integer overdueAccounts) { this.overdueAccounts = overdueAccounts; }
    public String getPaymentHistory() { return paymentHistory; }
    public void setPaymentHistory(String paymentHistory) { this.paymentHistory = paymentHistory; }
    public String getCheckStatus() { return checkStatus; }
    public void setCheckStatus(String checkStatus) { this.checkStatus = checkStatus; }
    public User getCheckedBy() { return checkedBy; }
    public void setCheckedBy(User checkedBy) { this.checkedBy = checkedBy; }
    public Date getCreatedAt() { return createdAt; }
    public Date getCheckedAt() { return checkedAt; }
    public void setCheckedAt(Date checkedAt) { this.checkedAt = checkedAt; }
}
