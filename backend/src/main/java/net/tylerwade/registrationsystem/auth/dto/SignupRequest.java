package net.tylerwade.registrationsystem.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SignupRequest(
        @NotBlank(message = "Email is required.")
        @Size(min = 3, max = 255, message = "Email must be between 3 - 255 characters.")
        @Email(message = "Email must be valid.")
        String username,

        @NotBlank(message = "Fist name is required.")
        @Size(min = 3, max = 50, message = "First name must be between 3 - 50 characters.")
        String firstName,

        @NotBlank(message = "Last name is required.")
        @Size(min = 3, max = 50, message = "Last name must be between 3 - 50 characters.")
        String lastName,

        @NotBlank(message = "Password is required.")
        @Size(min = 6, max = 100, message = "Password must be between 6 - 100 characters.")
        String password,

        @NotBlank(message = "Confirm Password is required.")
        @Size(min = 6, max = 100, message = "Confirm Password must be between 6 - 100 characters.")
        String confirmPassword
) {
}
