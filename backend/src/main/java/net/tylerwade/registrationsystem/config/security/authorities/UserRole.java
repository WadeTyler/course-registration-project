package net.tylerwade.registrationsystem.config.security.authorities;

import com.google.common.collect.Sets;
import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

import static net.tylerwade.registrationsystem.config.security.authorities.UserPermission.*;

@Getter
public enum UserRole {
    STUDENT(Sets.newHashSet(STUDENT_READ, STUDENT_WRITE, TERM_READ, COURSE_READ, PREREQUISITE_READ, COURSE_SECTION_READ, ENROLLMENT_READ)),
    INSTRUCTOR(Sets.newHashSet(STUDENT_READ, COURSE_READ, TERM_READ, PREREQUISITE_READ, COURSE_SECTION_READ, ENROLLMENT_READ, ENROLLMENT_WRITE)),
    ADMIN(Sets.newHashSet(STUDENT_READ, STUDENT_WRITE, TERM_READ, TERM_WRITE, COURSE_READ, COURSE_WRITE, PREREQUISITE_READ, PREREQUISITE_WRITE, COURSE_SECTION_READ, COURSE_SECTION_WRITE, ENROLLMENT_READ, ENROLLMENT_WRITE));

    private final Set<UserPermission> permissions;

    UserRole(Set<UserPermission> permissions) {
        this.permissions = permissions;
    }

    public Set<SimpleGrantedAuthority> getGrantedAuthorities() {
        Set<SimpleGrantedAuthority> permissions = getPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toSet());

        permissions.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return permissions;
    }

    public Set<String> getGrantedAuthoritiesString() {
        Set<String> permissions = getPermissions().stream()
                .map(permission -> permission.getPermission())
                .collect(Collectors.toSet());

        permissions.add("ROLE_" + this.name());
        return permissions;
    }
}
