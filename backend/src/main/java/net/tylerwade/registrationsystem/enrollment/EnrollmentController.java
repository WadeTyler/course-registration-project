package net.tylerwade.registrationsystem.enrollment;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import net.tylerwade.registrationsystem.enrollment.dto.CreateEnrollmentRequest;
import net.tylerwade.registrationsystem.enrollment.dto.EnrollmentDTO;
import net.tylerwade.registrationsystem.enrollment.dto.ManageEnrollmentRequest;
import net.tylerwade.registrationsystem.exception.HttpRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Enrollment Controller", description = "Operations related to enrollments")
@RestController
@RequestMapping("/api/enrollments")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    public EnrollmentController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    /**
     * Find all enrollments for the authenticated student
     */
    @Operation(summary = "Get all enrollments for student", description = "Returns a list of all enrollments for the authenticated student.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Enrollments retrieved successfully")
    })
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<EnrollmentDTO> findAllByStudent(Authentication authentication) {
        return enrollmentService.findAllByStudent(authentication).stream().map(Enrollment::toDTO).toList();
    }

    /**
     * Create a new enrollment
     */
    @Operation(summary = "Create enrollment", description = "Creates a new enrollment for the authenticated student.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Enrollment created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EnrollmentDTO create(Authentication authentication, @Valid @RequestBody CreateEnrollmentRequest createEnrollmentRequest) throws HttpRequestException {
        return enrollmentService.create(createEnrollmentRequest, authentication).toDTO();
    }

    /**
     * Remove an enrollment
     */
    @Operation(summary = "Delete enrollment", description = "Deletes an enrollment for the authenticated student.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Enrollment deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Enrollment not found")
    })
    @DeleteMapping("/{enrollmentId}")
    @ResponseStatus(HttpStatus.OK)
    public String delete(Authentication authentication, @Parameter(description = "ID of the enrollment") @PathVariable Long enrollmentId) throws HttpRequestException {
        enrollmentService.delete(enrollmentId, authentication);
        return "Enrollment deleted.";
    }

    ///  --- INSTRUCTOR ENDPOINTS

    /**
     * Update an enrollment (Instructor/Admin only)
     */
    @Operation(summary = "Update enrollment (INSTRUCTOR, ADMIN)", description = "Updates an enrollment. Instructor and Admin only.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Enrollment updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Enrollment not found")
    })
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_INSTRUCTOR')")
    @PutMapping("/{enrollmentId}")
    @ResponseStatus(HttpStatus.OK)
    public EnrollmentDTO update(Authentication authentication, @Parameter(description = "ID of the enrollment") @PathVariable Long enrollmentId, @Valid @RequestBody ManageEnrollmentRequest manageEnrollmentRequest) throws HttpRequestException {
        return enrollmentService.update(enrollmentId, manageEnrollmentRequest, authentication).toDTO();
    }
}
