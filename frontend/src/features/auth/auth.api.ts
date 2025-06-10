import {axiosInstance} from "../../config/axios.config.ts";
import type {SignupRequest, User} from "../../types/user.types.ts";
import type {AxiosError, AxiosResponse} from "axios";
import type {ErrorResponse, PageResponse} from "../../types/common.types.ts";

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
    throw new Error((e as AxiosError<ErrorResponse>).response?.data.message || "Something went wrong. Try again later.");
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
    throw new Error((e as AxiosError<ErrorResponse>).response?.data.message || "Something went wrong. Try again later.");
  }
}

/**
 * Retrieve a page of all users. User must be an Admin.
 * @param page the number of the page
 * @param size the size of the page
 * @param sort the direction to sort
 * @param search search fields (email/username, firstName, lastName)
 * @return a PageResponse containing users.
 * @throws Error
 */
export async function getAllUsers({page, size, sort, search}: {
  page: number,
  size: number,
  sort: "asc" | "desc",
  search: string | null
}): Promise<PageResponse<User>> {
  try {
    const response: AxiosResponse<PageResponse<User>> = await axiosInstance.get(`/auth/users`, {
      params: {
        page,
        size,
        sort,
        search
      }
    });
    return response.data;
  } catch (e) {
    throw new Error((e as AxiosError<ErrorResponse>).response?.data.message || "Something went wrong. Try again later.");
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
    throw new Error((e as AxiosError<ErrorResponse>).response?.data.message || "Something went wrong. Try again later.");
  }
}