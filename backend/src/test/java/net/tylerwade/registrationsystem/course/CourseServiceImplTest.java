package net.tylerwade.registrationsystem.course;

import net.tylerwade.registrationsystem.course.dto.ManageCourseRequest;
import net.tylerwade.registrationsystem.exception.HttpRequestException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class CourseServiceImplTest {

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private CourseServiceImpl courseService;

    private List<Course> mockCourses;

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

        mockCourses = List.of(course1);
    }

    @Test
    void create_ExistsByDepartmentAndCode_ThrowsHttpRequestException() {
        // Arrange
        ManageCourseRequest manageCourseRequest = new ManageCourseRequest(
                mockCourses.getFirst().getDepartment(),
                mockCourses.getFirst().getCode(),
                mockCourses.getFirst().getTitle(),
                mockCourses.getFirst().getDescription(),
                mockCourses.getFirst().getCredits()
        );

        Mockito.when(courseRepository.existsByDepartmentIgnoreCaseAndCode(manageCourseRequest.department(), manageCourseRequest.code())).thenReturn(true);

        // Act & Assert
        HttpRequestException exception = Assertions.assertThrows(HttpRequestException.class, () -> courseService.create(manageCourseRequest));

        Assertions.assertEquals(HttpStatus.CONFLICT, exception.getHttpStatus());
    }

    @Test
    void create_ExistsByTitle_ThrowsHttpRequestException() {
        // Arrange
        ManageCourseRequest manageCourseRequest = new ManageCourseRequest(
                "CMSC",
                350,
                mockCourses.getFirst().getTitle(),
                mockCourses.getFirst().getDescription(),
                mockCourses.getFirst().getCredits()
        );

        Mockito.when(courseRepository.existsByTitleIgnoreCase(manageCourseRequest.title())).thenReturn(true);

        // Act & Assert
        HttpRequestException exception = Assertions.assertThrows(HttpRequestException.class, () -> courseService.create(manageCourseRequest));

        Assertions.assertEquals(HttpStatus.CONFLICT, exception.getHttpStatus());
    }

    @Test
    void create_Valid_ReturnsNewCourse() throws HttpRequestException {
        // Arrange
        ManageCourseRequest manageCourseRequest = new ManageCourseRequest(
                "CMSC",
                375,
                "Software Principles III",
                "Learn about software principles",
                3
        );

        Mockito.when(courseRepository.existsByDepartmentIgnoreCaseAndCode(manageCourseRequest.department(), manageCourseRequest.code())).thenReturn(false);

        Mockito.when(courseRepository.existsByTitleIgnoreCase(manageCourseRequest.title())).thenReturn(false);

        var mockCourse = Course.builder()
                .department(manageCourseRequest.department())
                .code(manageCourseRequest.code())
                .title(manageCourseRequest.title())
                .description(manageCourseRequest.description())
                .credits(manageCourseRequest.credits())
                .prerequisites(new ArrayList<>())
                .courseSections(new ArrayList<>())
                .build();

        Mockito.when(courseRepository.save(any())).thenReturn(mockCourse);

        // Act & Assert
        Course updatedCourse = courseService.create(manageCourseRequest);

        Assertions.assertNotNull(updatedCourse);
        Assertions.assertEquals(manageCourseRequest.code(), updatedCourse.getCode());
        Assertions.assertEquals(manageCourseRequest.department(), updatedCourse.getDepartment());
        Assertions.assertEquals(manageCourseRequest.title(), updatedCourse.getTitle());
        Assertions.assertEquals(manageCourseRequest.description(), updatedCourse.getDescription());
        Assertions.assertEquals(manageCourseRequest.credits(), updatedCourse.getCredits());
    }

    @Test
    void update_ExistsByDepartmentAndCode_ThrowsHttpRequestException() {
        // Arrange
        Long courseId = mockCourses.getFirst().getId();

        ManageCourseRequest manageCourseRequest = new ManageCourseRequest(
                mockCourses.getFirst().getDepartment(),
                mockCourses.getFirst().getCode(),
                mockCourses.getFirst().getTitle(),
                mockCourses.getFirst().getDescription(),
                mockCourses.getFirst().getCredits()
        );
        Mockito.when(courseRepository.findById(courseId)).thenReturn(Optional.ofNullable(mockCourses.getFirst()));

        Mockito.when(courseRepository.existsByDepartmentIgnoreCaseAndCodeAndIdNot(manageCourseRequest.department(), manageCourseRequest.code(), courseId)).thenReturn(true);

        // Act & Assert
        HttpRequestException exception = Assertions.assertThrows(HttpRequestException.class, () -> courseService.update(courseId, manageCourseRequest));

        Assertions.assertEquals(HttpStatus.CONFLICT, exception.getHttpStatus());
    }


    @Test
    void update_ExistsByTitle_ThrowsHttpRequestException() {
        // Arrange
        Long courseId = mockCourses.getFirst().getId();

        ManageCourseRequest manageCourseRequest = new ManageCourseRequest(
                "CMSC",
                350,
                mockCourses.getFirst().getTitle(),
                mockCourses.getFirst().getDescription(),
                mockCourses.getFirst().getCredits()
        );

        Mockito.when(courseRepository.findById(courseId)).thenReturn(Optional.ofNullable(mockCourses.getFirst()));

        Mockito.when(courseRepository.existsByDepartmentIgnoreCaseAndCodeAndIdNot(manageCourseRequest.department(), manageCourseRequest.code(), courseId)).thenReturn(false);

        Mockito.when(courseRepository.existsByTitleIgnoreCaseAndIdNot(manageCourseRequest.title(), courseId)).thenReturn(true);

        // Act & Assert
        HttpRequestException exception = Assertions.assertThrows(HttpRequestException.class, () -> courseService.update(courseId, manageCourseRequest));

        Assertions.assertEquals(HttpStatus.CONFLICT, exception.getHttpStatus());
    }

    @Test
    void delete_NotFound_ThrowsHttpRequestException() {
        // Arrange
        Long courseId = 5L;

        Mockito.when(courseRepository.existsById(courseId)).thenReturn(false);

        // Act
        var exception = Assertions.assertThrows(HttpRequestException.class, () -> courseService.delete(courseId));

        // Assert
        Assertions.assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
    }

    @Test
    void delete_Found_DeletesCourse() throws HttpRequestException {
        // Arrange
        Long courseId = 1L;

        Mockito.when(courseRepository.existsById(courseId)).thenReturn(true);

        // Act
        courseService.delete(courseId);

        // Assert
        verify(courseRepository, times(1)).deleteById(courseId);
    }

}
