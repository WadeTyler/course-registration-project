import type {CourseAttributes} from "./course.types.ts";
import type {Term} from "./term.types.ts";
import type {User} from "./user.types.ts";
import type {InstructorEnrollment} from "./enrollment.types.ts";

/*
 * Course section fields
 */
export interface CourseSection {
  id: number;
  course: CourseAttributes;
  term: Term;
  instructor: User;
  room: string;
  capacity: number;
  schedule: string;
  enrolledCount: number;
  createdAt: string;
}

/*
 * Course section fields for an instructor assigned course section
 */
export interface InstructorCourseSection {
  id: number;
  course: CourseAttributes;
  term: Term;
  instructor: User;
  room: string;
  capacity: number;
  schedule: string;
  enrolledCount: number;
  enrollments: InstructorEnrollment[];
  createdAt: string;
}

/*
 * Required fields to update or create a course section.
 */
export type ManageCourseSectionRequest = {
  termId: number;
  instructorId: number;
  room: string;
  capacity: number;
  schedule: string;
}