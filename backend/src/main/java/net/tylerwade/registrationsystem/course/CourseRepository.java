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

    Course findByDepartmentIgnoreCaseAndCode(String department, Integer code);

    boolean existsByDepartmentIgnoreCaseAndCode(String department, Integer code);

    boolean existsByDepartmentIgnoreCaseAndCodeAndIdNot(String department, Integer code, Long id);

    boolean existsByTitleIgnoreCase(String title);

    boolean existsByTitleIgnoreCaseAndIdNot(String title, Long id);
}
