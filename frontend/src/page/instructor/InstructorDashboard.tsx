import {useQuery} from "@tanstack/react-query";
import {getAuthUser} from "@/features/auth/auth.api.ts";
import {Card, CardDescription, CardFooter, CardHeader, CardTitle} from "../../components/ui/card.tsx";
import {Link} from "react-router-dom";
import {Button} from "../../components/ui/button.tsx";
import {useEffect} from "react";

export default function InstructorDashboard() {
  useEffect(() => {
    document.title = "Instructor Dashboard | Register R Us";
  }, []);

  const {data: authUser} = useQuery({
    queryKey: ['authUser'],
    queryFn: getAuthUser
  });

  return (
    <div className="p-page">
      <div className="container mx-auto flex flex-col gap-8">
        <div>
          <h1 className="title">Instructor Dashboard</h1>
          <p>Welcome back, {authUser?.firstName} {authUser?.lastName}!</p>
        </div>

        <div className="grid">
          <Card>
            <CardHeader>
              <CardTitle>Course Sections</CardTitle>
              <CardDescription>View and manage your course sections.</CardDescription>
            </CardHeader>
            <CardFooter>
              <Link to={"/instructor/sections"}>
                <Button variant="outline">
                  Manage Sections
                </Button>
              </Link>
            </CardFooter>
          </Card>
        </div>

      </div>
    </div>
  )
}