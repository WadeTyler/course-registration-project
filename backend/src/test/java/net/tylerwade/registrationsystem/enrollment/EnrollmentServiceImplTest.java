package net.tylerwade.registrationsystem.enrollment;

import net.tylerwade.registrationsystem.auth.User;
import net.tylerwade.registrationsystem.auth.UserService;
import net.tylerwade.registrationsystem.auth.authority.Authority;
import net.tylerwade.registrationsystem.course.Course;
import net.tylerwade.registrationsystem.coursesection.CourseSection;
import net.tylerwade.registrationsystem.coursesection.CourseSectionService;
import net.tylerwade.registrationsystem.enrollment.dto.CreateEnrollmentRequest;
import net.tylerwade.registrationsystem.enrollment.dto.ManageEnrollmentRequest;
import net.tylerwade.registrationsystem.enrollment.enums.EnrollmentStatus;
import net.tylerwade.registrationsystem.exception.HttpRequestException;
import net.tylerwade.registrationsystem.term.Term;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class EnrollmentServiceImplTest {

    @Mock
    private EnrollmentRepository enrollmentRepository;

    @Mock
    private UserService userService;

    @Mock
    private CourseSectionService courseSectionService;

    @InjectMocks
    private EnrollmentServiceImpl enrollmentService;

    private List<User> mockUsers;
    private List<CourseSection> mockCourseSections;
    private List<Enrollment> mockEnrollments;
    private Authentication studentAuth;
    private Authentication instructorAuth;

    @BeforeEach
    void setup() {
        // Mock Authorities
        Authority adminAuthority = Authority.builder()
                .id(1)
                .name("ADMIN")
                .build();

        Authority instructorAuthority = Authority.builder()
                .id(2)
                .name("INSTRUCTOR")
                .build();

        Authority studentAuthority = Authority.builder()
                .id(3)
                .name("STUDENT")
                .build();

        // Mock Users
        User adminUser = User.builder()
                .id(1L)
                .username("admin@example.com")
                .firstName("Admin")
                .lastName("User")
                .password("encodedPassword")
                .userAuthorities(Set.of(adminAuthority))
                .build();

        User instructorUser = User.builder()
                .id(2L)
                .username("instructor@example.com")
                .firstName("Instructor")
                .lastName("User")
                .password("encodedPassword")
                .userAuthorities(Set.of(instructorAuthority))
                .build();

        User studentUser = User.builder()
                .id(3L)
                .username("student@example.com")
                .firstName("Student")
                .lastName("User")
                .password("encodedPassword")
                .userAuthorities(Set.of(studentAuthority))
                .build();

        User otherStudentUser = User.builder()
                .id(4L)
                .username("otherstudent@example.com")
                .firstName("Other")
                .lastName("Student")
                .password("encodedPassword")
                .userAuthorities(Set.of(studentAuthority))
                .build();

        mockUsers = List.of(adminUser, instructorUser, studentUser, otherStudentUser);

        // Mock Term
        Term mockTerm = Term.builder()
                .id(1L)
                .registrationStart(LocalDate.now().minusDays(7))
                .registrationEnd(LocalDate.now().plusDays(7))
                .startDate(LocalDate.now().plusDays(14))
                .endDate(LocalDate.now().plusDays(90))
                .build();

        // Mock Course
        Course mockCourse = Course.builder()
                .id(1L)
                .department("CMSC")
                .code(325)
                .title("Software Principles I")
                .description("Learn about software principles!")
                .credits(3)
                .prerequisites(new ArrayList<>())
                .build();

        // Mock CourseSection
        CourseSection mockCourseSection = CourseSection.builder()
                .id(1L)
                .course(mockCourse)
                .term(mockTerm)
                .instructor(instructorUser)
                .enrollments(new ArrayList<>())
                .enrolledCount(0)
                .room("Remote")
                .capacity(30)
                .schedule("M - F 12:00 AM - 1:30 PM")
                .build();

        mockCourseSections = List.of(mockCourseSection);

        // Mock Enrollments
        Enrollment mockEnrollment = Enrollment.builder()
                .studentId(studentUser.getId())
                .student(studentUser)
                .courseSectionId(mockCourseSection.getId())
                .courseSection(mockCourseSection)
                .grade(new BigDecimal("0"))
                .status(EnrollmentStatus.NOT_STARTED.getValue())
                .build();

        mockEnrollments = List.of(mockEnrollment);

        // Create Authentication objects
        studentAuth = new UsernamePasswordAuthenticationToken("student@example.com", "encodedPassword");
        instructorAuth = new UsernamePasswordAuthenticationToken("instructor@example.com", "encodedPassword");
    }

    @Test
    void findAllByStudent_AsStudent_ValidStudent_ReturnsEnrollments() throws HttpRequestException {
        // Arrange
        Long studentId = 3L;
        User studentUser = mockUsers.get(2); // The student user

        Mockito.when(userService.getUser(studentAuth)).thenReturn(studentUser);
        Mockito.when(enrollmentRepository.findAllByStudent_IdOrderByCourseSection_Term_StartDateDesc(studentId))
                .thenReturn(mockEnrollments);

        // Act
        List<Enrollment> enrollments = enrollmentService.findAllByStudent(studentId, studentAuth);

        // Assert
        assertNotNull(enrollments);
        assertEquals(mockEnrollments.size(), enrollments.size());
        assertEquals(mockEnrollments, enrollments);
    }

    @Test
    void findAllByStudent_AsInstructor_ValidInstructor_ReturnsEnrollments() throws HttpRequestException {
        // Arrange
        Long studentId = 3L;
        User instructorUser = mockUsers.get(1); // The instructor user

        Mockito.when(userService.getUser(instructorAuth)).thenReturn(instructorUser);
        Mockito.when(enrollmentRepository.findAllByStudent_IdOrderByCourseSection_Term_StartDateDesc(studentId))
                .thenReturn(mockEnrollments);

        // Act
        List<Enrollment> enrollments = enrollmentService.findAllByStudent(studentId, instructorAuth);

        // Assert
        assertNotNull(enrollments);
        assertEquals(mockEnrollments.size(), enrollments.size());
        assertEquals(mockEnrollments, enrollments);
    }

    @Test
    void findAllByStudent_Unauthorized_ThrowsHttpRequestException() {
        // Arrange
        Long studentId = 3L;
        User otherStudentUser = mockUsers.get(3); // The other student user

        Mockito.when(userService.getUser(studentAuth)).thenReturn(otherStudentUser);

        // Act & Assert
        HttpRequestException exception = assertThrows(HttpRequestException.class,
            () -> enrollmentService.findAllByStudent(studentId, studentAuth));

        assertEquals(HttpStatus.FORBIDDEN, exception.getHttpStatus());
    }

    @Test
    void create_AsStudent_ValidRequest_ReturnsEnrollment() throws HttpRequestException {
        // Arrange
        Long studentId = 3L;
        Long courseSectionId = 1L;
        User studentUser = mockUsers.get(2); // The student user
        CourseSection courseSection = mockCourseSections.getFirst();

        CreateEnrollmentRequest createRequest = new CreateEnrollmentRequest(courseSectionId);

        Mockito.when(userService.getUser(studentAuth)).thenReturn(studentUser);
        Mockito.when(userService.findById(studentId)).thenReturn(studentUser);
        Mockito.when(courseSectionService.findById(courseSectionId)).thenReturn(courseSection);
        Mockito.when(enrollmentRepository.existsByStudent_IdAndCourseSection_Id(studentId, courseSectionId))
                .thenReturn(false);

        Enrollment expectedEnrollment = Enrollment.builder()
                .studentId(studentId)
                .student(studentUser)
                .courseSectionId(courseSectionId)
                .courseSection(courseSection)
                .grade(new BigDecimal(0))
                .status(EnrollmentStatus.NOT_STARTED.getValue())
                .build();

        Mockito.when(enrollmentRepository.save(any(Enrollment.class))).thenReturn(expectedEnrollment);
        Mockito.when(enrollmentRepository.findAllByStudent_IdOrderByCourseSection_Term_StartDateDesc(studentId))
                .thenReturn(new ArrayList<>());

        // Act
        Enrollment enrollment = enrollmentService.create(studentId, createRequest, studentAuth);

        // Assert
        assertNotNull(enrollment);
        assertEquals(studentId, enrollment.getStudentId());
        assertEquals(courseSectionId, enrollment.getCourseSectionId());
        assertEquals(EnrollmentStatus.NOT_STARTED.getValue(), enrollment.getStatus());
    }

    @Test
    void create_Unauthorized_ThrowsHttpRequestException() {
        // Arrange
        Long studentId = 3L;
        User otherStudentUser = mockUsers.get(3); // The other student user

        CreateEnrollmentRequest createRequest = new CreateEnrollmentRequest(1L);

        Mockito.when(userService.getUser(studentAuth)).thenReturn(otherStudentUser);

        // Act & Assert
        HttpRequestException exception = assertThrows(HttpRequestException.class,
            () -> enrollmentService.create(studentId, createRequest, studentAuth));

        assertEquals(HttpStatus.FORBIDDEN, exception.getHttpStatus());
    }

    @Test
    void update_AsInstructor_ValidRequest_ReturnsUpdatedEnrollment() throws HttpRequestException {
        // Arrange
        Long studentId = 3L;
        Long courseSectionId = 1L;
        User instructorUser = mockUsers.get(1);
        User studentUser = mockUsers.get(2);
        CourseSection courseSection = mockCourseSections.getFirst();

        EnrollmentId enrollmentId = new EnrollmentId(studentId, courseSectionId);
        ManageEnrollmentRequest updateRequest = new ManageEnrollmentRequest(
                new BigDecimal("3.5"), EnrollmentStatus.COMPLETED);

        Enrollment existingEnrollment = Enrollment.builder()
                .studentId(studentId)
                .student(studentUser)
                .courseSectionId(courseSectionId)
                .courseSection(courseSection)
                .grade(new BigDecimal(0))
                .status(EnrollmentStatus.STARTED.getValue())
                .build();

        Mockito.when(userService.getUser(instructorAuth)).thenReturn(instructorUser);
        Mockito.when(enrollmentRepository.findById(enrollmentId)).thenReturn(Optional.of(existingEnrollment));

        Enrollment updatedEnrollment = Enrollment.builder()
                .studentId(studentId)
                .student(studentUser)
                .courseSectionId(courseSectionId)
                .courseSection(courseSection)
                .grade(new BigDecimal("3.5"))
                .status(EnrollmentStatus.COMPLETED.getValue())
                .build();

        Mockito.when(enrollmentRepository.save(any(Enrollment.class))).thenReturn(updatedEnrollment);

        // Act
        Enrollment enrollment = enrollmentService.update(studentId, courseSectionId, updateRequest, instructorAuth);

        // Assert
        assertNotNull(enrollment);
        assertEquals(new BigDecimal("3.5"), enrollment.getGrade());
        assertEquals(EnrollmentStatus.COMPLETED.getValue(), enrollment.getStatus());
    }

    @Test
    void update_Unauthorized_ThrowsHttpRequestException() {
        // Arrange
        Long studentId = 3L;
        Long courseSectionId = 1L;
        User studentUser = mockUsers.get(2);
        User otherStudentUser = mockUsers.get(3);
        CourseSection courseSection = mockCourseSections.getFirst();

        EnrollmentId enrollmentId = new EnrollmentId(studentId, courseSectionId);
        ManageEnrollmentRequest updateRequest = new ManageEnrollmentRequest(
                new BigDecimal("3.5"), EnrollmentStatus.COMPLETED);

        Enrollment existingEnrollment = Enrollment.builder()
                .studentId(studentId)
                .student(studentUser)
                .courseSectionId(courseSectionId)
                .courseSection(courseSection)
                .grade(new BigDecimal(0))
                .status(EnrollmentStatus.STARTED.getValue())
                .build();

        Mockito.when(userService.getUser(studentAuth)).thenReturn(otherStudentUser);
        Mockito.when(enrollmentRepository.findById(enrollmentId)).thenReturn(Optional.of(existingEnrollment));

        // Act & Assert
        HttpRequestException exception = assertThrows(HttpRequestException.class,
            () -> enrollmentService.update(studentId, courseSectionId, updateRequest, studentAuth));

        assertEquals(HttpStatus.UNAUTHORIZED, exception.getHttpStatus());
    }

    @Test
    void delete_AsStudent_ValidRequest_DeletesEnrollment() throws HttpRequestException {
        // Arrange
        Long studentId = 3L;
        Long courseSectionId = 1L;
        User studentUser = mockUsers.get(2);
        CourseSection courseSection = mockCourseSections.getFirst();

        EnrollmentId enrollmentId = new EnrollmentId(studentId, courseSectionId);

        Enrollment existingEnrollment = Enrollment.builder()
                .studentId(studentId)
                .student(studentUser)
                .courseSectionId(courseSectionId)
                .courseSection(courseSection)
                .grade(new BigDecimal(0))
                .status(EnrollmentStatus.NOT_STARTED.getValue())
                .build();

        Mockito.when(userService.getUser(studentAuth)).thenReturn(studentUser);
        Mockito.when(enrollmentRepository.findById(enrollmentId)).thenReturn(Optional.of(existingEnrollment));

        // Act
        enrollmentService.delete(studentId, courseSectionId, studentAuth);

        // Assert
        Mockito.verify(enrollmentRepository, Mockito.times(1)).delete(existingEnrollment);
    }

    @Test
    void delete_NotFound_ThrowsHttpRequestException() {
        // Arrange
        Long studentId = 3L;
        Long courseSectionId = 1L;

        EnrollmentId enrollmentId = new EnrollmentId(studentId, courseSectionId);

        Mockito.when(enrollmentRepository.findById(enrollmentId)).thenReturn(Optional.empty());

        // Act & Assert
        HttpRequestException exception = assertThrows(HttpRequestException.class,
            () -> enrollmentService.delete(studentId, courseSectionId, studentAuth));

        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
    }

    @Test
    void delete_AlreadyCompleted_ThrowsHttpRequestException() {
        // Arrange
        Long studentId = 3L;
        Long courseSectionId = 1L;
        User studentUser = mockUsers.get(2);
        CourseSection courseSection = mockCourseSections.getFirst();

        EnrollmentId enrollmentId = new EnrollmentId(studentId, courseSectionId);

        Enrollment completedEnrollment = Enrollment.builder()
                .studentId(studentId)
                .student(studentUser)
                .courseSectionId(courseSectionId)
                .courseSection(courseSection)
                .grade(new BigDecimal("4.0"))
                .status(EnrollmentStatus.COMPLETED.getValue())
                .build();

        Mockito.when(userService.getUser(studentAuth)).thenReturn(studentUser);
        Mockito.when(enrollmentRepository.findById(enrollmentId)).thenReturn(Optional.of(completedEnrollment));

        // Act & Assert
        HttpRequestException exception = assertThrows(HttpRequestException.class,
            () -> enrollmentService.delete(studentId, courseSectionId, studentAuth));

        assertEquals(HttpStatus.NOT_ACCEPTABLE, exception.getHttpStatus());
    }
}
