package net.tylerwade.registrationsystem.course;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.tylerwade.registrationsystem.auth.AuthTestsUtil;
import net.tylerwade.registrationsystem.config.security.SecurityConfig;
import net.tylerwade.registrationsystem.course.dto.ManageCourseRequest;
import net.tylerwade.registrationsystem.exception.HttpRequestException;
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

import java.time.Instant;
import java.util.ArrayList;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Import({SecurityConfig.class, AuthTestsUtil.class})
@ActiveProfiles("test")
class AdminCourseControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CourseService courseService;

    private Course course;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        course = Course.builder()
                .id(1L)
                .department("CMSC")
                .code("101")
                .title("Intro to CS")
                .description("Description")
                .prerequisites(new ArrayList<>())
                .courseSections(new ArrayList<>())
                .credits(3)
                .createdAt(Instant.now())
                .modifiedAt(Instant.now())
                .build();
    }

    @Test
    @DisplayName("Should reject unauthenticated users")
    void shouldRejectUnauthenticatedUsers() throws Exception {
        ManageCourseRequest req = new ManageCourseRequest("CMSC", "101", "Intro to CS", "Description", 3);
        mockMvc.perform(post("/api/admin/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Should allow users with 'course:write' and 'ROLE_ADMIN' authority to create course")
    @WithMockUser(authorities = {"course:write", "ROLE_ADMIN"})
    void shouldAllowUsersWithCourseWriteAuthorityToCreateCourse() throws Exception {
        ManageCourseRequest req = new ManageCourseRequest("CMSC", "101", "Intro to CS", "Description", 3);
        when(courseService.create(any(ManageCourseRequest.class))).thenReturn(course);

        mockMvc.perform(post("/api/admin/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.isSuccess", is(true)))
                .andExpect(jsonPath("$.message", is("Course Created.")))
                .andExpect(jsonPath("$.data.id", is(1)))
                .andExpect(jsonPath("$.data.title", is("Intro to CS")));
    }

    @Test
    @DisplayName("Should return BAD_REQUEST when create fails validation")
    @WithMockUser(authorities = {"course:write", "ROLE_ADMIN"})
    void shouldReturnBadRequestWhenCreateFailsValidation() throws Exception {
        ManageCourseRequest req = new ManageCourseRequest("", "101", "Intro to CS", "Description", 3);
        when(courseService.create(any(ManageCourseRequest.class)))
                .thenThrow(new HttpRequestException(HttpStatus.BAD_REQUEST, "Fields not valid."));

        mockMvc.perform(post("/api/admin/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.isSuccess", is(false)));
    }

    @Test
    @DisplayName("Should allow users with 'course:write' and 'ROLE_ADMIN' authority to update course")
    @WithMockUser(authorities = {"course:write", "ROLE_ADMIN"})
    void shouldAllowUsersWithCourseWriteAuthorityToUpdateCourse() throws Exception {
        ManageCourseRequest req = new ManageCourseRequest("CMSC", "101", "Intro to CS", "Description", 3);
        when(courseService.update(anyLong(), any(ManageCourseRequest.class))).thenReturn(course);

        mockMvc.perform(put("/api/admin/courses/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isSuccess", is(true)))
                .andExpect(jsonPath("$.message", is("Course updated.")))
                .andExpect(jsonPath("$.data.id", is(1)))
                .andExpect(jsonPath("$.data.title", is("Intro to CS")));
    }

    @Test
    @DisplayName("Should return NOT_FOUND when updating non-existent course")
    @WithMockUser(authorities = {"course:write", "ROLE_ADMIN"})
    void shouldReturnNotFoundWhenUpdatingNonExistentCourse() throws Exception {
        ManageCourseRequest req = new ManageCourseRequest("CMSC", "101", "Intro to CS", "Description", 3);
        when(courseService.update(anyLong(), any(ManageCourseRequest.class)))
                .thenThrow(new HttpRequestException(HttpStatus.NOT_FOUND, "Course not found."));

        mockMvc.perform(put("/api/admin/courses/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.isSuccess", is(false)))
                .andExpect(jsonPath("$.message", is("Course not found.")));
    }

    @Test
    @DisplayName("Should allow users with 'course:write' and 'ROLE_ADMIN' authority to delete course")
    @WithMockUser(authorities = {"course:write", "ROLE_ADMIN"})
    void shouldAllowUsersWithCourseWriteAuthorityToDeleteCourse() throws Exception {
        doNothing().when(courseService).delete(anyLong());

        mockMvc.perform(delete("/api/admin/courses/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isSuccess", is(true)))
                .andExpect(jsonPath("$.message", is("Course deleted.")))
                .andExpect(jsonPath("$.data").doesNotExist());
    }

    @Test
    @DisplayName("Should return NOT_FOUND when deleting non-existent course")
    @WithMockUser(authorities = {"course:write", "ROLE_ADMIN"})
    void shouldReturnNotFoundWhenDeletingNonExistentCourse() throws Exception {
        doNothing().when(courseService).delete(anyLong());
        org.mockito.Mockito.doThrow(new HttpRequestException(HttpStatus.NOT_FOUND, "Course not found."))
                .when(courseService).delete(999L);

        mockMvc.perform(delete("/api/admin/courses/999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.isSuccess", is(false)))
                .andExpect(jsonPath("$.message", is("Course not found.")));
    }

    @Test
    @DisplayName("Should reject users without required authority")
    void shouldRejectUsersWithoutRequiredAuthority() throws Exception {
        ManageCourseRequest req = new ManageCourseRequest("CMSC", "101", "Intro to CS", "Description", 3);
        mockMvc.perform(post("/api/admin/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isUnauthorized());
    }
}
