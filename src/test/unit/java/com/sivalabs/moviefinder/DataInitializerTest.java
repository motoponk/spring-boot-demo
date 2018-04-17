package com.sivalabs.moviefinder;

import com.sivalabs.moviefinder.config.MovieFinderConfig;
import com.sivalabs.moviefinder.entity.Movie;
import com.sivalabs.moviefinder.repository.MovieRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for DataInitializer
 */
@RunWith(MockitoJUnitRunner.class)
public class DataInitializerTest {

    @Mock
    MovieFinderConfig movieFinderConfig;

    @Mock
    MovieRepository repo;

    @Mock
    JdbcTemplate jdbcTemplate;

    @InjectMocks
    DataInitializer dataInitializer;

    @Test
    public void run() throws Exception {
        when(movieFinderConfig.getDataFiles()).thenReturn(Arrays.asList("movies-small.tsv"));
        dataInitializer.run();

        verify(jdbcTemplate, atLeast(1)).execute(anyString());
        verify(repo, atLeast(1)).save(any(Movie.class));
    }

}
