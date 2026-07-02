export interface AuditLog {
    id: number;
    action: string;
    username: string;
    details: string;
    timestamp: string;
}

export interface PageResponse<T> {
    content: T[];
    totalElements: number;
    totalPages: number;
    size: number;
    number: number;
    first: boolean;
    last: boolean;
}