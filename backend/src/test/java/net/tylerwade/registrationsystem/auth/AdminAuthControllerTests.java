package net.tylerwade.registrationsystem.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.tylerwade.registrationsystem.auth.dto.UpdateUserRequest;
import net.tylerwade.registrationsystem.config.security.SecurityConfig;
import net.tylerwade.registrationsystem.config.security.authorities.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Import({SecurityConfig.class, AuthTestsUtil.class})
@ActiveProfiles("test")
public class AdminAuthControllerTests {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private UserService service;

    @MockitoBean
    private PasswordEncoder passwordEncoder;

    @Autowired
    AuthTestsUtil authTestsUtil;

    List<User> mockUsers;

    @BeforeEach
    void loadMockUsers() {
        this.mockUsers = authTestsUtil.createMockUsers();
    }


    @Test
    public void getAllUsers_shouldBeForbidden_whenNotAdmin() throws Exception {

        Page<User> page = new PageImpl<>(mockUsers);
        when(service.findAll(any(), any())).thenReturn(page);

        mvc.perform(get("/api/admin/auth").with(
                user("johndoe@email.com").roles("STUDENT")
        )).andExpect(status().isForbidden());
    }

    @Test
    public void getAllUsers_shouldReturnUsers_whenAdmin() throws Exception {
        Page<User> page = new PageImpl<>(mockUsers);
        when(service.findAll(any(), any())).thenReturn(page);

        mvc.perform(get("/api/admin/auth").with(
                user("johndoe@email.com").roles("ADMIN")
        )).andExpect(status().isOk());
    }


    @Test
    public void updateUser_shouldReturnForbidden_whenNotAdmin() throws Exception {
        var updateUserRequest = new UpdateUserRequest(UserRole.INSTRUCTOR);
        var targetUser = mockUsers.getFirst();

        User updatedUser = new User(targetUser.getUsername(),
                targetUser.getFirstName(),
                targetUser.getLastName(),
                targetUser.getPassword(),
                targetUser.getGrantedAuthorities());
        updatedUser.setId(targetUser.getId());
        updatedUser.setCreatedAt(targetUser.getCreatedAt());

        when(service.updateUserAsAdmin(any(), any(), any()))
                .thenReturn(updatedUser);

        mvc.perform(put("/api/admin/auth/" + targetUser.getId())
                        .with(user("johndoe@gmail.com").roles("STUDENT"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateUserRequest))
                )
                .andExpect(status().isForbidden());
    }

    @Test
    public void updateUser_shouldReturnUpdatedUser_whenAdmin() throws Exception {
        var updateUserRequest = new UpdateUserRequest(UserRole.INSTRUCTOR);
        var targetUser = mockUsers.getFirst();

        User updatedUser = new User(targetUser.getUsername(),
                targetUser.getFirstName(),
                targetUser.getLastName(),
                targetUser.getPassword(),
                UserRole.INSTRUCTOR.getGrantedAuthoritiesString());
        updatedUser.setId(targetUser.getId());
        updatedUser.setCreatedAt(targetUser.getCreatedAt());
        updatedUser.setModifiedAt(targetUser.getModifiedAt());

        when(service.updateUserAsAdmin(any(), any(), any()))
                .thenReturn(updatedUser);

        mvc.perform(put("/api/admin/auth/" + targetUser.getId())
                        .with(user("jacobsmith@email.com").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateUserRequest))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isSuccess").value(true))
                .andExpect(jsonPath("$.data.id").value(targetUser.getId()));
    }
}