package com.sivalabs.moviefinder;

import com.sivalabs.moviefinder.config.MovieFinderConfig;
import com.sivalabs.moviefinder.entity.Movie;
import com.sivalabs.moviefinder.repository.MovieRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Database initializer
 */
@Slf4j
@Component
public class DataInitializer implements CommandLineRunner
{
    private final MovieFinderConfig movieFinderConfig;
    private final MovieRepository repo;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public DataInitializer(MovieFinderConfig movieFinderConfig, MovieRepository repo, JdbcTemplate jdbcTemplate) {
        this.movieFinderConfig = movieFinderConfig;
        this.repo = repo;
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public void run(String... strings) throws IOException {

        clearData();
        long start = System.currentTimeMillis();
        int count = 0;

        List<String> dataFiles = movieFinderConfig.getDataFiles();

        for (String dataFile : dataFiles) {
            InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(dataFile);
            log.debug("Filename: {}, stream: {}", dataFile, inputStream);

            try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
                String line;
                while ((line = br.readLine()) != null) {
                    count++;
                    if(count == 1){
                        continue;
                    }
                    final Movie movie = getAsMovie(line);
                    repo.save(movie);
                }
            }
        }

        long end = System.currentTimeMillis();
        log.info("Total {} movies data inserted into db. Time taken {} milliseconds",
                count-1,
                (end-start));
    }

    private void clearData() {
        jdbcTemplate.execute("DELETE FROM ACTORS");
        log.info("Deleted all ACTORS");

        jdbcTemplate.execute("DELETE FROM WRITERS");
        log.info("Deleted all WRITERS");

        jdbcTemplate.execute("DELETE FROM DIRECTORS");
        log.info("Deleted all DIRECTORS");

        jdbcTemplate.execute("DELETE FROM GENRES");
        log.info("Deleted all GENRES");

        jdbcTemplate.execute("DELETE FROM MOVIES");
        log.info("Deleted all MOVIES");

    }

    private Movie getAsMovie(String line) {
        log.debug("Reading movie info : {}",line);
        String[] parts = line.split("\t");
        String movie_code	= parts[0];
        String title = parts[1];
        boolean is_adult  = Boolean.parseBoolean(parts[2]);
        int release_year = NumberUtils.toInt(parts[3],0);
        int duration = NumberUtils.toInt(parts[4],0);
        double avg_rating = NumberUtils.toDouble(parts[6], 0);
        int votes = NumberUtils.toInt(parts[7],0);

        Set<String> genres = new HashSet<>();
        if(parts.length > 5)
        {
            genres = Arrays.asList(parts[5].split(","))
                    .stream()
                    .filter( d -> d !=null && d.trim().length()!=0)
                    .collect(Collectors.toSet());
        }

        Set<String> directors = new HashSet<>();
        if(parts.length > 8)
        {
            directors = Arrays.asList(parts[8].split("\\|"))
                    .stream()
                    .filter( d -> d !=null && d.trim().length()!=0)
                    .collect(Collectors.toSet());
        }

        Set<String> writers = new HashSet<>();
        if(parts.length > 9)
        {
            writers = Arrays.asList(parts[9].split("\\|"))
                    .stream()
                    .filter( d -> d !=null && d.trim().length()!=0)
                    .collect(Collectors.toSet());
        }

        Set<String>  actors = new HashSet<>();
        if(parts.length > 10)
        {
            actors = Arrays.asList(parts[10].split("\\|"))
                    .stream()
                    .filter( d -> d !=null && d.trim().length()!=0)
                    .collect(Collectors.toSet());
        }
        return new Movie(null, movie_code, title, is_adult, release_year, duration, avg_rating, votes,
                genres, directors, writers, actors);
    }
}
