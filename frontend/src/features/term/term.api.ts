import type {AxiosResponse} from "axios";
import type {ManageTermRequest, Term} from "../../types/term.types.ts";
import {axiosInstance} from "../../config/axios.config.ts";
import {handleApiError} from "../../lib/util/api.util.ts";

/**
 * Query to Retrieve all terms
 * @return A list of all terms
 * @throws Error
 */
export async function getAllTerms(): Promise<Term[]> {
  try {
    const response: AxiosResponse<Term[]> = await axiosInstance.get("/terms");
    return response.data;
  } catch (e) {
    handleApiError(e);
  }
}

/**
 * Query to Retrieve a term by Id.
 * @return the requested term
 * @throws Error
 */
export async function getTermById({termId}: { termId: number }): Promise<Term> {
  try {
    const response: AxiosResponse<Term> = await axiosInstance.get(`/terms/${termId}`);
    return response.data;
  } catch (e) {
    handleApiError(e);
  }
}

/**
 * Mutation function to create a new term. Must be admin.
 * @param manageTermRequest the object containing the new term data.
 * @return the newly created term.
 * @throws Error
 */
export async function createTerm(manageTermRequest: ManageTermRequest): Promise<Term> {
  try {
    const response: AxiosResponse<Term> = await axiosInstance.post(`/terms`, manageTermRequest);
    return response.data;
  } catch (e) {
    handleApiError(e);
  }
}

/**
 * Mutation function to update a term. Must be admin.
 * @param termId the target term to update
 * @param manageTermRequest the object containing the new term data.
 * @return the newly updated term.
 * @throws Error
 */
export async function updateTerm({termId, manageTermRequest}: {
  termId: number,
  manageTermRequest: ManageTermRequest
}): Promise<Term> {
  try {
    const response: AxiosResponse<Term> = await axiosInstance.put(`/terms/${termId}`, manageTermRequest);
    return response.data;
  } catch (e) {
    handleApiError(e);
  }
}

/**
 * Mutation function to delete a term
 * @param termId  the targeted term to delete
 * @throws Error
 */
export async function deleteTerm({termId}: { termId: number }): Promise<void> {
  try {
    await axiosInstance.delete(`/terms/${termId}`);
  } catch (e) {
    handleApiError(e);
  }
}

