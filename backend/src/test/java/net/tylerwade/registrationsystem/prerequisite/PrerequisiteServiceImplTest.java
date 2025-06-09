package net.tylerwade.registrationsystem.prerequisite;

import net.tylerwade.registrationsystem.course.Course;
import net.tylerwade.registrationsystem.course.CourseService;
import net.tylerwade.registrationsystem.exception.HttpRequestException;
import net.tylerwade.registrationsystem.prerequisites.Prerequisite;
import net.tylerwade.registrationsystem.prerequisites.PrerequisiteRepository;
import net.tylerwade.registrationsystem.prerequisites.PrerequisiteServiceImpl;
import net.tylerwade.registrationsystem.prerequisites.dto.ManagePrerequisiteRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class PrerequisiteServiceImplTest {

    @Mock
    private CourseService courseService;

    @Mock
    private PrerequisiteRepository prerequisiteRepository;

    @InjectMocks
    private PrerequisiteServiceImpl prerequisiteService;

    private List<Course> mockCourses;
    private List<Prerequisite> mockPrerequisites;

    @BeforeEach
    void setup() {
        Course course1 = Course.builder()
                .id(1L)
                .department("CMSC")
                .code(325)
                .title("Software Principles I")
                .description("Learn about software principles!")
                .credits(3)
                .build();

        Course course2 = Course.builder()
                .id(2L)
                .department("CMSC")
                .code(350)
                .title("Software Principles II")
                .description("Learn about software principles!")
                .credits(3)
                .build();

        Course course3 = Course.builder()
                .id(3L)
                .department("CMSC")
                .code(375)
                .title("Software Principles III")
                .description("Learn about software principles!")
                .credits(3)
                .build();

        mockCourses = List.of(course1, course2, course3);

        Prerequisite prerequisite = Prerequisite.builder()
                .id(1L)
                .course(course2)
                .requiredCourse(course1)
                .minimumGrade(new BigDecimal(80))
                .build();

        mockPrerequisites = List.of(prerequisite);
    }

    @Test
    void create_ExistsByCourseAndRequiredCourse_ThrowsHttpRequestException() {
        // Arrange
        var courseId = mockCourses.getFirst().getId();

        ManagePrerequisiteRequest managePrerequisiteRequest = new ManagePrerequisiteRequest(
                mockCourses.get(1).getId(),
                new BigDecimal(80)
        );

        Mockito.when(prerequisiteRepository.existsByCourse_IdAndRequiredCourse_Id(
                courseId,
                managePrerequisiteRequest.requiredCourseId()
        )).thenReturn(true);

        // Act & Assert
        var exception = Assertions.assertThrows(HttpRequestException.class, () -> prerequisiteService.create(courseId, managePrerequisiteRequest));

        Assertions.assertEquals(HttpStatus.CONFLICT, exception.getHttpStatus());
    }

    @Test
    void create_Valid_ReturnsPrerequisite() throws HttpRequestException {
        // Arrange
        var courseId = mockCourses.get(2).getId();
        var course = mockCourses.get(2);
        var requiredCourse = mockCourses.get(1);

        ManagePrerequisiteRequest managePrerequisiteRequest = new ManagePrerequisiteRequest(
                requiredCourse.getId(),
                new BigDecimal(80)
        );

        Mockito.when(prerequisiteRepository.existsByCourse_IdAndRequiredCourse_Id(
                courseId,
                managePrerequisiteRequest.requiredCourseId()
        )).thenReturn(false);

        Mockito.when(courseService.findById(courseId)).thenReturn(course);
        Mockito.when(courseService.findById(managePrerequisiteRequest.requiredCourseId())).thenReturn(requiredCourse);

        var expected = Prerequisite.builder()
                .id(2L)
                .course(course)
                .requiredCourse(requiredCourse)
                .minimumGrade(managePrerequisiteRequest.minimumGrade())
                .build();

        Mockito.when(prerequisiteRepository.save(any())).thenReturn(expected);

        // Act & Assert
        var prerequisite = prerequisiteService.create(courseId, managePrerequisiteRequest);

        Assertions.assertNotNull(prerequisite);
        Assertions.assertEquals(course.getId(), prerequisite.getCourse().getId());
        Assertions.assertEquals(requiredCourse.getId(), prerequisite.getRequiredCourse().getId());
        Assertions.assertEquals(managePrerequisiteRequest.minimumGrade(), prerequisite.getMinimumGrade());
    }

    @Test
    void update_NotFound_ThrowsHttpRequestException() {
        // Arrange

        var prerequisiteId = 9L;

        ManagePrerequisiteRequest managePrerequisiteRequest = new ManagePrerequisiteRequest(
                mockCourses.get(1).getId(),
                new BigDecimal(80)
        );

        Mockito.when(prerequisiteRepository.findById(prerequisiteId)).thenReturn(Optional.empty());

        // Act & Assert
        var exception = Assertions.assertThrows(HttpRequestException.class, () -> prerequisiteService.update(prerequisiteId, managePrerequisiteRequest));

        Assertions.assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
    }

    @Test
    void update_Found_ThrowsHttpRequestException() throws HttpRequestException {
        // Arrange

        var prerequisiteId = 1L;

        ManagePrerequisiteRequest managePrerequisiteRequest = new ManagePrerequisiteRequest(
                mockCourses.get(1).getId(),
                new BigDecimal(65)
        );

        Mockito.when(prerequisiteRepository.findById(prerequisiteId)).thenReturn(Optional.of(mockPrerequisites.getFirst()));

        var expected = Prerequisite.builder()
                .id(prerequisiteId)
                .course(mockPrerequisites.getFirst().getCourse())
                .requiredCourse(mockPrerequisites.getFirst().getRequiredCourse())
                .minimumGrade(managePrerequisiteRequest.minimumGrade())
                .build();

        Mockito.when(prerequisiteRepository.save(any())).thenReturn(expected);

        // Act & Assert
        var updatedPrerequisite = prerequisiteService.update(prerequisiteId, managePrerequisiteRequest);

        Assertions.assertNotNull(updatedPrerequisite);
        Assertions.assertEquals(updatedPrerequisite.getId(), expected.getId());
        Assertions.assertEquals(updatedPrerequisite.getMinimumGrade(), managePrerequisiteRequest.minimumGrade());
    }

    @Test
    void delete_NonExistentPrerequisite_ThrowsHttpRequestException() {
        // Arrange
        Long nonExistentPrerequisiteId = 999L;

        Mockito.when(prerequisiteRepository.existsById(nonExistentPrerequisiteId)).thenReturn(false);

        // Act & Assert
        HttpRequestException exception = Assertions.assertThrows(HttpRequestException.class, () ->
                prerequisiteService.delete(nonExistentPrerequisiteId)
        );

        Assertions.assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
    }

    @Test
    void delete_ExistingPrerequisite_Succeeds() throws HttpRequestException {
        // Arrange
        Long existingPrerequisiteId = 1L;

        Mockito.when(prerequisiteRepository.existsById(existingPrerequisiteId)).thenReturn(true);
        Mockito.doNothing().when(prerequisiteRepository).deleteById(existingPrerequisiteId);

        // Act
        prerequisiteService.delete(existingPrerequisiteId);

        // Assert
        Mockito.verify(prerequisiteRepository).deleteById(existingPrerequisiteId);
    }


}
