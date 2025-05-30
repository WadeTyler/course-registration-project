import type {Prerequisite} from "./prerequisite.types.ts";
import type {CourseSection} from "./course-section.types.ts";

// Course fields
export interface Course {
  id: number;
  department: string;
  code: number;
  title: string;
  description: string;
  credits: number;
  prerequisites: Prerequisite[];
  courseSections: CourseSection[];
  createdAt: string;
}

// Course Attribute fields used
export interface CourseAttributes {
  id: number;
  department: string;
  code: number;
  title: string;
  description: string;
  credits: number;
}

// Fields required to create or update a course
export type ManageCourseRequest = {
  department: string;
  code: number;
  title: string;
  description: string;
  credits: number;
}