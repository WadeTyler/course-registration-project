import type {Pageable, PageResponse} from "../../types/common.types.ts";
import type {Course, ManageCourseRequest} from "../../types/course.types.ts";
import type {AxiosResponse} from "axios";
import {axiosInstance} from "../../config/axios.config.ts";
import {handleApiError} from "../../lib/util/api.util.ts";

/**
 * Query function to retrieve a PageResponse of courses.
 * @param pageable  the pageable fields
 * @return a page response of courses.
 * @throws Error
 */
export async function getAllCourses(pageable: Pageable): Promise<PageResponse<Course>> {
  try {
    const response: AxiosResponse<PageResponse<Course>> = await axiosInstance.get("/courses", {
      params: pageable
    });
    return response.data;
  } catch (e) {
    handleApiError(e);
  }
}

/**
 * Query function to retrieve a specific course by ID.
 * @param courseId the target course's ID.
 * @return the target course
 * @throws Error
 */
export async function getCourseById({courseId}: {courseId: number}): Promise<Course> {
  try {
    const response: AxiosResponse<Course> = await axiosInstance.get(`/courses/${courseId}`);
    return response.data;
  } catch (e) {
    handleApiError(e);
  }

}

/**
 * Mutation function to create a new course.
 * @param manageCourseRequest the manage course payload.
 * @return the newly create course
 * @throws Error
 */
export async function createCourse(manageCourseRequest: ManageCourseRequest): Promise<Course> {
  try {
    const response: AxiosResponse<Course> = await axiosInstance.post(`/courses`, manageCourseRequest);
    return response.data;
  } catch (e) {
    handleApiError(e);
  }
}

/**
 * Mutation function to update a course.
 * @param courseId the target course's ID.
 * @param manageCourseRequest the manage course payload.
 * @return the newly updated course
 * @throws Error
 */
export async function updateCourse({courseId, manageCourseRequest}: {
  courseId: number;
  manageCourseRequest: ManageCourseRequest
}): Promise<Course> {
  try {
    const response: AxiosResponse<Course> = await axiosInstance.put(`/courses/${courseId}`, manageCourseRequest);
    return response.data;
  } catch (e) {
    handleApiError(e);
  }
}

/**
 * Mutation function to delete a course.
 * @param courseId the target course's ID.
 * @return void
 * @throws Error
 */
export async function deleteCourse({courseId}: {courseId: number}): Promise<void> {
  try {
    await axiosInstance.delete(`/courses/${courseId}`);
  } catch (e) {
    handleApiError(e);
  }
}