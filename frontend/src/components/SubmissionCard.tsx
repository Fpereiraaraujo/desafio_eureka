import React from 'react';

interface SubmissionCardProps {
    id: number;
    title: string;
    content: string;
    clientName: string;
    clientEmail: string;
    clientPhone: string;
    stage: string;
    approvalsCount: number;
    rejectionsCount: number;
    analystNote?: string;
    onAnalyze?: (id: number) => void;
    onReview?: (id: number) => void;
    onVote?: (id: number) => void;
}

const SubmissionCard: React.FC<SubmissionCardProps> = ({
                                                           id,
                                                           title,
                                                           content,
                                                           clientName,
                                                           clientEmail,
                                                           clientPhone,
                                                           stage,
                                                           approvalsCount,
                                                           rejectionsCount,
                                                           analystNote,
                                                           onAnalyze,
                                                           onReview,
                                                           onVote,
                                                       }) => {
    return (
        <div className="border rounded-lg p-4 shadow hover:shadow-lg transition mb-4 bg-white">
            <h2 className="text-xl font-bold mb-2">{title}</h2>
            <p className="text-gray-700 mb-1"><strong>Cliente:</strong> {clientName}</p>
            <p className="text-gray-700 mb-1"><strong>Email:</strong> {clientEmail}</p>
            <p className="text-gray-700 mb-1"><strong>Telefone:</strong> {clientPhone}</p>
            <p className="text-gray-700 mb-2"><strong>Conteúdo:</strong> {content}</p>
            <p className="text-sm text-gray-500 mb-2"><strong>Stage:</strong> {stage}</p>
            {analystNote && <p className="text-sm text-gray-500 mb-2"><strong>Nota do Analista:</strong> {analystNote}</p>}
            <p className="text-sm text-gray-500 mb-2">
                <strong>Aprovações:</strong> {approvalsCount} | <strong>Rejeições:</strong> {rejectionsCount}
            </p>

            <div className="flex gap-2 mt-2">
                {onAnalyze && (
                    <button
                        onClick={() => onAnalyze(id)}
                        className="bg-blue-500 text-white px-3 py-1 rounded hover:bg-blue-600"
                    >
                        Analisar
                    </button>
                )}
                {onReview && (
                    <button
                        onClick={() => onReview(id)}
                        className="bg-yellow-500 text-white px-3 py-1 rounded hover:bg-yellow-600"
                    >
                        Revisar
                    </button>
                )}
                {onVote && (
                    <button
                        onClick={() => onVote(id)}
                        className="bg-green-500 text-white px-3 py-1 rounded hover:bg-green-600"
                    >
                        Votar
                    </button>
                )}
            </div>
        </div>
    );
};

export default SubmissionCard;
