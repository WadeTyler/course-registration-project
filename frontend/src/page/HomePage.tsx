import React from 'react';

const HomePage: React.FC = () => {
    return (
        <div className="min-h-screen bg-gray-50 p-8">
            <div className="max-w-5xl mx-auto text-center">
                <h1 className="text-5xl font-bold text-yellow-600 mb-4">Welcome to Sunridge University</h1>
                <p className="text-lg text-gray-700 mb-6">
                    Your gateway to flexible, intuitive, and fast course registration.
                </p>

                <div className="bg-white shadow-md rounded-xl p-6 mb-10">
                    <h2 className="text-2xl font-semibold text-gray-800 mb-2">About Sunridge University</h2>
                    <p className="text-gray-600">
                        At Sunridge University, we believe education should be accessible and engaging. 
                        With world-class instructors, diverse course offerings, and a commitment to innovation, 
                        our mission is to empower students to thrive academically and professionally.
                    </p>
                </div>

                <div className="grid gap-6 md:grid-cols-3 text-left">
                    <div className="bg-white p-5 rounded-xl shadow hover:shadow-lg transition">
                        <h3 className="text-xl font-semibold text-yellow-700">Easy Registration</h3>
                        <p className="text-gray-600 text-sm mt-2">
                            Register for classes in just a few clicks. Our system is built for speed and simplicity.
                        </p>
                    </div>
                    <div className="bg-white p-5 rounded-xl shadow hover:shadow-lg transition">
                        <h3 className="text-xl font-semibold text-yellow-700">Flexible Scheduling</h3>
                        <p className="text-gray-600 text-sm mt-2">
                            View course times, instructors, and credit info to plan your perfect semester.
                        </p>
                    </div>
                    <div className="bg-white p-5 rounded-xl shadow hover:shadow-lg transition">
                        <h3 className="text-xl font-semibold text-yellow-700">Student Support</h3>
                        <p className="text-gray-600 text-sm mt-2">
                            Our advisors and tech support are here to help you every step of the way.
                        </p>
                    </div>
                </div>

                <div className="mt-10">
                    <button className="bg-yellow-600 text-white text-lg font-semibold px-6 py-3 rounded-full hover:bg-yellow-700 transition">
                        Explore Courses
                    </button>
                </div>
            </div>
        </div>
    );
};

export default HomePage;
