package com.sivalabs.moviefinder.repository;

import com.sivalabs.moviefinder.entity.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Long> {
    @Query("select m from Movie m where lower(m.title) like lower(concat('%', ?1,'%'))")
    Page<Movie> findMovies(String searchKey, Pageable pageable);

    @Query("select m from Movie m where ?1 MEMBER OF m.genres")
    List<Movie> findMoviesByGenre(String genre);
}
