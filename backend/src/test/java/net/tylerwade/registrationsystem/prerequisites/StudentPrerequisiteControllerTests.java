package net.tylerwade.registrationsystem.prerequisites;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.tylerwade.registrationsystem.auth.AuthTestsUtil;
import net.tylerwade.registrationsystem.config.security.SecurityConfig;
import net.tylerwade.registrationsystem.course.Course;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
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
class StudentPrerequisiteControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PrerequisiteService prerequisiteService;

    private Prerequisite prerequisite;
    private Course course;
    private Course requiredCourse;
    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        course = Course.builder()
                .id(2L)
                .department("CMSC")
                .code("115")
                .title("Data Structures")
                .description("Description")
                .prerequisites(new ArrayList<>())
                .credits(3)
                .createdAt(Instant.now())
                .modifiedAt(Instant.now())
                .build();

        requiredCourse = Course.builder()
                .id(1L)
                .department("CMSC")
                .code("101")
                .title("Intro to CS")
                .description("Description")
                .prerequisites(new ArrayList<>())
                .credits(3)
                .createdAt(Instant.now())
                .modifiedAt(Instant.now())
                .build();

        // Mock Prerequisite and its DTO
        prerequisite = Prerequisite.builder()
                .id(1L)
                .minimumGrade('C')
                .course(course)
                .requiredCourse(requiredCourse)
                .createdAt(Instant.now())
                .modifiedAt(Instant.now())
                .build();
    }

    @Test
    @DisplayName("Should reject unauthenticated users")
    void shouldRejectUnauthenticatedUsers() throws Exception {
        mockMvc.perform(get("/api/student/courses/1/prerequisites")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Should return list of prerequisites for a course")
    @WithMockUser(authorities = {"course:read"})
    void shouldReturnListOfPrerequisites() throws Exception {
        when(prerequisiteService.findAllByCourseId(anyLong()))
                .thenReturn(List.of(prerequisite));

        mockMvc.perform(get("/api/student/courses/1/prerequisites")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isSuccess", is(true)))
                .andExpect(jsonPath("$.message", is("Prerequisites retrieved.")))
                .andExpect(jsonPath("$.data", hasSize(1)))
                .andExpect(jsonPath("$.data[0].id", is(1)))
                .andExpect(jsonPath("$.data[0].requiredCourseDepartment", is("CMSC")))
                .andExpect(jsonPath("$.data[0].minimumGrade", is("C")));
    }

    @Test
    @DisplayName("Should return empty list when no prerequisites exist")
    @WithMockUser(authorities = {"course:read"})
    void shouldReturnEmptyListWhenNoPrerequisitesExist() throws Exception {
        when(prerequisiteService.findAllByCourseId(anyLong()))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/student/courses/1/prerequisites")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isSuccess", is(true)))
                .andExpect(jsonPath("$.data", hasSize(0)));
    }
}
