/**
 * File: SongCollection.java
 ************************************************************************
 *                     Revision History (newest first)
 ************************************************************************
 * 
 * 8.2016 - Anne Applin - formatting and JavaDoc skeletons added   
 * 2015 -   Prof. Bob Boothe - Starting code and main for testing  
 * [November 25, 2025] - [Jose KIANGA] - Implemented file reading and parsing
 ************************************************************************
 */

package student;

import java.io.*;
import java.util.*;

/**
 * SongCollection.java 
 * Reads the specified data file and build an array of songs.
 * @author boothe
 * @author [Jose KIANGA] - Implemented file reading and parsing
 */
public class SongCollection {

    private Song[] songs;

    /**
     * Note: in any other language, reading input inside a class is simply not
     * done!! No I/O inside classes because you would normally provide
     * precompiled classes and I/O is OS and Machine dependent and therefore 
     * not portable. Java runs on a virtual machine that IS portable. So this 
     * is permissable because we are programming in Java and Java runs on a 
     * virtual machine not directly on the hardware.
     *
     * @param filename The path and filename to the datafile that we are using
     * must be set in the Project Properties as an argument.
     */
    public SongCollection(String filename) {
        // use a try catch block
        try {
            // Read the entire file content
            String fileContent = readFile(filename);
            
            // Split the content using double quotes as delimiter
            String[] parts = fileContent.split("\"");
            
            // Use ArrayList while reading songs
            ArrayList<Song> songList = new ArrayList<>();
            
            // Each song has 6 parts in the split array: 
            // "ARTIST=", artist, "TITLE=", title, "LYRICS=", lyrics
            for (int i = 1; i < parts.length; i += 6) {
                if (i + 4 < parts.length) {
                    String artist = parts[i].trim();
                    String title = parts[i + 2].trim();
                    String lyrics = parts[i + 4].trim();
                    
                    // Create new Song object and add to list
                    Song song = new Song(artist, title, lyrics);
                    songList.add(song);
                }
            }
            
            // Convert ArrayList to array
            songs = new Song[songList.size()];
            songs = songList.toArray(songs);
            
            // sort the songs array using Arrays.sort
            Arrays.sort(songs);
            
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            songs = new Song[0]; // Empty array if file reading fails
        }
    }
    
    /**
     * Helper method to read the entire file content as a string
     * @param filename the name of the file to read
     * @return the entire file content as a string
     * @throws IOException if file cannot be read
     */
    private String readFile(String filename) throws IOException {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        }
        return content.toString();
    }
 
    /**
     * this is used as the data source for building other data structures
     * @return the songs array
     */
    public Song[] getAllSongs() {
        return songs;
    }
 
    /**
     * unit testing method
     * @param args
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("usage: prog songfile");
            return;
        }

        SongCollection sc = new SongCollection(args[0]);
        Song[] allSongs = sc.getAllSongs();

        // show song count
        System.out.println("Total songs = " + allSongs.length + ", first songs:");
        
        // Print first 10 songs or all if less than 10
        int limit = Math.min(10, allSongs.length);
        for (int i = 0; i < limit; i++) {
            System.out.println(allSongs[i]);
        }
    }
}