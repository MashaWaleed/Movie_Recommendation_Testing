package com.movie.recommendation.model;

import java.util.List;
import java.util.Objects;

/**
 * Represents a movie with its title, ID, and genres.
 * 
 * TESTING NOTES:
 * - Unit Test: Test constructor with valid inputs
 * - Unit Test: Test getters return correct values
 * - Unit Test: Test equals() and hashCode() methods
 * - Unit Test: Test toString() output format
 * 
 * Example test:
 * @Test
 * public void testMovieCreation() {
 *     List<String> genres = Arrays.asList("action", "thriller");
 *     Movie movie = new Movie("The Dark Knight", "TDK123", genres);
 *     assertEquals("The Dark Knight", movie.getTitle());
 *     assertEquals("TDK123", movie.getId());
 *     assertEquals(2, movie.getGenres().size());
 * }
 */
public class Movie {
    private final String title;
    private final String id;
    private final List<String> genres;
    
    /**
     * Constructs a Movie object.
     * 
     * @param title the movie title (each word should start with capital letter)
     * @param id the movie ID (capital letters from title + 3 unique numbers)
     * @param genres list of genres for this movie
     */
    public Movie(String title, String id, List<String> genres) {
        this.title = title;
        this.id = id;
        this.genres = genres;
    }
    
    /**
     * Gets the movie title.
     * 
     * @return the movie title
     */
    public String getTitle() {
        return title;
    }
    
    /**
     * Gets the movie ID.
     * 
     * @return the movie ID
     */
    public String getId() {
        return id;
    }
    
    /**
     * Gets the list of genres.
     * 
     * @return the list of genres
     */
    public List<String> getGenres() {
        return genres;
    }
    
    /**
     * Checks if this movie has the specified genre.
     * 
     * TESTING NOTE:
     * Test with:
     * - Genre that exists (should return true)
     * - Genre that doesn't exist (should return false)
     * - Case sensitivity (genres should be case-insensitive)
     * 
     * @param genre the genre to check
     * @return true if the movie has this genre, false otherwise
     */
    public boolean hasGenre(String genre) {
        return genres.stream()
                .anyMatch(g -> g.equalsIgnoreCase(genre));
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Movie movie = (Movie) o;
        return Objects.equals(id, movie.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    @Override
    public String toString() {
        return "Movie{" +
                "title='" + title + '\'' +
                ", id='" + id + '\'' +
                ", genres=" + genres +
                '}';
    }
}
