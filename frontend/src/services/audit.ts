import api from "./api";
import type { AuditLog, PageResponse } from "../types/audit";

export async function getAuditLogs(
    page = 0,
    size = 20
): Promise<PageResponse<AuditLog>> {
    const response = await api.get<PageResponse<AuditLog>>("/audit", {
        params: {
            page,
            size,
        },
    });

    return response.data;
}