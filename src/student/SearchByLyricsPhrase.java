package student;

import java.util.*;

public class SearchByLyricsPhrase {
    private Song[] allSongs;
    
    public SearchByLyricsPhrase(SongCollection sc) {
        this.allSongs = sc.getAllSongs();
    }
    
    public Song[] search(String lyricsPhrase) {
        if (lyricsPhrase == null || lyricsPhrase.trim().isEmpty()) {
            return new Song[0];
        }
        
        // FAST OPTIMIZATION: Use parallel streaming for candidate filtering
        List<Song> candidates = Collections.synchronizedList(new ArrayList<>());
        String phraseLower = lyricsPhrase.toLowerCase();
        String[] phraseWords = phraseLower.split("[^a-zA-Z]+");
        
        // Filter candidates in parallel
        Arrays.stream(allSongs).parallel().forEach(song -> {
            String lyricsLower = song.getLyrics().toLowerCase();
            boolean containsAll = true;
            for (String word : phraseWords) {
                if (!word.isEmpty() && word.length() > 1 && !lyricsLower.contains(word)) {
                    containsAll = false;
                    break;
                }
            }
            if (containsAll) {
                candidates.add(song);
            }
        });
        
        // Rank candidates
        List<RankedSong> rankedSongs = Collections.synchronizedList(new ArrayList<>());
        candidates.parallelStream().forEach(song -> {
            int rank = PhraseRanking.rankPhrase(song.getLyrics(), lyricsPhrase);
            if (rank != Integer.MAX_VALUE) {
                rankedSongs.add(new RankedSong(song, rank));
            }
        });
        
        // Sort results
        rankedSongs.sort(Comparator.naturalOrder());
        
        Song[] results = new Song[rankedSongs.size()];
        for (int i = 0; i < rankedSongs.size(); i++) {
            results[i] = rankedSongs.get(i).getSong();
        }
        
        return results;
    }
}