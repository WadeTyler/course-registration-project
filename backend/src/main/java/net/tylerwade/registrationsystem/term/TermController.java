package net.tylerwade.registrationsystem.term;

import jakarta.validation.Valid;
import net.tylerwade.registrationsystem.common.APIResponse;
import net.tylerwade.registrationsystem.common.PageResponse;
import net.tylerwade.registrationsystem.exception.HttpRequestException;
import net.tylerwade.registrationsystem.term.dto.ManageTermRequest;
import net.tylerwade.registrationsystem.term.dto.TermDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.lang.model.type.NullType;

@RestController
@RequestMapping("/api/terms")
public class TermController {

    private final TermService termService;

    public TermController(TermService termService) {
        this.termService = termService;
    }

    // Find all terms
    @GetMapping
    public ResponseEntity<APIResponse<PageResponse<TermDTO>>> findAll(Pageable pageable) {
        Page<TermDTO> page = termService.findAll(pageable).map(Term::toDTO);
        PageResponse<TermDTO> pageResponse = PageResponse.convertPage(page);

        return ResponseEntity.status(HttpStatus.OK)
                .body(APIResponse.success("Terms retrieved.", pageResponse));
    }

    // Find Specific term
    @GetMapping("/{termId}")
    public ResponseEntity<APIResponse<TermDTO>> findById(@PathVariable Long termId) throws HttpRequestException {
        TermDTO term = termService.findById(termId).toDTO();
        return ResponseEntity.status(HttpStatus.OK)
                .body(APIResponse.success("Term retrieved.", term));
    }

    /// ADMIN ENDPOINTS

    // Create Term
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<APIResponse<TermDTO>> create(@Valid @RequestBody ManageTermRequest manageTermRequest) throws HttpRequestException {
        TermDTO term = termService.create(manageTermRequest).toDTO();
        return ResponseEntity.status(HttpStatus.CREATED).body(APIResponse.success("Term created.", term));
    }

    // Update Term
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{termId}")
    public ResponseEntity<APIResponse<TermDTO>> update(@PathVariable Long termId, @Valid @RequestBody ManageTermRequest manageTermRequest) throws HttpRequestException {
        TermDTO term = termService.update(termId, manageTermRequest).toDTO();
        return ResponseEntity.status(HttpStatus.OK).body(APIResponse.success("Term updated.", term));
    }

    // Delete Term
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{termId}")
    public ResponseEntity<APIResponse<NullType>> delete(@PathVariable Long termId) throws HttpRequestException {
        termService.delete(termId);
        return ResponseEntity.status(HttpStatus.OK).body(APIResponse.success("Term deleted.", null));
    }

}
