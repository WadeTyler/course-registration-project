package net.tylerwade.registrationsystem;

import net.tylerwade.registrationsystem.config.AppProperties;
import net.tylerwade.registrationsystem.config.security.cors.CorsProperties;
import net.tylerwade.registrationsystem.config.security.jwt.JwtProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableConfigurationProperties({AppProperties.class, JwtProperties.class, CorsProperties.class})
@EnableScheduling
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
