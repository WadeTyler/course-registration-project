package net.tylerwade.registrationsystem.enrollment;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.tylerwade.registrationsystem.auth.AuthTestsUtil;
import net.tylerwade.registrationsystem.config.security.SecurityConfig;
import net.tylerwade.registrationsystem.enrollment.dto.CreateEnrollmentRequest;
import net.tylerwade.registrationsystem.enrollment.dto.EnrollmentDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Import({SecurityConfig.class, AuthTestsUtil.class})
@ActiveProfiles("test")
class StudentEnrollmentControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EnrollmentService enrollmentService;

    @MockitoBean
    private Authentication authentication;

    private Enrollment enrollment;
    private EnrollmentDTO enrollmentDTO;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        enrollment = mock(Enrollment.class);
        enrollmentDTO = mock(EnrollmentDTO.class);
        when(enrollment.toDTO()).thenReturn(enrollmentDTO);
    }

    @Test
    @DisplayName("Should return all enrollments for student")
    @WithMockUser(authorities = {"ROLE_STUDENT"})
    void shouldReturnAllEnrollments() throws Exception {
        when(enrollmentService.findAllByStudent(any(Authentication.class)))
                .thenReturn(List.of(enrollment));

        when(enrollment.toDTO()).thenReturn(enrollmentDTO);

        mockMvc.perform(get("/api/student/enrollments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isSuccess").value(true));
    }

    @Test
    @DisplayName("Should create enrollment")
    @WithMockUser(authorities = {"ROLE_STUDENT"})
    void shouldCreateEnrollment() throws Exception {
        CreateEnrollmentRequest req = new CreateEnrollmentRequest(1L);
        when(enrollmentService.create(any(CreateEnrollmentRequest.class), any(Authentication.class)))
                .thenReturn(enrollment);

        when(enrollment.toDTO()).thenReturn(enrollmentDTO);

        mockMvc.perform(post("/api/student/enrollments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.isSuccess").value(true));
    }

    @Test
    @DisplayName("Should delete enrollment")
    @WithMockUser(authorities = {"ROLE_STUDENT"})
    void shouldDeleteEnrollment() throws Exception {
        doNothing().when(enrollmentService).delete(eq(1L), any(Authentication.class));

        mockMvc.perform(delete("/api/student/enrollments/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isSuccess").value(true));
    }

    @Test
    @DisplayName("Should reject unauthenticated users")
    void shouldRejectUnauthenticatedUsers() throws Exception {
        mockMvc.perform(get("/api/student/enrollments"))
                .andExpect(status().isUnauthorized());
    }
}
