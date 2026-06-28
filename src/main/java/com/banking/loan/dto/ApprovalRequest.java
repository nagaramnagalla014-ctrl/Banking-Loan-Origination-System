package com.banking.loan.dto;

import java.math.BigDecimal;

public class ApprovalRequest {
    private String decision;
    private BigDecimal sanctionedAmount;
    private BigDecimal sanctionedRate;
    private Integer sanctionedTenure;
    private String conditions;
    private String rejectionReason;

    public String getDecision() { return decision; }
    public void setDecision(String decision) { this.decision = decision; }
    public BigDecimal getSanctionedAmount() { return sanctionedAmount; }
    public void setSanctionedAmount(BigDecimal sanctionedAmount) { this.sanctionedAmount = sanctionedAmount; }
    public BigDecimal getSanctionedRate() { return sanctionedRate; }
    public void setSanctionedRate(BigDecimal sanctionedRate) { this.sanctionedRate = sanctionedRate; }
    public Integer getSanctionedTenure() { return sanctionedTenure; }
    public void setSanctionedTenure(Integer sanctionedTenure) { this.sanctionedTenure = sanctionedTenure; }
    public String getConditions() { return conditions; }
    public void setConditions(String conditions) { this.conditions = conditions; }
    public String getRejectionReason() { return rejectionReason; }
    public void setRejectionReason(String rejectionReason) { this.rejectionReason = rejectionReason; }
}
