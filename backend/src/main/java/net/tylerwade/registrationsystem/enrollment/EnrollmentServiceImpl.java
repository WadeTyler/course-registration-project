package net.tylerwade.registrationsystem.enrollment;

import net.tylerwade.registrationsystem.auth.User;
import net.tylerwade.registrationsystem.auth.UserService;
import net.tylerwade.registrationsystem.coursesection.CourseSection;
import net.tylerwade.registrationsystem.coursesection.CourseSectionService;
import net.tylerwade.registrationsystem.enrollment.dto.CreateEnrollmentRequest;
import net.tylerwade.registrationsystem.enrollment.dto.ManageEnrollmentRequest;
import net.tylerwade.registrationsystem.enrollment.enums.EnrollmentStatus;
import net.tylerwade.registrationsystem.exception.HttpRequestException;
import net.tylerwade.registrationsystem.prerequisites.Prerequisite;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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
    public List<Enrollment> findAllByStudent(Long studentId, Authentication authentication) throws HttpRequestException {
        User authUser = userService.getUser(authentication);

        // Check if authUser is student, or an instructor/admin
        if (!authUser.getId().equals(studentId) && !authUser.isInstructor() && !authUser.isAdmin()) {
            throw new HttpRequestException(HttpStatus.FORBIDDEN, "You are not permitted to view this student's enrollment records.");
        }

        return enrollmentRepository.findAllByStudent_IdOrderByCourseSection_Term_StartDateDesc(studentId);
    }

    @Override
    public List<Enrollment> findAllByStudent(Long studentId) {
        return enrollmentRepository.findAllByStudent_IdOrderByCourseSection_Term_StartDateDesc(studentId);
    }

    @Override
    public Enrollment create(Long studentId, CreateEnrollmentRequest createEnrollmentRequest, Authentication authentication) throws HttpRequestException {

        User authUser = userService.getUser(authentication);

        // Check if authUser is student, or an admin
        if (!authUser.getId().equals(studentId) && !authUser.isAdmin()) {
            throw new HttpRequestException(HttpStatus.FORBIDDEN, "You are not permitted to create enrollments for this student.");
        }

        // Get student
        User student = userService.findById(studentId);
        if (!student.isStudent()) {
            throw new HttpRequestException(HttpStatus.FORBIDDEN, "User must be a student to enroll in a course section.");
        }

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
                if (completedEnrollment.getGrade().compareTo(prerequisite.getMinimumGrade()) < 0) {
                    throw new RuntimeException();
                }
            }
        } catch (Exception e) {
            throw new HttpRequestException(HttpStatus.NOT_ACCEPTABLE, "You have not completed all prerequisites for this course.");
        }

        // Create enrollment
        Enrollment enrollment = Enrollment.builder()
                .studentId(student.getId())
                .student(student)
                .courseSectionId(courseSection.getId())
                .courseSection(courseSection)
                .grade(new BigDecimal(0))
                .status(EnrollmentStatus.NOT_STARTED.getValue())
                .build();


        // Save and return
        enrollmentRepository.save(enrollment);

        // Update enrolledCount
        courseSectionService.refreshEnrollmentCount(courseSection.getId());

        return enrollment;
    }

    @Override
    public Enrollment update(Long studentId, Long courseSectionId, ManageEnrollmentRequest manageEnrollmentRequest, Authentication authentication) throws HttpRequestException {
        Enrollment enrollment = enrollmentRepository.findById(new EnrollmentId(studentId, courseSectionId))
                .orElseThrow(() -> new HttpRequestException(HttpStatus.NOT_FOUND, "Enrollment not found."));

        // Check if user modifying is the instructor for the course section, or an administrator
        User authUser = userService.getUser(authentication);
        if (!enrollment.getCourseSection().getInstructor().getId().equals(authUser.getId()) && !authUser.isAdmin()) {
            throw new HttpRequestException(HttpStatus.UNAUTHORIZED, "You are not authorized to perform that action.");
        }

        // Update
        enrollment.setGrade(manageEnrollmentRequest.grade());
        enrollment.setStatus(manageEnrollmentRequest.status().getValue());

        // save and return
        return enrollmentRepository.save(enrollment);
    }

    @Override
    public void delete(Long studentId, Long courseSectionId, Authentication authentication) throws HttpRequestException {
        // Check if enrollment exists
        Enrollment enrollment = enrollmentRepository.findById(new EnrollmentId(studentId, courseSectionId))
                .orElseThrow(() -> new HttpRequestException(HttpStatus.NOT_FOUND, "Enrollment not found."));

        // Check if AuthUser is either the student, the course section instructor, or an admin
        User authUser = userService.getUser(authentication);
        if (!enrollment.getStudent().getId().equals(authUser.getId()) && !enrollment.getCourseSection().getInstructor().getId().equals(authUser.getId()) && !authUser.isAdmin()) {
            throw new HttpRequestException(HttpStatus.UNAUTHORIZED, "You are not authorized to perform that action.");
        }

        // Check if course is already finished
        if (enrollment.getCourseSection().getTerm().hasEnded()) {
            throw new HttpRequestException(HttpStatus.NOT_ACCEPTABLE, "This course has already ended.");
        }

        // Check if enrollment has already completed
        if (enrollment.getStatus().equals(EnrollmentStatus.COMPLETED.getValue())) {
            throw new HttpRequestException(HttpStatus.NOT_ACCEPTABLE, "You have already completed this course.");
        }

        courseSectionService.refreshEnrollmentCount(enrollment.getCourseSection().getId());
        enrollmentRepository.delete(enrollment);
    }

    @Override
    public int updateStartedEnrollments() {
        List<Enrollment> unupdatedStartedEnrollments = enrollmentRepository.findAllByStatusIsAndCourseSection_Term_StartDateBefore(EnrollmentStatus.NOT_STARTED.getValue(), new Date(System.currentTimeMillis()));
        unupdatedStartedEnrollments.forEach(enrollment -> enrollment.setStatus(EnrollmentStatus.STARTED.getValue()));
        enrollmentRepository.saveAll(unupdatedStartedEnrollments);

        return unupdatedStartedEnrollments.size();
    }
}
