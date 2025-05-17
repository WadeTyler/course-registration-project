package net.tylerwade.registrationsystem.term;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.tylerwade.registrationsystem.term.dto.TermDTO;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.sql.Date;
import java.time.Instant;

@Entity
@Table(name = "terms")
@Getter
@Setter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Builder
public class Term {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private Date startDate;

    @Column(nullable = false)
    private Date endDate;

    @Column(nullable = false)
    private Date registrationStart;

    @Column(nullable = false)
    private Date registrationEnd;

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant modifiedAt;

    public Term(String title, Date startDate, Date endDate, Date registrationStart, Date registrationEnd) {
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.registrationStart = registrationStart;
        this.registrationEnd = registrationEnd;
    }

    public Term(Long id, String title, Date startDate, Date endDate, Date registrationStart, Date registrationEnd, Instant createdAt, Instant modifiedAt) {
        this.id = id;
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.registrationStart = registrationStart;
        this.registrationEnd = registrationEnd;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }

    public TermDTO toDTO() {
        return new TermDTO(id,
                title,
                startDate,
                endDate,
                registrationStart,
                registrationEnd,
                createdAt,
                modifiedAt);
    }

    public boolean isRegistrationOpen() {
        Date now = new Date(System.currentTimeMillis());
        return (registrationStart.before(now) || registrationStart.equals(now)) && (registrationEnd.after(now) || registrationEnd.equals(now));
    }

    public boolean hasEnded() {
        Date now = new Date(System.currentTimeMillis());
        return now.after(endDate);
    }
}
