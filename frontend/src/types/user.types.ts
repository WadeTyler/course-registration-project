
/*
 * user authorities
 */
export interface Authority {
  id: number;
  name: "ADMIN" | "INSTRUCTOR" | "STUDENT";
}

/*
 * user fields
 */
export type User = {
  id: number;
  username: string;
  firstName: string;
  lastName: string;
  userAuthorities: Authority[];
  createdAt: string;
}

/*
 * User Fields that can be changed by an admin
 */
export interface UpdateUserRequest {
  role:  "ADMIN" | "INSTRUCTOR" | "STUDENT";
}

/*
 * Required fields for signup
 */
export interface SignupRequest {
  username: string;
  firstName: string;
  lastName: string;
  password: string;
  confirmPassword: string;
}