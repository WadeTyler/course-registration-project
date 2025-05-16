package net.tylerwade.registrationsystem.course.dto;

import net.tylerwade.registrationsystem.coursesection.dto.CourseSectionDTO;
import net.tylerwade.registrationsystem.prerequisites.dto.PrerequisiteDTO;

import java.time.Instant;
import java.util.List;

public record CourseDTO(
        Long id,
        String department,
        String code,
        String title,
        String description,
        Integer credits,
        List<PrerequisiteDTO> prerequisites,
        List<CourseSectionDTO> courseSections,
        Instant createdAt,
        Instant modifiedAt
) {
}
