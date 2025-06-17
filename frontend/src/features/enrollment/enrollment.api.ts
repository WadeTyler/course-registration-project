import type {CreateEnrollmentRequest, Enrollment, ManageEnrollmentRequest} from "../../types/enrollment.types.ts";
import type {AxiosResponse} from "axios";
import {axiosInstance} from "../../config/axios.config.ts";
import {handleApiError} from "../../lib/util/api.util.ts";

/**
 * Query Function to retrieve all enrollments by student Id.
 * @param studentId the target student's. Must be the authUser's Id if the authUser's not an admin/instructor.
 * @return a list of the student's enrollments.
 * @throws Error
 */
export async function getEnrollmentsByStudentId({studentId}: { studentId: number }): Promise<Enrollment[]> {
  try {
    const response: AxiosResponse<Enrollment[]> = await axiosInstance.get(`/enrollments`, {
      params: {
        studentId
      }
    });
    return response.data;
  } catch (e) {
    handleApiError(e);
  }
}

/**
 * Mutation function to create a new enrollment. Must be student or admin.
 * @param studentId the target student's ID. Must be the authUser's Id or the AuthUser must be an administrator.
 * @param createEnrollmentRequest The payload containing the data to create a new enrollment.
 * @return The newly created enrollment
 * @throws Error
 */
export async function createEnrollment({studentId, createEnrollmentRequest}: {
  studentId: number,
  createEnrollmentRequest: CreateEnrollmentRequest
}): Promise<Enrollment> {
  try {
    const response: AxiosResponse<Enrollment> = await axiosInstance.post(`/enrollments`, createEnrollmentRequest, {
      params: {studentId}
    });
    return response.data;
  } catch (e) {
    handleApiError(e);
  }
}


/**
 * Mutation function to update an enrollment. Must be instructor or admin.
 * @param studentId the target student's ID.
 * @param courseSectionId the target course section ID.
 * @param manageEnrollmentRequest the data payload containing the info.
 * @return the newly updated enrollment
 * @throws Error
 */
export async function updateEnrollment({studentId, courseSectionId, manageEnrollmentRequest}: {
  studentId: number;
  courseSectionId: number;
  manageEnrollmentRequest: ManageEnrollmentRequest;
}): Promise<Enrollment> {
  try {
    const response: AxiosResponse<Enrollment> = await axiosInstance.put(`/enrollments`, manageEnrollmentRequest, {
      params: {studentId, courseSectionId}
    });
    return response.data;
  } catch (e) {
    handleApiError(e);
  }
}

/**
 * Mutation function to delete an enrollment.
 * @param studentId the target student's Id.
 * @param courseSectionId the target course section's Id.
 * @return Promise<void>
 * @throws Error
 */
export async function deleteEnrollment({studentId, courseSectionId}: {
  studentId: number;
  courseSectionId: number;
}): Promise<void> {
  try {
    await axiosInstance.delete(`/enrollments`, {
      params: {studentId, courseSectionId}
    });
  } catch (e) {
    handleApiError(e);
  }
}
