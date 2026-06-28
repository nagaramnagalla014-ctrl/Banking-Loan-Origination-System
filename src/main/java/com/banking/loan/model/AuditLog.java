package com.banking.loan.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "audit_logs")
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "audit_seq")
    @SequenceGenerator(name = "audit_seq", sequenceName = "audit_seq", allocationSize = 1)
    private Long logId;

    @Column(length = 20)
    private String entityType;

    @Column
    private Long entityId;

    @Column(nullable = false, length = 100)
    private String action;

    @Column(length = 30)
    private String fromStatus;

    @Column(length = 30)
    private String toStatus;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "performed_by")
    private User performedBy;

    @Column(columnDefinition = "CLOB")
    private String details;

    @Column(length = 50)
    private String ipAddress;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable = false)
    private Date performedAt;

    @PrePersist
    protected void onCreate() { performedAt = new Date(); }

    public Long getLogId() { return logId; }
    public void setLogId(Long logId) { this.logId = logId; }
    public String getEntityType() { return entityType; }
    public void setEntityType(String entityType) { this.entityType = entityType; }
    public Long getEntityId() { return entityId; }
    public void setEntityId(Long entityId) { this.entityId = entityId; }
    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }
    public String getFromStatus() { return fromStatus; }
    public void setFromStatus(String fromStatus) { this.fromStatus = fromStatus; }
    public String getToStatus() { return toStatus; }
    public void setToStatus(String toStatus) { this.toStatus = toStatus; }
    public User getPerformedBy() { return performedBy; }
    public void setPerformedBy(User performedBy) { this.performedBy = performedBy; }
    public String getDetails() { return details; }
    public void setDetails(String details) { this.details = details; }
    public String getIpAddress() { return ipAddress; }
    public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }
    public Date getPerformedAt() { return performedAt; }
}
