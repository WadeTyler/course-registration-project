package net.tylerwade.registrationsystem.enrollment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

    List<Enrollment> findAllByStudent_IdOrderByCourseSection_Term_StartDateDesc(String studentId);

    boolean existsByStudent_IdAndCourseSection_Id(String studentId, Long courseSectionId);
}
