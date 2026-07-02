export type IncidentStatus = "OPEN" | "IN_PROGRESS" | "CLOSED";

export interface Incident {
    id: number;
    timestamp: string;
    incidentType: string;
    severity: string;
    riskScore: number;
    summary: string;
    sourceIp: string;
    status: IncidentStatus | string;
    assignedAnalyst: string | null;
    notes: string | null;
    createdAt: string;
    updatedAt: string;
}