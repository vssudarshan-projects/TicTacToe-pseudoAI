import java.util.InputMismatchException;
import java.util.Scanner;

public class GameDriver {

    private static final Scanner scanner = new Scanner(System.in); //user input
    private static final char[] seq = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9'}; //game board as a sequence
    public static boolean gameEnd = false; // end game flag
    public static int result = -1;  //result 0: O won 1: X won -1: draw

    //print the current state of the board
    private static void showBoard() {

        for (int i = 0; i < seq.length; i++) {

            if (i % 3 == 0) {
                System.out.println();
            }

            if ((i + 1) % 3 != 0)
                System.out.print(seq[i] + " \t");
            else
                System.out.print(seq[i]);

        }
        System.out.println("\n");
    }

    //Start Here
    public static void gameStart() {

        System.out.println("Welcome to Tic-Tac-Toe\n");
        System.out.println("You will be X and system will be O.");
        showBoard();
        System.out.println("After game starts, enter the number corresponding to the position " +
                "where you want to place your symbol.");

        System.out.println("Press enter to start");
        scanner.nextLine();

        int turn = (int) (Math.random() * 2); //assign first turn randomly


        System.out.println("\n\n========START========");
        showBoard();

        int pos = 0;
        int counter = 0;

        //game runs for max 5 turns
        while (++counter <= 5) {

            if (turn == 0) {
                pos = userAction();  //user input
                showBoard();
                PseudoAI.systemAction(seq, pos); // call pseudo AI to respond
            } else {
                PseudoAI.systemAction(seq, pos);
                showBoard();

                if (gameEnd)
                    break;

                pos = userAction();
            }
            showBoard();
            if (gameEnd)
                break;
        }

        //result announcement
        switch (result) {
            case 0:
                System.out.println("System Won");
                break;
            case 1:
                System.out.println("You Won");
                break;
            default:
                System.out.println("Draw");
        }

    }

    //place user choice on board and return success or fail
    private static boolean userChoice(int pos) {

        if (pos <= 0 || pos >= 10)
            return false;

        if (seq[pos - 1] == 'X' || seq[pos - 1] == 'O')
            return false;

        seq[pos - 1] = 'X'; //place on board

        return true;
    }

    //get user input and check if valid
    private static int userAction() {

        boolean valid;
        int pos = 0;

        do {
            System.out.println("\nYour Turn\n");
            System.out.println("Enter Position:");


            try {
                pos = scanner.nextInt();
            } catch (InputMismatchException e) {
                valid = false;
                scanner.nextLine();
                continue;
            }

            valid = userChoice(pos);

        } while (!valid); //loop until valid input

        return pos;
    }

}
