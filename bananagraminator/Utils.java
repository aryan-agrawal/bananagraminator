import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

/**
 * A Utils class that provides helpful methods used throughout the program, such
 * as checking whether a certain word is valid, and finding all valid anagrams given a
 * collection of letters.
 *
 * Original Words List from Jeff Boulter's website: http://boulter.com/scrabble/words/words.txt
 * I then filtered Jeff's list with a list of profane, unacceptable words to ensure no awful words
 * made their way anywhere near this program. I found that list of profane words in a collection
 * of resources on Luis Von Ahn's research group, at cs.cmu.edu. I did not create either of those
 * original lists myself, I simply used one to filter the other and create the txt file used for
 * this project. Thank you Jeff Boulter and Luis Von Ahn for the resources. I am not profiting
 * off them in any way.
 *
 * @author Aryan Agrawal
 */
public class Utils {

    /** A Set of all legal words, initialized by adding all words from a file of valid words.*/
    private static HashSet<String> _words;
    /** A Map of all individual letters to "letter scores", used for determining Board heuristic values.*/
    private static HashMap<String, Integer> _letterScores;

    /**
     * Given a String s, returns whether s is a valid word.
     */
    public static boolean isWord(String s) throws FileNotFoundException {
        if (_words == null) {
            initializeWordSet();
        }
        return _words.contains(s);
    }

    /**
     * Helper method used to put all valid words in _words from a file, called only once per program
     * execution.
     */
    private static void initializeWordSet() throws FileNotFoundException {
        _words = new HashSet<>();
        File wordFile = new File("good-words.txt");
        Scanner wordScan = new Scanner(wordFile);
        while (wordScan.hasNextLine()) {
            String word = wordScan.nextLine();
            _words.add(word);
        }
    }

    /**
     * Given a Map of letter counts, returns a Set of all possible words that can be formed from those letters.
     */
    public static HashSet<String> allPossibleWords(HashMap<String, Integer> letters) throws FileNotFoundException {
        if (_words == null) {
            initializeWordSet();
        }
        return allPossibleWords(letters, _words);
    }


    /**
     * Given a Map of letter counts and a Set of valid words, returns a Set of all possible words in the words Set that
     * can be formed from those letters.
     */
    public static HashSet<String> allPossibleWords(HashMap<String, Integer> letters, HashSet<String> words) throws FileNotFoundException {
        if (_words == null) {
            initializeWordSet();
        }
        return allPossibleWords(letters, words, new HashSet<>());
    }

    /**
     * Given a Map of letters, a Set of valid words, and a Set of letters that are required to be in the words, returns
     * a Set of all possible words in the words Set that can be formed from the given letters, ensuring that all the
     * words returned contain all the letters in the given Set of required letters.
     */
    public static HashSet<String> allPossibleWords(HashMap<String, Integer> letters, HashSet<String> words, HashSet<String> requiredLetters) throws FileNotFoundException {
        if (_words == null) {
            initializeWordSet();
        }
        HashSet<String> anagrams = new HashSet<>();
        for (String w : words) {
            boolean lacksRequired = false;
            for (String letter : requiredLetters) {
                if (!w.contains(letter)) {
                    lacksRequired = true;
                }
            }
            if (lacksRequired) {
                continue;
            }
            HashMap<String, Integer> copy = new HashMap<>(letters);
            boolean possible = true;
            for (int i = 0; i < w.length(); i++) {
                String letter = w.substring(i, i + 1);
                if (!copy.containsKey(letter) || copy.get(letter) == 0) {
                    possible = false;
                    break;
                } else {
                    copy.put(letter, copy.get(letter) - 1);
                }
            }
            if (possible) {
                anagrams.add(w);
            }
        }
        return anagrams;
    }

    /**
     * Returns the score for a given word, based off the scores of each individual letter in the word.
     */
    public static int wordScore(String s) {
        if (_letterScores == null) {
            initializeLetterScores();
        }
        int score = 0;
        for (int i = 0; i < s.length(); i++) {
            String letter = s.substring(i, i + 1);
            score += _letterScores.getOrDefault(letter, 0);
        }
        return score;
    }

    /**
     * Returns the Set of all valid words.
     */
    public static HashSet<String> getWordList() {
        return _words;
    }

    /**
     * Called as a helper method to initialize all the scores for each individual
     * letter. Called only once during the execution of the program.
     */
    private static void initializeLetterScores() {
        _letterScores = new HashMap<>();
        _letterScores.put("E", 150);
        _letterScores.put("T", 100);
        _letterScores.put("A", 140);
        _letterScores.put("O", 140);
        _letterScores.put("I", 135);
        _letterScores.put("N", 120);
        _letterScores.put("S", 120);
        _letterScores.put("H", 400);
        _letterScores.put("R", 130);
        _letterScores.put("D", 200);
        _letterScores.put("L", 110);
        _letterScores.put("C", 300);
        _letterScores.put("U", 120);
        _letterScores.put("M", 300);
        _letterScores.put("W", 400);
        _letterScores.put("F", 400);
        _letterScores.put("G", 200);
        _letterScores.put("Y", 400);
        _letterScores.put("P", 300);
        _letterScores.put("B", 300);
        _letterScores.put("V", 400);
        _letterScores.put("K", 500);
        _letterScores.put("J", 800);
        _letterScores.put("X", 800);
        _letterScores.put("Q", 1000);
        _letterScores.put("Z", 1000);
    }

    /**
     * Returns whether or not the given String is a vowel.
     */
    public static boolean isVowel(String s) {
        HashSet<String> vowels = new HashSet<>();
        vowels.add("A");
        vowels.add("E");
        vowels.add("I");
        vowels.add("O");
        vowels.add("U");
        return vowels.contains(s.trim().toUpperCase());
    }
}
