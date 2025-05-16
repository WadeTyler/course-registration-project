package net.tylerwade.registrationsystem.prerequisites;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.tylerwade.registrationsystem.course.Course;
import net.tylerwade.registrationsystem.prerequisites.dto.PrerequisiteDTO;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

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
@Builder
public class Prerequisite {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @ManyToOne
    @JoinColumn(name = "required_course_id", nullable = false)
    private Course requiredCourse;

    @Column(nullable = false)
    private Character minimumGrade;

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant modifiedAt;

    public Prerequisite(Course course, Course requiredCourse, Character minimumGrade) {
        this.course = course;
        this.requiredCourse = requiredCourse;
        this.minimumGrade = minimumGrade;
    }

    public Prerequisite(Long id, Course course, Course requiredCourse, Character minimumGrade, Instant createdAt, Instant modifiedAt) {
        this.id = id;
        this.course = course;
        this.requiredCourse = requiredCourse;
        this.minimumGrade = minimumGrade;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }

    public PrerequisiteDTO toDTO() {
        return new PrerequisiteDTO(id,
                course.getId(),
                requiredCourse.getId(),
                requiredCourse.getDepartment(),
                requiredCourse.getCode(),
                minimumGrade,
                createdAt,
                modifiedAt);
    }
}
