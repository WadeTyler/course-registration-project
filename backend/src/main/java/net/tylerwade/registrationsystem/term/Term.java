package net.tylerwade.registrationsystem.term;

import jakarta.persistence.*;
import lombok.*;
import net.tylerwade.registrationsystem.term.dto.TermDTO;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.util.Date;

/**
 * Represents a term entity in the registration system.
 * Contains details about the term's start and end dates, registration period, and creation timestamp.
 */
@Entity
@Table(name = "terms")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Builder
public class Term {

    /**
     * Unique identifier for the term.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The start date of the term.
     */
    @Column(nullable = false)
    private LocalDate startDate;

    /**
     * The end date of the term.
     */
    @Column(nullable = false)
    private LocalDate endDate;

    /**
     * The start date for registration during the term.
     */
    @Column(nullable = false)
    private LocalDate registrationStart;

    /**
     * The end date for registration during the term.
     */
    @Column(nullable = false)
    private LocalDate registrationEnd;

    /**
     * Timestamp indicating when the term was created.
     * Automatically populated during creation.
     */
    @CreatedDate
    private Date createdAt;

    /**
     * Converts the Term entity to a TermDTO object.
     * Includes details about the term's dates and registration period.
     *
     * @return A TermDTO representation of the term.
     */
    public TermDTO toDTO() {
        return new TermDTO(id,
                startDate,
                endDate,
                registrationStart,
                registrationEnd,
                createdAt);
    }

    /**
     * Checks if the registration period for the term is currently open.
     *
     * @return True if the registration period is open, false otherwise.
     */
    public boolean isRegistrationOpen() {
        var now = LocalDate.now();
        return (registrationStart.isBefore(now) || registrationStart.equals(now)) && (registrationEnd.isAfter(now) || registrationEnd.equals(now));
    }

    /**
     * Checks if the term has ended.
     *
     * @return True if the term has ended, false otherwise.
     */
    public boolean hasEnded() {
        var now = LocalDate.now();
        return now.isAfter(endDate);
    }
}