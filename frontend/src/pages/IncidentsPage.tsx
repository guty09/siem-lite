import { useEffect, useState } from "react";
import { Alert, CircularProgress, Stack } from "@mui/material";

import AppLayout from "../components/layout/AppLayout";
import IncidentTable from "../components/incidents/IncidentTable";
import IncidentDetailsDrawer from "../components/incidents/IncidentDetailsDrawer";

import type { Incident, IncidentStatus } from "../types/incident";
import {
    assignIncident,
    getIncidents,
    updateIncidentNotes,
    updateIncidentStatus,
} from "../services/incidents";

export default function IncidentsPage() {
    const [incidents, setIncidents] = useState<Incident[]>([]);
    const [selectedIncident, setSelectedIncident] = useState<Incident | null>(null);
    const [drawerOpen, setDrawerOpen] = useState(false);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState<string | null>(null);
    const [success, setSuccess] = useState<string | null>(null);

    async function loadIncidents() {
        try {
            setLoading(true);
            setError(null);

            const data = await getIncidents();
            setIncidents(data);
        } catch {
            setError("Failed to load incidents.");
        } finally {
            setLoading(false);
        }
    }

    useEffect(() => {
        loadIncidents();
    }, []);

    function handleInvestigate(incident: Incident) {
        setSelectedIncident(incident);
        setDrawerOpen(true);
        setSuccess(null);
        setError(null);
    }

    function updateSelectedIncident(updatedIncident: Incident) {
        setSelectedIncident(updatedIncident);
        setIncidents((current) =>
            current.map((incident) =>
                incident.id === updatedIncident.id ? updatedIncident : incident
            )
        );
    }

    async function handleAssign(assignedAnalyst: string) {
        if (!selectedIncident) return;

        try {
            const updatedIncident = await assignIncident(
                selectedIncident.id,
                assignedAnalyst
            );

            updateSelectedIncident(updatedIncident);
            setSuccess("Incident assignment updated.");
        } catch {
            setError("Failed to update incident assignment.");
        }
    }

    async function handleStatusChange(status: IncidentStatus) {
        if (!selectedIncident) return;

        try {
            const updatedIncident = await updateIncidentStatus(
                selectedIncident.id,
                status
            );

            updateSelectedIncident(updatedIncident);
            setSuccess("Incident status updated.");
        } catch {
            setError("Failed to update incident status.");
        }
    }

    async function handleNotesSave(notes: string) {
        if (!selectedIncident) return;

        try {
            const updatedIncident = await updateIncidentNotes(
                selectedIncident.id,
                notes
            );

            updateSelectedIncident(updatedIncident);
            setSuccess("Incident notes updated.");
        } catch {
            setError("Failed to update incident notes.");
        }
    }

    return (
        <AppLayout title="Incident Management">
            <Stack spacing={3}>
                {error && <Alert severity="error">{error}</Alert>}
                {success && <Alert severity="success">{success}</Alert>}

                {loading ? (
                    <CircularProgress />
                ) : (
                    <IncidentTable
                        incidents={incidents}
                        onInvestigate={handleInvestigate}
                    />
                )}

                <IncidentDetailsDrawer
                    open={drawerOpen}
                    incident={selectedIncident}
                    onClose={() => setDrawerOpen(false)}
                    onAssign={handleAssign}
                    onStatusChange={handleStatusChange}
                    onNotesSave={handleNotesSave}
                />
            </Stack>
        </AppLayout>
    );
}