/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package com.movie.recommendation.parser;
import com.movie.recommendation.model.User;
import com.movie.recommendation.exception.ValidationException;
import com.movie.recommendation.validator.UserValidator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;
/**
 *
 * @author Mina_Antony
 */
public class UserParserTest {
 
@Test
public void testParseUsers_Valid() throws Exception {
    Path temp = Files.createTempFile("users", ".txt");
    Files.write(temp, List.of(
            "John Doe,123456789",          // valid user
            "M123,M245,M357"                // movie IDs
    ));

    UserParser parser = new UserParser(new UserValidator());
    List<User> users = parser.parseUsers(temp.toString());

    assertEquals(1, users.size());
    assertEquals("John Doe", users.get(0).getName());
    assertEquals(List.of("M123", "M245", "M357"), users.get(0).getLikedMovieIds());
}

@Test
public void testParseUsers_EmptyLine() throws Exception {
    Path temp = Files.createTempFile("users", ".txt");
    Files.write(temp, List.of(
            "",
            "John Doe,123456789",          // valid user
            "M123,M245,M357"                // movie IDs
    ));

    UserParser parser = new UserParser(new UserValidator());
    List<User> users = parser.parseUsers(temp.toString());

    assertEquals(1, users.size());
    assertEquals("John Doe", users.get(0).getName());
    assertEquals(List.of("M123", "M245", "M357"), users.get(0).getLikedMovieIds());
}

@Test
public void testParseUsers_InvalidName() throws Exception {
    Path temp = Files.createTempFile("users", ".txt");
    Files.write(temp, List.of(
            "John Doe",          // valid user
            "M123,M245,M357"                // movie IDs
    ));

    UserParser parser = new UserParser(new UserValidator());
    List<User> users = parser.parseUsers(temp.toString());

    assertEquals(1, users.size());
    assertEquals("John Doe", users.get(0).getName());
    assertEquals(List.of("M123", "M245", "M357"), users.get(0).getLikedMovieIds());
}


}