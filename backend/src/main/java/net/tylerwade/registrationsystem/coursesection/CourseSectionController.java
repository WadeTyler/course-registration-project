package net.tylerwade.registrationsystem.coursesection;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(name = "Course Section Controller", description = "Operations related to Course Section.")
@RestController
public class CourseSectionController {

    private final CourseSectionService courseSectionService;

    public CourseSectionController(CourseSectionService courseSectionService) {
        this.courseSectionService = courseSectionService;
    }

    /*
    * Find All by course ID
     */
    @Operation(summary = "Retrieves all sections within a course.", description = "Finds all sections by course id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found"),
            @ApiResponse(responseCode = "404", description = "Course not found.")
    })
    @GetMapping("/api/courses/{courseId}/sections")
    public ResponseEntity<APIResponse<List<CourseSectionDTO>>> findAllByCourse_Id(@PathVariable Long courseId) throws HttpRequestException {
        return ResponseEntity.status(HttpStatus.OK)
                .body(APIResponse.success("Course Sections retrieved.",
                        courseSectionService.findAllByCourse_Id(courseId).stream().map(CourseSection::toDTO).toList()));
    }

    /*
    * Find by ID
     */
    @Operation(summary = "Find course section by ID", description = "Finds a specified section by id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found"),
            @ApiResponse(responseCode = "404", description = "Section not found.")
    })
    @GetMapping("/api/sections/{sectionId}")
    public ResponseEntity<APIResponse<CourseSectionDTO>> findById(@PathVariable Long sectionId) throws HttpRequestException {
        CourseSectionDTO courseSection = courseSectionService.findById(sectionId).toDTO();
        return ResponseEntity.status(HttpStatus.OK).body(APIResponse.success("Course section retrieved.", courseSection));
    }

    /// --- INSTRUCTOR ENDPOINTS ---

    /*
     *   Find assigned courses
     */
    @Operation(summary = "Find assigned sections (INSTRUCTOR, ADMIN)", description = "Find instructor assigned courses. Instructor or Admin only.")
    @ApiResponse(responseCode = "200", description = "Retrieved")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_INSTRUCTOR')")
    @GetMapping("/api/sections/assigned")
    public ResponseEntity<APIResponse<List<InstructorCourseSectionDTO>>> findAssignedCourseSections(Authentication authentication) {
        List<InstructorCourseSectionDTO> assignedCourseSections = courseSectionService.findAssignedCourseSections_AsInstructor(authentication).stream()
                .map(CourseSection::toInstructorDTO).toList();

        return ResponseEntity.status(HttpStatus.OK)
                .body(APIResponse.success("Assigned course sections retrieved.", assignedCourseSections));
    }

    /*
     * Find assigned course
     */
    @Operation(summary = "Find assigned by id (INSTRUCTOR, ADMIN)", description = "Find instructor assigned course by id. Instructor or Admin only.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found"),
            @ApiResponse(responseCode = "404", description = "Assigned Section not found.")
    })
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_INSTRUCTOR')")
    @GetMapping("/api/sections/assigned/{sectionId}")
    public ResponseEntity<APIResponse<InstructorCourseSectionDTO>> findAssignedCourseSectionById(Authentication authentication, @PathVariable Long sectionId) throws HttpRequestException {
        InstructorCourseSectionDTO assignedCourseSection = courseSectionService.findAssignedCourseSectionById_AsInstructor(sectionId, authentication).toInstructorDTO();

        return ResponseEntity.status(HttpStatus.OK)
                .body(APIResponse.success("Assigned course section retrieved.", assignedCourseSection));
    }

    ///  --- ADMIN ENDPOINTS ---

    /**
     * Create new Section (Admin)
     */
    @Operation(summary = "Create a course section (Admin)", description = "Creates a new course section for the specified course. Admin only.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Course section created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/api/course/{courseId}/sections")
    public ResponseEntity<APIResponse<CourseSectionDTO>> create(@PathVariable Long courseId, @Valid @RequestBody ManageCourseSectionRequest manageCourseSectionRequest) throws HttpRequestException {
        CourseSectionDTO courseSection = courseSectionService.create(courseId, manageCourseSectionRequest).toDTO();

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(APIResponse.success("Course Section created.", courseSection));
    }

    /**
     * Update Section (Admin)
     */
    @Operation(summary = "Update a course section (Admin)", description = "Updates an existing course section. Admin only.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Course section updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input"),
        @ApiResponse(responseCode = "404", description = "Section not found")
    })
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/api/sections/{sectionId}")
    public ResponseEntity<APIResponse<CourseSectionDTO>> update(@PathVariable Long sectionId, @Valid @RequestBody ManageCourseSectionRequest manageCourseSectionRequest) throws HttpRequestException {
        CourseSectionDTO courseSection = courseSectionService.update(sectionId, manageCourseSectionRequest).toDTO();

        return ResponseEntity.status(HttpStatus.OK)
                .body(APIResponse.success("Course Section updated.", courseSection));
    }

    /**
     * Delete section (Admin)
     */
    @Operation(summary = "Delete a course section (Admin)", description = "Deletes a course section by its ID. Admin only.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Course section deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Section not found")
    })
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/api/sections/{sectionId}")
    public ResponseEntity<APIResponse<NullType>> delete(@PathVariable Long sectionId) throws HttpRequestException {
        courseSectionService.delete(sectionId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(APIResponse.success("Course section deleted."));
    }
}