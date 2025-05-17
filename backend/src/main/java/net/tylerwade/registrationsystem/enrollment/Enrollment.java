package net.tylerwade.registrationsystem.enrollment;

import jakarta.persistence.*;
import lombok.*;
import net.tylerwade.registrationsystem.auth.User;
import net.tylerwade.registrationsystem.coursesection.CourseSection;
import net.tylerwade.registrationsystem.enrollment.dto.EnrollmentDTO;
import net.tylerwade.registrationsystem.enrollment.dto.InstructorEnrollmentDTO;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

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
public class Enrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private User student;

    @ManyToOne
    @JoinColumn(name = "course_section_id")
    private CourseSection courseSection;

    private Integer grade;

    private String status;

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant modifiedAt;

    public EnrollmentDTO toDTO() {
        return new EnrollmentDTO(id,
                student.toDTO(),
                courseSection.toDTO(),
                grade,
                status,
                createdAt,
                modifiedAt);
    }

    public InstructorEnrollmentDTO toInstructorDTO() {
        return new InstructorEnrollmentDTO(
                id,
                student.toDTO(),
                courseSection.getId(),
                grade,
                status,
                createdAt,
                modifiedAt
        );
    }

}
