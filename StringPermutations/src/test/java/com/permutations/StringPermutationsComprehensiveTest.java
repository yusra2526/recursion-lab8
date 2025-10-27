package com.permutations;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import static org.junit.jupiter.api.Assertions.*;
// Remove this problematic line:
// import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.util.List;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Basic JUnit tests for StringPermutations class
 */
class StringPermutationsTest {
    
    private StringPermutations permutations;
    
    @BeforeEach
    void setUp() {
        permutations = new StringPermutations();
    }
    
    @Test
    void testEmptyString() {
        List<String> result = permutations.generatePermutationsRecursive("", true);
        assertEquals(1, result.size());
        assertEquals("", result.get(0));
    }
    
    @Test
    void testSingleCharacter() {
        List<String> result = permutations.generatePermutationsRecursive("a", true);
        assertEquals(1, result.size());
        assertEquals("a", result.get(0));
    }
    
    @Test
    void testTwoCharacters() {
        List<String> result = permutations.generatePermutationsRecursive("ab", true);
        assertEquals(2, result.size());
        assertTrue(result.contains("ab"));
        assertTrue(result.contains("ba"));
    }
    
    @Test
    void testThreeCharacters() {
        List<String> result = permutations.generatePermutationsRecursive("abc", true);
        assertEquals(6, result.size());
        assertTrue(result.contains("abc"));
        assertTrue(result.contains("acb"));
        assertTrue(result.contains("bac"));
        assertTrue(result.contains("bca"));
        assertTrue(result.contains("cab"));
        assertTrue(result.contains("cba"));
    }
    
    @Test
    void testDuplicateCharactersIncludeDuplicates() {
        List<String> result = permutations.generatePermutationsRecursive("aab", true);
        // With duplicates: 3! = 6 permutations
        assertEquals(6, result.size());
    }
    
    @Test
    void testDuplicateCharactersExcludeDuplicates() {
        List<String> result = permutations.generatePermutationsRecursive("aab", false);
        // Without duplicates: 3! / 2! = 3 unique permutations
        assertEquals(3, result.size());
        
        Set<String> uniqueResults = new HashSet<>(result);
        assertEquals(3, uniqueResults.size());
        
        assertTrue(result.contains("aab"));
        assertTrue(result.contains("aba"));
        assertTrue(result.contains("baa"));
    }
    
    @Test
    void testIterativeAlgorithm() {
        List<String> result = permutations.generatePermutationsIterative("abc", true);
        assertEquals(6, result.size());
        assertTrue(result.contains("abc"));
        assertTrue(result.contains("cba"));
    }
    
    @Test
    void testFactorialCalculation() {
        assertEquals(1, permutations.factorial(0));
        assertEquals(1, permutations.factorial(1));
        assertEquals(2, permutations.factorial(2));
        assertEquals(6, permutations.factorial(3));
        assertEquals(24, permutations.factorial(4));
        assertEquals(120, permutations.factorial(5));
    }
    
    @Test
    void testNullInput() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            permutations.generatePermutationsRecursive(null, true);
        });
        assertTrue(exception.getMessage().contains("cannot be null"));
    }
}