import {
    Button,
    Chip,
    Paper,
    Table,
    TableBody,
    TableCell,
    TableContainer,
    TableHead,
    TableRow,
    Typography,
} from "@mui/material";

import type { Incident } from "../../types/incident";

interface Props {
    incidents: Incident[];
    onInvestigate: (incident: Incident) => void;
}

function getSeverityColor(severity: string) {
    switch (severity?.toUpperCase()) {
        case "CRITICAL":
            return "error";
        case "HIGH":
            return "warning";
        case "MEDIUM":
            return "info";
        case "LOW":
            return "success";
        default:
            return "default";
    }
}

function getStatusColor(status: string) {
    switch (status?.toUpperCase()) {
        case "OPEN":
            return "error";
        case "IN_PROGRESS":
            return "warning";
        case "CLOSED":
            return "success";
        default:
            return "default";
    }
}

function formatDate(value: string) {
    if (!value) return "N/A";
    return new Date(value).toLocaleString();
}

export default function IncidentTable({ incidents, onInvestigate }: Props) {
    if (incidents.length === 0) {
        return (
            <Paper sx={{ p: 3, bgcolor: "#111827", color: "#e5e7eb" }}>
                <Typography>No incidents found.</Typography>
            </Paper>
        );
    }

    return (
        <TableContainer
            component={Paper}
            sx={{
                bgcolor: "#111827",
                border: "1px solid #1f2937",
            }}
        >
            <Table>
                <TableHead>
                    <TableRow>
                        <TableCell sx={{ color: "#9ca3af" }}>Severity</TableCell>
                        <TableCell sx={{ color: "#9ca3af" }}>Status</TableCell>
                        <TableCell sx={{ color: "#9ca3af" }}>Risk</TableCell>
                        <TableCell sx={{ color: "#9ca3af" }}>
                            Incident Type
                        </TableCell>
                        <TableCell sx={{ color: "#9ca3af" }}>Source IP</TableCell>
                        <TableCell sx={{ color: "#9ca3af" }}>
                            Assigned Analyst
                        </TableCell>
                        <TableCell sx={{ color: "#9ca3af" }}>Timestamp</TableCell>
                        <TableCell sx={{ color: "#9ca3af" }} align="right">
                            Action
                        </TableCell>
                    </TableRow>
                </TableHead>

                <TableBody>
                    {incidents.map((incident) => (
                        <TableRow key={incident.id} hover>
                            <TableCell>
                                <Chip
                                    label={incident.severity}
                                    color={getSeverityColor(incident.severity)}
                                    size="small"
                                />
                            </TableCell>

                            <TableCell>
                                <Chip
                                    label={incident.status}
                                    color={getStatusColor(incident.status)}
                                    size="small"
                                />
                            </TableCell>

                            <TableCell sx={{ color: "#e5e7eb" }}>
                                {incident.riskScore}
                            </TableCell>

                            <TableCell sx={{ color: "#e5e7eb" }}>
                                {incident.incidentType}
                            </TableCell>

                            <TableCell sx={{ color: "#e5e7eb" }}>
                                {incident.sourceIp}
                            </TableCell>

                            <TableCell sx={{ color: "#e5e7eb" }}>
                                {incident.assignedAnalyst || "Unassigned"}
                            </TableCell>

                            <TableCell sx={{ color: "#e5e7eb" }}>
                                {formatDate(incident.timestamp)}
                            </TableCell>

                            <TableCell align="right">
                                <Button
                                    variant="contained"
                                    size="small"
                                    onClick={() => onInvestigate(incident)}
                                >
                                    Investigate
                                </Button>
                            </TableCell>
                        </TableRow>
                    ))}
                </TableBody>
            </Table>
        </TableContainer>
    );
}