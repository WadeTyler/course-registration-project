import {Link, useParams} from "react-router-dom";
import {useQuery, useQueryClient} from "@tanstack/react-query";
import {getCourseById} from "../../../../features/course/course.api.ts";
import {useEffect} from "react";
import Loader from "../../../../components/Loader.tsx";
import {Card, CardContent, CardDescription, CardHeader, CardTitle} from "../../../../components/ui/card.tsx";
import EditCourseSheet from "./_components/course/EditCourseSheet.tsx";
import DeleteCourseDialog from "./_components/course/DeleteCourseDialog.tsx";
import {Accordion, AccordionContent, AccordionItem, AccordionTrigger} from "../../../../components/ui/accordion.tsx";
import CreateCourseSectionSheet from "./_components/section/CreateCourseSectionSheet.tsx";
import type {Course} from "../../../../types/course.types.ts";
import type {CourseSection} from "../../../../types/course-section.types.ts";
import DeleteSectionDialog from "./_components/section/DeleteSectionDialog.tsx";
import EditSectionSheet from "./_components/section/EditSectionSheet.tsx";
import {
  Breadcrumb,
  BreadcrumbItem,
  BreadcrumbLink,
  BreadcrumbList,
  BreadcrumbPage,
  BreadcrumbSeparator
} from "../../../../components/ui/breadcrumb.tsx";
import type {Prerequisite} from "../../../../types/prerequisite.types.ts";
import CreatePrerequisiteSheet from "./_components/prerequisite/CreatePrerequisiteSheet.tsx";
import EditPrerequisiteSheet from "./_components/prerequisite/EditPrerequisiteSheet.tsx";
import DeletePrerequisiteDialog from "./_components/prerequisite/DeletePrerequisiteDialog.tsx";

export default function ManageCoursePage() {
  const queryClient = useQueryClient();
  const {courseId} = useParams();

  const {data: course, isPending: isLoadingCourse, error: loadCourseError} = useQuery({
    queryKey: ['course'],
    queryFn: () => getCourseById({courseId: parseInt((courseId as string))}),
    enabled: courseId !== undefined
  });

  useEffect(() => {
    queryClient.invalidateQueries({queryKey: ['course']});
  }, [courseId, queryClient]);

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
              <BreadcrumbLink href="/admin/courses">Manage Courses</BreadcrumbLink>
            </BreadcrumbItem>
            <BreadcrumbSeparator/>
            <BreadcrumbItem>
              <BreadcrumbPage>{course ? (course.department + "-" + course.code) : "Course"}</BreadcrumbPage>
            </BreadcrumbItem>
          </BreadcrumbList>
        </Breadcrumb>

        <div>
          <h1 className="title">Manage Course</h1>
          <p>Manage your course.</p>
        </div>

        {isLoadingCourse && <Loader/>}
        {loadCourseError && (
          <p className="text-destructive text-balance text-center">
            {(loadCourseError as Error).message}
          </p>
        )}
        {course && !loadCourseError && (
          <>

            {/* Course */}
            <section className="flex flex-col gap-8 w-full">
              <div className="ml-auto flex items-center gap-4">
                <EditCourseSheet course={course}/>
                <DeleteCourseDialog course={course}/>
              </div>

              <Card>
                <CardHeader>
                  <CardTitle>
                    <h2 className="text-xl font-semibold tracking-tight">{course.title}</h2>
                  </CardTitle>
                  <CardDescription>{course.description}</CardDescription>
                </CardHeader>
                <CardContent>
                  <p><b>Department: </b>{course.department}</p>
                  <p><b>Code: </b>{course.code}</p>
                  <p><b>Credits: </b>{course.credits}</p>
                </CardContent>
              </Card>
            </section>

            <section className="flex flex-col gap-8 w-full">
              <div>
                <h3 className="text-xl font-semibold tracking-tight">Prerequisites</h3>
                <p>View and manage prerequisites for {course.title}.</p>
              </div>

              <div className="ml-auto flex items-center gap-4">
                <CreatePrerequisiteSheet course={course} />
              </div>

              <Card>
                <CardContent>
                  <Accordion type="single" className="w-full">
                    {course.prerequisites
                      .map(prerequisite => (
                        <PrerequisiteAccordionItem course={course} prerequisite={prerequisite} key={prerequisite.id}/>
                      ))}
                  </Accordion>
                </CardContent>
              </Card>
            </section>

            {/* Sections */}
            <section className="flex flex-col gap-8 w-full">
              <div>
                <h3 className="text-xl font-semibold tracking-tight">Sections</h3>
                <p>View and manage course sections for {course.title}.</p>
              </div>

              <div className="ml-auto flex items-center gap-4">
                <CreateCourseSectionSheet course={course}/>
              </div>

              <Card>
                <CardContent>
                  <Accordion type="single" className="w-full">
                    {course.courseSections
                      .map(section => (
                        <CourseSectionAccordionItem course={course} section={section} key={section.id}/>
                      ))}
                  </Accordion>
                </CardContent>
              </Card>
            </section>
          </>
        )}
      </div>
    </div>
  )
}

function CourseSectionAccordionItem({course, section}: { course: Course, section: CourseSection }) {
  return (
    <AccordionItem value={section.id.toString()}>
      <AccordionTrigger>
        <h4 className="font-semibold text-lg">{course.department}-{course.code}-{section.id}</h4>
      </AccordionTrigger>
      <AccordionContent className="w-full flex flex-col gap-4">
        <div className="grid gap-8 md:grid-cols-2">
          <div>
            <h5 className="font-semibold tracking-tight">Section Info</h5>
            <p><b>Instructor: </b>{section.instructor.firstName} {section.instructor.lastName}</p>
            <p><b>Room: </b>{section.room}</p>
            <p><b>Schedule: </b>{section.schedule}</p>
            <p><b>Enrolled: </b>{section.enrolledCount}/{section.capacity}</p>
          </div>
          <div>
            <h5 className="font-semibold tracking-tight">Term Info</h5>
            <p><b>Registration Start Date: </b>{section.term.registrationStart}</p>
            <p><b>Registration End Date: </b>{section.term.registrationEnd}</p>
            <p><b>Start Date: </b>{section.term.startDate}</p>
            <p><b>End Date: </b>{section.term.endDate}</p>
          </div>
        </div>
        <div className="flex gap-4 items-center">
          <EditSectionSheet section={section}/>
          <DeleteSectionDialog section={section}/>
        </div>
      </AccordionContent>
    </AccordionItem>
  )
}

function PrerequisiteAccordionItem({course, prerequisite}: { course: Course, prerequisite: Prerequisite }) {
  return (
    <AccordionItem value={prerequisite.id.toString()}>
      <AccordionTrigger>
        <h4
          className="font-semibold text-lg">{prerequisite.requiredCourseDepartment}-{prerequisite.requiredCourseCode}</h4>
      </AccordionTrigger>
      <AccordionContent className="w-full flex flex-col gap-4">
        <div>
          <h5 className="font-semibold tracking-tight">Prerequisite Info</h5>
          <p><b>Minimum Grade: </b> {prerequisite.minimumGrade}</p>
        </div>
        <Link className="link-1" to={`/admin/courses/${prerequisite.requiredCourseId}`}>
          View {prerequisite.requiredCourseDepartment}-{prerequisite.requiredCourseCode}
        </Link>

        <div className="flex gap-4 items-center">
          <EditPrerequisiteSheet course={course} prerequisite={prerequisite} />
          <DeletePrerequisiteDialog prerequisite={prerequisite} course={course} />
        </div>
      </AccordionContent>
    </AccordionItem>
  )
}