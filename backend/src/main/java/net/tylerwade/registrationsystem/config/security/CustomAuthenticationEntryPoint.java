package net.tylerwade.registrationsystem.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import net.tylerwade.registrationsystem.common.APIResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.lang.model.type.NullType;
import java.io.IOException;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");

        ObjectMapper objectMapper = new ObjectMapper();
        APIResponse<NullType> apiResponse = new APIResponse<>(false, "You are unauthorized to perform that action.", null);

        response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
    }
}
