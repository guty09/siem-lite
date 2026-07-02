import api from "./api";

import type {
    IndicatorOfCompromise,
    IndicatorOfCompromiseRequest,
} from "../types/ioc";

export async function getIocs(): Promise<IndicatorOfCompromise[]> {
    const response = await api.get<IndicatorOfCompromise[]>("/iocs");
    return response.data;
}

export async function getIoc(id: number): Promise<IndicatorOfCompromise> {
    const response = await api.get<IndicatorOfCompromise>(`/iocs/${id}`);
    return response.data;
}

export async function createIoc(
    request: IndicatorOfCompromiseRequest
): Promise<IndicatorOfCompromise> {
    const response = await api.post<IndicatorOfCompromise>("/iocs", request);
    return response.data;
}

export async function updateIoc(
    id: number,
    request: IndicatorOfCompromiseRequest
): Promise<IndicatorOfCompromise> {
    const response = await api.put<IndicatorOfCompromise>(`/iocs/${id}`, request);
    return response.data;
}

export async function enableIoc(id: number): Promise<IndicatorOfCompromise> {
    const response = await api.put<IndicatorOfCompromise>(`/iocs/${id}/enable`);
    return response.data;
}

export async function disableIoc(id: number): Promise<IndicatorOfCompromise> {
    const response = await api.put<IndicatorOfCompromise>(`/iocs/${id}/disable`);
    return response.data;
}

export async function deleteIoc(id: number): Promise<void> {
    await api.delete(`/iocs/${id}`);
}