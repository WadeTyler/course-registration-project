package net.tylerwade.registrationsystem.coursesection;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import net.tylerwade.registrationsystem.coursesection.dto.CourseSectionDTO;
import net.tylerwade.registrationsystem.coursesection.dto.InstructorCourseSectionDTO;
import net.tylerwade.registrationsystem.coursesection.dto.ManageCourseSectionRequest;
import net.tylerwade.registrationsystem.exception.HttpRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Course Section Controller", description = "Operations related to Course Section.")
@RestController
@RequestMapping("/api/sections")
public class CourseSectionController {

    private final CourseSectionService courseSectionService;

    public CourseSectionController(CourseSectionService courseSectionService) {
        this.courseSectionService = courseSectionService;
    }


    /*
     * Find All or find all by course id
     */
    @Operation(summary = "Retrieves all sections or all sections within a course.", description = "Finds all sections or all sections within a course")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found"),
            @ApiResponse(responseCode = "404", description = "Course not found.")
    })
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CourseSectionDTO> findAllByCourse_Id(@RequestParam(required = false) Long courseId) throws HttpRequestException {
        if (courseId != null) {
            return courseSectionService.findAllByCourse_Id(courseId).stream().map(CourseSection::toDTO).toList();
        } else {
            return courseSectionService.findAll().stream().map(CourseSection::toDTO).toList();
        }
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
    @ResponseStatus(HttpStatus.OK)
    public CourseSectionDTO findById(@PathVariable Long sectionId) throws HttpRequestException {
        return courseSectionService.findById(sectionId).toDTO();
    }

    /// --- INSTRUCTOR ENDPOINTS ---

    /*
     *   Find assigned courses
     */
    @Operation(summary = "Find assigned sections (INSTRUCTOR, ADMIN)", description = "Find instructor assigned courses. Instructor or Admin only.")
    @ApiResponse(responseCode = "200", description = "Retrieved")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_INSTRUCTOR')")
    @GetMapping("//assigned")
    @ResponseStatus(HttpStatus.OK)
    public List<InstructorCourseSectionDTO> findAssignedCourseSections(Authentication authentication) {
        return courseSectionService.findAssignedCourseSections_AsInstructor(authentication).stream()
                .map(CourseSection::toInstructorDTO).toList();
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
    @GetMapping("/assigned/{sectionId}")
    @ResponseStatus(HttpStatus.OK)
    public InstructorCourseSectionDTO findAssignedCourseSectionById(Authentication authentication, @PathVariable Long sectionId) throws HttpRequestException {
        return courseSectionService.findAssignedCourseSectionById_AsInstructor(sectionId, authentication).toInstructorDTO();
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
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CourseSectionDTO create(@RequestParam Long courseId, @Valid @RequestBody ManageCourseSectionRequest manageCourseSectionRequest) throws HttpRequestException {
        return courseSectionService.create(courseId, manageCourseSectionRequest).toDTO();
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
    @PutMapping("/{sectionId}")
    @ResponseStatus(HttpStatus.OK)
    public CourseSectionDTO update(@PathVariable Long sectionId, @Valid @RequestBody ManageCourseSectionRequest manageCourseSectionRequest) throws HttpRequestException {
        return courseSectionService.update(sectionId, manageCourseSectionRequest).toDTO();
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
    @DeleteMapping("/{sectionId}")
    @ResponseStatus(HttpStatus.OK)
    public String delete(@PathVariable Long sectionId) throws HttpRequestException {
        courseSectionService.delete(sectionId);
        return "Course Section deleted.";
    }
}