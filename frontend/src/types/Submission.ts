// src/types.ts
export interface Submission {
    id: number;
    title: string;
    content: string;
    clientName: string;
    clientEmail: string;
    clientPhone: string;
    stage: string;
    analystNote: string | null;
    approvalsCount: number;
    rejectionsCount: number;
}
