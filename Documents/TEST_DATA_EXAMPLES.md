# Test Data Examples

This directory contains sample input files for testing various scenarios.

## Valid Test Data

### Example 1: Basic Valid Data

**movies.txt**:
```
The Dark Knight,TDK123
action,thriller
```

**users.txt**:
```
John Doe,123456789
TDK123
```

**Expected Output**: User has no recommendations (already liked the only action/thriller movie)

---

### Example 2: Multiple Genres

**movies.txt**:
```
The Dark Knight,TDK123
action,thriller,drama
Inception,I456
action,sci-fi,thriller
The Godfather,TG789
crime,drama
```

**users.txt**:
```
John Doe,123456789
TDK123
```

**Expected Output**: John Doe should get Inception (action, thriller) and The Godfather (drama)

---

## Invalid Test Data - Movie Title Errors

### Test Case 1: Lowercase Start

**movies.txt**:
```
the Dark Knight,TDK123
action,thriller
```

**Expected Error**: `ERROR: Movie Title {the Dark Knight} is wrong`

---

### Test Case 2: Lowercase Middle Word

**movies.txt**:
```
The dark Knight,TDK123
action,thriller
```

**Expected Error**: `ERROR: Movie Title {The dark Knight} is wrong`

---

## Invalid Test Data - Movie ID Errors

### Test Case 3: Wrong Letters in ID

**movies.txt**:
```
The Dark Knight,ABC123
action,thriller
```

**Expected Error**: `ERROR: Movie Id letters {ABC123} are wrong`

(Correct letters should be "TDK" from "The Dark Knight")

---

### Test Case 4: Missing Letters in ID

**movies.txt**:
```
The Dark Knight,TD123
action,thriller
```

**Expected Error**: `ERROR: Movie Id letters {TD123} are wrong`

---

### Test Case 5: Extra Letters in ID

**movies.txt**:
```
The Dark Knight,TDKR123
action,thriller
```

**Expected Error**: `ERROR: Movie Id letters {TDKR123} are wrong`

---

### Test Case 6: Non-Unique Numbers (All Same)

**movies.txt**:
```
The Dark Knight,TDK111
action,thriller
```

**Expected Error**: `ERROR: Movie Id numbers {TDK111} aren't unique`

---

### Test Case 7: Non-Unique Numbers (Two Same)

**movies.txt**:
```
The Dark Knight,TDK112
action,thriller
```

**Expected Error**: `ERROR: Movie Id numbers {TDK112} aren't unique`

---

### Test Case 8: Wrong Number Count (Too Few)

**movies.txt**:
```
The Dark Knight,TDK12
action,thriller
```

**Expected Error**: `ERROR: Movie Id numbers {TDK12} aren't unique`

---

### Test Case 9: Wrong Number Count (Too Many)

**movies.txt**:
```
The Dark Knight,TDK1234
action,thriller
```

**Expected Error**: `ERROR: Movie Id numbers {TDK1234} aren't unique`

---

## Invalid Test Data - User Name Errors

### Test Case 10: Name Starts with Space

**movies.txt**:
```
The Dark Knight,TDK123
action,thriller
```

**users.txt**:
```
 John Doe,123456789
TDK123
```

**Expected Error**: `ERROR: User Name { John Doe} is wrong`

---

### Test Case 11: Name Contains Numbers

**movies.txt**:
```
The Dark Knight,TDK123
action,thriller
```

**users.txt**:
```
John123,123456789
TDK123
```

**Expected Error**: `ERROR: User Name {John123} is wrong`

---

### Test Case 12: Name Contains Special Characters

**movies.txt**:
```
The Dark Knight,TDK123
action,thriller
```

**users.txt**:
```
John@Doe,123456789
TDK123
```

**Expected Error**: `ERROR: User Name {John@Doe} is wrong`

---

## Invalid Test Data - User ID Errors

### Test Case 13: ID Too Short (8 characters)

**movies.txt**:
```
The Dark Knight,TDK123
action,thriller
```

**users.txt**:
```
John Doe,12345678
TDK123
```

**Expected Error**: `ERROR: User Id {12345678} is wrong`

---

### Test Case 14: ID Too Long (10 characters)

**movies.txt**:
```
The Dark Knight,TDK123
action,thriller
```

**users.txt**:
```
John Doe,1234567890
TDK123
```

**Expected Error**: `ERROR: User Id {1234567890} is wrong`

---

### Test Case 15: ID Starts with Letter

**movies.txt**:
```
The Dark Knight,TDK123
action,thriller
```

**users.txt**:
```
John Doe,A12345678
TDK123
```

**Expected Error**: `ERROR: User Id {A12345678} is wrong`

---

### Test Case 16: ID Ends with Two Letters

**movies.txt**:
```
The Dark Knight,TDK123
action,thriller
```

**users.txt**:
```
John Doe,1234567AB
TDK123
```

**Expected Error**: `ERROR: User Id {1234567AB} is wrong`

---

### Test Case 17: ID Has Letter in Middle

**movies.txt**:
```
The Dark Knight,TDK123
action,thriller
```

**users.txt**:
```
John Doe,1234A5678
TDK123
```

**Expected Error**: `ERROR: User Id {1234A5678} is wrong`

---

### Test Case 18: ID Has Lowercase Letter

**movies.txt**:
```
The Dark Knight,TDK123
action,thriller
```

**users.txt**:
```
John Doe,12345678a
TDK123
```

**Expected Error**: `ERROR: User Id {12345678a} is wrong`

---

### Test Case 19: Duplicate User IDs

**movies.txt**:
```
The Dark Knight,TDK123
action,thriller
```

**users.txt**:
```
John Doe,123456789
TDK123
Jane Smith,123456789
TDK123
```

**Expected Error**: `ERROR: User Id {123456789} is wrong`

---

## Complex Test Scenarios

### Test Case 20: Multiple Errors (Should Report First Only)

**movies.txt**:
```
the Dark Knight,ABC111
action,thriller
```

**Expected Error**: `ERROR: Movie Title {the Dark Knight} is wrong`

(Even though ID also has errors, only first error is reported)

---

### Test Case 21: Large Dataset

**movies.txt**: (20 movies with various genres)
**users.txt**: (10 users with different preferences)

**Purpose**: Test performance and correctness with larger datasets

---

### Test Case 22: Genre Edge Cases

**movies.txt**:
```
Movie A,MA123
action,ACTION,Action
Movie B,MB456
action
```

**users.txt**:
```
John Doe,123456789
MA123
```

**Expected Output**: Should handle case-insensitive genre matching

---

### Test Case 23: User Likes All Movies in Genre

**movies.txt**:
```
Movie A,MA123
action
Movie B,MB456
action
```

**users.txt**:
```
John Doe,123456789
MA123,MB456
```

**Expected Output**: No recommendations (user already liked all action movies)

---

### Test Case 24: Movie with No Genres

**movies.txt**:
```
Movie A,MA123

```

**Expected Output**: Movie parsed with empty genre list

---

### Test Case 25: User with No Liked Movies

**users.txt**:
```
John Doe,123456789

```

**Expected Output**: User parsed with empty liked movies list, no recommendations

---

## How to Use These Test Cases

### For Unit Testing:
1. Create test methods for each test case
2. Use the input data in your test
3. Assert the expected output or error

### For Integration Testing:
1. Create test input files
2. Run the complete application
3. Verify the output file contents

### For Manual Testing:
1. Copy the input data to movies.txt and users.txt
2. Run the application
3. Check recommendations.txt for expected output

---

## Test Coverage Matrix

| Validation Rule | Test Cases | Status |
|----------------|------------|--------|
| Movie title capitalization | 1, 2 | ✓ |
| Movie ID letters | 3, 4, 5 | ✓ |
| Movie ID numbers uniqueness | 6, 7 | ✓ |
| Movie ID numbers count | 8, 9 | ✓ |
| User name format | 10, 11, 12 | ✓ |
| User ID length | 13, 14 | ✓ |
| User ID format | 15, 16, 17, 18 | ✓ |
| User ID uniqueness | 19 | ✓ |
| Multiple errors | 20 | ✓ |
| Large datasets | 21 | ✓ |
| Genre matching | 22 | ✓ |
| Edge cases | 23, 24, 25 | ✓ |
