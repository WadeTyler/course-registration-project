package net.tylerwade.registrationsystem.auth.authority;

import lombok.RequiredArgsConstructor;
import net.tylerwade.registrationsystem.exception.HttpRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthorityService {

    private final AuthorityRepository authorityRepository;

    public Authority getStudentAuthority() throws HttpRequestException {
        return authorityRepository.findByNameEqualsIgnoreCase("STUDENT").orElseThrow(() -> new HttpRequestException(HttpStatus.INTERNAL_SERVER_ERROR, "Student authority not found."));
    }

    public Authority getInstructorAuthority() throws HttpRequestException {
        return authorityRepository.findByNameEqualsIgnoreCase("INSTRUCTOR").orElseThrow(() -> new HttpRequestException(HttpStatus.INTERNAL_SERVER_ERROR, "Instructor authority not found."));
    }

    public Authority getAdminAuthority() throws HttpRequestException {
        return authorityRepository.findByNameEqualsIgnoreCase("ADMIN").orElseThrow(() -> new HttpRequestException(HttpStatus.INTERNAL_SERVER_ERROR, "Admin authority not found."));
    }

    public Authority findByName(String name) throws HttpRequestException {
        return authorityRepository.findByNameEqualsIgnoreCase(name).orElseThrow(() -> new HttpRequestException(HttpStatus.NOT_FOUND, "Authority not found."));
    }
}
