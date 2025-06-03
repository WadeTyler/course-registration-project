package net.tylerwade.registrationsystem.auth.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import net.tylerwade.registrationsystem.auth.dto.SignupRequest;

public class PasswordsMatchValidator implements ConstraintValidator<PasswordsMatch, SignupRequest> {

    @Override
    public boolean isValid(SignupRequest signupRequest, ConstraintValidatorContext constraintValidatorContext) {
        return signupRequest.password() != null && signupRequest.password().equals(signupRequest.confirmPassword());
    }
}
