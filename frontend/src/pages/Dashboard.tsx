// src/pages/Dashboard.tsx
import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import {
    fetchSubmissions,
    claimSubmission,
    analyzeSubmission,
    reviewClaim,
    reviewFinish,
    voteSubmission
} from '../api/api';

interface Submission {
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

export default function Dashboard() {
    const [submissions, setSubmissions] = useState<Submission[]>([]);
    const [analysisNotes, setAnalysisNotes] = useState<{ [key: number]: string }>({});
    const [reviewNotes, setReviewNotes] = useState<{ [key: number]: string }>({});
    const [loading, setLoading] = useState<{ [key: number]: boolean }>({});
    const [errorMessage, setErrorMessage] = useState<string | null>(null);

    const token = localStorage.getItem('token') || '';
    const navigate = useNavigate();

    useEffect(() => {
        const load = async () => {
            try {
                const data = await fetchSubmissions(token);
                setSubmissions(
                    data.map((s: Submission) => ({
                        ...s,
                        stage: s.stage.toUpperCase(),
                    }))
                );
            } catch (err) {
                console.error(err);
                setErrorMessage('Erro ao carregar submissões');
            }
        };
        load();
    }, [token]);

    const handleLogout = () => {
        localStorage.removeItem('token');
        navigate('/login');
    };

    const reloadSubmissions = async () => {
        try {
            const data = await fetchSubmissions(token);
            setSubmissions(
                data.map((s: Submission) => ({
                    ...s,
                    stage: s.stage.toUpperCase(),
                }))
            );
        } catch (err) {
            console.error(err);
            setErrorMessage('Erro ao recarregar submissões');
        }
    };


    // AWAITING_ANALYSIS -> IN_ANALYSIS
    const handleClaim = async (id: number) => {
        try {
            setLoading({ ...loading, [id]: true });
            await claimSubmission(id, token);
            await reloadSubmissions();
            setSubmissions(subs =>
                subs.map(s => s.id === id ? { ...s, stage: 'IN_ANALYSIS' } : s)
            );
        } catch (err: any) {
            console.error(err);
            setErrorMessage(err.response?.data?.error || 'Erro ao reivindicar');
        } finally {
            setLoading({ ...loading, [id]: false });
        }
    };

    // IN_ANALYSIS -> AWAITING_REVIEW
    const handleAnalyze = async (id: number, approve: boolean) => {
        try {
            setLoading({ ...loading, [id]: true });
            await analyzeSubmission(id, token, approve, analysisNotes[id] || '');
            await reloadSubmissions();
            setSubmissions(subs =>
                subs.map(s =>
                    s.id === id
                        ? {
                            ...s,
                            stage: approve ? 'AWAITING_REVIEW' : 'REJECTED',
                            analystNote: analysisNotes[id] || ''
                        }
                        : s
                )
            );
        } catch (err: any) {
            console.error(err);
            setErrorMessage(err.response?.data?.error || 'Erro ao analisar');
        } finally {
            setLoading({ ...loading, [id]: false });
        }
    };

    // AWAITING_REVIEW -> IN_REVIEW
    const handleReviewClaim = async (id: number) => {
        try {
            setLoading({ ...loading, [id]: true });
            await reviewClaim(id, token);
            await reloadSubmissions();
            setSubmissions(subs =>
                subs.map(s =>
                    s.id === id
                        ? { ...s, stage: 'IN_REVIEW' }
                        : s
                )
            );
        } catch (err: any) {
            console.error(err);
            setErrorMessage(err.response?.data?.error || 'Erro ao reivindicar revisão');
        } finally {
            setLoading({ ...loading, [id]: false });
        }
    };

    // IN_REVIEW -> AWAITING_APPROVAL
    const handleReviewFinish = async (id: number) => {
        try {
            setLoading({ ...loading, [id]: true });
            await reviewFinish(id, token, reviewNotes[id] || '');
            setSubmissions(subs =>
                subs.map(s =>
                    s.id === id
                        ? { ...s, stage: 'AWAITING_APPROVAL' }
                        : s
                )
            );
        } catch (err: any) {
            console.error(err);
            setErrorMessage(err.response?.data?.error || 'Erro ao finalizar revisão');
        } finally {
            setLoading({ ...loading, [id]: false });
        }
    };




    const handleVote = async (id: number, decision: 'YES' | 'NO') => {
        try {
            setLoading({ ...loading, [id]: true });

            // backend deve retornar a submissão completa atualizada
            const updated: Submission = await voteSubmission(id, token, decision);
            await reloadSubmissions();

            // atualiza a submissão no estado usando o objeto completo retornado
            setSubmissions(subs =>
                subs.map(s => s.id === id ? updated : s)
            );

        } catch (err: any) {
            console.error(err);
            setErrorMessage(err.response?.data?.error || 'Erro ao votar');
        } finally {
            setLoading({ ...loading, [id]: false });
        }
    };




    return (
        <div className="p-6 bg-gray-100 min-h-screen">
            <div className="flex justify-between items-center mb-6">
                <h1 className="text-3xl font-bold">Dashboard</h1>
                <button
                    onClick={handleLogout}
                    className="bg-red-600 text-white px-4 py-2 rounded hover:bg-red-700"
                >
                    Sair
                </button>
            </div>

            {errorMessage && (
                <div className="mb-4 p-2 bg-red-100 text-red-700 border border-red-400 rounded">
                    {errorMessage}
                </div>
            )}

            {submissions.length === 0 && <p>Nenhuma submissão encontrada.</p>}

            <div className="grid gap-6">
                {submissions.map((s) => (
                    <div key={s.id} className="p-4 bg-white shadow rounded space-y-3">
                        <h2 className="font-bold text-xl">{s.title}</h2>
                        <p>{s.content}</p>
                        <p className="text-sm text-gray-500">
                            {s.clientName} — {s.clientEmail} — {s.clientPhone}
                        </p>
                        <p className="text-sm font-semibold">Stage: {s.stage}</p>
                        {s.analystNote && (
                            <p className="text-sm text-gray-700">
                                Nota do Analista: {s.analystNote}
                            </p>
                        )}
                        <p className="text-sm text-gray-700">
                            Aprovações: {s.approvalsCount} | Rejeições: {s.rejectionsCount}
                        </p>

                        {/* AWAITING_ANALYSIS */}
                        {s.stage === 'AWAITING_ANALYSIS' && (
                            <button
                                onClick={() => handleClaim(s.id)}
                                className="bg-yellow-500 text-white px-3 py-1 rounded hover:bg-yellow-600"
                                disabled={loading[s.id]}
                            >
                                {loading[s.id] ? 'Processando...' : 'Reivindicar para Análise'}
                            </button>
                        )}

                        {/* IN_ANALYSIS */}
                        {s.stage === 'IN_ANALYSIS' && (
                            <div className="space-y-2">
                                <input
                                    type="text"
                                    placeholder="Notas de análise"
                                    className="w-full p-2 border rounded"
                                    value={analysisNotes[s.id] || ''}
                                    onChange={(e) =>
                                        setAnalysisNotes({
                                            ...analysisNotes,
                                            [s.id]: e.target.value,
                                        })
                                    }
                                />
                                <div className="flex gap-2">
                                    <button
                                        onClick={() => handleAnalyze(s.id, true)}
                                        className="bg-green-600 text-white px-3 py-1 rounded hover:bg-green-700"
                                        disabled={loading[s.id]}
                                    >
                                        {loading[s.id] ? 'Processando...' : 'Aprovar'}
                                    </button>
                                    <button
                                        onClick={() => handleAnalyze(s.id, false)}
                                        className="bg-red-600 text-white px-3 py-1 rounded hover:bg-red-700"
                                        disabled={loading[s.id]}
                                    >
                                        {loading[s.id] ? 'Processando...' : 'Rejeitar'}
                                    </button>
                                </div>
                            </div>
                        )}

                        {/* AWAITING_REVIEW */}
                        {s.stage === 'AWAITING_REVIEW' && (
                            <button
                                onClick={() => handleReviewClaim(s.id)}
                                className="bg-blue-500 text-white px-3 py-1 rounded hover:bg-blue-600"
                                disabled={loading[s.id]}
                            >
                                {loading[s.id] ? 'Processando...' : 'Reivindicar para Revisão'}
                            </button>
                        )}

                        {/* IN_REVIEW */}
                        {s.stage === 'IN_REVIEW' && (
                            <div className="space-y-2">
                                <input
                                    type="text"
                                    placeholder="Notas de revisão"
                                    className="w-full p-2 border rounded"
                                    value={reviewNotes[s.id] || ''}
                                    onChange={(e) =>
                                        setReviewNotes({
                                            ...reviewNotes,
                                            [s.id]: e.target.value,
                                        })
                                    }
                                />
                                <button
                                    onClick={() => handleReviewFinish(s.id)}
                                    className="bg-green-600 text-white px-3 py-1 rounded hover:bg-green-700"
                                    disabled={loading[s.id]}
                                >
                                    {loading[s.id] ? 'Processando...' : 'Finalizar Revisão'}
                                </button>
                            </div>
                        )}

                        {/* AWAITING_APPROVAL */}
                        {s.stage === 'AWAITING_APPROVAL' && (
                            <div className="flex gap-2 mt-2">
                                <button
                                    onClick={() => handleVote(s.id, 'YES')}
                                    className="bg-green-500 text-white px-3 py-1 rounded hover:bg-green-600"
                                    disabled={loading[s.id]}
                                >
                                    {loading[s.id] ? 'Processando...' : 'Votar YES'}
                                </button>
                                <button
                                    onClick={() => handleVote(s.id, 'NO')}
                                    className="bg-red-500 text-white px-3 py-1 rounded hover:bg-red-600"
                                    disabled={loading[s.id]}
                                >
                                    {loading[s.id] ? 'Processando...' : 'Votar NO'}
                                </button>
                            </div>
                        )}

                        {/* IN_APPROVAL */}
                        {s.stage === 'IN_APPROVAL' && (
                            <p className="text-blue-600 font-semibold">
                                Aguardando resultado final da votação
                            </p>
                        )}

                        {/* APPROVED */}
                        {s.stage === 'APPROVED' && (
                            <p className="text-green-600 font-semibold">✅ Aprovado</p>
                        )}

                        {/* REJECTED */}
                        {s.stage === 'REJECTED' && (
                            <p className="text-red-600 font-semibold">❌ Recusado</p>
                        )}
                    </div>
                ))}
            </div>
        </div>
    );
}
