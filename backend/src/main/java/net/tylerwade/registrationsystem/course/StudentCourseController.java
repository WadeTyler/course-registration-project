package net.tylerwade.registrationsystem.course;

import net.tylerwade.registrationsystem.common.APIResponse;
import net.tylerwade.registrationsystem.common.PageResponse;
import net.tylerwade.registrationsystem.course.dto.CourseDTO;
import net.tylerwade.registrationsystem.exception.HttpRequestException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/student/courses")
public class StudentCourseController {

    private final CourseService courseService;

    public StudentCourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping
    public ResponseEntity<APIResponse<PageResponse<CourseDTO>>> findAll(Pageable pageable) {
        Page<CourseDTO> page = courseService.findAll(pageable).map(Course::toDTO);
        PageResponse<CourseDTO> pageResponse = PageResponse.convertPage(page);

        return ResponseEntity.status(HttpStatus.OK).body(APIResponse.success("Courses retrieved.", pageResponse));
    }

    @GetMapping("/{courseId}")
    public ResponseEntity<APIResponse<CourseDTO>> findById(@PathVariable Long courseId) throws HttpRequestException {
        CourseDTO course = courseService.findById(courseId).toDTO();
        return ResponseEntity.status(HttpStatus.OK).body(APIResponse.success("Course retrieved.", course));
    }
}
