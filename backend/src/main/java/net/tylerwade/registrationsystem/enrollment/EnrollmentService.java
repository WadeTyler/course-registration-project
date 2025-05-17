package net.tylerwade.registrationsystem.enrollment;

import net.tylerwade.registrationsystem.enrollment.dto.CreateEnrollmentRequest;
import net.tylerwade.registrationsystem.enrollment.dto.ManageEnrollmentRequest;
import net.tylerwade.registrationsystem.exception.HttpRequestException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface EnrollmentService {

    List<Enrollment> findAllByStudent(Authentication authentication);

    List<Enrollment> findAllByStudent(String studentId);

    Enrollment create(CreateEnrollmentRequest createEnrollmentRequest, Authentication authentication) throws HttpRequestException;

    Enrollment update(Long enrollmentId, ManageEnrollmentRequest manageEnrollmentRequest, Authentication authentication) throws HttpRequestException;

    void delete(Long enrollmentId, Authentication authentication) throws HttpRequestException;

    int updateStartedEnrollments();



}
