import React, { useState } from 'react';

const SignUp: React.FC = () => {
    const [firstName, setFirstName] = useState('');
    const [lastName, setLastName] = useState('');
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');
    const [error, setError] = useState('');

    const handleSubmit = (e: React.FormEvent) => {
        e.preventDefault();

    if (password !== confirmPassword) {
        setError("Passwords do not match!");
        return;
    }

    setError('');
    console.log("Form submitted:", { firstName, lastName, email, password });

    // TODO: Send to backend API
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

            {error && <p className="text-red-500 mb-4">{error}</p>}

            <form className="w-full flex flex-col gap-4" onSubmit={handleSubmit}>
            <input
                type="text"
                placeholder="First Name"
                className="border border-gray-300 rounded px-4 py-2"
                value={firstName}
                onChange={(e) => setFirstName(e.target.value)}
            />
            <input
                type="text"
                placeholder="Last Name"
                className="border border-gray-300 rounded px-4 py-2"
                value={lastName}
                onChange={(e) => setLastName(e.target.value)}
            />
            <input
                type="email"
                placeholder="Email"
                className="border border-gray-300 rounded px-4 py-2"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
            />
            <input
                type="password"
                placeholder="Password"
                className="border border-gray-300 rounded px-4 py-2"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
            />
            <input
                type="password"
                placeholder="Confirm Password"
                className="border border-gray-300 rounded px-4 py-2"
                value={confirmPassword}
                onChange={(e) => setConfirmPassword(e.target.value)}
            />

            <button
                type="submit"
                className="bg-blue-600 text-white font-semibold py-2 rounded hover:bg-blue-700"
            >
                Sign Up
            </button>
            </form>

            <button className="mt-4 text-blue-600 font-semibold hover:underline">
                Already have an account? Login
            </button>
        </div>
        </div>
    );
};

export default SignUp;
