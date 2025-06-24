import {useQuery} from "@tanstack/react-query";
import {getAuthUser} from "@/features/auth/auth.api.ts";
import {getEnrollmentsByStudentId} from "@/features/enrollment/enrollment.api.ts";
import type {User} from "@/types/user.types.ts";
import {Card, CardContent, CardDescription, CardFooter, CardHeader, CardTitle} from "../../components/ui/card.tsx";
import {Button} from "../../components/ui/button.tsx";
import {Link} from "react-router-dom";
import Loader from "../../components/Loader.tsx";
import {BookIcon} from "lucide-react";
import type {Enrollment} from "@/types/enrollment.types.ts";
import DashboardDropEnrollmentDialog from "./courses/_components/DashboardDropEnrollmentDialog.tsx";

export default function StudentDashboard() {

  const {data: authUser} = useQuery({
    queryKey: ['authUser'],
    queryFn: getAuthUser
  });

  const {data: enrollments, isPending: isLoadingEnrollments, error: loadEnrollmentsError} = useQuery({
    queryKey: ['enrollments'],
    queryFn: () => getEnrollmentsByStudentId({studentId: (authUser as User).id}),
    enabled: authUser !== undefined && authUser !== null
  });

  return (
    <div className="p-page">
      <div className="container mx-auto flex flex-col gap-8">
        <div>
          <h1 className="title">Student Dashboard</h1>
          <p>Welcome back, {authUser?.firstName}!</p>
        </div>

        <div className="grid gap-8">

          <Card>
            <CardHeader>
              <CardTitle>Courses</CardTitle>
              <CardDescription>Browse our course catalog and register for your next courses.</CardDescription>
            </CardHeader>
            <CardFooter className="w-full mt-auto">
              <Link to="/student/courses" className="w-full">
                <Button variant="outline" className="w-full">
                  <BookIcon />
                  Browse Catalog
                </Button>
              </Link>
            </CardFooter>
          </Card>

          <Card>
            <CardHeader>
              <CardTitle>Current & Upcoming Enrollments</CardTitle>
            </CardHeader>
            <hr className="mx-6"/>
            <CardContent>
              {isLoadingEnrollments && <Loader/>}
              {loadEnrollmentsError && (
                <p className="text-center text-balance text-destructive">{(loadEnrollmentsError as Error).message}</p>
              )}
              {enrollments?.filter(enrollment => enrollment.status === "STARTED" || enrollment.status === "In Progress" || enrollment.status === "NOT_STARTED")
                .map(enrollment => (
                  <EnrollmentRow enrollment={enrollment} key={enrollment.student.id + enrollment.courseSection.id} />
                ))}
            </CardContent>
          </Card>
        </div>


      </div>
    </div>
  )
}

function EnrollmentRow({enrollment}: {enrollment: Enrollment}) {
  return (
    <div className="w-full flex justify-between border-b py-4 last-of-type:border-b-0">
      <div className="flex flex-col gap-4">
        <h3 className="font-semibold tracking-tight text-base">
          <span>{enrollment.courseSection.course.department}-{enrollment.courseSection.course.code}-{enrollment.courseSection.id}</span>
        </h3>
        <div className="flex items-center gap-4">
          <p
            className="text-sm bg-secondary rounded-3xl px-2 shadow-md font-semibold text-secondary-foreground">
            {enrollment.status}
          </p>
          <p
            className="text-sm bg-secondary rounded-3xl px-2 shadow-md font-semibold text-secondary-foreground">
            Grade: {enrollment.grade}
          </p>
          <DashboardDropEnrollmentDialog enrollment={enrollment} />
        </div>
      </div>
    </div>
  )
}