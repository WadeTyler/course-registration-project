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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private User student;

    @ManyToOne
    @JoinColumn(name = "course_section_id", nullable = false)
    private CourseSection courseSection;

    @Column(nullable = false)
    private BigDecimal grade;

    @Column(nullable = false)
    private String status;

    @CreatedDate
    private Date createdAt;

    public EnrollmentDTO toDTO() {
        return new EnrollmentDTO(id,
                student.toDTO(),
                courseSection.toDTO(),
                grade,
                status,
                createdAt);
    }

    public InstructorEnrollmentDTO toInstructorDTO() {
        return new InstructorEnrollmentDTO(
                id,
                student.toDTO(),
                courseSection.getId(),
                grade,
                status,
                createdAt
        );
    }

}
