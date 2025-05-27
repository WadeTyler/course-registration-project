package net.tylerwade.registrationsystem.prerequisites;

import jakarta.persistence.*;
import lombok.*;
import net.tylerwade.registrationsystem.course.Course;
import net.tylerwade.registrationsystem.prerequisites.dto.PrerequisiteDTO;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.util.Date;

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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @ManyToOne
    @JoinColumn(name = "required_course_id", nullable = false)
    private Course requiredCourse;

    @Column(nullable = false)
    private BigDecimal minimumGrade;

    @CreatedDate
    private Date createdAt;

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
