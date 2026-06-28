package com.banking.loan.dto;

import java.math.BigDecimal;

public class UnderwritingRequest {
    private String riskCategory;
    private Integer riskScore;
    private BigDecimal debtToIncomeRatio;
    private BigDecimal loanToValueRatio;
    private String riskAssessment;
    private String recommendation;
    private String underwritingDecision;
    private BigDecimal recommendedAmount;
    private BigDecimal recommendedRate;

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
}
