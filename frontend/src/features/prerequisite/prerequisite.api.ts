import type {ManagePrerequisiteRequest, Prerequisite} from "../../types/prerequisite.types.ts";
import type {AxiosResponse} from "axios";
import {axiosInstance} from "../../config/axios.config.ts";
import {handleApiError} from "../../lib/util/api.util.ts";

/**
 * Query function to retrieve a courses prerequisites.
 * @param courseId the target course's ID.
 * @return A list of prerequisites.
 * @throws Error
 */
export async function getPrerequisites({courseId}: {courseId: number}): Promise<Prerequisite[]> {
  try {
    const response: AxiosResponse<Prerequisite[]> = await axiosInstance.get(`/courses/${courseId}/prerequisites`);
    return response.data;
  } catch (e) {
    handleApiError(e);
  }
}

/**
 * Mutation function to create a new prerequisite for a course. Must be admin.
 * @param courseId the target course to add the prerequisite to.
 * @param managePrerequisiteRequest the data payload to manage the prerequisite.
 * @return the newly created prerequisite
 * @throws Error
 */
export async function createPrerequisite({courseId, managePrerequisiteRequest}: {
  courseId: number,
  managePrerequisiteRequest: ManagePrerequisiteRequest
}): Promise<Prerequisite> {
  try {
    const response: AxiosResponse<Prerequisite> = await axiosInstance.post(`/courses/${courseId}/prerequisites`, managePrerequisiteRequest);
    return response.data;
  } catch (e) {
    handleApiError(e);
  }
}

/**
 * Mutation function to update a prerequisite for a course. Must be admin.
 * @param courseId the target course the prerequisite belongs to.
 * @param managePrerequisiteRequest the data payload to manage the prerequisite.
 * @param prerequisiteId the prerequisite's ID.
 * @return the newly updated prerequisite
 * @throws Error
 */
export async function updatePrerequisite({courseId, managePrerequisiteRequest, prerequisiteId}: {
  courseId: number,
  managePrerequisiteRequest: ManagePrerequisiteRequest,
  prerequisiteId: number
}): Promise<Prerequisite> {
  try {
    const response: AxiosResponse<Prerequisite> = await axiosInstance.put(`/courses/${courseId}/prerequisites/${prerequisiteId}`, managePrerequisiteRequest);
    return response.data;
  } catch (e) {
    handleApiError(e);
  }
}

/**
 * Mutation function to delete a prerequisite from a course. Must be admin.
 * @param courseId the target course the prerequisite belongs to
 * @param prerequisiteId the prerequisite's ID.
 * @return Promise<void>
 * @throws Error
 */
export async function deletePrerequisite({courseId, prerequisiteId}: {
  courseId: number,
  prerequisiteId: number
}): Promise<void> {
  try {
    await axiosInstance.delete(`/courses/${courseId}/prerequisites/${prerequisiteId}`);
  } catch (e) {
    handleApiError(e);
  }
}