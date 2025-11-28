/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package com.movie.recommendation.model;

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
public class MovieTest {
    
    @Test
    public void testMovieCreation() {
        List<String> genres = Arrays.asList("action", "thriller");
        Movie movie = new Movie("The Dark Knight", "TDK123", genres);
        
        assertEquals("The Dark Knight", movie.getTitle());
        assertEquals("TDK123", movie.getId());
        assertEquals(2, movie.getGenres().size());
    }
    
    @Test
    public void testHasGenre_Exists() {
        List<String> genres = Arrays.asList("action", "thriller");
        Movie movie = new Movie("The Dark Knight", "TDK123", genres);
        
        assertTrue(movie.hasGenre("action"));
        assertTrue(movie.hasGenre("thriller"));
    }
    
    @Test
    public void testHasGenre_NotExists() {
        List<String> genres = Arrays.asList("action", "thriller");
        Movie movie = new Movie("The Dark Knight", "TDK123", genres);
        
        assertFalse(movie.hasGenre("comedy"));
        assertFalse(movie.hasGenre("drama"));
    }
    
    @Test
    public void testHasGenre_CaseInsensitive() {
        List<String> genres = Arrays.asList("action", "thriller");
        Movie movie = new Movie("The Dark Knight", "TDK123", genres);
        
        assertTrue(movie.hasGenre("ACTION"));
        assertTrue(movie.hasGenre("Action"));
        assertTrue(movie.hasGenre("THRILLER"));
    }
    
    @Test
    public void testEquals_SameId() {
        Movie movie1 = new Movie("The Dark Knight", "TDK123", Arrays.asList("action"));
        Movie movie2 = new Movie("Different Title", "TDK123", Arrays.asList("drama"));
        
        assertEquals(movie1, movie2); // Same ID means equal
    }
    
    @Test
    public void testEquals_DifferentId() {
        Movie movie1 = new Movie("The Dark Knight", "TDK123", Arrays.asList("action"));
        Movie movie2 = new Movie("The Dark Knight", "TDK456", Arrays.asList("action"));
        
        assertNotEquals(movie1, movie2); // Different ID means not equal
    }
}