package net.tylerwade.registrationsystem.term;

import jakarta.validation.Valid;
import net.tylerwade.registrationsystem.common.APIResponse;
import net.tylerwade.registrationsystem.exception.HttpRequestException;
import net.tylerwade.registrationsystem.term.dto.ManageTermRequest;
import net.tylerwade.registrationsystem.term.dto.TermDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.lang.model.type.NullType;

@RestController
@RequestMapping("/api/admin/terms")
@PreAuthorize("hasAuthority('term:write')")
public class AdminTermController {

    private final TermService termService;

    public AdminTermController(TermService termService) {
        this.termService = termService;
    }

    @PostMapping
    public ResponseEntity<APIResponse<TermDTO>> create(@Valid @RequestBody ManageTermRequest manageTermRequest) throws HttpRequestException {
        TermDTO term = termService.create(manageTermRequest).toDTO();
        return ResponseEntity.status(HttpStatus.CREATED).body(APIResponse.success("Term created.", term));
    }

    @PutMapping("/{termId}")
    public ResponseEntity<APIResponse<TermDTO>> update(@PathVariable Long termId, @Valid @RequestBody ManageTermRequest manageTermRequest) throws HttpRequestException {
        TermDTO term = termService.update(termId, manageTermRequest).toDTO();
        return ResponseEntity.status(HttpStatus.OK).body(APIResponse.success("Term updated.", term));
    }

    @DeleteMapping("/{termId}")
    public ResponseEntity<APIResponse<NullType>> delete(@PathVariable Long termId) throws HttpRequestException {
        termService.delete(termId);
        return ResponseEntity.status(HttpStatus.OK).body(APIResponse.success("Term deleted.", null));
    }
}
