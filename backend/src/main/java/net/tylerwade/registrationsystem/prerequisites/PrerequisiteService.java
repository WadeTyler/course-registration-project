package net.tylerwade.registrationsystem.prerequisites;

import net.tylerwade.registrationsystem.exception.HttpRequestException;
import net.tylerwade.registrationsystem.prerequisites.dto.ManagePrerequisiteRequest;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service interface for managing course prerequisites.
 */
@Service
public interface PrerequisiteService {

    /**
     * Finds all prerequisites for a given course.
     *
     * @param courseId the ID of the course
     * @return list of prerequisites
     */
    List<Prerequisite> findAllByCourseId(Long courseId);

    /**
     * Creates a new prerequisite for a course.
     *
     * @param courseId the ID of the course
     * @param managePrerequisiteRequest the request data for the prerequisite
     * @return the created prerequisite
     * @throws HttpRequestException if creation fails
     */
    Prerequisite create(Long courseId, ManagePrerequisiteRequest managePrerequisiteRequest) throws HttpRequestException;

    /**
     * Updates an existing prerequisite for a course.
     *
     * @param prerequisiteId            the ID of the prerequisite to update
     * @param managePrerequisiteRequest the request data for the prerequisite
     * @return the updated prerequisite
     * @throws HttpRequestException if update fails
     */
    Prerequisite update(Long prerequisiteId, ManagePrerequisiteRequest managePrerequisiteRequest) throws HttpRequestException;

    /**
     * Deletes a prerequisite by its ID.
     *
     * @param prerequisiteId the ID of the prerequisite to delete
     * @throws HttpRequestException if deletion fails
     */
    void delete(Long prerequisiteId) throws HttpRequestException;
}
