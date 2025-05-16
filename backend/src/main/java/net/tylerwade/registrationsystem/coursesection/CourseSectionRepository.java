package net.tylerwade.registrationsystem.coursesection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseSectionRepository extends JpaRepository<CourseSection, Long> {
    List<CourseSection> findAllByCourse_Id(Long courseId);

    List<CourseSection> findAllByInstructor_Id(String instructorId);

    Optional<CourseSection> findByIdAndInstructor_Id(Long id, String instructorId);
}
