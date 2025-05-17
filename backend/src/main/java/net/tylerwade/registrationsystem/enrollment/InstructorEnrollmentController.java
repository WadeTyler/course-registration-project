package net.tylerwade.registrationsystem.enrollment;

import jakarta.validation.Valid;
import net.tylerwade.registrationsystem.common.APIResponse;
import net.tylerwade.registrationsystem.enrollment.dto.EnrollmentDTO;
import net.tylerwade.registrationsystem.enrollment.dto.ManageEnrollmentRequest;
import net.tylerwade.registrationsystem.exception.HttpRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/instructor/enrollments")
public class InstructorEnrollmentController {

    private final EnrollmentService enrollmentService;

    public InstructorEnrollmentController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    @PutMapping("/{enrollmentId}")
    public ResponseEntity<APIResponse<EnrollmentDTO>> update(Authentication authentication, @PathVariable Long enrollmentId, @Valid @RequestBody ManageEnrollmentRequest manageEnrollmentRequest) throws HttpRequestException {
        EnrollmentDTO enrollment = enrollmentService.update(enrollmentId, manageEnrollmentRequest, authentication).toDTO();

        return ResponseEntity.status(HttpStatus.OK).body(APIResponse.success("Enrollment updated.", enrollment));
    }
}
