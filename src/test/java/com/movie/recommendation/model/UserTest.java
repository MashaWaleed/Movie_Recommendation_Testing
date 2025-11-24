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
public class UserTest {
    
    @Test
    public void testUserCreation() {
        List<String> likedMovies = Arrays.asList("TDK123", "I456");
        User user = new User("John Doe", "123456789", likedMovies);
        
        assertEquals("John Doe", user.getName());
        assertEquals("123456789", user.getId());
        assertEquals(2, user.getLikedMovieIds().size());
    }
    
    @Test
    public void testHasLikedMovie_Exists() {
        List<String> likedMovies = Arrays.asList("TDK123", "I456");
        User user = new User("John Doe", "123456789", likedMovies);
        
        assertTrue(user.hasLikedMovie("TDK123"));
        assertTrue(user.hasLikedMovie("I456"));
    }
    
    @Test
    public void testHasLikedMovie_NotExists() {
        List<String> likedMovies = Arrays.asList("TDK123", "I456");
        User user = new User("John Doe", "123456789", likedMovies);
        
        assertFalse(user.hasLikedMovie("TG789"));
        assertFalse(user.hasLikedMovie("XYZ999"));
    }
    
    @Test
    public void testHasLikedMovie_EmptyList() {
        User user = new User("John Doe", "123456789", Arrays.asList());
        
        assertFalse(user.hasLikedMovie("TDK123"));
    }
}
