import type {Course} from "@/types/course.types.ts";
import type {CourseSection} from "@/types/course-section.types.ts";
import {useMutation, useQuery, useQueryClient} from "@tanstack/react-query";
import {getAuthUser} from "@/features/auth/auth.api.ts";
import {createEnrollment, deleteEnrollment, getEnrollmentsByStudentId} from "@/features/enrollment/enrollment.api.ts";
import type {User} from "@/types/user.types.ts";
import type {Enrollment} from "@/types/enrollment.types.ts";
import {AccordionContent, AccordionItem, AccordionTrigger} from "@/components/ui/accordion.tsx";
import {Tooltip, TooltipContent, TooltipTrigger} from "@/components/ui/tooltip.tsx";
import {Button} from "@/components/ui/button.tsx";
import {toast} from "sonner";

export default function CourseSectionAccordionItem({course, section}: { course: Course, section: CourseSection }) {
  const queryClient = useQueryClient();

  const {data: authUser} = useQuery({
    queryKey: ['authUser'],
    queryFn: getAuthUser
  });

  const {data: enrollments} = useQuery({
    queryKey: ['enrollments'],
    queryFn: () => getEnrollmentsByStudentId({studentId: (authUser as User).id}),
    enabled: authUser !== undefined && authUser !== null
  });

  const now = new Date();

  const isOpenForRegistration =
    section.enrolledCount < section.capacity &&
    new Date(section.term.registrationStart) <= now &&
    now <= new Date(section.term.registrationEnd);

  const isEnrolled = enrollments?.some((enrollment: Enrollment) => enrollment.courseSection.id === section.id);

  const {mutate: registerMutation, isPending: isRegistering} = useMutation({
    mutationFn: createEnrollment,
    onSuccess: async () => {
      await queryClient.invalidateQueries({queryKey: ['enrollments']});
    },
    onError: err => {
      toast.error(err.message);
    }
  });

  function handleRegister() {
    if (isRegistering || isDropping || !authUser) return;
    registerMutation({studentId: authUser.id, createEnrollmentRequest: {courseSectionId: section.id}});
  }

  const {mutate: dropMutation, isPending: isDropping} = useMutation({
    mutationFn: deleteEnrollment,
    onSuccess: async () => {
      await queryClient.invalidateQueries({queryKey: ['enrollments']});
    },
    onError: err => {
      toast.error(err.message);
    }
  });

  function handleDrop() {
    if (isRegistering || isDropping || !authUser) return;
    dropMutation({studentId: authUser.id, courseSectionId: section.id});
  }


  return (
    <AccordionItem value={section.id.toString()} className="bg-accent/5 p-4">
      <AccordionTrigger>
        <h4 className="flex items-center gap-8 justify-between w-full">
          <span>{course.department}-{course.code}-{section.id}</span>
          {isOpenForRegistration ? (
            <span className="text-green-600">Open</span>
          ) : (
            <span className="text-red-600">Closed</span>
          )}
        </h4>
      </AccordionTrigger>
      <AccordionContent className="flex flex-col gap-4">
        <hr/>
        <div className="grid md:grid-cols-2 lg:grid-cols-3 gap-4 w-fit">
          <p>
            <b>Registration: </b>
            {isOpenForRegistration ? (
              <span className="text-green-600">Open</span>
            ) : (
              <span className="text-red-600">Closed</span>
            )}
          </p>
          <p>
            <b>Registration Ends: </b> {section.term.registrationEnd}
          </p>
          <p>
            <b>Starts: </b> {section.term.startDate}
          </p>
          <p>
            <b>Ends: </b> {section.term.endDate}
          </p>
          <p>
            <b>Seats Remaining:</b> {section.capacity - section.enrolledCount}
          </p>
          <p>
            <b>Instructor: </b>
            <Tooltip>
              <TooltipTrigger className="underline">
                {section.instructor.firstName} {section.instructor.lastName}
              </TooltipTrigger>
              <TooltipContent>{section.instructor.username}</TooltipContent>
            </Tooltip>
          </p>
          <p>
            <b>Schedule: </b> {section.schedule}
          </p>
          <p>
            <b>Room: </b> {section.room}
          </p>
        </div>

        {isOpenForRegistration && !isEnrolled && (
          <Button variant="outline" disabled={isRegistering} onClick={handleRegister}>
            Register
          </Button>
        )}
        {isEnrolled && (
          <Button variant="destructive" disabled={isDropping} onClick={handleDrop}>
            Drop
          </Button>
        )}

      </AccordionContent>
    </AccordionItem>
  )
}