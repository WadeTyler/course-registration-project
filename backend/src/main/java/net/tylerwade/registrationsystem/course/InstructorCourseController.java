package net.tylerwade.registrationsystem.course;

import net.tylerwade.registrationsystem.common.APIResponse;
import net.tylerwade.registrationsystem.course.dto.CourseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/instructor/courses")
@PreAuthorize("hasAnyRole('INSTRUCTOR', 'ADMIN')")
public class InstructorCourseController {

    private final CourseService courseService;

    public InstructorCourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping
    public ResponseEntity<?> findAssignedCourses(Authentication authentication) {
        List<CourseDTO> courses = courseService.findAssignedCourses(authentication).stream()
                .map(Course::toDTO)
                .toList();

        return ResponseEntity.status(HttpStatus.OK).body(APIResponse.success("Assigned courses retrieved.", courses));
    }
}
