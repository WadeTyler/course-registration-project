import React, { useEffect, useState } from 'react';

interface Course {
    id: string;
    title: string;
    code: string;
    instructor: string;
    time: string;
    location: string;
}

const MySchedule: React.FC = () => {
    const [courses, setCourses] = useState<Course[]>([]);
    const [loading, setLoading] = useState<boolean>(true);

    useEffect(() => {
        // Simulate fetching data from an API or database
        // Replace this with a real fetch call later
        const fetchSchedule = async () => {
            setLoading(true);
            try {
                // const response = await fetch('/api/student/schedule');
                // const data = await response.json();
                // setCourses(data);

                // Temporary: simulate empty state
                setCourses([]);
            } catch (error) {
                console.error('Failed to fetch schedule:', error);
            } finally {
                setLoading(false);
            }
        };

        fetchSchedule();
    }, []);

    return (
        <div className="p-6">
            <h1 className="text-4xl font-bold text-pink-600 mb-6">My Schedule</h1>

            {loading ? (
                <p className="text-gray-500 text-lg">Loading your schedule...</p>
            ) : courses.length === 0 ? (
                <p className="text-gray-600 text-lg">You are not enrolled in any courses yet.</p>
            ) : (
                <div className="space-y-4">
                    {courses.map(course => (
                        <div
                            key={course.id}
                            className="bg-white border border-gray-200 rounded-2xl shadow-sm p-5 hover:shadow-md transition"
                        >
                            <h2 className="text-xl font-semibold text-gray-800">
                                {course.title} <span className="text-sm text-gray-500">({course.code})</span>
                            </h2>
                            <p className="text-sm text-gray-600 mt-1">
                                <strong>Instructor:</strong> {course.instructor}
                            </p>
                            <p className="text-sm text-gray-600">
                                <strong>Time:</strong> {course.time}
                            </p>
                            <p className="text-sm text-gray-600">
                                <strong>Location:</strong> {course.location}
                            </p>
                        </div>
                    ))}
                </div>
            )}
        </div>
    );
};

export default MySchedule;
