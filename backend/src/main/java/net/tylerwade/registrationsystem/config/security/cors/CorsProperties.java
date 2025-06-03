package net.tylerwade.registrationsystem.config.security.cors;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "cors")
public record CorsProperties(String clientUrl) {
}
