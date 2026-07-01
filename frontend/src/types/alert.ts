export interface Alert {
    id: number;
    alertType?: string;
    type?: string;
    severity: string;
    riskScore?: number;
    mitreTechnique?: string;
    mitreDescription?: string;
    status?: string;
    assignedAnalyst?: string;
    createdAt?: string;
    sourceIp?: string;
    username?: string;
    eventType?: string;
    message?: string;
    description?: string;
    notes?: string;
}