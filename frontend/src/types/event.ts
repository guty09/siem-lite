export interface SecurityEvent {
    id: number;
    eventType?: string;
    type?: string;
    severity?: string;
    sourceIp?: string;
    destinationIp?: string;
    username?: string;
    message?: string;
    rawLog?: string;
    timestamp?: string;
    createdAt?: string;
    mitreTechnique?: string;
    mitreTactic?: string;
}