package net.tylerwade.registrationsystem.coursesection;

import jakarta.persistence.*;
import lombok.*;
import net.tylerwade.registrationsystem.auth.User;
import net.tylerwade.registrationsystem.course.Course;
import net.tylerwade.registrationsystem.coursesection.dto.CourseSectionDTO;
import net.tylerwade.registrationsystem.coursesection.dto.InstructorCourseSectionDTO;
import net.tylerwade.registrationsystem.enrollment.Enrollment;
import net.tylerwade.registrationsystem.term.Term;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "course_sections")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseSection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @ManyToOne
    @JoinColumn(name = "term_id", nullable = false)
    private Term term;

    @ManyToOne
    @JoinColumn(name = "instructor_id")
    private User instructor;

    @OneToMany(mappedBy = "courseSection")
    private List<Enrollment> enrollments = new ArrayList<>();

    @Column(nullable = false)
    private Integer enrolledCount;

    @Column(nullable = false, length = 20)
    private String room;

    @Column(nullable = false)
    private Integer capacity = 0;

    @Column(nullable = false)
    private String schedule;

    @CreatedDate
    private Date createdAt;

    public CourseSectionDTO toDTO() {
        return new CourseSectionDTO(id,
                course.toAttributeDTO(),
                term.toDTO(),
                instructor != null ? instructor.toDTO() : null,
                room,
                capacity,
                schedule,
                enrolledCount,
                createdAt
        );
    }

    public InstructorCourseSectionDTO toInstructorDTO() {
        return new InstructorCourseSectionDTO(
                id,
                course.toAttributeDTO(),
                term.toDTO(),
                instructor != null ? instructor.toDTO() : null,
                room,
                capacity,
                schedule,
                enrolledCount,
                enrollments.stream().map(Enrollment::toInstructorDTO).toList(),
                createdAt);
    }
}
