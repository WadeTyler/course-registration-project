import React, {useEffect, useState} from 'react';
import {Link, useNavigate} from 'react-router-dom';
import {useMutation, useQueryClient} from "@tanstack/react-query";
import {signup} from "@/features/auth/auth.api.ts";
import type {User} from "@/types/user.types.ts";
import Loader from "../../components/Loader.tsx";

const SignUp: React.FC = () => {
    useEffect(() => {
        document.title = "SignUp | Register R Us";
    }, []);

    const [step, setStep] = useState(1);
    const [firstName, setFirstName] = useState('');
    const [lastName, setLastName] = useState('');
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');
    const [error, setError] = useState('');
    const [success, setSuccess] = useState('');
    const [showPassword, setShowPassword] = useState(false);

    const navigate = useNavigate();
    const queryClient = useQueryClient();

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
        setSuccess('');

        if (!firstName || !lastName || !username) {
            setError('Please fill in all fields.');
            return;
        }

        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (!emailRegex.test(username)) {
            setError('Please enter a valid email address.');
            return;
        }

        setStep(2);
    };

    // Handle signup
    const {mutate:signupMutation, isPending: isSigningUp} = useMutation({
        mutationFn: signup,
        onSuccess: async (user: User) => {
          queryClient.setQueryData(['authUser'], user);
          navigate("/dashboard");
        },
        onError: (e) => {
          setError((e as Error).message);
        }
    })

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setError('');
        setSuccess('');

        const passwordRules = [
            { regex: /.{8,}/, message: "at least 8 characters" },
            { regex: /[A-Z]/, message: "at least 1 uppercase letter" },
            { regex: /[a-z]/, message: "at least 1 lowercase letter" },
            { regex: /[0-9]/, message: "at least 1 number" },
            { regex: /[^A-Za-z0-9]/, message: "at least 1 special character" },
        ];

        const failedRules = passwordRules.filter(rule => !rule.regex.test(password));
        if (failedRules.length > 0) {
            setError(`Password must have ${failedRules.map(r => r.message).join(', ')}`);
            return;
        }

        if (password !== confirmPassword) {
            setError("Passwords do not match!");
            return;
        }

        signupMutation({firstName, lastName, username, password, confirmPassword});
    };

    return (
        <div
            className="min-h-screen bg-cover bg-center flex items-center justify-center"
            style={{ backgroundImage: 'url("/login-bg.jpg")' }}
        >
            <div className="bg-white p-8 rounded-lg shadow-lg w-96 flex flex-col items-center">
                <img
                    src="/rrs-logo.png"
                    alt="RRS Logo"
                    className="w-64 mb-6"
                />

                <div className="text-sm text-gray-600 mb-2">Step {step} of 2</div>

                {error && <p className="text-red-500 mb-4" role="alert">{error}</p>}
                {success && <p className="text-green-600 mb-4" role="alert">{success}</p>}

                {step === 1 && (
                    <form className="w-full flex flex-col gap-4" onSubmit={handleNext}>
                        <label>
                            <input
                                type="text"
                                placeholder="First Name"
                                className="border border-gray-300 rounded px-4 py-2 w-full"
                                value={firstName}
                                onChange={(e) => setFirstName(e.target.value)}
                                required
                                aria-label="First Name"
                            />
                        </label>
                        <label>
                            <input
                                type="text"
                                placeholder="Last Name"
                                className="border border-gray-300 rounded px-4 py-2 w-full"
                                value={lastName}
                                onChange={(e) => setLastName(e.target.value)}
                                required
                                aria-label="Last Name"
                            />
                        </label>
                        <label>
                            <input
                                type="text"
                                placeholder="Email"
                                className="border border-gray-300 rounded px-4 py-2 w-full"
                                value={username}
                                onChange={(e) => setUsername(e.target.value)}
                                required
                                aria-label="Email"
                            />
                        </label>
                        <button
                            type="submit"
                            className="bg-blue-600 text-white font-semibold py-2 rounded hover:bg-blue-700 disabled:opacity-50"
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
                            <p className="font-semibold mb-2">Password must contain:</p>
                            <ul className="list-none space-y-1">
                                <li className={`${passwordChecks.length ? 'text-green-600' : 'text-gray-600'}`}>
                                    • At least 8 characters
                                </li>
                                <li className={`${passwordChecks.uppercase ? 'text-green-600' : 'text-gray-600'}`}>
                                    • At least 1 uppercase letter
                                </li>
                                <li className={`${passwordChecks.lowercase ? 'text-green-600' : 'text-gray-600'}`}>
                                    • At least 1 lowercase letter
                                </li>
                                <li className={`${passwordChecks.number ? 'text-green-600' : 'text-gray-600'}`}>
                                    • At least 1 number
                                </li>
                                <li className={`${passwordChecks.special ? 'text-green-600' : 'text-gray-600'}`}>
                                    • At least 1 special character
                                </li>
                            </ul>
                        </div>

                        <label>
                            <input
                                type={showPassword ? "text" : "password"}
                                placeholder="Password"
                                className="border border-gray-300 rounded px-4 py-2 w-full"
                                value={password}
                                onChange={(e) => handlePasswordChange(e.target.value)}
                                aria-label="Password"
                            />
                        </label>
                        <label>
                            <input
                                type={showPassword ? "text" : "password"}
                                placeholder="Confirm Password"
                                className="border border-gray-300 rounded px-4 py-2 w-full"
                                value={confirmPassword}
                                onChange={(e) => setConfirmPassword(e.target.value)}
                                aria-label="Confirm Password"
                            />
                        </label>

                        <button
                            type="button"
                            onClick={() => setStep(1)}
                            className="text-blue-600 font-semibold hover:underline"
                        >
                            Back
                        </button>

                        <div className="flex items-center gap-2">
                            <input
                                type="checkbox"
                                id="showPassword"
                                checked={showPassword}
                                onChange={() => setShowPassword(prev => !prev)}
                            />
                            <label htmlFor="showPassword" className="text-sm text-gray-700">Show Password</label>
                        </div>

                        <button
                            type="submit"
                            className="bg-blue-600 text-white font-semibold py-2 rounded hover:bg-blue-700 disabled:opacity-50"
                            disabled={isSigningUp}
                        >
                            {isSigningUp ? <Loader /> : 'Sign Up'}
                        </button>
                    </form>
                )}
            </div>
        </div>
    );
};

export default SignUp;