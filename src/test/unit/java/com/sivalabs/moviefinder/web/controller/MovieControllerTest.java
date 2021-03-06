package com.sivalabs.moviefinder.web.controller;

import com.sivalabs.moviefinder.entity.Movie;
import com.sivalabs.moviefinder.service.MovieService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toSet;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for MovieController
 */
@RunWith(MockitoJUnitRunner.class)
public class MovieControllerTest {

    @Mock
    MovieService movieService;

    @InjectMocks
    MovieController controller;

    Movie m1 = new Movie(1L,
            "m1",
            "Pulp Fiction",
            false,
            2000,
            60,
            9.0,
            456,
            asList("Comedy","Crime","Drama","Thriller").stream().collect(toSet()),
            asList("Quentin Tarantino").stream().collect(toSet()),
            asList("Quentin Tarantino").stream().collect(toSet()),
            asList("Samuel L Jackson","John Travolta","Uma Thuraman").stream().collect(toSet())
    );

    Movie m2 = new Movie(2L,
            "m2",
            "Kill Bill",
            false,
            2003,
            70,
            8.0,
            496,
            asList("Crime","Drama").stream().collect(toSet()),
            asList("Quentin Tarantino").stream().collect(toSet()),
            asList("Quentin Tarantino").stream().collect(toSet()),
            asList("Samuel L Jackson","Uma Thuraman").stream().collect(toSet())
    );

    List<String> genres = asList("Comedy","Crime","Drama","Thriller");

    @Test
    public void test_index_redirection() {
        final String view = controller.index();
        assertThat(view).isEqualTo("redirect:/movies");
    }

    @Test
    public void test_get_all_movies() {
        Model model = new ExtendedModelMap();
        String searchKey = "";
        String genre = "";

        when(movieService.getGenres()).thenReturn(genres);
        when(movieService.findMovies(searchKey)).thenReturn(asList(m1,m2));

        final String view = controller.list(genre, searchKey, model);
        assertThat(view).isEqualTo("index");
        assertThat(model.containsAttribute("movies")).isTrue();
        assertThat((List)model.asMap().get("movies")).hasSize(2);

        assertThat(model.containsAttribute("genres")).isTrue();
        assertThat((List)model.asMap().get("genres")).hasSize(genres.size());

        verify(movieService, times(1)).getGenres();
        verify(movieService, times(1)).findMovies(searchKey);
    }

    @Test
    public void test_get_movies_by_genre() {
        Model model = new ExtendedModelMap();
        String searchKey = "";
        String genre = "Comedy";

        when(movieService.getGenres()).thenReturn(genres);
        when(movieService.findMoviesByGenre(genre)).thenReturn(asList(m1));

        final String view = controller.list(genre, searchKey, model);
        assertThat(view).isEqualTo("index");
        assertThat(model.containsAttribute("movies")).isTrue();
        assertThat((List)model.asMap().get("movies")).hasSize(1);

        assertThat(model.containsAttribute("genres")).isTrue();
        assertThat((List)model.asMap().get("genres")).hasSize(genres.size());

        verify(movieService, times(1)).getGenres();
        verify(movieService, times(1)).findMoviesByGenre(genre);
    }

    @Test
    public void test_get_all_movies_when_genre_is_blank() {
        Model model = new ExtendedModelMap();
        String searchKey = "";
        String genre = "    ";

        when(movieService.getGenres()).thenReturn(genres);
        when(movieService.findMovies(searchKey)).thenReturn(asList(m1,m2));

        controller.list(genre, searchKey, model);

        verify(movieService, times(1)).findMovies(searchKey);
    }
}
