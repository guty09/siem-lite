package com.guty.siemlite.audit.repository;

import com.guty.siemlite.audit.model.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
}
