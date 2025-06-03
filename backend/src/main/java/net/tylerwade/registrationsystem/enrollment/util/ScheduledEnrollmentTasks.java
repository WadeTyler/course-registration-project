package net.tylerwade.registrationsystem.enrollment.util;

import lombok.extern.slf4j.Slf4j;
import net.tylerwade.registrationsystem.enrollment.EnrollmentService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ScheduledEnrollmentTasks {

    private final EnrollmentService enrollmentService;

    public ScheduledEnrollmentTasks(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    @Scheduled(fixedRate = 43200000)
    public void updateEnrollmentStatuses() {
        int totalUpdatedCount = enrollmentService.updateStartedEnrollments();
        log.info(totalUpdatedCount + " enrollments set to STARTED.");
    }

}
