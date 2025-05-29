package net.tylerwade.registrationsystem.coursesection;

import net.tylerwade.registrationsystem.auth.User;
import net.tylerwade.registrationsystem.auth.UserService;
import net.tylerwade.registrationsystem.config.security.authorities.UserRole;
import net.tylerwade.registrationsystem.course.Course;
import net.tylerwade.registrationsystem.course.CourseService;
import net.tylerwade.registrationsystem.coursesection.dto.ManageCourseSectionRequest;
import net.tylerwade.registrationsystem.exception.HttpRequestException;
import net.tylerwade.registrationsystem.term.Term;
import net.tylerwade.registrationsystem.term.TermService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseSectionServiceImpl implements CourseSectionService {

    private final CourseSectionRepository courseSectionRepository;
    private final UserService userService;
    private final TermService termService;
    private final CourseService courseService;

    public CourseSectionServiceImpl(CourseSectionRepository courseSectionRepository, UserService userService, TermService termService, CourseService courseService) {
        this.courseSectionRepository = courseSectionRepository;
        this.userService = userService;
        this.termService = termService;
        this.courseService = courseService;
    }


    @Override
    public List<CourseSection> findAllByCourse_Id(Long courseId) throws HttpRequestException {
        Course course = courseService.findById(courseId);
        return courseSectionRepository.findAllByCourse_Id(course.getId());
    }

    @Override
    public CourseSection findById(Long sectionId) throws HttpRequestException {
        return courseSectionRepository.findById(sectionId).orElseThrow(() -> new HttpRequestException(HttpStatus.NOT_FOUND, "Course Section not found."));
    }

    @Override
    public List<CourseSection> findAssignedCourseSections_AsInstructor(Authentication authentication) {
        Long instructorId = userService.getUser(authentication).getId();
        return courseSectionRepository.findAllByInstructor_Id(instructorId);
    }

    @Override
    public CourseSection findAssignedCourseSectionById_AsInstructor(Long sectionId, Authentication authentication) throws HttpRequestException {
        Long instructorId = userService.getUser(authentication).getId();
        return courseSectionRepository.findByIdAndInstructor_Id(sectionId, instructorId).orElseThrow(() -> new HttpRequestException(HttpStatus.NOT_FOUND, "Assigned Course section not found."));
    }

    @Override
    public CourseSection create(Long courseId, ManageCourseSectionRequest manageCourseSectionRequest) throws HttpRequestException {
        // Check if term exists
        Term term = termService.findById(manageCourseSectionRequest.termId());

        // Find target course
        Course course = courseService.findById(courseId);

        User instructor = null;

        // If instructor is present
        if (manageCourseSectionRequest.instructorId() != null) {
            // Find target instructor
            instructor = userService.findById(manageCourseSectionRequest.instructorId());

            // Check instructor is ADMIN or INSTRUCTOR
            if (!instructor.getGrantedAuthorities().contains(UserRole.INSTRUCTOR.getNameWithPrefix()) && !instructor.getGrantedAuthorities().contains(UserRole.ADMIN.getNameWithPrefix())) {
                throw new HttpRequestException(HttpStatus.NOT_ACCEPTABLE, "Target Instructor does not have INSTRUCTOR or ADMIN permissions.");
            }
        }


        // Create course section
        CourseSection courseSection = CourseSection.builder()
                .course(course)
                .term(term)
                .instructor(instructor)
                .room(manageCourseSectionRequest.room())
                .capacity(manageCourseSectionRequest.capacity())
                .schedule(manageCourseSectionRequest.schedule())
                .enrolledCount(0)
                .build();

        // Save and return
        return courseSectionRepository.save(courseSection);
    }

    @Override
    public CourseSection update(Long sectionId, ManageCourseSectionRequest manageCourseSectionRequest) throws HttpRequestException {

        // Find target section
        CourseSection courseSection = findById(sectionId);

        // Check if term exists
        Term term = termService.findById(manageCourseSectionRequest.termId());

        User instructor = null;

        // If instructor is present
        if (manageCourseSectionRequest.instructorId() != null) {
            // Find target instructor
            instructor = userService.findById(manageCourseSectionRequest.instructorId());

            // Check instructor is ADMIN or INSTRUCTOR
            if (!instructor.getGrantedAuthorities().contains(UserRole.INSTRUCTOR.getNameWithPrefix()) && !instructor.getGrantedAuthorities().contains(UserRole.ADMIN.getNameWithPrefix())) {
                throw new HttpRequestException(HttpStatus.NOT_ACCEPTABLE, "Target Instructor does not have INSTRUCTOR or ADMIN permissions.");
            }
        }

        // Check if trying to set capacity lower than current enrolled.
        if (courseSection.getEnrollments().size() > manageCourseSectionRequest.capacity()) {
            throw new HttpRequestException(HttpStatus.CONFLICT, "You cannot set capacity lower than the amount of students already enrolled.");
        }

        // Update
        courseSection.setTerm(term);
        courseSection.setInstructor(instructor);
        courseSection.setRoom(manageCourseSectionRequest.room());
        courseSection.setCapacity(manageCourseSectionRequest.capacity());
        courseSection.setSchedule(manageCourseSectionRequest.schedule());

        // Save and return
        return courseSectionRepository.save(courseSection);
    }

    @Override
    public void delete(Long sectionId) throws HttpRequestException {
        if (!courseSectionRepository.existsById(sectionId)) {
            throw new HttpRequestException(HttpStatus.NOT_FOUND, "Course Section not found.");
        }
        courseSectionRepository.deleteById(sectionId);
    }

    @Override
    public CourseSection refreshEnrollmentCount(Long courseSectionId) throws HttpRequestException {
        CourseSection courseSection = findById(courseSectionId);
        courseSection.setEnrolledCount(courseSection.getEnrollments().size());
        return courseSectionRepository.save(courseSection);
    }
}
