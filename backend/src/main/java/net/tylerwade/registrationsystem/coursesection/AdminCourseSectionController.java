package net.tylerwade.registrationsystem.coursesection;

import jakarta.validation.Valid;
import net.tylerwade.registrationsystem.common.APIResponse;
import net.tylerwade.registrationsystem.coursesection.dto.CourseSectionDTO;
import net.tylerwade.registrationsystem.coursesection.dto.ManageCourseSectionRequest;
import net.tylerwade.registrationsystem.exception.HttpRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.lang.model.type.NullType;

@RestController
@RequestMapping("/api/admin/courses/{courseId}/sections")
public class AdminCourseSectionController {

    private final CourseSectionService courseSectionService;

    public AdminCourseSectionController(CourseSectionService courseSectionService) {
        this.courseSectionService = courseSectionService;
    }

    @PostMapping
    public ResponseEntity<APIResponse<CourseSectionDTO>> create(@PathVariable Long courseId, @Valid @RequestBody ManageCourseSectionRequest manageCourseSectionRequest) throws HttpRequestException {
        CourseSectionDTO courseSection = courseSectionService.create(courseId, manageCourseSectionRequest).toDTO();

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(APIResponse.success("Course Section created.", courseSection));
    }

    @PutMapping("/{sectionId}")
    public ResponseEntity<APIResponse<CourseSectionDTO>> update(@PathVariable Long sectionId, @Valid @RequestBody ManageCourseSectionRequest manageCourseSectionRequest) throws HttpRequestException {
        CourseSectionDTO courseSection = courseSectionService.update(sectionId, manageCourseSectionRequest).toDTO();

        return ResponseEntity.status(HttpStatus.OK)
                .body(APIResponse.success("Course Section updated.", courseSection));
    }

    @DeleteMapping("/{sectionId}")
    public ResponseEntity<APIResponse<NullType>> delete(@PathVariable Long sectionId) throws HttpRequestException {
        courseSectionService.delete(sectionId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(APIResponse.success("Course section deleted."));
    }
}
