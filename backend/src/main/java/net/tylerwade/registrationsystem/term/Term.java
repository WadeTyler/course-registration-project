package net.tylerwade.registrationsystem.term;

import jakarta.persistence.*;
import lombok.*;
import net.tylerwade.registrationsystem.term.dto.TermDTO;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

@Entity
@Table(name = "terms")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Builder
public class Term {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Date startDate;

    @Column(nullable = false)
    private Date endDate;

    @Column(nullable = false)
    private Date registrationStart;

    @Column(nullable = false)
    private Date registrationEnd;

    @CreatedDate
    private Date createdAt;

    public TermDTO toDTO() {
        return new TermDTO(id,
                startDate,
                endDate,
                registrationStart,
                registrationEnd,
                createdAt);
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
