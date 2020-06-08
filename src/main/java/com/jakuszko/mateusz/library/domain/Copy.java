package com.jakuszko.mateusz.library.domain;

import lombok.*;

import javax.persistence.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity(name = "COPYS")
public class Copy {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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
