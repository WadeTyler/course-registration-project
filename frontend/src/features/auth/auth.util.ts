import type {User} from "../../types/user.types.ts";

/**
 * Checks if the given user has the 'ADMIN' authority.
 * @param {User} user - The user object to check.
 * @returns {boolean} True if the user is an admin, otherwise false.
 */
export function isAdmin(user: User): boolean {
    return user.userAuthorities.some(auth => auth.name === "ADMIN");
}

/**
 * Checks if the given user has the 'INSTRUCTOR' authority.
 * @param {User} user - The user object to check.
 * @returns {boolean} True if the user is an instructor, otherwise false.
 */
export function isInstructor(user: User): boolean {
    return user.userAuthorities.some(auth => auth.name === "INSTRUCTOR");
}

/**
 * Checks if the given user has the 'STUDENT' authority.
 * @param {User} user - The user object to check.
 * @returns {boolean} True if the user is a student, otherwise false.
 */
export function isStudent(user: User): boolean {
    return user.userAuthorities.some(auth => auth.name === "STUDENT");
}