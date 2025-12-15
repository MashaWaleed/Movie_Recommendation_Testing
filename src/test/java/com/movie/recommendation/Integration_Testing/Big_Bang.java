/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package com.movie.recommendation.Integration_Testing;
import com.movie.recommendation.Main;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Arrays;
import java.util.List;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;
import java.io.BufferedReader;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;


/**
 *
 * @author Mina_Antony
 */
public class Big_Bang {
   
    private static final Path INPUT_DIR = Path.of(
        "F:/Senior-2/Sw Testing/Project/Juint/Testing_Project/test_input");

    private static final Path OUTPUT_DIR = Path.of(
        "F:/Senior-2/Sw Testing/Project/Juint/Testing_Project/test_output");


    @BeforeEach
    void setUp() throws IOException {
        Files.createDirectories(INPUT_DIR);
        Files.createDirectories(OUTPUT_DIR);
    }


    @Test
    void testCompleteFlow_ValidInputs() throws Exception {

        Path moviesFile = INPUT_DIR.resolve("movies.txt");
        Path usersFile  = INPUT_DIR.resolve("users.txt");
        Path outputFile = OUTPUT_DIR.resolve("recommendations.txt");

        String movies =
            "The Dark Knight,TDK123\n" +
            "action,thriller\n" +
            "The Legend,TL012\n" +
            "action,crime\n" +
            "The Godfather,TG456\n" +
            "crime,drama\n"
                ;

        String users =
            "John Doe,123456789\n" +
            "TDK123\n";

        Files.writeString(moviesFile, movies);
        Files.writeString(usersFile, users);

        Main.processFiles(
            moviesFile.toString(),
            usersFile.toString(),
            outputFile.toString()
        );

        String output = Files.readString(outputFile);

        assertTrue(output.contains("John Doe"));
        assertTrue(output.contains("123456789"));
        assertTrue(output.contains("The Legend"));
    }
    
    @Test
    void testCompleteFlow_InvalidMovieTitle() throws Exception {

        Path moviesFile = INPUT_DIR.resolve("movies2.txt");
        Path usersFile  = INPUT_DIR.resolve("users2.txt");
        Path outputFile = OUTPUT_DIR.resolve("recommendations2.txt");

        // Invalid: movie title starts with lowercase
        String movies =
            "the dark knight,TDK123\n" +
            "action,thriller\n";

        String users =
            "John Doe,123456789\n" +
            "TDK123\n";

        Files.writeString(moviesFile, movies);
        Files.writeString(usersFile, users);

        Main.processFiles(
            moviesFile.toString(),
            usersFile.toString(),
            outputFile.toString()
        );

        String output = Files.readString(outputFile);

        assertTrue(output.contains("Error"));
        assertTrue(output.contains("ERROR: Movie Title {the dark knight} is wrong"));
    }

    @Test
    void testCompleteFlow_InvalidUserId() throws Exception {

        Path moviesFile = INPUT_DIR.resolve("movies3.txt");
        Path usersFile  = INPUT_DIR.resolve("users3.txt");
        Path outputFile = OUTPUT_DIR.resolve("recommendations3.txt");

        String movies =
            "The Dark Knight,TDK123\n" +
            "action,thriller\n";

        // Invalid: user ID too short / non-numeric
        String users =
            "John Doe,12A45\n" +
            "TDK123\n";

        Files.writeString(moviesFile, movies);
        Files.writeString(usersFile, users);

        Main.processFiles(
            moviesFile.toString(),
            usersFile.toString(),
            outputFile.toString()
        );

        String output = Files.readString(outputFile);

        assertTrue(output.contains("ERROR"));
        assertTrue(output.contains("ERROR: User Id {12A45} is wrong"));
    }

    
    
}

