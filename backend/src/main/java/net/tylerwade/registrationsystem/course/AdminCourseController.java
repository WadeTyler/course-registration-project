package net.tylerwade.registrationsystem.course;

import jakarta.validation.Valid;
import net.tylerwade.registrationsystem.common.APIResponse;
import net.tylerwade.registrationsystem.course.dto.CourseDTO;
import net.tylerwade.registrationsystem.course.dto.ManageCourseRequest;
import net.tylerwade.registrationsystem.exception.HttpRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.lang.model.type.NullType;

@RestController
@RequestMapping("/api/admin/courses")
public class AdminCourseController {

    private final CourseService courseService;

    public AdminCourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @PostMapping
    public ResponseEntity<APIResponse<CourseDTO>> create(@Valid @RequestBody ManageCourseRequest manageCourseRequest) throws HttpRequestException {
        CourseDTO course = courseService.create(manageCourseRequest).toDTO();
        return ResponseEntity.status(HttpStatus.CREATED).body(APIResponse.success("Course Created.", course));
    }

    @PutMapping("/{courseId}")
    public ResponseEntity<APIResponse<CourseDTO>> update(@PathVariable Long courseId, @Valid @RequestBody ManageCourseRequest manageCourseRequest) throws HttpRequestException {
        CourseDTO course = courseService.update(courseId, manageCourseRequest).toDTO();
        return ResponseEntity.status(HttpStatus.OK).body(APIResponse.success("Course updated.", course));
    }

    @DeleteMapping("/{courseId}")
    public ResponseEntity<APIResponse<NullType>> delete(@PathVariable Long courseId) throws HttpRequestException {
        courseService.delete(courseId);
        return ResponseEntity.status(HttpStatus.OK).body(APIResponse.success("Course deleted."));
    }
}
