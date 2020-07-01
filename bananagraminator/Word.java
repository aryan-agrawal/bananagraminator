/**
 * A class that represents a Word on the BananaBoard. Used to keep track
 * of words when there are multiple of the same String value.
 *
 * @author Aryan Agrawal
 */
public class Word {
    /** A String representation of this Word.*/
    private String _word;
    /** A int representing which specific iteration of a word this
     * is on a BananaBoard.*/
    private int _num;

    /**
     * Standard Constructor, takes the String representation of the word and the
     * Integer value representing which specific iteration of a word this is as
     * arguments.
     */
    public Word(String word, int num) {
        _word = word;
        _num = num;
    }

    /**
     * Getter method that returns the String representation of this word.
     */
    public String getWord() {
        return _word;
    }

    /**
     * Getter method that returns the Integer value representing which specific iteration of
     * a word this is on a BananaBoard.
     */
    public int getNum() {
        return _num;
    }

    /**
     * Returns the number of characters in the String representation of the word.
     */
    public int length() {
        return _word.length();
    }

    /**
     * Implemented so Word instances can be used as keys in any data structure that utilizes a
     * HashTable format. Two words are deemed equal if they have the same String representation
     * and the same Integer value representing which specific iteration of a word they are on
     * a BananaBoard.
     */
    public boolean equals(Object obj) {
        return _word == ((Word) obj).getWord() && _num == ((Word) obj).getNum();
    }

    /**
     * Implemented so Word instances can be used as keys in any data structure that utilizes a
     * HashTable format. Returns the hashCode for this Word instance.
     */
    public int hashCode() {
        String hasher = _word + _num;
        return hasher.hashCode();
    }
}
