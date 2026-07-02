import { useEffect, useState } from "react";
import {
    Alert,
    Box,
    CircularProgress,
    Container,
    Pagination,
    Stack,
    Typography,
} from "@mui/material";

import AuditLogTable from "../components/AuditLogTable";
import { getAuditLogs } from "../services/audit";
import type { AuditLog, PageResponse } from "../types/audit";

const PAGE_SIZE = 20;

export default function AuditLogsPage() {
    const [logs, setLogs] = useState<AuditLog[]>([]);
    const [page, setPage] = useState(1);
    const [totalPages, setTotalPages] = useState(1);

    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

    useEffect(() => {
        loadAuditLogs(page);
    }, [page]);

    async function loadAuditLogs(currentPage: number) {
        try {
            setLoading(true);
            setError("");

            const response: PageResponse<AuditLog> =
                await getAuditLogs(currentPage - 1, PAGE_SIZE);

            setLogs(response.content);
            setTotalPages(Math.max(response.totalPages, 1));
        } catch (err) {
            console.error(err);
            setError("Failed to load audit logs.");
        } finally {
            setLoading(false);
        }
    }

    if (loading) {
        return (
            <Box
                sx={{
                    display: "flex",
                    justifyContent: "center",
                    alignItems: "center",
                    mt: 6,
                }}
            >
                <CircularProgress />
            </Box>
        );
    }

    if (error) {
        return (
            <Container sx={{ mt: 3 }}>
                <Alert severity="error">{error}</Alert>
            </Container>
        );
    }

    return (
        <Container maxWidth="xl" sx={{ mt: 3 }}>
            <Stack spacing={3}>
                <Typography variant="h4">
                    Audit Logs
                </Typography>

                <AuditLogTable logs={logs} />

                <Box
                    sx={{
                        display: "flex",
                        justifyContent: "center",
                    }}
                >
                    <Pagination
                        page={page}
                        count={totalPages}
                        color="primary"
                        onChange={(_, value) => setPage(value)}
                    />
                </Box>
            </Stack>
        </Container>
    );
}