package net.tylerwade.registrationsystem.prerequisites;

import jakarta.validation.Valid;
import net.tylerwade.registrationsystem.common.APIResponse;
import net.tylerwade.registrationsystem.exception.HttpRequestException;
import net.tylerwade.registrationsystem.prerequisites.dto.ManagePrerequisiteRequest;
import net.tylerwade.registrationsystem.prerequisites.dto.PrerequisiteDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/courses/{courseId}/prerequisites")
public class AdminPrerequisiteController {

    private final PrerequisiteService prerequisiteService;

    public AdminPrerequisiteController(PrerequisiteService prerequisiteService) {
        this.prerequisiteService = prerequisiteService;
    }

    @PostMapping
    public ResponseEntity<APIResponse<PrerequisiteDTO>> create(@PathVariable Long courseId,
                                                               @Valid @RequestBody ManagePrerequisiteRequest managePrerequisiteRequest) throws HttpRequestException {
        PrerequisiteDTO prerequisite = prerequisiteService.create(courseId, managePrerequisiteRequest).toDTO();
        return ResponseEntity.status(HttpStatus.CREATED).body(APIResponse.success("Prerequisite created.", prerequisite));
    }

    @PutMapping("/{prerequisiteId}")
    public ResponseEntity<APIResponse<PrerequisiteDTO>> update(@PathVariable Long courseId,
                                                               @PathVariable Long prerequisiteId,
                                                               @Valid @RequestBody ManagePrerequisiteRequest managePrerequisiteRequest) throws HttpRequestException {
        PrerequisiteDTO prerequisite = prerequisiteService.update(courseId, prerequisiteId, managePrerequisiteRequest).toDTO();
        return ResponseEntity.status(HttpStatus.CREATED).body(APIResponse.success("Prerequisite updated.", prerequisite));
    }

    @DeleteMapping("/{prerequisiteId}")
    public ResponseEntity<APIResponse<PrerequisiteDTO>> delete(@PathVariable Long prerequisiteId) throws HttpRequestException {
        prerequisiteService.delete(prerequisiteId);
        return ResponseEntity.status(HttpStatus.CREATED).body(APIResponse.success("Prerequisite deleted."));
    }

}


