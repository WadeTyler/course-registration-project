package net.tylerwade.registrationsystem.course;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.tylerwade.registrationsystem.course.dto.CourseDTO;
import net.tylerwade.registrationsystem.coursesection.CourseSection;
import net.tylerwade.registrationsystem.prerequisites.Prerequisite;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

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
@Builder
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(nullable = false, length = 10)
    private String department;

    @Column(nullable = false, length = 10)
    private String code;

    @Column(nullable = false, length = 100, unique = true)
    private String title;

    @Column(nullable = false, length = 1000)
    private String description;

    @Column(nullable = false)
    private Integer credits;

    @OneToMany(mappedBy = "course", orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Prerequisite> prerequisites = new ArrayList<>();

    @OneToMany(mappedBy = "course", orphanRemoval = true, fetch = FetchType.EAGER)
    private List<CourseSection> courseSections = new ArrayList<>();

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant modifiedAt;

    public Course(String department, String code, String title, String description, Integer credits) {
        this.department = department;
        this.code = code;
        this.title = title;
        this.description = description;
        this.credits = credits;
    }

    public Course(Long id, String department, String code, String title, String description, Integer credits, List<Prerequisite> prerequisites, List<CourseSection> courseSections, Instant createdAt, Instant modifiedAt) {
        this.id = id;
        this.department = department;
        this.code = code;
        this.title = title;
        this.description = description;
        this.credits = credits;
        this.prerequisites = prerequisites;
        this.courseSections = courseSections;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }

    public CourseDTO toDTO() {
        return new CourseDTO(id,
                department,
                code,
                title,
                description,
                credits,
                prerequisites.stream().map(Prerequisite::toDTO).toList(),
                courseSections.stream().map(CourseSection::toDTO).toList(),
                createdAt,
                modifiedAt);
    }

}
