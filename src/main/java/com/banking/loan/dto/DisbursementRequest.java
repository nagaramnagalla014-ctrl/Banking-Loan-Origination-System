package com.banking.loan.dto;

import java.math.BigDecimal;

public class DisbursementRequest {
    private BigDecimal disbursedAmount;
    private String beneficiaryName;
    private String beneficiaryAccountNumber;
    private String beneficiaryIfsc;
    private String disbursementMode;

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
}
