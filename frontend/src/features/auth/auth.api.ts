import {axiosInstance} from "../../config/axios.config.ts";
import type {SignupRequest, User} from "../../types/user.types.ts";
import type {AxiosError, AxiosResponse} from "axios";
import type {ErrorResponse} from "../../types/common.types.ts";
import {handleApiError} from "../../lib/util/api.util.ts";

/**
 * Retrieves the authenticated user.
 * @return the authUser if authenticated, null if not
 */
export async function getAuthUser(): Promise<User | null> {
  try {
    const response: AxiosResponse<User> = await axiosInstance.get("/auth");
    console.log(response.data);
    return response.data;
  } catch (e) {
    console.log(e);
    console.error((e as AxiosError<ErrorResponse>).response?.data.message || "Something went wrong fetching auth user.");
    return null;
  }
}

/**
 * Login mutation to handle logging the auth user in.
 * @param username  the user's email
 * @param password  the user's password
 * @return the auth user
 * @throws error if login failed
 */
export async function login({username, password}: { username: string, password: string }): Promise<User> {
  try {
    const response: AxiosResponse<User> = await axiosInstance.post("/auth/login", null, {
      auth: {
        username,
        password
      }
    });

    return response.data;
  } catch (e) {
    console.log(e);
    throw new Error((e as AxiosError<ErrorResponse>).response?.data ? "Incorrect Email or Password" : "Something went wrong. Try again later");
  }
}

/**
 * Logout mutation.
 * @return logout confirmation string
 * @throws Error
 */
export async function logout(): Promise<void> {
  try {
    await axiosInstance.post("/auth/logout");
  } catch (e) {
    handleApiError(e);
  }
}

/**
 * Signup mutation. Handles signup.
 * @param signupRequest the signup request data.
 * @return the authenticated user
 * @throws Error
 */
export async function signup(signupRequest: SignupRequest): Promise<User> {
  try {
    const response = await axiosInstance.post("/auth/signup", signupRequest);
    return response.data;
  } catch (e) {
    handleApiError(e);
  }
}

/**
 * Retrieve a list of all users. User must be an Admin.
 * @throws Error
 */
export async function getAllUsers(): Promise<User[]> {
  try {
    const response: AxiosResponse<User[]> = await axiosInstance.get(`/auth/users`);
    return response.data;
  } catch (e) {
    handleApiError(e);
  }
}

/**
 * Updates a user's account. AuthUser must be an Admin.
 * @param userId  target user's ID
 * @param role  target user's new role. Must be 'ADMIN' or 'INSTRUCTOR' or 'STUDENT'
 * @return the updated user.
 * @throws Error
 */
export async function updateUserAsAdmin({userId, role}: {userId: number, role: "ADMIN" | "INSTRUCTOR" | "STUDENT"}): Promise<User>  {
  try {
    const response: AxiosResponse<User> = await axiosInstance.put(`/auth/users/${userId}`, {role});
    return response.data;
  } catch (e) {
    handleApiError(e);
  }
}