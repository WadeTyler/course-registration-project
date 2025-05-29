package net.tylerwade.registrationsystem.course;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import net.tylerwade.registrationsystem.common.APIResponse;
import net.tylerwade.registrationsystem.common.PageResponse;
import net.tylerwade.registrationsystem.course.dto.CourseDTO;
import net.tylerwade.registrationsystem.course.dto.ManageCourseRequest;
import net.tylerwade.registrationsystem.exception.HttpRequestException;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.lang.model.type.NullType;

@Tag(name = "Course Controller", description = "Operations related to courses")
@RestController
@RequestMapping("/api/courses")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    /**
     * Find all courses
     */
    @Operation(summary = "Get all courses", description = "Returns a paginated list of all courses.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Courses retrieved successfully")
    })
    @GetMapping
    public ResponseEntity<APIResponse<PageResponse<CourseDTO>>> findAll(@ParameterObject Pageable pageable) {
        Page<CourseDTO> page = courseService.findAll(pageable).map(Course::toDTO);
        PageResponse<CourseDTO> pageResponse = PageResponse.convertPage(page);

        return ResponseEntity.status(HttpStatus.OK).body(APIResponse.success("Courses retrieved.", pageResponse));
    }

    /**
     * Find a course by id
     */
    @Operation(summary = "Get course by ID", description = "Returns a course by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Course retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Course not found")
    })
    @GetMapping("/{courseId}")
    public ResponseEntity<APIResponse<CourseDTO>> findById(
            @Parameter(description = "ID of the course") @PathVariable Long courseId) throws HttpRequestException {
        CourseDTO course = courseService.findById(courseId).toDTO();
        return ResponseEntity.status(HttpStatus.OK).body(APIResponse.success("Course retrieved.", course));
    }

    /**
     * Create a course
     */
    @Operation(summary = "Create a course (Admin)", description = "Creates a new course. Admin only.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Course created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "409", description = "Course already exists with department and code or name")
    })
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<APIResponse<CourseDTO>> create(
            @Valid @RequestBody ManageCourseRequest manageCourseRequest) throws HttpRequestException {
        CourseDTO course = courseService.create(manageCourseRequest).toDTO();
        return ResponseEntity.status(HttpStatus.CREATED).body(APIResponse.success("Course Created.", course));
    }

    /**
     * Update a course
     */
    @Operation(summary = "Update a course (Admin)", description = "Updates an existing course. Admin only.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Course updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Course not found"),
            @ApiResponse(responseCode = "409", description = "Course already exists with department and code or name")
    })
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{courseId}")
    public ResponseEntity<APIResponse<CourseDTO>> update(
            @Parameter(description = "ID of the course") @PathVariable Long courseId,
            @Valid @RequestBody ManageCourseRequest manageCourseRequest) throws HttpRequestException {
        CourseDTO course = courseService.update(courseId, manageCourseRequest).toDTO();
        return ResponseEntity.status(HttpStatus.OK).body(APIResponse.success("Course updated.", course));
    }

    /**
     * Delete a course
     */
    @Operation(summary = "Delete a course (Admin)", description = "Deletes a course by its ID. Admin only.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Course deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Course not found")
    })
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{courseId}")
    public ResponseEntity<APIResponse<NullType>> delete(
            @Parameter(description = "ID of the course") @PathVariable Long courseId) throws HttpRequestException {
        courseService.delete(courseId);
        return ResponseEntity.status(HttpStatus.OK).body(APIResponse.success("Course deleted."));
    }

}
