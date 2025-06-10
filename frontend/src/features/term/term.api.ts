import type {AxiosError, AxiosResponse} from "axios";
import type {ManageTermRequest, Term} from "../../types/term.types.ts";
import type {ErrorResponse} from "../../types/common.types.ts";
import {axiosInstance} from "../../config/axios.config.ts";

/**
 * Query to Retrieve all terms
 * @return A list of all terms
 */
export async function getAllTerms(): Promise<Term[]> {
  try {
    const response: AxiosResponse<Term[]> = await axiosInstance.get("/terms");
    return response.data;
  } catch (e) {
    throw new Error((e as AxiosError<ErrorResponse>).response?.data.message || "Something went wrong. Try again later.");
  }
}

/**
 * Query to Retrieve a term by Id.
 * @return the requested term
 */
export async function getTermById({termId}: {termId: string}): Promise<Term> {
  try {
    const response: AxiosResponse<Term> = await axiosInstance.get(`/terms/${termId}`);
    return response.data;
  } catch (e) {
    throw new Error((e as AxiosError<ErrorResponse>).response?.data.message || "Something went wrong. Try again later.");
  }
}

/**
 * Mutation function to create a new term. Must be admin.
 * @param manageTermRequest the object containing the new term data.
 * @return the newly created term.
 */
export async function createTerm(manageTermRequest: ManageTermRequest): Promise<Term> {
  try {
    const response: AxiosResponse<Term> = await axiosInstance.post(`/terms`, manageTermRequest);
    return response.data;
  } catch (e) {
    throw new Error((e as AxiosError<ErrorResponse>).response?.data.message || "Something went wrong. Try again later.");
  }
}

/**
 * Mutation function to update a term. Must be admin.
 * @param manageTermRequest the object containing the new term data.
 * @return the newly updated term.
 */
export async function updateTerm(manageTermRequest: ManageTermRequest): Promise<Term> {
  try {
    const response: AxiosResponse<Term> = await axiosInstance.put(`/terms`, manageTermRequest);
    return response.data;
  } catch (e) {
    throw new Error((e as AxiosError<ErrorResponse>).response?.data.message || "Something went wrong. Try again later.");
  }
}

/**
 * Mutation function to delete a term
 * @param termId  the targeted term to delete
 */
export async function deleteTerm({termId}: {termId: string}): Promise<void> {
  try {
    await axiosInstance.delete(`/terms/${termId}`);
  } catch (e) {
    throw new Error((e as AxiosError<ErrorResponse>).response?.data.message || "Something went wrong. Try again later.");
  }
}

