import { useEffect, useState } from "react";
import {
    Alert,
    Box,
    Card,
    CardContent,
    CircularProgress,
    Typography,
} from "@mui/material";

import AppLayout from "../components/layout/AppLayout";
import {
    getDashboardSummary,
    type DashboardSummary,
} from "../services/dashboard";

export default function DashboardPage() {
    const [summary, setSummary] = useState<DashboardSummary | null>(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

    useEffect(() => {
        getDashboardSummary()
            .then((data) => {
                console.log("Dashboard summary:", data);
                setSummary(data);
                setError("");
            })
            .catch((err) => {
                console.error("Dashboard error:", err);
                setError("Failed to load dashboard metrics.");
            })
            .finally(() => {
                setLoading(false);
            });
    }, []);

    return (
        <AppLayout title="Security Operations Dashboard">
            <Typography variant="body1" sx={{ color: "#9ca3af", mb: 4 }}>
                Real-time visibility into alerts, events, detections, and audit activity.
            </Typography>

            {loading && <CircularProgress />}

            {error && (
                <Alert severity="error" sx={{ mb: 3 }}>
                    {error}
                </Alert>
            )}

            {!loading && !error && summary && (
                <Box
                    sx={{
                        display: "grid",
                        gridTemplateColumns: "repeat(4, minmax(0, 1fr))",
                        gap: 3,
                    }}
                >
                    <SummaryCard title="Total Alerts" value={summary.totalAlerts} />
                    <SummaryCard title="Critical Alerts" value={summary.criticalAlerts} />
                    <SummaryCard title="High Alerts" value={summary.highAlerts} />
                    <SummaryCard title="Total Events" value={summary.totalEvents} />
                </Box>
            )}
        </AppLayout>
    );
}

function SummaryCard({
                         title,
                         value,
                     }: {
    title: string;
    value: number;
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