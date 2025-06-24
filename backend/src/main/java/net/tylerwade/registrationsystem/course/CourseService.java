package net.tylerwade.registrationsystem.course;

import net.tylerwade.registrationsystem.course.dto.ManageCourseRequest;
import net.tylerwade.registrationsystem.exception.HttpRequestException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service interface for managing courses.
 */
@Service
public interface CourseService {

    /**
     * Retrieves a paginated list of all courses.
     *
     * @return a page of courses
     */
    List<Course> findAll();

    /**
     * Finds a course by its ID.
     *
     * @param courseId the ID of the course
     * @return the found course
     * @throws HttpRequestException if the course is not found
     */
    Course findById(Long courseId) throws HttpRequestException;

    /**
     * Creates a new course.
     *
     * @param manageCourseRequest the request containing course details
     * @return the created course
     * @throws HttpRequestException if creation fails
     */
    Course create(ManageCourseRequest manageCourseRequest) throws HttpRequestException;

    /**
     * Updates an existing course.
     *
     * @param courseId the ID of the course to update
     * @param manageCourseRequest the request containing updated course details
     * @return the updated course
     * @throws HttpRequestException if update fails
     */
    Course update(Long courseId, ManageCourseRequest manageCourseRequest) throws HttpRequestException;

    /**
     * Deletes a course by its ID.
     *
     * @param courseId the ID of the course to delete
     * @throws HttpRequestException if deletion fails
     */
    void delete(Long courseId) throws HttpRequestException;
}
