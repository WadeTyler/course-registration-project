package net.tylerwade.registrationsystem.term;

import net.tylerwade.registrationsystem.exception.HttpRequestException;
import net.tylerwade.registrationsystem.term.dto.ManageTermRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Service interface for managing academic terms.
 */
@Service
public interface TermService {

    /**
     * Retrieves a paginated list of all terms.
     *
     * @param pageable the pagination information
     * @return a page of terms
     */
    Page<Term> findAll(Pageable pageable);

    /**
     * Finds a term by its ID.
     *
     * @param termId the ID of the term
     * @return the found term
     * @throws HttpRequestException if the term is not found
     */
    Term findById(Long termId) throws HttpRequestException;

    /**
     * Creates a new term.
     *
     * @param manageTermRequest the request containing term details
     * @return the created term
     * @throws HttpRequestException if creation fails
     */
    Term create(ManageTermRequest manageTermRequest) throws HttpRequestException;

    /**
     * Updates an existing term.
     *
     * @param termId the ID of the term to update
     * @param manageTermRequest the request containing updated term details
     * @return the updated term
     * @throws HttpRequestException if update fails
     */
    Term update(Long termId, ManageTermRequest manageTermRequest) throws HttpRequestException;

    /**
     * Deletes a term by its ID.
     *
     * @param termId the ID of the term to delete
     * @throws HttpRequestException if deletion fails
     */
    void delete(Long termId) throws HttpRequestException;
}
