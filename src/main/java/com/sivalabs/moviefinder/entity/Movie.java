package com.sivalabs.moviefinder.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "MOVIES")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Movie {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String title;

    @ElementCollection
    @CollectionTable(
            name="GENRES",
            joinColumns=@JoinColumn(name="MOVIE_ID")
    )
    @Column(name="GENRE")
    private List<String> genres;
}
