package net.tylerwade.registrationsystem.course;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    Page<Course> findAll(Pageable pageable);

    List<Course> findAllByDepartmentIgnoreCase(String department);

    Course findByDepartmentIgnoreCaseAndCodeIgnoreCase(String department, String code);

    boolean existsByDepartmentIgnoreCaseAndCodeIgnoreCase(String department, String code);

    boolean existsByDepartmentIgnoreCaseAndCodeIgnoreCaseAndIdNot(String department, String code, Long id);

    boolean existsByTitleIgnoreCase(String title);

    boolean existsByTitleIgnoreCaseAndIdNot(String title, Long id);
}
