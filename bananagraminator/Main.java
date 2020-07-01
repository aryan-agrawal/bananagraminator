import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.HashMap;

/**
 * Main class for the Bananagraminator, an AI that creates a valid crossword given a set of letters.
 * The Main class handles primarily I/O operations.
 * @author Aryan Agrawal
 */
public class Main {

    /**
     * Main, runner method for the program. Supply with program arguments of args[0] as
     * the desired side length of the board, and args[1] as a String containing each of
     * the individual letters desired in the result, with only spaces separating them.
     */
    public static void main(String[] args) throws FileNotFoundException {
        if (args.length != 0) {
            int boardDim = Integer.parseInt(args[0]);
            String tiles = args[1];
            HashMap<String, Integer> letters = new HashMap<>();
            Scanner line = new Scanner(tiles);
            while (line.hasNext()) {
                String letter = line.next().toUpperCase();
                letters.put(letter, letters.getOrDefault(letter, 0) + 1);
            }
            Player AI;
            if (boardDim > 11) {
                AI = new Player(letters, boardDim);
            } else {
                AI = new Player(letters);
            }
            System.out.println();
            AI.createBananagrams();
        } else {
            Scanner kb = new Scanner(System.in);
            System.out.println("Hello! Initial Set Up:");
            System.out.print("Do you wish to set a custom board dimension? (y/n): ");
            String decision = kb.nextLine();
            int sideLen = 0;
            if (decision.trim().equalsIgnoreCase("y")) {
                System.out.print("Board side length: ");
                sideLen = Integer.parseInt(kb.nextLine());
            }
            HashMap<String, Integer> letters = new HashMap<>();
            System.out.print("Enter all tiles, separated only by spaces: ");
            String tiles = kb.nextLine();
            Scanner line = new Scanner(tiles);
            while (line.hasNext()) {
                String letter = line.next().toUpperCase();
                letters.put(letter, letters.getOrDefault(letter, 0) + 1);
            }
            Player AI;
            if (sideLen != 0) {
                AI = new Player(letters, sideLen);
            } else {
                AI = new Player(letters);
            }
            System.out.println();
            AI.createBananagrams();
        }
    }
}
