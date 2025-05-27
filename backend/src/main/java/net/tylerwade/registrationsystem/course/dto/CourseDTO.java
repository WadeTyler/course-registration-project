package net.tylerwade.registrationsystem.course.dto;

import net.tylerwade.registrationsystem.coursesection.dto.CourseSectionDTO;
import net.tylerwade.registrationsystem.prerequisites.dto.PrerequisiteDTO;

import java.util.Date;
import java.util.List;

public record CourseDTO(
        Long id,
        String department,
        Integer code,
        String title,
        String description,
        Integer credits,
        List<PrerequisiteDTO> prerequisites,
        List<CourseSectionDTO> courseSections,
        Date createdAt
) {
}
