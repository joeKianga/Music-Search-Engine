# Music Search Engine 
 
## Project Overview 
This is a Java-based music search engine with advanced data structures. 
 
## Features 
- Inverted index with 31,385 unique words 
- Phrase ranking algorithm 
- 0-second search performance 
- GUI integration 
## ?? Project Structure 
 
``` 
Music-Search-Engine/ 
ÃÄÄ src/student/                 # Core Java source files 
³   ÃÄÄ Song.java               # Song data model 
³   ÃÄÄ SongCollection.java     # Song file parsing and management 
³   ÃÄÄ SearchByLyricsWords.java # Inverted index implementation 
³   ÃÄÄ SearchByLyricsPhrase.java # Optimized phrase search 
³   ÃÄÄ PhraseRanking.java      # Phrase matching algorithm 
³   ÃÄÄ RankedSong.java         # Ranked song wrapper 
³   ÃÄÄ P10timer.java           # Performance test harness 
³   ÃÄÄ GUITestLyricsWords.java # Word search GUI 
³   ÀÄÄ GUITestPhraseSearch.java # Phrase search GUI 
ÃÄÄ data/                       # Data files 
³   ÃÄÄ allSongs.txt           # Song database (10,514 songs) 
³   ÀÄÄ commonWords.txt        # Common words filter 
ÃÄÄ docs/                       # Documentation 
ÃÄÄ images/                     # Screenshots 
ÃÄÄ README.md                   # This file 
ÀÄÄ .gitignore                  # Git ignore rules 
``` 
 
## ?? Getting Started 
 
### Prerequisites 
- Java 8 or higher 
- Git (for cloning) 
 
### Installation 
```bash 
# Clone the repository 
git clone https://github.com/YOUR-USERNAME/Music-Search-Engine.git 
cd Music-Search-Engine 
 
# Compile all source files 
javac src/student/*.java 
``` 
 
### Running the Application 
 
#### Run Performance Tests 
```bash 
java -cp src student.P10timer data/allSongs.txt 
``` 
 
#### Launch Phrase Search GUI 
```bash 
java -cp src student.GUITestPhraseSearch 
``` 
 
### Sample Queries 
 
#### Word Search Examples 
```bash 
"blue jeans"           # Returns 19 songs 
"she loves my car"     # Returns 4 songs  
"little"               # Returns 1,675 songs 
"notaword"             # Returns 0 songs 
``` 
 
## ?? Author 
 
**JOSE KIANGA**  
- ?? GitHub: [@JOSE-KIANGA](https://github.com/joekianga) 
- ?? LinkedIn: [Jose Mbila Kianga](https://www.linkedin.com/in/jose-mbila-kianga-3aa03458/) 
 
## ?? License 
 
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details. 
 
--- 
 
? **If you found this project useful, please give it a star!** ? 
