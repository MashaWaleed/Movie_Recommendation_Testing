package com.movie.recommendation.Data_Flow_Testing;
import com.movie.recommendation.parser.MovieParser;
import com.movie.recommendation.model.Movie;
import com.movie.recommendation.model.User;
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
import com.movie.recommendation.service.RecommendationService;
/**
 *
 * @author Mina_Antony
 */
public class GetRecommendations_Test {

    private RecommendationService service;

    @BeforeEach
    public void setUp() {
        service = new RecommendationService();
    }

    // Scenario 1: Single genre match (already exists)
    @Test
    public void testGetRecommendations_SingleGenre() {
        List<Movie> movies = Arrays.asList(
            new Movie("Movie A", "MA123", Arrays.asList("action")),
            new Movie("Movie B", "MB456", Arrays.asList("action")),
            new Movie("Movie C", "MC789", Arrays.asList("drama"))
        );

        User user = new User("John Doe", "123456789", Arrays.asList("MA123"));

        List<String> recommendations = service.getRecommendations(user, movies);

        assertEquals(1, recommendations.size(), "Expected only one recommendation");
        assertTrue(recommendations.contains("Movie B"), "Movie B should be recommended");
        assertFalse(recommendations.contains("Movie A"), "Movie A should not be recommended (already liked)");
        assertFalse(recommendations.contains("Movie C"), "Movie C should not be recommended (genre mismatch)");
    }

    // Scenario 2: User liked no movies
    @Test
    public void testGetRecommendations_NoLikedMovies() {
        List<Movie> movies = Arrays.asList(
            new Movie("Movie A", "MA123", Arrays.asList("action")),
            new Movie("Movie B", "MB456", Arrays.asList("comedy"))
        );

        User user = new User("John Doe", "123456789", Arrays.asList());

        List<String> recommendations = service.getRecommendations(user, movies);

        assertEquals(0, recommendations.size(), "Expected no recommendations when user liked no movies");
    }

    // Scenario 3: User liked all movies
    @Test
    public void testGetRecommendations_AllLikedMovies() {
        List<Movie> movies = Arrays.asList(
            new Movie("Movie A", "MA123", Arrays.asList("action")),
            new Movie("Movie B", "MB456", Arrays.asList("comedy"))
        );

        User user = new User("John Doe", "123456789", Arrays.asList("MA123", "MB456"));

        List<String> recommendations = service.getRecommendations(user, movies);

        assertEquals(0, recommendations.size(), "Expected no recommendations when user liked all movies");
    }

    // Scenario 4: User liked some movies but no genre matches
    @Test
    public void testGetRecommendations_NoGenreMatch() {
        List<Movie> movies = Arrays.asList(
            new Movie("Movie A", "MA123", Arrays.asList("drama")),
            new Movie("Movie B", "MB456", Arrays.asList("action"))
        );

        User user = new User("John Doe", "123456789", Arrays.asList("MA123"));

        List<String> recommendations = service.getRecommendations(user, movies);

        assertEquals(0, recommendations.size(), "Expected no recommendations when no genres match");
    }

    // Scenario 5: Multiple genre matches
    @Test
    public void testGetRecommendations_MultipleGenreMatches() {
        List<Movie> movies = Arrays.asList(
            new Movie("Movie A", "MA123", Arrays.asList("action", "thriller")),
            new Movie("Movie B", "MB456", Arrays.asList("action", "comedy")),
            new Movie("Movie C", "MC789", Arrays.asList("thriller", "horror")),
            new Movie("Movie D", "MD101", Arrays.asList("romance"))
        );

        User user = new User("John Doe", "123456789", Arrays.asList("MA123"));

        List<String> recommendations = service.getRecommendations(user, movies);

        assertEquals(2, recommendations.size(), "Expected two recommendations");
        assertTrue(recommendations.contains("Movie B"), "Movie B should be recommended");
        assertTrue(recommendations.contains("Movie C"), "Movie C should be recommended");
        assertFalse(recommendations.contains("Movie A"), "Movie A should not be recommended (already liked)");
        assertFalse(recommendations.contains("Movie D"), "Movie D should not be recommended (no matching genre)");
    }
}
