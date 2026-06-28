package com.banking.loan.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "underwriting_reports")
public class UnderwritingReport {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "uw_seq")
    @SequenceGenerator(name = "uw_seq", sequenceName = "uw_seq", allocationSize = 1)
    private Long reportId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "application_id", nullable = false, unique = true)
    private LoanApplication loanApplication;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "underwriter_id", nullable = false)
    private User underwriter;

    @Column(nullable = false, length = 20)
    private String riskCategory;

    @Column(nullable = false)
    private Integer riskScore;

    @Column(precision = 5, scale = 2)
    private BigDecimal debtToIncomeRatio;

    @Column(precision = 5, scale = 2)
    private BigDecimal loanToValueRatio;

    @Column(columnDefinition = "CLOB")
    private String riskAssessment;

    @Column(columnDefinition = "CLOB")
    private String recommendation;

    @Column(nullable = false, length = 20)
    private String underwritingDecision;

    @Column(precision = 14, scale = 2)
    private BigDecimal recommendedAmount;

    @Column(precision = 5, scale = 2)
    private BigDecimal recommendedRate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable = false)
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    private Date completedAt;

    @PrePersist
    protected void onCreate() { createdAt = new Date(); }

    public Long getReportId() { return reportId; }
    public void setReportId(Long reportId) { this.reportId = reportId; }
    public LoanApplication getLoanApplication() { return loanApplication; }
    public void setLoanApplication(LoanApplication loanApplication) { this.loanApplication = loanApplication; }
    public User getUnderwriter() { return underwriter; }
    public void setUnderwriter(User underwriter) { this.underwriter = underwriter; }
    public String getRiskCategory() { return riskCategory; }
    public void setRiskCategory(String riskCategory) { this.riskCategory = riskCategory; }
    public Integer getRiskScore() { return riskScore; }
    public void setRiskScore(Integer riskScore) { this.riskScore = riskScore; }
    public BigDecimal getDebtToIncomeRatio() { return debtToIncomeRatio; }
    public void setDebtToIncomeRatio(BigDecimal debtToIncomeRatio) { this.debtToIncomeRatio = debtToIncomeRatio; }
    public BigDecimal getLoanToValueRatio() { return loanToValueRatio; }
    public void setLoanToValueRatio(BigDecimal loanToValueRatio) { this.loanToValueRatio = loanToValueRatio; }
    public String getRiskAssessment() { return riskAssessment; }
    public void setRiskAssessment(String riskAssessment) { this.riskAssessment = riskAssessment; }
    public String getRecommendation() { return recommendation; }
    public void setRecommendation(String recommendation) { this.recommendation = recommendation; }
    public String getUnderwritingDecision() { return underwritingDecision; }
    public void setUnderwritingDecision(String underwritingDecision) { this.underwritingDecision = underwritingDecision; }
    public BigDecimal getRecommendedAmount() { return recommendedAmount; }
    public void setRecommendedAmount(BigDecimal recommendedAmount) { this.recommendedAmount = recommendedAmount; }
    public BigDecimal getRecommendedRate() { return recommendedRate; }
    public void setRecommendedRate(BigDecimal recommendedRate) { this.recommendedRate = recommendedRate; }
    public Date getCreatedAt() { return createdAt; }
    public Date getCompletedAt() { return completedAt; }
    public void setCompletedAt(Date completedAt) { this.completedAt = completedAt; }
}
