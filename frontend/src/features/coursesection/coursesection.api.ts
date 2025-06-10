import type {
  CourseSection,
  InstructorCourseSection,
  ManageCourseSectionRequest
} from "../../types/course-section.types.ts";
import type {AxiosResponse} from "axios";
import {axiosInstance} from "../../config/axios.config.ts";
import {handleApiError} from "../../lib/util/api.util.ts";

/**
 * Query function to retrieve all course sections.
 * @param courseId  (Optional) used to retrieve all course sections within a target course.
 * @return A list of course sections.
 * @throws Error
 */
export async function getAllCourseSection({courseId = null}: {
  courseId?: null | number
}): Promise<CourseSection[]> {
  try {
    const response: AxiosResponse<CourseSection[]> = await axiosInstance.get(`/sections`, {
      params: {
        courseId
      }
    });
    return response.data;
  } catch (e) {
    handleApiError(e);
  }
}

/**
 * Query function to retrieve a course section by ID.
 * @param sectionId the target course section's ID.
 * @return The target course section
 * @throws Error
 */
export async function getCourseSectionById({sectionId}: {sectionId: number}): Promise<CourseSection> {
  try {
    const response: AxiosResponse<CourseSection> = await axiosInstance.get(`/sections/${sectionId}`);
    return response.data;
  } catch (e) {
    handleApiError(e);
  }
}

/**
 * Mutation function to create a course section. Must be admin.
 * @param courseId the target course's ID.
 * @param manageCourseSectionRequest  the data payload request to create the course section
 * @return the newly created course section.
 * @throws Error
 */
export async function createCourseSection({courseId, manageCourseSectionRequest}: {
  courseId: number;
  manageCourseSectionRequest: ManageCourseSectionRequest;
}): Promise<CourseSection> {
  try {
    const response: AxiosResponse<CourseSection> = await axiosInstance.post(`/sections`, manageCourseSectionRequest, {
      params: { courseId }
    });
    return response.data;
  } catch (e) {
    handleApiError(e);
  }
}


/**
 * Mutation function to update a course section. Must be admin.
 * @param sectionId the target section to update.
 * @param manageCourseSectionRequest  the data payload request to update the course section.
 * @return the newly updated course section.
 * @throws Error
 */
export async function updateCourseSection({sectionId, manageCourseSectionRequest}: {
  sectionId: number;
  manageCourseSectionRequest: ManageCourseSectionRequest;
}): Promise<CourseSection> {
  try {
    const response: AxiosResponse<CourseSection> = await axiosInstance.put(`/sections/${sectionId}`, manageCourseSectionRequest);
    return response.data;
  } catch (e) {
    handleApiError(e);
  }
}

/**
 * Mutation function to delete a course section.
 * @param sectionId the target course section's ID.
 * @return Promise<void>
 * @throws Error
 */
export async function deleteCourseSection({sectionId}: {sectionId: number}): Promise<void> {
  try {
    await axiosInstance.delete(`/sections/${sectionId}`);
  } catch (e) {
    handleApiError(e);
  }
}

/**
 * Query function to retrieve all assigned course sections. Must be instructor or admin.
 * @return A list of assigned instructor course section
 * @throws Error
 */
export async function getAllAssignedCourseSections(): Promise<InstructorCourseSection[]> {
  try {
    const response: AxiosResponse<InstructorCourseSection[]> = await axiosInstance.get("/sections/assigned");
    return response.data;
  } catch (e) {
    handleApiError(e);
  }
}

/**
 * Query function to retrieve an assigned course section by id. Must be instructor or admin.
 * @param sectionId the target course section's ID.
 * @return The target course section.
 * @throws Error
 */
export async function getAssignedCourseSectionById({sectionId}: {sectionId: number}): Promise<InstructorCourseSection> {
  try {
    const response: AxiosResponse<InstructorCourseSection> = await axiosInstance.get(`/sections/assigned/${sectionId}`);
    return response.data;
  } catch (e) {
    handleApiError(e);
  }
}