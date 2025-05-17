package net.tylerwade.registrationsystem.coursesection;

import net.tylerwade.registrationsystem.common.APIResponse;
import net.tylerwade.registrationsystem.coursesection.dto.InstructorCourseSectionDTO;
import net.tylerwade.registrationsystem.exception.HttpRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// Uses separate endpoints. Not all are nested.
@RestController
public class InstructorCourseSectionController {

    private final CourseSectionService courseSectionService;

    public InstructorCourseSectionController(CourseSectionService courseSectionService) {
        this.courseSectionService = courseSectionService;
    }

    @GetMapping("/api/instructor/sections")
    public ResponseEntity<APIResponse<List<InstructorCourseSectionDTO>>> findAssignedCourseSections(Authentication authentication) {
        List<InstructorCourseSectionDTO> assignedCourseSections = courseSectionService.findAssignedCourseSections_AsInstructor(authentication).stream()
                .map(CourseSection::toInstructorDTO).toList();

        return ResponseEntity.status(HttpStatus.OK)
                .body(APIResponse.success("Assigned course sections retrieved.", assignedCourseSections));
    }

    @GetMapping("/api/instructor/courses/{courseId}/sections/{sectionId}")
    public ResponseEntity<APIResponse<InstructorCourseSectionDTO>> findAssignedCourseSectionById(Authentication authentication, @PathVariable Long sectionId) throws HttpRequestException {
        InstructorCourseSectionDTO assignedCourseSection = courseSectionService.findAssignedCourseSectionById_AsInstructor(sectionId, authentication).toInstructorDTO();

        return ResponseEntity.status(HttpStatus.OK)
                .body(APIResponse.success("Assigned course section retrieved.", assignedCourseSection));
    }

}
