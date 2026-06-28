package com.banking.loan.messaging;

import java.io.Serializable;
import java.util.Date;

public class LoanEvent implements Serializable {

    private String eventType;
    private String applicationNumber;
    private Long applicationId;
    private String applicantEmail;
    private String applicantName;
    private String currentStatus;
    private String message;
    private Date occurredAt;

    public LoanEvent() {}

    public LoanEvent(String eventType, String applicationNumber, Long applicationId,
                     String applicantEmail, String applicantName, String status, String message) {
        this.eventType = eventType;
        this.applicationNumber = applicationNumber;
        this.applicationId = applicationId;
        this.applicantEmail = applicantEmail;
        this.applicantName = applicantName;
        this.currentStatus = status;
        this.message = message;
        this.occurredAt = new Date();
    }

    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }
    public String getApplicationNumber() { return applicationNumber; }
    public void setApplicationNumber(String applicationNumber) { this.applicationNumber = applicationNumber; }
    public Long getApplicationId() { return applicationId; }
    public void setApplicationId(Long applicationId) { this.applicationId = applicationId; }
    public String getApplicantEmail() { return applicantEmail; }
    public void setApplicantEmail(String applicantEmail) { this.applicantEmail = applicantEmail; }
    public String getApplicantName() { return applicantName; }
    public void setApplicantName(String applicantName) { this.applicantName = applicantName; }
    public String getCurrentStatus() { return currentStatus; }
    public void setCurrentStatus(String currentStatus) { this.currentStatus = currentStatus; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public Date getOccurredAt() { return occurredAt; }
    public void setOccurredAt(Date occurredAt) { this.occurredAt = occurredAt; }
}
