package student;

public class RankedSong implements Comparable<RankedSong> {
    private Song song;
    private int rank;
    
    public RankedSong(Song song, int rank) {
        this.song = song;
        this.rank = rank;
    }
    
    public Song getSong() { 
        return song; 
    }
    
    public int getRank() { 
        return rank; 
    }
    
    @Override
    public int compareTo(RankedSong other) {
        return Integer.compare(this.rank, other.rank);
    }
    
    @Override
    public String toString() {
        return rank + " " + song.toString();
    }
}