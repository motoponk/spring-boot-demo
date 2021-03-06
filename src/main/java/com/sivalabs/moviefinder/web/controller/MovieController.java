package com.sivalabs.moviefinder.web.controller;

import com.sivalabs.moviefinder.entity.Movie;
import com.sivalabs.moviefinder.service.MovieService;
import com.sivalabs.moviefinder.support.logging.Loggable;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Metrics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Controller which handler web requests for /movies endpoint
 */
@Controller
@Loggable
public class MovieController
{
    Counter counter = Metrics.counter("handler.calls", "uri", "/movies");

    private MovieService movieService;

    @Autowired
    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping("/")
    public String index() {
        return "redirect:/movies";
    }

    @GetMapping("/movies")
    public String list(@RequestParam(name = "genre", defaultValue = "") String genre,
                       @RequestParam(name = "searchKey", defaultValue = "") String searchKey,
                       Model model
                       )
    {
        counter.increment();
        List<Movie> movies;
        if(genre.trim().isEmpty()) {
            movies = movieService.findMovies(searchKey);
        } else {
            movies = movieService.findMoviesByGenre(genre);
        }
        model.addAttribute("movies", movies);
        model.addAttribute("genres", movieService.getGenres());
        return "index";
    }
}
