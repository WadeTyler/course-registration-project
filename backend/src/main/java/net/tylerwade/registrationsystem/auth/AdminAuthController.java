package net.tylerwade.registrationsystem.auth;

import net.tylerwade.registrationsystem.auth.dto.UpdateUserRequest;
import net.tylerwade.registrationsystem.auth.dto.UserDTO;
import net.tylerwade.registrationsystem.common.APIResponse;
import net.tylerwade.registrationsystem.common.PageResponse;
import net.tylerwade.registrationsystem.exception.HttpRequestException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/auth")
public class AdminAuthController {

    private final UserService userService;

    public AdminAuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<?> getAllUsers(Pageable pageable,
                                         @RequestParam(required = false) String search) {
        Page<UserDTO> page = userService.findAll(pageable, search).map(User::toDTO);
        PageResponse<UserDTO> pageResponse = new PageResponse<>(page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages());
        return ResponseEntity.status(HttpStatus.OK).body(new APIResponse<>(true, "Users retrieved.", pageResponse));
    }

    @PutMapping("/{userId}")
    public ResponseEntity<?> updateUser(Authentication authentication, @PathVariable String userId, @RequestBody UpdateUserRequest updateUserRequest) throws HttpRequestException {
        UserDTO updatedUser = userService.updateUserAsAdmin(userId, updateUserRequest, authentication).toDTO();
        return ResponseEntity.status(HttpStatus.OK).body(new APIResponse<>(true, "User updated.", updatedUser));
    }
}
