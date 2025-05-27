package net.tylerwade.registrationsystem.course;

import net.tylerwade.registrationsystem.course.dto.ManageCourseRequest;
import net.tylerwade.registrationsystem.exception.HttpRequestException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;

    public CourseServiceImpl(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Override
    public Page<Course> findAll(Pageable pageable) {
        return courseRepository.findAll(pageable);
    }

    @Override
    public Course findById(Long courseId) throws HttpRequestException {
        return courseRepository.findById(courseId).orElseThrow(() -> new HttpRequestException(HttpStatus.NOT_FOUND, "Course not found."));
    }

    @Override
    public List<Course> findAssignedCourses(Authentication authentication) {
        // TODO: Implement findAssignedCourses
        return List.of();
    }

    @Override
    public Course create(ManageCourseRequest manageCourseRequest) throws HttpRequestException {
        // Check if course exists with department and code already
        if (courseRepository.existsByDepartmentIgnoreCaseAndCode(manageCourseRequest.department(), manageCourseRequest.code())) {
            throw new HttpRequestException(HttpStatus.CONFLICT, "A Course already exists with department and code.");
        }

        // Check if course exits by name
        if (courseRepository.existsByTitleIgnoreCase(manageCourseRequest.title())) {
            throw new HttpRequestException(HttpStatus.CONFLICT, "A Course already exists with that name.");
        }

        // Create new course
        Course course = Course.builder()
                .department(manageCourseRequest.department())
                .code(manageCourseRequest.code())
                .title(manageCourseRequest.title())
                .description(manageCourseRequest.description())
                .credits(manageCourseRequest.credits())
                .prerequisites(new ArrayList<>())
                .courseSections(new ArrayList<>())
                .build();

        // Save and return
        return courseRepository.save(course);
    }

    @Override
    public Course update(Long courseId, ManageCourseRequest manageCourseRequest) throws HttpRequestException {
        // Find target course
        Course course = findById(courseId);

        // Check if course exists with department and code already and is not target course.
        if (courseRepository.existsByDepartmentIgnoreCaseAndCodeAndIdNot(manageCourseRequest.department(), manageCourseRequest.code(), courseId)) {
            throw new HttpRequestException(HttpStatus.CONFLICT, "A Course already exists with department and code.");
        }

        // Check if course exits by name
        if (courseRepository.existsByTitleIgnoreCaseAndIdNot(manageCourseRequest.title(), courseId)) {
            throw new HttpRequestException(HttpStatus.CONFLICT, "A Course already exists with that name.");
        }

        // Update course
        course.setDepartment(manageCourseRequest.department());
        course.setCode(manageCourseRequest.code());
        course.setTitle(manageCourseRequest.title());
        course.setDescription(manageCourseRequest.description());
        course.setCredits(manageCourseRequest.credits());

        // Save and return
        return courseRepository.save(course);
    }

    @Override
    public void delete(Long courseId) throws HttpRequestException {
        // Check if course exists
        if (!courseRepository.existsById(courseId)) {
            throw new HttpRequestException(HttpStatus.NOT_FOUND, "Course not found.");
        }

        // Delete
        courseRepository.deleteById(courseId);
    }
}
