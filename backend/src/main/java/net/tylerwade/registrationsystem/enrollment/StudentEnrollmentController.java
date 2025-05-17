package net.tylerwade.registrationsystem.enrollment;

import jakarta.validation.Valid;
import net.tylerwade.registrationsystem.common.APIResponse;
import net.tylerwade.registrationsystem.enrollment.dto.CreateEnrollmentRequest;
import net.tylerwade.registrationsystem.enrollment.dto.EnrollmentDTO;
import net.tylerwade.registrationsystem.exception.HttpRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.lang.model.type.NullType;
import java.util.List;

@RestController
@RequestMapping("/api/student/enrollments")
public class StudentEnrollmentController {

    private final EnrollmentService enrollmentService;

    public StudentEnrollmentController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    @GetMapping
    public ResponseEntity<APIResponse<List<EnrollmentDTO>>> findAllByStudent(Authentication authentication) {
        List<EnrollmentDTO> enrollments = enrollmentService.findAllByStudent(authentication).stream().map(Enrollment::toDTO).toList();

        return ResponseEntity.status(HttpStatus.OK).body(APIResponse.success("Enrollments retrieved.", enrollments));
    }

    @PostMapping
    public ResponseEntity<APIResponse<EnrollmentDTO>> create(Authentication authentication, @Valid @RequestBody CreateEnrollmentRequest createEnrollmentRequest) throws HttpRequestException {
        EnrollmentDTO enrollment = enrollmentService.create(createEnrollmentRequest, authentication).toDTO();

        return ResponseEntity.status(HttpStatus.CREATED).body(APIResponse.success("Enrollment successful.", enrollment));
    }

    @DeleteMapping("/{enrollmentId}")
    public ResponseEntity<APIResponse<NullType>> delete(Authentication authentication, @PathVariable Long enrollmentId) throws HttpRequestException {
        enrollmentService.delete(enrollmentId, authentication);
        return ResponseEntity.status(HttpStatus.OK).body(APIResponse.success("Enrollment deleted."));
    }
}
