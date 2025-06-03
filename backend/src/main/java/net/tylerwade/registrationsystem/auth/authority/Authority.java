package net.tylerwade.registrationsystem.auth.authority;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "authorities")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Authority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 13)
    private String name;

}
