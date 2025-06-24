import {useParams} from "react-router-dom";
import {useQuery} from "@tanstack/react-query";
import {getAssignedCourseSectionById} from "../../../../features/coursesection/coursesection.api.ts";
import Loader from "../../../../components/Loader.tsx";
import {Card, CardContent, CardHeader, CardTitle} from "../../../../components/ui/card.tsx";
import {DataTable} from "../../../../components/data-table.tsx";
import {instructorEnrollmentColumns} from "./_components/instructor-enrollment-columns.tsx";
import {
  Breadcrumb,
  BreadcrumbLink,
  BreadcrumbList,
  BreadcrumbPage,
  BreadcrumbSeparator
} from "../../../../components/ui/breadcrumb.tsx";

export default function ManageInstructorSection() {

  const {sectionId} = useParams();

  const {data: assignedSection, isPending: isLoadingSection, error: loadSectionError} = useQuery({
    queryKey: ['assignedSection'],
    queryFn: () => getAssignedCourseSectionById({sectionId: parseInt(sectionId as string)}),
    enabled: sectionId !== undefined,
  });
  const course = assignedSection?.course;

  return (
    <div className="p-page">
      <div className="container mx-auto flex flex-col gap-8">
        <div>
          <h1 className="title">Manage Section</h1>
          <p>Manage course section.</p>
        </div>

        <Breadcrumb>
          <BreadcrumbList>
            <BreadcrumbLink href="/instructor">Instructor Dashboard</BreadcrumbLink>
            <BreadcrumbSeparator />
            <BreadcrumbLink href="/instructor/sections">Manage Sections</BreadcrumbLink>
            <BreadcrumbSeparator />
            <BreadcrumbPage>{!assignedSection ? "Section" : `${course?.department}-${course?.code}-${assignedSection.id}`}</BreadcrumbPage>
          </BreadcrumbList>
        </Breadcrumb>

        {isLoadingSection && <Loader/>}
        {loadSectionError && (
          <p className="text-center text-balance text-destructive">{(loadSectionError as Error).message}</p>
        )}
        {assignedSection && (
          <>
            <section className="flex flex-col gap-8">
              <Card>
                <CardHeader>
                  <CardTitle>
                    <h2 className="font-semibold tracking-tight text-xl">
                      {course?.department}-{course?.code}-{assignedSection.id}
                    </h2>
                  </CardTitle>
                </CardHeader>
                <CardContent>
                  <p><b>Course Title:</b> {course?.title}</p>
                  <p><b>Course Description: </b>{course?.description}</p>
                  <p><b>Starts: </b>{assignedSection.term.startDate}</p>
                  <p><b>Ends: </b>{assignedSection.term.endDate}</p>
                  <p><b>Enrolled: </b>{assignedSection.enrolledCount}/{assignedSection.capacity}</p>
                  <p><b>Schedule:</b> {assignedSection.schedule}</p>
                  <p><b>Room: </b>{assignedSection.room}</p>
                </CardContent>
              </Card>
            </section>

            <section className="flex flex-col gap-8">
              <div>
                <h2 className="font-semibold tracking-tight text-xl">Enrollments</h2>
                <p>View and manage student enrollments.</p>
              </div>

              <DataTable columns={instructorEnrollmentColumns} data={assignedSection.enrollments}/>
            </section>
          </>

        )}
      </div>
    </div>
  )
}