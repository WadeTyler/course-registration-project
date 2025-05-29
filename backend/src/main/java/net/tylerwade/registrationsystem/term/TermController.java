package net.tylerwade.registrationsystem.term;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import net.tylerwade.registrationsystem.common.APIResponse;
import net.tylerwade.registrationsystem.common.PageResponse;
import net.tylerwade.registrationsystem.exception.HttpRequestException;
import net.tylerwade.registrationsystem.term.dto.ManageTermRequest;
import net.tylerwade.registrationsystem.term.dto.TermDTO;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.lang.model.type.NullType;

@Tag(name = "Term Controller", description = "Operations related to terms.")
@RestController
@RequestMapping("/api/terms")
public class TermController {

    private final TermService termService;

    public TermController(TermService termService) {
        this.termService = termService;
    }

    // Find all terms
    @Operation(summary = "Find all terms.", description = "Returns a PageResponse of terms.")
    @ApiResponse(responseCode = "200", description = "Terms retrieved.")
    @GetMapping
    public ResponseEntity<APIResponse<PageResponse<TermDTO>>> findAll(@ParameterObject Pageable pageable) {
        Page<TermDTO> page = termService.findAll(pageable).map(Term::toDTO);
        PageResponse<TermDTO> pageResponse = PageResponse.convertPage(page);

        return ResponseEntity.status(HttpStatus.OK)
                .body(APIResponse.success("Terms retrieved.", pageResponse));
    }

    // Find Specific term
    @Operation(summary = "Find a specific term.", description = "Returns an APIResponse of a term.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Term found."),
            @ApiResponse(responseCode = "404", description = "Term not found.")
    })
    @GetMapping("/{termId}")
    public ResponseEntity<APIResponse<TermDTO>> findById(
            @PathVariable Long termId
    ) throws HttpRequestException {
        TermDTO term = termService.findById(termId).toDTO();
        return ResponseEntity.status(HttpStatus.OK)
                .body(APIResponse.success("Term retrieved.", term));
    }

    /// ADMIN ENDPOINTS

    // Create Term
    @Operation(summary = "Create a new term. (ADMIN)", description = "Creates a new Term.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Term created"),
            @ApiResponse(responseCode = "400", description = "Fields invalid."),
            @ApiResponse(responseCode = "409", description = "Term already exists with start and end date."),
            @ApiResponse(responseCode = "403", description = "Unauthorized.")
    })
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<APIResponse<TermDTO>> create(@Valid @RequestBody ManageTermRequest manageTermRequest) throws HttpRequestException {
        TermDTO term = termService.create(manageTermRequest).toDTO();
        return ResponseEntity.status(HttpStatus.CREATED).body(APIResponse.success("Term created.", term));
    }

    // Update Term
    @Operation(summary = "Update a term. (ADMIN)", description = "Updates a term.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Term updated."),
            @ApiResponse(responseCode = "400", description = "Fields invalid."),
            @ApiResponse(responseCode = "409", description = "Term already exists with start and end date."),
            @ApiResponse(responseCode = "403", description = "Unauthorized.")
    })
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{termId}")
    public ResponseEntity<APIResponse<TermDTO>> update(@PathVariable Long termId, @Valid @RequestBody ManageTermRequest manageTermRequest) throws HttpRequestException {
        TermDTO term = termService.update(termId, manageTermRequest).toDTO();
        return ResponseEntity.status(HttpStatus.OK).body(APIResponse.success("Term updated.", term));
    }

    // Delete Term
    @Operation(summary = "Deletes a term. (ADMIN)", description = "Deletes a term.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Term deleted."),
            @ApiResponse(responseCode = "404", description = "Term not found"),
            @ApiResponse(responseCode = "403", description = "Unauthorized.")
    })
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{termId}")
    public ResponseEntity<APIResponse<NullType>> delete(@PathVariable Long termId) throws HttpRequestException {
        termService.delete(termId);
        return ResponseEntity.status(HttpStatus.OK).body(APIResponse.success("Term deleted.", null));
    }

}
