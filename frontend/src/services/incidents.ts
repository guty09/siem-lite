import api from "./api";
import type { Incident } from "../types/incident";

export async function getIncidents(): Promise<Incident[]> {
    const response = await api.get<Incident[]>("/incidents");
    return response.data;
}

export async function getIncidentById(id: number): Promise<Incident> {
    const response = await api.get<Incident>(`/incidents/${id}`);
    return response.data;
}

export async function assignIncident(
    id: number,
    assignedAnalyst: string
): Promise<Incident> {
    const response = await api.put<Incident>(
        `/incidents/${id}/assign`,
        null,
        {
            params: { assignedAnalyst },
        }
    );

    return response.data;
}

export async function updateIncidentStatus(
    id: number,
    status: string
): Promise<Incident> {
    const response = await api.put<Incident>(
        `/incidents/${id}/status`,
        null,
        {
            params: { status },
        }
    );

    return response.data;
}

export async function updateIncidentNotes(
    id: number,
    notes: string
): Promise<Incident> {
    const response = await api.put<Incident>(
        `/incidents/${id}/notes`,
        null,
        {
            params: { notes },
        }
    );

    return response.data;
}