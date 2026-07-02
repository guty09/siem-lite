import { useEffect, useMemo, useState } from "react";
import {
    Alert,
    Box,
    Button,
    CircularProgress,
    Stack,
    TextField,
    Typography,
} from "@mui/material";

import AppLayout from "../components/layout/AppLayout";
import IocTable from "../components/iocs/IocTable";
import IocDialog from "../components/iocs/IocDialog";
import DeleteIocDialog from "../components/iocs/DeleteIocDialog";

import {
    createIoc,
    deleteIoc,
    disableIoc,
    enableIoc,
    getIocs,
    updateIoc,
} from "../services/iocs";

import type {
    IndicatorOfCompromise,
    IndicatorOfCompromiseRequest,
} from "../types/ioc";

export default function IocsPage() {
    const [iocs, setIocs] = useState<IndicatorOfCompromise[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");
    const [search, setSearch] = useState("");

    const [dialogOpen, setDialogOpen] = useState(false);
    const [deleteDialogOpen, setDeleteDialogOpen] = useState(false);
    const [selectedIoc, setSelectedIoc] = useState<IndicatorOfCompromise | null>(null);

    async function loadIocs() {
        try {
            const data = await getIocs();
            setIocs(data);
            setError("");
        } catch (err) {
            console.error("IOC load error:", err);
            setError("Failed to load IOCs.");
        } finally {
            setLoading(false);
        }
    }

    useEffect(() => {
        loadIocs();
    }, []);

    const filteredIocs = useMemo(() => {
        const query = search.trim().toLowerCase();

        if (!query) {
            return iocs;
        }

        return iocs.filter((ioc) => {
            return [
                ioc.value,
                ioc.type,
                ioc.threatSource,
                ioc.malwareFamily,
                ioc.campaign,
                ioc.description,
            ]
                .filter(Boolean)
                .some((field) => field.toLowerCase().includes(query));
        });
    }, [iocs, search]);

    function openCreateDialog() {
        setSelectedIoc(null);
        setDialogOpen(true);
    }

    function openEditDialog(ioc: IndicatorOfCompromise) {
        setSelectedIoc(ioc);
        setDialogOpen(true);
    }

    function openDeleteDialog(ioc: IndicatorOfCompromise) {
        setSelectedIoc(ioc);
        setDeleteDialogOpen(true);
    }

    async function handleSave(request: IndicatorOfCompromiseRequest) {
        if (selectedIoc) {
            await updateIoc(selectedIoc.id, request);
        } else {
            await createIoc(request);
        }

        await loadIocs();
    }

    async function handleDelete() {
        if (!selectedIoc) return;

        await deleteIoc(selectedIoc.id);
        setDeleteDialogOpen(false);
        setSelectedIoc(null);
        await loadIocs();
    }

    async function handleToggleActive(ioc: IndicatorOfCompromise) {
        if (ioc.active) {
            await disableIoc(ioc.id);
        } else {
            await enableIoc(ioc.id);
        }

        await loadIocs();
    }

    return (
        <AppLayout title="Threat Intelligence / IOC Management">
            <Typography variant="body1" sx={{ color: "#9ca3af", mb: 4 }}>
                Manage Indicators of Compromise used by the SIEM-Lite detection engine.
            </Typography>

            <Stack
                direction={{ xs: "column", md: "row" }}
                spacing={2}
                sx={{ mb: 3 }}
            >
                <TextField
                    placeholder="Search IOCs by value, type, source, malware family, campaign, or description"
                    value={search}
                    onChange={(event) => setSearch(event.target.value)}
                    fullWidth
                    size="small"
                    sx={{
                        "& .MuiOutlinedInput-root": {
                            bgcolor: "#111827",
                            color: "#e5e7eb",
                        },
                        "& input": {
                            color: "#e5e7eb",
                        },
                        "& .MuiOutlinedInput-notchedOutline": {
                            borderColor: "#1f2937",
                        },
                    }}
                />

                <Button
                    variant="contained"
                    onClick={openCreateDialog}
                    sx={{ whiteSpace: "nowrap" }}
                >
                    Add IOC
                </Button>
            </Stack>

            {loading && (
                <Box sx={{ display: "flex", justifyContent: "center", mt: 6 }}>
                    <CircularProgress />
                </Box>
            )}

            {error && (
                <Alert severity="error" sx={{ mb: 3 }}>
                    {error}
                </Alert>
            )}

            {!loading && !error && (
                <IocTable
                    iocs={filteredIocs}
                    onEdit={openEditDialog}
                    onDelete={openDeleteDialog}
                    onToggleActive={handleToggleActive}
                />
            )}

            <IocDialog
                open={dialogOpen}
                ioc={selectedIoc}
                onClose={() => setDialogOpen(false)}
                onSave={handleSave}
            />

            <DeleteIocDialog
                open={deleteDialogOpen}
                ioc={selectedIoc}
                onClose={() => setDeleteDialogOpen(false)}
                onConfirm={handleDelete}
            />
        </AppLayout>
    );
}