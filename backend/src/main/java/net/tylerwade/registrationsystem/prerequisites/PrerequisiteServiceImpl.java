package net.tylerwade.registrationsystem.prerequisites;

import net.tylerwade.registrationsystem.course.Course;
import net.tylerwade.registrationsystem.course.CourseService;
import net.tylerwade.registrationsystem.exception.HttpRequestException;
import net.tylerwade.registrationsystem.prerequisites.dto.ManagePrerequisiteRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PrerequisiteServiceImpl implements PrerequisiteService{

    private final CourseService courseService;
    private final PrerequisiteRepository prerequisiteRepository;

    public PrerequisiteServiceImpl(CourseService courseService, PrerequisiteRepository prerequisiteRepository) {
        this.courseService = courseService;
        this.prerequisiteRepository = prerequisiteRepository;
    }

    @Override
    public List<Prerequisite> findAllByCourseId(Long courseId) {
        return prerequisiteRepository.findByCourse_Id(courseId);
    }

    @Override
    public Prerequisite create(Long courseId, ManagePrerequisiteRequest managePrerequisiteRequest) throws HttpRequestException {
        // Check if prerequisite already exists for course and required course.
        if (prerequisiteRepository.existsByCourse_IdAndRequiredCourse_Id(courseId, managePrerequisiteRequest.requiredCourseId())) {
            throw new HttpRequestException(HttpStatus.CONFLICT, "A prerequisite already exists with that required course.");
        }

        // Find target Course and required course
        Course course = courseService.findById(courseId);
        Course requiredCourse = courseService.findById(managePrerequisiteRequest.requiredCourseId());

        // Create prerequisite
        Prerequisite prerequisite = Prerequisite.builder()
                .course(course)
                .requiredCourse(requiredCourse)
                .minimumGrade(managePrerequisiteRequest.minimumGrade())
                .build();

        // Save and return
        return prerequisiteRepository.save(prerequisite);
    }

    @Override
    public Prerequisite update(Long prerequisiteId, ManagePrerequisiteRequest managePrerequisiteRequest) throws HttpRequestException {

        // Find target prerequisite
        Prerequisite prerequisite = prerequisiteRepository.findById(prerequisiteId)
                .orElseThrow(() -> new HttpRequestException(HttpStatus.NOT_FOUND, "Prerequisite not found."));

        // Update
        prerequisite.setMinimumGrade(managePrerequisiteRequest.minimumGrade());

        // save and return
        return prerequisiteRepository.save(prerequisite);
    }

    @Override
    public void delete(Long prerequisiteId) throws HttpRequestException {
        if (!prerequisiteRepository.existsById(prerequisiteId)) {
            throw new HttpRequestException(HttpStatus.NOT_FOUND, "Prerequisite not found.");
        }

        prerequisiteRepository.deleteById(prerequisiteId);
    }
}
