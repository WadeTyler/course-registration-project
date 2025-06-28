package net.tylerwade.registrationsystem.auth;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DefaultAccountsInitializer implements CommandLineRunner {
    private final UserService userService;

    public DefaultAccountsInitializer(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void run(String... args) throws Exception {
        userService.createDefaultAdmin();
        userService.createDefaultInstructor();
        userService.createDefaultStudent();
    }
}
