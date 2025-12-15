/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package com.movie.recommendation.Integration_Testing;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.movie.recommendation.service.RecommendationService;
import com.movie.recommendation.parser.MovieParser;
import com.movie.recommendation.model.Movie;
import com.movie.recommendation.validator.MovieValidator;
import com.movie.recommendation.parser.UserParser;
import com.movie.recommendation.model.User;
import com.movie.recommendation.validator.UserValidator;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertThrows;
import com.movie.recommendation.exception.ValidationException;
import static org.junit.jupiter.api.Assertions.*;
/**
 *
 * @author Mina_Antony
 */
public class Bottom_Up {
    
    @Test
    void testMovieParserWithValidator_ValidInput() throws Exception {

        Path tempFile = Files.createTempFile("movies", ".txt");

        String movies =
            "The Dark Knight,TDK123\n" +
            "action,thriller\n";

        Files.writeString(tempFile, movies);

        MovieValidator validator = new MovieValidator();
        MovieParser parser = new MovieParser(validator);
        List<Movie> movieList = parser.parseMovies(tempFile.toString());

        assertEquals(1, movieList.size());
        assertEquals("The Dark Knight", movieList.get(0).getTitle());
    }
    
    @Test
    void testMovieParserWithValidator_InvalidTitle() throws Exception {

        Path tempFile = Files.createTempFile("movies", ".txt");

        String movies =
            "the dark knight,TDK123\n" +  // invalid title
            "action,thriller\n";

        Files.writeString(tempFile, movies);

        MovieValidator validator = new MovieValidator();
        MovieParser parser = new MovieParser(validator);

        ValidationException exception = assertThrows(
            ValidationException.class,
            () -> parser.parseMovies(tempFile.toString())
        );

        assertTrue(exception.getMessage().contains("Movie Title"));
    }
    
     @Test
    void testParserToService_ValidInput() {

        Movie movie1 = new Movie("The Dark Knight", "TDK123", List.of("action", "thriller"));
        Movie movie2 = new Movie("The Godfather", "TG456", List.of("crime", "drama"));
        Movie movie3 = new Movie("Inception", "IN456", List.of("action", "sci-fi"));
        List<Movie> movieList = List.of(movie1, movie2, movie3);

        User user = new User("John Doe", "123456789", List.of("TDK123")); // Already liked The Dark Knight
        List<User> userList = List.of(user);


        RecommendationService service = new RecommendationService();


        Map<User, List<String>> allRecommendations = service.generateRecommendationsForAllUsers(userList, movieList);

        List<String> johnRecs = allRecommendations.get(user); 
        assertNotNull(johnRecs, "Recommendations should not be null");

        assertTrue(johnRecs.contains("Inception"), "Should recommend Inception");
        assertFalse(johnRecs.contains("The Godfather"), "Should not recommend The Godfather (different genre)");
        assertFalse(johnRecs.contains("The Dark Knight"), "Should NOT recommend already liked movie");
    }
    
    
    @Test
void testFullFlowValidatorParserService_validINPUTS() throws Exception {

    MovieValidator movieValidator = new MovieValidator();
    UserValidator userValidator = new UserValidator();

    Path moviesFile = Files.createTempFile("movies", ".txt");
    String movies = "The Dark Knight,TDK123\naction,thriller\n" +
                    "Inception,I456\naction,sci-fi\n";   
    Files.writeString(moviesFile, movies);

    Path usersFile = Files.createTempFile("users", ".txt");
    String users = "John Doe,123456789\nTDK123\n";
    Files.writeString(usersFile, users);

    MovieParser movieParser = new MovieParser(movieValidator);
    UserParser userParser = new UserParser(userValidator);

    List<Movie> moviesList = movieParser.parseMovies(moviesFile.toString());
    List<User> usersList = userParser.parseUsers(usersFile.toString());

    RecommendationService service = new RecommendationService();
    Map<User, List<String>> recs = service.generateRecommendationsForAllUsers(usersList, moviesList);

    User john = usersList.get(0);
    List<String> johnRecs = recs.get(john);
    assertNotNull(johnRecs);
    assertTrue(johnRecs.contains("Inception"));
    assertFalse(johnRecs.contains("The Dark Knight")); 
}



    @Test
void testFullFlowValidatorParserService_InValidInputs() throws Exception {
    MovieValidator movieValidator = new MovieValidator();
    UserValidator userValidator = new UserValidator();

    Path moviesFile = Files.createTempFile("movies", ".txt");
    String movies = "The Dark Knight,TDK123\naction,thriller\n" +
                    "Inception,IN456\naction,sci-fi\n";   // HERE throw Exception bec IN456 IS wrong ID which ensures fill intergration works
    Files.writeString(moviesFile, movies);

    Path usersFile = Files.createTempFile("users", ".txt");
    String users = "John Doe,123456789\nTDK123\n";
    Files.writeString(usersFile, users);

    MovieParser movieParser = new MovieParser(movieValidator);
    UserParser userParser = new UserParser(userValidator);

    List<Movie> moviesList = movieParser.parseMovies(moviesFile.toString());
    List<User> usersList = userParser.parseUsers(usersFile.toString());

    RecommendationService service = new RecommendationService();
    Map<User, List<String>> recs = service.generateRecommendationsForAllUsers(usersList, moviesList);

    User john = usersList.get(0);
    List<String> johnRecs = recs.get(john);
    assertNotNull(johnRecs);
    assertTrue(johnRecs.contains("Inception"));
    assertFalse(johnRecs.contains("The Dark Knight")); 
}

    
    
    
}
