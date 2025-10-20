import { useState } from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import HomePage from './pages/HomePage';
import Login from './pages/Login';
import SubmissionForm from './pages/SubmissionForm';
import Dashboard from './pages/Dashboard';

function App() {
    const [isLoggedIn, setIsLoggedIn] = useState(!!localStorage.getItem('token'));

    return (
        <Router>
            <Routes>
                <Route path="/" element={<HomePage />} />
                <Route
                    path="/login"
                    element={<Login/>}
                />
                <Route path="/submission" element={<SubmissionForm />} />
                <Route
                    path="/dashboard"
                    element={isLoggedIn ? <Dashboard /> : <Navigate to="/login" />}
                />
                <Route path="*" element={<div className="p-10 text-center">Página não encontrada</div>} />
            </Routes>
        </Router>
    );
}

export default App;
