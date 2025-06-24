//App.tsx
import React, {useEffect} from 'react';
import {Navigate, Route, Routes} from 'react-router-dom';

import Login from './page/auth/Login.tsx';
import SignUp from './page/auth/SignUp.tsx'
import {useQuery} from "@tanstack/react-query";
import {getAuthUser} from "./features/auth/auth.api.ts";
import {isAdmin, isInstructor, isStudent} from "./features/auth/auth.util.ts";
import AdminDashboard from "./page/admin/AdminDashboard.tsx";
import {Loader} from "lucide-react";
import Navbar from "./components/Navbar.tsx";
import ManageCoursesPage from "./page/admin/courses/ManageCoursesPage.tsx";
import ManageCoursePage from "./page/admin/courses/[courseId]/ManageCoursePage.tsx";
import ManageTermsPage from "./page/admin/terms/ManageTermsPage.tsx";
import ManageInstructorsPage from "./page/admin/instructors/ManageInstructorsPage.tsx";
import ManageStudentsPage from "./page/admin/students/ManageStudentsPage.tsx";
import ManageStudentPage from "./page/admin/students/[studentId]/ManageStudentPage.tsx";
import InstructorDashboard from "./page/instructor/InstructorDashboard.tsx";
import ManageInstructorSections from "./page/instructor/sections/ManageInstructorSections.tsx";
import ManageInstructorSection from "./page/instructor/sections/[sectionId]/ManageInstructorSection.tsx";
import StudentDashboard from "./page/student/StudentDashboard.tsx";
import CoursesCatalogPage from "./page/student/courses/CoursesCatalogPage.tsx";
import {Toaster} from "./components/ui/sonner.tsx";


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
      {authUser && <Navbar />}
      <div className="h-16"/>

      <Routes>
        {/* Public Routes */}
        <Route path="/"
               element={!authUser ? <Login/> : isAdmin(authUser) ? <Navigate to={"/admin"}/> : isInstructor(authUser) ?
                 <Navigate to={"/instructor"}/> : <Navigate to="/student"/>}/>
        <Route path="/signup" element={!authUser ? <SignUp/> : <Navigate to="/"/>}/>

        {/* Student Routes */}
        <Route path="/student" element={(authUser && isStudent(authUser)) ? <StudentDashboard/> : <Navigate to="/"/>}/>
        <Route path="/student/courses" element={(authUser && isStudent(authUser)) ? <CoursesCatalogPage/> : <Navigate to="/"/>}/>

        {/* Instructor Routes */}
        <Route path={"/instructor"}
               element={(authUser && (isInstructor(authUser) || isAdmin(authUser))) ? <InstructorDashboard/> : <Navigate to={"/"}/>}/>
        <Route path={"/instructor/sections"}
               element={(authUser && (isInstructor(authUser) || isAdmin(authUser))) ? <ManageInstructorSections/> : <Navigate to={"/"}/>}/>
        <Route path={"/instructor/sections/:sectionId"}
               element={(authUser && (isInstructor(authUser) || isAdmin(authUser))) ? <ManageInstructorSection/> : <Navigate to={"/"}/>}/>

        {/* Admin Routes */}
        <Route path="/admin" element={(authUser && isAdmin(authUser)) ? <AdminDashboard/> : <Navigate to="/"/>}/>
        <Route path="/admin/courses" element={(authUser && isAdmin(authUser)) ? <ManageCoursesPage/> : <Navigate to="/"/>}/>
        <Route path="/admin/courses/:courseId" element={(authUser && isAdmin(authUser)) ? <ManageCoursePage/> : <Navigate to="/"/>}/>
        <Route path="/admin/terms" element={(authUser && isAdmin(authUser)) ? <ManageTermsPage/> : <Navigate to="/"/>}/>
        <Route path="/admin/instructors" element={(authUser && isAdmin(authUser)) ? <ManageInstructorsPage/> : <Navigate to="/"/>}/>
        <Route path="/admin/students" element={(authUser && isAdmin(authUser)) ? <ManageStudentsPage/> : <Navigate to="/"/>}/>
        <Route path="/admin/students/:studentId" element={(authUser && isAdmin(authUser)) ? <ManageStudentPage/> : <Navigate to="/"/>}/>




      </Routes>
      <Toaster />
    </>
  );
};

export default App;