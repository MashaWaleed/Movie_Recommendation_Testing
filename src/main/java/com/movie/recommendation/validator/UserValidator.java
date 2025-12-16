package com.movie.recommendation.validator;

import com.movie.recommendation.exception.ValidationException;
import java.util.Set;
import java.util.HashSet;

/**
 * Validates user data according to business rules.
 * 
 * TESTING STRATEGY:
 * 
 * Unit Tests for validateName():
 * - testValidName_SingleWord() - "John"
 * - testValidName_TwoWords() - "John Doe"
 * - testValidName_MultipleWords() - "Mary Jane Watson"
 * - testValidName_MultipleSpaces() - "John  Doe" (double space between)
 * - testInvalidName_StartsWithSpace() - " John Doe"
 * - testInvalidName_ContainsNumbers() - "John123"
 * - testInvalidName_ContainsSpecialChars() - "John@Doe"
 * - testInvalidName_Empty() - ""
 * - testInvalidName_Null() - null
 * - testInvalidName_OnlySpaces() - "   "
 * 
 * Unit Tests for validateId():
 * - testValidId_NineDigits() - "123456789"
 * - testValidId_EightDigitsOneLetter() - "12345678A"
 * - testValidId_VariousLetters() - "12345678Z", "12345678B"
 * - testInvalidId_StartsWithLetter() - "A12345678"
 * - testInvalidId_TwoLettersAtEnd() - "1234567AB"
 * - testInvalidId_LetterInMiddle() - "1234A5678"
 * - testInvalidId_TooShort() - "12345678"
 * - testInvalidId_TooLong() - "1234567890"
 * - testInvalidId_LowercaseLetter() - "12345678a"
 * - testInvalidId_Empty() - ""
 * - testInvalidId_Null() - null
 * - testInvalidId_AllLetters() - "ABCDEFGHI"
 * 
 * Unit Tests for checkUniqueUserId():
 * - testUniqueId_FirstUser() - First user ID should be valid
 * - testUniqueId_DifferentIds() - Different IDs should be valid
 * - testDuplicateId_SameId() - Same ID should throw exception
 * - testDuplicateId_AfterMultipleUsers() - Duplicate after several users
 * 
 * Integration Tests:
 * - testValidateUser_AllValid() - Both name and ID valid
 * - testValidateUser_InvalidName() - Invalid name, valid ID
 * - testValidateUser_InvalidId() - Valid name, invalid ID
 * - testValidateUser_BothInvalid() - Both invalid (should return first error)
 * - testValidateMultipleUsers_NoDuplicates() - Multiple users with unique IDs
 * - testValidateMultipleUsers_WithDuplicate() - Multiple users with one duplicate
 */
public class UserValidator {
    
    // Set to track used user IDs for uniqueness validation
    private Set<String> usedUserIds = new HashSet<>();
    
    /**
     * Validates the user name.
     * Rules:
     * 1. Must contain only alphabetic characters and spaces
     * 2. Must not start with a space
     * 
     * @param name the user name to validate
     * @throws ValidationException if the name is invalid
     */
    public void validateName(String name) throws ValidationException {
        if (name == null || name.isEmpty()) {
            throw new ValidationException("ERROR: User Name {" + name + "} is wrong");
        }
        
        // Check if name starts with space
        if (name.startsWith(" ")) {
            throw new ValidationException("ERROR: User Name {" + name + "} is wrong");
        }
        
        // Check if name contains only alphabetic characters and spaces
        for (char c : name.toCharArray()) {
            if (!Character.isLetter(c) && c != ' ') {
                throw new ValidationException("ERROR: User Name {" + name + "} is wrong");
            }
        }
    }
    
    /**
     * Validates the user ID.
     * Rules:
     * 1. Must be exactly 9 characters long
     * 2. Must be alphanumeric
     * 3. Must start with numbers
     * 4. May end with only one alphabetic character (uppercase)
     * 
     * @param id the user ID to validate
     * @throws ValidationException if the ID is invalid
     */
    public void validateId(String id) throws ValidationException {
        if (id == null || id.length() != 9) {
            throw new ValidationException("ERROR: User Id {" + id + "} is wrong");
        }
        
        // Check if first character is a digit
        if (!Character.isDigit(id.charAt(0))) {
            throw new ValidationException("ERROR: User Id {" + id + "} is wrong");
        }
        
        // Check pattern: must be digits, optionally ending with one uppercase letter
        // Valid patterns: DDDDDDDDD or DDDDDDDDL (where D=digit, L=uppercase letter)
        
        int letterCount = 0;
        boolean foundLetter = false;
        
        for (int i = 0; i < id.length(); i++) {
            char c = id.charAt(i);
            
            if (Character.isLetter(c)) {
                // Must be uppercase
                if (!Character.isUpperCase(c)) {
                    throw new ValidationException("ERROR: User Id {" + id + "} is wrong");
                }
                letterCount++;
                foundLetter = true;
                
                // Letter can only be at the end
                if (i != id.length() - 1) {
                    throw new ValidationException("ERROR: User Id {" + id + "} is wrong");
                }
            } else if (Character.isDigit(c)) {
                // If we already found a letter, no more digits allowed
                if (foundLetter) {
                    throw new ValidationException("ERROR: User Id {" + id + "} is wrong");
                }
            } else {
                // Invalid character (not alphanumeric)
                throw new ValidationException("ERROR: User Id {" + id + "} is wrong");
            }
        }
        
        // Check if more than one letter
        if (letterCount > 1) {
            throw new ValidationException("ERROR: User Id {" + id + "} is wrong");
        }
    }
    
    /**
     * Checks if the user ID is unique (not already used).
     * 
     * @param id the user ID to check
     * @throws ValidationException if the ID is already used
     */
    public void checkUniqueUserId(String id) throws ValidationException {
        if (usedUserIds.contains(id)) {
            throw new ValidationException("ERROR: User Id {" + id + "} is wrong");
        }
        usedUserIds.add(id);
    }
    
    /**
     * Validates both name and ID of a user, and checks ID uniqueness.
     * Validates name first, then ID, then uniqueness (stops at first error).
     * 
     * @param name the user name
     * @param id the user ID
     * @throws ValidationException if name, ID, or uniqueness check fails
     */
    public void validateUser(String name, String id) throws ValidationException {
        // Validate name first (as per requirement to stop at first error)
        validateName(name);
        // Then validate ID format
        validateId(id);
        // Finally check uniqueness
        checkUniqueUserId(id);
    }
    
    /**
     * Resets the set of used user IDs.
     * Useful for testing or processing multiple batches.
     * 
     * TESTING NOTE:
     * Call this method in @Before or @BeforeEach to reset state between tests.
     */
    public void reset() {
        usedUserIds.clear();
    }
}
