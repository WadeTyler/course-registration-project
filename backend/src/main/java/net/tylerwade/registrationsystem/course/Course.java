package net.tylerwade.registrationsystem.course;

import jakarta.persistence.*;
import lombok.*;
import net.tylerwade.registrationsystem.course.dto.CourseAttributeDTO;
import net.tylerwade.registrationsystem.course.dto.CourseDTO;
import net.tylerwade.registrationsystem.coursesection.CourseSection;
import net.tylerwade.registrationsystem.prerequisites.Prerequisite;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Represents a course entity in the registration system.
 * Contains details about the course, including its department, code, title, description, credits,
 * prerequisites, and associated course sections.
 */
@Entity
@Table(
        name = "courses",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"department", "code"})
        }
)
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Course {

    /**
     * Unique identifier for the course.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The department offering the course (e.g., "CS").
     * Limited to 10 characters.
     */
    @Column(nullable = false, length = 10)
    private String department;

    /**
     * The course code (e.g., 101).
     */
    @Column(nullable = false)
    private Integer code;

    /**
     * The title of the course.
     * Must be unique.
     */
    @Column(nullable = false, unique = true)
    private String title;

    /**
     * A detailed description of the course.
     * Limited to 4000 characters.
     */
    @Column(nullable = false, length = 4000)
    private String description;

    /**
     * The number of credits assigned to the course.
     */
    @Column(nullable = false)
    private Integer credits;

    /**
     * List of prerequisites required for the course.
     * Mapped by the "course" field in the Prerequisite entity.
     * Fetched eagerly and removed when orphaned.
     */
    @OneToMany(mappedBy = "course", orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Prerequisite> prerequisites = new ArrayList<>();

    /**
     * List of course sections associated with the course.
     * Mapped by the "course" field in the CourseSection entity.
     * Fetched eagerly and removed when orphaned.
     */
    @OneToMany(mappedBy = "course", orphanRemoval = true, fetch = FetchType.EAGER)
    private List<CourseSection> courseSections = new ArrayList<>();

    /**
     * Timestamp indicating when the course was created.
     * Automatically populated during creation.
     */
    @CreatedDate
    private Date createdAt;

    /**
     * Converts the Course entity to a CourseDTO object.
     * Includes details about prerequisites and course sections.
     *
     * @return A CourseDTO representation of the course.
     */
    public CourseDTO toDTO() {
        return new CourseDTO(id,
                department,
                code,
                title,
                description,
                credits,
                prerequisites.stream().map(Prerequisite::toDTO).toList(),
                courseSections.stream().map(CourseSection::toDTO).toList(),
                createdAt);
    }

    /**
     * Converts the Course entity to a CourseAttributeDTO object.
     * Excludes details about prerequisites and course sections.
     *
     * @return A CourseAttributeDTO representation of the course.
     */
    public CourseAttributeDTO toAttributeDTO() {
        return new CourseAttributeDTO(
                id,
                department,
                code,
                title,
                description,
                credits
        );
    }

}