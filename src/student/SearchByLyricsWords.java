
package student;
import java.io.*;
import java.util.*;

public class SearchByLyricsWords {
    private TreeMap<String, TreeSet<Song>> wordToSongMap;
    private TreeSet<String> commonWords;
    
    public SearchByLyricsWords(SongCollection sc) {
        wordToSongMap = new TreeMap<>();
        commonWords = new TreeSet<>();
        
        buildCommonWordsSet();
        buildLyricsMap(sc);
    }
    
    private void buildCommonWordsSet() {
        try {
            InputStream inputStream = getClass().getResourceAsStream("commonWords.txt");
            if (inputStream != null) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    // Handle both space-separated and line-separated words
                    String[] wordsInLine = line.trim().split("\\s+");
                    for (String word : wordsInLine) {
                        if (!word.isEmpty()) {
                            commonWords.add(word.toLowerCase());
                        }
                    }
                }
                reader.close();
                System.out.println("Loaded common words from file: " + commonWords.size() + " words");
            } else {
                System.err.println("Common words file not found, using defaults");
                useDefaultCommonWords();
            }
        } catch (IOException e) {
            System.err.println("Error reading common words file: " + e.getMessage());
            useDefaultCommonWords();
        }
    }
    
    private void useDefaultCommonWords() {
        String[] defaultCommonWords = {
            "a", "an", "the", "me", "my", "she", "he", "it", "we", "they", 
            "is", "am", "are", "was", "were", "be", "being", "been",
            "i", "you", "his", "her", "our", "your", "their", "my"
        };
        for (String word : defaultCommonWords) {
            commonWords.add(word.toLowerCase());
        }
        System.out.println("Using default common words: " + commonWords.size() + " words");
    }
    
    private void buildLyricsMap(SongCollection sc) {
        Song[] allSongs = sc.getAllSongs();
        System.out.println("Processing " + allSongs.length + " songs...");
        
        for (Song song : allSongs) {
            String lyrics = song.getLyrics().toLowerCase();
            
            // Split on non-letter characters
            String[] words = lyrics.split("[^a-zA-Z]+");
            
            for (String word : words) {
                // Skip single letters and common words
                if (word.length() <= 1 || commonWords.contains(word)) {
                    continue;
                }
                
                // Add to map
                wordToSongMap.computeIfAbsent(word, k -> new TreeSet<>()).add(song);
            }
        }
        
        System.out.println("Map building complete. Unique words: " + wordToSongMap.size());
    }
    
    /**
     * Search for songs that contain all the given lyrics words
     * @param lyricsWords the search query string
     * @return array of songs that contain all the search words
     */
    public Song[] search(String lyricsWords) {
        // If search string is empty or null, return empty array
        if (lyricsWords == null || lyricsWords.trim().isEmpty()) {
            return new Song[0];
        }
        
        // Process search terms exactly like we processed lyrics
        String[] searchTerms = lyricsWords.toLowerCase().split("[^a-zA-Z]+");
        TreeSet<Song> resultSet = null;
        
        for (String term : searchTerms) {
            // Skip single letters and common words (same rules as building the map)
            if (term.length() <= 1 || commonWords.contains(term)) {
                continue;
            }
            
            // Get the set of songs for this word
            TreeSet<Song> songSet = wordToSongMap.get(term);
            
            if (songSet == null) {
                // If any word isn't found, no results
                return new Song[0];
            }
            
            if (resultSet == null) {
                // First valid word - start with this set
                resultSet = new TreeSet<>(songSet);
            } else {
                // Intersect with current result set
                resultSet.retainAll(songSet);
            }
        }
        
        // Handle case where all search terms were filtered out
        if (resultSet == null) {
            return new Song[0];
        }
        
        // Convert to array and return
        return resultSet.toArray(new Song[0]);
    }
    
    public void statistics() {
        // 1. Number of keys in the map
        int numKeys = wordToSongMap.size();
        System.out.println("1. Number of word keys in map: " + numKeys);
        
        // 2. Total number of song references (N)
        long totalReferences = 0;
        for (TreeSet<Song> songSet : wordToSongMap.values()) {
            totalReferences += songSet.size();
        }
        System.out.println("2. Total song references (N): " + totalReferences);
        
        // 3. Calculate approximate space usage
        double avgWordLength = 5.0;
        double keySpace = numKeys * avgWordLength * 2;
        double mapReferenceSpace = numKeys * 8;
        double setsSpace = totalReferences * 8;
        double totalSpaceBytes = keySpace + mapReferenceSpace + setsSpace;
        double totalSpaceMB = totalSpaceBytes / (1024 * 1024);
        
        System.out.println("3. Space Usage Analysis:");
        System.out.println("   - Key storage: ~" + (long)keySpace + " bytes");
        System.out.println("   - Map references: " + (long)mapReferenceSpace + " bytes");
        System.out.println("   - Set references: " + (long)setsSpace + " bytes");
        System.out.println("   - TOTAL: ~" + (long)totalSpaceBytes + " bytes (" + 
                         String.format("%.2f", totalSpaceMB) + " MB)");
        
        // 6. Space as function of N
        System.out.println("4. Space complexity: O(N) where N = " + totalReferences);
    }
    
    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("Usage: java student.SearchByLyricsWords <songfile> [search_terms]");
            System.err.println("Examples:");
            System.err.println("  java student.SearchByLyricsWords allSongs.txt");
            System.err.println("  java student.SearchByLyricsWords allSongs.txt \"We don't need no education\"");
            return;
        }
        
        String songFile = args[0];
        System.out.println("=== Part VII & VIII: SearchByLyricsWords ===");
        System.out.println("Loading songs from: " + songFile);
        
        // Build the data structure
        SongCollection sc = new SongCollection(songFile);
        System.out.println("Songs loaded: " + sc.getAllSongs().length);
        
        System.out.println("\nBuilding inverted index...");
        SearchByLyricsWords searcher = new SearchByLyricsWords(sc);
        
        System.out.println("\n=== STATISTICS ===");
        searcher.statistics();
        
        // Part VIII: Test search if search terms provided
        if (args.length >= 2) {
            String searchQuery = args[1];
            System.out.println("\n=== SEARCH RESULTS ===");
            System.out.println("Search query: \"" + searchQuery + "\"");
            
            long searchStart = System.currentTimeMillis();
            Song[] results = searcher.search(searchQuery);
            long searchTime = System.currentTimeMillis() - searchStart;
            
            System.out.println("Total matches: " + results.length);
            System.out.println("Search time: " + searchTime + " ms");
            
            // Print first 10 results (or all if less than 10)
            int limit = Math.min(10, results.length);
            System.out.println("\nFirst " + limit + " matches:");
            for (int i = 0; i < limit; i++) {
                System.out.println("  " + results[i]);
            }
        } else {
            System.out.println("\nNo search terms provided. To test search, add lyrics as second argument.");
        }
    }
}