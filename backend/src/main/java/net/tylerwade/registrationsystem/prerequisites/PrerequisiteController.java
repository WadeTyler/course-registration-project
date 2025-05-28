package net.tylerwade.registrationsystem.prerequisites;

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

@RestController
@RequestMapping("/api/courses/{courseId}/prerequisites")
public class PrerequisiteController {

    private final PrerequisiteService prerequisiteService;

    public PrerequisiteController(PrerequisiteService prerequisiteService) {
        this.prerequisiteService = prerequisiteService;
    }

    // Find all prerequisites for a course
    @GetMapping
    public ResponseEntity<APIResponse<List<PrerequisiteDTO>>> findAllByCourseId(@PathVariable Long courseId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(APIResponse.success("Prerequisites retrieved.",
                        prerequisiteService.findAllByCourseId(courseId).stream().map(Prerequisite::toDTO).toList())
                );
    }

    // Create a new prerequisite for a course
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<APIResponse<PrerequisiteDTO>> create(@PathVariable Long courseId,
                                                               @Valid @RequestBody ManagePrerequisiteRequest managePrerequisiteRequest) throws HttpRequestException {
        PrerequisiteDTO prerequisite = prerequisiteService.create(courseId, managePrerequisiteRequest).toDTO();
        return ResponseEntity.status(HttpStatus.CREATED).body(APIResponse.success("Prerequisite created.", prerequisite));
    }

    // Update a prerequisite
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{prerequisiteId}")
    public ResponseEntity<APIResponse<PrerequisiteDTO>> update(@PathVariable Long courseId,
                                                               @PathVariable Long prerequisiteId,
                                                               @Valid @RequestBody ManagePrerequisiteRequest managePrerequisiteRequest) throws HttpRequestException {
        PrerequisiteDTO prerequisite = prerequisiteService.update(courseId, prerequisiteId, managePrerequisiteRequest).toDTO();
        return ResponseEntity.status(HttpStatus.CREATED).body(APIResponse.success("Prerequisite updated.", prerequisite));
    }

    // Delete a prerequisites
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{prerequisiteId}")
    public ResponseEntity<APIResponse<PrerequisiteDTO>> delete(@PathVariable Long prerequisiteId) throws HttpRequestException {
        prerequisiteService.delete(prerequisiteId);
        return ResponseEntity.status(HttpStatus.CREATED).body(APIResponse.success("Prerequisite deleted."));
    }
}
