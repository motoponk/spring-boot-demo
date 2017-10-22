package com.sivalabs.moviefinder.web.controller;

import com.sivalabs.moviefinder.entity.Movie;
import com.sivalabs.moviefinder.service.MovieService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;

/**
 * Integration tests for MovieController
 */
@RunWith(SpringRunner.class)
@WebMvcTest(controllers = MovieController.class)
public class MovieControllerIT {

    @Autowired
    private MockMvc mvc;

    @MockBean
    MovieService movieService;

    Movie m1 = new Movie(1L,"Pulp Fiction", Arrays.asList("Comedy","Crime","Drama","Thriller"));
    Movie m2 = new Movie(2L,"Priest", Arrays.asList("Drama"));
    Movie m3 = new Movie(3L,"Roommates", Arrays.asList("Comedy","Drama"));
    Movie m4 = new Movie(4L,"Shawshank Redemption", Arrays.asList("Crime","Drama"));

    List<String> genres = Arrays.asList("Comedy","Crime","Drama","Thriller");

    @Test
    public void test_get_all_movies() throws Exception {
        when(movieService.findMovies("")).thenReturn(Arrays.asList(m1,m2,m3));
        when(movieService.getGenres()).thenReturn(genres);

        this.mvc.perform(get("/movies")
                .accept(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attribute("movies", hasSize(3)))
                .andExpect(model().attribute("genres", hasSize(genres.size())))
        ;
        verify(movieService, times(1)).findMovies("");
        verify(movieService, times(1)).getGenres();

    }

    @Test
    public void test_get_movies_by_genre() throws Exception {
        String genre = "Crime";
        when(movieService.findMoviesByGenre(genre)).thenReturn(Arrays.asList(m1,m4));
        when(movieService.getGenres()).thenReturn(genres);

        this.mvc.perform(get("/movies")
                .param("genre", genre)
                .accept(MediaType.TEXT_HTML))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attribute("movies", hasSize(2)))
                .andExpect(model().attribute("genres", hasSize(genres.size())))
        ;
        verify(movieService, times(1)).findMoviesByGenre(genre);
        verify(movieService, times(1)).getGenres();
    }
}
