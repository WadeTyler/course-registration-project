
/*
 * Prerequisite fields
 */
export type Prerequisite = {
  id: number;
  courseId: number;
  requiredCourseId: number;
  requiredCourseDepartment: string;
  requiredCourseCode: number;
  minimumGrade: number;
  createdAt: string;
}

/*
 * Required fields to update or create a prerequisite.
 */
export type ManagePrerequisiteRequest = {
  requiredCourseId: number;
  minimumGrade: number;
}