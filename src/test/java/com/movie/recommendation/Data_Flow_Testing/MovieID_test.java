package com.movie.recommendation.parser;
import com.movie.recommendation.model.Movie;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.movie.recommendation.validator.MovieValidator;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;
import com.movie.recommendation.exception.ValidationException;
/**
 *
 * @author Mina_Antony
 */
public class MovieID_test {
    
@Test
public void testValidateId_Valid() throws Exception {
    MovieValidator validator = new MovieValidator();
    validator.validateId("TDK123", "The Dark Knight");
}

@Test
public void testValidateId_NullId() {
    MovieValidator validator = new MovieValidator();
    assertThrows(ValidationException.class, () ->
        validator.validateId(null, "Movie")
    );
}

@Test
public void testValidateId_ExtraCharacters() {
    MovieValidator validator = new MovieValidator();
    assertThrows(ValidationException.class, () ->
        validator.validateId("TDK12A", "The Dark Knight")
    );
}

@Test
public void testValidateId_LetterMismatch() {
    MovieValidator validator = new MovieValidator();
    assertThrows(ValidationException.class, () ->
        validator.validateId("ABC123", "The Dark Knight")
    );
}

@Test
public void testValidateId_WrongDigitCount() {
    MovieValidator validator = new MovieValidator();
    assertThrows(ValidationException.class, () ->
        validator.validateId("TDK12", "The Dark Knight")
    );
}

@Test
public void testValidateId_DuplicateDigits() {
    MovieValidator validator = new MovieValidator();
    assertThrows(ValidationException.class, () ->
        validator.validateId("TDK112", "The Dark Knight")
    );
}
}