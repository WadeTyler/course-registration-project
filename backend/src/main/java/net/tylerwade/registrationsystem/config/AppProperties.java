package net.tylerwade.registrationsystem.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app")
public record AppProperties(String environment) {

    public boolean isProduction() {
        return environment.equalsIgnoreCase("PRODUCTION");
    }
}
