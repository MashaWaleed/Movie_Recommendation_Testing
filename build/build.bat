@echo off
REM Compilation and Execution Script for Movie Recommendation System (Windows)

echo =========================================
echo Movie Recommendation System - Build Script
echo =========================================
echo.

REM Create bin directory if it doesn't exist
if not exist bin mkdir bin

REM Create output directory if it doesn't exist
if not exist output mkdir output

echo Compiling Java source files...
echo.

REM Compile all Java files (targeting Java 11 compatibility)
javac -source 11 -target 11 -d bin ^
    src\main\java\com\movie\recommendation\exception\*.java ^
    src\main\java\com\movie\recommendation\model\*.java ^
    src\main\java\com\movie\recommendation\validator\*.java ^
    src\main\java\com\movie\recommendation\parser\*.java ^
    src\main\java\com\movie\recommendation\service\*.java ^
    src\main\java\com\movie\recommendation\*.java

REM Check if compilation was successful
if %errorlevel% equ 0 (
    echo [92m[OK] Compilation successful![0m
    echo.
    echo Running the application...
    echo =========================================
    echo.
    
    REM Run the application
    java -cp bin com.movie.recommendation.Main
    
    echo.
    echo =========================================
    echo Application finished.
    echo.
    
    REM Display output file if it exists
    if exist output\recommendations.txt (
        echo Output file generated: output\recommendations.txt
        echo.
        echo Contents:
        echo ----------------------------------------
        type output\recommendations.txt
        echo ----------------------------------------
    )
) else (
    echo [91m[ERROR] Compilation failed![0m
    echo Please check the error messages above.
    exit /b 1
)

pause
