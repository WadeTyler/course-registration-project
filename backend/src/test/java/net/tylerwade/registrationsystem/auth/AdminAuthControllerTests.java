package net.tylerwade.registrationsystem.auth;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.tylerwade.registrationsystem.auth.dto.UpdateUserRequest;
import net.tylerwade.registrationsystem.auth.dto.UserDTO;
import net.tylerwade.registrationsystem.common.APIResponse;
import net.tylerwade.registrationsystem.config.security.SecurityConfig;
import net.tylerwade.registrationsystem.config.security.authorities.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Import(SecurityConfig.class)
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
public class AdminAuthControllerTests {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private UserService service;

    @MockitoBean
    private PasswordEncoder passwordEncoder;

    List<User> mockUsers;

    @BeforeEach
    void loadMockUsers() {
        User user1 = new User("johndoe@email.com", "John", "Doe", passwordEncoder.encode("123456"), UserRole.STUDENT.getGrantedAuthoritiesString());
        user1.setId(UUID.randomUUID().toString());
        user1.setCreatedAt(new Timestamp(System.currentTimeMillis()));

        User user2 = new User("janedoe@email.com", "Jane", "Doe", passwordEncoder.encode("123456"), UserRole.INSTRUCTOR.getGrantedAuthoritiesString());
        user2.setId(UUID.randomUUID().toString());
        user2.setCreatedAt(new Timestamp(System.currentTimeMillis()));

        User user3 = new User("jacobsmith@email.com", "Jacob", "Smith", passwordEncoder.encode("123456"), UserRole.ADMIN.getGrantedAuthoritiesString());
        user3.setId(UUID.randomUUID().toString());
        user3.setCreatedAt(new Timestamp(System.currentTimeMillis()));

        mockUsers = List.of(user1, user2, user3);
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

        when(service.updateUserAsAdmin(any(), any(), any()))
                .thenReturn(updatedUser);

        String expectedResponseBody = objectMapper.writeValueAsString(
                new APIResponse<>(true, "User updated.", updatedUser.toDTO())
        );

        MvcResult result = mvc.perform(put("/api/admin/auth/" + targetUser.getId())
                        .with(user("jacobsmith@email.com").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateUserRequest))
                )
                .andExpect(status().isOk())
                .andReturn();

        String json = result.getResponse().getContentAsString();
        APIResponse<UserDTO> actualResponse = objectMapper.readValue(json, new TypeReference<APIResponse<UserDTO>>() {});

        assertTrue(actualResponse.isSuccess());
        assertEquals(actualResponse.data().id(), targetUser.getId());
        assertEquals(actualResponse.data().grantedAuthorities(), updatedUser.toDTO().grantedAuthorities());
    }
}