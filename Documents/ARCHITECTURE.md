# Movie Recommendation System - Architecture & Design

## System Architecture

### High-Level Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                        Main Application                      │
│                      (Orchestrator Layer)                    │
└────────────────────┬────────────────────────────────────────┘
                     │
        ┌────────────┴────────────┐
        │                         │
┌───────▼────────┐       ┌───────▼────────┐
│  Input Layer   │       │  Output Layer  │
│  (Parsers)     │       │  (Formatter)   │
└───────┬────────┘       └────────────────┘
        │
        │ validates while parsing
        │
┌───────▼────────┐
│ Validation     │
│ Layer          │
└───────┬────────┘
        │
        │ creates validated models
        │
┌───────▼────────┐
│  Model Layer   │
│ (Movie, User)  │
└───────┬────────┘
        │
        │ feeds into
        │
┌───────▼────────┐
│ Service Layer  │
│ (Business      │
│  Logic)        │
└────────────────┘
```

## Component Details

### 1. Model Layer

**Purpose**: Data representation

**Components**:
- `Movie.java`: Represents a movie with title, ID, and genres
- `User.java`: Represents a user with name, ID, and liked movies

**Testing Focus**:
- Constructor validation
- Getter methods
- Business methods (hasGenre, hasLikedMovie)
- Equals/HashCode contracts

### 2. Validator Layer

**Purpose**: Input validation and business rule enforcement

**Components**:
- `MovieValidator.java`: Validates movie title and ID
- `UserValidator.java`: Validates user name and ID, ensures uniqueness

**Validation Rules**:

| Validator | Rule | Error Message |
|-----------|------|---------------|
| MovieValidator | Title words start with capital | `ERROR: Movie Title {title} is wrong` |
| MovieValidator | ID letters match title capitals | `ERROR: Movie Id letters {id} are wrong` |
| MovieValidator | ID has 3 unique numbers | `ERROR: Movie Id numbers {id} aren't unique` |
| UserValidator | Name is alphabetic + spaces | `ERROR: User Name {name} is wrong` |
| UserValidator | Name doesn't start with space | `ERROR: User Name {name} is wrong` |
| UserValidator | ID is 9 chars, starts with digits | `ERROR: User Id {id} is wrong` |
| UserValidator | ID ends with max 1 letter | `ERROR: User Id {id} is wrong` |
| UserValidator | ID is unique | `ERROR: User Id {id} is wrong` |

**Testing Focus**:
- Each validation rule independently
- Edge cases (empty, null, boundary values)
- Error message format
- Uniqueness checking with state

### 3. Parser Layer

**Purpose**: File reading and data parsing

**Components**:
- `MovieParser.java`: Parses movies.txt
- `UserParser.java`: Parses users.txt

**Parsing Strategy**:
1. Read file line by line
2. Parse alternating lines (data line, then details line)
3. Validate during parsing (fail-fast on first error)
4. Create model objects

**Testing Focus**:
- Valid file parsing
- Invalid format handling
- Missing lines
- File I/O errors
- Integration with validators

### 4. Service Layer

**Purpose**: Business logic for recommendations

**Component**: `RecommendationService.java`

**Algorithm**:
```
For each user:
  1. Find all movies the user has liked
  2. Collect all genres from those movies
  3. Find all OTHER movies with any of those genres
  4. Return titles of matching movies (excluding already liked)
```

**Testing Focus**:
- Recommendation accuracy
- Duplicate handling
- Genre matching (case-insensitive)
- Edge cases (no likes, no matches, all liked)
- Output formatting

### 5. Exception Layer

**Purpose**: Custom exception handling

**Component**: `ValidationException.java`

**Usage**: Thrown when any validation rule fails

**Testing Focus**:
- Exception creation
- Message propagation
- Exception handling in calling code

### 6. Main Application

**Purpose**: Application orchestration

**Component**: `Main.java`

**Flow**:
```
1. Initialize validators
2. Initialize parsers with validators
3. Parse movies (validates during parse)
   └─> If error: write error to output, stop
4. Parse users (validates during parse)
   └─> If error: write error to output, stop
5. Generate recommendations
6. Write recommendations to output file
```

**Testing Focus**:
- End-to-end flow
- Error handling
- File I/O
- First error only rule

## Data Flow

### Success Path

```
movies.txt ──┐
             ├─> Parse & Validate ─> Movie Objects ─┐
             │                                       │
users.txt ───┤                                       ├─> Recommendation
             ├─> Parse & Validate ─> User Objects ──┘     Service
             │                                       │
             └─────────────────────────────────────> │
                                                      │
                                                      ▼
                                            recommendations.txt
```

### Error Path

```
movies.txt ──> Parse ──> Validation Error ──┐
                                            │
users.txt ───> Parse ──> Validation Error ──┤
                                            │
                                            ├─> Write Error
                                            │
                                            ▼
                                    recommendations.txt
                                    (contains error)
```

## Design Patterns Used

### 1. **Single Responsibility Principle**
Each class has one clear responsibility:
- Validators: Only validation
- Parsers: Only parsing
- Service: Only business logic
- Models: Only data representation

### 2. **Dependency Injection**
Parsers receive validators through constructor injection, allowing:
- Easy testing with mock validators
- Loose coupling
- Flexibility to change validators

### 3. **Fail-Fast Pattern**
- Validation happens during parsing
- Stops at first error
- Prevents processing invalid data

### 4. **Exception-Based Error Handling**
- ValidationException for all validation errors
- Consistent error reporting
- Easy to test and maintain

## Testing Strategy by Layer

### Unit Testing Strategy

```
Layer 1: Models
  ├─ Test data holding
  ├─ Test business methods
  └─ No external dependencies

Layer 2: Validators
  ├─ Test each validation rule
  ├─ Test error messages
  └─ Test state (uniqueness)

Layer 3: Service
  ├─ Test recommendation algorithm
  ├─ Mock data (no file I/O)
  └─ Test edge cases

Layer 4: Parsers (with mocked validators)
  ├─ Test parsing logic
  ├─ Mock validators to isolate
  └─ Test file format handling
```

### Integration Testing Strategy

```
Integration Tests
  ├─ Parser + Validator integration
  ├─ Service + Models integration
  ├─ Complete file-to-file flow
  └─ Error handling flow
```

## Performance Considerations

### Time Complexity

| Operation | Complexity | Explanation |
|-----------|-----------|-------------|
| Parse movies | O(n) | n = number of movies |
| Parse users | O(u) | u = number of users |
| Get recommendations (per user) | O(m × g) | m = movies, g = genres per movie |
| Overall | O(n + u × m × g) | Linear in users and movies |

### Space Complexity

| Structure | Space | Explanation |
|-----------|-------|-------------|
| Movie storage | O(n) | All movies in memory |
| User storage | O(u) | All users in memory |
| Recommendation map | O(u × r) | r = recommendations per user |
| Validator state | O(u) | User ID uniqueness set |

### Scalability

**Current limits**:
- All data must fit in memory
- Sequential processing

**For large-scale systems**, consider:
- Database storage instead of file I/O
- Batch processing for users
- Caching of genre-to-movie mappings
- Parallel processing of user recommendations

## Error Handling Strategy

### Validation Errors
- **When**: During parsing
- **Action**: Stop processing, write error to output
- **Rule**: First error only

### File I/O Errors
- **When**: File not found, read/write errors
- **Action**: Propagate IOException
- **Handling**: Caught in main, logged to console

### Runtime Errors
- **When**: Unexpected situations
- **Action**: Let exception propagate
- **Handling**: Display stack trace for debugging

## Extension Points

### Adding New Validation Rules
1. Add method to appropriate validator
2. Call from `validateMovie()` or `validateUser()`
3. Add corresponding tests

### Adding New Recommendation Algorithms
1. Create new method in `RecommendationService`
2. Keep existing algorithm for compatibility
3. Test both algorithms

### Supporting New Input Formats
1. Create new parser implementation
2. Keep interface consistent
3. Swap parser in Main

## Security Considerations

1. **File Path Injection**: Use fixed paths or validate input paths
2. **Large Files**: No current limit on file size - could cause OutOfMemoryError
3. **Invalid Data**: All validated before processing
4. **Duplicate IDs**: Prevented by uniqueness check

## Future Enhancements

1. **Configuration File**: Externalize paths and settings
2. **Logging**: Add proper logging instead of console output
3. **Database**: Replace file I/O with database
4. **REST API**: Expose as web service
5. **Machine Learning**: Improve recommendations with ML
6. **User Ratings**: Consider ratings, not just likes
7. **Collaborative Filtering**: Recommend based on similar users
8. **Performance Metrics**: Track recommendation accuracy

## Conclusion

This architecture provides:
- ✓ Clear separation of concerns
- ✓ Easy to test (each layer independently testable)
- ✓ Easy to extend (add new validators, algorithms)
- ✓ Fail-fast validation
- ✓ Consistent error handling
- ✓ Clean, maintainable code

The design prioritizes:
1. **Correctness**: Strict validation ensures data quality
2. **Testability**: Each component can be tested in isolation
3. **Maintainability**: Clear structure, single responsibilities
4. **Extensibility**: Easy to add new features
