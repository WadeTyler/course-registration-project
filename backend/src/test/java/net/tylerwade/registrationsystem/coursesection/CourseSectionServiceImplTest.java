package net.tylerwade.registrationsystem.coursesection;

import net.tylerwade.registrationsystem.auth.User;
import net.tylerwade.registrationsystem.auth.UserService;
import net.tylerwade.registrationsystem.auth.authority.Authority;
import net.tylerwade.registrationsystem.course.Course;
import net.tylerwade.registrationsystem.course.CourseService;
import net.tylerwade.registrationsystem.coursesection.dto.ManageCourseSectionRequest;
import net.tylerwade.registrationsystem.exception.HttpRequestException;
import net.tylerwade.registrationsystem.term.Term;
import net.tylerwade.registrationsystem.term.TermService;
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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class CourseSectionServiceImplTest {

    @Mock
    private CourseSectionRepository courseSectionRepository;

    @Mock
    private UserService userService;

    @Mock
    private TermService termService;

    @Mock
    private CourseService courseService;

    @InjectMocks
    private CourseSectionServiceImpl courseSectionService;

    private List<Course> mockCourses;
    private List<User> mockUsers;
    private List<Term> mockTerms;
    private List<CourseSection> mockSections;

    private Authority mockInstructorAuthority;
    private Authority mockStudentAuthority;

    @BeforeEach
    void setup() {
        // Mock Terms
        Term mockTerm1 = Term.builder()
                .id(1L)
                .registrationStart(LocalDate.now())
                .registrationEnd(LocalDate.now().plusWeeks(1))
                .startDate(LocalDate.now().plusWeeks(1).plusDays(1))
                .endDate(LocalDate.now().plusWeeks(9))
                .build();

        Term mockTerm2 = Term.builder()
                .id(1L)
                .registrationStart(LocalDate.now())
                .registrationEnd(LocalDate.now().plusWeeks(1))
                .startDate(LocalDate.now().plusWeeks(3).plusDays(1))
                .endDate(LocalDate.now().plusWeeks(9))
                .build();

        mockTerms = List.of(mockTerm1, mockTerm2);

        // Mock Users
        // Setup common test data
        mockInstructorAuthority = Authority.builder()
                .id(1)
                .name("INSTRUCTOR")
                .build();

         mockStudentAuthority = Authority.builder()
                .id(1)
                .name("STUDENT")
                .build();

        var mockInstructor = User.builder()
                .id(1L)
                .username("test@example.com")
                .firstName("Test")
                .lastName("User")
                .password("encodedPassword")
                .userAuthorities(Set.of(mockInstructorAuthority))
                .build();

        var mockStudent = User.builder()
                .id(1L)
                .username("test2@example.com")
                .firstName("Test")
                .lastName("User")
                .password("encodedPassword")
                .userAuthorities(Set.of(mockStudentAuthority))
                .build();

        mockUsers = List.of(mockInstructor, mockStudent);

        // Mock Courses
        Course course1 = Course.builder()
                .id(1L)
                .department("CMSC")
                .code(325)
                .title("Software Principles I")
                .description("Learn about software principles!")
                .credits(3)
                .build();

        mockCourses = List.of(course1);


        // Mock Course Sections
        CourseSection mockSection1 = CourseSection.builder()
                .id(1L)
                .course(course1)
                .term(mockTerm1)
                .instructor(mockInstructor)
                .enrollments(new ArrayList<>())
                .enrolledCount(0)
                .room("Remote")
                .capacity(30)
                .schedule("M - F 12:00 AM - 1:30 PM")
                .build();

        mockSections = List.of(mockSection1);
    }

    @Test
    void findAssignedCourseSections_AsInstructor_ValidInstructor_ReturnsCourseSections() {
        Authentication authentication = new UsernamePasswordAuthenticationToken("test@example.com", "encodedpassword", null);

        Mockito.when(userService.getUser(authentication)).thenReturn(mockUsers.getFirst());

        var expectedResult = List.of(mockSections.getFirst());

        Mockito.when(courseSectionRepository.findAllByInstructor_Id(mockUsers.getFirst().getId())).thenReturn(expectedResult);

        // Act
        List<CourseSection> courseSections = courseSectionService.findAssignedCourseSections_AsInstructor(authentication);

        assertEquals(expectedResult, courseSections);
    }

    @Test
    void create_TargetInstructorNotPermitted_ThrowsHttpRequestException() throws HttpRequestException {
        // Arrange
        var courseId = 1L;

        ManageCourseSectionRequest manageCourseSectionRequest = new ManageCourseSectionRequest(
                1L,
                2L,
                "Remote",
                33,
                "W-F 12:00 AM"
        );

        var term = mockTerms.getFirst();
        var course = mockCourses.getFirst();
        var targetInstructor = mockUsers.getLast();

        Mockito.when(termService.findById(1L)).thenReturn(term);
        Mockito.when(courseService.findById(1L)).thenReturn(course);
        Mockito.when(userService.findById(2L)).thenReturn(targetInstructor);

        // Act & Assert
        HttpRequestException exception = assertThrows(HttpRequestException.class, () -> courseSectionService.create(courseId, manageCourseSectionRequest));

        assertEquals(HttpStatus.NOT_ACCEPTABLE, exception.getHttpStatus());
    }

    @Test
    void create_Valid_ReturnsNewCourseSection() throws HttpRequestException {
        // Arrange
        var courseId = 1L;

        ManageCourseSectionRequest manageCourseSectionRequest = new ManageCourseSectionRequest(
                1L,
                1L,
                "Remote",
                33,
                "W-F 12:00 AM"
        );

        var term = mockTerms.getFirst();
        var course = mockCourses.getFirst();
        var targetInstructor = mockUsers.getFirst();

        Mockito.when(termService.findById(1L)).thenReturn(term);
        Mockito.when(courseService.findById(1L)).thenReturn(course);
        Mockito.when(userService.findById(1L)).thenReturn(targetInstructor);

        CourseSection expectedResult = CourseSection.builder()
                .course(course)
                .term(term)
                .instructor(targetInstructor)
                .room(manageCourseSectionRequest.room())
                .capacity(manageCourseSectionRequest.capacity())
                .schedule(manageCourseSectionRequest.schedule())
                .enrolledCount(0)
                .build();

        Mockito.when(courseSectionRepository.save(any())).thenReturn(expectedResult);

        // Act & Assert
        CourseSection courseSection = courseSectionService.create(courseId, manageCourseSectionRequest);

        assertNotNull(courseSection);
        assertEquals(courseId, courseSection.getCourse().getId());
        assertEquals(targetInstructor.getId(), courseSection.getInstructor().getId());
        assertEquals(term.getId(), courseSection.getTerm().getId());
    }

    @Test
    void update_TargetInstructorNotPermitted_ThrowsHttpRequestException() throws HttpRequestException {
        // Arrange
        var courseSectionId = 1L;

        ManageCourseSectionRequest manageCourseSectionRequest = new ManageCourseSectionRequest(
                1L,
                2L,
                "Remote",
                33,
                "W-F 12:00 AM"
        );

        var term = mockTerms.getFirst();
        var targetInstructor = mockUsers.getLast();

        Mockito.when(courseSectionRepository.findById(courseSectionId)).thenReturn(Optional.ofNullable(mockSections.getFirst()));
        Mockito.when(termService.findById(1L)).thenReturn(term);
        Mockito.when(userService.findById(2L)).thenReturn(targetInstructor);

        // Act & Assert
        HttpRequestException exception = assertThrows(HttpRequestException.class, () -> courseSectionService.update(courseSectionId, manageCourseSectionRequest));

        assertEquals(HttpStatus.NOT_ACCEPTABLE, exception.getHttpStatus());
    }

    @Test
    void update_Valid_ReturnsNewCourseSection() throws HttpRequestException {
        // Arrange
        var courseSectionId = 1L;

        ManageCourseSectionRequest manageCourseSectionRequest = new ManageCourseSectionRequest(
                1L,
                1L,
                "Remote",
                33,
                "W-F 12:00 AM"
        );

        var term = mockTerms.getFirst();
        var course = mockCourses.getFirst();
        var targetInstructor = mockUsers.getFirst();

        Mockito.when(courseSectionRepository.findById(courseSectionId)).thenReturn(Optional.ofNullable(mockSections.getFirst()));
        Mockito.when(termService.findById(1L)).thenReturn(term);
        Mockito.when(userService.findById(1L)).thenReturn(targetInstructor);

        CourseSection expectedResult = CourseSection.builder()
                .course(course)
                .term(term)
                .instructor(targetInstructor)
                .room(manageCourseSectionRequest.room())
                .capacity(manageCourseSectionRequest.capacity())
                .schedule(manageCourseSectionRequest.schedule())
                .enrolledCount(0)
                .build();

        Mockito.when(courseSectionRepository.save(any())).thenReturn(expectedResult);

        // Act & Assert
        CourseSection courseSection = courseSectionService.update(courseSectionId, manageCourseSectionRequest);

        assertNotNull(courseSection);
        assertEquals(course.getId(), courseSection.getCourse().getId());
        assertEquals(targetInstructor.getId(), courseSection.getInstructor().getId());
        assertEquals(term.getId(), courseSection.getTerm().getId());
    }

    @Test
    void delete_NotFound_ThrowsHttpRequestException() {
        // Arrange
        var courseSectionId = 4L;
        Mockito.when(courseSectionRepository.existsById(courseSectionId)).thenReturn(false);

        // Act & Assert
        HttpRequestException exception = assertThrows(HttpRequestException.class, () -> courseSectionService.delete(courseSectionId));

        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
    }

    @Test
    void delete_Found_Executes() throws HttpRequestException {
        // Arrange
        var courseSectionId = 4L;
        Mockito.when(courseSectionRepository.existsById(courseSectionId)).thenReturn(true);

        // Act & Assert
        courseSectionService.delete(courseSectionId);

        Mockito.verify(courseSectionRepository, Mockito.times(1)).deleteById(courseSectionId);
    }
}


