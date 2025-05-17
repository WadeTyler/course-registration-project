package net.tylerwade.registrationsystem.enrollment;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.tylerwade.registrationsystem.auth.AuthTestsUtil;
import net.tylerwade.registrationsystem.config.security.SecurityConfig;
import net.tylerwade.registrationsystem.enrollment.dto.EnrollmentDTO;
import net.tylerwade.registrationsystem.enrollment.dto.ManageEnrollmentRequest;
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

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Import({SecurityConfig.class, AuthTestsUtil.class})
@ActiveProfiles("test")
class InstructorEnrollmentControllerTests {

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
    @DisplayName("Should update enrollment as instructor")
    @WithMockUser(authorities = {"ROLE_INSTRUCTOR"})
    void shouldUpdateEnrollment() throws Exception {
        ManageEnrollmentRequest req = new ManageEnrollmentRequest(90, "STARTED");
        when(enrollmentService.update(eq(1L), any(ManageEnrollmentRequest.class), any(Authentication.class)))
                .thenReturn(enrollment);

        when(enrollment.toDTO()).thenReturn(enrollmentDTO);

        mockMvc.perform(put("/api/instructor/enrollments/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isSuccess").value(true));
    }

    @Test
    @DisplayName("Should reject unauthenticated users")
    void shouldRejectUnauthenticatedUsers() throws Exception {
        ManageEnrollmentRequest req = new ManageEnrollmentRequest(90, "STARTED");
        mockMvc.perform(put("/api/instructor/enrollments/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isUnauthorized());
    }
}
