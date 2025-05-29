import React from 'react';

const SignUp: React.FC = () => {
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
                        type="text"
                        placeholder="First Name"
                        className="border border-gray-300 rounded px-4 py-2"
                    />
                    <input
                        type="text"
                        placeholder="Last Name"
                        className="border border-gray-300 rounded px-4 py-2"
                    />
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