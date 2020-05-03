package com.jakuszko.mateusz.library.domain;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity(name = "BORROWS")
public class Borrow {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @NotNull
    @Column(name = "ID")
    private Long id;

    @Column(name = "START_DATE")
    private final LocalDate startDate = LocalDate.now();

    @Column(name = "END_DATE")
    private final LocalDate endDate = LocalDate.of(LocalDate.now().getYear(),
            LocalDate.now().getMonth().getValue() + 3,
            LocalDate.now().getDayOfMonth());

    @ManyToOne(targetEntity = Reader.class,
            fetch = FetchType.EAGER)
    private Reader reader;

    @OneToMany(targetEntity = Copy.class,
            mappedBy = "borrow",
            fetch = FetchType.LAZY)
    private List<Copy> copies;
}
