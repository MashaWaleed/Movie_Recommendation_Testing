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
import com.movie.recommendation.Main;
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



class StubMovieValidator extends MovieValidator {
    @Override
    public void validateTitle(String title) {
        // do nothing (always valid)
    }

    @Override
    public void validateId(String id,String title) {
        // do nothing
    }

    @Override
    public void validateMovie(String id,String title) {
        // do nothing
    }
}
    

class StubRecommendationService extends RecommendationService {

    @Override
    public Map<User, List<String>> generateRecommendationsForAllUsers(
            List<User> users, List<Movie> movies) {

        Map<User, List<String>> result = new HashMap<>();
        result.put(users.get(0), List.of("Inception"));
        return result;
    }
}


class StubMovieParser extends MovieParser {
    private final List<Movie> stubMovies;

    public StubMovieParser(List<Movie> stubMovies) {
        super(null); // pass null validator, not used
        this.stubMovies = stubMovies;
    }

    @Override
    public List<Movie> parseMovies(String filePath) {
        // Ignore file, return stub data
        return stubMovies;
    }
}

// Stub for UserParser
class StubUserParser extends UserParser {
    private final List<User> stubUsers;

    public StubUserParser(List<User> stubUsers) {
        super(null); // pass null validator, not used
        this.stubUsers = stubUsers;
    }

    @Override
    public List<User> parseUsers(String filePath) {
        return stubUsers;
    }
}






public class Top_Down {
 // can’t create stubs for everything used in Main
//bec Main.processFiles() creates its dependencies internally so, A stub must be injected from outside.
  
//Test Service Logic core using stubbed parser data (( unit testing))     
@Test
void testServiceWithStubbedParsersData() {

    // STUB OUTPUT (pretend parsers already worked) 

    Movie movie1 = new Movie(
            "The Dark Knight", "TDK123",
            List.of("action", "thriller")
    );

    Movie movie2 = new Movie(
            "Inception", "IN456",
            List.of("action", "sci-fi")
    );

    List<Movie> stubMovies = List.of(movie1, movie2);

    User stubUser = new User(
            "John Doe",
            "123456789",
            List.of("TDK123") 
    );

    List<User> stubUsers = List.of(stubUser);

    // REAL SERVICE ----
    RecommendationService service = new RecommendationService();

    Map<User, List<String>> result =
            service.generateRecommendationsForAllUsers(stubUsers, stubMovies);

    List<String> johnRecs = result.get(stubUser);

    assertNotNull(johnRecs);
    assertTrue(johnRecs.contains("Inception"));      
    assertFalse(johnRecs.contains("The Dark Knight"));
}



//Service + Parser integration using stub parsers       

@Test
void testServiceWithStubbedParsers() throws Exception {
    //  Prepare stub data ---
    Movie movie1 = new Movie("The Dark Knight", "TDK123", List.of("action", "thriller"));
    Movie movie2 = new Movie("Inception", "IN456", List.of("action", "sci-fi"));
    List<Movie> stubMovies = List.of(movie1, movie2);

    User user = new User("John Doe", "123456789", List.of("TDK123"));
    List<User> stubUsers = List.of(user);

    Path moviesFile = Files.createTempFile("movies", ".txt");
    Path usersFile  = Files.createTempFile("users", ".txt");

    Files.writeString(moviesFile, "dummy content");
    Files.writeString(usersFile, "dummy content");

    // Create stub parsers ---
    MovieParser movieParser = new StubMovieParser(stubMovies);
    UserParser userParser = new StubUserParser(stubUsers);

    // Real service ---
    RecommendationService service = new RecommendationService();

    //  Simulate parsing + service flow ---
    List<Movie> movies = movieParser.parseMovies(moviesFile.toString());
    List<User> users = userParser.parseUsers(usersFile.toString());
    Map<User, List<String>> result = service.generateRecommendationsForAllUsers(users, movies);

    List<String> johnRecs = result.get(user);
    assertNotNull(johnRecs);
    assertTrue(johnRecs.contains("Inception"));
    assertFalse(johnRecs.contains("The Dark Knight")); 
}

//Parser + Validator integration  Using stubbed validator
@Test
void testTopDown_ServiceWithRealParser_StubValidator() throws Exception {

    MovieValidator stubValidator = new StubMovieValidator();
    MovieParser parser = new MovieParser(stubValidator);

    Path moviesFile = Files.createTempFile("movies", ".txt");

    String moviesData =
            "The Dark Knight,TDK123\n" +
            "action,thriller\n" +
            "Inception,I456\n" +
            "action,sci-fi\n";

    Files.writeString(moviesFile, moviesData);

    List<Movie> movies = parser.parseMovies(moviesFile.toString());

    User user = new User("John Doe", "123456789", List.of("TDK123"));

    RecommendationService service = new RecommendationService();

    List<String> recs = service.getRecommendations(user, movies);

    assertTrue(recs.contains("Inception"));
    assertFalse(recs.contains("The Dark Knight"));
}

//Parser + Validator integration  Using stubbed validator but invalid data to ensure that validator work as i want

@Test
void testTopDown_ServiceWithRealParser_StubValidator_InvalidTitles() throws Exception {

    MovieValidator stubValidator = new StubMovieValidator();
    MovieParser parser = new MovieParser(stubValidator);

    Path moviesFile = Files.createTempFile("movies", ".txt");

    String moviesData =
            "the dark knight,TDK123\n" +
            "action,thriller\n" +
            "inception,IN456\n" +
            "action,sci-fi\n";

    Files.writeString(moviesFile, moviesData);

    List<Movie> movies = parser.parseMovies(moviesFile.toString());

    User user = new User("John Doe", "123456789", List.of("TDK123"));

    RecommendationService service = new RecommendationService();

    List<String> recs = service.getRecommendations(user, movies);

    assertTrue(recs.contains("inception"));
    assertFalse(recs.contains("the dark knight"));
}





}
