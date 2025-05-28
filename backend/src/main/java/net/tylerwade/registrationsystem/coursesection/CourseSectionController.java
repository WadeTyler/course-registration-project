package net.tylerwade.registrationsystem.coursesection;

import jakarta.validation.Valid;
import net.tylerwade.registrationsystem.common.APIResponse;
import net.tylerwade.registrationsystem.coursesection.dto.CourseSectionDTO;
import net.tylerwade.registrationsystem.coursesection.dto.InstructorCourseSectionDTO;
import net.tylerwade.registrationsystem.coursesection.dto.ManageCourseSectionRequest;
import net.tylerwade.registrationsystem.exception.HttpRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.lang.model.type.NullType;
import java.util.List;

@RestController
public class CourseSectionController {

    private final CourseSectionService courseSectionService;

    public CourseSectionController(CourseSectionService courseSectionService) {
        this.courseSectionService = courseSectionService;
    }

    // Find all sections by course Id
    @GetMapping("/api/courses/{courseId}/sections")
    public ResponseEntity<APIResponse<List<CourseSectionDTO>>> findAllByCourse_Id(@PathVariable Long courseId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(APIResponse.success("Course Sections retrieved.",
                        courseSectionService.findAllByCourse_Id(courseId).stream().map(CourseSection::toDTO).toList()));
    }

    // Find target section by id
    @GetMapping("/api/sections/{sectionId}")
    public ResponseEntity<APIResponse<CourseSectionDTO>> findById(@PathVariable Long sectionId) throws HttpRequestException {
        CourseSectionDTO courseSection = courseSectionService.findById(sectionId).toDTO();
        return ResponseEntity.status(HttpStatus.OK).body(APIResponse.success("Course section retrieved.", courseSection));
    }

    /// --- INSTRUCTOR ENDPOINTS ---

    // Find all assigned courses
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_INSTRUCTOR')")
    @GetMapping("/api/sections/assigned")
    public ResponseEntity<APIResponse<List<InstructorCourseSectionDTO>>> findAssignedCourseSections(Authentication authentication) {
        List<InstructorCourseSectionDTO> assignedCourseSections = courseSectionService.findAssignedCourseSections_AsInstructor(authentication).stream()
                .map(CourseSection::toInstructorDTO).toList();

        return ResponseEntity.status(HttpStatus.OK)
                .body(APIResponse.success("Assigned course sections retrieved.", assignedCourseSections));
    }

    // Find assigned course
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_INSTRUCTOR')")
    @GetMapping("/api/sections/assigned/{sectionId}")
    public ResponseEntity<APIResponse<InstructorCourseSectionDTO>> findAssignedCourseSectionById(Authentication authentication, @PathVariable Long sectionId) throws HttpRequestException {
        InstructorCourseSectionDTO assignedCourseSection = courseSectionService.findAssignedCourseSectionById_AsInstructor(sectionId, authentication).toInstructorDTO();

        return ResponseEntity.status(HttpStatus.OK)
                .body(APIResponse.success("Assigned course section retrieved.", assignedCourseSection));
    }

    ///  --- ADMIN ENDPOINTS ---

    // Create new Section
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/api/course/{courseId}/sections")
    public ResponseEntity<APIResponse<CourseSectionDTO>> create(@PathVariable Long courseId, @Valid @RequestBody ManageCourseSectionRequest manageCourseSectionRequest) throws HttpRequestException {
        CourseSectionDTO courseSection = courseSectionService.create(courseId, manageCourseSectionRequest).toDTO();

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(APIResponse.success("Course Section created.", courseSection));
    }


    // Update Section
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/api/sections/{sectionId}")
    public ResponseEntity<APIResponse<CourseSectionDTO>> update(@PathVariable Long sectionId, @Valid @RequestBody ManageCourseSectionRequest manageCourseSectionRequest) throws HttpRequestException {
        CourseSectionDTO courseSection = courseSectionService.update(sectionId, manageCourseSectionRequest).toDTO();

        return ResponseEntity.status(HttpStatus.OK)
                .body(APIResponse.success("Course Section updated.", courseSection));
    }

    // Delete section
    @DeleteMapping("/api/sections/{sectionId}")
    public ResponseEntity<APIResponse<NullType>> delete(@PathVariable Long sectionId) throws HttpRequestException {
        courseSectionService.delete(sectionId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(APIResponse.success("Course section deleted."));
    }


}
