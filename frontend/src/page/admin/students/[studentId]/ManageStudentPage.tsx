import {
  Breadcrumb,
  BreadcrumbItem,
  BreadcrumbLink,
  BreadcrumbList,
  BreadcrumbPage,
  BreadcrumbSeparator
} from "@/components/ui/breadcrumb.tsx";
import {useQuery, useQueryClient} from "@tanstack/react-query";
import {getAllUsers} from "@/features/auth/auth.api.ts";
import {useParams} from "react-router-dom";
import {useEffect, useState} from "react";
import Loader from "../../../../components/Loader.tsx";
import EditUserSheet from "../_components/EditUserSheet.tsx";
import {Card, CardContent, CardHeader, CardTitle} from "@/components/ui/card.tsx";
import {getUserRole} from "@/features/auth/auth.util.ts";
import {getEnrollmentsByStudentId} from "@/features/enrollment/enrollment.api.ts";
import {DataTable} from "@/components/data-table.tsx";
import {enrollmentColumns} from "@/components/enrollment/enrollment-columns.tsx";

export default function ManageStudentPage() {
  useEffect(() => {
    document.title = "Manage Student | Register R Us";
  }, []);

  const queryClient = useQueryClient();
  const {studentId} = useParams();

  const {data: users, isPending: isLoadingUsers, error: loadUsersError} = useQuery({
    queryKey: ['users'],
    queryFn: getAllUsers
  });

  const {data: enrollments, isPending: isLoadingEnrollments, error: loadEnrollmentsError} = useQuery({
    queryKey: ['enrollments'],
    queryFn: () => getEnrollmentsByStudentId({studentId: parseInt(studentId as string)}),
    enabled: studentId !== undefined
  });

  const [user, setUser] = useState(users?.find(u => u.id === parseInt(studentId as string)));

  useEffect(() => {
    setUser(users?.find(u => u.id === parseInt(studentId as string)));
    queryClient.invalidateQueries({queryKey: ['enrollments']});
  }, [studentId, users]);


  return (
    <div className="p-page">
      <div className="container mx-auto flex flex-col gap-8">
        <Breadcrumb>
          <BreadcrumbList>
            <BreadcrumbItem>
              <BreadcrumbLink href="/admin">Admin Dashboard</BreadcrumbLink>
            </BreadcrumbItem>
            <BreadcrumbSeparator/>
            <BreadcrumbItem>
              <BreadcrumbLink href="/admin/students">Manage Students</BreadcrumbLink>
            </BreadcrumbItem>
            <BreadcrumbSeparator/>
            <BreadcrumbItem>
              <BreadcrumbPage>{!user ? "Student" : user.firstName + " " + user.lastName}</BreadcrumbPage>
            </BreadcrumbItem>
          </BreadcrumbList>
        </Breadcrumb>

        <div>
          <h1 className="title">Manage {!user ? "Student" : user.firstName + " " + user.lastName}</h1>
          <p>View and Manage {!user ? "Student" : user.firstName + " " + user.lastName}</p>
        </div>

        {isLoadingUsers && <Loader/>}
        {loadUsersError && (
          <p className="text-center text-destructive text-balance">{(loadUsersError as Error).message}</p>
        )}
        {!loadUsersError && user && (
          <>
            {/* User Card */}
            <section className="flex flex-col gap-8 w-full">
              {/* Action Button */}
              <div className="ml-auto flex items-center gap-4">
                <EditUserSheet user={user}/>
              </div>

              <Card>
                <CardHeader>
                  <CardTitle>
                    <h3 className="font-semibold tracking-tight text-xl">{user.firstName} {user.lastName}</h3>
                  </CardTitle>
                </CardHeader>
                <CardContent>
                  <p><b>ID: </b>{user.id}</p>
                  <p><b>Email: </b>{user.username}</p>
                  <p><b>Role: </b>{getUserRole(user)}</p>
                  <p><b>User Since: </b>{new Date(user.createdAt).toLocaleDateString()}</p>
                </CardContent>
              </Card>
            </section>

            <section className="flex flex-col gap-8 w-full">
              <div>
                <h2 className="font-semibold tracking-tight text-xl">Enrollments</h2>
                <p>View user enrollments.</p>
              </div>

              {isLoadingEnrollments && <Loader/>}
              {loadEnrollmentsError && (
                <p className="text-destructive text-center text-balance">{(loadEnrollmentsError as Error).message}</p>
              )}
              {enrollments && (
                <DataTable columns={enrollmentColumns} data={enrollments}/>
              )}
            </section>
          </>
        )}


      </div>
    </div>
  )
}