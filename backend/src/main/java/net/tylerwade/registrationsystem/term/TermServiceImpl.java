package net.tylerwade.registrationsystem.term;

import net.tylerwade.registrationsystem.exception.HttpRequestException;
import net.tylerwade.registrationsystem.term.dto.ManageTermRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class TermServiceImpl implements TermService{

    private final TermRepository termRepository;

    public TermServiceImpl(TermRepository termRepository) {
        this.termRepository = termRepository;
    }


    @Override
    public Page<Term> findAll(Pageable pageable) {
        return termRepository.findAll(pageable);
    }

    @Override
    public Term findById(Long termId) throws HttpRequestException {
        return termRepository.findById(termId).orElseThrow(() -> new HttpRequestException(HttpStatus.NOT_FOUND, "Term not found."));
    }

    @Override
    public Term create(ManageTermRequest manageTermRequest) throws HttpRequestException {
        // Check if valid
        if (!manageTermRequest.isValid()) {
            throw new HttpRequestException(HttpStatus.BAD_REQUEST, "Fields not valid. End Dates must be after Start Dates and Registration must end be before start date.");
        }

        // Check if term already exists with start dates and end date.
        if (termRepository.existsByStartDateAndEndDate(manageTermRequest.startDate(), manageTermRequest.endDate())) {
            throw new HttpRequestException(HttpStatus.CONFLICT, "A term already exists with that start date and end date.");
        }

        // Create term
        Term term = Term.builder()
                .startDate(manageTermRequest.startDate())
                .endDate(manageTermRequest.endDate())
                .registrationStart(manageTermRequest.registrationStart())
                .registrationEnd(manageTermRequest.registrationEnd())
                .build();

        // Save and return
        return termRepository.save(term);
    }

    @Override
    public Term update(Long termId, ManageTermRequest manageTermRequest) throws HttpRequestException {
        if (!manageTermRequest.isValid()) {
            throw new HttpRequestException(HttpStatus.BAD_REQUEST, "Fields not valid. End Dates must be after Start Dates and Registration must end be before start date.");
        }

        // Find target term
        Term term = findById(termId);

        // Check if term already exists with start dates and end date and is not the current term.
        if (termRepository.existsByStartDateAndEndDateAndIdNot(manageTermRequest.startDate(), manageTermRequest.endDate(), termId)) {
            throw new HttpRequestException(HttpStatus.CONFLICT, "A term already exists with that start date and end date.");
        }

        // Update term
        term.setStartDate(manageTermRequest.startDate());
        term.setEndDate(manageTermRequest.endDate());
        term.setRegistrationStart(manageTermRequest.registrationStart());
        term.setRegistrationEnd(manageTermRequest.registrationEnd());

        // Save and return
        return termRepository.save(term);
    }

    @Override
    public void delete(Long termId) throws HttpRequestException {
        // Check if term exists
        if (!termRepository.existsById(termId)) {
            throw new HttpRequestException(HttpStatus.NOT_FOUND, "Term not found.");
        }

        // Delete term
        termRepository.deleteById(termId);
    }
}
