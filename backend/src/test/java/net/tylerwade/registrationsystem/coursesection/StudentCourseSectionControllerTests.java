package net.tylerwade.registrationsystem.coursesection;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.tylerwade.registrationsystem.auth.AuthTestsUtil;
import net.tylerwade.registrationsystem.config.security.SecurityConfig;
import net.tylerwade.registrationsystem.course.Course;
import net.tylerwade.registrationsystem.exception.HttpRequestException;
import net.tylerwade.registrationsystem.term.Term;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Date;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Import({SecurityConfig.class, AuthTestsUtil.class})
@ActiveProfiles("test")
class StudentCourseSectionControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CourseSectionService courseSectionService;

    private CourseSection section;
    private Term term;
    private Course course;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        LocalDate today = LocalDate.now();
        course = Course.builder()
                .id(1L)
                .department("CMSC")
                .code("101")
                .title("Intro to CS")
                .description("Description")
                .credits(3)
                .prerequisites(new ArrayList<>())
                .courseSections(new ArrayList<>())
                .createdAt(Instant.now())
                .modifiedAt(Instant.now())
                .build();

        term = Term.builder()
                .id(1L)
                .title("Fall 2025")
                .startDate(Date.valueOf(today.plusMonths(3)))
                .endDate(Date.valueOf(today.plusMonths(6)))
                .registrationStart(Date.valueOf(today.plusMonths(1)))
                .registrationEnd(Date.valueOf(today.plusMonths(2)))
                .createdAt(Instant.now())
                .modifiedAt(Instant.now())
                .build();

        section = CourseSection.builder()
                .id(1L)
                .term(term)
                .room("Room 101")
                .capacity(30)
                .schedule("MWF 10:00-11:00")
                .course(course)
                .enrollments(new ArrayList<>())
                .createdAt(Instant.now())
                .modifiedAt(Instant.now())
                .build();
    }

    @Test
    @DisplayName("Should reject unauthenticated users")
    void shouldRejectUnauthenticatedUsers() throws Exception {
        mockMvc.perform(get("/api/student/courses/1/sections")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Should return list of course sections for a course")
    @WithMockUser(authorities = {"ROLE_STUDENT"})
    void shouldReturnListOfCourseSections() throws Exception {
        when(courseSectionService.findAllByCourse_Id(anyLong()))
                .thenReturn(List.of(section));

        mockMvc.perform(get("/api/student/courses/1/sections")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isSuccess", is(true)))
                .andExpect(jsonPath("$.message", is("Course Sections retrieved.")))
                .andExpect(jsonPath("$.data", hasSize(1)))
                .andExpect(jsonPath("$.data[0].id", is(1)))
                .andExpect(jsonPath("$.data[0].room", is("Room 101")))
                .andExpect(jsonPath("$.data[0].capacity", is(30)));
    }

    @Test
    @DisplayName("Should return empty list when no course sections exist")
    @WithMockUser(authorities = {"ROLE_STUDENT"})
    void shouldReturnEmptyListWhenNoCourseSectionsExist() throws Exception {
        when(courseSectionService.findAllByCourse_Id(anyLong()))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/student/courses/1/sections")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isSuccess", is(true)))
                .andExpect(jsonPath("$.data", hasSize(0)));
    }

    @Test
    @DisplayName("Should return course section by ID")
    @WithMockUser(authorities = {"ROLE_STUDENT"})
    void shouldReturnCourseSectionById() throws Exception {
        when(courseSectionService.findById(anyLong()))
                .thenReturn(section);

        mockMvc.perform(get("/api/student/courses/1/sections/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isSuccess", is(true)))
                .andExpect(jsonPath("$.message", is("Course section retrieved.")))
                .andExpect(jsonPath("$.data.id", is(1)))
                .andExpect(jsonPath("$.data.room", is("Room 101")));
    }

    @Test
    @DisplayName("Should return NOT_FOUND when course section doesn't exist")
    @WithMockUser(authorities = {"ROLE_STUDENT"})
    void shouldReturnNotFoundWhenCourseSectionDoesNotExist() throws Exception {
        when(courseSectionService.findById(anyLong()))
                .thenThrow(new HttpRequestException(HttpStatus.NOT_FOUND, "Course section not found"));

        mockMvc.perform(get("/api/student/courses/1/sections/999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.isSuccess", is(false)))
                .andExpect(jsonPath("$.message", is("Course section not found")));
    }

    @Test
    @DisplayName("Should reject users without required role or authority")
    void shouldRejectUsersWithoutRequiredRoleOrAuthority() throws Exception {
        mockMvc.perform(get("/api/student/courses/1/sections")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }
}
