import api from "./api";
import type { SecurityEvent } from "../types/event";

export async function getEvents(): Promise<SecurityEvent[]> {
    const response = await api.get("/events");

    if (Array.isArray(response.data)) {
        return response.data;
    }

    if (Array.isArray(response.data.content)) {
        return response.data.content;
    }

    return [];
}