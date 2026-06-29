package com.guty.siemlite.audit.service;

import com.guty.siemlite.audit.model.AuditAction;
import com.guty.siemlite.audit.model.AuditLog;
import com.guty.siemlite.audit.repository.AuditLogRepository;
import org.springframework.stereotype.Service;

@Service
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;

    public AuditLogService(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    public void log(AuditAction action, String username, String details) {
        AuditLog auditLog = new AuditLog(action, username, details);
        auditLogRepository.save(auditLog);
    }
}
