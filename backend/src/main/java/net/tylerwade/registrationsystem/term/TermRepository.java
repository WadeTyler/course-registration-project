package net.tylerwade.registrationsystem.term;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface TermRepository extends JpaRepository<Term, Long> {

    Page<Term> findAll(Pageable pageable);

    boolean existsByStartDateAndEndDate(LocalDate startDate, LocalDate endDate);

    boolean existsByStartDateAndEndDateAndIdNot(LocalDate startDate, LocalDate endDate, Long id);

}