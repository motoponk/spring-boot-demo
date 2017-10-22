package com.sivalabs.moviefinder;

import com.sivalabs.moviefinder.config.MovieFinderConfig;
import com.sivalabs.moviefinder.entity.Movie;
import com.sivalabs.moviefinder.repository.MovieRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

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

    @InjectMocks
    DataInitializer dataInitializer;

    @Test
    public void run() throws Exception {
        when(movieFinderConfig.getDataFile()).thenReturn("movies-200.csv");
        dataInitializer.run();

        verify(repo, times(1)).deleteAll();
        verify(repo, atLeast(1)).save(any(Movie.class));
    }

}
