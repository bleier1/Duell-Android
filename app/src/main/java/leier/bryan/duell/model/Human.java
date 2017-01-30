package leier.bryan.duell.model;

import java.util.Random;

import leier.bryan.duell.model.Player;

/**
 * Created by Bryan on 11/23/2016.
 */

public class Human extends Player {

    /**
     * default constructor
     */
    public Human()
    {
        this.playerName = "Human";
    }

    /**
     * determines if the human can make the move they are trying to make on the board
     * @param board the board to make a move on
     * @param dieRow the die row of the die the human is trying to move
     * @param dieColumn the die column of the die the human is trying to move
     * @param spaceRow the space row of the space the human wants to move to
     * @param spaceColumn the space column of the space the human wants to move to
     * @return a boolean corresponding to whether or not the move is possible
     */
    public boolean canMakeMove(Board board, int dieRow, int dieColumn, int spaceRow, int spaceColumn)
    {
        boolean moveResult = canMoveToSpace(board, dieRow, dieColumn, spaceRow, spaceColumn, 'H');
        if (moveResult) return true;
        else return false;
    }

    /**
     * determines how the die should be moved on the board
     * @param board the board to move on
     * @param dieRow the row of the die being moved
     * @param dieColumn the column of the die being moved
     * @param spaceRow the row of the space being moved to
     * @param spaceColumn the column of the space being moved to
     * @return 0 if both directions are possible, 1 if lateral only, 2 if frontal only
     */
    public int determineDirection(Board board, int dieRow, int dieColumn, int spaceRow, int spaceColumn)
    {
        // These integers are the number of rolls it takes to change from the die position to the space position.
        int rowRolls = Math.abs(spaceRow - dieRow);
        int columnRolls = Math.abs(spaceColumn - dieColumn);
        // Booleans that determine if a frontal or lateral move is possible at first.
        boolean lateralMove = canMoveLaterally(board, dieRow, dieColumn, spaceColumn, board.getDieTopNum(dieRow, dieColumn), 'H');
        boolean frontalMove = canMoveFrontally(board, dieRow, dieColumn, spaceRow, board.getDieTopNum(dieRow, dieColumn), 'H');
        boolean secondFrontalMove = false;
        boolean secondLateralMove = false;
        if (!canMoveFrontally(board, dieRow, spaceColumn, spaceRow, rowRolls, 'H') && rowRolls != 0) {}
        else
        {
            secondFrontalMove = true;
        }
        if (!canMoveLaterally(board, spaceRow, dieColumn, spaceColumn, columnRolls, 'H') && columnRolls != 0) {}
        else
        {
            secondLateralMove = true;
        }

        // See how the player should determine their move. If both are possible, both directions are
        // possible. Return 0.
        if ((lateralMove && secondFrontalMove) && (frontalMove && secondLateralMove)) return 0;
        // If you only move laterally, only move laterally.
        if (lateralMove && secondFrontalMove) return 1;
        // If you can only move frontally, only move frontally.
        if (frontalMove && secondLateralMove) return 2;

        // error if we got here
        return 0;
    }

    /**
     * lets the human make a move on the board
     * @param board the board to make a move on
     * @param dieRow the row of the die to move
     * @param dieColumn the column of the die to move
     * @param spaceRow the row of the space to move to
     * @param spaceColumn the column of the space to move to
     * @param direction the direction to first move in
     */
    public void play(Board board, int dieRow, int dieColumn, int spaceRow, int spaceColumn, String direction)
    {
        // Call makeMove.
        if (direction.equals("frontally"))
        {
            makeMove(board, dieRow, dieColumn, spaceRow, "frontally");
            makeMove(board, spaceRow, dieColumn, spaceColumn, "laterally");
        }
        else
        {
            makeMove(board, dieRow, dieColumn, spaceColumn, "laterally");
            makeMove(board, dieRow, spaceColumn, spaceRow, "frontally");
        }
    }

    /**
     * gets help from the computer on a move to make
     * @param board the board to look for a recommendation on
     * @return a string containing the recommendation
     */
    public String getHelp(Board board)
    {
        // Array to initialize to get the results of the score functions:
        int[] dieCoords;
        // The computer will determine which of the human's dice that it could move. It needs to check for certain scenarios
        // to make the appropriate move. This is actually quite similar to how Computer.play() works, but it does not actually
        // make moves.

        // The key die results in an immediate win, so find where the human's key die is. If it can be captured, do it.
        dieCoords = captureKeyDieScore(board, 'H');
        if (dieCoords[0] != 0)
        {
            // Recommend a move to capture the key die.
            return recommendMove(board, dieCoords[0], dieCoords[1], dieCoords[2], dieCoords[3], "keyDieCapture");
        }
        // Key space capture results in a win as well, so see if the human can travel to it.
        dieCoords = captureKeySpaceScore(board, 'H');
        if (dieCoords[0] != 0)
        {
            // Recommend a move to capture the key space.
            return recommendMove(board, dieCoords[0], dieCoords[1], dieCoords[2], dieCoords[3], "keySpaceCapture");
        }
        // The computer must also make moves to play defensively if it detects that the human could possibly win.
        // If a human's die is close to the computer's key die, block the capture or move the die.
        dieCoords = blockKeyDieScore(board, 'H');
        if (dieCoords[0] != 0)
        {
            // Recommend a move to block key die capture.
            return recommendMove(board, dieCoords[0], dieCoords[1], dieCoords[2], dieCoords[3], "blockKeyDie");
        }
        // If a human's die is close to the computer's key space, block the capture or move the die.
        dieCoords = blockKeySpaceScore(board, 'H');
        if (dieCoords[0] != 0)
        {
            // Recommend a move to block key space capture.
            return recommendMove(board, dieCoords[0], dieCoords[1], dieCoords[2], dieCoords[3], "blockKeySpace");
        }
        // If the code flow is at this point, there's no reason to play defensively. Seek a die to capture.
        dieCoords = captureDieScore(board, 'H');
        if (dieCoords[0] != 0)
        {
            // Recommend a move to capture the enemy die.
            return recommendMove(board, dieCoords[0], dieCoords[1], dieCoords[2], dieCoords[3], "dieCapture");
        }
        // Otherwise, random move.
        else
        {
            // Find a random move to make.
            dieCoords = randomMove(board, 'H');
            // Recommend the move.
            return recommendMove(board, dieCoords[0], dieCoords[1], dieCoords[2], dieCoords[3], "random");
        }
    }

    /**
     * puts together the recommendation that the computer makes to the player
     * @param board the board that is being played on
     * @param dieRow row of die to move
     * @param dieColumn column of die to move
     * @param spaceRow row of space to move to
     * @param spaceColumn column of space to move to
     * @param strategy the reasoning behind the recommendation
     * @return a string that contains the message to give to the player
     */
    private String recommendMove(Board board, int dieRow, int dieColumn, int spaceRow, int spaceColumn, String strategy)
    {
        // The spaces to move the die.
        int spacesToMove = board.getDieTopNum(dieRow, dieColumn);
        // The amount of spaces needed to traverse to the given coordinates.
        int rowRolls = Math.abs(spaceRow - dieRow);
        int columnRolls = Math.abs(spaceColumn - dieColumn);
        // Boolean values for frontal and lateral moves.
        boolean frontalMove = false, lateralMove = false, secondFrontalMove = false, secondLateralMove = false;
        // The string to return.
        String recommendation;
        // Random seed.
        Random rand = new Random();

        // Start printing the recommendation.
        recommendation = "The computer recommends moving " + board.getDieName(dieRow, dieColumn) + " at (" + Integer.toString(dieRow)
                + "," + Integer.toString(dieColumn) + ") because ";
        // Look at the strategy passed into the function.
        if (strategy.equals("keyDieCapture"))
            // The die picked was within reach of the key die.
            recommendation += "it is within distance of the computer's key die.\n";
        if (strategy.equals("keySpaceCapture"))
            // The die picked was within reach of the key space, and that is why it was moved.
            recommendation += "it is within distance of the computer's key space.\n";
        if (strategy.equals("blockKeyDie"))
            // The die picked was needed to block a key die capture.
            recommendation += "the key die is in danger of being captured, and the capture needs to be blocked.\n";
        if (strategy.equals("blockKeySpace"))
            // The die picked was needed to block a key space capture.
            recommendation += "the key space in danger of being captured, and the capture needs to be blocked.\n";
        if (strategy.equals("dieCapture"))
            // The die picked was within reach of an enemy die that could be captured.
            recommendation += "it is within distance of a computer's die that is able to be captured.\n";
        if (strategy.equals("random"))
            // Random move.
            recommendation += "the computer could not determine a decisive move to make, so it is making a move at random.\n";

        // Start printing the rest of the recommendation.
        recommendation += "It recommends rolling ";

        // Determine if the die can be moved frontally or laterally from the die's coordinates.
        frontalMove = canMoveFrontally(board, dieRow, dieColumn, spaceRow, spacesToMove, 'H');
        lateralMove = canMoveLaterally(board, dieRow, dieColumn, spaceColumn, spacesToMove, 'H');
        // Determine if the die can be moved frontally or laterally after a 90 degree turn.
        if (frontalMove)
        {
            // If you can move frontally (at first) but you cannot move laterally afterwards and
            // the remaining number of spaces to travel is not 0, you cannot travel to that space.
            if (!canMoveLaterally(board, spaceRow, dieColumn, spaceColumn, columnRolls, 'H') && columnRolls != 0)
                secondLateralMove = false;
                // Otherwise, a second move is possible.
            else secondLateralMove = true;
        }
        if (lateralMove)
        {
            // If you can move laterally (at first) but you cannot move frontally afterwards and the remaining
            // number of spaces to travel is not 0, you cannot travel to that space.
            if (!canMoveFrontally(board, dieRow, spaceColumn, spaceRow, rowRolls, 'H') && rowRolls != 0)
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
                // Recommend to move frontally and not laterally.
                lateralMove = false;
            }
            else
            {
                // Recommend to move laterally and not frontally.
                frontalMove = false;
            }

        }
        // If we can move frontally, make the move.
        if (frontalMove && secondLateralMove)
        {
            // Start printing the move recommendation.
            recommendation += "frontally by " + Integer.toString(rowRolls);
            // If you can also move laterally, describe that as well.
            if (columnRolls != 0)
                recommendation += " and laterally by " + Integer.toString(columnRolls);
        }
        if (lateralMove && secondFrontalMove)
        {
            // Start printing the move recommendation.
            recommendation += "laterally by " + Integer.toString(columnRolls);
            // If you can also move laterally, describe that as well.
            if (rowRolls != 0)
                recommendation += " and frontally by " + Integer.toString(rowRolls);
        }

        // Start printing the rest of the sentence.
        recommendation += " because ";

        // Look at the strategy passed into the function once more.
        if (strategy.equals("keyDieCapture"))
            // The die picked was within reach of the key die.
            recommendation += "it can capture the key die with this move.";
        if (strategy.equals("keySpaceCapture"))
            // The die picked was within reach of the key space, and that is why it was moved.
            recommendation += "it can capture the key space with this move.";
        if (strategy.equals("blockKeyDie"))
            // The die picked was needed to block a key die capture.
            recommendation += "the key die capture will be blocked with this move.";
        if (strategy.equals("blockKeySpace"))
            // The die picked was needed to block a key space capture.
            recommendation += "the key space capture will be blocked with this move.";
        if (strategy.equals("dieCapture"))
            // The die picked was within reach of an enemy die that could be captured.
            recommendation += "the die will be captured with this move.";
        if (strategy.equals("random"))
            // Random move.
            recommendation += "the die is able to move this way without any problems.";

        return recommendation;
    }
}
