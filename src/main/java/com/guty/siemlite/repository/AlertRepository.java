package com.guty.siemlite.repository;

import com.guty.siemlite.model.Alert;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlertRepository extends JpaRepository<Alert, Long> {

    List<Alert> findBySourceIpAndAlertType(
            String sourceIp,
            String alertType);

}
