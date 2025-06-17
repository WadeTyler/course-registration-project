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

/**
 * Represents a course section entity in the registration system.
 * Contains details about the course section, including its course, term, instructor, room, capacity, schedule, and enrollments.
 */
@Entity
@Table(name = "course_sections")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseSection {

    /**
     * Unique identifier for the course section.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The course associated with this section.
     * Mapped as a many-to-one relationship.
     */
    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    /**
     * The term during which this course section is offered.
     * Mapped as a many-to-one relationship.
     */
    @ManyToOne
    @JoinColumn(name = "term_id", nullable = false)
    private Term term;

    /**
     * The instructor assigned to this course section.
     * Mapped as a many-to-one relationship.
     */
    @ManyToOne
    @JoinColumn(name = "instructor_id")
    private User instructor;

    /**
     * List of enrollments associated with this course section.
     * Mapped by the "courseSection" field in the Enrollment entity.
     */
    @OneToMany(mappedBy = "courseSection")
    private List<Enrollment> enrollments = new ArrayList<>();

    /**
     * The number of students currently enrolled in this course section.
     */
    @Column(nullable = false)
    private Integer enrolledCount;

    /**
     * The room where this course section is held.
     * Limited to 20 characters.
     */
    @Column(nullable = false, length = 20)
    private String room;

    /**
     * The maximum capacity of students for this course section.
     * Defaults to 0.
     */
    @Column(nullable = false)
    private Integer capacity = 0;

    /**
     * The schedule for this course section (e.g., days and times).
     */
    @Column(nullable = false)
    private String schedule;

    /**
     * Timestamp indicating when the course section was created.
     * Automatically populated during creation.
     */
    @CreatedDate
    private Date createdAt;

    /**
     * Converts the CourseSection entity to a CourseSectionDTO object.
     * Includes details about the course, term, instructor, room, capacity, schedule, and enrolled count.
     *
     * @return A CourseSectionDTO representation of the course section.
     */
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

    /**
     * Converts the CourseSection entity to an InstructorCourseSectionDTO object.
     * Includes additional details about enrollments.
     *
     * @return An InstructorCourseSectionDTO representation of the course section.
     */
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