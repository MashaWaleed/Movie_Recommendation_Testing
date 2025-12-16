/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package com.movie.recommendation.validator;
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
public class UserValidatorTest {
    
    private UserValidator validator;
    
    @BeforeEach
    public void setUp() {
        validator = new UserValidator();
    }
    
    // =====================================================================
    // USER NAME TESTS
    // =====================================================================

    
    
    @Test
    public void testUserName_Valid() throws ValidationException {
        validator.validateName("John Doe");
        // If no exception, test passes
    }

    
    @Test
    public void testUserName_StartsWithSpace() {
        String username = " John Doe";
        ValidationException ex = assertThrows(ValidationException.class, () -> {
            validator.validateName(username);
        });
        String expectedMessage = "ERROR: User Name {" + username + "} is wrong";
        System.out.println("Expected: " + expectedMessage);
        System.out.println("Actual:   " + ex.getMessage());
        assertEquals(expectedMessage, ex.getMessage());
    }

    @Test
    public void testUserName_HasNumbers() {
        String username = "John Doe123";
        ValidationException ex = assertThrows(ValidationException.class, () -> {
            validator.validateName(username);
        });
        String expectedMessage = "ERROR: User Name {" + username + "} is wrong";
        System.out.println("Expected: " + expectedMessage);
        System.out.println("Actual:   " + ex.getMessage());
        assertEquals(expectedMessage, ex.getMessage());
    }
    
    
    @Test
    public void testUserName_HasUnderscore() {
        String username = "John_Doe";
        ValidationException ex = assertThrows(ValidationException.class, () -> {
            validator.validateName(username);
        });
        String expectedMessage = "ERROR: User Name {" + username + "} is wrong";
        System.out.println("Expected: " + expectedMessage);
        System.out.println("Actual:   " + ex.getMessage());
        assertEquals(expectedMessage, ex.getMessage());
    }
        

    // =====================================================================
    // USER ID TESTS
    // =====================================================================

    
    @Test
    public void testUserId_Valid() throws ValidationException {
        validator.validateId("123456789");
    }
    
    @Test
    public void testUserId_LastCharIsLetter() throws ValidationException {
        validator.validateId("12345678A");
    }

    
    @Test
    public void testUserId_TooShort() {
        String ID = "12345678";
        ValidationException ex = assertThrows(ValidationException.class, () -> {
            validator.validateId(ID);
        });
        String expectedMessage = "ERROR: User Id {" + ID + "} is wrong";
        System.out.println("Expected: " + expectedMessage);
        System.out.println("Actual:   " + ex.getMessage());
        assertEquals(expectedMessage, ex.getMessage());
    }    

    @Test
    public void testUserId_TooLong() {
        String ID = "1234567890";
        ValidationException ex = assertThrows(ValidationException.class, () -> {
            validator.validateId(ID);
        });
        String expectedMessage = "ERROR: User Id {" + ID + "} is wrong";
        System.out.println("Expected: " + expectedMessage);
        System.out.println("Actual:   " + ex.getMessage());
        assertEquals(expectedMessage, ex.getMessage());
    }      

    @Test
    public void testUserId_StartsWithLetter() {
        String ID = "A23456789";
        ValidationException ex = assertThrows(ValidationException.class, () -> {
            validator.validateId(ID);
        });
        String expectedMessage = "ERROR: User Id {" + ID + "} is wrong";
        System.out.println("Expected: " + expectedMessage);
        System.out.println("Actual:   " + ex.getMessage());
        assertEquals(expectedMessage, ex.getMessage());
    }  

    @Test
    public void testUserId_MultipleLetters() {
        String ID = "1234567AB";
        ValidationException ex = assertThrows(ValidationException.class, () -> {
            validator.validateId(ID);
        });
        String expectedMessage = "ERROR: User Id {" + ID + "} is wrong";
        System.out.println("Expected: " + expectedMessage);
        System.out.println("Actual:   " + ex.getMessage());
        assertEquals(expectedMessage, ex.getMessage());
    } 
    


    // =====================================================================
    // FULL USER VALIDATION
    // =====================================================================
    @Test
    public void testValidateUser_Valid() throws ValidationException {
        validator.validateUser("John Doe", "123456789");
    }
        
    @Test
    public void testValidateUser_RepeatedIDS() throws ValidationException {
        validator.validateUser("John Doe", "123456789");
        validator.validateUser("Mina Alfons", "123456789");
    }
}
