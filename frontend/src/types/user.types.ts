// Auth and User types

// Valid user roles
export const UserRole = {
  ADMIN: "ROLE_ADMIN",
  INSTRUCTOR: "ROLE_INSTRUCTOR",
  STUDENT: "ROLE_STUDENT"
}

// user fields
export type User = {
  id: number;
  username: string;
  firstName: string;
  lastName: string;
  grantedAuthorities: string[];
  createdAt: string;
}

// User Fields that can be changed by an admin
export interface UpdateUserRequest {
  role: "ROLE_ADMIN" | "ROLE_INSTRUCTOR" | "ROLE_STUDENT"
}

// Required fields for signup
export interface SignupRequest {
  username: string;
  firstName: string;
  lastName: string;
  password: string;
  confirmPassword: string;
}