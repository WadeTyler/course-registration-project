//App.tsx
import React from 'react';
import {Navigate, Route, Routes} from 'react-router-dom';

import Login from './page/Login';
import SignUp from './page/SignUp'
import Dashboard from './page/Dashboard';
import Courses from './page/Courses';
import CourseDetails from './page/CourseDetails';
import MySchedule from './page/MySchedule';
import AdminPanel from './page/AdminPanel';
import ProfileSettings from './page/ProfileSettings';
import Billing from './page/Billing';
import {useQuery} from "@tanstack/react-query";
import {getAuthUser} from "./features/auth/auth.api.ts";


const App: React.FC = () => {

  const {data: authUser} = useQuery({
    queryKey: ['authUser'],
    queryFn: getAuthUser
  });


  return (
      <Routes>
        <Route path="/" element={!authUser ? <Login/> : <Navigate to="/dashboard" />} />
        <Route path="/signup" element={!authUser ? <SignUp /> : <Navigate to="/dashboard" />}/>
        <Route path="/dashboard" element={authUser ? <Dashboard/> : <Navigate to="/" />}/>
        <Route path="/courses" element={<Courses/>}/>
        <Route path="/courses/:id" element={<CourseDetails/>}/>
        <Route path="/schedule" element={<MySchedule/>}/>
        <Route path="/admin" element={<AdminPanel/>}/>
        <Route path="/profile" element={<ProfileSettings/>}/>
        <Route path="/billing" element={<Billing/>}/>
      </Routes>
  );
};

export default App;