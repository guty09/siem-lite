import { useEffect, useMemo, useState } from "react";
import { useNavigate, useSearchParams } from "react-router-dom";
import {
    Box,
    Button,
    Chip,
    CircularProgress,
    Divider,
    Drawer,
    IconButton,
    Link,
    Paper,
    Stack,
    Table,
    TableBody,
    TableCell,
    TableContainer,
    TableHead,
    TableRow,
    TextField,
    Typography,
} from "@mui/material";
import CloseIcon from "@mui/icons-material/Close";
import VisibilityIcon from "@mui/icons-material/Visibility";

import AppLayout from "../components/layout/AppLayout";
import { getEvents } from "../services/events";
import type { SecurityEvent } from "../types/event";

export default function EventsPage() {
    const [searchParams] = useSearchParams();
    const navigate = useNavigate();

    const [events, setEvents] = useState<SecurityEvent[]>([]);
    const [selectedEvent, setSelectedEvent] = useState<SecurityEvent | null>(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");
    const [search, setSearch] = useState("");

    const sourceIpFilter = searchParams.get("sourceIp") ?? "";
    const usernameFilter = searchParams.get("username") ?? "";
    const mitreFilter = searchParams.get("mitreTechnique") ?? "";

    useEffect(() => {
        async function loadEvents() {
            try {
                const data = await getEvents();
                setEvents(data);
                setError("");
            } catch {
                setError("Failed to load events.");
            } finally {
                setLoading(false);
            }
        }

        loadEvents();
    }, []);

    const filteredEvents = useMemo(() => {
        return events.filter((event) => {
            const eventType = event.eventType ?? event.type ?? "";
            const sourceIp = event.sourceIp ?? "";
            const username = event.username ?? "";
            const mitreTechnique = event.mitreTechnique ?? "";
            const message = event.message ?? "";
            const rawLog = event.rawLog ?? "";

            const matchesSourceIp =
                sourceIpFilter === "" || sourceIp === sourceIpFilter;

            const matchesUsername =
                usernameFilter === "" || username === usernameFilter;

            const matchesMitre =
                mitreFilter === "" || mitreTechnique === mitreFilter;

            const searchableText = [
                eventType,
                sourceIp,
                username,
                mitreTechnique,
                message,
                rawLog,
            ]
                .join(" ")
                .toLowerCase();

            const matchesSearch =
                search.trim() === "" ||
                searchableText.includes(search.trim().toLowerCase());

            return (
                matchesSourceIp &&
                matchesUsername &&
                matchesMitre &&
                matchesSearch
            );
        });
    }, [events, sourceIpFilter, usernameFilter, mitreFilter, search]);

    const getEventType = (event: SecurityEvent) => {
        return event.eventType ?? event.type ?? "N/A";
    };

    const getEventTime = (event: SecurityEvent) => {
        return event.timestamp ?? event.createdAt ?? "";
    };

    const formatTime = (value?: string) => {
        if (!value) return "N/A";
        return new Date(value).toLocaleString();
    };

    const pivotTo = (
        key: "sourceIp" | "username" | "mitreTechnique",
        value?: string
    ) => {
        if (!value) return;

        setSelectedEvent(null);
        navigate(`/events?${key}=${encodeURIComponent(value)}`);
    };

    const clearFilters = () => {
        navigate("/events");
    };

    const PivotLink = ({
                           field,
                           value,
                       }: {
        field: "sourceIp" | "username" | "mitreTechnique";
        value?: string;
    }) => {
        if (!value) return <>N/A</>;

        return (
            <Link
                component="button"
                underline="hover"
                onClick={() => pivotTo(field, value)}
                sx={{
                    fontWeight: 700,
                    cursor: "pointer",
                }}
            >
                {value}
            </Link>
        );
    };

    return (
        <AppLayout title="Events">
            <Typography sx={{ color: "#9ca3af", mb: 3 }}>
                Raw security events ingested by SIEM-Lite.
            </Typography>

            {(sourceIpFilter || usernameFilter || mitreFilter) && (
                <Box sx={{ display: "flex", gap: 1, mb: 3, flexWrap: "wrap" }}>
                    {sourceIpFilter && (
                        <Chip label={`Source IP: ${sourceIpFilter}`} color="info" />
                    )}

                    {usernameFilter && (
                        <Chip label={`Username: ${usernameFilter}`} color="info" />
                    )}

                    {mitreFilter && (
                        <Chip label={`MITRE: ${mitreFilter}`} color="info" />
                    )}

                    <Chip
                        label="Clear filters"
                        color="default"
                        variant="outlined"
                        onClick={clearFilters}
                    />
                </Box>
            )}

            <TextField
                fullWidth
                label="Search events"
                value={search}
                onChange={(event) => setSearch(event.target.value)}
                sx={{ mb: 3 }}
            />

            {error !== "" && (
                <Typography color="error" sx={{ mb: 2 }}>
                    {error}
                </Typography>
            )}

            {loading ? (
                <Box sx={{ display: "flex", justifyContent: "center", mt: 6 }}>
                    <CircularProgress />
                </Box>
            ) : (
                <TableContainer component={Paper}>
                    <Table>
                        <TableHead>
                            <TableRow>
                                <TableCell>Event Type</TableCell>
                                <TableCell>Source IP</TableCell>
                                <TableCell>Username</TableCell>
                                <TableCell>MITRE</TableCell>
                                <TableCell>Message</TableCell>
                                <TableCell>Timestamp</TableCell>
                                <TableCell align="right">Actions</TableCell>
                            </TableRow>
                        </TableHead>

                        <TableBody>
                            {filteredEvents.length === 0 ? (
                                <TableRow>
                                    <TableCell colSpan={7}>No events found.</TableCell>
                                </TableRow>
                            ) : (
                                filteredEvents.map((event) => (
                                    <TableRow key={event.id} hover>
                                        <TableCell>
                                            <Chip
                                                label={getEventType(event)}
                                                size="small"
                                                color="primary"
                                                variant="outlined"
                                            />
                                        </TableCell>

                                        <TableCell>
                                            <PivotLink
                                                field="sourceIp"
                                                value={event.sourceIp}
                                            />
                                        </TableCell>

                                        <TableCell>
                                            <PivotLink
                                                field="username"
                                                value={event.username}
                                            />
                                        </TableCell>

                                        <TableCell>
                                            <PivotLink
                                                field="mitreTechnique"
                                                value={event.mitreTechnique}
                                            />
                                        </TableCell>

                                        <TableCell
                                            sx={{
                                                maxWidth: 350,
                                                whiteSpace: "nowrap",
                                                overflow: "hidden",
                                                textOverflow: "ellipsis",
                                            }}
                                        >
                                            {event.message ?? event.rawLog ?? "No message"}
                                        </TableCell>

                                        <TableCell>
                                            {formatTime(getEventTime(event))}
                                        </TableCell>

                                        <TableCell align="right">
                                            <Button
                                                size="small"
                                                variant="outlined"
                                                startIcon={<VisibilityIcon />}
                                                onClick={() => setSelectedEvent(event)}
                                            >
                                                View
                                            </Button>
                                        </TableCell>
                                    </TableRow>
                                ))
                            )}
                        </TableBody>
                    </Table>
                </TableContainer>
            )}

            <Drawer
                anchor="right"
                open={Boolean(selectedEvent)}
                onClose={() => setSelectedEvent(null)}
                slotProps={{
                    paper: {
                        sx: {
                            width: { xs: "100%", sm: 540 },
                            p: 3,
                        },
                    },
                }}
            >
                {selectedEvent && (
                    <Stack spacing={2}>
                        <Box
                            sx={{
                                display: "flex",
                                justifyContent: "space-between",
                                alignItems: "center",
                            }}
                        >
                            <Typography variant="h5" sx={{ fontWeight: 700 }}>
                                Event Details
                            </Typography>

                            <IconButton onClick={() => setSelectedEvent(null)}>
                                <CloseIcon />
                            </IconButton>
                        </Box>

                        <Divider />

                        <Detail label="Event Type" value={getEventType(selectedEvent)} />
                        <Detail label="Source IP" value={selectedEvent.sourceIp ?? "N/A"} />
                        <Detail label="Username" value={selectedEvent.username ?? "N/A"} />
                        <Detail
                            label="MITRE Technique"
                            value={selectedEvent.mitreTechnique ?? "N/A"}
                        />
                        <Detail
                            label="Destination IP"
                            value={selectedEvent.destinationIp ?? "N/A"}
                        />
                        <Detail label="Severity" value={selectedEvent.severity ?? "N/A"} />
                        <Detail
                            label="Timestamp"
                            value={formatTime(getEventTime(selectedEvent))}
                        />

                        <Divider />

                        <Box>
                            <Typography
                                variant="subtitle1"
                                sx={{ fontWeight: 700 }}
                                gutterBottom
                            >
                                Raw Event
                            </Typography>

                            <Paper
                                variant="outlined"
                                sx={{
                                    p: 2,
                                    borderRadius: 2,
                                    bgcolor: "background.default",
                                    maxHeight: 220,
                                    overflow: "auto",
                                    whiteSpace: "pre-wrap",
                                    wordBreak: "break-word",
                                }}
                            >
                                <Typography variant="body2" sx={{ fontFamily: "monospace" }}>

                                    {selectedEvent.rawLog ??
                                        selectedEvent.message ??
                                        "No raw event available."}
                                </Typography>
                            </Paper>
                        </Box>

                        <Divider />

                        <Box>
                            <Typography
                                variant="subtitle1"
                                sx={{ fontWeight: 700 }}
                                gutterBottom
                            >
                                Pivot Investigation
                            </Typography>

                            <Stack spacing={1}>
                                <Button
                                    variant="outlined"
                                    disabled={!selectedEvent.sourceIp}
                                    onClick={() =>
                                        pivotTo("sourceIp", selectedEvent.sourceIp)
                                    }
                                >
                                    View all events from this IP
                                </Button>

                                <Button
                                    variant="outlined"
                                    disabled={!selectedEvent.username}
                                    onClick={() =>
                                        pivotTo("username", selectedEvent.username)
                                    }
                                >
                                    View all events for this user
                                </Button>

                                <Button
                                    variant="outlined"
                                    disabled={!selectedEvent.mitreTechnique}
                                    onClick={() =>
                                        pivotTo(
                                            "mitreTechnique",
                                            selectedEvent.mitreTechnique
                                        )
                                    }
                                >
                                    View all {selectedEvent.mitreTechnique ?? "MITRE"} events
                                </Button>
                            </Stack>
                        </Box>
                    </Stack>
                )}
            </Drawer>
        </AppLayout>
    );
}

function Detail({ label, value }: { label: string; value: string }) {
    return (
        <Box>
            <Typography variant="subtitle2" sx={{ color: "#9ca3af" }}>
                {label}
            </Typography>

            <Typography variant="body1" sx={{ fontWeight: 500 }}>
                {value}
            </Typography>
        </Box>
    );
}