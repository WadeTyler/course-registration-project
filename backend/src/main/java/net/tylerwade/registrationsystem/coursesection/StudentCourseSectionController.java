package net.tylerwade.registrationsystem.coursesection;

import net.tylerwade.registrationsystem.common.APIResponse;
import net.tylerwade.registrationsystem.coursesection.dto.CourseSectionDTO;
import net.tylerwade.registrationsystem.exception.HttpRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/student/courses/{courseId}/sections")
public class StudentCourseSectionController {

    private final CourseSectionService courseSectionService;

    public StudentCourseSectionController(CourseSectionService courseSectionService) {
        this.courseSectionService = courseSectionService;
    }

    @GetMapping
    public ResponseEntity<APIResponse<List<CourseSectionDTO>>> findAllByCourse_Id(@PathVariable Long courseId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(APIResponse.success("Course Sections retrieved.",
                        courseSectionService.findAllByCourse_Id(courseId).stream().map(CourseSection::toDTO).toList()));
    }

    @GetMapping("/{sectionId}")
    public ResponseEntity<APIResponse<CourseSectionDTO>> findById(@PathVariable Long sectionId) throws HttpRequestException {
        CourseSectionDTO courseSection = courseSectionService.findById(sectionId).toDTO();
        return ResponseEntity.status(HttpStatus.OK).body(APIResponse.success("Course section retrieved.", courseSection));
    }
}
