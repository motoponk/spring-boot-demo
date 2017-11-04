package com.sivalabs.moviefinder.repository;

import com.sivalabs.moviefinder.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Repository to perform database operations on Movie entity
 */
public interface MovieRepository extends JpaRepository<Movie, Long> {
    @Query("select m from Movie m join fetch m.genres where lower(m.title) like lower(concat('%', ?1,'%'))")
    List<Movie> findMovies(String searchKey);

    @Query("select m from Movie m where ?1 MEMBER OF m.genres")
    List<Movie> findMoviesByGenre(String genre);
}
