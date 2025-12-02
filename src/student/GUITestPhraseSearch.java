package student;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GUITestPhraseSearch {
    public static void main(String[] args) {
        // Create GUI to test SearchByLyricsPhrase
        JFrame frame = new JFrame("Part X - Search By Lyrics Phrase");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 500);
        
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        
        // Search components
        JTextField searchField = new JTextField(40);
        JButton searchButton = new JButton("Search Phrase");
        JTextArea resultsArea = new JTextArea(20, 60);
        resultsArea.setEditable(false);
        
        JPanel searchPanel = new JPanel();
        searchPanel.add(new JLabel("Lyrics Phrase:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        
        panel.add(searchPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(resultsArea), BorderLayout.CENTER);
        
        frame.add(panel);
        
        // Add action listener
        searchButton.addActionListener(e -> {
            try {
                SongCollection sc = new SongCollection("allSongs.txt");
                SearchByLyricsPhrase searcher = new SearchByLyricsPhrase(sc);
                String searchPhrase = searchField.getText();
                
                long startTime = System.currentTimeMillis();
                Song[] results = searcher.search(searchPhrase);
                long searchTime = System.currentTimeMillis() - startTime;
                
                resultsArea.setText("=== Search By Lyrics Phrase ===\n");
                resultsArea.append("Search phrase: '" + searchPhrase + "'\n");
                resultsArea.append("Total matches: " + results.length + "\n");
                resultsArea.append("Search time: " + searchTime + " ms\n\n");
                
                int limit = Math.min(10, results.length);
                resultsArea.append("First " + limit + " matches (best ranked first):\n");
                for (int i = 0; i < limit; i++) {
                    resultsArea.append((i + 1) + ". " + results[i] + "\n");
                }
                
                if (results.length > 10) {
                    resultsArea.append("... and " + (results.length - 10) + " more matches\n");
                }
                
                if (results.length == 0) {
                    resultsArea.append("No songs found containing the exact phrase.\n");
                }
                
            } catch (Exception ex) {
                resultsArea.setText("Error: " + ex.getMessage());
                ex.printStackTrace();
            }
        });
        
        // Add sample phrases button
        JButton samplesButton = new JButton("Sample Phrases");
        searchPanel.add(samplesButton);
        
        samplesButton.addActionListener(e -> {
            String[] samples = {
                "she loves you",
                "love love love", 
                "school's out",
                "Time can bring you down",
                "You can't always get what you want"
            };
            
            String sampleList = "Sample Phrases for Testing:\n";
            for (String sample : samples) {
                sampleList += "• \"" + sample + "\"\n";
            }
            JOptionPane.showMessageDialog(frame, sampleList, "Sample Search Phrases", JOptionPane.INFORMATION_MESSAGE);
        });
        
        frame.setVisible(true);
        
        System.out.println("Part X GUI Test Running - SearchByLyricsPhrase integrated with GUI!");
    }
}
