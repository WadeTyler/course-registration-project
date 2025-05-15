package net.tylerwade.registrationsystem.auth;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DefaultAdminInitializer implements CommandLineRunner {
    private final UserService userService;

    public DefaultAdminInitializer(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void run(String... args) throws Exception {
        userService.createDefaultAdmin();
    }
}
