import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * A class representing the AI that makes moves and adds words to a BananaBoard given an initial
 * set of letters. Uses a maximizing game tree of variable search depth, and also contains info
 * about scoring specific BananaBoard states with custom heuristic expressions.
 *
 * @author Aryan Agrawal
 */
public class Player {

    /** The BananaBoard that this Player is actively adding words to.*/
    private BananaBoard _board;
    /** The original Map of letter counts, saved in case a need to revert to a previous
     * state arises during the execution of the program.*/
    private HashMap<String, Integer> _originalLetters;
    /** The current Map of remaining letter counts.*/
    private HashMap<String, Integer> _letters;
    /** An int value near Integer.MAX_VALUE that signifies a winning move.*/
    private static final int WIN_SCORE = Integer.MAX_VALUE - 30;
    /** An int value that represents the maximum possible Integer value.*/
    private static final int INFINITY = Integer.MAX_VALUE;
    /** The current best move found by the move finding algorithm.*/
    private Move _foundMove;
    /** The depth at which the game tree searches. Default value is 1.*/
    private int _searchDepth;

    /**
     * Standard constructor for a Player instance, takes a Map of letter counts
     * as an argument.
     */
    public Player(HashMap<String, Integer> letters) {
        _letters = letters;
        _originalLetters = new HashMap<>(letters);
        _board = new BananaBoard();
        _searchDepth = 1;
    }

    /**
     * Alternate constructor for a Player instance that take a Map of letter counts,
     * but also an integer representing the desired side length of this Player's
     * BananaBoard as arguments.
     */
    public Player(HashMap<String, Integer> letters, int boardDim) {
        _letters = letters;
        _originalLetters = new HashMap<>(letters);
        _board = new BananaBoard(boardDim);
        _searchDepth = 1;
    }

    /**
     * The method called from the Main class that creates a valid crossword.
     */
    public void createBananagrams() throws FileNotFoundException {
        setFirstWord();
        while (!noLettersLeft(_letters)) {
            searchForMove();
            if (_foundMove == null) {
                System.out.println("Unable to find an accurate crossword. For now, try entering more characters. As this " +
                        "tool is improved with better heuristic expressions, this query will yield better results. Thanks!");
            }
            _board.addWord(_foundMove);
            useLetters(_foundMove.getNewWord(), _letters);
            String common = _foundMove.getSharedLetter();
            _letters.put(common, _letters.getOrDefault(common, 0) + 1);
        }
        _board.displayBoard();
    }

    /**
     * *Not currently in use* This method is a key part of a later development for this project, and
     * allows the user to add more letters to a board after it has already been formed.
     */
    public void addLetters(HashMap<String, Integer> moreLetters) throws FileNotFoundException {
        _letters = moreLetters;
        while (!noLettersLeft(_letters)) {
            searchForMove();
            if (_foundMove == null) {
                System.out.println("Unable to find an accurate crossword. For now, try entering more characters. As this " +
                        "tool is improved with better heuristic expressions, this query will yield better results. Thanks!");
            }
            _board.addWord(_foundMove);
            useLetters(_foundMove.getNewWord(), _letters);
            String common = _foundMove.getSharedLetter();
            _letters.put(common, _letters.getOrDefault(common, 0) + 1);
        }
        _board.displayBoard();
    }

    /**
     * This method calls findMove and sets/returns _foundMove accordingly. Does so with deep copies
     * of this Player's BananaBoard and Map of letter counts.
     */
    private Move searchForMove() throws FileNotFoundException {
        BananaBoard temp = new BananaBoard(_board);
        HashMap<String, Integer> letterCopy = new HashMap<>(_letters);
        _foundMove = null;
        findMove(temp, letterCopy, _searchDepth, true, -INFINITY, INFINITY);
        return _foundMove;
    }

    /**
     * A recursive, maximizing game tree, this function sets _foundMove to the move that results in the maximum
     * score as determined by the heuristic method, and is capable of immediately forcing completed boards if it
     * detects a set of Moves that would do so. Take a BananaBoard, Map of letter counts, search depth, a boolean
     * that specifies whether to save the move found to _foundMove, and initial alpha/beta values. Alpha/beta values
     * are crucial to pruning and drastically reducing search time in mini/max trees, and although they are not
     * currently in use, they may be used in future versions of this program. This method makes heavy use of the
     * BananaBoard method legalMoves(), and sorts through them to find the one that results in the best resultant
     * board, as determined by heuristic().
     */
    private int findMove(BananaBoard board, HashMap<String, Integer> letters,
                         int depth, boolean saveMove, int alpha, int beta) throws FileNotFoundException {
        if (depth == 0) {
            return heuristic(board, letters, false);
        }
        int bestScore = 0;
        Move bestMove = null;
        boolean firstMove = true;
        ArrayList<Move> legalMoves = board.legalMoves(letters);
        if (legalMoves.size() == 0) {
            return heuristic(board, letters, true);
        }
        for (Move M : legalMoves) {
            BananaBoard localCopy = new BananaBoard(board);
            HashMap<String, Integer> localLetterCopy = new HashMap<>(letters);
            useLetters(M.getNewWord(), localLetterCopy);
            String common = M.getSharedLetter();
            localLetterCopy.put(common, localLetterCopy.getOrDefault(common, 0) + 1);
            localCopy.addWord(M);
            int score = findMove(localCopy, localLetterCopy, depth - 1, saveMove, alpha, beta);
            if (score == WIN_SCORE) {
                bestScore = score;
                bestMove = M;
                break;
            }
            if (firstMove) {
                bestScore = score;
                bestMove = M;
                firstMove = false;
            }
            if (score > bestScore) {
                bestScore = score;
                bestMove = M;
            }
            alpha = Math.max(score, alpha);
            if (alpha >= beta) {
                break;
            }
        }
        if (saveMove) {
            _foundMove = bestMove;
        }
        return bestScore;
    }

    /**
     * Uses Utils allPossibleWords methods to generate a list of possible first words, and then picks the
     * best one by finding the one with the highest word score, as defined and calculated in Utils.
     */
    private void setFirstWord() throws FileNotFoundException {
        HashSet<String> possibleFirsts = Utils.allPossibleWords(_letters);
        String bestFirst = "";
        int bestFirstScore = 0;
        for (String word : possibleFirsts) {
            int score = Utils.wordScore(word);
            if (score > bestFirstScore) {
                bestFirst = word;
                bestFirstScore = score;
            }
        }
        _board.addWord(bestFirst);
        useLetters(bestFirst, _letters);
    }

    /**
     * Given a board, a set of letters, and a boolean that tells the function if there are
     * not any possible moves remaining, this method assigns a score to the board based
     * on several different factors, including the word scores of the words on the board and
     * the usefulness/quantity of the remaining letters in hand. As this method is updated
     * and optimized alongside the word score from Utils, this entire AI will become more
     * effective in searching for ideal boards, significantly reducing the amount of letter
     * combinations that result in unsolved boards.
     */
    private int heuristic(BananaBoard board, HashMap<String, Integer> letters, boolean noMoves) {
        if (noLettersLeft(letters)) {
            return WIN_SCORE;
        }
        if (noMoves && !noLettersLeft(letters)) {
            return -WIN_SCORE;
        }
        HashSet<String> wordsOnBoard = board.getWords();
        int result = 0;
        for (String w : wordsOnBoard) {
            result += board.wordFrequency(w) * Utils.wordScore(w);
        }
        result -= 20 * numLettersLeft(letters);
        result -= 70 * letters.getOrDefault("z", 0);
        result -= 70 * letters.getOrDefault("q", 0);
        result -= 70 * letters.getOrDefault("x", 0);
        int numLeft = numLettersLeft(letters);
        for (String letter : letters.keySet()) {
            double numTimes = letters.getOrDefault(letter, 0);
            if (Utils.isVowel(letter)) {
                if (numTimes / numLeft >= .4) {
                    result -= 60;
                } else if (numTimes / numLeft >= .5 && numLeft <= 6) {
                    result -= 85;
                }
                if (numTimes >= 3 && numLeft - numTimes <= 2) {
                    result -= 200;
                }
            } else {
                if (numTimes / numLeft >= .4) {
                    result -= 100;
                } else if (numTimes / numLeft >= .5 && numLeft <= 6) {
                    result -= 140;
                }
                if (numTimes >= 3 && numLeft - numTimes <= 2) {
                    result -= 400;
                }
            }
        }
        return result;
    }

    /**
     * Given a Map of letter counts, returns whether there are no
     * letters left in the Map.
     */
    private boolean noLettersLeft(HashMap<String, Integer> letters) {
        return numLettersLeft(letters) == 0;
    }

    /**
     * Given a Map of letter counts, returns the number of letters
     * left in the Map.
     */
    private int numLettersLeft(HashMap<String, Integer> letters) {
        int total = 0;
        for (Integer val : letters.values()) {
            total += val;
        }
        return total;
    }

    /**
     * Given a String and a Map of letter counts, removes one of each letter
     * in the word from the Map of letter counts.
     */
    private void useLetters(String word, HashMap<String, Integer> letters) {
        for (int i = 0; i < word.length(); i++) {
            String letter = word.substring(i, i + 1);
            letters.put(letter, letters.get(letter) - 1);
        }
    }
}
