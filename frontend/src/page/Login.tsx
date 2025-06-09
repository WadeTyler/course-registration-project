import React, {type FormEvent, useState} from 'react';
import {Link, useNavigate} from 'react-router-dom'
import {useMutation, useQueryClient} from "@tanstack/react-query";
import {login} from "../features/auth/auth.api.ts";
import Loader from "../components/Loader.tsx";
import type {User} from "../types/user.types.ts";

const Login: React.FC = () => {

    // States
    const [username, setUsername] = useState<string>("")
    const [password, setPassword] = useState<string>("");

    // QueryClient & Navigation
    const queryClient = useQueryClient();
    const navigate = useNavigate();

    // Mutation
    const {mutate: loginMutation, isPending:isLoggingIn, error:loginError} = useMutation({
        mutationFn: login,
        onSuccess: async (user: User) => {
            queryClient.setQueryData(['authUser'], user);
            navigate("/dashboard");
        }
    });

    // Form Event
    async function handleSubmit(e: FormEvent) {
        e.preventDefault();
        if (isLoggingIn || !username || !password) return;
        loginMutation({username, password});
    }

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

                <form className="w-full flex flex-col gap-4" onSubmit={handleSubmit}>
                    <input
                        id="username"
                        name="username"
                        type="email"
                        placeholder="Email"
                        value={username}
                        onChange={e => setUsername(e.target.value)}
                        className="border border-gray-300 rounded px-4 py-2"
                    />
                    <input
                        id="password"
                        name="password"
                        type="password"
                        placeholder="Password"
                        value={password}
                        onChange={e => setPassword(e.target.value)}
                        className="border border-gray-300 rounded px-4 py-2"
                    />

                    {loginError && <p className="text-red-400">{(loginError as Error).message}</p>}
                    
                    <button
                        type="submit"
                        disabled={isLoggingIn}
                        className="bg-blue-600 text-white font-semibold py-2 rounded hover:bg-blue-700 text-center"
                    >
                        {isLoggingIn ? <Loader /> : "Login"}
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