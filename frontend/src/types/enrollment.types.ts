import type {User} from "./user.types.ts";
import type {CourseSection} from "./course-section.types.ts";

/*
 * Enrollment fields
 */
export interface Enrollment {
  student: User;
  courseSection: CourseSection;
  grade: number;
  status: string;
  createdAt: string;
}

/*
 * Enrollment fields for instructor assigned courses.
 */
export interface InstructorEnrollment {
  student: User;
  courseSectionId: number;
  grade: number;
  status: string;
  createdAt: string;
}

/*
 * Required fields for creating an enrollment
 */
export type CreateEnrollmentRequest = {
  courseSectionId: number;
}

/*
 * Required fields for updating an enrollment as an instructor/admin
 */
export type ManageEnrollmentRequest = {
  grade: number;
  status: string;
}

export type ValidEnrollmentStatus = "NOT_STARTED" | "STARTED" | "COMPLETED" | "DROPPED" | "FAILED";