package com.movie.recommendation.parser;
import com.movie.recommendation.model.Movie;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.movie.recommendation.validator.MovieValidator;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;
import com.movie.recommendation.exception.ValidationException;
public class ParseMoviesTest {

    // DF1 – Empty file
    @Test
    public void testParseMovies_EmptyFile() throws Exception {
        Path temp = Files.createTempFile("movies", ".txt");

        MovieParser parser = new MovieParser(new MovieValidator());
        List<Movie> movies = parser.parseMovies(temp.toString());

        assertNotNull(movies);
        assertEquals(0, movies.size());
    }

    // DF2 – Skip empty line
    @Test
    public void testParseMovies_EmptyLineSkipped() throws Exception {
        Path temp = Files.createTempFile("movies", ".txt");
        Files.write(temp, List.of(
                "",
                "Inception,I123",
                "sci-fi"
        ));

        MovieParser parser = new MovieParser(new MovieValidator());
        List<Movie> movies = parser.parseMovies(temp.toString());

        assertEquals(1, movies.size());
    }

    // DF3 – Invalid format (no comma)
    @Test
    public void testParseMovies_InvalidFormat() throws Exception {
        Path temp = Files.createTempFile("movies", ".txt");
        Files.write(temp, List.of("MovieWithoutComma"));

        MovieParser parser = new MovieParser(new MovieValidator());

        assertThrows(ValidationException.class, () ->
                parser.parseMovies(temp.toString()));
    }

    // DF4 – Missing genre line
    @Test
    public void testParseMovies_MissingGenreLine() throws Exception {
        Path temp = Files.createTempFile("movies", ".txt");
        Files.write(temp, List.of("Interstellar,INT456"));

        MovieParser parser = new MovieParser(new MovieValidator());

        assertThrows(ValidationException.class, () ->
                parser.parseMovies(temp.toString()));
    }

    // DF5 – Valid movie (YOUR TEST)
    @Test
    public void testParseMovies_ValidRecord() throws Exception {
        Path temp = Files.createTempFile("movies", ".txt");
        Files.write(temp, List.of(
                "The Dark Knight,TDK123",
                "action,thriller"
        ));

        MovieParser parser = new MovieParser(new MovieValidator());
        List<Movie> movies = parser.parseMovies(temp.toString());

        assertEquals(1, movies.size());

        Movie movie = movies.get(0);
        assertEquals("The Dark Knight", movie.getTitle());
        assertEquals("TDK123", movie.getId());
        assertEquals(2, movie.getGenres().size());
        assertTrue(movie.getGenres().contains("action"));
        assertTrue(movie.getGenres().contains("thriller"));
    }
}
