import React from 'react';

interface Course {
    id: string;
    title: string;
    code: string;
    description: string;
    instructor: string;
    credits: number;
    schedule: string;
}

const mockCourses: Course[] = [
    {
        id: '1',
        title: 'Introduction to Psychology',
        code: 'PSY101',
        description: 'Explore the fundamentals of human behavior, cognitive processes, and emotional responses.',
        instructor: 'Dr. Jane Thompson',
        credits: 3,
        schedule: 'Mon & Wed, 10:00AM - 11:15AM'
    },
    {
        id: '2',
        title: 'Data Structures and Algorithms',
        code: 'CS202',
        description: 'Learn efficient ways to store and manipulate data, including trees, graphs, and sorting algorithms.',
        instructor: 'Prof. Alan Chen',
        credits: 4,
        schedule: 'Tue & Thu, 1:00PM - 2:45PM'
    },
    {
        id: '3',
        title: 'Modern World History',
        code: 'HIS150',
        description: 'Survey the major events and ideologies shaping the world from 1500 to present.',
        instructor: 'Dr. Maria Lopez',
        credits: 3,
        schedule: 'Mon, Wed & Fri, 9:00AM - 9:50AM'
    },
    {
        id: '4',
        title: 'Calculus I',
        code: 'MATH101',
        description: 'An introduction to differential and integral calculus for science and engineering majors.',
        instructor: 'Prof. David Nguyen',
        credits: 4,
        schedule: 'Tue & Thu, 3:00PM - 4:45PM'
    },
    {
        id: '5',
        title: 'Creative Writing Workshop',
        code: 'ENG205',
        description: 'A hands-on workshop focused on crafting short stories, poetry, and creative nonfiction.',
        instructor: 'Ms. Sarah Allen',
        credits: 3,
        schedule: 'Wed, 2:00PM - 4:30PM'
    },
    {
        id: '6',
        title: 'Introduction to Philosophy',
        code: 'PHIL101',
        description: 'Discuss fundamental questions about existence, knowledge, ethics, and the mind.',
        instructor: 'Dr. Liam Walker',
        credits: 3,
        schedule: 'Tue & Thu, 10:00AM - 11:15AM'
    },
    {
        id: '7',
        title: 'Environmental Science',
        code: 'ENV110',
        description: 'Study human impact on the environment and explore sustainability and ecological systems.',
        instructor: 'Dr. Nina Patel',
        credits: 3,
        schedule: 'Mon & Wed, 12:00PM - 1:15PM'
    },
    {
        id: '8',
        title: 'Fundamentals of Marketing',
        code: 'BUS120',
        description: 'Learn the principles of marketing including product, price, promotion, and distribution.',
        instructor: 'Prof. Tyler Morgan',
        credits: 3,
        schedule: 'Fri, 10:00AM - 12:30PM'
    },
];

const CourseDetails: React.FC = () => {
    return (
        <div className="p-6">
            <h1 className="text-4xl font-bold text-yellow-600 mb-6">Course Details</h1>
            <div className="grid gap-6 md:grid-cols-2">
                {mockCourses.map((course) => (
                    <div key={course.id} className="bg-white shadow-lg rounded-2xl p-6 border border-gray-200">
                        <h2 className="text-xl font-semibold text-gray-800">{course.title} <span className="text-sm text-gray-500">({course.code})</span></h2>
                        <p className="text-sm text-gray-600 mt-2">{course.description}</p>
                        <div className="mt-4 text-sm text-gray-700">
                            <p><strong>Instructor:</strong> {course.instructor}</p>
                            <p><strong>Credits:</strong> {course.credits}</p>
                            <p><strong>Schedule:</strong> {course.schedule}</p>
                        </div>
                    </div>
                ))}
            </div>
        </div>
    );
};

export default CourseDetails;
