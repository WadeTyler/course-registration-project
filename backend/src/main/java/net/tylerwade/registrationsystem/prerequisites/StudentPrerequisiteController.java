package net.tylerwade.registrationsystem.prerequisites;

import net.tylerwade.registrationsystem.common.APIResponse;
import net.tylerwade.registrationsystem.prerequisites.dto.PrerequisiteDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/student/courses/{courseId}/prerequisites")
public class StudentPrerequisiteController {

    private final PrerequisiteService prerequisiteService;

    public StudentPrerequisiteController(PrerequisiteService prerequisiteService) {
        this.prerequisiteService = prerequisiteService;
    }

    @GetMapping
    public ResponseEntity<APIResponse<List<PrerequisiteDTO>>> findAllByCourseId(@PathVariable Long courseId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(APIResponse.success("Prerequisites retrieved.",
                        prerequisiteService.findAllByCourseId(courseId).stream().map(Prerequisite::toDTO).toList())
                );
    }
}
