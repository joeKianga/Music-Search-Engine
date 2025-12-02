package student;

import java.util.*;
import java.util.regex.*;

public class PhraseRanking {

    /**
     * Ranks how closely lyrics match a given phrase
     * @param lyrics the song lyrics to search in
     * @param phrase the phrase to search for
     * @return the best rank found (lower is better), or Integer.MAX_VALUE if no match
     * 
     * Created By JOSE KIANGA
     * Date November 25, 2028
     * CSCI 290 Project PartC part 09 Labs.
     */
    public static int rankPhrase(String lyrics, String phrase) {
        if (lyrics == null || phrase == null || lyrics.isEmpty() || phrase.isEmpty()) {
            return Integer.MAX_VALUE;
        }
        
        // Convert to lowercase for case-insensitive search
        String lyricsLower = lyrics.toLowerCase();
        String phraseLower = phrase.toLowerCase();
        
        // Split phrase into words
        String[] phraseWords = phraseLower.split("[^a-zA-Z]+");
        
        // Remove empty strings from phrase words
        List<String> cleanPhraseWords = new ArrayList<>();
        for (String word : phraseWords) {
            if (!word.isEmpty()) {
                cleanPhraseWords.add(word);
            }
        }
        
        if (cleanPhraseWords.isEmpty()) {
            return Integer.MAX_VALUE;
        }
        
        // Find all occurrences of the first word in the phrase
        String firstWord = cleanPhraseWords.get(0);
        Pattern pattern = Pattern.compile("\\b" + Pattern.quote(firstWord) + "\\b");
        Matcher matcher = pattern.matcher(lyricsLower);
        
        int bestRank = Integer.MAX_VALUE;
        
        // Check each occurrence of the first word
        while (matcher.find()) {
            int startPos = matcher.start();
            
            // Try to match the rest of the phrase
            int currentPos = startPos;
            boolean fullMatch = true;
            
            // Check each subsequent word in the phrase
            for (int i = 0; i < cleanPhraseWords.size(); i++) {
                String currentWord = cleanPhraseWords.get(i);
                
                // Find this word starting from current position
                Pattern wordPattern = Pattern.compile("\\b" + Pattern.quote(currentWord) + "\\b");
                Matcher wordMatcher = wordPattern.matcher(lyricsLower);
                
                if (wordMatcher.find(currentPos)) {
                    if (i == cleanPhraseWords.size() - 1) {
                        // Last word - calculate the rank
                        int endPos = wordMatcher.end();
                        int rank = endPos - startPos;
                        if (rank < bestRank) {
                            bestRank = rank;
                        }
                    }
                    // Move position to after this word
                    currentPos = wordMatcher.end();
                } else {
                    fullMatch = false;
                    break;
                }
            }
        }
        
        return bestRank;
    }
    
    /**
     * Testing method
     */
   public static void main(String[] args) {
    // If no arguments provided, use defaults for testing
    if (args.length == 0) {
        args = new String[]{"allSongs.txt", "Time can bring you down"};
    }
    
    if (args.length < 2) {
        System.err.println("Usage: java student.PhraseRanking <songfile> \"<phrase>\"");
        System.err.println("Example: java student.PhraseRanking allSongs.txt \"she loves you\"");
        System.err.println("Expected: 58 songs for \"she loves you\"");
        return;
    }
    
    String songFile = args[0];
    String phrase = args[1];
    
    System.out.println("=== Part IX: Phrase Ranking ===");
    System.out.println("Song file: " + songFile);
    System.out.println("Search phrase: \"" + phrase + "\"");
    System.out.println();
    
    SongCollection sc = new SongCollection(songFile);
    Song[] allSongs = sc.getAllSongs();
    
    System.out.println("Total songs: " + allSongs.length);
    System.out.println("Ranking songs...");
    System.out.println();
    
    // Rank all songs and collect results
    List<RankedSong> rankedSongs = new ArrayList<>();
    
    for (Song song : allSongs) {
        int rank = rankPhrase(song.getLyrics(), phrase);
        if (rank != Integer.MAX_VALUE) {
            rankedSongs.add(new RankedSong(song, rank));
        }
    }
    
    // Sort by rank (best first)
    Collections.sort(rankedSongs);
    
    System.out.println("Songs with phrase matches: " + rankedSongs.size());
    System.out.println();
    System.out.println("Ranked results (best matches first):");
    System.out.println("Rank | Artist - Title");
    System.out.println("-----|----------------");
    
    for (RankedSong rs : rankedSongs) {
        System.out.printf("%4d | %s%n", rs.getRank(), rs.getSong());
    }
    
    // Show expected results for verification
    System.out.println();
    System.out.println("=== Expected Results ===");
    System.out.println("\"she loves you\" should find 58 songs");
    System.out.println("\"Time can bring you down\" should find 9 songs");
    System.out.println("\"You can't always get what you want\" should find 5 songs");
}}