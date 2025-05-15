package net.tylerwade.registrationsystem.term;

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

import java.sql.Date;
import java.time.Instant;
import java.time.LocalDate;
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
class StudentTermControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TermService termService;

    private Term term;
    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        // Create test data
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
        // Act & Assert
        mockMvc.perform(get("/api/student/terms")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Should allow users with 'term:read' authority")
    @WithMockUser(authorities = {"term:read"})
    void shouldAllowUsersWithTermReadAuthority() throws Exception {
        // Arrange
        List<Term> terms = Collections.singletonList(term);
        Page<Term> termPage = new PageImpl<>(terms, PageRequest.of(0, 10), terms.size());
        when(termService.findAll(any(Pageable.class))).thenReturn(termPage);

        // Act & Assert
        mockMvc.perform(get("/api/student/terms")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should allow users with 'STUDENT' role")
    @WithMockUser(roles = {"STUDENT"})
    void shouldAllowUsersWithStudentRole() throws Exception {
        // Arrange
        List<Term> terms = Collections.singletonList(term);
        Page<Term> termPage = new PageImpl<>(terms, PageRequest.of(0, 10), terms.size());
        when(termService.findAll(any(Pageable.class))).thenReturn(termPage);

        // Act & Assert
        mockMvc.perform(get("/api/student/terms")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should return page of terms")
    @WithMockUser(authorities = {"term:read"})
    void shouldReturnPageOfTerms() throws Exception {
        // Arrange
        List<Term> terms = Collections.singletonList(term);
        Page<Term> termPage = new PageImpl<>(terms, PageRequest.of(0, 10), terms.size());
        when(termService.findAll(any(Pageable.class))).thenReturn(termPage);

        // Act & Assert
        mockMvc.perform(get("/api/student/terms")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.isSuccess", is(true)))
                .andExpect(jsonPath("$.message", is("Terms retrieved.")))
                .andExpect(jsonPath("$.data.content", hasSize(1)))
                .andExpect(jsonPath("$.data.content[0].id", is(1)))
                .andExpect(jsonPath("$.data.content[0].title", is("Fall 2025")))
                .andExpect(jsonPath("$.data.totalElements", is(1)))
                .andExpect(jsonPath("$.data.pageSize", is(10)));
    }

    @Test
    @DisplayName("Should return term by ID")
    @WithMockUser(authorities = {"term:read"})
    void shouldReturnTermById() throws Exception {
        // Arrange
        when(termService.findById(anyLong())).thenReturn(term);

        // Act & Assert
        mockMvc.perform(get("/api/student/terms/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.isSuccess", is(true)))
                .andExpect(jsonPath("$.message", is("Term retrieved.")))
                .andExpect(jsonPath("$.data.id", is(1)))
                .andExpect(jsonPath("$.data.title", is("Fall 2025")));
    }

    @Test
    @DisplayName("Should return NOT_FOUND when term doesn't exist")
    @WithMockUser(authorities = {"term:read"})
    void shouldReturnNotFoundWhenTermDoesNotExist() throws Exception {
        // Arrange
        when(termService.findById(anyLong()))
                .thenThrow(new HttpRequestException(HttpStatus.NOT_FOUND, "Term not found"));

        // Act & Assert
        mockMvc.perform(get("/api/student/terms/999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.isSuccess", is(false)))
                .andExpect(jsonPath("$.message", is("Term not found")));
    }

    @Test
    @DisplayName("Should return empty page when no terms exist")
    @WithMockUser(authorities = {"term:read"})
    void shouldReturnEmptyPageWhenNoTermsExist() throws Exception {
        // Arrange
        Page<Term> emptyPage = new PageImpl<>(Collections.emptyList(), PageRequest.of(0, 10), 0);
        when(termService.findAll(any(Pageable.class))).thenReturn(emptyPage);

        // Act & Assert
        mockMvc.perform(get("/api/student/terms")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isSuccess", is(true)))
                .andExpect(jsonPath("$.data.content", hasSize(0)))
                .andExpect(jsonPath("$.data.totalElements", is(0)));
    }

    @Test
    @DisplayName("Should reject users without required role or authority")
    void shouldRejectUsersWithoutRequiredRoleOrAuthority() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/student/terms")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }
}