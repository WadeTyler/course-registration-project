// src/page/Dashboard.tsx
import React, {useState} from 'react';
import {useNavigate} from 'react-router-dom';
import type {Layout} from 'react-grid-layout';
import GridLayout from 'react-grid-layout';
import {
    Bell,
    BookOpen,
    Calendar,
    CreditCard,
    GraduationCap,
    LayoutDashboard,
    LayoutTemplate,
    LogOut,
    Minus,
    Plus,
    Settings,
    User,
} from 'lucide-react';
import 'react-grid-layout/css/styles.css';
import 'react-resizable/css/styles.css';
import '../../shake.css';
import {useMutation, useQueryClient} from "@tanstack/react-query";
import {logout} from "@/features/auth/auth.api.ts";
import Loader from "../../components/Loader.tsx";

// List of all possible widgets
const widgetOptions = [
    { key: 'classSchedule', label: 'Class Schedule', icon: <Calendar size={18} className="text-blue-600" /> },
    { key: 'classSearch', label: 'Class Search & Registration', icon: <BookOpen size={18} className="text-green-600" /> },
    { key: 'accountBalance', label: 'Account Balance', icon: <CreditCard size={18} className="text-yellow-600" /> },
    { key: 'profileInfo', label: 'Profile Info', icon: <User size={18} className="text-purple-600" /> },
    { key: 'degreeProgress', label: 'Degree Progress', icon: <GraduationCap size={18} className="text-pink-600" /> },
    { key: 'grades', label: 'Grades', icon: <LayoutDashboard size={18} className="text-indigo-600" /> },
    { key: 'notifications', label: 'Notifications / To-Do / Announcements', icon: <Bell size={18} className="text-red-600" /> },
];

const Dashboard: React.FC = () => {
    const navigate = useNavigate();
    const queryClient = useQueryClient();

    // Keep track of each widget's x, y, w, h
    const [layout, setLayout] = useState<Layout[]>([
        { i: 'classSchedule', x: 0, y: 0, w: 4, h: 2 },
        { i: 'degreeProgress', x: 4, y: 0, w: 2, h: 2 },
        { i: 'accountBalance', x: 0, y: 2, w: 3, h: 2 },
        { i: 'grades', x: 3, y: 2, w: 3, h: 2 },
    ]);

    const [showModal, setShowModal] = useState(false);
    const [removeMode, setRemoveMode] = useState(false);

    // Reset to the original 4‐widget layout
    const loadPrebuiltScreen = () => {
        setLayout([
        { i: 'classSchedule', x: 0, y: 0, w: 4, h: 2 },
        { i: 'degreeProgress', x: 4, y: 0, w: 2, h: 2 },
        { i: 'accountBalance', x: 0, y: 2, w: 3, h: 2 },
        { i: 'grades', x: 3, y: 2, w: 3, h: 2 },
        ]);
        setRemoveMode(false);
        setShowModal(false);
    };

    // Add a widget at the first available “row” (y=Infinity)
    const addWidget = (widgetKey: string) => {
        if (!layout.find((w) => w.i === widgetKey)) {
        setLayout((prev) => [
            ...prev,
            { i: widgetKey, x: 0, y: Infinity, w: 3, h: 2 }
        ]);
        }
        setShowModal(false);
    };

    const removeWidget = (widgetKey: string) => {
        setLayout((prev) => prev.filter((w) => w.i !== widgetKey));
        // Leave removeMode = true so you can remove multiple in succession,
        // or toggle off if you’d like to exit automatically:
        // setRemoveMode(false);
    };

    const handleLayoutChange = (newLayout: Layout[]) => {
        setLayout(newLayout);
    };

    /// Signout functionality
    const {mutate:signoutMutation, isPending:isSigningOut} = useMutation({
        mutationFn: logout,
        onSuccess: () => {
            queryClient.invalidateQueries({queryKey: ['authUser']});
            navigate("/");
        }
    });

    function handleSignOut() {
        signoutMutation();
    }

    return (
        <div className="flex min-h-screen bg-gray-100">
        {/* Sidebar */}
        <aside className="w-72 bg-gradient-to-b from-blue-900 to-blue-700 text-white p-6 flex flex-col">
            <img
            src="/src/assets/rrs-logo-white.png"
            alt="Logo"
            className="w-60 mx-auto mb-12"
            />
            <div className="text-center mb-8">
            <h1 className="text-lg font-bold">Welcome,</h1>
            <h2 className="text-lg font-bold">Walter Andrzejewski</h2>
            <p className="text-sm">Student ID: 1107893</p>
            <p className="text-sm">Computer Science B.S.</p>
            </div>

            <nav className="flex flex-col gap-3 mt-auto">
            <button
                onClick={() => setShowModal(true)}
                className="flex items-center gap-2 bg-blue-800 hover:bg-blue-600 p-2 rounded"
            >
                <Plus size={18} /> Add Widget
            </button>

            <button
                onClick={() => setRemoveMode(!removeMode)}
                className={`flex items-center gap-2 p-2 rounded ${
                removeMode ? 'bg-red-700 hover:bg-red-600' : 'bg-blue-800 hover:bg-blue-600'
                }`}
            >
                <Minus size={18} />{removeMode ? ' Exit Remove Mode' : ' Remove Widget'}
            </button>

            <button
                onClick={loadPrebuiltScreen}
                className="flex items-center gap-2 bg-blue-800 hover:bg-blue-600 p-2 rounded"
            >
                <LayoutTemplate size={18} /> Load Prebuilt Screen
            </button>

            <button
                onClick={() => navigate('/profile')}
                className="flex items-center gap-2 bg-blue-800 hover:bg-blue-600 p-2 rounded"
            >
                <Settings size={18} /> Profile Settings
            </button>

            <button
                onClick={handleSignOut}
                disabled={isSigningOut}
                className="flex items-center gap-2 bg-blue-800 hover:bg-blue-600 p-2 rounded"
            >
                {isSigningOut ? <Loader /> : (<><LogOut size={18} /> Sign Out</>)}
            </button>
            </nav>
        </aside>

        {/* Dashboard Content */}
        <main className="flex-1 p-6">
            <h1 className="text-3xl font-bold mb-6 text-gray-800">My Dashboard</h1>

            <GridLayout
            className="layout"
            layout={layout}
            cols={6}
            rowHeight={120}
            width={1500}
            isDraggable={!removeMode}
            isResizable={!removeMode}
            compactType={null}
            preventCollision={false}
            onLayoutChange={handleLayoutChange}
            >
            {layout.map((widget) => (
                <div
                key={widget.i}
                className={`bg-white p-4 rounded-lg shadow relative ${
                    removeMode ? 'shake' : ''
                }`}
                >
                <h2 className="font-semibold">
                    {widgetOptions.find((w) => w.key === widget.i)?.label || widget.i}
                </h2>
                {removeMode && (
                    <button
                    onClick={() => removeWidget(widget.i)}
                    className="absolute top-2 right-2 bg-red-600 text-white rounded-full w-6 h-6 flex items-center justify-center hover:bg-red-700"
                    >
                    &times;
                    </button>
                )}
                </div>
            ))}
            </GridLayout>
        </main>

        {/* Add Widget Modal */}
        {showModal && (
            <div className="fixed inset-0 bg-black/20 backdrop-blur-xs flex items-center justify-center z-50">
            <div className="bg-white rounded-lg shadow p-6 w-96">
                <h2 className="text-xl font-bold mb-4">Add Widget</h2>
                <ul className="space-y-3">
                {widgetOptions.map((widget) => (
                    <li
                    key={widget.key}
                    className="flex items-center justify-between hover:bg-gray-100 p-2 rounded cursor-pointer"
                    onClick={() => addWidget(widget.key)}
                    >
                    <div className="flex items-center gap-2">
                        {widget.icon}
                        <span>{widget.label}</span>
                    </div>
                    <Plus size={16} className="text-gray-600" />
                    </li>
                ))}
                </ul>
                <button
                onClick={() => setShowModal(false)}
                className="mt-4 text-blue-600 hover:underline text-sm"
                >
                Close
                </button>
            </div>
            </div>
        )}
        </div>
    );
};

export default Dashboard;