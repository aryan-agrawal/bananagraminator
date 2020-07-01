/**
 * Extends RuntimeException. Exists to differentiate developer-implemented
 * exceptions from standard RuntimeExceptions.
 *
 * @author Aryan Agrawal
 */
public class BananaException extends RuntimeException {

    /** BananaException Constructor that takes no arguments.*/
    BananaException() {
        super();
    }

    /** BananaException Constructor that takes a String message as an argument.*/
    BananaException(String s) {
        super(s);
    }
}
