export interface DashboardSummary {
    averageRiskScore: number;
    criticalAlerts: number;
    criticalRiskAlertCount: number;
    failedLogins: number;
    highAlerts: number;
    highRiskAlertCount: number;
    highestRiskScore: number;
    portScans: number;
    privilegeEscalations: number;
    totalAlerts: number;
    totalEvents: number;
    totalRiskScore: number;
}

export interface DashboardTrends {
    alertsLast24Hours: number;
    alertsLast7Days: number;
    alertsLast30Days: number;
    eventsLast24Hours: number;
    eventsLast7Days: number;
    eventsLast30Days: number;
}

export interface TopAlertType {
    alertType: string;
    count: number;
}

export interface TopSourceIp {
    sourceIp: string;
    count: number;
}

export interface TopUsername {
    username: string;
    count: number;
}

export interface RiskDistribution {
    low: number;
    medium: number;
    high: number;
    critical: number;
}

export interface MitreStatistic {
    mitreTechnique: string;
    count: number;
}

export interface DashboardTimelinePoint {
    date: string;
    alertCount: number;
    eventCount: number;
}

export interface IocStatistics {
    totalIocAlerts: number;
    enabledIocs: number;
    disabledIocs: number;
}