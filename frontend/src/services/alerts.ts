import api from "./api";
import type { Alert } from "../types/alert";
import type { SecurityEvent } from "../types/event";

export async function getAlerts(): Promise<Alert[]> {
    const response = await api.get("/alerts");
    const data = response.data;

    if (Array.isArray(data)) {
        return data;
    }

    if (data && Array.isArray(data.content)) {
        return data.content;
    }

    if (data && Array.isArray(data.alerts)) {
        return data.alerts;
    }

    return [];
}

export async function getRelatedEvents(alertId: number): Promise<SecurityEvent[]> {
    const response = await api.get(`/alerts/${alertId}/related-events`);

    if (Array.isArray(response.data)) {
        return response.data;
    }

    return [];
}

export async function assignAlert(id: number, analyst: string): Promise<void> {
    await api.put(`/alerts/${id}/assign`, null, {
        params: { analyst },
    });
}

export async function updateAlertStatus(id: number, status: string): Promise<void> {
    await api.put(`/alerts/${id}/status`, null, {
        params: { status },
    });
}

export async function updateAlertNotes(id: number, note: string): Promise<void> {
    await api.put(`/alerts/${id}/notes`, null, {
        params: { note },
    });
}