package net.tylerwade.registrationsystem.coursesection;

import net.tylerwade.registrationsystem.coursesection.dto.ManageCourseSectionRequest;
import net.tylerwade.registrationsystem.exception.HttpRequestException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service interface for managing course sections.
 */
@Service
public interface CourseSectionService {

    /**
     * Finds all course sections for a given course.
     *
     * @param courseId the ID of the course
     * @return list of course sections
     */
    List<CourseSection> findAllByCourse_Id(Long courseId);

    /**
     * Finds a course section by its ID.
     *
     * @param sectionId the ID of the section
     * @return the course section
     * @throws HttpRequestException if not found
     */
    CourseSection findById(Long sectionId) throws HttpRequestException;

    /**
     * Finds all course sections assigned to the authenticated instructor.
     *
     * @param authentication the authentication object
     * @return list of assigned course sections
     */
    List<CourseSection> findAssignedCourseSections_AsInstructor(Authentication authentication);

    /**
     * Finds a specific assigned course section by ID for the authenticated instructor.
     *
     * @param sectionId the ID of the section
     * @param authentication the authentication object
     * @return the assigned course section
     * @throws HttpRequestException if not found or not assigned
     */
    CourseSection findAssignedCourseSectionById_AsInstructor(Long sectionId, Authentication authentication) throws HttpRequestException;

    /**
     * Creates a new course section for a course.
     *
     * @param courseId the ID of the course
     * @param manageCourseSectionRequest the request data for the section
     * @return the created course section
     * @throws HttpRequestException if creation fails
     */
    CourseSection create(Long courseId, ManageCourseSectionRequest manageCourseSectionRequest) throws HttpRequestException;

    /**
     * Updates an existing course section.
     *
     * @param sectionId the ID of the section
     * @param manageCourseSectionRequest the request data for the section
     * @return the updated course section
     * @throws HttpRequestException if update fails
     */
    CourseSection update(Long sectionId, ManageCourseSectionRequest manageCourseSectionRequest) throws HttpRequestException;

    /**
     * Deletes a course section by its ID.
     *
     * @param sectionId the ID of the section
     * @throws HttpRequestException if deletion fails
     */
    void delete(Long sectionId) throws HttpRequestException;

}
