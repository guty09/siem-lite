import {
    Box,
    Button,
    Chip,
    Divider,
    Drawer,
    IconButton,
    MenuItem,
    Select,
    Stack,
    TextField,
    Typography,
} from "@mui/material";

import CloseIcon from "@mui/icons-material/Close";
import SearchIcon from "@mui/icons-material/Search";
import type { Alert } from "../types/alert";

interface Props {
    open: boolean;
    alert: Alert | null;
    notes: string;
    onNotesChange: (value: string) => void;
    onClose: () => void;
    onStatusChange: (id: number, status: string) => Promise<void>;
    onSaveNotes: () => Promise<void>;
    onOpenAssign: (alert: Alert) => void;
    onPivot: (field: "sourceIp" | "username" | "mitreTechnique", value: string) => void;
}

export default function AlertDetailsDrawer({
                                               open,
                                               alert,
                                               notes,
                                               onNotesChange,
                                               onClose,
                                               onStatusChange,
                                               onSaveNotes,
                                               onOpenAssign,
                                               onPivot,
                                           }: Props) {
    if (!alert) {
        return null;
    }

    const alertType = alert.alertType || alert.type || "Unknown Alert";

    return (
        <Drawer
            anchor="right"
            open={open}
            onClose={onClose}
            slotProps={{
                paper: {
                    sx: {
                        width: {
                            xs: "100vw",
                            sm: 480,
                        },
                        maxWidth: "100vw",
                        bgcolor: "#111827",
                        color: "#e5e7eb",
                        borderLeft: "1px solid #1f2937",
                        overflowX: "hidden",
                        boxSizing: "border-box",
                    },
                },
            }}
        >
            <Box
                sx={{
                    p: 3,
                    width: "100%",
                    maxWidth: "100%",
                    boxSizing: "border-box",
                    overflowX: "hidden",
                }}
            >
                <Box sx={{ display: "flex", alignItems: "center", mb: 2 }}>
                    <Box sx={{ flexGrow: 1, minWidth: 0 }}>
                        <Typography variant="h6" sx={{ fontWeight: 900 }}>
                            {alertType}
                        </Typography>

                        <Typography variant="caption" sx={{ color: "#9ca3af" }}>
                            Alert ID #{alert.id}
                        </Typography>
                    </Box>

                    <IconButton onClick={onClose} sx={{ color: "#e5e7eb" }}>
                        <CloseIcon />
                    </IconButton>
                </Box>

                <Stack direction="row" spacing={1} sx={{ mb: 3 }}>
                    <Chip
                        label={alert.severity ?? "UNKNOWN"}
                        size="small"
                        color={getSeverityColor(alert.severity)}
                    />

                    <Chip
                        label={`Risk ${alert.riskScore ?? 0}`}
                        size="small"
                        variant="outlined"
                        sx={{ color: "#e5e7eb", borderColor: "#374151" }}
                    />
                </Stack>

                <Divider sx={{ borderColor: "#1f2937", mb: 3 }} />

                <Stack spacing={2.5}>
                    <Box>
                        <Typography variant="caption" sx={{ color: "#9ca3af" }}>
                            Status
                        </Typography>

                        <Select
                            fullWidth
                            size="small"
                            value={alert.status ?? "OPEN"}
                            onChange={(event) =>
                                onStatusChange(alert.id, event.target.value)
                            }
                            sx={{
                                mt: 0.5,
                                color: "#e5e7eb",
                                ".MuiOutlinedInput-notchedOutline": {
                                    borderColor: "#374151",
                                },
                                ".MuiSvgIcon-root": {
                                    color: "#e5e7eb",
                                },
                            }}
                        >
                            <MenuItem value="OPEN">OPEN</MenuItem>
                            <MenuItem value="INVESTIGATING">INVESTIGATING</MenuItem>
                            <MenuItem value="CLOSED">CLOSED</MenuItem>
                        </Select>
                    </Box>

                    <Box>
                        <Typography variant="caption" sx={{ color: "#9ca3af" }}>
                            Assigned Analyst
                        </Typography>

                        <Box
                            sx={{
                                display: "flex",
                                alignItems: "center",
                                gap: 1,
                                mt: 0.5,
                            }}
                        >
                            <Typography sx={{ flexGrow: 1, fontWeight: 700 }}>
                                {alert.assignedAnalyst || "Unassigned"}
                            </Typography>

                            <Button
                                size="small"
                                variant="outlined"
                                onClick={() => onOpenAssign(alert)}
                            >
                                Assign
                            </Button>
                        </Box>
                    </Box>

                    <PivotDetail
                        label="MITRE Technique"
                        value={alert.mitreTechnique || "N/A"}
                        onClick={() =>
                            alert.mitreTechnique &&
                            onPivot("mitreTechnique", alert.mitreTechnique)
                        }
                    />

                    <Detail
                        label="MITRE Description"
                        value={alert.mitreDescription || "N/A"}
                    />

                    <PivotDetail
                        label="Source IP"
                        value={alert.sourceIp || "N/A"}
                        onClick={() =>
                            alert.sourceIp && onPivot("sourceIp", alert.sourceIp)
                        }
                    />

                    <PivotDetail
                        label="Username"
                        value={alert.username || "N/A"}
                        onClick={() =>
                            alert.username && onPivot("username", alert.username)
                        }
                    />

                    <Detail
                        label="Event Type"
                        value={alert.eventType || "N/A"}
                    />

                    <Detail
                        label="Created"
                        value={
                            alert.createdAt
                                ? new Date(alert.createdAt).toLocaleString()
                                : "N/A"
                        }
                    />

                    <Divider sx={{ borderColor: "#1f2937" }} />

                    <Box>
                        <Typography variant="caption" sx={{ color: "#9ca3af" }}>
                            Detection Summary
                        </Typography>

                        <Typography sx={{ mt: 0.5 }}>
                            {alert.message ||
                                alert.description ||
                                "No detection summary available."}
                        </Typography>
                    </Box>

                    <Divider sx={{ borderColor: "#1f2937" }} />

                    <Box sx={{ width: "100%", maxWidth: "100%", boxSizing: "border-box" }}>
                        <Typography variant="caption" sx={{ color: "#9ca3af" }}>
                            Investigation Notes
                        </Typography>

                        <TextField
                            fullWidth
                            multiline
                            minRows={4}
                            value={notes}
                            onChange={(event) => onNotesChange(event.target.value)}
                            sx={{
                                mt: 1,
                                width: "100%",
                                maxWidth: "100%",
                                boxSizing: "border-box",
                                "& .MuiOutlinedInput-root": {
                                    bgcolor: "#0f172a",
                                    color: "#e5e7eb",
                                    width: "100%",
                                    maxWidth: "100%",
                                    boxSizing: "border-box",
                                },
                                "& textarea": {
                                    color: "#e5e7eb",
                                },
                                "& .MuiOutlinedInput-notchedOutline": {
                                    borderColor: "#374151",
                                },
                            }}
                        />

                        <Button
                            fullWidth
                            variant="contained"
                            sx={{ mt: 2 }}
                            onClick={onSaveNotes}
                        >
                            Save Notes
                        </Button>
                    </Box>
                </Stack>
            </Box>
        </Drawer>
    );
}

function Detail({ label, value }: { label: string; value: string | number }) {
    return (
        <Box>
            <Typography variant="caption" sx={{ color: "#9ca3af" }}>
                {label}
            </Typography>

            <Typography sx={{ fontWeight: 700 }}>
                {value}
            </Typography>
        </Box>
    );
}

function PivotDetail({
                         label,
                         value,
                         onClick,
                     }: {
    label: string;
    value: string;
    onClick: () => void;
}) {
    const disabled = value === "N/A";

    return (
        <Box>
            <Typography variant="caption" sx={{ color: "#9ca3af" }}>
                {label}
            </Typography>

            <Box sx={{ display: "flex", alignItems: "center", gap: 1 }}>
                <Typography sx={{ flexGrow: 1, fontWeight: 700 }}>
                    {value}
                </Typography>

                <IconButton
                    size="small"
                    disabled={disabled}
                    onClick={onClick}
                    sx={{ color: "#93c5fd" }}
                >
                    <SearchIcon fontSize="small" />
                </IconButton>
            </Box>
        </Box>
    );
}

function getSeverityColor(
    severity?: string
): "default" | "error" | "warning" | "info" | "success" {
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