package net.tylerwade.registrationsystem.coursesection;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.tylerwade.registrationsystem.auth.AuthTestsUtil;
import net.tylerwade.registrationsystem.config.security.SecurityConfig;
import net.tylerwade.registrationsystem.course.Course;
import net.tylerwade.registrationsystem.coursesection.dto.ManageCourseSectionRequest;
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
class AdminCourseSectionControllerTests {

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
        ManageCourseSectionRequest req = new ManageCourseSectionRequest(1L, null, "Room 101", 30, "MWF 10:00-11:00");
        mockMvc.perform(post("/api/admin/courses/1/sections")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Should allow users with 'section:write' and 'ROLE_ADMIN' authority to create section")
    @WithMockUser(authorities = {"section:write", "ROLE_ADMIN"})
    void shouldAllowUsersWithSectionWriteAuthorityToCreateSection() throws Exception {
        ManageCourseSectionRequest req = new ManageCourseSectionRequest(1L, null, "Room 101", 30, "MWF 10:00-11:00");
        when(courseSectionService.create(anyLong(), any(ManageCourseSectionRequest.class))).thenReturn(section);

        mockMvc.perform(post("/api/admin/courses/1/sections")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.isSuccess", is(true)))
                .andExpect(jsonPath("$.message", is("Course Section created.")))
                .andExpect(jsonPath("$.data.id", is(1)))
                .andExpect(jsonPath("$.data.room", is("Room 101")));
    }

    @Test
    @DisplayName("Should return BAD_REQUEST when create fails validation")
    @WithMockUser(authorities = {"section:write", "ROLE_ADMIN"})
    void shouldReturnBadRequestWhenCreateFailsValidation() throws Exception {
        ManageCourseSectionRequest req = new ManageCourseSectionRequest(null, null, "", null, "");
        when(courseSectionService.create(anyLong(), any(ManageCourseSectionRequest.class)))
                .thenThrow(new HttpRequestException(HttpStatus.BAD_REQUEST, "Fields not valid."));

        mockMvc.perform(post("/api/admin/courses/1/sections")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.isSuccess", is(false)));
    }

    @Test
    @DisplayName("Should allow users with 'section:write' and 'ROLE_ADMIN' authority to update section")
    @WithMockUser(authorities = {"section:write", "ROLE_ADMIN"})
    void shouldAllowUsersWithSectionWriteAuthorityToUpdateSection() throws Exception {
        ManageCourseSectionRequest req = new ManageCourseSectionRequest(1L, null, "Room 102", 40, "TTh 12:00-13:30");
        CourseSection updatedSection = CourseSection.builder()
                .id(1L)
                .term(term)
                .room("Room 102")
                .capacity(40)
                .schedule("TTh 12:00-13:30")
                .course(course)
                .createdAt(Instant.now())
                .enrollments(new ArrayList<>())
                .modifiedAt(Instant.now())
                .build();
        when(courseSectionService.update(anyLong(), any(ManageCourseSectionRequest.class))).thenReturn(updatedSection);

        mockMvc.perform(put("/api/admin/courses/1/sections/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isSuccess", is(true)))
                .andExpect(jsonPath("$.message", is("Course Section updated.")))
                .andExpect(jsonPath("$.data.id", is(1)))
                .andExpect(jsonPath("$.data.room", is("Room 102")));
    }

    @Test
    @DisplayName("Should return NOT_FOUND when updating non-existent section")
    @WithMockUser(authorities = {"section:write", "ROLE_ADMIN"})
    void shouldReturnNotFoundWhenUpdatingNonExistentSection() throws Exception {
        ManageCourseSectionRequest req = new ManageCourseSectionRequest(1L, null, "Room 101", 30, "MWF 10:00-11:00");
        when(courseSectionService.update(anyLong(), any(ManageCourseSectionRequest.class)))
                .thenThrow(new HttpRequestException(HttpStatus.NOT_FOUND, "Course section not found."));

        mockMvc.perform(put("/api/admin/courses/1/sections/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.isSuccess", is(false)))
                .andExpect(jsonPath("$.message", is("Course section not found.")));
    }

    @Test
    @DisplayName("Should allow users with 'section:write' and 'ROLE_ADMIN' authority to delete section")
    @WithMockUser(authorities = {"section:write", "ROLE_ADMIN"})
    void shouldAllowUsersWithSectionWriteAuthorityToDeleteSection() throws Exception {
        doNothing().when(courseSectionService).delete(anyLong());

        mockMvc.perform(delete("/api/admin/courses/1/sections/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isSuccess", is(true)))
                .andExpect(jsonPath("$.message", is("Course section deleted.")))
                .andExpect(jsonPath("$.data").doesNotExist());
    }

    @Test
    @DisplayName("Should return NOT_FOUND when deleting non-existent section")
    @WithMockUser(authorities = {"section:write", "ROLE_ADMIN"})
    void shouldReturnNotFoundWhenDeletingNonExistentSection() throws Exception {
        doNothing().when(courseSectionService).delete(anyLong());
        org.mockito.Mockito.doThrow(new HttpRequestException(HttpStatus.NOT_FOUND, "Course section not found."))
                .when(courseSectionService).delete(999L);

        mockMvc.perform(delete("/api/admin/courses/1/sections/999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.isSuccess", is(false)))
                .andExpect(jsonPath("$.message", is("Course section not found.")));
    }

    @Test
    @DisplayName("Should reject users without required authority")
    void shouldRejectUsersWithoutRequiredAuthority() throws Exception {
        ManageCourseSectionRequest req = new ManageCourseSectionRequest(1L, null, "Room 101", 30, "MWF 10:00-11:00");
        mockMvc.perform(post("/api/admin/courses/1/sections")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isUnauthorized());
    }
}
