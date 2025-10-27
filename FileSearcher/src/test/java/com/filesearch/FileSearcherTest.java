package com.filesearch;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.io.TempDir;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Unit tests for FileSearcher class
 */
class FileSearcherTest {
    
    @TempDir
    Path tempDir;
    
    private FileSearcher caseSensitiveSearcher;
    private FileSearcher caseInsensitiveSearcher;
    
    @BeforeEach
    void setUp() {
        caseSensitiveSearcher = new FileSearcher(true);
        caseInsensitiveSearcher = new FileSearcher(false);
    }
    
    @Test
    void testSearchSingleFileCaseSensitive() throws IOException {
        // Create test directory structure
        Path subDir = tempDir.resolve("subdir");
        Files.createDirectories(subDir);
        
        // Create files in different directories to avoid name conflicts
        Files.createFile(tempDir.resolve("test.txt"));
        Files.createFile(subDir.resolve("TEST.TXT"));  // Different case, different directory
        Files.createFile(subDir.resolve("test2.txt")); // Different name to avoid conflict
        
        String[] fileNames = {"test.txt"};
        List<String> results = caseSensitiveSearcher.searchFiles(tempDir.toString(), fileNames);
        
        // Should find only the exact match "test.txt" in root directory
        assertEquals(1, results.size());
        assertEquals(1, caseSensitiveSearcher.countFileOccurrences("test.txt"));
    }
    
    @Test
    void testSearchSingleFileCaseInsensitive() throws IOException {
        // Create test directory structure
        Path subDir = tempDir.resolve("subdir");
        Files.createDirectories(subDir);
        
        // Create files with different cases in different directories
        Files.createFile(tempDir.resolve("test.txt"));
        Files.createFile(subDir.resolve("TEST.TXT"));
        Files.createFile(subDir.resolve("Test.File.txt")); // Different file
        
        String[] fileNames = {"test.txt"};
        List<String> results = caseInsensitiveSearcher.searchFiles(tempDir.toString(), fileNames);
        
        // Should find both test.txt and TEST.TXT (case insensitive)
        assertEquals(2, results.size());
        assertEquals(2, caseInsensitiveSearcher.countFileOccurrences("test.txt"));
    }
    
    @Test
    void testSearchMultipleFiles() throws IOException {
        Files.createFile(tempDir.resolve("file1.txt"));
        Files.createFile(tempDir.resolve("file2.xml"));
        Files.createFile(tempDir.resolve("config.properties"));
        
        String[] fileNames = {"file1.txt", "file2.xml"};
        List<String> results = caseSensitiveSearcher.searchFiles(tempDir.toString(), fileNames);
        
        assertEquals(2, results.size());
        assertEquals(1, caseSensitiveSearcher.countFileOccurrences("file1.txt"));
        assertEquals(1, caseSensitiveSearcher.countFileOccurrences("file2.xml"));
    }
    
    @Test
    void testSearchNonExistentFile() throws IOException {
        Files.createFile(tempDir.resolve("existing.txt"));
        
        String[] fileNames = {"nonexistent.txt"};
        List<String> results = caseSensitiveSearcher.searchFiles(tempDir.toString(), fileNames);
        
        assertTrue(results.isEmpty());
        assertEquals(0, caseSensitiveSearcher.countFileOccurrences("nonexistent.txt"));
    }
    
    @Test
    void testSearchInNonExistentDirectory() {
        String[] fileNames = {"test.txt"};
        
        assertThrows(IOException.class, () -> {
            caseSensitiveSearcher.searchFiles("/nonexistent/directory/path/that/does/not/exist", fileNames);
        });
    }
    
    @Test
    void testEmptyDirectory() throws IOException {
        String[] fileNames = {"test.txt"};
        List<String> results = caseSensitiveSearcher.searchFiles(tempDir.toString(), fileNames);
        
        assertTrue(results.isEmpty());
    }
    
    @Test
    void testCaseSensitivityToggle() {
        assertTrue(caseSensitiveSearcher.isCaseSensitive());
        assertFalse(caseInsensitiveSearcher.isCaseSensitive());
        
        caseSensitiveSearcher.setCaseSensitive(false);
        assertFalse(caseSensitiveSearcher.isCaseSensitive());
        
        caseInsensitiveSearcher.setCaseSensitive(true);
        assertTrue(caseInsensitiveSearcher.isCaseSensitive());
    }
    
    @Test
    void testSearchWithEmptyFileNames() throws IOException {
        Files.createFile(tempDir.resolve("test.txt"));
        
        String[] fileNames = {};
        List<String> results = caseSensitiveSearcher.searchFiles(tempDir.toString(), fileNames);
        
        assertTrue(results.isEmpty());
    }
    
    @Test
    void testCountWithNoSearchPerformed() {
        assertEquals(0, caseSensitiveSearcher.countFileOccurrences("anyfile.txt"));
    }
    
    @Test
    void testGetSearchResultsReturnsCopy() throws IOException {
        Files.createFile(tempDir.resolve("test.txt"));
        
        String[] fileNames = {"test.txt"};
        caseSensitiveSearcher.searchFiles(tempDir.toString(), fileNames);
        
        List<String> results1 = caseSensitiveSearcher.getSearchResults();
        List<String> results2 = caseSensitiveSearcher.getSearchResults();
        
        assertNotSame(results1, results2, "getSearchResults should return a new list instance");
        assertEquals(results1, results2, "Both lists should have the same content");
    }
}