package com.filesearch;

import java.util.List;
import java.util.Scanner;

/**
 * Main application class for recursive file search
 * Handles command-line arguments and user interaction
 */
public class FileSearchApp {
    
    /**
     * Main method - entry point of the application
     * @param args command line arguments
     */
    public static void main(String[] args) {
        // If command line arguments are provided, use them
        if (args.length >= 2) {
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
            String directoryPath = args[0];
            boolean caseSensitive = false;
            
            // Check for case sensitivity option
            int fileStartIndex = 1;
            if (args[1].equalsIgnoreCase("-case") || args[1].equalsIgnoreCase("-c")) {
                caseSensitive = true;
                fileStartIndex = 2;
            }
            
            // Extract file names
            String[] fileNames = new String[args.length - fileStartIndex];
            for (int i = fileStartIndex; i < args.length; i++) {
                fileNames[i - fileStartIndex] = args[i];
            }
            
            performSearch(directoryPath, fileNames, caseSensitive);
            
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
        
        System.out.println("=== Recursive File Search ===");
        
        try {
            // Get directory path
            System.out.print("Enter directory path to search: ");
            String directoryPath = scanner.nextLine();
            
            // Get case sensitivity preference
            System.out.print("Case sensitive search? (y/n): ");
            boolean caseSensitive = scanner.nextLine().trim().equalsIgnoreCase("y");
            
            // Get file names
            System.out.print("Enter file names to search (separated by commas): ");
            String fileInput = scanner.nextLine();
            String[] fileNames = fileInput.split(",");
            
            performSearch(directoryPath, fileNames, caseSensitive);
            
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }
    
    /**
     * Performs the file search operation
     * @param directoryPath directory to search in
     * @param fileNames files to search for
     * @param caseSensitive case sensitivity setting
     */
    private static void performSearch(String directoryPath, String[] fileNames, boolean caseSensitive) {
        try {
            FileSearcher searcher = new FileSearcher(caseSensitive);
            
            System.out.println("\nSearching for files in: " + directoryPath);
            System.out.println("Case sensitive: " + caseSensitive);
            System.out.println("Files to find: " + String.join(", ", fileNames));
            System.out.println("=" .repeat(50));
            
            List<String> results = searcher.searchFiles(directoryPath, fileNames);
            
            // Display summary
            System.out.println("\n" + "=" .repeat(50));
            System.out.println("SEARCH COMPLETED");
            System.out.println("=" .repeat(50));
            
            if (results.isEmpty()) {
                System.out.println("No files found.");
            } else {
                System.out.println("Found " + results.size() + " file(s):");
                for (int i = 0; i < results.size(); i++) {
                    System.out.println((i + 1) + ". " + results.get(i));
                }
                
                // Count occurrences for each file
                System.out.println("\nOccurrence count:");
                for (String fileName : fileNames) {
                    int count = searcher.countFileOccurrences(fileName.trim());
                    System.out.println("  " + fileName + ": " + count + " time(s)");
                }
            }
            
        } catch (Exception e) {
            System.err.println("Search failed: " + e.getMessage());
        }
    }
    
    /**
     * Prints usage instructions
     */
    private static void printUsage() {
        System.out.println("\nUsage:");
        System.out.println("  java FileSearchApp <directory> [-case] <file1> [file2 ...]");
        System.out.println("\nExamples:");
        System.out.println("  java FileSearchApp /home/user/documents -case readme.txt");
        System.out.println("  java FileSearchApp C:\\Projects config.xml pom.xml");
        System.out.println("  java FileSearchApp (for interactive mode)");
    }
}