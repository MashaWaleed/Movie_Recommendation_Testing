#!/bin/bash

# Compilation and Execution Script for Movie Recommendation System

echo "========================================="
echo "Movie Recommendation System - Build Script"
echo "========================================="
echo ""

# Create bin directory if it doesn't exist
if [ ! -d "bin" ]; then
    echo "Creating bin directory..."
    mkdir bin
fi

# Create output directory if it doesn't exist
if [ ! -d "output" ]; then
    echo "Creating output directory..."
    mkdir output
fi

echo "Compiling Java source files..."
echo ""

# Compile all Java files (targeting Java 11 compatibility)
javac -source 11 -target 11 -d bin \
    src/main/java/com/movie/recommendation/exception/*.java \
    src/main/java/com/movie/recommendation/model/*.java \
    src/main/java/com/movie/recommendation/validator/*.java \
    src/main/java/com/movie/recommendation/parser/*.java \
    src/main/java/com/movie/recommendation/service/*.java \
    src/main/java/com/movie/recommendation/*.java

# Check if compilation was successful
if [ $? -eq 0 ]; then
    echo "✓ Compilation successful!"
    echo ""
    echo "Running the application..."
    echo "========================================="
    echo ""
    
    # Run the application
    java -cp bin com.movie.recommendation.Main
    
    echo ""
    echo "========================================="
    echo "Application finished."
    echo ""
    
    # Display output file if it exists
    if [ -f "output/recommendations.txt" ]; then
        echo "Output file generated: output/recommendations.txt"
        echo ""
        echo "Contents:"
        echo "----------------------------------------"
        cat output/recommendations.txt
        echo "----------------------------------------"
    fi
else
    echo "✗ Compilation failed!"
    echo "Please check the error messages above."
    exit 1
fi
