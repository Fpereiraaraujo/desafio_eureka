import axios from "axios";
import {Submission} from "../types/Submission";

const API_URL = 'http://localhost:18080/v1';

// Login
export const login = async (email: string, password: string) => {
    const res = await axios.post(`${API_URL}/auth/login`, { email, password });
    return res.data;
};

// Buscar todas as submissões
export const fetchSubmissions = async (token: string) => {
    const res = await axios.get(`${API_URL}/scripts/all`, {
        headers: { Authorization: `Bearer ${token}` }
    });
    return res.data;
};

// Claim roteiro (Content Analyst)
export const claimSubmission = async (id: number, token: string) => {
    await axios.post(`${API_URL}/scripts/${id}/claim`, {}, {
        headers: { Authorization: `Bearer ${token}` }
    });
};

// Analisar submissão (Content Analyst)
export const analyzeSubmission = async (id: number, token: string, approve: boolean, note: string) => {
    await axios.post(`${API_URL}/scripts/${id}/analyze`,
        { goodIdea: approve, note },
        { headers: { Authorization: `Bearer ${token}` } }
    );
};

// Claim revisão (Quality Reviser)
export const reviewClaim = async (id: number, token: string) => {
    await axios.post(`${API_URL}/scripts/${id}/review/claim`, {}, {
        headers: { Authorization: `Bearer ${token}` }
    });
};

// Finalizar revisão (Quality Reviser)
export const reviewFinish = async (id: number, token: string, notes: string) => {
    await axios.post(`${API_URL}/scripts/${id}/review/finish`,
        { notes },
        { headers: { Authorization: `Bearer ${token}` } }
    );
};

export const voteSubmission = async (id: number, token: string, decision: 'YES' | 'NO'): Promise<Submission> => {
    const res = await fetch(`/v1/scripts/${id}/vote`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`,
        },
        body: JSON.stringify({ decision }),
    });
    if (!res.ok) {
        throw new Error(`Erro ao votar: ${res.statusText}`);
    }
    return res.json() as Promise<Submission>;
};




