import { useNavigate } from 'react-router-dom';

export default function HomePage() {
    const navigate = useNavigate();

    return (
        <div className="flex flex-col items-center justify-center h-screen bg-gray-100">
            <h1 className="text-4xl font-bold mb-8">Bem-vindo ao CooperFilme</h1>

            <div className="flex gap-4">
                <button
                    onClick={() => navigate('/login')}
                    className="bg-blue-600 text-white px-6 py-3 rounded-lg hover:bg-blue-700"
                >
                    Login
                </button>

                <button
                    onClick={() => navigate('/submission')}
                    className="bg-green-600 text-white px-6 py-3 rounded-lg hover:bg-green-700"
                >
                    Fazer Submissão
                </button>
            </div>
        </div>
    );
}
