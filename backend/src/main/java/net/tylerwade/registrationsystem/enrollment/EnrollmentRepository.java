package net.tylerwade.registrationsystem.enrollment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, EnrollmentId> {

    List<Enrollment> findAllByStatusIsAndCourseSection_Term_StartDateBefore(String status, LocalDate courseSectionTermStartDateBefore);

    List<Enrollment> findAllByStudent_IdOrderByCourseSection_Term_StartDateDesc(Long studentId);

    boolean existsByStudent_IdAndCourseSection_Id(Long studentId, Long courseSectionId);
}
