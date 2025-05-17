package net.tylerwade.registrationsystem.prerequisites;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.tylerwade.registrationsystem.auth.AuthTestsUtil;
import net.tylerwade.registrationsystem.config.security.SecurityConfig;
import net.tylerwade.registrationsystem.course.Course;
import net.tylerwade.registrationsystem.exception.HttpRequestException;
import net.tylerwade.registrationsystem.prerequisites.dto.ManagePrerequisiteRequest;
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
class AdminPrerequisiteControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PrerequisiteService prerequisiteService;

    private Prerequisite prerequisite;
    private Course course;
    private Course requiredCourse;
    private final ObjectMapper objectMapper = new ObjectMapper();

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

        prerequisite = Prerequisite.builder()
                .id(1L)
                .minimumGrade(70)
                .course(course)
                .requiredCourse(requiredCourse)
                .createdAt(Instant.now())
                .modifiedAt(Instant.now())
                .build();
    }

    @Test
    @DisplayName("Should reject unauthenticated users")
    void shouldRejectUnauthenticatedUsers() throws Exception {
        ManagePrerequisiteRequest req = new ManagePrerequisiteRequest(1L, 70);
        mockMvc.perform(post("/api/admin/courses/2/prerequisites")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Should allow users with 'course:write' and 'ROLE_ADMIN' authority to create prerequisite")
    @WithMockUser(authorities = {"course:write", "ROLE_ADMIN"})
    void shouldAllowUsersWithCourseWriteAuthorityToCreatePrerequisite() throws Exception {
        ManagePrerequisiteRequest req = new ManagePrerequisiteRequest(1L, 70);
        when(prerequisiteService.create(anyLong(), any(ManagePrerequisiteRequest.class))).thenReturn(prerequisite);

        mockMvc.perform(post("/api/admin/courses/2/prerequisites")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.isSuccess", is(true)))
                .andExpect(jsonPath("$.message", is("Prerequisite created.")))
                .andExpect(jsonPath("$.data.id", is(1)))
                .andExpect(jsonPath("$.data.requiredCourseDepartment", is("CMSC")))
                .andExpect(jsonPath("$.data.minimumGrade", is(70)));
    }

    @Test
    @DisplayName("Should return BAD_REQUEST when create fails validation")
    @WithMockUser(authorities = {"course:write", "ROLE_ADMIN"})
    void shouldReturnBadRequestWhenCreateFailsValidation() throws Exception {
        ManagePrerequisiteRequest req = new ManagePrerequisiteRequest(null, null);
        when(prerequisiteService.create(anyLong(), any(ManagePrerequisiteRequest.class)))
                .thenThrow(new HttpRequestException(HttpStatus.BAD_REQUEST, "Fields not valid."));

        mockMvc.perform(post("/api/admin/courses/2/prerequisites")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.isSuccess", is(false)));
    }

    @Test
    @DisplayName("Should allow users with 'course:write' and 'ROLE_ADMIN' authority to update prerequisite")
    @WithMockUser(authorities = {"course:write", "ROLE_ADMIN"})
    void shouldAllowUsersWithCourseWriteAuthorityToUpdatePrerequisite() throws Exception {
        ManagePrerequisiteRequest req = new ManagePrerequisiteRequest(1L, 80);
        Prerequisite updatedPrerequisite = Prerequisite.builder()
                .id(1L)
                .minimumGrade(80)
                .course(course)
                .requiredCourse(requiredCourse)
                .createdAt(Instant.now())
                .modifiedAt(Instant.now())
                .build();
        when(prerequisiteService.update(anyLong(), anyLong(), any(ManagePrerequisiteRequest.class))).thenReturn(updatedPrerequisite);

        mockMvc.perform(put("/api/admin/courses/2/prerequisites/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.isSuccess", is(true)))
                .andExpect(jsonPath("$.message", is("Prerequisite updated.")))
                .andExpect(jsonPath("$.data.id", is(1)))
                .andExpect(jsonPath("$.data.minimumGrade", is(80)));
    }

    @Test
    @DisplayName("Should return NOT_FOUND when updating non-existent prerequisite")
    @WithMockUser(authorities = {"course:write", "ROLE_ADMIN"})
    void shouldReturnNotFoundWhenUpdatingNonExistentPrerequisite() throws Exception {
        ManagePrerequisiteRequest req = new ManagePrerequisiteRequest(1L, 70);
        when(prerequisiteService.update(anyLong(), anyLong(), any(ManagePrerequisiteRequest.class)))
                .thenThrow(new HttpRequestException(HttpStatus.NOT_FOUND, "Prerequisite not found."));

        mockMvc.perform(put("/api/admin/courses/2/prerequisites/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.isSuccess", is(false)))
                .andExpect(jsonPath("$.message", is("Prerequisite not found.")));
    }

    @Test
    @DisplayName("Should allow users with 'course:write' and 'ROLE_ADMIN' authority to delete prerequisite")
    @WithMockUser(authorities = {"course:write", "ROLE_ADMIN"})
    void shouldAllowUsersWithCourseWriteAuthorityToDeletePrerequisite() throws Exception {
        doNothing().when(prerequisiteService).delete(anyLong());

        mockMvc.perform(delete("/api/admin/courses/2/prerequisites/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.isSuccess", is(true)))
                .andExpect(jsonPath("$.message", is("Prerequisite deleted.")))
                .andExpect(jsonPath("$.data").doesNotExist());
    }

    @Test
    @DisplayName("Should return NOT_FOUND when deleting non-existent prerequisite")
    @WithMockUser(authorities = {"course:write", "ROLE_ADMIN"})
    void shouldReturnNotFoundWhenDeletingNonExistentPrerequisite() throws Exception {
        doNothing().when(prerequisiteService).delete(anyLong());
        org.mockito.Mockito.doThrow(new HttpRequestException(HttpStatus.NOT_FOUND, "Prerequisite not found."))
                .when(prerequisiteService).delete(999L);

        mockMvc.perform(delete("/api/admin/courses/2/prerequisites/999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.isSuccess", is(false)))
                .andExpect(jsonPath("$.message", is("Prerequisite not found.")));
    }

    @Test
    @DisplayName("Should reject users without required authority")
    void shouldRejectUsersWithoutRequiredAuthority() throws Exception {
        ManagePrerequisiteRequest req = new ManagePrerequisiteRequest(1L, 70);
        mockMvc.perform(post("/api/admin/courses/2/prerequisites")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isUnauthorized());
    }
}
