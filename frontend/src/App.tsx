//App.tsx
import React, {useEffect} from 'react';
import {Navigate, Route, Routes} from 'react-router-dom';

import Login from './page/Login';
import SignUp from './page/SignUp'
import Dashboard from './page/Dashboard';
import Courses from './page/Courses';
import CourseDetails from './page/CourseDetails';
import MySchedule from './page/MySchedule';
import ProfileSettings from './page/ProfileSettings';
import Billing from './page/Billing';
import {useQuery} from "@tanstack/react-query";
import {getAuthUser} from "./features/auth/auth.api.ts";
import {isAdmin, isInstructor, isStudent} from "./features/auth/auth.util.ts";
import AdminDashboard from "./page/admin/AdminDashboard.tsx";
import {Loader} from "lucide-react";
import Navbar from "./components/Navbar.tsx";
import InstructorDashboard from "./page/InstructorDashboard.tsx";
import ManageCoursesPage from "./page/admin/courses/ManageCoursesPage.tsx";
import ManageCoursePage from "./page/admin/courses/[courseId]/ManageCoursePage.tsx";
import ManageTermsPage from "./page/admin/terms/ManageTermsPage.tsx";


const App: React.FC = () => {

  const {data: authUser, isPending: isLoadingAuthUser} = useQuery({
    queryKey: ['authUser'],
    queryFn: getAuthUser
  });

  console.log(authUser);

  useEffect(() => {
    if (authUser) {
      console.log(isAdmin(authUser));
    }
  }, [authUser])

  if (isLoadingAuthUser) return (
    <div className="p-page">
      <div className="container mx-auto">
        <Loader size="16" className="animate-spin mx-auto"/>
      </div>
    </div>
  )

  return (
    <>
      <Navbar/>
      <div className="h-16"/>

      <Routes>
        {/* Public Routes */}
        <Route path="/"
               element={!authUser ? <Login/> : isAdmin(authUser) ? <Navigate to={"/admin"}/> : isInstructor(authUser) ?
                 <Navigate to={"/instructor"}/> : <Navigate to="/dashboard"/>}/>
        <Route path="/signup" element={!authUser ? <SignUp/> : <Navigate to="/"/>}/>


        {/* Student Routes */}
        <Route path="/dashboard" element={(authUser && isStudent(authUser)) ? <Dashboard/> : <Navigate to="/"/>}/>
        <Route path="/courses" element={<Courses/>}/>
        <Route path="/courses/:id" element={<CourseDetails/>}/>
        <Route path="/schedule" element={<MySchedule/>}/>
        <Route path="/profile" element={<ProfileSettings/>}/>
        <Route path="/billing" element={<Billing/>}/>

        {/* Instructor Routes */}
        <Route path={"/instructor"}
               element={(authUser && isInstructor(authUser)) ? <InstructorDashboard/> : <Navigate to={"/"}/>}/>

        {/* Admin Routes */}
        <Route path="/admin" element={(authUser && isAdmin(authUser)) ? <AdminDashboard/> : <Navigate to="/"/>}/>
        <Route path="/admin/courses" element={(authUser && isAdmin(authUser)) ? <ManageCoursesPage/> : <Navigate to="/"/>}/>
        <Route path="/admin/courses/:courseId" element={(authUser && isAdmin(authUser)) ? <ManageCoursePage/> : <Navigate to="/"/>}/>
        <Route path="/admin/terms" element={(authUser && isAdmin(authUser)) ? <ManageTermsPage/> : <Navigate to="/"/>}/>




      </Routes>
    </>
  );
};

export default App;