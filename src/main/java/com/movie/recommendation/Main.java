package com.movie.recommendation;

import com.movie.recommendation.model.Movie;
import com.movie.recommendation.model.User;
import com.movie.recommendation.parser.MovieParser;
import com.movie.recommendation.parser.UserParser;
import com.movie.recommendation.service.RecommendationService;
import com.movie.recommendation.validator.MovieValidator;
import com.movie.recommendation.validator.UserValidator;
import com.movie.recommendation.exception.ValidationException;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Main class for the Movie Recommendation System.
 * 
 * TESTING STRATEGY:
 * 
 * Integration Tests for main():
 * - testMain_ValidInputFiles() - Complete valid input files
 * - testMain_InvalidMovieTitle() - Error in movie title
 * - testMain_InvalidMovieId() - Error in movie ID
 * - testMain_InvalidUserName() - Error in user name
 * - testMain_InvalidUserId() - Error in user ID
 * - testMain_DuplicateUserId() - Duplicate user IDs
 * - testMain_FileNotFound() - Missing input files
 * - testMain_OutputFileCreated() - Verify output file is created
 * - testMain_OutputFileContent() - Verify output content is correct
 * 
 * Integration Tests for processFiles():
 * - testProcessFiles_ValidData() - Process valid input files
 * - testProcessFiles_ErrorInMovies() - Error in movies file
 * - testProcessFiles_ErrorInUsers() - Error in users file
 * - testProcessFiles_FirstErrorOnly() - Multiple errors, only first reported
 * - testProcessFiles_CustomPaths() - Custom input/output paths
 * 
 * Integration Tests for writeErrorToFile():
 * - testWriteError_MovieTitleError() - Write movie title error
 * - testWriteError_MovieIdError() - Write movie ID error
 * - testWriteError_UserNameError() - Write user name error
 * - testWriteError_UserIdError() - Write user ID error
 * - testWriteError_FileCreated() - Verify error file is created
 * 
 * Integration Tests for writeRecommendationsToFile():
 * - testWriteRecommendations_SingleUser() - Single user output
 * - testWriteRecommendations_MultipleUsers() - Multiple users output
 * - testWriteRecommendations_UserWithNoRecommendations() - User with empty recommendations
 * - testWriteRecommendations_FileFormat() - Verify correct format
 * 
 * End-to-End Tests:
 * - testE2E_CompleteFlow() - Full application flow from input to output
 * - testE2E_VariousGenres() - Test with various movie genres
 * - testE2E_MultipleUsersMultipleMovies() - Complex scenario
 * 
 * How to run integration tests:
 * 1. Create test input files in test directories
 * 2. Run the main method or processFiles() with test paths
 * 3. Verify output file content matches expected
 * 4. Clean up test files in @After/@AfterEach
 */
public class Main {
    
    private static final String MOVIES_FILE = "input/movies.txt";
    private static final String USERS_FILE = "input/users.txt";
    private static final String OUTPUT_FILE = "output/recommendations.txt";
    
    /**
     * Main entry point of the application.
     * 
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        try {
            processFiles(MOVIES_FILE, USERS_FILE, OUTPUT_FILE);
            System.out.println("Recommendations generated successfully in: " + OUTPUT_FILE);
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Processes input files and generates recommendations.
     * This method is public static to allow integration testing.
     * 
     * TESTING NOTE:
     * Call this method directly in integration tests with custom file paths:
     * processFiles("test_input/movies.txt", "test_input/users.txt", "test_output/recommendations.txt");
     * 
     * @param moviesFilePath path to movies input file
     * @param usersFilePath path to users input file
     * @param outputFilePath path to output file
     * @throws IOException if file I/O error occurs
     */
    public static void processFiles(String moviesFilePath, String usersFilePath, String outputFilePath) 
            throws IOException {
        try {
            // Initialize validators
            MovieValidator movieValidator = new MovieValidator();
            UserValidator userValidator = new UserValidator();
            
            // Initialize parsers
            MovieParser movieParser = new MovieParser(movieValidator);
            UserParser userParser = new UserParser(userValidator);
            
            // Parse movies (validates during parsing, stops at first error)
            List<Movie> movies = movieParser.parseMovies(moviesFilePath);
            
            // Parse users (validates during parsing, stops at first error)
            List<User> users = userParser.parseUsers(usersFilePath);
            
            // Generate recommendations
            RecommendationService recommendationService = new RecommendationService();
            Map<User, List<String>> allRecommendations = 
                recommendationService.generateRecommendationsForAllUsers(users, movies);
            
            // Write recommendations to output file
            writeRecommendationsToFile(allRecommendations, recommendationService, outputFilePath);
            
        } catch (ValidationException e) {
            // If validation error occurs, write error to output file
            writeErrorToFile(e.getMessage(), outputFilePath);
            System.err.println("Validation error: " + e.getMessage());
        }
    }
    
    /**
     * Writes recommendations to the output file.
     * 
     * @param allRecommendations map of users to their recommendations
     * @param service the recommendation service (for formatting)
     * @param outputFilePath path to output file
     * @throws IOException if file writing fails
     */
    private static void writeRecommendationsToFile(Map<User, List<String>> allRecommendations,
                                                   RecommendationService service,
                                                   String outputFilePath) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath))) {
            for (Map.Entry<User, List<String>> entry : allRecommendations.entrySet()) {
                User user = entry.getKey();
                List<String> recommendations = entry.getValue();
                
                String formattedOutput = service.formatRecommendations(user, recommendations);
                writer.write(formattedOutput);
            }
        }
    }
    
    /**
     * Writes error message to the output file.
     * Format: "Error\n{error_message}\n"
     * 
     * TESTING NOTE:
     * Verify that:
     * - File contains "Error" on first line
     * - Error message is on second line
     * - Format matches specification
     * 
     * @param errorMessage the error message to write
     * @param outputFilePath path to output file
     * @throws IOException if file writing fails
     */
    private static void writeErrorToFile(String errorMessage, String outputFilePath) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath))) {
            writer.write("Error\n");
            writer.write(errorMessage + "\n");
        }
    }
}
