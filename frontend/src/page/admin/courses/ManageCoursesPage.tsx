import {useQuery} from "@tanstack/react-query";
import {getAllCourses} from "../../../features/course/course.api.ts";
import Loader from "../../../components/Loader.tsx";
import {ManageCoursesTable} from "./_components/manage-courses-table.tsx";
import {columns} from "./_components/columns.tsx";
import {
  Breadcrumb,
  BreadcrumbItem,
  BreadcrumbLink,
  BreadcrumbList,
  BreadcrumbPage,
  BreadcrumbSeparator
} from "../../../components/ui/breadcrumb.tsx";
import CreateCourseSheet from "./_components/CreateCourseSheet.tsx";

export default function ManageCoursesPage() {

  const {data: courses, isPending: isLoadingCourses, error: loadCoursesError} = useQuery({
    queryKey: ['courses'],
    queryFn: getAllCourses
  })

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
              <BreadcrumbPage>Manage Courses</BreadcrumbPage>
            </BreadcrumbItem>
          </BreadcrumbList>
        </Breadcrumb>

        <div>
          <h1 className="title">Manage Courses</h1>
          <p>View and manage your courses.</p>
        </div>

        {isLoadingCourses && <Loader/>}
        {loadCoursesError && (
          <p className="text-destructive text-center text-balance">
            {(loadCoursesError as Error).message}
          </p>
        )}
        {!loadCoursesError && courses && (
          <>

            <div className="ml-auto flex items-center gap-4">
              <CreateCourseSheet />
            </div>
            <div className='w-full bg-secondary rounded-md shadow-md p-4'>
              <div className="w-full bg-background">
                <ManageCoursesTable columns={columns} data={courses}/>
              </div>
            </div>
          </>
        )}
      </div>
    </div>
  )
}