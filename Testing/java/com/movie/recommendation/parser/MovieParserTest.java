/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
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
/**
 *
 * @author Mina_Antony
 */
public class MovieParserTest {


@Test
public void testParseMovies_ValidRecord() throws Exception {
    // Create temporary file
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


    
@Test
public void testParseMovies_MultipleGenres() throws Exception {
    Path temp = Files.createTempFile("movies", ".txt");
    Files.write(temp, List.of(
            "The Legend,TL012",
            "action,drama,crime"
    ));

    MovieParser parser = new MovieParser(new MovieValidator());
    List<Movie> movies = parser.parseMovies(temp.toString());

    assertEquals(1, movies.size());
    Movie movie = movies.get(0);

    assertEquals(3, movie.getGenres().size());
    assertTrue(movie.getGenres().contains("action"));
    assertTrue(movie.getGenres().contains("drama"));
    assertTrue(movie.getGenres().contains("crime"));
}


@Test
public void testParseMovies_InvalidLineFormat() throws Exception {
    Path temp = Files.createTempFile("movies", ".txt");
    Files.write(temp, List.of(
            "The Dark Knight TDK123", // ❌ missing comma
            "action,thriller"
    ));

    MovieParser parser = new MovieParser(new MovieValidator());

    assertThrows(ValidationException.class, () -> {
        parser.parseMovies(temp.toString());
    });
}

@Test
public void testParseMovies_EmptyLine() throws Exception {
    Path temp = Files.createTempFile("movies", ".txt");
    Files.write(temp, List.of(
            "",                                
            "Inception,I012",
            "sci-fi,thriller"
    ));

    MovieParser parser = new MovieParser(new MovieValidator());
    List<Movie> movies = parser.parseMovies(temp.toString());

    assertEquals(1, movies.size());
    assertEquals("Inception", movies.get(0).getTitle());
}

}