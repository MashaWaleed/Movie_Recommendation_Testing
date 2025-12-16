package com.movie.recommendation.parser;

import com.movie.recommendation.model.Movie;
import com.movie.recommendation.validator.MovieValidator;
import com.movie.recommendation.exception.ValidationException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Parses movie data from the movies.txt file.
 * 
 * TESTING STRATEGY:
 * 
 * Unit Tests for parseLine():
 * - testParseLine_ValidMovie() - "The Dark Knight,TDK123"
 * - testParseLine_ValidGenres() - "action,thriller,drama"
 * - testParseLine_SingleGenre() - "action"
 * - testParseLine_GenresWithSpaces() - "action, thriller" (should trim)
 * - testParseLine_EmptyLine() - ""
 * - testParseLine_MissingComma() - "The Dark Knight TDK123"
 * - testParseLine_ExtraCommas() - "The Dark Knight,,TDK123"
 * 
 * Unit Tests for parseMovies():
 * - testParseMovies_SingleMovie() - One movie with genres
 * - testParseMovies_MultipleMovies() - Several movies
 * - testParseMovies_EmptyFile() - Empty file
 * - testParseMovies_InvalidTitle() - Movie with invalid title
 * - testParseMovies_InvalidId() - Movie with invalid ID
 * - testParseMovies_MissingGenreLine() - Title line but no genre line
 * - testParseMovies_FileNotFound() - Non-existent file
 * 
 * Integration Tests:
 * - testParseMoviesFile_CompleteValid() - Parse complete valid file
 * - testParseMoviesFile_StopsAtFirstError() - Verify it stops at first error
 * - testParseMoviesFile_WithValidator() - Test integration with MovieValidator
 * 
 * Mock Tests:
 * - testParseMovies_MockValidator() - Use mocked validator to isolate parsing logic
 * - testParseMovies_MockFileReader() - Mock file reading to test parsing logic
 */
public class MovieParser {
    
    private MovieValidator validator;
    
    /**
     * Constructs a MovieParser with a validator.
     * 
     * @param validator the validator to use for validating movies
     */
    public MovieParser(MovieValidator validator) {
        this.validator = validator;
    }
    
    /**
     * Parses movies from a file.
     * File format:
     * Line 1: Movie Title,Movie ID
     * Line 2: genre1,genre2,genre3
     * Line 3: Next Movie Title,Movie ID
     * Line 4: genre1,genre2
     * ...
     * 
     * @param filePath the path to the movies.txt file
     * @return list of parsed and validated Movie objects
     * @throws ValidationException if any movie fails validation (stops at first error)
     * @throws IOException if file cannot be read
     */
    public List<Movie> parseMovies(String filePath) throws ValidationException, IOException {
        List<Movie> movies = new ArrayList<>();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            int lineNumber = 0;
            
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                
                // Skip empty lines
                if (line.trim().isEmpty()) {
                    continue;
                }
                
                // Parse movie title and ID (odd line numbers: 1, 3, 5, ...)
                String[] parts = line.split(",", 2);
                if (parts.length != 2) {
                    throw new ValidationException("ERROR: Invalid format at line " + lineNumber);
                }
                
                String title = parts[0].trim();
                String id = parts[1].trim();
                
                // Validate title and ID (stops at first error)
                validator.validateMovie(title, id);
                
                // Read next line for genres
                String genreLine = reader.readLine();
                lineNumber++;
                
                if (genreLine == null) {
                    throw new ValidationException("ERROR: Missing genre line for movie: " + title);
                }
                
                // Parse genres
                List<String> genres = parseGenres(genreLine);
                
                // Create movie object
                Movie movie = new Movie(title, id, genres);
                movies.add(movie);
            }
        }
        
        return movies;
    }
    
    /**
     * Parses genres from a comma-separated string.
     * 
     * @param genreLine the line containing genres separated by commas
     * @return list of genre strings
     */
    private List<String> parseGenres(String genreLine) {
        String[] genreArray = genreLine.split(",");
        List<String> genres = new ArrayList<>();
        
        for (String genre : genreArray) {
            String trimmedGenre = genre.trim();
            if (!trimmedGenre.isEmpty()) {
                genres.add(trimmedGenre.toLowerCase());
            }
        }
        
        return genres;
    }
}
