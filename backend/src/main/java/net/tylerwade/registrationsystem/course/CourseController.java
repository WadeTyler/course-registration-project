package net.tylerwade.registrationsystem.course;

import jakarta.validation.Valid;
import net.tylerwade.registrationsystem.common.APIResponse;
import net.tylerwade.registrationsystem.common.PageResponse;
import net.tylerwade.registrationsystem.course.dto.CourseDTO;
import net.tylerwade.registrationsystem.course.dto.ManageCourseRequest;
import net.tylerwade.registrationsystem.exception.HttpRequestException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.lang.model.type.NullType;


@RestController
@RequestMapping("/api/courses")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    // Find all courses
    @GetMapping
    public ResponseEntity<APIResponse<PageResponse<CourseDTO>>> findAll(Pageable pageable) {
        Page<CourseDTO> page = courseService.findAll(pageable).map(Course::toDTO);
        PageResponse<CourseDTO> pageResponse = PageResponse.convertPage(page);

        return ResponseEntity.status(HttpStatus.OK).body(APIResponse.success("Courses retrieved.", pageResponse));
    }

    // Find a course by id
    @GetMapping("/{courseId}")
    public ResponseEntity<APIResponse<CourseDTO>> findById(@PathVariable Long courseId) throws HttpRequestException {
        CourseDTO course = courseService.findById(courseId).toDTO();
        return ResponseEntity.status(HttpStatus.OK).body(APIResponse.success("Course retrieved.", course));
    }

    // Create a course
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<APIResponse<CourseDTO>> create(@Valid @RequestBody ManageCourseRequest manageCourseRequest) throws HttpRequestException {
        CourseDTO course = courseService.create(manageCourseRequest).toDTO();
        return ResponseEntity.status(HttpStatus.CREATED).body(APIResponse.success("Course Created.", course));
    }

    // Update a course
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{courseId}")
    public ResponseEntity<APIResponse<CourseDTO>> update(@PathVariable Long courseId, @Valid @RequestBody ManageCourseRequest manageCourseRequest) throws HttpRequestException {
        CourseDTO course = courseService.update(courseId, manageCourseRequest).toDTO();
        return ResponseEntity.status(HttpStatus.OK).body(APIResponse.success("Course updated.", course));
    }

    // Delete a course
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{courseId}")
    public ResponseEntity<APIResponse<NullType>> delete(@PathVariable Long courseId) throws HttpRequestException {
        courseService.delete(courseId);
        return ResponseEntity.status(HttpStatus.OK).body(APIResponse.success("Course deleted."));
    }

}
