import java.util.ArrayList;

public class PseudoAI {

    private static final int[][] weights = new int[][]{{10, 5, 10}, {5, 8, 5}, {10, 5, 10}}; //give each position on board a weight corners>middle>other
    private static final char[][] boardState = new char[][]{{'1', '2', '3'}, {'4', '5', '6'}, {'7', '8', '9'}}; //board state during game
    private static int xCount = 0, oCount = 0; //count of Os and Xs on the board
    private static final ArrayList<Integer[]> index = new ArrayList<>(); //list of indexes whose weights should be updated


    static void systemAction(char[] seq, int pos) {

        if (pos != 0) { //not first move

            //convert sequential position to 2D matrix index
            int i = (pos - 1) / 3;
            int j = (pos - 1) % 3;

            boardState[i][j] = 'X';    //place user choice on board
            evaluateBoard(i, j);       //evaluate the new board configuration
        }

        if (!GameDriver.gameEnd) {
            System.out.println("\nSystem Turn\n");
            selectPosition(seq);           //select pseudo AI move
        }
    }


    private static void evaluateBoard(int i, int j) {


        int n = boardState.length - 1;

        //main diagonal
        if (i == j) {

            for (int k = 0; k < 3; k++)
                xoCounter(k, k);

            checkGameEnd();

            if (!GameDriver.gameEnd) {
                if (index.size() != 0)
                    updateWeights();

                flushData(); //reset counts and arraylist
            } else
                return;

            //minor diagonal
        }

        if (n == j + i) {

            for (int k = 0; k < 3; k++)
                xoCounter(k, n - k);

            checkGameEnd();

            if (!GameDriver.gameEnd) {
                if (index.size() != 0)
                    updateWeights();

                flushData(); //reset counts and arraylist
            } else
                return;

        }

        //row
        for (int k = 0; k < 3; k++)
            xoCounter(i, k);

        checkGameEnd();

        if (!GameDriver.gameEnd) {
            if (index.size() != 0)
                updateWeights();

            flushData(); //reset counts and arraylist
        } else
            return;

        //column
        for (int k = 0; k < 3; k++)
            xoCounter(k, j);

        checkGameEnd();

        if (!GameDriver.gameEnd) {
            if (index.size() != 0)
                updateWeights();

            flushData(); //reset counts and arraylist
        }
    }

    //count the Os and Xs, add to arraylist if position is free
    private static void xoCounter(int i, int j) {

        if (boardState[i][j] == 'O')
            oCount++;
        else if (boardState[i][j] == 'X')
            xCount++;
        else
            index.add(new Integer[]{i, j});
    }

    //update weights of position for new board state
    private static void updateWeights() {
        if (oCount + xCount == 1) //one symbol
            for (Integer[] point : index)
                weights[point[0]][point[1]] += 1;

        else if (xCount == 2) //two user symbol has lower weight
            weights[index.get(0)[0]][index.get(0)[1]] *= 4;

        else if (oCount == 2) //two system symbol has max weight
            weights[index.get(0)[0]][index.get(0)[1]] *= 5;
    }

    //Check if game end state has been reached.
    private static void checkGameEnd() {
        if (oCount == 3) {
            GameDriver.gameEnd = true;
            GameDriver.result = 0;
        } else if (xCount == 3) {
            GameDriver.gameEnd = true;
            GameDriver.result = 1;
        }
    }

    private static void update() {

    }

    //Clear counts and arraylist
    private static void flushData() {
        xCount = 0;
        oCount = 0;
        index.clear();
    }


    //Select placement of symbol by pseudo AI
    private static void selectPosition(char[] seq) {

        int maxI = -1, maxJ = -1;
        double max = 0;

        //select free position with max weight
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++) {
                if (boardState[i][j] == 'O' || boardState[i][j] == 'X')
                    continue;
                if (weights[i][j] > max) {
                    max = weights[i][j];
                    maxI = i;
                    maxJ = j;
                }
            }

        if (maxI != -1) {
            int pos = (3 * maxI) + maxJ; //calculate position in linear sequence

            //place symbol at position
            seq[pos] = 'O';
            boardState[maxI][maxJ] = 'O';

            evaluateBoard(maxI, maxJ); //evaluate new state
        }
    }
}
