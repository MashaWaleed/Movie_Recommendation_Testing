# Movie Recommendation System

## Overview
A Java application that recommends movies to users based on their genre preferences.

## Project Structure
```
Movie_Recommendation_Testing/
├── src/
│   └── main/
│       └── java/
│           └── com/
│               └── movie/
│                   └── recommendation/
│                       ├── Main.java
│                       ├── model/
│                       │   ├── Movie.java
│                       │   └── User.java
│                       ├── validator/
│                       │   ├── MovieValidator.java
│                       │   └── UserValidator.java
│                       ├── parser/
│                       │   ├── MovieParser.java
│                       │   └── UserParser.java
│                       ├── service/
│                       │   └── RecommendationService.java
│                       └── exception/
│                           └── ValidationException.java
├── input/
│   ├── movies.txt
│   └── users.txt
├── output/
│   └── recommendations.txt
└── README.md
```

## Compilation and Execution

### Compile
```bash
javac -d bin src/main/java/com/movie/recommendation/**/*.java src/main/java/com/movie/recommendation/*.java
```

### Run
```bash
java -cp bin com.movie.recommendation.Main
```

## Input File Formats

### movies.txt
```
Movie Title,MOVIEID123
genre1,genre2,genre3
Another Movie Title,AMT456
action,drama
```

### users.txt
```
John Doe,123456789
MOVIEID123,AMT456
Jane Smith,987654321A
MOVIEID123
```

## Testing Documentation

### Unit Testing Strategy

#### 1. **MovieValidator Unit Tests**
Tests to validate movie title and ID validation logic.

**Test Cases:**
- `testValidMovieTitle()` - Test valid titles (e.g., "The Dark Knight")
- `testInvalidMovieTitle_LowercaseStart()` - Test title starting with lowercase (e.g., "the Dark Knight")
- `testInvalidMovieTitle_LowercaseMiddle()` - Test word in middle with lowercase (e.g., "The dark Knight")
- `testValidMovieId()` - Test valid movie IDs (e.g., "TDK123")
- `testInvalidMovieId_WrongLetters()` - Test ID with wrong letters (e.g., "ABC123" for title "The Dark Knight")
- `testInvalidMovieId_NonUniqueNumbers()` - Test ID with duplicate numbers (e.g., "TDK111")
- `testInvalidMovieId_TooFewNumbers()` - Test ID with less than 3 numbers
- `testInvalidMovieId_TooManyNumbers()` - Test ID with more than 3 numbers

**How to implement:**
```java
import org.junit.Test;
import static org.junit.Assert.*;

public class MovieValidatorTest {
    private MovieValidator validator = new MovieValidator();
    
    @Test
    public void testValidMovieTitle() {
        assertTrue(validator.isValidTitle("The Dark Knight"));
        assertTrue(validator.isValidTitle("A Star Is Born"));
    }
    
    @Test
    public void testInvalidMovieTitle() {
        assertFalse(validator.isValidTitle("the Dark Knight"));
        assertFalse(validator.isValidTitle("The dark Knight"));
    }
}
```

#### 2. **UserValidator Unit Tests**
Tests to validate user name and ID validation logic.

**Test Cases:**
- `testValidUserName()` - Test valid names (e.g., "John Doe")
- `testInvalidUserName_StartsWithSpace()` - Test name starting with space
- `testInvalidUserName_ContainsNumbers()` - Test name with numbers
- `testInvalidUserName_SpecialCharacters()` - Test name with special characters
- `testValidUserId()` - Test valid user IDs (e.g., "123456789", "12345678A")
- `testInvalidUserId_WrongLength()` - Test IDs not exactly 9 characters
- `testInvalidUserId_StartsWithLetter()` - Test ID starting with letter
- `testInvalidUserId_MultipleLettersAtEnd()` - Test ID ending with multiple letters
- `testInvalidUserId_DuplicateId()` - Test duplicate user IDs

**How to implement:**
```java
@Test
public void testValidUserName() {
    assertTrue(validator.isValidName("John Doe"));
    assertTrue(validator.isValidName("Mary Jane Watson"));
}

@Test
public void testInvalidUserName() {
    assertFalse(validator.isValidName(" John Doe"));
    assertFalse(validator.isValidName("John123"));
}
```

#### 3. **MovieParser Unit Tests**
Tests to verify parsing of movie data from file lines.

**Test Cases:**
- `testParseValidMovie()` - Parse valid movie line
- `testParseMovieWithMultipleGenres()` - Parse movie with multiple genres
- `testParseInvalidFormat()` - Test malformed input
- `testParseEmptyLine()` - Test empty line handling

#### 4. **UserParser Unit Tests**
Tests to verify parsing of user data from file lines.

**Test Cases:**
- `testParseValidUser()` - Parse valid user line
- `testParseUserWithMultipleMovies()` - Parse user with multiple liked movies
- `testParseInvalidFormat()` - Test malformed input

#### 5. **RecommendationService Unit Tests**
Tests to verify recommendation algorithm logic.

**Test Cases:**
- `testRecommendations_SingleGenre()` - User likes one genre
- `testRecommendations_MultipleGenres()` - User likes multiple genres
- `testRecommendations_NoLikedMovies()` - User has no liked movies
- `testRecommendations_AllMoviesAlreadyLiked()` - User already liked all movies in genre
- `testRecommendations_ExcludeAlreadyLiked()` - Verify already liked movies are not recommended

**How to implement:**
```java
@Test
public void testRecommendations_SingleGenre() {
    List<Movie> movies = Arrays.asList(
        new Movie("Movie A", "MA123", Arrays.asList("action")),
        new Movie("Movie B", "MB456", Arrays.asList("action")),
        new Movie("Movie C", "MC789", Arrays.asList("drama"))
    );
    
    User user = new User("John Doe", "123456789", Arrays.asList("MA123"));
    
    List<String> recommendations = service.getRecommendations(user, movies);
    
    assertTrue(recommendations.contains("Movie B"));
    assertFalse(recommendations.contains("Movie A")); // already liked
    assertFalse(recommendations.contains("Movie C")); // different genre
}
```

### Integration Testing Strategy

#### 1. **End-to-End File Processing Tests**
Test the complete flow from reading input files to writing output file.

**Test Cases:**
- `testCompleteFlow_ValidInputs()` - Test with completely valid input files
- `testCompleteFlow_MovieTitleError()` - Test with invalid movie title
- `testCompleteFlow_MovieIdLettersError()` - Test with invalid movie ID letters
- `testCompleteFlow_MovieIdNumbersError()` - Test with non-unique numbers in movie ID
- `testCompleteFlow_UserNameError()` - Test with invalid user name
- `testCompleteFlow_UserIdError()` - Test with invalid user ID
- `testCompleteFlow_MultipleErrors()` - Test that only first error is reported

**How to implement:**
```java
import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

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
    }
    
    @Test
    public void testCompleteFlow_ValidInputs() throws Exception {
        // Create test movies.txt
        String moviesContent = "The Dark Knight,TDK123\n" +
                              "action,thriller\n" +
                              "The Godfather,TG456\n" +
                              "crime,drama\n";
        Files.write(Paths.get(TEST_INPUT_DIR + "movies.txt"), 
                   moviesContent.getBytes());
        
        // Create test users.txt
        String usersContent = "John Doe,123456789\n" +
                             "TDK123\n";
        Files.write(Paths.get(TEST_INPUT_DIR + "users.txt"), 
                   usersContent.getBytes());
        
        // Run the application
        Main.processFiles(TEST_INPUT_DIR + "movies.txt",
                         TEST_INPUT_DIR + "users.txt",
                         TEST_OUTPUT_DIR + "recommendations.txt");
        
        // Verify output
        String output = new String(Files.readAllBytes(
            Paths.get(TEST_OUTPUT_DIR + "recommendations.txt")));
        
        assertTrue(output.contains("John Doe,123456789"));
        // Should not recommend The Dark Knight (already liked)
        // Should recommend other action/thriller movies if any exist
    }
    
    @Test
    public void testCompleteFlow_MovieTitleError() throws Exception {
        String moviesContent = "the dark knight,TDK123\n" +
                              "action,thriller\n";
        Files.write(Paths.get(TEST_INPUT_DIR + "movies.txt"), 
                   moviesContent.getBytes());
        
        String usersContent = "John Doe,123456789\n" +
                             "TDK123\n";
        Files.write(Paths.get(TEST_INPUT_DIR + "users.txt"), 
                   usersContent.getBytes());
        
        Main.processFiles(TEST_INPUT_DIR + "movies.txt",
                         TEST_INPUT_DIR + "users.txt",
                         TEST_OUTPUT_DIR + "recommendations.txt");
        
        String output = new String(Files.readAllBytes(
            Paths.get(TEST_OUTPUT_DIR + "recommendations.txt")));
        
        assertTrue(output.contains("Error"));
        assertTrue(output.contains("ERROR: Movie Title {the dark knight} is wrong"));
    }
}
```

#### 2. **File I/O Integration Tests**
Test file reading and writing operations.

**Test Cases:**
- `testReadMoviesFile_Success()`
- `testReadUsersFile_Success()`
- `testWriteRecommendationsFile_Success()`
- `testFileNotFound()`
- `testFilePermissionDenied()`

#### 3. **Data Flow Integration Tests**
Test the interaction between different components.

**Test Cases:**
- `testValidatorToParser_Integration()`
- `testParserToService_Integration()`
- `testServiceToOutput_Integration()`

### Mock Testing Strategy

For isolated testing, use mocks for file I/O operations:

```java
import org.mockito.Mock;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;

public class RecommendationServiceMockTest {
    @Mock
    private MovieParser movieParser;
    
    @Mock
    private UserParser userParser;
    
    @Test
    public void testServiceWithMockedParsers() {
        when(movieParser.parseMovies(anyString()))
            .thenReturn(createMockMovieList());
        
        when(userParser.parseUsers(anyString()))
            .thenReturn(createMockUserList());
        
        // Test the service logic in isolation
    }
}
```

### Test Data Organization

Create a `test-data/` directory with sample files:

```
test-data/
├── valid/
│   ├── movies.txt
│   └── users.txt
├── invalid-movie-title/
│   └── movies.txt
├── invalid-movie-id-letters/
│   └── movies.txt
├── invalid-movie-id-numbers/
│   └── movies.txt
├── invalid-user-name/
│   └── users.txt
└── invalid-user-id/
    └── users.txt
```

### Testing Tools Required

Add to `pom.xml` (if using Maven):
```xml
<dependencies>
    <dependency>
        <groupId>junit</groupId>
        <artifactId>junit</artifactId>
        <version>4.13.2</version>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>org.mockito</groupId>
        <artifactId>mockito-core</artifactId>
        <version>4.11.0</version>
        <scope>test</scope>
    </dependency>
</dependencies>
```

Or for Gradle:
```gradle
dependencies {
    testImplementation 'junit:junit:4.13.2'
    testImplementation 'org.mockito:mockito-core:4.11.0'
}
```

### Running Tests

**Using Maven:**
```bash
mvn test                    # Run all tests
mvn test -Dtest=ClassName   # Run specific test class
```

**Using Gradle:**
```bash
./gradlew test              # Run all tests
./gradlew test --tests ClassName  # Run specific test class
```

**Using IDE:**
- Right-click on test class/method → Run Test
- Use test coverage tools to see code coverage

### Test Coverage Goals

Aim for:
- **Unit Tests**: 90%+ code coverage
- **Integration Tests**: Cover all main workflows
- **Edge Cases**: All validation rules tested
- **Error Handling**: All error messages verified
