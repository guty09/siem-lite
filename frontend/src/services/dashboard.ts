import api from "./api";

import type {
    DashboardSummary,
    DashboardTimelinePoint,
    DashboardTrends,
    IocStatistics,
    MitreStatistic,
    RiskDistribution,
    TopAlertType,
    TopSourceIp,
    TopUsername,
} from "../types/dashboard";

export async function getDashboardSummary(): Promise<DashboardSummary> {
    const response = await api.get<DashboardSummary>("/dashboard");
    return response.data;
}

export async function getDashboardTrends(): Promise<DashboardTrends> {
    const response = await api.get<DashboardTrends>("/dashboard/trends");
    return response.data;
}

export async function getTopAlertTypes(): Promise<TopAlertType[]> {
    const response = await api.get<TopAlertType[]>("/dashboard/top-alert-types");
    return response.data;
}

export async function getTopSourceIps(): Promise<TopSourceIp[]> {
    const response = await api.get<TopSourceIp[]>("/dashboard/top-source-ips");
    return response.data;
}

export async function getTopUsernames(): Promise<TopUsername[]> {
    const response = await api.get<TopUsername[]>("/dashboard/top-usernames");
    return response.data;
}

export async function getRiskDistribution(): Promise<RiskDistribution> {
    const response = await api.get<RiskDistribution>("/dashboard/risk-distribution");
    return response.data;
}

export async function getMitreStatistics(): Promise<MitreStatistic[]> {
    const response = await api.get<MitreStatistic[]>("/dashboard/mitre-statistics");
    return response.data;
}

export async function getDashboardTimeline(
    days = 7
): Promise<DashboardTimelinePoint[]> {
    const response = await api.get<DashboardTimelinePoint[]>(
        `/dashboard/timeline?days=${days}`
    );
    return response.data;
}

export async function getIocStatistics(): Promise<IocStatistics> {
    const response = await api.get<IocStatistics>("/dashboard/ioc-statistics");
    return response.data;
}