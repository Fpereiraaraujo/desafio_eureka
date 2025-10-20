// src/pages/SubmissionForm.tsx
import React, { useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';

const SubmissionForm: React.FC = () => {
    const [title, setTitle] = useState('');
    const [content, setContent] = useState('');
    const [clientName, setClientName] = useState('');
    const [clientEmail, setClientEmail] = useState('');
    const [clientPhone, setClientPhone] = useState('');
    const [loading, setLoading] = useState(false);
    const [message, setMessage] = useState('');
    const navigate = useNavigate();

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setLoading(true);
        setMessage('');

        try {
            const response = await axios.post('http://localhost:18080/v1/public/submissions', {
                title,
                content,
                clientName,
                clientEmail,
                clientPhone,
            });
            setMessage('Submissão enviada com sucesso!');
            // Limpa o formulário
            setTitle('');
            setContent('');
            setClientName('');
            setClientEmail('');
            setClientPhone('');
            // Redireciona para a página inicial ou dashboard, se quiser
            // navigate('/dashboard');
        } catch (error: any) {
            console.error(error);
            setMessage('Erro ao enviar a submissão.');
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="max-w-xl mx-auto mt-10 p-6 bg-white rounded shadow">
            <h1 className="text-2xl font-bold mb-4">Nova Submissão</h1>
            {message && (
                <div className="mb-4 p-2 bg-green-200 text-green-800 rounded">{message}</div>
            )}
            <form onSubmit={handleSubmit} className="flex flex-col gap-4">
                <input
                    type="text"
                    placeholder="Título"
                    value={title}
                    onChange={(e) => setTitle(e.target.value)}
                    required
                    className="border px-3 py-2 rounded"
                />
                <textarea
                    placeholder="Conteúdo"
                    value={content}
                    onChange={(e) => setContent(e.target.value)}
                    required
                    className="border px-3 py-2 rounded"
                />
                <input
                    type="text"
                    placeholder="Nome do Cliente"
                    value={clientName}
                    onChange={(e) => setClientName(e.target.value)}
                    required
                    className="border px-3 py-2 rounded"
                />
                <input
                    type="email"
                    placeholder="Email do Cliente"
                    value={clientEmail}
                    onChange={(e) => setClientEmail(e.target.value)}
                    required
                    className="border px-3 py-2 rounded"
                />
                <input
                    type="tel"
                    placeholder="Telefone do Cliente"
                    value={clientPhone}
                    onChange={(e) => setClientPhone(e.target.value)}
                    required
                    className="border px-3 py-2 rounded"
                />
                <button
                    type="submit"
                    disabled={loading}
                    className="bg-blue-500 text-white py-2 rounded hover:bg-blue-600"
                >
                    {loading ? 'Enviando...' : 'Enviar Submissão'}
                </button>
            </form>
        </div>
    );
};

export default SubmissionForm;
