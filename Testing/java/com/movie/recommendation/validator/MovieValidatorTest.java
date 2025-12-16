/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package com.movie.recommendation.validator;
import com.movie.recommendation.exception.ValidationException;
import com.movie.recommendation.exception.ValidationException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Mina_Antony
 */


public class MovieValidatorTest {
    
    private MovieValidator validator;
    
    @BeforeEach
    public void setUp() {
        validator = new MovieValidator();
    }
    
     // ==========================
    // Movie Title Tests
    // ==========================
    
    @Test
    public void testValidateTitle_ValidMultipleWords() throws ValidationException {
        validator.validateTitle("The Dark Knight");
        // If no exception, test passes
    }

    
    @Test
    public void testValidateTitle_StartsWithLowercase() {
        String movieTitle = "the Dark Knight";
        ValidationException ex = assertThrows(ValidationException.class, () -> {
            validator.validateTitle(movieTitle);
        });
        String expectedMessage = "ERROR: Movie Title {" + movieTitle + "} is wrong";
        System.out.println("Expected: " + expectedMessage);
        System.out.println("Actual:   " + ex.getMessage());
        assertEquals(expectedMessage, ex.getMessage());
    }


    
    @Test
    public void testValidateTitle_MiddleWordLowercase() {
        String movieTitle = "The dark Knight";

        ValidationException ex = assertThrows(ValidationException.class, () -> {
            validator.validateTitle(movieTitle);
        });

        // Include curly braces around the movie title
        String expectedMessage = "ERROR: Movie Title {" + movieTitle + "} is wrong";

        // Print expected vs actual
        System.out.println("Expected: " + expectedMessage);
        System.out.println("Actual:   " + ex.getMessage());

        // Assert that the message matches exactly
        assertEquals(expectedMessage, ex.getMessage());
    }

    
    
    @Test
    public void testValidateTitle_ValidWithNumbers(){
        String movieTitle = "2001 A Space Odyssey";

        ValidationException ex = assertThrows(ValidationException.class, () -> {
            validator.validateTitle(movieTitle);
        });

        // Include curly braces around the movie title
        String expectedMessage = "ERROR: Movie Title {" + movieTitle + "} is wrong";

        // Print expected vs actual
        System.out.println("Expected: " + expectedMessage);
        System.out.println("Actual:   " + ex.getMessage());

        // Assert that the message matches exactly
        assertEquals(expectedMessage, ex.getMessage());
    } 
    
    


    @Test
    public void testValidateTitle_ValidSingleWord() throws ValidationException {
        validator.validateTitle("Inception");
        // If no exception, test passes
    }
    
 



    // ==========================
    // Movie ID Tests
    // ==========================

    @Test
    public void testValidateId_Correct() throws ValidationException {
        validator.validateId("TDK123", "The Dark Knight");
    }
    

    @Test
    public void testValidateId_WrongLetters() {
        String movieTitle = "The Dark Knight";
        String movieId = "ABC123";
        ValidationException ex = assertThrows(ValidationException.class, () -> {
            validator.validateId(movieId, movieTitle);
        });
        String expectedMessage = "ERROR: Movie Id letters {" + movieId + "} are wrong";
        System.out.println("Expected: " + expectedMessage);
        System.out.println("Actual:   " + ex.getMessage());
        assertEquals(expectedMessage, ex.getMessage());
    }

    @Test
    public void testValidateId_AllSameNumbers() {
        String movieTitle = "The Dark Knight";
        String movieId = "TDK111";
        ValidationException ex = assertThrows(ValidationException.class, () -> {
            validator.validateId(movieId, movieTitle);
        });
        String expectedMessage = "ERROR: Movie Id numbers {" + movieId + "} aren't unique";
        System.out.println("Expected: " + expectedMessage);
        System.out.println("Actual:   " + ex.getMessage());
        assertEquals(expectedMessage, ex.getMessage());
    }

    @Test
    public void testValidateId_TwoDigits() {
        String movieTitle = "The Dark Knight";
        String movieId = "TDK12";
        ValidationException ex = assertThrows(ValidationException.class, () -> {
            validator.validateId(movieId, movieTitle);
        });
        String expectedMessage = "ERROR: Movie Id numbers {" + movieId + "} aren't unique";
        System.out.println("Expected: " + expectedMessage);
        System.out.println("Actual:   " + ex.getMessage());
        assertEquals(expectedMessage, ex.getMessage());
    }

    @Test
    public void testValidateId_FourDigits() {
        String movieTitle = "The Dark Knight";
        String movieId = "TDK1234";
        ValidationException ex = assertThrows(ValidationException.class, () -> {
            validator.validateId(movieId, movieTitle);
        });
        String expectedMessage = "ERROR: Movie Id numbers {" + movieId + "} aren't unique";
        System.out.println("Expected: " + expectedMessage);
        System.out.println("Actual:   " + ex.getMessage());
        assertEquals(expectedMessage, ex.getMessage());
    }

}


