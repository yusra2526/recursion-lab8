package com.filesearch;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * FileSearcher class provides recursive file search functionality
 * with support for multiple files, case sensitivity options, and counting.
 */
public class FileSearcher {
    
    private boolean caseSensitive;
    private List<String> searchResults;
    
    /**
     * Constructor for FileSearcher
     * @param caseSensitive true for case-sensitive search, false for case-insensitive
     */
    public FileSearcher(boolean caseSensitive) {
        this.caseSensitive = caseSensitive;
        this.searchResults = new ArrayList<>();
    }
    
    /**
     * Searches for multiple files in the specified directory and its subdirectories
     * @param directoryPath the path to search in
     * @param fileNames array of file names to search for
     * @return List of found file paths
     * @throws IOException if directory doesn't exist or cannot be accessed
     */
    public List<String> searchFiles(String directoryPath, String[] fileNames) throws IOException {
        searchResults.clear();
        
        // Validate directory
        Path dirPath = Paths.get(directoryPath);
        if (!Files.exists(dirPath) || !Files.isDirectory(dirPath)) {
            throw new IOException("Directory does not exist or is not accessible: " + directoryPath);
        }
        
        // Search for each file
        for (String fileName : fileNames) {
            searchRecursive(dirPath.toFile(), fileName.trim());
        }
        
        return new ArrayList<>(searchResults);
    }
    
    /**
     * Recursive method to search for a file in directory and subdirectories
     * @param currentDir the current directory to search
     * @param fileName the file name to search for
     */
    private void searchRecursive(File currentDir, String fileName) {
        // Check if current directory is accessible
        if (!currentDir.canRead()) {
            System.out.println("Warning: Cannot read directory: " + currentDir.getAbsolutePath());
            return;
        }
        
        File[] files = currentDir.listFiles();
        
        if (files == null) {
            return;
        }
        
        for (File file : files) {
            if (file.isDirectory()) {
                // Recursive call for subdirectories
                searchRecursive(file, fileName);
            } else {
                // Check if file matches the search criteria
                if (matchesFileName(file.getName(), fileName)) {
                    String result = file.getAbsolutePath();
                    searchResults.add(result);
                    System.out.println("Found: " + result);
                }
            }
        }
    }
    
    /**
     * Checks if the actual file name matches the search file name
     * @param actualFileName the actual file name
     * @param searchFileName the file name to search for
     * @return true if matches, false otherwise
     */
    private boolean matchesFileName(String actualFileName, String searchFileName) {
        if (caseSensitive) {
            return actualFileName.equals(searchFileName);
        } else {
            return actualFileName.equalsIgnoreCase(searchFileName);
        }
    }
    
    /**
     * Counts occurrences of a specific file within the search results
     * @param fileName the file name to count
     * @return number of occurrences
     */
    public int countFileOccurrences(String fileName) {
        int count = 0;
        for (String result : searchResults) {
            String resultFileName = Paths.get(result).getFileName().toString();
            if (matchesFileName(resultFileName, fileName)) {
                count++;
            }
        }
        return count;
    }
    
    /**
     * Gets all search results
     * @return List of found file paths
     */
    public List<String> getSearchResults() {
        return new ArrayList<>(searchResults);
    }
    
    /**
     * Sets case sensitivity for search
     * @param caseSensitive true for case-sensitive, false for case-insensitive
     */
    public void setCaseSensitive(boolean caseSensitive) {
        this.caseSensitive = caseSensitive;
    }
    
    /**
     * Gets current case sensitivity setting
     * @return true if case-sensitive, false otherwise
     */
    public boolean isCaseSensitive() {
        return caseSensitive;
    }
}