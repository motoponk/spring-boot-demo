package com.sivalabs.moviefinder.service;

import com.sivalabs.moviefinder.entity.Movie;
import com.sivalabs.moviefinder.repository.MovieRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service to perform operations on movies like searching for movies by name or by genre
 */
@Service
@Slf4j
public class MovieService {

    private static final int PAGE_SIZE = 500;

    private MovieRepository repo;
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public MovieService(MovieRepository repo, JdbcTemplate jdbcTemplate) {
        this.repo = repo;
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<String> getGenres() {
        return jdbcTemplate.query("select distinct genre from genres",
                (resultSet, i) -> resultSet.getString("genre"));
    }

    public List<Movie> findMovies(String searchKey) {
        Pageable pageable   = new PageRequest(0, PAGE_SIZE);
        return repo.findMovies(searchKey, pageable).getContent();
    }

    public List<Movie> findMoviesByGenre(String genre) {
        return repo.findMoviesByGenre(genre);
    }

}
