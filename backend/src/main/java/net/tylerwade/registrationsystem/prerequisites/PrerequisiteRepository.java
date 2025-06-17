package net.tylerwade.registrationsystem.prerequisites;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrerequisiteRepository extends JpaRepository<Prerequisite, Long> {

    boolean existsByCourse_IdAndRequiredCourse_Id(Long courseId, Long requiredCourseId);

    boolean existsByCourse_IdAndRequiredCourse_IdAndIdNot(Long courseId, Long requiredCourseId, Long id);

    List<Prerequisite> findByCourse_Id(Long courseId);


}
