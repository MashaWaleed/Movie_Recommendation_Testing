package com.movie.recommendation.model;

import java.util.List;
import java.util.Objects;

/**
 * Represents a user with their name, ID, and list of liked movie IDs.
 * 
 * TESTING NOTES:
 * - Unit Test: Test constructor with valid inputs
 * - Unit Test: Test getters return correct values
 * - Unit Test: Test equals() and hashCode() methods
 * - Unit Test: Test hasLikedMovie() method
 * 
 * Example test:
 * @Test
 * public void testUserCreation() {
 *     List<String> likedMovies = Arrays.asList("TDK123", "TG456");
 *     User user = new User("John Doe", "123456789", likedMovies);
 *     assertEquals("John Doe", user.getName());
 *     assertEquals("123456789", user.getId());
 *     assertTrue(user.hasLikedMovie("TDK123"));
 *     assertFalse(user.hasLikedMovie("ABC999"));
 * }
 */
public class User {
    private final String name;
    private final String id;
    private final List<String> likedMovieIds;
    
    /**
     * Constructs a User object.
     * 
     * @param name the user's name (alphabetic characters and spaces, not starting with space)
     * @param id the user's ID (9 alphanumeric characters, starting with numbers, ending with at most one letter)
     * @param likedMovieIds list of movie IDs that the user likes
     */
    public User(String name, String id, List<String> likedMovieIds) {
        this.name = name;
        this.id = id;
        this.likedMovieIds = likedMovieIds;
    }
    
    /**
     * Gets the user's name.
     * 
     * @return the user's name
     */
    public String getName() {
        return name;
    }
    
    /**
     * Gets the user's ID.
     * 
     * @return the user's ID
     */
    public String getId() {
        return id;
    }
    
    /**
     * Gets the list of liked movie IDs.
     * 
     * @return the list of liked movie IDs
     */
    public List<String> getLikedMovieIds() {
        return likedMovieIds;
    }
    
    /**
     * Checks if the user has liked a movie with the specified ID.
     * 
     * TESTING NOTE:
     * Test with:
     * - Movie ID that user liked (should return true)
     * - Movie ID that user hasn't liked (should return false)
     * - Empty liked movies list
     * 
     * @param movieId the movie ID to check
     * @return true if the user has liked this movie, false otherwise
     */
    public boolean hasLikedMovie(String movieId) {
        return likedMovieIds.contains(movieId);
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", id='" + id + '\'' +
                ", likedMovieIds=" + likedMovieIds +
                '}';
    }
}
