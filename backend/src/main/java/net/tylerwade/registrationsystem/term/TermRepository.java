package net.tylerwade.registrationsystem.term;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Date;

@Repository
public interface TermRepository extends JpaRepository<Term, Long> {

    Page<Term> findAll(Pageable pageable);

    boolean existsByStartDateAndEndDate(Date startDate, Date endDate);

    boolean existsByStartDateAndEndDateAndIdNot(Date startDate, Date endDate, Long id);

}
