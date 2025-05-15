package net.tylerwade.registrationsystem.config.security.authorities;

import lombok.Getter;

@Getter
public enum UserPermission {
    STUDENT_READ("student:read"),
    STUDENT_WRITE("student:write"),
    COURSE_READ("course_read"),
    COURSE_WRITE("course:write");

    private final String permission;


    UserPermission(String permission) {
        this.permission = permission;
    }

}
