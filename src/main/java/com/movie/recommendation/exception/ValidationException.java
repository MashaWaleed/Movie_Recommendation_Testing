package com.movie.recommendation.exception;

/**
 * Custom exception for validation errors in the movie recommendation system.
 * 
 * TESTING NOTES:
 * - Unit Test: Create test that throws this exception and verify the message
 * - Test that exception message contains the correct error details
 * - Example test:
 *   @Test(expected = ValidationException.class)
 *   public void testValidationExceptionThrown() {
 *       throw new ValidationException("Test error");
 *   }
 */
public class ValidationException extends Exception {
    
    /**
     * Constructs a new ValidationException with the specified detail message.
     * 
     * @param message the detail message explaining the validation error
     */
    public ValidationException(String message) {
        super(message);
    }
    
    /**
     * Constructs a new ValidationException with the specified detail message and cause.
     * 
     * @param message the detail message
     * @param cause the cause of the exception
     */
    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
