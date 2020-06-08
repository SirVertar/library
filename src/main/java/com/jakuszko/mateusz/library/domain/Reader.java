package com.jakuszko.mateusz.library.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity(name = "READERS")
public class Reader {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private Long id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "SURNAME")
    private String surname;

    @Column(name = "REGISTERED_DATE")
    private LocalDate registeredDate;

    @OneToMany(targetEntity = Borrow.class,
            mappedBy = "reader",
            fetch = FetchType.LAZY)
    private List<Borrow> borrows;
}
