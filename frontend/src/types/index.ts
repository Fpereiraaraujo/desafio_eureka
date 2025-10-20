export interface Screenplay {
    id: number;
    title: string;
    content: string;
    clientName: string;
    clientEmail: string;
    clientPhone: string;
    stage: string;
    createdAt: string;
    analystNote?: string;
    approvalsCount?: number;
    rejectionsCount?: number;
}
