export interface Alert {
    id: number;
    alertType?: string;
    type?: string;
    severity: string;
    riskScore?: number;
    mitreTechnique?: string;
    mitreTactic?: string;
    status?: string;
    assignedAnalyst?: string;
    createdAt?: string;
    sourceIp?: string;
    username?: string;
    description?: string;
    notes?: string;
}