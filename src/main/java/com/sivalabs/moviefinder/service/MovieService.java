package com.sivalabs.moviefinder.service;

import com.sivalabs.moviefinder.entity.Movie;
import com.sivalabs.moviefinder.repository.MovieRepository;
import com.sivalabs.moviefinder.support.logging.Loggable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service to perform operations on movies like searching for movies by name or by genre
 */
@Service
@Transactional
@Slf4j
@Loggable
public class MovieService {

    private final MovieRepository repo;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public MovieService(MovieRepository repo, JdbcTemplate jdbcTemplate) {
        this.repo = repo;
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<String> getGenres() {
        return jdbcTemplate.query("select distinct genre from genres ORDER BY genre",
                (resultSet, i) -> resultSet.getString("genre"));
    }

    public List<Movie> findMovies(String searchKey) {
        if(searchKey != null && searchKey.trim().isEmpty()){
            return repo.findAll();
        }
        return repo.findMovies(searchKey);
    }

    public List<Movie> findMoviesByGenre(String genre) {
        return repo.findMoviesByGenre(genre);
    }

}
