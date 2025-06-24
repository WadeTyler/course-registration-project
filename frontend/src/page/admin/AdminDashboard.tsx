import {Card, CardContent, CardDescription, CardHeader, CardTitle} from "../../components/ui/card.tsx";
import {Button} from "../../components/ui/button.tsx";
import {Link} from "react-router-dom";
import {CalendarIcon, ClipboardIcon, PencilIcon, UsersIcon} from "lucide-react";

export default function AdminDashboard() {
  return (
    <div className="p-page">
      <div className="container mx-auto flex flex-col gap-8">

        <div>
          <h1 className="title">Admin Dashboard</h1>
          <p>View and manage your students, courses, and instructors.</p>
        </div>

        <section className="grid lg:grid-cols-2 gap-8">
          <Card>
            <CardHeader>
              <CardTitle>Courses</CardTitle>
              <CardDescription>View and manage your courses.</CardDescription>
            </CardHeader>
            <CardContent>
              <Link to="/admin/courses">
                <Button variant="outline" className="w-full">
                  <PencilIcon />
                  Manage Courses
                </Button>
              </Link>
            </CardContent>
          </Card>

          <Card>
            <CardHeader>
              <CardTitle>Terms</CardTitle>
              <CardDescription>View and manage your terms.</CardDescription>
            </CardHeader>
            <CardContent>
              <Link to="/admin/terms">
                <Button variant="outline" className="w-full">
                  <CalendarIcon />
                  Manage Terms
                </Button>
              </Link>
            </CardContent>
          </Card>

          <Card>
            <CardHeader>
              <CardTitle>Instructors</CardTitle>
              <CardDescription>View and manage your instructors.</CardDescription>
            </CardHeader>
            <CardContent>
              <Link to="/admin/instructors">
                <Button variant="outline" className="w-full">
                  <ClipboardIcon />
                  Manage Instructors
                </Button>
              </Link>
            </CardContent>
          </Card>

          <Card>
            <CardHeader>
              <CardTitle>Students</CardTitle>
              <CardDescription>View and manage your students.</CardDescription>
            </CardHeader>
            <CardContent>
              <Link to="/admin/students">
                <Button variant="outline" className="w-full">
                  <UsersIcon />
                  Manage Students
                </Button>
              </Link>
            </CardContent>
          </Card>
        </section>

      </div>
    </div>
  )
}