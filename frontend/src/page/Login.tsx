import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';

const Login: React.FC = () => {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [showPassword, setShowPassword] = useState(false);
    const [error, setError] = useState('');
    const [loading, setLoading] = useState(false);
    const [rememberMe, setRememberMe] = useState(false);
    const navigate = useNavigate();

    const validateEmail = (email: string) =>
        /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email);

    const handleSubmit = (e: React.FormEvent) => {
        e.preventDefault();
        setError('');

        if (!email || !password) {
            setError('Please enter both email and password.');
            return;
        }

        if (!validateEmail(email)) {
            setError('Please enter a valid email address.');
            return;
        }

        setLoading(true);

        // Simulate login request
        setTimeout(() => {
            setLoading(false);

            // Dummy login check (replace with real auth)
            if (email === 'user@example.com' && password === 'Password123!') {
                // Optional: save login info if rememberMe
                if (rememberMe) {
                    localStorage.setItem('rememberEmail', email);
                } else {
                    localStorage.removeItem('rememberEmail');
                }

                navigate('/dashboard');
            } else {
                setError('Invalid email or password.');
            }
        }, 1000);
    };

    return (
        <div
            className="min-h-screen bg-cover bg-center flex items-center justify-center"
            style={{
                backgroundImage: 'url("/src/assets/login-bg.jpg")',
            }}
        >
            <div className="bg-white p-8 rounded-lg shadow-lg w-96 flex flex-col items-center">
                <img
                    src="/src/assets/rrs-logo.png"
                    alt="RRS Logo"
                    className="w-64 mb-6"
                />

                {error && <p className="text-red-500 mb-4 text-sm">{error}</p>}

                <form className="w-full flex flex-col gap-4" onSubmit={handleSubmit}>
                    <input
                        type="email"
                        aria-label="Email"
                        placeholder="Email"
                        className="border border-gray-300 rounded px-4 py-2"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                    />
                    <div className="relative">
                        <input
                            type={showPassword ? 'text' : 'password'}
                            aria-label="Password"
                            placeholder="Password"
                            className="border border-gray-300 rounded px-4 py-2 w-full"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                        />
                        <button
                            type="button"
                            className="absolute right-3 top-2 text-sm text-blue-600"
                            onClick={() => setShowPassword(!showPassword)}
                            aria-label="Toggle password visibility"
                        >
                            {showPassword ? 'Hide' : 'Show'}
                        </button>
                    </div>

                    <div className="flex justify-between items-center text-sm">
                        <label className="flex items-center gap-2">
                            <input
                                type="checkbox"
                                checked={rememberMe}
                                onChange={() => setRememberMe(!rememberMe)}
                            />
                            Remember me
                        </label>
                        <Link to="/forgot-password" className="text-blue-600 hover:underline">
                            Forgot password?
                        </Link>
                    </div>

                    <button
                        type="submit"
                        className="bg-blue-600 text-white font-semibold py-2 rounded hover:bg-blue-700 disabled:opacity-50"
                        disabled={loading}
                    >
                        {loading ? 'Logging in...' : 'Login'}
                    </button>
                </form>

                <Link
                    to="/signup"
                    className="mt-4 text-blue-600 font-semibold hover:underline"
                >
                    Don't have an account? Sign Up
                </Link>
            </div>
        </div>
    );
};

export default Login;
