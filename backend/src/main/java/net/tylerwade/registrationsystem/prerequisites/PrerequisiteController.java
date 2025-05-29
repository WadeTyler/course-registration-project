package net.tylerwade.registrationsystem.prerequisites;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import net.tylerwade.registrationsystem.common.APIResponse;
import net.tylerwade.registrationsystem.exception.HttpRequestException;
import net.tylerwade.registrationsystem.prerequisites.dto.ManagePrerequisiteRequest;
import net.tylerwade.registrationsystem.prerequisites.dto.PrerequisiteDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Prerequisite Controller", description = "Operations for prerequisites.")
@RestController
@RequestMapping("/api/courses/{courseId}/prerequisites")
public class PrerequisiteController {

    private final PrerequisiteService prerequisiteService;

    public PrerequisiteController(PrerequisiteService prerequisiteService) {
        this.prerequisiteService = prerequisiteService;
    }

    /**
     * Find all prerequisites for a course
     */
    @Operation(summary = "Get all prerequisites for a course", description = "Returns a list of prerequisites for the specified course.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Prerequisites retrieved successfully")
    })
    @GetMapping
    public ResponseEntity<APIResponse<List<PrerequisiteDTO>>> findAllByCourseId(
            @Parameter(description = "ID of the course") @PathVariable Long courseId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(APIResponse.success("Prerequisites retrieved.",
                        prerequisiteService.findAllByCourseId(courseId).stream().map(Prerequisite::toDTO).toList())
                );
    }

    /**
     * Create a new prerequisite for a course
     */
    @Operation(summary = "Create a prerequisite for a course (ADMIN)", description = "Creates a new prerequisite for the specified course. Admin only.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Prerequisite created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<APIResponse<PrerequisiteDTO>> create(
            @Parameter(description = "ID of the course") @PathVariable Long courseId,
            @Valid @RequestBody ManagePrerequisiteRequest managePrerequisiteRequest) throws HttpRequestException {
        PrerequisiteDTO prerequisite = prerequisiteService.create(courseId, managePrerequisiteRequest).toDTO();
        return ResponseEntity.status(HttpStatus.CREATED).body(APIResponse.success("Prerequisite created.", prerequisite));
    }

    /**
     * Update a prerequisite for a course
     */
    @Operation(summary = "Update a prerequisite for a course (ADMIN)", description = "Updates an existing prerequisite for the specified course. Admin only.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Prerequisite updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input"),
        @ApiResponse(responseCode = "404", description = "Prerequisite not found")
    })
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{prerequisiteId}")
    public ResponseEntity<APIResponse<PrerequisiteDTO>> update(
            @Parameter(description = "ID of the course") @PathVariable Long courseId,
            @Parameter(description = "ID of the prerequisite") @PathVariable Long prerequisiteId,
            @Valid @RequestBody ManagePrerequisiteRequest managePrerequisiteRequest) throws HttpRequestException {
        PrerequisiteDTO prerequisite = prerequisiteService.update(courseId, prerequisiteId, managePrerequisiteRequest).toDTO();
        return ResponseEntity.status(HttpStatus.CREATED).body(APIResponse.success("Prerequisite updated.", prerequisite));
    }

    /**
     * Delete a prerequisite for a course
     */
    @Operation(summary = "Delete a prerequisite for a course (ADMIN)", description = "Deletes a prerequisite from the specified course. Admin only.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Prerequisite deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Prerequisite not found")
    })
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{prerequisiteId}")
    public ResponseEntity<APIResponse<PrerequisiteDTO>> delete(
            @Parameter(description = "ID of the prerequisite") @PathVariable Long prerequisiteId) throws HttpRequestException {
        prerequisiteService.delete(prerequisiteId);
        return ResponseEntity.status(HttpStatus.CREATED).body(APIResponse.success("Prerequisite deleted."));
    }
}
