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
@Entity(name = "BORROWS")
public class Borrow {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private Long id;

    @Column(name = "START_DATE")
    private LocalDate startDate;

    @Column(name = "END_DATE")
    private LocalDate endDate;

    @ManyToOne(targetEntity = Reader.class,
            fetch = FetchType.EAGER)
    private Reader reader;

    @OneToMany(targetEntity = Copy.class,
            mappedBy = "borrow",
            fetch = FetchType.LAZY)
    private List<Copy> copies;
}
