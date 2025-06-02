package net.tylerwade.registrationsystem.enrollment;

import net.tylerwade.registrationsystem.enrollment.dto.CreateEnrollmentRequest;
import net.tylerwade.registrationsystem.enrollment.dto.ManageEnrollmentRequest;
import net.tylerwade.registrationsystem.exception.HttpRequestException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service interface for managing enrollments.
 */
@Service
public interface EnrollmentService {

    /**
     * Finds all enrollments for the authenticated student.
     *
     * @param authentication the authentication object
     * @return list of enrollments
     */
    List<Enrollment> findAllByStudent(Long studentId, Authentication authentication) throws HttpRequestException;

    /**
     * Finds all enrollments for a student by their ID.
     *
     * @param studentId the student ID
     * @return list of enrollments
     */
    List<Enrollment> findAllByStudent(Long studentId);

    /**
     * Creates a new enrollment for the authenticated student.
     *
     * @param studentId               the target student ID
     * @param createEnrollmentRequest the enrollment request
     * @param authentication          the authentication object
     * @return the created enrollment
     * @throws HttpRequestException if the enrollment cannot be created
     */
    Enrollment create(Long studentId, CreateEnrollmentRequest createEnrollmentRequest, Authentication authentication) throws HttpRequestException;

    /**
     * Updates an existing enrollment.
     *
     * @param studentId               the student ID
     * @param courseSectionId         the enrollment course section
     * @param manageEnrollmentRequest the update request
     * @param authentication          the authentication object
     * @return the updated enrollment
     * @throws HttpRequestException if the enrollment cannot be updated
     */
    Enrollment update(Long studentId, Long courseSectionId, ManageEnrollmentRequest manageEnrollmentRequest, Authentication authentication) throws HttpRequestException;

    /**
     * Deletes an enrollment.
     *
     * @param studentId       the student ID
     * @param courseSectionId the enrollment course section ID
     * @param authentication  the authentication object
     * @throws HttpRequestException if the enrollment cannot be deleted
     */
    void delete(Long studentId, Long courseSectionId, Authentication authentication) throws HttpRequestException;

    /**
     * Updates the status of started enrollments.
     *
     * @return the number of updated enrollments
     */
    int updateStartedEnrollments();

}
