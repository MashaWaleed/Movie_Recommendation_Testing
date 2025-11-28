/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package com.movie.recommendation.service;
import com.movie.recommendation.model.Movie;
import com.movie.recommendation.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Arrays;
import java.util.List;
/**
 *
 * @author Mina_Antony
 */
public class RecommendationServiceTest {
    
    private RecommendationService service;
    
    @BeforeEach
    public void setUp() {
        service = new RecommendationService();
    }
    
        @Test
    public void testGetRecommendations_SingleGenre() {
        List<Movie> movies = Arrays.asList(
            new Movie("Movie A", "MA123", Arrays.asList("action")),
            new Movie("Movie B", "MB456", Arrays.asList("action")),
            new Movie("Movie C", "MC789", Arrays.asList("drama"))
        );
        
        User user = new User("John Doe", "123456789", Arrays.asList("MA123"));
        
        List<String> recommendations = service.getRecommendations(user, movies);
        
        assertEquals(1, recommendations.size());
        assertTrue(recommendations.contains("Movie B"));
        assertFalse(recommendations.contains("Movie A")); // Already liked
        assertFalse(recommendations.contains("Movie C")); // Different genre
    }
    
    @Test
    public void testGetRecommendations_MultipleGenres() {
        List<Movie> movies = Arrays.asList(
            new Movie("Movie A", "MA123", Arrays.asList("action")),
            new Movie("Movie B", "MB456", Arrays.asList("action")),
            new Movie("Movie C", "MC789", Arrays.asList("drama")),
            new Movie("Movie D", "MD012", Arrays.asList("drama")),
            new Movie("Movie E", "ME345", Arrays.asList("comedy"))
        );
        
        User user = new User("John", "123456789", Arrays.asList("MA123", "MC789"));
        
        List<String> recommendations = service.getRecommendations(user, movies);
        assertEquals(2, recommendations.size());
        assertTrue(recommendations.contains("Movie B")); // Same genre as Movie A
        assertTrue(recommendations.contains("Movie D")); // Same genre as Movie C
        assertFalse(recommendations.contains("Movie A")); // Already liked
        assertFalse(recommendations.contains("Movie C")); // Already liked
        assertFalse(recommendations.contains("Movie E")); // Different genre
    }
    
    @Test
    public void testGetRecommendations_NoOtherMoviesInGenre() {
        List<Movie> movies = Arrays.asList(
            new Movie("Movie A", "MA123", Arrays.asList("action")),
            new Movie("Movie B", "MB456", Arrays.asList("drama"))
        );
        
        User user = new User("John", "123456789", Arrays.asList("MA123"));
        
        List<String> recommendations = service.getRecommendations(user, movies);
        
        assertEquals(0, recommendations.size());
    }
    
    @Test
    public void testGetRecommendations_EmptyLikedMovies() {
        List<Movie> movies = Arrays.asList(
            new Movie("Movie A", "MA123", Arrays.asList("action"))
        );
        
        User user = new User("John", "123456789", Arrays.asList());
        
        List<String> recommendations = service.getRecommendations(user, movies);
        
        assertEquals(0, recommendations.size());
    }
    
    @Test
    public void testGetRecommendations_MovieWithMultipleGenres() {
        List<Movie> movies = Arrays.asList(
            new Movie("Movie A", "MA123", Arrays.asList("action", "thriller")),
            new Movie("Movie B", "MB456", Arrays.asList("action")),
            new Movie("Movie C", "MC789", Arrays.asList("thriller")),
            new Movie("Movie D", "MD012", Arrays.asList("drama"))
        );
        
        User user = new User("John", "123456789", Arrays.asList("MA123"));
        
        List<String> recommendations = service.getRecommendations(user, movies);
        assertEquals(2, recommendations.size());
        assertTrue(recommendations.contains("Movie B")); // Matches action
        assertTrue(recommendations.contains("Movie C")); // Matches thriller
        assertFalse(recommendations.contains("Movie D")); // No match
    }
    
    @Test
    public void testFormatRecommendations_WithMovies() {
        User user = new User("John Doe", "123456789", Arrays.asList());
        List<String> recommendations = Arrays.asList("Movie A", "Movie B", "Movie C");
        
        String formatted = service.formatRecommendations(user, recommendations);
        
        assertTrue(formatted.contains("John Doe,123456789"));
        assertTrue(formatted.contains("Movie A,Movie B,Movie C"));
    }
    
    @Test
    public void testFormatRecommendations_NoMovies() {
        User user = new User("John Doe", "123456789", Arrays.asList());
        List<String> recommendations = Arrays.asList();
        
        String formatted = service.formatRecommendations(user, recommendations);
        
        assertTrue(formatted.contains("John Doe,123456789"));
        String[] lines = formatted.split("\n");
        assertEquals(3, lines.length); // Name line, empty recommendations line, final newline
    }
}