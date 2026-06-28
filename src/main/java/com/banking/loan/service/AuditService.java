package com.banking.loan.service;

import com.banking.loan.model.AuditLog;
import com.banking.loan.model.User;
import com.banking.loan.repository.AuditLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class AuditService {

    @Autowired private AuditLogRepository auditLogRepository;

    @Async
    @Transactional
    public void log(String entityType, Long entityId, String action,
                    String fromStatus, String toStatus, User performedBy, String details) {
        AuditLog log = new AuditLog();
        log.setEntityType(entityType);
        log.setEntityId(entityId);
        log.setAction(action);
        log.setFromStatus(fromStatus);
        log.setToStatus(toStatus);
        log.setPerformedBy(performedBy);
        log.setDetails(details);
        auditLogRepository.save(log);
    }

    @Transactional(readOnly = true)
    public List<AuditLog> getLogsForApplication(Long applicationId) {
        return auditLogRepository.findByEntityTypeAndEntityIdOrderByPerformedAtDesc("LOAN_APPLICATION", applicationId);
    }

    @Transactional(readOnly = true)
    public List<AuditLog> getAllLogs() {
        return auditLogRepository.findAllByOrderByPerformedAtDesc();
    }
}
