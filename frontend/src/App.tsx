import React from 'react';
import { Routes, Route } from 'react-router-dom';

import Login from './page/Login';
import Dashboard from './page/Dashboard';
import Courses from './page/Courses';
import CourseDetails from './page/CourseDetails';
import MySchedule from './page/MySchedule';
import AdminPanel from './page/AdminPanel';

const App: React.FC = () => {
  return (
    <Routes>
      <Route path="/" element={<Login />} />
      <Route path="/dashboard" element={<Dashboard />} />
      <Route path="/courses" element={<Courses />} />
      <Route path="/courses/:id" element={<CourseDetails />} />
      <Route path="/schedule" element={<MySchedule />} />
      <Route path="/admin" element={<AdminPanel />} />
    </Routes>
  );
};

export default App;
