package com.guty.siemlite.dto;

import java.time.LocalDate;

/**
 * Represents event and alert activity for a single dashboard timeline date.
 *
 * @param date   timeline date
 * @param events number of security events created on the date
 * @param alerts number of alerts created on the date
 */
public record DashboardTimelinePoint(
        LocalDate date,
        long events,
        long alerts
) {
}
