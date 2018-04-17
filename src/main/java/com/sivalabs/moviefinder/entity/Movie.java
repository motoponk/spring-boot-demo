package com.sivalabs.moviefinder.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Movie database entity
 */
@Entity
@Table(name = "MOVIES")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Movie {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String movieCode;
    private String title;

    private boolean isAdult;
    private int releasedYear;
    private int durationInMinutes;
    private double avgRating;
    private int votes;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name="GENRES",
            joinColumns=@JoinColumn(name="MOVIE_ID")
    )
    @Column(name="GENRE")
    private Set<String> genres;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
            name="DIRECTORS",
            joinColumns=@JoinColumn(name="MOVIE_ID")
    )
    @Column(name="DIRECTOR")
    private Set<String> directors;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
            name="WRITERS",
            joinColumns=@JoinColumn(name="MOVIE_ID")
    )
    @Column(name="WRITER")
    private Set<String> writers;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(
            name="ACTORS",
            joinColumns=@JoinColumn(name="MOVIE_ID")
    )
    @Column(name="ACTOR")
    private Set<String> actors;

}
