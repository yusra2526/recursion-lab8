package com.permutations;

import java.util.List;
import java.util.Scanner;

/**
 * Main application class for generating string permutations
 * Provides interactive and command-line interfaces
 */
public class PermutationApp {
    
    /**
     * Main method - entry point of the application
     * @param args command line arguments
     */
    public static void main(String[] args) {
        // If command line arguments are provided, use them
        if (args.length >= 1) {
            handleCommandLine(args);
        } else {
            // Otherwise, start interactive mode
            startInteractiveMode();
        }
    }
    
    /**
     * Handles command line arguments
     * @param args command line arguments
     */
    private static void handleCommandLine(String[] args) {
        try {
            String input = args[0];
            boolean includeDuplicates = true; // Default to include duplicates
            boolean performanceTest = false;
            String algorithm = "recursive"; // Default algorithm
            
            // Parse command line options
            for (int i = 1; i < args.length; i++) {
                switch (args[i].toLowerCase()) {
                    case "-nodupes":
                    case "-unique":
                        includeDuplicates = false;
                        break;
                    case "-performance":
                    case "-perf":
                        performanceTest = true;
                        break;
                    case "-iterative":
                    case "-iter":
                        algorithm = "iterative";
                        break;
                    case "-recursive":
                    case "-rec":
                        algorithm = "recursive";
                        break;
                    case "-help":
                    case "-h":
                        printUsage();
                        return;
                }
            }
            
            if (performanceTest) {
                runPerformanceTests(input, includeDuplicates);
            } else {
                generateAndDisplayPermutations(input, includeDuplicates, algorithm);
            }
            
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            printUsage();
        }
    }
    
    /**
     * Starts interactive mode for user input
     */
    private static void startInteractiveMode() {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("=== String Permutations Generator ===");
        
        try {
            while (true) {
                System.out.println("\nOptions:");
                System.out.println("1. Generate permutations");
                System.out.println("2. Performance comparison");
                System.out.println("3. Exit");
                System.out.print("Choose option (1-3): ");
                
                String choice = scanner.nextLine().trim();
                
                switch (choice) {
                    case "1":
                        generatePermutationsInteractive(scanner);
                        break;
                    case "2":
                        performanceComparisonInteractive(scanner);
                        break;
                    case "3":
                        System.out.println("Goodbye!");
                        return;
                    default:
                        System.out.println("Invalid option. Please try again.");
                }
            }
            
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }
    
    /**
     * Interactive permutation generation
     * @param scanner the scanner for user input
     */
    private static void generatePermutationsInteractive(Scanner scanner) {
        System.out.print("Enter string to permute: ");
        String input = scanner.nextLine();
        
        System.out.print("Include duplicate permutations? (y/n): ");
        boolean includeDuplicates = scanner.nextLine().trim().equalsIgnoreCase("y");
        
        System.out.print("Algorithm (recursive/iterative): ");
        String algorithm = scanner.nextLine().trim().toLowerCase();
        
        if (!algorithm.equals("recursive") && !algorithm.equals("iterative")) {
            System.out.println("Invalid algorithm. Using recursive.");
            algorithm = "recursive";
        }
        
        generateAndDisplayPermutations(input, includeDuplicates, algorithm);
    }
    
    /**
     * Interactive performance comparison
     * @param scanner the scanner for user input
     */
    private static void performanceComparisonInteractive(Scanner scanner) {
        System.out.print("Enter string for performance test: ");
        String input = scanner.nextLine();
        
        System.out.print("Include duplicate permutations? (y/n): ");
        boolean includeDuplicates = scanner.nextLine().trim().equalsIgnoreCase("y");
        
        runPerformanceTests(input, includeDuplicates);
    }
    
    /**
     * Generates and displays permutations
     * @param input the input string
     * @param includeDuplicates whether to include duplicates
     * @param algorithm the algorithm to use
     */
    private static void generateAndDisplayPermutations(String input, boolean includeDuplicates, String algorithm) {
        StringPermutations generator = new StringPermutations();
        
        try {
            System.out.println("\nGenerating permutations for: \"" + input + "\"");
            System.out.println("Algorithm: " + algorithm);
            System.out.println("Include duplicates: " + includeDuplicates);
            System.out.println("=" .repeat(50));
            
            List<String> permutations;
            long startTime = System.nanoTime();
            
            if (algorithm.equals("iterative")) {
                permutations = generator.generatePermutationsIterative(input, includeDuplicates);
            } else {
                permutations = generator.generatePermutationsRecursive(input, includeDuplicates);
            }
            
            long endTime = System.nanoTime();
            double duration = (endTime - startTime) / 1_000_000.0;
            
            // Display results
            System.out.println("Generated " + permutations.size() + " permutations");
            System.out.printf("Time taken: %.3f ms%n", duration);
            
            // Show first 20 permutations to avoid overwhelming output
            int maxDisplay = Math.min(20, permutations.size());
            System.out.println("\nFirst " + maxDisplay + " permutations:");
            for (int i = 0; i < maxDisplay; i++) {
                System.out.println((i + 1) + ". " + permutations.get(i));
            }
            
            if (permutations.size() > maxDisplay) {
                System.out.println("... and " + (permutations.size() - maxDisplay) + " more");
            }
            
        } catch (Exception e) {
            System.err.println("Error generating permutations: " + e.getMessage());
        }
    }
    
    /**
     * Runs performance comparison tests
     * @param input the input string
     * @param includeDuplicates whether to include duplicates
     */
    private static void runPerformanceTests(String input, boolean includeDuplicates) {
        StringPermutations generator = new StringPermutations();
        generator.comparePerformance(input, includeDuplicates);
        
        // Additional analysis
        System.out.println("Time Complexity Analysis:");
        System.out.println("• Recursive: O(n!) time, O(n!) space (due to recursion stack and result storage)");
        System.out.println("• Iterative: O(n!) time, O(n!) space");
        System.out.println("• For large strings (n > 10), both methods become impractical");
        System.out.println("• Iterative methods avoid recursion stack overflow but still have factorial complexity");
    }
    
    /**
     * Prints usage instructions
     */
    private static void printUsage() {
        System.out.println("\nUsage:");
        System.out.println("  java -jar StringPermutations.jar <string> [options]");
        System.out.println("\nOptions:");
        System.out.println("  -nodupes, -unique   Exclude duplicate permutations");
        System.out.println("  -performance, -perf Run performance comparison");
        System.out.println("  -recursive          Use recursive algorithm (default)");
        System.out.println("  -iterative          Use iterative algorithm");
        System.out.println("  -help, -h           Show this help message");
        System.out.println("\nExamples:");
        System.out.println("  java -jar StringPermutations.jar abc");
        System.out.println("  java -jar StringPermutations.jar aab -nodupes");
        System.out.println("  java -jar StringPermutations.jar abc -performance");
        System.out.println("  java -jar StringPermutations.jar (for interactive mode)");
    }
}