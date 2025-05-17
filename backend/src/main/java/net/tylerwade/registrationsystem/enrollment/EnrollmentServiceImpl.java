package net.tylerwade.registrationsystem.enrollment;

import net.tylerwade.registrationsystem.auth.User;
import net.tylerwade.registrationsystem.auth.UserService;
import net.tylerwade.registrationsystem.config.security.authorities.UserRole;
import net.tylerwade.registrationsystem.coursesection.CourseSection;
import net.tylerwade.registrationsystem.coursesection.CourseSectionService;
import net.tylerwade.registrationsystem.enrollment.dto.CreateEnrollmentRequest;
import net.tylerwade.registrationsystem.enrollment.dto.ManageEnrollmentRequest;
import net.tylerwade.registrationsystem.exception.HttpRequestException;
import net.tylerwade.registrationsystem.prerequisites.Prerequisite;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;

@Service
public class EnrollmentServiceImpl implements EnrollmentService {

    private final UserService userService;
    private final EnrollmentRepository enrollmentRepository;
    private final CourseSectionService courseSectionService;

    public EnrollmentServiceImpl(UserService userService, EnrollmentRepository enrollmentRepository, CourseSectionService courseSectionService) {
        this.userService = userService;
        this.enrollmentRepository = enrollmentRepository;
        this.courseSectionService = courseSectionService;
    }

    @Override
    public List<Enrollment> findAllByStudent(Authentication authentication) {
        String userId = userService.getUser(authentication).getId();
        return enrollmentRepository.findAllByStudent_IdOrderByCourseSection_Term_StartDateDesc(userId);
    }

    @Override
    public List<Enrollment> findAllByStudent(String studentId) {
        return enrollmentRepository.findAllByStudent_IdOrderByCourseSection_Term_StartDateDesc(studentId);
    }

    @Override
    public Enrollment create(CreateEnrollmentRequest createEnrollmentRequest, Authentication authentication) throws HttpRequestException {
        // Get student
        User student = userService.getUser(authentication);

        // Find target course section
        CourseSection courseSection = courseSectionService.findById(createEnrollmentRequest.courseSectionId());

        // Check if student already registered for the course
        if (enrollmentRepository.existsByStudent_IdAndCourseSection_Id(student.getId(), courseSection.getId())) {
            throw new HttpRequestException(HttpStatus.NOT_ACCEPTABLE, "You are already enrolled for this course section.");
        }

        // Check if registration open
        if (!courseSection.getTerm().isRegistrationOpen()) {
            throw new HttpRequestException(HttpStatus.NOT_ACCEPTABLE, "This course section is currently not open for registration.");
        }

        // Check if course section has room
        if (courseSection.getEnrollments().size() >= courseSection.getCapacity()) {
            throw new HttpRequestException(HttpStatus.NOT_ACCEPTABLE, "This course section has reached the maximum capacity.");
        }

        // Find students enrolled courses
        List<Enrollment> enrollments = findAllByStudent(student.getId());
        List<Prerequisite> prerequisites = courseSection.getCourse().getPrerequisites();

        // Check if user completed all prerequisites
        try {
            for (Prerequisite prerequisite : prerequisites) {
                // Check if completed
                Enrollment completedEnrollment = enrollments.stream()
                        // Filter to find the prerequisite in enrollments
                        .filter(enrollment -> enrollment
                                .getCourseSection().getCourse().getId().equals(prerequisite.getCourse().getId())
                        )
                        .findFirst()
                        .orElseThrow();

                // This means the student has enrolled in the prerequisites. Now check if term has ended.
                if (!completedEnrollment.getCourseSection().getTerm().hasEnded()) {
                    throw new RuntimeException();
                }

                // Check if Grade passed
                if (completedEnrollment.getGrade() < prerequisite.getMinimumGrade()) {
                    throw new RuntimeException();
                }
            }
        } catch (Exception e) {
            throw new HttpRequestException(HttpStatus.NOT_ACCEPTABLE, "You have not completed all prerequisites for this course.");
        }

        // Create enrollment
        Enrollment enrollment = Enrollment.builder()
                .student(student)
                .courseSection(courseSection)
                .grade(0)
                .status("NOT_STARTED")
                .build();

        // Save and return
        return enrollmentRepository.save(enrollment);
    }

    @Override
    public Enrollment update(Long enrollmentId, ManageEnrollmentRequest manageEnrollmentRequest, Authentication authentication) throws HttpRequestException {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new HttpRequestException(HttpStatus.NOT_FOUND, "Enrollment not found."));

        // Check if user modifying is the instructor for the course section, or an administrator
        User authUser = userService.getUser(authentication);
        if (!enrollment.getCourseSection().getInstructor().getId().equals(authUser.getId()) && !authUser.getGrantedAuthorities().contains(UserRole.ADMIN.getNameWithPrefix())) {
            throw new HttpRequestException(HttpStatus.UNAUTHORIZED, "You are not authorized to perform that action.");
        }

        // Update
        enrollment.setGrade(manageEnrollmentRequest.grade());
        enrollment.setStatus(manageEnrollmentRequest.status());

        // save and return
        return enrollmentRepository.save(enrollment);
    }

    @Override
    public void delete(Long enrollmentId, Authentication authentication) throws HttpRequestException {
        // Check if enrollment exists
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new HttpRequestException(HttpStatus.NOT_FOUND, "Enrollment not found."));

        // Check if AuthUser is either the student, the course section instructor, or an admin
        User authUser = userService.getUser(authentication);
        if (!enrollment.getStudent().getId().equals(authUser.getId()) && !enrollment.getCourseSection().getInstructor().getId().equals(authUser.getId()) && !authUser.getGrantedAuthorities().contains(UserRole.ADMIN.getNameWithPrefix())) {
            throw new HttpRequestException(HttpStatus.UNAUTHORIZED, "You are not authorized to perform that action.");
        }

        // Check if course is already finished
        if (enrollment.getCourseSection().getTerm().hasEnded()) {
            throw new HttpRequestException(HttpStatus.NOT_ACCEPTABLE, "This course has already ended.");
        }

        // Check if enrollment has already completed
        if (enrollment.getStatus().equals("COMPLETED")) {
            throw new HttpRequestException(HttpStatus.NOT_ACCEPTABLE, "You have already completed this course.");
        }

        enrollmentRepository.delete(enrollment);
    }

    @Override
    public int updateStartedEnrollments() {
        List<Enrollment> unupdatedStartedEnrollments = enrollmentRepository.findAllByStatusIsAndCourseSection_Term_StartDateBefore("NOT_STARTED", new Date(System.currentTimeMillis()));
        unupdatedStartedEnrollments.forEach(enrollment -> enrollment.setStatus("STARTED"));
        enrollmentRepository.saveAll(unupdatedStartedEnrollments);

        return unupdatedStartedEnrollments.size();
    }
}
