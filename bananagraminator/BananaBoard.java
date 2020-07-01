import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * This class represents a Board for the game, and allows the user to add words to a board in a variety of ways,
 * while also containing checks to ensure the board is valid, and that a move is legal. Also has functionality that
 * allows it to report all legal moves on the board given a set of letters to work with.
 *
 * @author Aryan Agrawal
 */
public class BananaBoard {

    /**A Grid object that represents the contents of the board. */
    private Grid _board;
    /**A map of all words on the board to CoordinatePairs that represent
     * their starting positions and directions. */
    private HashMap<Word, CoordinatePair> _wordPositions;
    /**A map of the String representations of all words on the board to
     * the number of words on the board with the same String contents. Used to
     * differentiate between multiple of the same words on the board. */
    private HashMap<String, Integer> _wordCounts;

    /**
     * Copy constructor used throughout the program, used to simplify game trees, recursion, and
     * decision making by allowing changes to be directly implemented on exact, deep copies of the
     * current board.
     * @param b The BananaBoard instance to copy.
     */
    public BananaBoard(BananaBoard b) {
        _board = new Grid(b._board);
        _wordPositions = new HashMap<>();
        _wordCounts = new HashMap<>();
        for (Word w : b._wordPositions.keySet()) {
            CoordinatePair otherCP = b._wordPositions.get(w);
            Word wNew = new Word(w.getWord(), w.getNum());
            CoordinatePair cpNew = new CoordinatePair(otherCP);
            _wordPositions.put(wNew, cpNew);
        }
        for (String s : b._wordCounts.keySet()) {
            int count = b._wordCounts.get(s);
            _wordCounts.put(s, count);
        }
    }

    /**
     * Default Constructor, sets all variables to default, including Grid size.
     */
    public BananaBoard() {
        _board = new Grid();
        _wordPositions = new HashMap<>();
        _wordCounts = new HashMap<>();
    }

    /**
     * Alternate Constructor, sets all variables to default except for grid size,
     * which is passed in.
     * @param dim Side length of the grid.
     */
    public BananaBoard(int dim) {
        _board = new Grid(dim);
        _wordPositions = new HashMap<>();
        _wordCounts = new HashMap<>();
    }

    /**
     * Returns whether a board is currently set up in a valid configuration. This involves
     * ensuring that all formed words on the board are legal.
     */
    public boolean isValid() throws FileNotFoundException {
        for (int y = _board.max(); y >= _board.min(); y--) {
            for (int x = _board.min(); x <= _board.max(); x++) {
                if (!_board.isEmpty(x, y)) {
                    char space = _board.getSpace(x, y);
                    if (y == _board.min() && x == _board.max()) {
                        continue;
                    } else if (y == _board.min()) {
                        if (x == _board.min()) {
                            if (!_board.isEmpty(x + 1, y)) {
                                String w = getFullWord(x, y, 0);
                                if (!Utils.isWord(w)) {
                                    return false;
                                }
                            }
                        } else {
                            if (_board.isEmpty(x - 1, y) && !_board.isEmpty(x + 1, y)) {
                                String w = getFullWord(x, y, 0);
                                if (!Utils.isWord(w)) {
                                    return false;
                                }
                            }
                        }
                    } else if (x == _board.max()) {
                        if (y == _board.max()) {
                            if (!_board.isEmpty(x, y - 1)) {
                                String w = getFullWord(x, y, 1);
                                if (!Utils.isWord(w)) {
                                    return false;
                                }
                            }
                        } else {
                            if (_board.isEmpty(x, y + 1) && !_board.isEmpty(x, y - 1)) {
                                String w = getFullWord(x, y, 1);
                                if (!Utils.isWord(w)) {
                                    return false;
                                }
                            }
                        }
                    } else {
                        if (y == _board.max()) {
                            if (!_board.isEmpty(x, y - 1)) {
                                String w = getFullWord(x, y, 1);
                                if (!Utils.isWord(w)) {
                                    return false;
                                }
                            }
                        } else {
                            if (_board.isEmpty(x, y + 1) && !_board.isEmpty(x, y - 1)) {
                                String w = getFullWord(x, y, 1);
                                if (!Utils.isWord(w)) {
                                    return false;
                                }
                            }
                        }
                        if (x == _board.min()) {
                            if (!_board.isEmpty(x + 1, y)) {
                                String w = getFullWord(x, y, 0);
                                if (!Utils.isWord(w)) {
                                    return false;
                                }
                            }
                        } else {
                            if (_board.isEmpty(x - 1, y) && !_board.isEmpty(x + 1, y)) {
                                String w = getFullWord(x, y, 0);
                                if (!Utils.isWord(w)) {
                                    return false;
                                }
                            }
                        }
                    }
                }
            }
        }
        return true;
    }

    /**
     * Helper method for isValid. Given a coordinate and direction, returns
     * the full word formed at that position.
     * @param x The x coordinate, as on a cartesian plane.
     * @param y The y coordinate, as on a cartesian plane.
     * @param dir The direction that the word is formed. 1 for vertical, 0 for horizontal.
     */
    private String getFullWord(int x, int y, int dir) {
        String result = "";
        if (dir == 0) {
            for (int xPos = x; xPos <= _board.max(); xPos++) {
                if (!_board.isEmpty(xPos, y)) {
                    result += _board.getSpace(xPos, y);
                } else {
                    break;
                }
            }
        } else {
            for (int yPos = y; yPos >= _board.min(); yPos--) {
                if (!_board.isEmpty(x, yPos)) {
                    result += _board.getSpace(x, yPos);
                } else {
                    break;
                }
            }
        }
        return result;
    }

    /**
     * Given a String word, adds word to the current board if it fits and it results in a valid board.
     * Also updates all necessary variables, such as _wordCounts and _wordPositions with the new
     * information.
     * This method is intended only for use for the FIRST word on the board. It will throw an error if
     * called for any subsequent word.
     * @return Whether the addition was successful.
     */
    public boolean addWord(String word) throws FileNotFoundException {
        if (_wordPositions.keySet().size() != 0) {
            throw new BananaException("Only use one arg method to add first word to board.");
        }
        boolean fits = wordFits(word);
        if (!fits) {
            return false;
        }
        BananaBoard copy = new BananaBoard(this);
        int x = (word.length() / 2) * -1;
        for (int i = 0; i < word.length(); i++, x++) {
            copy._board.setSpace(word.charAt(i), x, 0);
        }
        boolean isValid = copy.isValid();
        if (isValid) {
            x = (word.length() / 2) * - 1;
            int originX = x;
            for (int i = 0; i < word.length(); i++, x++) {
                _board.setSpace(word.charAt(i), x, 0);
            }
            _wordCounts.put(word, wordFrequency(word) + 1);
            _wordPositions.put(new Word(word, wordFrequency(word)), new CoordinatePair(originX, 0, 0));
            return true;
        } else {
            return false;
        }
    }

    /**
     * Alternate header for addWord that accepts a Move instance as a parameter, implemented to simplify
     * the process of making many Moves from a collection in the Player class.
     * @param m The Move to make.
     */
    public boolean addWord(Move m) throws FileNotFoundException {
        return addWord(m.getNewWord(), m.getAddTo().getWord(), m.getAddTo().getNum(), m.getAddToIndex(), m.getNewIndex());
    }

    /**
     * Method that adds a Word onto the board. Used for all words except the first one. Requires information about which
     * other word to add to, and what indexes they must overlap at. Will only add the word if it determines that it fits
     * and that it results in a valid board. Returns whether the addition of the word was successful.
     * @param word String representing the new word to add.
     * @param addTo String representing the old word to add the new word to.
     * @param wordNumber Integer value of the number word to add to, in case of duplicates. Can be found in _wordCounts.
     * @param startCharIndex Integer value of the index on the old word where the two words should overlap.
     * @param newWordIndex Integer value of the index on the new word where the two words should overlap.
     */
    public boolean addWord(String word, String addTo, int wordNumber, int startCharIndex, int newWordIndex) throws FileNotFoundException {
        CoordinatePair otherStart = _wordPositions.get(new Word(addTo, wordNumber));
        if (otherStart == null) {
            throw new BananaException("The word to which this word is to be appended does not exist");
        }
        if (addTo.charAt(startCharIndex) != word.charAt(newWordIndex)) {
            throw new BananaException("The letters do not match as they are meant to be aligned.");
        }
        boolean fits = wordFits(word, addTo, wordNumber, startCharIndex, newWordIndex);
        if (!fits) {
            return false;
        }
        BananaBoard copy = new BananaBoard(this);
        int dir = otherStart.dir;
        if (dir == 1) {
            int startY = otherStart.y - startCharIndex;
            int startX = otherStart.x - newWordIndex;
            int x = startX;
            for (int i = 0; i < word.length(); i++, x++) {
                copy._board.setSpace(word.charAt(i), x, startY);
            }
        } else {
            int startX = otherStart.x + startCharIndex;
            int startY = otherStart.y + newWordIndex;
            int y = startY;
            for (int i = 0; i < word.length(); i++, y--) {
                copy._board.setSpace(word.charAt(i), startX, y);
            }
        }
        boolean isValid = copy.isValid();
        if (isValid) {
            if (dir == 1) {
                int startY = otherStart.y - startCharIndex;
                int startX = otherStart.x - newWordIndex;
                int x = startX;
                for (int i = 0; i < word.length(); i++, x++) {
                    _board.setSpace(word.charAt(i), x, startY);
                }
                _wordCounts.put(word, wordFrequency(word) + 1);
                _wordPositions.put(new Word(word, wordFrequency(word)), new CoordinatePair(startX, startY, 0));
            } else {
                int startX = otherStart.x + startCharIndex;
                int startY = otherStart.y + newWordIndex;
                int y = startY;
                for (int i = 0; i < word.length(); i++, y--) {
                    _board.setSpace(word.charAt(i), startX, y);
                }
                _wordCounts.put(word, wordFrequency(word) + 1);
                _wordPositions.put(new Word(word, wordFrequency(word)), new CoordinatePair(startX, startY, 1));
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * Accepts the same parameters as addWord, used to determine whether there is space on the board
     * for a certain word to be added in a certain location. Returns whether the desired word can be
     * added in the desired location on the board.
     */
    public boolean wordFits(String word, String addTo, int wordNumber, int startCharIndex, int newWordIndex) {
        Word connectWord = new Word(addTo, wordNumber);
        if (!_wordPositions.containsKey(connectWord)) {
            throw new BananaException("The word designated to connect this word to doesn't exist.");
        }
        if (startCharIndex >= addTo.length()) {
            throw new BananaException("The character index to start with on the connecting word is out of range.");
        }
        CoordinatePair start = _wordPositions.get(connectWord);
        int direction = start.dir;
        int startX = start.x;
        int startY = start.y;
        boolean fits = true;
        if (direction == 1) {
            startY -= startCharIndex;
            startX -= newWordIndex;
            int x = startX;
            for (int len = 0; len < word.length(); len++, x++) {
                if (x < _board.min() || x > _board.max()
                        || (!_board.isEmpty(x, startY) && _board.getSpace(x, startY) != word.charAt(len))) {
                    fits = false;
                    break;
                }
            }
        } else {
            startX += startCharIndex;
            startY += newWordIndex;
            int y = startY;
            for (int len = 0; len < word.length(); len++, y--) {
                if (y < _board.min()|| y > _board.max()
                        || (!_board.isEmpty(startX, y) && _board.getSpace(startX, y) != word.charAt(len))) {
                    fits = false;
                    break;
                }
            }
        }
        return fits;
    }

    /**
     * Used in the Player class, returns a List of all legal moves on this board, given a map of letter counts. Uses
     * various other methods in the BananaBoard class as helpers in determining whether a certain move is legal or not.
     */
    public ArrayList<Move> legalMoves(HashMap<String, Integer> letters) throws FileNotFoundException {
        HashSet<Word> allWords = new HashSet<>(_wordPositions.keySet());
        ArrayList<Move> allLegal = new ArrayList<>();
        for (Word w : allWords) {
            CoordinatePair cp = _wordPositions.get(w);
            int startX = cp.x;
            int startY = cp.y;
            int wDir = cp.dir;
            boolean incrementY = (wDir == 1);
            String word = w.getWord();
            for (int i = 0; i < word.length(); i++) {
                HashMap<String, Integer> localLetters = new HashMap<>(letters);
                if (wDir == 1 && startX != _board.max()) {
                    if (_board.isEmpty(startX + 1, startY)) {
                        String letter = _board.getSpace(startX, startY) + "";
                        localLetters.put(letter, localLetters.getOrDefault(letter, 0) + 1);
                        HashSet<String> required = new HashSet<>();
                        required.add(letter);
                        HashSet<String> possibleWords = Utils.allPossibleWords(localLetters, Utils.getWordList(), required);
                        for (String s : possibleWords) {
                            Word addTo = w;
                            String newWord = s;
                            int addToIndex = i;
                            int newIndex = newWord.indexOf(letter);
                            Move m = new Move(addTo, addToIndex, newWord, newIndex, 0);
                            if (isLegal(m)) {
                                allLegal.add(m);
                            }
                        }
                    }
                    startY--;
                } else if (wDir == 0 && startY != _board.min()) {
                    if (_board.isEmpty(startX, startY - 1)) {
                        String letter = _board.getSpace(startX, startY) + "";
                        localLetters.put(letter, localLetters.getOrDefault(letter, 0) + 1);
                        HashSet<String> required = new HashSet<>();
                        required.add(letter);
                        HashSet<String> possibleWords = Utils.allPossibleWords(localLetters, Utils.getWordList(), required);
                        for (String s : possibleWords) {
                            Word addTo = w;
                            String newWord = s;
                            int addToIndex = i;
                            int newIndex = newWord.indexOf(letter);
                            Move m = new Move(addTo, addToIndex, newWord, newIndex, 1);
                            if (isLegal(m)) {
                                allLegal.add(m);
                            }
                        }
                    }
                    startX++;
                } else if ((wDir == 1 && startX == _board.max()) || (wDir == 0 && startY == _board.min())) {
                    break;
                }
            }
        }
        return allLegal;
    }

    /**
     * Returns whether a certain move is legal on this board. Used primarily as a helper method for legalMoves.
     * @param m Move instance to determine the legality of.
     */
    public boolean isLegal(Move m) throws FileNotFoundException {
        BananaBoard copy = new BananaBoard(this);
        return copy.addWord(m.getNewWord(), m.getAddTo().getWord(), m.getAddTo().getNum(), m.getAddToIndex(), m.getNewIndex());
    }

    /**
     * Returns whether the first word fits on the board. Used *ONLY FOR THE FIRST WORD*. It will error if
     * called after a word has already been placed on the board.
     * @param word String representing the word to check.
     */
    public boolean wordFits(String word) {
        if (_wordPositions.keySet().size() != 0) {
            throw new BananaException("Only use one arg method to check if the first word fits on the board.");
        }
        return word.length() <= _board._sideLen;
    }

    /**
     * Convenient helper method that returns the amount of times a certain word has been placed on the board.
     * Directly uses _wordCounts.
     * @param word String representing the word to check the count of.
     */
    public int wordFrequency(String word) {
        Integer count = _wordCounts.get(word);
        if (count == null) {
            return 0;
        } else {
            return count;
        }
    }

    /**
     * Returns the set of all words currently on the board, as Strings.
     */
    public HashSet<String> getWords() {
        return new HashSet<String>(_wordCounts.keySet());
    }

    /**
     * Prints the contents of the board to the standard output.
     */
    public void displayBoard() {
        System.out.println(this.toString());
    }

    /**
     * A toString method, returns a String representing the contents of the board.
     */
    public String toString() {
        return _board.toString();
    }

    /**
     * Getter method that returns the side length of the board.
     */
    public int getDim() {
        return _board._sideLen;
    }

    /**
     * Private class used to facilitate operations involving adding and removing characters from spaces on the board.
     */
    private class Grid {

        /** A 2D char array that represents the contents of the board.*/
        private char[][] _grid;
        /** The Integers representing the relative centers of the grid for both the x and y axes.*/
        private int _centerX, _centerY;
        /** The Integer representing the length of the side of the square grid.*/
        private int _sideLen;

        /**
         * Default constructor for the Grid Class, sets the side length to 33 by default.
         */
        private Grid() {
            _grid = new char[33][33];
            _centerX = 16;
            _centerY = 16;
            _sideLen = 33;
        }

        /**
         * Alternate constructor for the Grid class, allows client to specify sideLength.
         * @param dim The desired side length of the square grid.
         */
        private Grid(int dim) {
            _grid = new char[dim][dim];
            _centerX = dim / 2;
            _centerY = dim / 2;
            _sideLen = dim;
        }

        /**
         * Copy Constructor used to facilitate deep copies in higher level copy constructors, such as
         * the BananaBoard copy constructor.
         * @param g The Grid instance to deep copy all data from.
         */
        private Grid(Grid g) {
            _centerX = g._centerX;
            _centerY = g._centerY;
            _sideLen = g._sideLen;
            _grid = new char[g._sideLen][g._sideLen];
            for (int row = 0; row < g._grid.length; row++) {
                for (int col = 0; col < g._grid[0].length; col++) {
                    _grid[row][col] = g._grid[row][col];
                }
            }
        }

        /**
         * Returns the maximum x/y coordinate value for this board.
         */
        private int max() {
            return _sideLen / 2;
        }

        /**
         * Returns the minimum x/y coordinate value for this baard.
         */
        private int min() {
            return (_sideLen / 2) * -1;
        }

        /**
         * Sets the space of a given coordinate on the board to the desired value.
         * @param c char value to set on the given space.
         * @param xPos x coordinate of the space to set.
         * @param yPos y coordinate of the space to set.
         */
        private void setSpace(char c, int xPos, int yPos) {
            _grid[_centerX + xPos][_centerY + yPos] = c;
        }

        /**
         * Returns the char value of the space of a given coordinate.
         * @param xPos x coordinate of the space to get.
         * @param yPos y coordinate of the space to get.
         */
        private char getSpace(int xPos, int yPos) {
            return _grid[_centerX + xPos][_centerY + yPos];
        }

        /**
         * Returns whether the space of a given coordinate is empty.
         */
        private boolean isEmpty(int xPos, int yPos) {
            return getSpace(xPos, yPos) == '\u0000';
        }

        /**
         * Prints the contents of the grid onto the standard output.
         */
        private void displayGrid() {
            for (int y = _sideLen - 1; y >= 0; y--) {
                for (int x = 0; x < _sideLen; x++) {
                    char space = _grid[x][y];
                    if (space == '\u0000') {
                        space = '-';
                    }
                    System.out.print(space + " ");
                }
                System.out.println();
            }
        }

        /**
         * Method that returns a String representing the contents of the Grid.
         */
        @Override
        public String toString() {
            String result = "";
            for (int y = _sideLen - 1; y >= 0; y--) {
                for (int x = 0; x < _sideLen; x++) {
                    char space = _grid[x][y];
                    if (space == '\u0000') {
                        space = '-';
                    }
                    result += space + " ";
                }
                result += "\n";
            }
            return result;
        }
    }

    /**
     * Class used to represent a coordinate space on the grid for a word, along with the direction that
     * word is set. 1 for vertical, 0 for horizontal.
     */
    private class CoordinatePair {
        /** The x coordinate of this CoordinatePair.*/
        private int x;
        /** The y coordinate of this CoordinatePair.*/
        private int y;
        /** The direction the word mapped to this Coordinate Pair is set.
         * 1 for vertical, 0 for horizontal.*/
        private int dir;

        /**
         * Constructor that initializes a coordinate pair instance with the given
         * x/y coordinates and word direction.
         */
        private CoordinatePair(int x, int y, int dir) {
            this.x = x;
            this.y = y;
            this.dir = dir;
        }

        /**
         * Copy constructor used in higher level copy constructors, used to initialize an
         * exact deep copy of a given CoordinatePair.
         */
        private CoordinatePair(CoordinatePair cp) {
            this.x = cp.x;
            this.y = cp.y;
            this.dir = cp.dir;
        }

        /**
         * Returns the x coordinate for this CoordinatePair object.
         */
        private int getX() {
            return x;
        }

        /**
         * Returns the y coordinate for this CoordinatePair object.
         */
        private int getY() {
            return y;
        }

        /**
         * Returns the direction for this CoordinatePair object.
         * 1 for vertical, 0 for horizontal.
         */
        private int getDir() {
            return dir;
        }
    }
}
