package com.sivalabs.moviefinder;

import com.sivalabs.moviefinder.config.MovieFinderConfig;
import com.sivalabs.moviefinder.entity.Movie;
import com.sivalabs.moviefinder.repository.MovieRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

/**
 * Database initializer
 */
@Slf4j
@Component
public class DataInitializer implements CommandLineRunner
{
    private MovieFinderConfig movieFinderConfig;
    private MovieRepository repo;

    @Autowired
    public DataInitializer(MovieFinderConfig movieFinderConfig, MovieRepository repo) {
        this.movieFinderConfig = movieFinderConfig;
        this.repo = repo;
    }


    @Override
    public void run(String... strings) throws URISyntaxException, IOException {
        repo.deleteAll();
        log.info("Deleted all movies");

        long start = System.currentTimeMillis();

        InputStream inputStream = this.getClass().getClassLoader()
                .getResourceAsStream(movieFinderConfig.getDataFile());
        log.debug("Filename: {}, stream: {}", movieFinderConfig.getDataFile(), inputStream);
        int count = 0;
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = br.readLine()) != null) {
                final Movie movie = getAsMovie(line);
                repo.save(movie);
                count++;
            }
        }

        long end = System.currentTimeMillis();
        log.info("Total {} movies data inserted into db. Time taken {} milliseconds",
                count,
                (end-start));
    }

    private Movie getAsMovie(String line) {
        Long id = Long.valueOf(line.substring(0, line.indexOf(",")));
        String title = line.substring(line.indexOf(",")+1, line.lastIndexOf(","));
        if(title.startsWith("\"") && title.endsWith("\"")){
            title = title.substring(1, title.length()-1);
        }
        final String genresStr = line.substring(line.lastIndexOf(",") + 1);
        List<String> genres = null;
        if(!"(no genres listed)".equalsIgnoreCase(genresStr)){
            genres =  Arrays.asList(genresStr.split("\\|"));
        }
        return new Movie(id, title, genres);
    }
}
