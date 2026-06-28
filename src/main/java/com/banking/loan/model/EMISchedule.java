package com.banking.loan.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "emi_schedules")
public class EMISchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "emi_seq")
    @SequenceGenerator(name = "emi_seq", sequenceName = "emi_seq", allocationSize = 1)
    private Long emiId;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "disbursement_id", nullable = false)
    private LoanDisbursement disbursement;

    @Column(nullable = false)
    private Integer installmentNumber;

    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    private Date dueDate;

    @Column(nullable = false, precision = 14, scale = 2)
    private BigDecimal emiAmount;

    @Column(nullable = false, precision = 14, scale = 2)
    private BigDecimal principalComponent;

    @Column(nullable = false, precision = 14, scale = 2)
    private BigDecimal interestComponent;

    @Column(nullable = false, precision = 14, scale = 2)
    private BigDecimal outstandingBalance;

    @Column(nullable = false, length = 20)
    private String status = "PENDING";

    @Temporal(TemporalType.DATE)
    private Date paidDate;

    @Column(precision = 14, scale = 2)
    private BigDecimal paidAmount;

    public Long getEmiId() { return emiId; }
    public void setEmiId(Long emiId) { this.emiId = emiId; }
    public LoanDisbursement getDisbursement() { return disbursement; }
    public void setDisbursement(LoanDisbursement disbursement) { this.disbursement = disbursement; }
    public Integer getInstallmentNumber() { return installmentNumber; }
    public void setInstallmentNumber(Integer installmentNumber) { this.installmentNumber = installmentNumber; }
    public Date getDueDate() { return dueDate; }
    public void setDueDate(Date dueDate) { this.dueDate = dueDate; }
    public BigDecimal getEmiAmount() { return emiAmount; }
    public void setEmiAmount(BigDecimal emiAmount) { this.emiAmount = emiAmount; }
    public BigDecimal getPrincipalComponent() { return principalComponent; }
    public void setPrincipalComponent(BigDecimal principalComponent) { this.principalComponent = principalComponent; }
    public BigDecimal getInterestComponent() { return interestComponent; }
    public void setInterestComponent(BigDecimal interestComponent) { this.interestComponent = interestComponent; }
    public BigDecimal getOutstandingBalance() { return outstandingBalance; }
    public void setOutstandingBalance(BigDecimal outstandingBalance) { this.outstandingBalance = outstandingBalance; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Date getPaidDate() { return paidDate; }
    public void setPaidDate(Date paidDate) { this.paidDate = paidDate; }
    public BigDecimal getPaidAmount() { return paidAmount; }
    public void setPaidAmount(BigDecimal paidAmount) { this.paidAmount = paidAmount; }
}
