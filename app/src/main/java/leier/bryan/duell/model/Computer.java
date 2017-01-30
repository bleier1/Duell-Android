package leier.bryan.duell.model;
import java.util.Random;
import static java.lang.Math.abs;

/**
 * Created by Bryan on 11/15/2016.
 */

public class Computer extends Player {
    // GUI components... later.

    /**
     * Default constructor.
     */
    public Computer()
    {
        // Give the computer its name.
        this.playerName = "Computer";
    }

    /**
     * The play() function that lets the computer play the game of Duell.
     * @param board the board that the computer will play on
     * @return a message displaying the move the computer just made
     */
    public String play(Board board)
    {
        // Array to initialize so the computer can properly make its move:
        int[] dieCoords = {0, 0, 0, 0};
        // The computer needs to decide which die to move. For this, it will look to see if specific scenarios are true or not.
        // The key die results in an immediate win, so find where the human's key die is. If it can be captured, do it.
        dieCoords = captureKeyDieScore(board, 'C');
        if (dieCoords[0] != 0)
        {
            // Make the move to capture the key die.
            return computerMakesMove(board, dieCoords[0], dieCoords[1], dieCoords[2], dieCoords[3], "keyDieCapture");
        }
        // Key space capture results in a win as well, so see if the computer can travel to it.
        dieCoords = captureKeySpaceScore(board, 'C');
        if (dieCoords[0] != 0)
        {
            // Make the move to capture the key die.
            return computerMakesMove(board, dieCoords[0], dieCoords[1], dieCoords[2], dieCoords[3], "keySpaceCapture");
        }
        // The computer must also make moves to play defensively if it detects that the human could possibly win.
        // If a human's die is close to the computer's key die, block the capture or move the die.
        dieCoords = blockKeyDieScore(board, 'C');
        if (dieCoords[0] != 0)
        {
            // Make the move to capture the key die.
            return computerMakesMove(board, dieCoords[0], dieCoords[1], dieCoords[2], dieCoords[3], "blockKeyDie");
        }
        // If a human's die is close to the computer's key space, block the capture.
        dieCoords = blockKeySpaceScore(board, 'C');
        if (dieCoords[0] != 0)
        {
            // Make the move to capture the key die.
            return computerMakesMove(board, dieCoords[0], dieCoords[1], dieCoords[2], dieCoords[3], "blockKeySpace");
        }
        // If the code flow is at this point, there's no reason to play defensively. Seek a die to capture.
        dieCoords = captureDieScore(board, 'C');
        if (dieCoords[0] != 0)
        {
            // Make the move to capture the key die.
            return computerMakesMove(board, dieCoords[0], dieCoords[1], dieCoords[2], dieCoords[3], "dieCapture");
        }
        // Otherwise, random move.
        else
        {
            // Randomly move a die to random coordinates.
            dieCoords = randomMove(board, 'C');
            // Make the move.
            return computerMakesMove(board, dieCoords[0], dieCoords[1], dieCoords[2], dieCoords[3], "random");
        }
    }

    // test function
    public static void main(String [] args)
    {
        Board model = new Board();
        model.newGameSetUp();
        Computer test = new Computer();
        test.play(model);
    }

    /**
     * Performs the move that the computer wants to make
     * @param board the board to make a move on
     * @param dieRow the integer that will contain the row of the die to move
     * @param dieColumn the integer that will contain the column of the die to move
     * @param spaceRow the integer that will contain the row of the space to move to
     * @param spaceColumn the integer that will contain the row of the space to move to
     * @param strategy the strategy that the computer uses to make the move
     * @return a string that contains the move the computer decided to make
     */
    private String computerMakesMove(Board board, int dieRow, int dieColumn, int spaceRow, int spaceColumn, String strategy)
    {
        // The spaces to move the die.
        int spacesToMove = board.getDieTopNum(dieRow, dieColumn);
        // The amount of spaces needed to traverse to the given coordinates.
        int rowRolls = abs(spaceRow - dieRow);
        int columnRolls = abs(spaceColumn - dieColumn);
        // Boolean values for frontal and lateral moves.
        boolean frontalMove = false, lateralMove = false, secondFrontalMove = false, secondLateralMove = false;
        // String values for the die name before and after it's moved.
        String dieNameBefore = board.getDieName(dieRow, dieColumn), dieNameAfter;
        // Random seed.
        Random rand = new Random();
        // Determine if the die can be moved frontally or laterally from the die's coordinates.
        frontalMove = canMoveFrontally(board, dieRow, dieColumn, spaceRow, spacesToMove, 'C');
        lateralMove = canMoveLaterally(board, dieRow, dieColumn, spaceColumn, spacesToMove, 'C');
        // Determine if the die can be moved frontally or laterally after a 90 degree turn.
        if (frontalMove)
        {
            // If you can move frontally (at first) but you cannot move laterally afterwards and
            // the remaining number of spaces to travel is not 0, you cannot travel to that space.
            if (!canMoveLaterally(board, spaceRow, dieColumn, spaceColumn, columnRolls, 'C') && columnRolls != 0)
                secondLateralMove = false;
                // Otherwise, a second move is possible.
            else secondLateralMove = true;
        }
        if (lateralMove)
        {
            // If you can move laterally (at first) but you cannot move frontally afterwards and the remaining
            // number of spaces to travel is not 0, you cannot travel to that space.
            if (!canMoveFrontally(board, dieRow, spaceColumn, spaceRow, rowRolls, 'C') && rowRolls != 0)
                secondFrontalMove = false;
                // Otherwise, a second move is possible.
            else secondFrontalMove = true;
        }
        // Check if both ways are possible. If so, the computer will randomly decide if it wants to move laterally or frontally.
        if ((frontalMove && secondLateralMove) && (lateralMove && secondFrontalMove))
        {
            // Generate the number 0 or 1 randomly. 0 will be a frontal move, and 1 will be a lateral move.
            int decision = rand.nextInt(2);
            if (decision == 0)
            {
                // Make the move.
                makeMove(board, dieRow, dieColumn, spaceRow, "frontally");
                makeMove(board, spaceRow, dieColumn, spaceColumn, "laterally");
                // Get the name of the die after it is moved.
                dieNameAfter = board.getDieName(spaceRow, spaceColumn);
                // Print the computer's move.
                return printMove(dieNameBefore, dieNameAfter, dieRow, dieColumn, spaceRow, spaceColumn, "frontally", strategy);
            }
            else
            {
                // Make the move.
                makeMove(board, dieRow, dieColumn, spaceColumn, "laterally");
                makeMove(board, dieRow, spaceColumn, spaceRow, "frontally");
                // Get the name of the die after it is moved.
                dieNameAfter = board.getDieName(spaceRow, spaceColumn);
                // Print the computer's move.
                return printMove(dieNameBefore, dieNameAfter, dieRow, dieColumn, spaceRow, spaceColumn, "laterally", strategy);
            }
        }
        // If we can move frontally, make the move.
        if (frontalMove && secondLateralMove)
        {
            // Make the move.
            makeMove(board, dieRow, dieColumn, spaceRow, "frontally");
            makeMove(board, spaceRow, dieColumn, spaceColumn, "laterally");
            // Get the name of the die after it is moved.
            dieNameAfter = board.getDieName(spaceRow, spaceColumn);
            // Print the computer's move.
            return printMove(dieNameBefore, dieNameAfter, dieRow, dieColumn, spaceRow, spaceColumn, "frontally", strategy);
        }
        if (lateralMove && secondFrontalMove)
        {
            // Make the move.
            makeMove(board, dieRow, dieColumn, spaceColumn, "laterally");
            makeMove(board, dieRow, spaceColumn, spaceRow, "frontally");
            // Get the name of the die after it is moved.
            dieNameAfter = board.getDieName(spaceRow, spaceColumn);
            // Print the computer's move.
            return printMove(dieNameBefore, dieNameAfter, dieRow, dieColumn, spaceRow, spaceColumn, "laterally", strategy);
        }
        // somehow we got here. return error
        return "error";
    }

    /**
     * Prints the move that the computer made to the console
     * @param dieNameBefore the name of the die before it was moved
     * @param dieNameAfter the name of the die after it was moved
     * @param dieRow the row of the die before it was moved
     * @param dieColumn the column of the die before it was moved
     * @param spaceRow the row of the die after it was moved
     * @param spaceColumn the column of the die after it was moved
     * @param direction the direction the die initially moved in
     * @param strategy the strategy used to make the move
     * @return the move the computer made
     */
    private String printMove(String dieNameBefore, String dieNameAfter, int dieRow, int dieColumn, int spaceRow, int spaceColumn,
                           String direction, String strategy)
    {
        // Determine how many spaces the die was rolled frontally and laterally.
        int rowRolls = abs(spaceRow - dieRow);
        int columnRolls = abs(spaceColumn - dieColumn);

        // Start printing out the sentence that describes the move.
        String computerMove = "The computer picked " + dieNameBefore + " at (" + Integer.toString(dieRow) + "," +
                Integer.toString(dieColumn) + ") to roll because ";

        // The strategy is passed into the parameters from the score function that determines the strategy.
        if (strategy.equals("keyDieCapture"))
            // The die picked was within reach of the key die, and that is why it was moved.
            computerMove += "it was within distance of the human's key die.";
        if (strategy.equals("keySpaceCapture"))
            // The die picked was within reach of the key space, and that is why it was moved.
            computerMove += "it was within distance of the human's key space.";
        if (strategy.equals("blockKeyDie"))
            // The die picked was needed to block a key die capture.
            computerMove += "the key die was in danger of being captured, and the capture needed to be blocked.";
        if (strategy.equals("blockKeySpace"))
            // The die picked was needed to block a key space capture.
            computerMove += "the key space was in danger of being captured, and the capture needed to be blocked.";
        if (strategy.equals("dieCapture"))
            // The die picked was within reach of an enemy die that could be captured.
            computerMove += "it was within distance of a human's die that was able to be captured.";
        if (strategy.equals("random"))
            // Random move.
            computerMove += "the computer could not determine a decisive move to make, so it is making a move at random.";

        // Continue to print the computer's move.
        computerMove += "\nIt rolled it ";

        // Now print how the die was rolled. Use the direction passed into the function to determine how the die was rolled.
        if (direction.equals("frontally"))
        {
            computerMove += "frontally by " + Integer.toString(rowRolls);
            // If columnRolls is not 0, it was then rolled laterally. Display that roll as well.
            if (columnRolls != 0)
            {
                computerMove += " and laterally by " + Integer.toString(columnRolls);
            }
        }
        // Otherwise, it was rolled laterally.
        else
        {
            computerMove += "laterally by " + Integer.toString(columnRolls);
            // If rowRolls is not 0, it was then rolled frontally. Display that roll as well.
            if (rowRolls != 0)
            {
                computerMove += " and frontally by " + Integer.toString(rowRolls);
            }
        }

        // Print the rest of the sentence depending on the strategy.
        computerMove += " because ";

        if (strategy.equals("keyDieCapture"))
            // The die picked was able to capture the key die.
            computerMove += "those movements allowed it to move to the coordinates of the key die.";
        if (strategy.equals("keySpaceCapture"))
            // The die picked was able to cpature the key space.
            computerMove += "those movements allowed it to move to the coordinates of the key space.";
        if (strategy.equals("blockKeyDie"))
            // The die picked blocked a key die capture.
            computerMove += "those movements were able to block a key die capture.";
        if (strategy.equals("blockKeySpace"))
            // The die picked blocked a key space capture.
            computerMove += "those movements were able to block a key space capture.";
        if (strategy.equals("dieCapture"))
            // The die picked was able to capture an enemy die.
            computerMove += "those movements allowed it to move to the coordinates of the die it wants to capture.";
        if (strategy.equals("random"))
            // Random move.
            computerMove += "the die was able to move that way without any problems.";

        // Finish up the statement.
        computerMove += "\nThe die is now " + dieNameAfter + " at (" + Integer.toString(spaceRow) + "," +
                Integer.toString(spaceColumn) + ").";

        return computerMove;
    }
}