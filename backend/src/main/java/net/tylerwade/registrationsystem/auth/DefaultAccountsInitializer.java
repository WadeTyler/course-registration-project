package net.tylerwade.registrationsystem.auth;

import net.tylerwade.registrationsystem.config.AppProperties;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DefaultAccountsInitializer implements CommandLineRunner {
    private final UserService userService;
    private final AppProperties appProperties;

    public DefaultAccountsInitializer(UserService userService, AppProperties appProperties) {
        this.userService = userService;
        this.appProperties = appProperties;
    }

    @Override
    public void run(String... args) throws Exception {
        if (!appProperties.isProduction()) {
            userService.createDefaultAdmin();
            userService.createDefaultInstructor();
            userService.createDefaultStudent();
        }
    }
}
