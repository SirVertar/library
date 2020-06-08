package com.jakuszko.mateusz.library.domain;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Entity(name = "TITLES")
public class Title {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private Long id;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "AUTHOR")
    private String author;

    @Column(name = "RELEASE_DATE")
    private Integer releaseDate;

    @OneToMany(
            targetEntity = Copy.class,
            mappedBy = "title",
            fetch = FetchType.LAZY
    )
    private List<Copy> copies;

}
