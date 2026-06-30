import api from "./api";

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

export async function getDashboardSummary(): Promise<DashboardSummary> {
    const response = await api.get<DashboardSummary>("/dashboard");
    return response.data;
}