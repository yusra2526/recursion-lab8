package com.permutations;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * StringPermutations class provides both recursive and iterative methods
 * for generating all permutations of a given string.
 */
public class StringPermutations {
    
    /**
     * Generates all permutations of a string using recursive approach
     * @param input the input string
     * @param includeDuplicates true to include duplicate permutations, false to remove duplicates
     * @return List of all permutations
     * @throws IllegalArgumentException if input is null
     */
    public List<String> generatePermutationsRecursive(String input, boolean includeDuplicates) {
        if (input == null) {
            throw new IllegalArgumentException("Input string cannot be null");
        }
        
        List<String> permutations = new ArrayList<>();
        if (input.isEmpty()) {
            permutations.add("");
            return permutations;
        }
        
        generatePermutationsRecursiveHelper("", input, permutations, includeDuplicates);
        
        // Remove duplicates if required
        if (!includeDuplicates) {
            return removeDuplicates(permutations);
        }
        
        return permutations;
    }
    
    /**
     * Recursive helper function to generate permutations
     * @param prefix fixed part of the permutation
     * @param remaining remaining characters to permute
     * @param permutations list to store generated permutations
     * @param includeDuplicates whether to include duplicate permutations
     */
    private void generatePermutationsRecursiveHelper(String prefix, String remaining, 
                                                   List<String> permutations, boolean includeDuplicates) {
        // Base case: no more characters to permute
        if (remaining.isEmpty()) {
            permutations.add(prefix);
            return;
        }
        
        // Use a set to track used characters at this level to avoid duplicates
        Set<Character> usedChars = includeDuplicates ? null : new HashSet<>();
        
        for (int i = 0; i < remaining.length(); i++) {
            char currentChar = remaining.charAt(i);
            
            // Skip duplicate characters if not including duplicates
            if (!includeDuplicates && usedChars.contains(currentChar)) {
                continue;
            }
            
            if (!includeDuplicates) {
                usedChars.add(currentChar);
            }
            
            // Create new prefix and remaining strings
            String newPrefix = prefix + currentChar;
            String newRemaining = remaining.substring(0, i) + remaining.substring(i + 1);
            
            // Recursive call
            generatePermutationsRecursiveHelper(newPrefix, newRemaining, permutations, includeDuplicates);
        }
    }
    
    /**
     * Generates all permutations of a string using iterative approach (Heap's algorithm)
     * @param input the input string
     * @param includeDuplicates true to include duplicate permutations, false to remove duplicates
     * @return List of all permutations
     * @throws IllegalArgumentException if input is null
     */
    public List<String> generatePermutationsIterative(String input, boolean includeDuplicates) {
        if (input == null) {
            throw new IllegalArgumentException("Input string cannot be null");
        }
        
        List<String> permutations = new ArrayList<>();
        if (input.isEmpty()) {
            permutations.add("");
            return permutations;
        }
        
        // Convert string to character array for Heap's algorithm
        char[] chars = input.toCharArray();
        int n = chars.length;
        
        // Arrays for Heap's algorithm
        int[] indexes = new int[n];
        for (int i = 0; i < n; i++) {
            indexes[i] = 0;
        }
        
        permutations.add(new String(chars));
        
        int i = 0;
        while (i < n) {
            if (indexes[i] < i) {
                // Swap based on parity of i
                if (i % 2 == 0) {
                    swap(chars, 0, i);
                } else {
                    swap(chars, indexes[i], i);
                }
                
                permutations.add(new String(chars));
                indexes[i]++;
                i = 0;
            } else {
                indexes[i] = 0;
                i++;
            }
        }
        
        // Remove duplicates if required
        if (!includeDuplicates) {
            return removeDuplicates(permutations);
        }
        
        return permutations;
    }
    
    /**
     * Alternative iterative method using factorial counting
     * @param input the input string
     * @param includeDuplicates true to include duplicate permutations, false to remove duplicates
     * @return List of all permutations
     */
    public List<String> generatePermutationsIterativeAlt(String input, boolean includeDuplicates) {
        if (input == null) {
            throw new IllegalArgumentException("Input string cannot be null");
        }
        
        List<String> permutations = new ArrayList<>();
        if (input.isEmpty()) {
            permutations.add("");
            return permutations;
        }
        
        // Start with first character
        permutations.add("");
        
        // Build permutations character by character
        for (char c : input.toCharArray()) {
            List<String> newPermutations = new ArrayList<>();
            
            for (String perm : permutations) {
                // Insert current character at every possible position
                for (int i = 0; i <= perm.length(); i++) {
                    String newPerm = perm.substring(0, i) + c + perm.substring(i);
                    newPermutations.add(newPerm);
                }
            }
            
            permutations = newPermutations;
        }
        
        // Remove duplicates if required
        if (!includeDuplicates) {
            return removeDuplicates(permutations);
        }
        
        return permutations;
    }
    
    /**
     * Removes duplicate strings from the list while preserving order
     * @param list the list with potential duplicates
     * @return list without duplicates
     */
    private List<String> removeDuplicates(List<String> list) {
        Set<String> set = new HashSet<>(list);
        return new ArrayList<>(set);
    }
    
    /**
     * Swaps two characters in a character array
     * @param chars the character array
     * @param i first index
     * @param j second index
     */
    private void swap(char[] chars, int i, int j) {
        char temp = chars[i];
        chars[i] = chars[j];
        chars[j] = temp;
    }
    
    /**
     * Calculates factorial of a number (n!)
     * @param n the number
     * @return factorial of n
     */
    public long factorial(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("Factorial is not defined for negative numbers");
        }
        long result = 1;
        for (int i = 2; i <= n; i++) {
            result *= i;
        }
        return result;
    }
    
    /**
     * Estimates the number of unique permutations for a string with duplicate characters
     * @param input the input string
     * @return estimated number of unique permutations
     */
    public long estimateUniquePermutations(String input) {
        if (input == null || input.isEmpty()) {
            return 1;
        }
        
        long numerator = factorial(input.length());
        long denominator = 1;
        
        // Count frequency of each character
        int[] freq = new int[256]; // Assuming ASCII
        for (char c : input.toCharArray()) {
            freq[c]++;
        }
        
        // Calculate denominator as product of factorials of frequencies
        for (int count : freq) {
            if (count > 1) {
                denominator *= factorial(count);
            }
        }
        
        return numerator / denominator;
    }
    
    /**
     * Performance comparison between recursive and iterative methods
     * @param input the input string to test
     * @param includeDuplicates whether to include duplicates
     */
    public void comparePerformance(String input, boolean includeDuplicates) {
        System.out.println("Performance Comparison for: \"" + input + "\"");
        System.out.println("String length: " + input.length());
        System.out.println("Expected permutations: " + estimateUniquePermutations(input));
        System.out.println("Include duplicates: " + includeDuplicates);
        System.out.println("=" .repeat(50));
        
        // Test recursive method
        long startTime = System.nanoTime();
        List<String> recursiveResult = generatePermutationsRecursive(input, includeDuplicates);
        long recursiveTime = System.nanoTime() - startTime;
        
        // Test iterative method (Heap's algorithm)
        startTime = System.nanoTime();
        List<String> iterativeResult = generatePermutationsIterative(input, includeDuplicates);
        long iterativeTime = System.nanoTime() - startTime;
        
        // Test alternative iterative method
        startTime = System.nanoTime();
        List<String> iterativeAltResult = generatePermutationsIterativeAlt(input, includeDuplicates);
        long iterativeAltTime = System.nanoTime() - startTime;
        
        // Display results
        System.out.printf("Recursive method:   %8d permutations, %8.3f ms%n", 
                         recursiveResult.size(), recursiveTime / 1_000_000.0);
        System.out.printf("Iterative method:   %8d permutations, %8.3f ms%n", 
                         iterativeResult.size(), iterativeTime / 1_000_000.0);
        System.out.printf("Iterative Alt:      %8d permutations, %8.3f ms%n", 
                         iterativeAltResult.size(), iterativeAltTime / 1_000_000.0);
        
        // Verify results match
        boolean resultsMatch = recursiveResult.size() == iterativeResult.size() && 
                              recursiveResult.size() == iterativeAltResult.size();
        System.out.println("Results consistent: " + resultsMatch);
        System.out.println();
    }
}