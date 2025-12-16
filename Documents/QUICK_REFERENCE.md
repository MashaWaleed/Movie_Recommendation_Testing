# Quick Reference Guide

## Project Overview
Movie Recommendation System - A Java application that recommends movies to users based on genre preferences.

## Quick Start

### Compile and Run
```bash
# Linux/Mac
./build.sh

# Windows
build.bat

# Manual compilation
javac -d bin src/main/java/com/movie/recommendation/**/*.java src/main/java/com/movie/recommendation/*.java

# Manual execution
java -cp bin com.movie.recommendation.Main
```

## Input File Rules

### movies.txt Format
```
Movie Title,MOVIEID
genre1,genre2
```

**Movie Title Rules:**
- Every word must start with a CAPITAL letter
- Examples: ✓ "The Dark Knight", ✗ "the Dark Knight"

**Movie ID Rules:**
- All capital letters from title + 3 unique numbers
- Examples: "The Dark Knight" → ✓ "TDK123", ✗ "TDK111" (not unique)

### users.txt Format
```
User Name,UserId
MOVIEID1,MOVIEID2
```

**User Name Rules:**
- Only alphabetic characters and spaces
- Must NOT start with space
- Examples: ✓ "John Doe", ✗ " John Doe", ✗ "John123"

**User ID Rules:**
- Exactly 9 characters
- Must start with numbers
- May end with ONE uppercase letter
- Examples: ✓ "123456789", ✓ "12345678A", ✗ "1234567AB"

## Output

### Success Case (recommendations.txt)
```
User Name,User Id
Recommended Movie 1,Recommended Movie 2
```

### Error Case (recommendations.txt)
```
Error
ERROR: Movie Title {title} is wrong
```

## Error Messages Reference

| Error Type | Message Format |
|------------|----------------|
| Movie Title | `ERROR: Movie Title {movie_title} is wrong` |
| Movie ID Letters | `ERROR: Movie Id letters {movie_id} are wrong` |
| Movie ID Numbers | `ERROR: Movie Id numbers {movie_id} aren't unique` |
| User Name | `ERROR: User Name {user_name} is wrong` |
| User ID | `ERROR: User Id {user_id} is wrong` |

## Testing Instructions

See `TESTING_GUIDE.md` for comprehensive testing documentation.

### Key Testing Areas

1. **Unit Tests**
   - MovieValidator: Title and ID validation
   - UserValidator: Name and ID validation
   - RecommendationService: Recommendation algorithm

2. **Integration Tests**
   - Complete file processing flow
   - Error handling and reporting
   - First error only rule

3. **Test Coverage Goals**
   - Unit tests: 90%+ coverage
   - All validation rules tested
   - All error messages verified

## Project Structure

```
Movie_Recommendation_Testing/
├── src/main/java/com/movie/recommendation/
│   ├── Main.java                    # Entry point
│   ├── exception/
│   │   └── ValidationException.java # Custom exception
│   ├── model/
│   │   ├── Movie.java              # Movie data model
│   │   └── User.java               # User data model
│   ├── validator/
│   │   ├── MovieValidator.java     # Movie validation
│   │   └── UserValidator.java      # User validation
│   ├── parser/
│   │   ├── MovieParser.java        # Parse movies file
│   │   └── UserParser.java         # Parse users file
│   └── service/
│       └── RecommendationService.java # Recommendation logic
├── input/
│   ├── movies.txt                  # Input: movies data
│   └── users.txt                   # Input: users data
├── output/
│   └── recommendations.txt         # Output: recommendations
├── README.md                       # Full documentation
├── TESTING_GUIDE.md               # Testing documentation
└── QUICK_REFERENCE.md             # This file
```

## How It Works

1. **Validation**: All inputs validated before processing
2. **Parsing**: Files parsed into Movie and User objects
3. **Recommendation**: For each user:
   - Find genres of liked movies
   - Find other movies with same genres
   - Exclude already liked movies
4. **Output**: Write recommendations or first error

## Example

### Input Files

**movies.txt:**
```
The Dark Knight,TDK123
action,thriller
Inception,I456
action,sci-fi
```

**users.txt:**
```
John Doe,123456789
TDK123
```

### Expected Output

**recommendations.txt:**
```
John Doe,123456789
Inception
```

(Inception recommended because it shares "action" genre with The Dark Knight)

## Common Issues

### Compilation Errors
- Ensure Java JDK is installed: `java -version`
- Check file structure matches project layout
- Verify all source files are present

### Runtime Errors
- Check input files exist in `input/` directory
- Verify file format matches specification
- Check file permissions

### Incorrect Output
- Verify input data follows all validation rules
- Check movie IDs in users.txt match movies.txt
- Ensure genres are spelled consistently

## Testing Checklist

Before considering testing complete:

- [ ] All validators tested (Movie, User)
- [ ] All models tested (Movie, User)
- [ ] Parser tests (Movie, User)
- [ ] Service tests (RecommendationService)
- [ ] Integration tests (full flow)
- [ ] Error case tests (each validation rule)
- [ ] Edge case tests (empty files, no matches, etc.)
- [ ] File I/O tests
- [ ] First error only rule verified

## Additional Resources

- **README.md**: Complete project documentation with testing strategy
- **TESTING_GUIDE.md**: Detailed testing instructions with code examples
- Source code comments: Every class has detailed testing notes
