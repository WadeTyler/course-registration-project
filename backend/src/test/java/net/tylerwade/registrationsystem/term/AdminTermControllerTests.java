package net.tylerwade.registrationsystem.term;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.tylerwade.registrationsystem.auth.AuthTestsUtil;
import net.tylerwade.registrationsystem.config.security.SecurityConfig;
import net.tylerwade.registrationsystem.exception.HttpRequestException;
import net.tylerwade.registrationsystem.term.dto.ManageTermRequest;
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
class AdminTermControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TermService termService;

    private Term term;
    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        LocalDate today = LocalDate.now();
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
    }

    @Test
    @DisplayName("Should reject unauthenticated users")
    void shouldRejectUnauthenticatedUsers() throws Exception {
        ManageTermRequest req = new ManageTermRequest("Fall 2025", term.getStartDate(), term.getEndDate(), term.getRegistrationStart(), term.getRegistrationEnd());
        mockMvc.perform(post("/api/admin/terms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Should allow users with 'term:write' and 'ROLE_ADMIN' authority to create term")
    @WithMockUser(authorities = {"term:write", "ROLE_ADMIN"})
    void shouldAllowUsersWithTermWriteAuthorityToCreateTerm() throws Exception {
        ManageTermRequest req = new ManageTermRequest("Fall 2025", term.getStartDate(), term.getEndDate(), term.getRegistrationStart(), term.getRegistrationEnd());
        when(termService.create(any(ManageTermRequest.class))).thenReturn(term);

        mockMvc.perform(post("/api/admin/terms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.isSuccess", is(true)))
                .andExpect(jsonPath("$.message", is("Term created.")))
                .andExpect(jsonPath("$.data.id", is(1)))
                .andExpect(jsonPath("$.data.title", is("Fall 2025")));
    }

    @Test
    @DisplayName("Should return BAD_REQUEST when create fails validation")
    @WithMockUser(authorities = {"term:write", "ROLE_ADMIN"})
    void shouldReturnBadRequestWhenCreateFailsValidation() throws Exception {
        ManageTermRequest req = new ManageTermRequest("", term.getStartDate(), term.getEndDate(), term.getRegistrationStart(), term.getRegistrationEnd());
        when(termService.create(any(ManageTermRequest.class)))
                .thenThrow(new HttpRequestException(HttpStatus.BAD_REQUEST, "Fields not valid. End Dates must be after Start Dates and Registration must end be before start date."));

        mockMvc.perform(post("/api/admin/terms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.isSuccess", is(false)));
    }

    @Test
    @DisplayName("Should allow users with 'term:write' and 'ROLE_ADMIN' authority to update term")
    @WithMockUser(authorities = {"term:write", "ROLE_ADMIN"})
    void shouldAllowUsersWithTermWriteAuthorityToUpdateTerm() throws Exception {
        ManageTermRequest req = new ManageTermRequest("Fall 2025", term.getStartDate(), term.getEndDate(), term.getRegistrationStart(), term.getRegistrationEnd());
        when(termService.update(anyLong(), any(ManageTermRequest.class))).thenReturn(term);

        mockMvc.perform(put("/api/admin/terms/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isSuccess", is(true)))
                .andExpect(jsonPath("$.message", is("Term updated.")))
                .andExpect(jsonPath("$.data.id", is(1)))
                .andExpect(jsonPath("$.data.title", is("Fall 2025")));
    }

    @Test
    @DisplayName("Should return NOT_FOUND when updating non-existent term")
    @WithMockUser(authorities = {"term:write", "ROLE_ADMIN"})
    void shouldReturnNotFoundWhenUpdatingNonExistentTerm() throws Exception {
        ManageTermRequest req = new ManageTermRequest("Fall 2025", term.getStartDate(), term.getEndDate(), term.getRegistrationStart(), term.getRegistrationEnd());
        when(termService.update(anyLong(), any(ManageTermRequest.class)))
                .thenThrow(new HttpRequestException(HttpStatus.NOT_FOUND, "Term not found."));

        mockMvc.perform(put("/api/admin/terms/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.isSuccess", is(false)))
                .andExpect(jsonPath("$.message", is("Term not found.")));
    }

    @Test
    @DisplayName("Should allow users with 'term:write' and 'ROLE_ADMIN' authority to delete term")
    @WithMockUser(authorities = {"term:write", "ROLE_ADMIN"})
    void shouldAllowUsersWithTermWriteAuthorityToDeleteTerm() throws Exception {
        doNothing().when(termService).delete(anyLong());

        mockMvc.perform(delete("/api/admin/terms/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isSuccess", is(true)))
                .andExpect(jsonPath("$.message", is("Term deleted.")))
                .andExpect(jsonPath("$.data").doesNotExist());
    }

    @Test
    @DisplayName("Should return NOT_FOUND when deleting non-existent term")
    @WithMockUser(authorities = {"term:write", "ROLE_ADMIN"})
    void shouldReturnNotFoundWhenDeletingNonExistentTerm() throws Exception {
        doNothing().when(termService).delete(anyLong());
        org.mockito.Mockito.doThrow(new HttpRequestException(HttpStatus.NOT_FOUND, "Term not found."))
                .when(termService).delete(999L);

        mockMvc.perform(delete("/api/admin/terms/999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.isSuccess", is(false)))
                .andExpect(jsonPath("$.message", is("Term not found.")));
    }

    @Test
    @DisplayName("Should reject users without required authority")
    void shouldRejectUsersWithoutRequiredAuthority() throws Exception {
        ManageTermRequest req = new ManageTermRequest("Fall 2025", term.getStartDate(), term.getEndDate(), term.getRegistrationStart(), term.getRegistrationEnd());
        mockMvc.perform(post("/api/admin/terms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isUnauthorized());
    }
}
