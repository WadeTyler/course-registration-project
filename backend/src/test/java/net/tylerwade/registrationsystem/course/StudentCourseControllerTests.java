package net.tylerwade.registrationsystem.course;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.tylerwade.registrationsystem.auth.AuthTestsUtil;
import net.tylerwade.registrationsystem.config.security.SecurityConfig;
import net.tylerwade.registrationsystem.exception.HttpRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Import({SecurityConfig.class, AuthTestsUtil.class})
@ActiveProfiles("test")
class StudentCourseControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CourseService courseService;

    private Course course;
    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        course = Course.builder()
                .id(1L)
                .department("CMSC")
                .code("101")
                .title("Intro to CS")
                .description("Description")
                .credits(3)
                .createdAt(Instant.now())
                .modifiedAt(Instant.now())
                .build();
    }

    @Test
    @DisplayName("Should reject unauthenticated users")
    void shouldRejectUnauthenticatedUsers() throws Exception {
        mockMvc.perform(get("/api/student/courses")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Should return page of courses")
    @WithMockUser(authorities = {"course:read"})
    void shouldReturnPageOfCourses() throws Exception {
        List<Course> courses = Collections.singletonList(course);
        Page<Course> coursePage = new PageImpl<>(courses, PageRequest.of(0, 10), courses.size());
        when(courseService.findAll(any(Pageable.class))).thenReturn(coursePage);

        mockMvc.perform(get("/api/student/courses")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.isSuccess", is(true)))
                .andExpect(jsonPath("$.message", is("Courses retrieved.")))
                .andExpect(jsonPath("$.data.content", hasSize(1)))
                .andExpect(jsonPath("$.data.content[0].id", is(1)))
                .andExpect(jsonPath("$.data.content[0].title", is("Intro to CS")))
                .andExpect(jsonPath("$.data.totalElements", is(1)))
                .andExpect(jsonPath("$.data.pageSize", is(10)));
    }

    @Test
    @DisplayName("Should return course by ID")
    @WithMockUser(authorities = {"course:read"})
    void shouldReturnCourseById() throws Exception {
        when(courseService.findById(anyLong())).thenReturn(course);

        mockMvc.perform(get("/api/student/courses/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.isSuccess", is(true)))
                .andExpect(jsonPath("$.message", is("Course retrieved.")))
                .andExpect(jsonPath("$.data.id", is(1)))
                .andExpect(jsonPath("$.data.title", is("Intro to CS")));
    }

    @Test
    @DisplayName("Should return NOT_FOUND when course doesn't exist")
    @WithMockUser(authorities = {"course:read"})
    void shouldReturnNotFoundWhenCourseDoesNotExist() throws Exception {
        when(courseService.findById(anyLong()))
                .thenThrow(new HttpRequestException(HttpStatus.NOT_FOUND, "Course not found"));

        mockMvc.perform(get("/api/student/courses/999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.isSuccess", is(false)))
                .andExpect(jsonPath("$.message", is("Course not found")));
    }

    @Test
    @DisplayName("Should return empty page when no courses exist")
    @WithMockUser(authorities = {"course:read"})
    void shouldReturnEmptyPageWhenNoCoursesExist() throws Exception {
        Page<Course> emptyPage = new PageImpl<>(Collections.emptyList(), PageRequest.of(0, 10), 0);
        when(courseService.findAll(any(Pageable.class))).thenReturn(emptyPage);

        mockMvc.perform(get("/api/student/courses")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isSuccess", is(true)))
                .andExpect(jsonPath("$.data.content", hasSize(0)))
                .andExpect(jsonPath("$.data.totalElements", is(0)));
    }
}
