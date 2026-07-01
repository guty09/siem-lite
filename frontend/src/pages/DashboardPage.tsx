import { useEffect, useState } from "react";
import {
    Alert,
    Box,
    Card,
    CardContent,
    CircularProgress,
    LinearProgress,
    Stack,
    Typography,
} from "@mui/material";

import AppLayout from "../components/layout/AppLayout";

import {
    getDashboardSummary,
    getDashboardTrends,
    getDashboardTimeline,
    getTopAlertTypes,
    getTopSourceIps,
    getTopUsernames,
    getRiskDistribution,
    getMitreStatistics,
    getIocStatistics,
} from "../services/dashboard";

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

export default function DashboardPage() {
    const [summary, setSummary] = useState<DashboardSummary | null>(null);
    const [trends, setTrends] = useState<DashboardTrends | null>(null);
    const [timeline, setTimeline] = useState<DashboardTimelinePoint[]>([]);
    const [topAlertTypes, setTopAlertTypes] = useState<TopAlertType[]>([]);
    const [topSourceIps, setTopSourceIps] = useState<TopSourceIp[]>([]);
    const [topUsernames, setTopUsernames] = useState<TopUsername[]>([]);
    const [riskDistribution, setRiskDistribution] = useState<RiskDistribution | null>(null);
    const [mitreStats, setMitreStats] = useState<MitreStatistic[]>([]);
    const [iocStats, setIocStats] = useState<IocStatistics | null>(null);

    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

    useEffect(() => {
        async function loadDashboard() {
            try {
                const [
                    summaryData,
                    trendsData,
                    timelineData,
                    alertTypesData,
                    sourceIpsData,
                    usernamesData,
                    riskData,
                    mitreData,
                    iocData,
                ] = await Promise.all([
                    getDashboardSummary(),
                    getDashboardTrends(),
                    getDashboardTimeline(7),
                    getTopAlertTypes(),
                    getTopSourceIps(),
                    getTopUsernames(),
                    getRiskDistribution(),
                    getMitreStatistics(),
                    getIocStatistics(),
                ]);

                setSummary(summaryData);
                setTrends(trendsData);
                setTimeline(timelineData);
                setTopAlertTypes(alertTypesData);
                setTopSourceIps(sourceIpsData);
                setTopUsernames(usernamesData);
                setRiskDistribution(riskData);
                setMitreStats(mitreData);
                setIocStats(iocData);
                setError("");
            } catch (err) {
                console.error("Dashboard error:", err);
                setError("Failed to load dashboard metrics.");
            } finally {
                setLoading(false);
            }
        }

        loadDashboard();
    }, []);

    return (
        <AppLayout title="Security Operations Dashboard">
            <Typography variant="body1" sx={{ color: "#9ca3af", mb: 4 }}>
                Real-time visibility into alerts, events, detections, IOC activity, MITRE techniques, and SOC risk.
            </Typography>

            {loading && <CircularProgress />}

            {error && (
                <Alert severity="error" sx={{ mb: 3 }}>
                    {error}
                </Alert>
            )}

            {!loading && !error && summary && (
                <Stack spacing={3}>
                    <Box
                        sx={{
                            display: "grid",
                            gridTemplateColumns: {
                                xs: "1fr",
                                sm: "repeat(2, minmax(0, 1fr))",
                                lg: "repeat(4, minmax(0, 1fr))",
                            },
                            gap: 3,
                        }}
                    >
                        <SummaryCard title="Total Alerts" value={summary.totalAlerts} />
                        <SummaryCard title="Critical Alerts" value={summary.criticalAlerts} />
                        <SummaryCard title="High Alerts" value={summary.highAlerts} />
                        <SummaryCard title="Total Events" value={summary.totalEvents} />
                        <SummaryCard title="Failed Logins" value={summary.failedLogins} />
                        <SummaryCard title="Port Scans" value={summary.portScans} />
                        <SummaryCard title="Privilege Escalations" value={summary.privilegeEscalations} />
                        <SummaryCard title="Highest Risk Score" value={summary.highestRiskScore} />
                    </Box>

                    {trends && (
                        <SectionCard title="Trends">
                            <Box
                                sx={{
                                    display: "grid",
                                    gridTemplateColumns: {
                                        xs: "1fr",
                                        md: "repeat(3, minmax(0, 1fr))",
                                    },
                                    gap: 2,
                                }}
                            >
                                <MiniMetric title="Alerts / 24h" value={trends.alertsLast24Hours} />
                                <MiniMetric title="Alerts / 7d" value={trends.alertsLast7Days} />
                                <MiniMetric title="Alerts / 30d" value={trends.alertsLast30Days} />
                                <MiniMetric title="Events / 24h" value={trends.eventsLast24Hours} />
                                <MiniMetric title="Events / 7d" value={trends.eventsLast7Days} />
                                <MiniMetric title="Events / 30d" value={trends.eventsLast30Days} />
                            </Box>
                        </SectionCard>
                    )}

                    <Box
                        sx={{
                            display: "grid",
                            gridTemplateColumns: {
                                xs: "1fr",
                                lg: "repeat(2, minmax(0, 1fr))",
                            },
                            gap: 3,
                        }}
                    >
                        <ListCard
                            title="Top Alert Types"
                            items={topAlertTypes.map((item) => ({
                                label: item.alertType,
                                value: item.count,
                            }))}
                        />

                        <ListCard
                            title="Top Source IPs"
                            items={topSourceIps.map((item) => ({
                                label: item.sourceIp,
                                value: item.count,
                            }))}
                        />

                        <ListCard
                            title="Top Usernames"
                            items={topUsernames.map((item) => ({
                                label: item.username,
                                value: item.count,
                            }))}
                        />

                        <ListCard
                            title="MITRE ATT&CK Statistics"
                            items={mitreStats.map((item) => ({
                                label: item.mitreTechnique,
                                value: item.count,
                            }))}
                        />
                    </Box>

                    {riskDistribution && (
                        <SectionCard title="Risk Distribution">
                            <Stack spacing={2}>
                                <RiskBar label="Critical" value={riskDistribution.critical} total={summary.totalAlerts} />
                                <RiskBar label="High" value={riskDistribution.high} total={summary.totalAlerts} />
                                <RiskBar label="Medium" value={riskDistribution.medium} total={summary.totalAlerts} />
                                <RiskBar label="Low" value={riskDistribution.low} total={summary.totalAlerts} />
                            </Stack>
                        </SectionCard>
                    )}

                    <Box
                        sx={{
                            display: "grid",
                            gridTemplateColumns: {
                                xs: "1fr",
                                lg: "repeat(2, minmax(0, 1fr))",
                            },
                            gap: 3,
                        }}
                    >
                        <ListCard
                            title="7-Day Timeline"
                            items={timeline.map((item) => ({
                                label: item.date,
                                value: `A:${item.alertCount} / E:${item.eventCount}`,
                            }))}
                        />

                        {iocStats && (
                            <SectionCard title="IOC Statistics">
                                <Stack spacing={2}>
                                    <MiniMetric title="Total IOC Alerts" value={iocStats.totalIocAlerts} />
                                    <MiniMetric title="Enabled IOCs" value={iocStats.enabledIocs} />
                                    <MiniMetric title="Disabled IOCs" value={iocStats.disabledIocs} />
                                </Stack>
                            </SectionCard>
                        )}
                    </Box>
                </Stack>
            )}
        </AppLayout>
    );
}

function SummaryCard({ title, value }: { title: string; value: number }) {
    return (
        <Card
            sx={{
                bgcolor: "#111827",
                color: "#e5e7eb",
                border: "1px solid #1f2937",
                borderRadius: 3,
            }}
            elevation={0}
        >
            <CardContent>
                <Typography variant="body2" sx={{ color: "#9ca3af", mb: 1 }}>
                    {title}
                </Typography>

                <Typography variant="h4" sx={{ fontWeight: 800 }}>
                    {value}
                </Typography>
            </CardContent>
        </Card>
    );
}

function SectionCard({
                         title,
                         children,
                     }: {
    title: string;
    children: React.ReactNode;
}) {
    return (
        <Card
            sx={{
                bgcolor: "#111827",
                color: "#e5e7eb",
                border: "1px solid #1f2937",
                borderRadius: 3,
            }}
            elevation={0}
        >
            <CardContent>
                <Typography variant="h6" sx={{ fontWeight: 800, mb: 2 }}>
                    {title}
                </Typography>

                {children}
            </CardContent>
        </Card>
    );
}

function MiniMetric({ title, value }: { title: string; value: number }) {
    return (
        <Box
            sx={{
                p: 2,
                border: "1px solid #1f2937",
                borderRadius: 2,
                bgcolor: "#0f172a",
            }}
        >
            <Typography variant="body2" sx={{ color: "#9ca3af" }}>
                {title}
            </Typography>

            <Typography variant="h5" sx={{ fontWeight: 800 }}>
                {value}
            </Typography>
        </Box>
    );
}

function ListCard({
                      title,
                      items,
                  }: {
    title: string;
    items: Array<{ label: string; value: string | number }>;
}) {
    return (
        <SectionCard title={title}>
            {items.length === 0 ? (
                <Typography sx={{ color: "#9ca3af" }}>No data available.</Typography>
            ) : (
                <Stack spacing={1.5}>
                    {items.map((item, index) => (
                        <Box
                            key={`${item.label}-${index}`}
                            sx={{
                                display: "flex",
                                justifyContent: "space-between",
                                gap: 2,
                                p: 1.5,
                                borderRadius: 2,
                                bgcolor: "#0f172a",
                                border: "1px solid #1f2937",
                            }}
                        >
                            <Typography sx={{ overflowWrap: "anywhere" }}>
                                {item.label}
                            </Typography>

                            <Typography sx={{ fontWeight: 800 }}>
                                {item.value}
                            </Typography>
                        </Box>
                    ))}
                </Stack>
            )}
        </SectionCard>
    );
}

function RiskBar({
                     label,
                     value,
                     total,
                 }: {
    label: string;
    value: number;
    total: number;
}) {
    const percent = total > 0 ? Math.round((value / total) * 100) : 0;

    return (
        <Box>
            <Box sx={{ display: "flex", justifyContent: "space-between", mb: 0.5 }}>
                <Typography>{label}</Typography>
                <Typography sx={{ color: "#9ca3af" }}>
                    {value} / {percent}%
                </Typography>
            </Box>

            <LinearProgress
                variant="determinate"
                value={percent}
                sx={{
                    height: 8,
                    borderRadius: 999,
                    bgcolor: "#1f2937",
                }}
            />
        </Box>
    );
}