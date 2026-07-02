import { useEffect, useState } from "react";
import {
    Box,
    Button,
    Chip,
    Divider,
    Drawer,
    MenuItem,
    Stack,
    TextField,
    Typography,
} from "@mui/material";

import type { Incident, IncidentStatus } from "../../types/incident";

interface Props {
    open: boolean;
    incident: Incident | null;
    onClose: () => void;
    onAssign: (assignedAnalyst: string) => void;
    onStatusChange: (status: IncidentStatus) => void;
    onNotesSave: (notes: string) => void;
}

const statuses: IncidentStatus[] = ["OPEN", "IN_PROGRESS", "CLOSED"];

function formatDate(value: string | null | undefined) {
    if (!value) return "N/A";
    return new Date(value).toLocaleString();
}

export default function IncidentDetailsDrawer({
                                                  open,
                                                  incident,
                                                  onClose,
                                                  onAssign,
                                                  onStatusChange,
                                                  onNotesSave,
                                              }: Props) {
    const [assignedAnalyst, setAssignedAnalyst] = useState("");
    const [status, setStatus] = useState<IncidentStatus>("OPEN");
    const [notes, setNotes] = useState("");

    useEffect(() => {
        if (incident) {
            setAssignedAnalyst(incident.assignedAnalyst || "");
            setStatus((incident.status as IncidentStatus) || "OPEN");
            setNotes(incident.notes || "");
        }
    }, [incident]);

    if (!incident) {
        return null;
    }

    return (
        <Drawer anchor="right" open={open} onClose={onClose}>
            <Box
                sx={{
                    width: 480,
                    p: 3,
                    bgcolor: "#0f172a",
                    color: "#e5e7eb",
                    minHeight: "100vh",
                }}
            >
                <Typography variant="h5" sx={{ fontWeight: 800, mb: 1 }}>
                    Incident Investigation
                </Typography>

                <Typography variant="body2" sx={{ color: "#9ca3af", mb: 3 }}>
                    Incident #{incident.id}
                </Typography>

                <Stack direction="row" spacing={1} sx={{ mb: 3 }}>
                    <Chip label={incident.severity} color="warning" />
                    <Chip label={incident.status} color="info" />
                    <Chip label={`Risk ${incident.riskScore}`} />
                </Stack>

                <Divider sx={{ borderColor: "#1f2937", mb: 3 }} />

                <Typography variant="h6" sx={{ mb: 2 }}>
                    Overview
                </Typography>

                <Stack spacing={1.5} sx={{ mb: 3 }}>
                    <Typography>
                        <strong>Type:</strong> {incident.incidentType}
                    </Typography>
                    <Typography>
                        <strong>Source IP:</strong> {incident.sourceIp}
                    </Typography>
                    <Typography>
                        <strong>Timestamp:</strong> {formatDate(incident.timestamp)}
                    </Typography>
                    <Typography>
                        <strong>Created:</strong> {formatDate(incident.createdAt)}
                    </Typography>
                    <Typography>
                        <strong>Updated:</strong> {formatDate(incident.updatedAt)}
                    </Typography>
                </Stack>

                <Typography variant="h6" sx={{ mb: 1 }}>
                    Summary
                </Typography>

                <Typography
                    sx={{
                        color: "#d1d5db",
                        bgcolor: "#111827",
                        border: "1px solid #1f2937",
                        borderRadius: 1,
                        p: 2,
                        mb: 3,
                    }}
                >
                    {incident.summary}
                </Typography>

                <Divider sx={{ borderColor: "#1f2937", mb: 3 }} />

                <Typography variant="h6" sx={{ mb: 2 }}>
                    Analyst Workflow
                </Typography>

                <Stack spacing={2}>
                    <TextField
                        label="Assigned Analyst"
                        value={assignedAnalyst}
                        onChange={(event) => setAssignedAnalyst(event.target.value)}
                        fullWidth
                    />

                    <Button
                        variant="contained"
                        onClick={() => onAssign(assignedAnalyst)}
                        disabled={!assignedAnalyst.trim()}
                    >
                        Save Assignment
                    </Button>

                    <TextField
                        select
                        label="Status"
                        value={status}
                        onChange={(event) =>
                            setStatus(event.target.value as IncidentStatus)
                        }
                        fullWidth
                    >
                        {statuses.map((item) => (
                            <MenuItem key={item} value={item}>
                                {item}
                            </MenuItem>
                        ))}
                    </TextField>

                    <Button
                        variant="contained"
                        onClick={() => onStatusChange(status)}
                    >
                        Save Status
                    </Button>

                    <TextField
                        label="Investigation Notes"
                        value={notes}
                        onChange={(event) => setNotes(event.target.value)}
                        multiline
                        minRows={5}
                        fullWidth
                    />

                    <Button
                        variant="contained"
                        onClick={() => onNotesSave(notes)}
                    >
                        Save Notes
                    </Button>

                    <Button variant="outlined" onClick={onClose}>
                        Close
                    </Button>
                </Stack>
            </Box>
        </Drawer>
    );
}