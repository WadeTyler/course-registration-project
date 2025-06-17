package net.tylerwade.registrationsystem.enrollment;

import jakarta.persistence.*;
import lombok.*;
import net.tylerwade.registrationsystem.auth.User;
import net.tylerwade.registrationsystem.coursesection.CourseSection;
import net.tylerwade.registrationsystem.enrollment.dto.EnrollmentDTO;
import net.tylerwade.registrationsystem.enrollment.dto.InstructorEnrollmentDTO;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Represents an enrollment entity in the registration system.
 * Contains details about the enrollment, including the student, course section, grade, status, and creation timestamp.
 */
@Entity
@Table(name = "enrollments", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"course_section_id", "student_id"})
})
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@IdClass(EnrollmentId.class)
public class Enrollment {

    /**
     * The unique identifier for the student in the enrollment.
     */
    @Id
    @Column(name = "student_id")
    private Long studentId;

    /**
     * The unique identifier for the course section in the enrollment.
     */
    @Id
    @Column(name = "course_section_id")
    private Long courseSectionId;

    /**
     * The student associated with this enrollment.
     * Mapped as a lazy-loaded many-to-one relationship.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", referencedColumnName = "id", insertable = false, updatable = false)
    private User student;

    /**
     * The course section associated with this enrollment.
     * Mapped as a lazy-loaded many-to-one relationship.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_section_id", referencedColumnName = "id", insertable = false, updatable = false)
    private CourseSection courseSection;

    /**
     * The grade assigned to the student for this enrollment.
     */
    @Column(nullable = false)
    private BigDecimal grade;

    /**
     * The status of the enrollment (e.g., "active", "completed").
     */
    @Column(nullable = false)
    private String status;

    /**
     * The timestamp indicating when the enrollment was created.
     * Automatically populated during creation.
     */
    @CreatedDate
    private Date createdAt;

    /**
     * Converts the Enrollment entity to an EnrollmentDTO object.
     * Includes details about the student, course section, grade, status, and creation timestamp.
     *
     * @return An EnrollmentDTO representation of the enrollment.
     */
    public EnrollmentDTO toDTO() {
        return new EnrollmentDTO(
                student.toDTO(),
                courseSection.toDTO(),
                grade,
                status,
                createdAt);
    }

    /**
     * Converts the Enrollment entity to an InstructorEnrollmentDTO object.
     * Includes details about the student, course section ID, grade, status, and creation timestamp.
     *
     * @return An InstructorEnrollmentDTO representation of the enrollment.
     */
    public InstructorEnrollmentDTO toInstructorDTO() {
        return new InstructorEnrollmentDTO(
                student.toDTO(),
                courseSection.getId(),
                grade,
                status,
                createdAt
        );
    }

}