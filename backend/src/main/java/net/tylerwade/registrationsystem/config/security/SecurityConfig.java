package net.tylerwade.registrationsystem.config.security;

import net.tylerwade.registrationsystem.config.security.jwt.JwtCookieToAuthorizationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;

import static net.tylerwade.registrationsystem.config.security.authorities.UserRole.*;
import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtCookieToAuthorizationFilter jwtCookieToAuthorizationFilter;
    private final JwtAuthenticationConverter jwtAuthenticationConverter;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    @Autowired
    public SecurityConfig(JwtCookieToAuthorizationFilter jwtCookieToAuthorizationFilter, JwtAuthenticationConverter jwtAuthenticationConverter, CustomAccessDeniedHandler customAccessDeniedHandler) {
        this.jwtCookieToAuthorizationFilter = jwtCookieToAuthorizationFilter;
        this.jwtAuthenticationConverter = jwtAuthenticationConverter;
        this.customAccessDeniedHandler = customAccessDeniedHandler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .cors(withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(jwtCookieToAuthorizationFilter, BearerTokenAuthenticationFilter.class)
                .authorizeHttpRequests(auth -> auth
                        // Doc Routes
                        .requestMatchers("/v3/api-docs/**", "/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()

                        // Public Routes
                        .requestMatchers("/api/auth/signup").permitAll()

                        // Student Routes
                        .requestMatchers("/api/student/**").hasAnyRole(STUDENT.name(), INSTRUCTOR.name(), ADMIN.name())

                        // Instructor Routes
                        .requestMatchers("/api/instructor/**").hasAnyRole(ADMIN.name(), INSTRUCTOR.name())

                        // Admin Routes
                        .requestMatchers("/api/admin/**").hasRole(ADMIN.name())

                        // All Routes
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt
                                .jwtAuthenticationConverter(jwtAuthenticationConverter)
                        )
                )
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                        .accessDeniedHandler(customAccessDeniedHandler)
                )
                .httpBasic(basic -> basic
                        .authenticationEntryPoint(new NoPopupBasicAuthEntryPoint())
                )
                .build();
    }

}
