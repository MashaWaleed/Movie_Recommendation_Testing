package com.movie.recommendation.parser;

import com.movie.recommendation.model.User;
import com.movie.recommendation.validator.UserValidator;
import com.movie.recommendation.exception.ValidationException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Parses user data from the users.txt file.
 * 
 * TESTING STRATEGY:
 * 
 * Unit Tests for parseLine():
 * - testParseLine_ValidUser() - "John Doe,123456789"
 * - testParseLine_ValidMovieIds() - "TDK123,TG456,ID789"
 * - testParseLine_SingleMovieId() - "TDK123"
 * - testParseLine_MovieIdsWithSpaces() - "TDK123, TG456" (should trim)
 * - testParseLine_EmptyLine() - ""
 * - testParseLine_MissingComma() - "John Doe 123456789"
 * 
 * Unit Tests for parseUsers():
 * - testParseUsers_SingleUser() - One user with movie IDs
 * - testParseUsers_MultipleUsers() - Several users
 * - testParseUsers_UserWithNoMovies() - User line but empty movie line
 * - testParseUsers_EmptyFile() - Empty file
 * - testParseUsers_InvalidName() - User with invalid name
 * - testParseUsers_InvalidId() - User with invalid ID
 * - testParseUsers_DuplicateIds() - Two users with same ID
 * - testParseUsers_MissingMovieLine() - User line but no movie line
 * - testParseUsers_FileNotFound() - Non-existent file
 * 
 * Integration Tests:
 * - testParseUsersFile_CompleteValid() - Parse complete valid file
 * - testParseUsersFile_StopsAtFirstError() - Verify it stops at first error
 * - testParseUsersFile_WithValidator() - Test integration with UserValidator
 * - testParseUsersFile_UniqueIdCheck() - Verify duplicate IDs are caught
 * 
 * Mock Tests:
 * - testParseUsers_MockValidator() - Use mocked validator to isolate parsing logic
 * - testParseUsers_MockFileReader() - Mock file reading to test parsing logic
 */
public class UserParser {
    
    private UserValidator validator;
    
    /**
     * Constructs a UserParser with a validator.
     * 
     * @param validator the validator to use for validating users
     */
    public UserParser(UserValidator validator) {
        this.validator = validator;
    }
    
    /**
     * Parses users from a file.
     * File format:
     * Line 1: User Name,User ID
     * Line 2: movieId1,movieId2,movieId3
     * Line 3: Next User Name,User ID
     * Line 4: movieId1,movieId2
     * ...
     * 
     * @param filePath the path to the users.txt file
     * @return list of parsed and validated User objects
     * @throws ValidationException if any user fails validation (stops at first error)
     * @throws IOException if file cannot be read
     */
    public List<User> parseUsers(String filePath) throws ValidationException, IOException {
        List<User> users = new ArrayList<>();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            int lineNumber = 0;
            
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                
                // Skip empty lines
                if (line.trim().isEmpty()) {
                    continue;
                }
                
                // Parse user name and ID
                String[] parts = line.split(",", 2);
                if (parts.length != 2) {
                    throw new ValidationException("ERROR: Invalid format at line " + lineNumber);
                }
                
                String name = parts[0].trim();
                String id = parts[1].trim();
                
                // Validate name and ID (stops at first error)
                validator.validateUser(name, id);
                
                // Read next line for movie IDs
                String movieIdsLine = reader.readLine();
                lineNumber++;
                
                if (movieIdsLine == null) {
                    throw new ValidationException("ERROR: Missing movie IDs line for user: " + name);
                }
                
                // Parse movie IDs
                List<String> movieIds = parseMovieIds(movieIdsLine);
                
                // Create user object
                User user = new User(name, id, movieIds);
                users.add(user);
            }
        }
        
        return users;
    }
    
    /**
     * Parses movie IDs from a comma-separated string.
     * 
     * @param movieIdsLine the line containing movie IDs separated by commas
     * @return list of movie ID strings
     */
    private List<String> parseMovieIds(String movieIdsLine) {
        String[] idArray = movieIdsLine.split(",");
        List<String> movieIds = new ArrayList<>();
        
        for (String id : idArray) {
            String trimmedId = id.trim();
            if (!trimmedId.isEmpty()) {
                movieIds.add(trimmedId);
            }
        }
        
        return movieIds;
    }
}
