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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 10)
    private String department;

    @Column(nullable = false)
    private Integer code;

    @Column(nullable = false, unique = true)
    private String title;

    @Column(nullable = false, length = 4000)
    private String description;

    @Column(nullable = false)
    private Integer credits;

    @OneToMany(mappedBy = "course", orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Prerequisite> prerequisites = new ArrayList<>();

    @OneToMany(mappedBy = "course", orphanRemoval = true, fetch = FetchType.EAGER)
    private List<CourseSection> courseSections = new ArrayList<>();

    @CreatedDate
    private Date createdAt;

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
