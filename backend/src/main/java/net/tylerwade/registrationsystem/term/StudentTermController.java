package net.tylerwade.registrationsystem.term;

import net.tylerwade.registrationsystem.common.APIResponse;
import net.tylerwade.registrationsystem.common.PageResponse;
import net.tylerwade.registrationsystem.exception.HttpRequestException;
import net.tylerwade.registrationsystem.term.dto.TermDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/student/terms")
@PreAuthorize("hasAuthority('term:read')")
public class StudentTermController {

    private final TermService termService;

    public StudentTermController(TermService termService) {
        this.termService = termService;
    }

    @GetMapping
    public ResponseEntity<APIResponse<PageResponse<TermDTO>>> findAll(Pageable pageable) {
        Page<TermDTO> page = termService.findAll(pageable).map(Term::toDTO);
        PageResponse<TermDTO> pageResponse = PageResponse.convertPage(page);

        return ResponseEntity.status(HttpStatus.OK)
                .body(APIResponse.success("Terms retrieved.", pageResponse));
    }

    @GetMapping("/{termId}")
    public ResponseEntity<APIResponse<TermDTO>> findById(@PathVariable Long termId) throws HttpRequestException {
        TermDTO term = termService.findById(termId).toDTO();
        return ResponseEntity.status(HttpStatus.OK)
                .body(APIResponse.success("Term retrieved.", term));
    }

}
