package com.jakuszko.mateusz.library.domain;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity(name = "COPYS")
public class Copy {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @NotNull
    @Column(name = "ID")
    private Long id;

    @Column(name = "IS_BORROWED")
    private Boolean isBorrowed;

    @ManyToOne(fetch = FetchType.EAGER)
    private Borrow borrow;

    @ManyToOne(targetEntity = Title.class,
            fetch = FetchType.EAGER)
    private Title title;
}
