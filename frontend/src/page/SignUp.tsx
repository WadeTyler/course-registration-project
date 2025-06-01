import React, { useState } from 'react';
import { Link } from 'react-router-dom';

const SignUp: React.FC = () => {
    const [step, setStep] = useState(1);
    const [firstName, setFirstName] = useState('');
    const [lastName, setLastName] = useState('');
    const [username, setUsername] = useState('');
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');
    const [error, setError] = useState('');

    const [passwordChecks, setPasswordChecks] = useState({
        length: false,
        uppercase: false,
        lowercase: false,
        number: false,
        special: false,
    });

    const handlePasswordChange = (value: string) => {
        setPassword(value);

        setPasswordChecks({
            length: /.{8,}/.test(value),
            uppercase: /[A-Z]/.test(value),
            lowercase: /[a-z]/.test(value),
            number: /[0-9]/.test(value),
            special: /[^A-Za-z0-9]/.test(value),
        });
    };

    const handleNext = (e: React.FormEvent) => {
        e.preventDefault();
        setError('');
        setStep(2);
    };

    const handleSubmit = (e: React.FormEvent) => {
        e.preventDefault();

        const passwordRules = [
        { regex: /.{8,}/, message: "at least 8 characters" },
        { regex: /[A-Z]/, message: "at least 1 uppercase letter" },
        { regex: /[a-z]/, message: "at least 1 lowercase letter" },
        { regex: /[0-9]/, message: "at least 1 number" },
        { regex: /[^A-Za-z0-9]/, message: "at least 1 special character" },
        ];

        const failedRules = passwordRules.filter(rule => !rule.regex.test(password));

        if (failedRules.length > 0) {
        setError(`Password must have ${failedRules.map(r => r.message).join(", ")}`);
        return;
        }

        if (password !== confirmPassword) {
        setError("Passwords do not match!");
        return;
        }

        setError("");
        console.log("Form submitted:", { firstName, lastName, username, email, password });

        // TODO: Send data to backend API
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

            {step === 1 && (
            <form className="w-full flex flex-col gap-4" onSubmit={handleNext}>
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
                type="text"
                placeholder="Username"
                className="border border-gray-300 rounded px-4 py-2"
                value={username}
                onChange={(e) => setUsername(e.target.value)}
                />
                <input
                type="email"
                placeholder="Email"
                className="border border-gray-300 rounded px-4 py-2"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                />
                <button
                type="submit"
                className="bg-blue-600 text-white font-semibold py-2 rounded hover:bg-blue-700"
                >
                Next
                </button>
                <div className="flex justify-center">
                    <Link to="/" className="mt-4 text-blue-600 font-semibold hover:underline">
                    Already have an account? Login
                    </Link>
                </div>
            </form>
            )}

            {step === 2 && (
            <form className="w-full flex flex-col gap-4" onSubmit={handleSubmit}>
                <div className="bg-gray-100 p-4 rounded text-sm">
                <p className="font-semibold">Password must contain:</p>
                <ul className="list-none ml-0">
                    <li className={`flex items-center ${passwordChecks.length ? 'text-green-600' : 'text-gray-600'}`}>
                        At least 8 characters
                    </li>
                    <li className={`flex items-center ${passwordChecks.uppercase ? 'text-green-600' : 'text-gray-600'}`}>
                        At least 1 uppercase letter
                    </li>
                    <li className={`flex items-center ${passwordChecks.lowercase ? 'text-green-600' : 'text-gray-600'}`}>
                        At least 1 lowercase letter
                    </li>
                    <li className={`flex items-center ${passwordChecks.number ? 'text-green-600' : 'text-gray-600'}`}>
                        At least 1 number
                    </li>
                    <li className={`flex items-center ${passwordChecks.special ? 'text-green-600' : 'text-gray-600'}`}>
                        At least 1 special character
                    </li>
                </ul>

                </div>

                <input
                type="password"
                placeholder="Password"
                className="border border-gray-300 rounded px-4 py-2"
                value={password}
                onChange={(e) => handlePasswordChange(e.target.value)}
                />
                <input
                type="password"
                placeholder="Confirm Password"
                className="border border-gray-300 rounded px-4 py-2"
                value={confirmPassword}
                onChange={(e) => setConfirmPassword(e.target.value)}
                />

                <button
                    type="button"
                    onClick={() => setStep(1)}
                    className="text-blue-600 font-semibold hover:underline"
                    >
                    Back
                </button>

                <button
                    type="submit"
                    className="bg-blue-600 text-white font-semibold py-2 rounded hover:bg-blue-700"
                    >
                    Sign Up
                </button>
            </form>
            )}
        </div>
        </div>
    );
};

export default SignUp;
