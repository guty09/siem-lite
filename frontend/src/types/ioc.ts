export interface IndicatorOfCompromise {
    id: number;
    value: string;
    type: string;
    threatSource: string;
    confidence: number;
    malwareFamily: string;
    campaign: string;
    description: string;
    active: boolean;
    createdAt: string;
}

export interface IndicatorOfCompromiseRequest {
    value: string;
    type: string;
    threatSource: string;
    confidence: number;
    malwareFamily: string;
    campaign: string;
    description: string;
}