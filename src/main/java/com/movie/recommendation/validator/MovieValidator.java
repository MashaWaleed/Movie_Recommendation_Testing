package com.movie.recommendation.validator;

import com.movie.recommendation.exception.ValidationException;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Validates movie data according to business rules.
 * 
 * TESTING STRATEGY:
 * 
 * Unit Tests for validateTitle():
 * - testValidTitle_SingleWord() - "Inception"
 * - testValidTitle_MultipleWords() - "The Dark Knight"
 * - testValidTitle_WithNumbers() - "2001 A Space Odyssey" (if allowed)
 * - testInvalidTitle_LowercaseStart() - "the Dark Knight"
 * - testInvalidTitle_LowercaseMiddle() - "The dark Knight"
 * - testInvalidTitle_AllLowercase() - "the dark knight"
 * - testInvalidTitle_Empty() - ""
 * - testInvalidTitle_Null() - null
 * 
 * Unit Tests for validateId():
 * - testValidId_ThreeDigits() - "TDK123"
 * - testValidId_CorrectLetters() - "TDK456" for "The Dark Knight"
 * - testInvalidId_WrongLetters() - "ABC123" for "The Dark Knight"
 * - testInvalidId_MissingLetters() - "TD123" for "The Dark Knight"
 * - testInvalidId_ExtraLetters() - "TDKR123" for "The Dark Knight"
 * - testInvalidId_DuplicateNumbers() - "TDK111"
 * - testInvalidId_TwoUniqueOneRepeat() - "TDK112"
 * - testInvalidId_TwoDigitsOnly() - "TDK12"
 * - testInvalidId_FourDigits() - "TDK1234"
 * - testInvalidId_NoDigits() - "TDK"
 * - testInvalidId_Empty() - ""
 * - testInvalidId_Null() - null
 * - testInvalidId_LowercaseLetters() - "tdk123"
 * 
 * Integration Tests:
 * - testValidateMovie_AllValid() - Both title and ID are valid
 * - testValidateMovie_InvalidTitle() - Invalid title, valid ID
 * - testValidateMovie_InvalidId() - Valid title, invalid ID
 * - testValidateMovie_BothInvalid() - Both invalid (should return first error)
 */
public class MovieValidator {
    
    /**
     * Validates the movie title.
     * Rule: Every word in the title must start with a capital letter.
     * 
     * @param title the movie title to validate
     * @throws ValidationException if the title is invalid
     */
    public void validateTitle(String title) throws ValidationException {
        if (title == null || title.trim().isEmpty()) {
            throw new ValidationException("ERROR: Movie Title {" + title + "} is wrong");
        }
        
        String[] words = title.split("\\s+");
        for (String word : words) {
            if (word.isEmpty()) {
                continue;
            }
            // Check if first character is uppercase
            if (!Character.isUpperCase(word.charAt(0))) {
                throw new ValidationException("ERROR: Movie Title {" + title + "} is wrong");
            }
        }
    }
    
    /**
     * Validates the movie ID.
     * Rules:
     * 1. Must contain all capital letters from the title
     * 2. Followed by exactly 3 unique numbers
     * 
     * @param id the movie ID to validate
     * @param title the movie title (to extract capital letters)
     * @throws ValidationException if the ID is invalid
     */
    public void validateId(String id, String title) throws ValidationException {
        if (id == null || id.trim().isEmpty()) {
            throw new ValidationException("ERROR: Movie Id letters {" + id + "} are wrong");
        }
        
        // Extract capital letters from title
        StringBuilder expectedLetters = new StringBuilder();
        for (char c : title.toCharArray()) {
            if (Character.isUpperCase(c)) {
                expectedLetters.append(c);
            }
        }
        
        // Split ID into letter part and number part
        String letterPart = "";
        String numberPart = "";
        int i = 0;
        
        // Extract letter part
        while (i < id.length() && Character.isLetter(id.charAt(i))) {
            letterPart += id.charAt(i);
            i++;
        }
        
        // Extract number part
        while (i < id.length() && Character.isDigit(id.charAt(i))) {
            numberPart += id.charAt(i);
            i++;
        }
        
        // Check if there are extra characters after numbers
        if (i < id.length()) {
            throw new ValidationException("ERROR: Movie Id letters {" + id + "} are wrong");
        }
        
        // Validate letter part matches expected letters
        if (!letterPart.equals(expectedLetters.toString())) {
            throw new ValidationException("ERROR: Movie Id letters {" + id + "} are wrong");
        }
        
        // Validate number part has exactly 3 digits
        if (numberPart.length() != 3) {
            throw new ValidationException("ERROR: Movie Id numbers {" + id + "} aren't unique");
        }
        
        // Validate all 3 numbers are unique
        Set<Character> uniqueDigits = new HashSet<>();
        for (char digit : numberPart.toCharArray()) {
            if (!uniqueDigits.add(digit)) {
                throw new ValidationException("ERROR: Movie Id numbers {" + id + "} aren't unique");
            }
        }
    }
    
    /**
     * Validates both title and ID of a movie.
     * Validates title first, then ID (stops at first error).
     * 
     * @param title the movie title
     * @param id the movie ID
     * @throws ValidationException if either title or ID is invalid
     */
    public void validateMovie(String title, String id) throws ValidationException {
        // Validate title first (as per requirement to stop at first error)
        validateTitle(title);
        // Then validate ID
        validateId(id, title);
    }
}
