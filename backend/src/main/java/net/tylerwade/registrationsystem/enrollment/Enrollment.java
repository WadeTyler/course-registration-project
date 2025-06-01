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
@IdClass(EnrollmentId.class)
public class Enrollment {

    @Id
    @Column(name = "student_id")
    private Long studentId;

    @Id
    @Column(name = "course_section_id")
    private Long courseSectionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", referencedColumnName = "id", insertable = false, updatable = false)
    private User student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_section_id", referencedColumnName = "id", insertable = false, updatable = false)
    private CourseSection courseSection;

    @Column(nullable = false)
    private BigDecimal grade;

    @Column(nullable = false)
    private String status;

    @CreatedDate
    private Date createdAt;

    public EnrollmentDTO toDTO() {
        return new EnrollmentDTO(
                student.toDTO(),
                courseSection.toDTO(),
                grade,
                status,
                createdAt);
    }

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
