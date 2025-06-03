package net.tylerwade.registrationsystem.term;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import net.tylerwade.registrationsystem.common.PageResponse;
import net.tylerwade.registrationsystem.exception.HttpRequestException;
import net.tylerwade.registrationsystem.term.dto.ManageTermRequest;
import net.tylerwade.registrationsystem.term.dto.TermDTO;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
    @ResponseStatus(HttpStatus.OK)
    public PageResponse<TermDTO> findAll(@ParameterObject Pageable pageable) {
        Page<TermDTO> page = termService.findAll(pageable).map(Term::toDTO);
        return PageResponse.convertPage(page);
    }

    // Find Specific term
    @Operation(summary = "Find a specific term.", description = "Returns an APIResponse of a term.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Term found."),
            @ApiResponse(responseCode = "404", description = "Term not found.")
    })
    @GetMapping("/{termId}")
    @ResponseStatus(HttpStatus.OK)
    public TermDTO findById(
            @PathVariable Long termId
    ) throws HttpRequestException {
        return termService.findById(termId).toDTO();
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
    @ResponseStatus(HttpStatus.CREATED)
    public TermDTO create(@Valid @RequestBody ManageTermRequest manageTermRequest) throws HttpRequestException {
        return termService.create(manageTermRequest).toDTO();
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
    @ResponseStatus(HttpStatus.OK)
    public TermDTO update(@PathVariable Long termId, @Valid @RequestBody ManageTermRequest manageTermRequest) throws HttpRequestException {
        return termService.update(termId, manageTermRequest).toDTO();
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
    @ResponseStatus(HttpStatus.OK)
    public String delete(@PathVariable Long termId) throws HttpRequestException {
        termService.delete(termId);
        return "Term deleted.";
    }

}
