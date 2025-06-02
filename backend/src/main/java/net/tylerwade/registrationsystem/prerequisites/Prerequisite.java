package net.tylerwade.registrationsystem.prerequisites;

import jakarta.persistence.*;
import lombok.*;
import net.tylerwade.registrationsystem.course.Course;
import net.tylerwade.registrationsystem.prerequisites.dto.PrerequisiteDTO;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Represents a prerequisite entity in the registration system.
 * Contains details about the course, required course, minimum grade, and creation timestamp.
 */
@Entity
@Table(name = "prerequisites",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"course_id", "required_course_id"})
        }
)
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Prerequisite {

    /**
     * Unique identifier for the prerequisite.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The course that has this prerequisite.
     * Mapped as a many-to-one relationship.
     */
    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    /**
     * The required course for this prerequisite.
     * Mapped as a many-to-one relationship.
     */
    @ManyToOne
    @JoinColumn(name = "required_course_id", nullable = false)
    private Course requiredCourse;

    /**
     * The minimum grade required in the prerequisite course.
     */
    @Column(nullable = false)
    private BigDecimal minimumGrade;

    /**
     * Timestamp indicating when the prerequisite was created.
     * Automatically populated during creation.
     */
    @CreatedDate
    private Date createdAt;

    /**
     * Converts the Prerequisite entity to a PrerequisiteDTO object.
     * Includes details about the course, required course, minimum grade, and creation timestamp.
     *
     * @return A PrerequisiteDTO representation of the prerequisite.
     */
    public PrerequisiteDTO toDTO() {
        return new PrerequisiteDTO(id,
                course.getId(),
                requiredCourse.getId(),
                requiredCourse.getDepartment(),
                requiredCourse.getCode(),
                minimumGrade,
                createdAt);
    }
}