import React from 'react';
import { Link } from 'react-router-dom'

const Login: React.FC = () => {
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

                <form className="w-full flex flex-col gap-4">
                    <input
                        type="email"
                        placeholder="Email"
                        className="border border-gray-300 rounded px-4 py-2"
                    />
                    <input
                        type="password"
                        placeholder="Password"
                        className="border border-gray-300 rounded px-4 py-2"
                    />
                    
                    <button
                        type="submit"
                        className="bg-blue-600 text-white font-semibold py-2 rounded hover:bg-blue-700"
                    >
                        Login
                    </button>
                </form>

                <Link to="/signup" className="mt-4 text-blue-600 font-semibold hover:underline">
                    Sign Up
                </Link>
            </div>
        </div>
    );
};

export default Login;