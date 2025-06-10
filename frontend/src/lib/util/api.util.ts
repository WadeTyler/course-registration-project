import type {AxiosError} from "axios";
import type {ErrorResponse} from "../../types/common.types";

// Helper function for error handling
export function handleApiError(e: unknown): never {
  throw new Error((e as AxiosError<ErrorResponse>).response?.data.message ||
    "Something went wrong. Try again later.");
}