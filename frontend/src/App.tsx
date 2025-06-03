import React from 'react';
import { Routes, Route } from 'react-router-dom';

import Login from './page/Login';
import SignUp from './page/SignUp'
import Dashboard from './page/Dashboard';
import Courses from './page/Courses';
import CourseDetails from './page/CourseDetails';
import MySchedule from './page/MySchedule';
import AdminPanel from './page/AdminPanel';
import ProfileSettings from './page/ProfileSettings';
import Billing from './page/Billing';

const App: React.FC = () => {
  return (
    <Routes>
      <Route path="/" element={<Login />} />
      <Route path="/signup" element={<SignUp />} />
      <Route path="/dashboard" element={<Dashboard />} />
      <Route path="/courses" element={<Courses />} />
      <Route path="/courses/:id" element={<CourseDetails />} />
      <Route path="/schedule" element={<MySchedule />} />
      <Route path="/admin" element={<AdminPanel />} />
      <Route path="/profile" element={<ProfileSettings />} />
      <Route path="/billing" element={<Billing />} />
    </Routes>
  );
};

export default App;
