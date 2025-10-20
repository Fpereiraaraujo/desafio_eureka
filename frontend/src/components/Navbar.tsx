// src/components/Navbar.tsx
import React from "react";
import { Link, useNavigate } from "react-router-dom";

type Props = {
    isLoggedIn: boolean;
};

const Navbar: React.FC<Props> = ({ isLoggedIn }) => {
    const navigate = useNavigate();

    const handleLogout = () => {
        localStorage.removeItem("token");
        navigate("/"); // volta para a página inicial
    };

    return (
        <nav className="bg-gray-800 text-white px-6 py-4 flex justify-between items-center">
            <div className="text-2xl font-bold">
                <Link to="/">Cooperfilme</Link>
            </div>
            <div className="space-x-4">
                {!isLoggedIn && (
                    <>
                        <Link
                            to="/login"
                            className="bg-blue-600 hover:bg-blue-700 px-4 py-2 rounded"
                        >
                            Login
                        </Link>
                        <Link
                            to="/submission"
                            className="bg-green-600 hover:bg-green-700 px-4 py-2 rounded"
                        >
                            Submissão
                        </Link>
                    </>
                )}
                {isLoggedIn && (
                    <>
                        <Link
                            to="/dashboard"
                            className="bg-blue-600 hover:bg-blue-700 px-4 py-2 rounded"
                        >
                            Dashboard
                        </Link>
                        <button
                            onClick={handleLogout}
                            className="bg-red-600 hover:bg-red-700 px-4 py-2 rounded"
                        >
                            Logout
                        </button>
                    </>
                )}
            </div>
        </nav>
    );
};

export default Navbar;
