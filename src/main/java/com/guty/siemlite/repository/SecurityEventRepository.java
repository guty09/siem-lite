package com.guty.siemlite.repository;

import com.guty.siemlite.model.SecurityEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface SecurityEventRepository extends JpaRepository<SecurityEvent, Long> {

    List<SecurityEvent> findBySourceIpAndEventType(
            String sourceIp,
            String eventType);

    List<SecurityEvent> findBySourceIpAndEventTypeAndTimestampAfter(
            String sourceIp,
            String eventType,
            LocalDateTime timestamp);

}
