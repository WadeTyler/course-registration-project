package net.tylerwade.registrationsystem.config.security.authorities;

import lombok.Getter;

@Getter
public enum UserPermission {
    STUDENT_READ("student:read"),
    STUDENT_WRITE("student:write"),
    TERM_READ("term:read"),
    TERM_WRITE("term:write"),
    COURSE_READ("course:read"),
    COURSE_WRITE("course:write"),
    PREREQUISITE_READ("prerequisite:read"),
    PREREQUISITE_WRITE("prerequisite:write"),
    COURSE_SECTION_READ("course_section:read"),
    COURSE_SECTION_WRITE("course_section:write"),
    ENROLLMENT_READ("enrollment_read"),
    ENROLLMENT_WRITE("enrollment_write");

    private final String permission;


    UserPermission(String permission) {
        this.permission = permission;
    }

}
