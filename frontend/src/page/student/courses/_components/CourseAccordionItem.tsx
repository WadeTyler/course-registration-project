import {useQuery} from "@tanstack/react-query";
import type {Course} from "@/types/course.types.ts";
import {getAuthUser} from "@/features/auth/auth.api.ts";
import {getEnrollmentsByStudentId} from "@/features/enrollment/enrollment.api.ts";
import type {User} from "@/types/user.types.ts";
import {Accordion, AccordionContent, AccordionItem, AccordionTrigger} from "@/components/ui/accordion.tsx";
import CourseSectionAccordionItem from "./CourseSectionAccordionItem.tsx";

export default function CourseAccordionItem({course}: { course: Course }) {

  const {data: authUser} = useQuery({
    queryKey: ['authUser'],
    queryFn: getAuthUser
  });

  const {data: enrollments} = useQuery({
    queryKey: ['enrollments'],
    queryFn: () => getEnrollmentsByStudentId({studentId: (authUser as User).id}),
    enabled: authUser !== undefined && authUser !== null
  });

  function hasTakenCourse(courseId: number): boolean {
    return enrollments?.find(enrollment => enrollment.courseSection.course.id === courseId) !== undefined;
  }

  return (
    <AccordionItem value={course.id.toString()}>
      <AccordionTrigger className="text-xl font-semibold tracking-tight">
        <h2 className="flex items-center justify-between gap-4 w-full">
          <span>{course.department}-{course.code} | {course.title}</span>
          {hasTakenCourse(course.id) && <span className="text-green-600 font-light text-xs">Taken/Enrolled</span>}
        </h2>
      </AccordionTrigger>
      <AccordionContent className="flex flex-col gap-4">
        <hr/>
        {/* Course Info */}
        <section>
          <p>{course.description}</p>
          <p><b>Credits: </b>{course.credits}</p>
          {course.prerequisites.length !== 0 && (
            <p><b>Prerequisites: </b>{course.prerequisites.map(prerequisite => (
              <span key={prerequisite.id}
                    className={`${hasTakenCourse(prerequisite.requiredCourseId) ? "text-green-600" : "text-red-400"}`}>
            {prerequisite.requiredCourseDepartment}-{prerequisite.requiredCourseCode}, </span>
            ))}</p>
          )}
        </section>


        <section>
          <h3 className="font-semibold tracking-tight text-xl">Course Sections</h3>
          <Accordion type={"multiple"} className="rounded-md overflow-hidden shadow-md">
            {course.courseSections.map(section => (
              <CourseSectionAccordionItem course={course} section={section} key={section.id}/>
            ))}
          </Accordion>
        </section>

      </AccordionContent>
    </AccordionItem>
  )
}