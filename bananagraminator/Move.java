/**
 * A class representing a Move on a BananaBoard.
 *
 * @author Aryan Agrawal
 */
public class Move {
    /** The preexisting word on the board to add this word to.*/
    private Word _addTo;
    /** The index of the word on the board where the two words intersect.*/
    private int _addToIndex;
    /** A String representation of the new word to be added.*/
    private String _newWord;
    /** The index of the new word to be added where the two words intersect.*/
    private int _newIndex;
    /** The direction in which the new word is to be placed. 1 for vertical, 0 for horizontal. */
    private int _dir;

    /**
     * Constructor that accepts one argument for each instance variable and assigns them accordingly.
     */
    public Move(Word addTo, int addToIndex, String newWord, int newIndex, int dir) {
        _addTo = addTo;
        _addToIndex = addToIndex;
        _newWord = newWord;
        _newIndex = newIndex;
        _dir = dir;
    }

    /**
     * Getter method, returns the preexisting word on the board to add this word to.
     */
    public Word getAddTo() {
        return _addTo;
    }

    /**
     * Getter method, returns the index of the word on the board where the two words intersect.
     */
    public int getAddToIndex() {
        return _addToIndex;
    }

    /**
     * Getter method, returns the String representation of the new word to be added.
     */
    public String getNewWord() {
        return _newWord;
    }

    /**
     * Getter method, returns the index of the new word to be added where the two words intersect.
     */
    public int getNewIndex() {
        return _newIndex;
    }

    /**
     * Getter method, returns the direction in which the new word is to be placed. 1 for vertical, 0 for horizontal.
     */
    public int getDir() {
        return _dir;
    }

    /**
     * Utility method, returns the letter that is shared between the two words involved in this Move.
     */
    public String getSharedLetter() {
        return _newWord.charAt(_newIndex) + "";
    }
}
