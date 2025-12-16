# Testing Guide for Movie Recommendation System

## Overview
This document provides comprehensive guidance on how to test the Movie Recommendation System application. While we are not implementing tests in this project, this guide explains exactly how future testers can approach unit and integration testing.

---

## Table of Contents
1. [Unit Testing](#unit-testing)
2. [Integration Testing](#integration-testing)
3. [Test Data Preparation](#test-data-preparation)
4. [Testing Tools and Setup](#testing-tools-and-setup)
5. [Test Execution](#test-execution)
6. [Common Test Scenarios](#common-test-scenarios)

---

## Unit Testing

Unit tests focus on testing individual components in isolation.

### 1. Testing Validators

#### MovieValidator Tests

Create a test class `MovieValidatorTest.java` in `src/test/java/com/movie/recommendation/validator/`:

```java
package com.movie.recommendation.validator;

import com.movie.recommendation.exception.ValidationException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MovieValidatorTest {
    
    private MovieValidator validator;
    
    @Before
    public void setUp() {
        validator = new MovieValidator();
    }
    
    // Test valid movie titles
    @Test
    public void testValidateTitle_ValidSingleWord() throws ValidationException {
        validator.validateTitle("Inception");
        // If no exception, test passes
    }
    
    @Test
    public void testValidateTitle_ValidMultipleWords() throws ValidationException {
        validator.validateTitle("The Dark Knight");
    }
    
    @Test
    public void testValidateTitle_ValidWithNumbers() throws ValidationException {
        validator.validateTitle("2001 A Space Odyssey");
    }
    
    // Test invalid movie titles
    @Test(expected = ValidationException.class)
    public void testValidateTitle_StartsWithLowercase() throws ValidationException {
        validator.validateTitle("the Dark Knight");
    }
    
    @Test(expected = ValidationException.class)
    public void testValidateTitle_MiddleWordLowercase() throws ValidationException {
        validator.validateTitle("The dark Knight");
    }
    
    @Test(expected = ValidationException.class)
    public void testValidateTitle_EmptyString() throws ValidationException {
        validator.validateTitle("");
    }
    
    @Test(expected = ValidationException.class)
    public void testValidateTitle_Null() throws ValidationException {
        validator.validateTitle(null);
    }
    
    // Test valid movie IDs
    @Test
    public void testValidateId_Correct() throws ValidationException {
        validator.validateId("TDK123", "The Dark Knight");
    }
    
    @Test
    public void testValidateId_CorrectWithMoreCapitals() throws ValidationException {
        validator.validateId("ASIBO456", "A Star Is Born Once");
    }
    
    // Test invalid movie IDs - wrong letters
    @Test(expected = ValidationException.class)
    public void testValidateId_WrongLetters() throws ValidationException {
        validator.validateId("ABC123", "The Dark Knight");
    }
    
    @Test(expected = ValidationException.class)
    public void testValidateId_MissingLetters() throws ValidationException {
        validator.validateId("TD123", "The Dark Knight");
    }
    
    @Test(expected = ValidationException.class)
    public void testValidateId_ExtraLetters() throws ValidationException {
        validator.validateId("TDKR123", "The Dark Knight");
    }
    
    // Test invalid movie IDs - non-unique numbers
    @Test(expected = ValidationException.class)
    public void testValidateId_AllSameNumbers() throws ValidationException {
        validator.validateId("TDK111", "The Dark Knight");
    }
    
    @Test(expected = ValidationException.class)
    public void testValidateId_TwoSameNumbers() throws ValidationException {
        validator.validateId("TDK112", "The Dark Knight");
    }
    
    // Test invalid movie IDs - wrong number count
    @Test(expected = ValidationException.class)
    public void testValidateId_TwoDigits() throws ValidationException {
        validator.validateId("TDK12", "The Dark Knight");
    }
    
    @Test(expected = ValidationException.class)
    public void testValidateId_FourDigits() throws ValidationException {
        validator.validateId("TDK1234", "The Dark Knight");
    }
    
    @Test(expected = ValidationException.class)
    public void testValidateId_NoDigits() throws ValidationException {
        validator.validateId("TDK", "The Dark Knight");
    }
}
```

#### UserValidator Tests

Create `UserValidatorTest.java`:

```java
package com.movie.recommendation.validator;

import com.movie.recommendation.exception.ValidationException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class UserValidatorTest {
    
    private UserValidator validator;
    
    @Before
    public void setUp() {
        validator = new UserValidator();
    }
    
    // Test valid user names
    @Test
    public void testValidateName_ValidSingleWord() throws ValidationException {
        validator.validateName("John");
    }
    
    @Test
    public void testValidateName_ValidMultipleWords() throws ValidationException {
        validator.validateName("John Doe");
    }
    
    @Test
    public void testValidateName_ValidThreeWords() throws ValidationException {
        validator.validateName("Mary Jane Watson");
    }
    
    // Test invalid user names
    @Test(expected = ValidationException.class)
    public void testValidateName_StartsWithSpace() throws ValidationException {
        validator.validateName(" John Doe");
    }
    
    @Test(expected = ValidationException.class)
    public void testValidateName_ContainsNumbers() throws ValidationException {
        validator.validateName("John123");
    }
    
    @Test(expected = ValidationException.class)
    public void testValidateName_ContainsSpecialChars() throws ValidationException {
        validator.validateName("John@Doe");
    }
    
    @Test(expected = ValidationException.class)
    public void testValidateName_Empty() throws ValidationException {
        validator.validateName("");
    }
    
    @Test(expected = ValidationException.class)
    public void testValidateName_Null() throws ValidationException {
        validator.validateName(null);
    }
    
    // Test valid user IDs
    @Test
    public void testValidateId_NineDigits() throws ValidationException {
        validator.validateId("123456789");
    }
    
    @Test
    public void testValidateId_EightDigitsOneLetter() throws ValidationException {
        validator.validateId("12345678A");
    }
    
    @Test
    public void testValidateId_DifferentLetters() throws ValidationException {
        validator.validateId("12345678Z");
        validator.reset();
        validator.validateId("12345678B");
    }
    
    // Test invalid user IDs
    @Test(expected = ValidationException.class)
    public void testValidateId_StartsWithLetter() throws ValidationException {
        validator.validateId("A12345678");
    }
    
    @Test(expected = ValidationException.class)
    public void testValidateId_TwoLettersAtEnd() throws ValidationException {
        validator.validateId("1234567AB");
    }
    
    @Test(expected = ValidationException.class)
    public void testValidateId_LetterInMiddle() throws ValidationException {
        validator.validateId("1234A5678");
    }
    
    @Test(expected = ValidationException.class)
    public void testValidateId_TooShort() throws ValidationException {
        validator.validateId("12345678");
    }
    
    @Test(expected = ValidationException.class)
    public void testValidateId_TooLong() throws ValidationException {
        validator.validateId("1234567890");
    }
    
    @Test(expected = ValidationException.class)
    public void testValidateId_LowercaseLetter() throws ValidationException {
        validator.validateId("12345678a");
    }
    
    // Test duplicate user IDs
    @Test(expected = ValidationException.class)
    public void testCheckUniqueUserId_Duplicate() throws ValidationException {
        validator.validateUser("John Doe", "123456789");
        validator.validateUser("Jane Smith", "123456789"); // Should fail
    }
    
    @Test
    public void testCheckUniqueUserId_Different() throws ValidationException {
        validator.validateUser("John Doe", "123456789");
        validator.validateUser("Jane Smith", "987654321");
    }
}
```

### 2. Testing Models

#### Movie Model Tests

Create `MovieTest.java`:

```java
package com.movie.recommendation.model;

import org.junit.Test;
import java.util.Arrays;
import java.util.List;
import static org.junit.Assert.*;

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
```

#### User Model Tests

Create `UserTest.java`:

```java
package com.movie.recommendation.model;

import org.junit.Test;
import java.util.Arrays;
import java.util.List;
import static org.junit.Assert.*;

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
```

### 3. Testing Service Layer

#### RecommendationService Tests

Create `RecommendationServiceTest.java`:

```java
package com.movie.recommendation.service;

import com.movie.recommendation.model.Movie;
import com.movie.recommendation.model.User;
import org.junit.Before;
import org.junit.Test;
import java.util.Arrays;
import java.util.List;
import static org.junit.Assert.*;

public class RecommendationServiceTest {
    
    private RecommendationService service;
    
    @Before
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
```

---

## Integration Testing

Integration tests verify that multiple components work together correctly.

### Complete Application Flow Tests

Create `MovieRecommendationIntegrationTest.java`:

```java
package com.movie.recommendation;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import static org.junit.Assert.*;

public class MovieRecommendationIntegrationTest {
    
    private static final String TEST_INPUT_DIR = "test_input/";
    private static final String TEST_OUTPUT_DIR = "test_output/";
    
    @Before
    public void setUp() {
        new File(TEST_INPUT_DIR).mkdirs();
        new File(TEST_OUTPUT_DIR).mkdirs();
    }
    
    @After
    public void tearDown() {
        // Clean up test files
        deleteDirectory(new File(TEST_INPUT_DIR));
        deleteDirectory(new File(TEST_OUTPUT_DIR));
    }
    
    @Test
    public void testCompleteFlow_ValidInputs() throws Exception {
        // Create test movies.txt
        String moviesContent = 
            "The Dark Knight,TDK123\n" +
            "action,thriller\n" +
            "The Godfather,TG456\n" +
            "crime,drama\n" +
            "Inception,I789\n" +
            "action,sci-fi\n";
        Files.write(Paths.get(TEST_INPUT_DIR + "movies.txt"), moviesContent.getBytes());
        
        // Create test users.txt
        String usersContent = 
            "John Doe,123456789\n" +
            "TDK123\n";
        Files.write(Paths.get(TEST_INPUT_DIR + "users.txt"), usersContent.getBytes());
        
        // Run the application
        Main.processFiles(
            TEST_INPUT_DIR + "movies.txt",
            TEST_INPUT_DIR + "users.txt",
            TEST_OUTPUT_DIR + "recommendations.txt"
        );
        
        // Verify output
        String output = new String(Files.readAllBytes(
            Paths.get(TEST_OUTPUT_DIR + "recommendations.txt")));
        
        assertTrue(output.contains("John Doe,123456789"));
        assertTrue(output.contains("Inception")); // Same action genre
        assertFalse(output.contains("The Dark Knight")); // Already liked
    }
    
    @Test
    public void testCompleteFlow_MovieTitleError() throws Exception {
        String moviesContent = 
            "the dark knight,TDK123\n" + // Invalid title
            "action,thriller\n";
        Files.write(Paths.get(TEST_INPUT_DIR + "movies.txt"), moviesContent.getBytes());
        
        String usersContent = "John Doe,123456789\nTDK123\n";
        Files.write(Paths.get(TEST_INPUT_DIR + "users.txt"), usersContent.getBytes());
        
        Main.processFiles(
            TEST_INPUT_DIR + "movies.txt",
            TEST_INPUT_DIR + "users.txt",
            TEST_OUTPUT_DIR + "recommendations.txt"
        );
        
        String output = new String(Files.readAllBytes(
            Paths.get(TEST_OUTPUT_DIR + "recommendations.txt")));
        
        assertTrue(output.contains("Error"));
        assertTrue(output.contains("ERROR: Movie Title {the dark knight} is wrong"));
    }
    
    @Test
    public void testCompleteFlow_MovieIdLettersError() throws Exception {
        String moviesContent = 
            "The Dark Knight,ABC123\n" + // Wrong letters in ID
            "action,thriller\n";
        Files.write(Paths.get(TEST_INPUT_DIR + "movies.txt"), moviesContent.getBytes());
        
        String usersContent = "John Doe,123456789\nABC123\n";
        Files.write(Paths.get(TEST_INPUT_DIR + "users.txt"), usersContent.getBytes());
        
        Main.processFiles(
            TEST_INPUT_DIR + "movies.txt",
            TEST_INPUT_DIR + "users.txt",
            TEST_OUTPUT_DIR + "recommendations.txt"
        );
        
        String output = new String(Files.readAllBytes(
            Paths.get(TEST_OUTPUT_DIR + "recommendations.txt")));
        
        assertTrue(output.contains("Error"));
        assertTrue(output.contains("ERROR: Movie Id letters {ABC123} are wrong"));
    }
    
    @Test
    public void testCompleteFlow_MovieIdNumbersError() throws Exception {
        String moviesContent = 
            "The Dark Knight,TDK111\n" + // Non-unique numbers
            "action,thriller\n";
        Files.write(Paths.get(TEST_INPUT_DIR + "movies.txt"), moviesContent.getBytes());
        
        String usersContent = "John Doe,123456789\nTDK111\n";
        Files.write(Paths.get(TEST_INPUT_DIR + "users.txt"), usersContent.getBytes());
        
        Main.processFiles(
            TEST_INPUT_DIR + "movies.txt",
            TEST_INPUT_DIR + "users.txt",
            TEST_OUTPUT_DIR + "recommendations.txt"
        );
        
        String output = new String(Files.readAllBytes(
            Paths.get(TEST_OUTPUT_DIR + "recommendations.txt")));
        
        assertTrue(output.contains("Error"));
        assertTrue(output.contains("ERROR: Movie Id numbers {TDK111} aren't unique"));
    }
    
    @Test
    public void testCompleteFlow_UserNameError() throws Exception {
        String moviesContent = 
            "The Dark Knight,TDK123\n" +
            "action,thriller\n";
        Files.write(Paths.get(TEST_INPUT_DIR + "movies.txt"), moviesContent.getBytes());
        
        String usersContent = 
            " John Doe,123456789\n" + // Name starts with space
            "TDK123\n";
        Files.write(Paths.get(TEST_INPUT_DIR + "users.txt"), usersContent.getBytes());
        
        Main.processFiles(
            TEST_INPUT_DIR + "movies.txt",
            TEST_INPUT_DIR + "users.txt",
            TEST_OUTPUT_DIR + "recommendations.txt"
        );
        
        String output = new String(Files.readAllBytes(
            Paths.get(TEST_OUTPUT_DIR + "recommendations.txt")));
        
        assertTrue(output.contains("Error"));
        assertTrue(output.contains("ERROR: User Name { John Doe} is wrong"));
    }
    
    @Test
    public void testCompleteFlow_UserIdError() throws Exception {
        String moviesContent = 
            "The Dark Knight,TDK123\n" +
            "action,thriller\n";
        Files.write(Paths.get(TEST_INPUT_DIR + "movies.txt"), moviesContent.getBytes());
        
        String usersContent = 
            "John Doe,12345678\n" + // Only 8 characters
            "TDK123\n";
        Files.write(Paths.get(TEST_INPUT_DIR + "users.txt"), usersContent.getBytes());
        
        Main.processFiles(
            TEST_INPUT_DIR + "movies.txt",
            TEST_INPUT_DIR + "users.txt",
            TEST_OUTPUT_DIR + "recommendations.txt"
        );
        
        String output = new String(Files.readAllBytes(
            Paths.get(TEST_OUTPUT_DIR + "recommendations.txt")));
        
        assertTrue(output.contains("Error"));
        assertTrue(output.contains("ERROR: User Id {12345678} is wrong"));
    }
    
    private void deleteDirectory(File directory) {
        if (directory.exists()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        deleteDirectory(file);
                    } else {
                        file.delete();
                    }
                }
            }
            directory.delete();
        }
    }
}
```

---

## Test Data Preparation

### Valid Test Data

Create test files with valid data for positive testing.

### Invalid Test Data

Create separate test files for each validation rule:

1. **invalid-movie-title-lowercase.txt**
2. **invalid-movie-id-letters.txt**
3. **invalid-movie-id-numbers-duplicate.txt**
4. **invalid-user-name-space.txt**
5. **invalid-user-id-length.txt**
6. **invalid-user-id-duplicate.txt**

---

## Testing Tools and Setup

### Required Dependencies

Add to `pom.xml` (Maven):
```xml
<dependencies>
    <dependency>
        <groupId>junit</groupId>
        <artifactId>junit</artifactId>
        <version>4.13.2</version>
        <scope>test</scope>
    </dependency>
</dependencies>
```

### Directory Structure for Tests

```
src/
├── main/
│   └── java/
│       └── com/movie/recommendation/
└── test/
    ├── java/
    │   └── com/movie/recommendation/
    │       ├── validator/
    │       │   ├── MovieValidatorTest.java
    │       │   └── UserValidatorTest.java
    │       ├── model/
    │       │   ├── MovieTest.java
    │       │   └── UserTest.java
    │       ├── service/
    │       │   └── RecommendationServiceTest.java
    │       └── MovieRecommendationIntegrationTest.java
    └── resources/
        └── test-data/
            ├── valid/
            └── invalid/
```

---

## Test Execution

### Using Maven

```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=MovieValidatorTest

# Run with coverage
mvn test jacoco:report
```

### Using IDE (IntelliJ/Eclipse)

1. Right-click on test class
2. Select "Run 'TestClassName'"
3. View results in test runner panel

---

## Common Test Scenarios

### Scenario 1: Valid Complete Flow
- All inputs valid
- Verify correct recommendations generated

### Scenario 2: Each Validation Error
- Test each validation rule individually
- Verify correct error message

### Scenario 3: Multiple Errors
- Input with multiple errors
- Verify only first error reported

### Scenario 4: Edge Cases
- Empty files
- Single movie/user
- No matching genres
- All movies already liked

---

## Test Coverage Goals

- **Unit Tests**: 90%+ line coverage
- **Integration Tests**: All main workflows
- **Edge Cases**: All boundary conditions
- **Error Cases**: All validation rules

---

## Best Practices

1. **Arrange-Act-Assert**: Structure each test clearly
2. **One Assertion Per Concept**: Test one thing at a time
3. **Descriptive Names**: Test names should explain what they test
4. **Independent Tests**: Tests shouldn't depend on each other
5. **Clean Up**: Use @Before/@After to set up/tear down
6. **Test Data**: Use realistic but simple test data
7. **Error Messages**: Verify exact error message format

---

This completes the testing documentation. All code includes detailed comments explaining what to test and how to test it.
