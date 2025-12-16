package com.movie.recommendation.service;

import com.movie.recommendation.model.Movie;
import com.movie.recommendation.model.User;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Service for generating movie recommendations based on user preferences.
 * 
 * TESTING STRATEGY:
 * 
 * Unit Tests for getRecommendations():
 * - testRecommendations_UserLikesSingleMovie() - User likes one movie, recommend same genre
 * - testRecommendations_UserLikesMultipleMoviesSameGenre() - Multiple movies, same genre
 * - testRecommendations_UserLikesMultipleMoviesDifferentGenres() - Multiple genres
 * - testRecommendations_NoOtherMoviesInGenre() - User likes all movies in genre
 * - testRecommendations_UserLikesNoMovies() - Empty liked movies list
 * - testRecommendations_MovieNotFound() - User likes movie ID that doesn't exist
 * - testRecommendations_ExcludeAlreadyLiked() - Don't recommend already liked movies
 * - testRecommendations_MultipleGenresPerMovie() - Movies with multiple genres
 * - testRecommendations_CaseInsensitiveGenres() - Genre matching is case-insensitive
 * - testRecommendations_DuplicatesRemoved() - Same movie recommended through different genres
 * 
 * Unit Tests for generateRecommendationsForAllUsers():
 * - testGenerateForAllUsers_SingleUser() - One user
 * - testGenerateForAllUsers_MultipleUsers() - Multiple users
 * - testGenerateForAllUsers_EmptyUsers() - No users
 * - testGenerateForAllUsers_EmptyMovies() - No movies
 * - testGenerateForAllUsers_MixedScenarios() - Some users get recommendations, some don't
 * 
 * Integration Tests:
 * - testRecommendationFlow_EndToEnd() - Complete flow with real data
 * - testRecommendationFlow_WithParsedData() - Test with data from parsers
 * - testRecommendationFlow_ComplexScenario() - Multiple users, movies, genres
 * 
 * Edge Case Tests:
 * - testRecommendations_AllMoviesSameGenre() - All movies are same genre
 * - testRecommendations_NoGenreOverlap() - User likes genre with no other movies
 * - testRecommendations_EmptyGenreList() - Movie with no genres
 * 
 * Performance Tests:
 * - testRecommendations_LargeMovieList() - Performance with 1000+ movies
 * - testRecommendations_LargeUserList() - Performance with 1000+ users
 */
public class RecommendationService {
    
    /**
     * Generates movie recommendations for a user based on their liked movies.
     * 
     * Algorithm:
     * 1. Find all movies the user has liked
     * 2. Extract all genres from these movies
     * 3. Find all other movies (not already liked) that have any of these genres
     * 4. Return the titles of recommended movies
     * 
     * @param user the user to generate recommendations for
     * @param allMovies the complete list of available movies
     * @return list of recommended movie titles
     */
    public List<String> getRecommendations(User user, List<Movie> allMovies) {
        // Create a map for quick movie lookup by ID
        Map<String, Movie> movieMap = new HashMap<>();
        for (Movie movie : allMovies) {
            movieMap.put(movie.getId(), movie);
        }
        
        // Find all genres the user likes
        Set<String> likedGenres = new HashSet<>();
        for (String likedMovieId : user.getLikedMovieIds()) {
            Movie likedMovie = movieMap.get(likedMovieId);
            if (likedMovie != null) {
                likedGenres.addAll(likedMovie.getGenres());
            }
        }
        
        // Find recommendations: movies with matching genres that user hasn't liked
        Set<String> recommendedTitles = new HashSet<>(); // Use Set to avoid duplicates
        
        for (Movie movie : allMovies) {
            // Skip if user already liked this movie
            if (user.hasLikedMovie(movie.getId())) {
                continue;
            }
            
            // Check if movie has any genre the user likes
            for (String genre : movie.getGenres()) {
                if (likedGenres.contains(genre.toLowerCase())) {
                    recommendedTitles.add(movie.getTitle());
                    break; // No need to check other genres for this movie
                }
            }
        }
        
        return new ArrayList<>(recommendedTitles);
    }
    
    /**
     * Generates recommendations for all users.
     * 
     * @param users the list of all users
     * @param movies the list of all movies
     * @return map of user to their recommendations
     */
    public Map<User, List<String>> generateRecommendationsForAllUsers(List<User> users, List<Movie> movies) {
        Map<User, List<String>> allRecommendations = new HashMap<>();
        
        for (User user : users) {
            List<String> recommendations = getRecommendations(user, movies);
            allRecommendations.put(user, recommendations);
        }
        
        return allRecommendations;
    }
    
    /**
     * Formats recommendations as a string for output file.
     * Format:
     * User Name,User ID
     * Movie Title 1,Movie Title 2,Movie Title 3
     * 
     * TESTING NOTE:
     * Test cases:
     * - User with recommendations
     * - User with no recommendations (should output user info and empty line)
     * - Multiple users
     * - Correct comma separation
     * - No trailing commas
     * 
     * @param user the user
     * @param recommendations the list of recommended movie titles
     * @return formatted string for output file
     */
    public String formatRecommendations(User user, List<String> recommendations) {
        StringBuilder sb = new StringBuilder();
        
        // Line 1: User Name,User ID
        sb.append(user.getName()).append(",").append(user.getId()).append("\n");
        
        // Line 2: Recommended movie titles separated by commas
        if (recommendations.isEmpty()) {
            sb.append("\n");
        } else {
            sb.append(String.join(",", recommendations)).append("\n");
        }
        
        return sb.toString();
    }
}
