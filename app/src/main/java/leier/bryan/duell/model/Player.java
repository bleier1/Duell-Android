package leier.bryan.duell.model;

import java.util.Random;

import static java.lang.Math.abs;
import static java.lang.Math.subtractExact;

/**
 * Created by Bryan on 11/12/2016.
 */

public class Player {
    // The name of the player.
    protected String playerName;

    // GUI components... later.

    /**
     * Default constructor. The name is assumed to be N/A.
     */
    public Player()
    {
        this.playerName = "N/A";
    }

    /**
     * Constructor that lets a name be defined.
     */
    public Player(String name)
    {
        this.playerName = name;
    }

    /**
     * Returns the name of the player.
     * @return a string containing the name of the player
     */
    public String getPlayerName()
    {
        return playerName;
    }

    /**
     * Makes the move that the player wants to make to the space provided by the coordinates in the parameters.
     * @param board the board to make a move on
     * @param dieRow the row coordinate of the die to move
     * @param dieColumn the column coordinate of the die to move
     * @param spaceCoordinate the coordinate to move the die to
     * @param direction the direction to move in
     */
    protected void makeMove(Board board, int dieRow, int dieColumn, int spaceCoordinate, String direction)
    {
        // First, determine to move frontally or laterally.
        if (direction.equals("frontally"))
        {
            // Is the space coordinate entered greater than the dieRow? If so, we must move upwards.
            if (spaceCoordinate > dieRow)
            {
                for (int i = dieRow; i < spaceCoordinate; i++)
                {
                    board.performRoll(i, dieColumn, "up");
                }
            }
            // Otherwise, move downwards.
            else
            {
                for (int i = dieRow; i > spaceCoordinate; i--)
                {
                    board.performRoll(i, dieColumn, "down");
                }
            }
        }
        // Otherwise, move laterally.
        else
        {
            // Is the spaceCoordinate entered greater than the dieColumn? If so, we must move towards the right.
            if (spaceCoordinate > dieColumn)
            {
                for (int i = dieColumn; i < spaceCoordinate; i++)
                {
                    board.performRoll(dieRow, i, "right");
                }
            }
            // Otherwise, move towards the left.
            else
            {
                for (int i = dieColumn; i > spaceCoordinate; i--)
                {
                    board.performRoll(dieRow, i, "left");
                }
            }
        }
    }

    /**
     * Determines if a die can move to a space laterally.
     * @param board the board to check for a move on
     * @param dieRow the row coordinate of the die to check
     * @param dieColumn the column coordinate of the die to check
     * @param spaceColumn the column coordinate of the space to move to
     * @param spacesToMove the amount of spaces the die will move to reach the column
     * @param playerType the player type of the die to check
     * @return boolean determining if a move if possible or not
     */
    protected boolean canMoveLaterally(Board board, int dieRow, int dieColumn, int spaceColumn, int spacesToMove, char playerType)
    {
        // First, if spacesToMove is 0, we can't move anywhere.
        if (spacesToMove == 0) return false;
        // If dieColumn and spaceColumn are the same, we can only move frontally.
        if (dieColumn == spaceColumn) return false;
        // We'll move to the right if the spaceColumn is greater than the dieColumn. Otherwise, we move to the left.
        if (spaceColumn > dieColumn)
        {
            // A for loop to check all of the spaces that we want to visit.
            for (int i = dieColumn; i < spaceColumn; i++)
            {
                // Check if there is a die on the space.
                if (board.isDieOn(dieRow, i+1))
                {
                    // If spacesToMove is not 1, you cannot place the die there regardless of whether or not the die on it
                    // is yours or the opponent's.
                    if (spacesToMove != 1) return false;
                        // If it is 1, check the playerType of the die on it.
                    else
                    {
                        // If it is not the playerType passed into the function, it is an opponent's die and can be captured.
                        if (!board.isDiePlayerType(dieRow, i+1, playerType)) return true;
                            // Otherwise, this is one of the player's own dice and thus cannot be captured or moved to.
                        else return false;
                    }
                }
                // If there is not, decrement spacesToMove.
                spacesToMove--;
            }
            // We can move this way.
            return true;
        }
        // Move to the left if this is not the case.
        else
        {
            // A for loop to check all of the spaces that we want to visit.
            for (int i = dieColumn; i > spaceColumn; i--)
            {
                // Check if there is a die on the space.
                if (board.isDieOn(dieRow, i-1))
                {
                    // If spacesToMove is not 1, you cannot place the die there regardless of whether or not the die on it
                    // is yours or the opponent's.
                    if (spacesToMove != 1) return false;
                        // If it is 1, check the playerType of the die on it.
                    else
                    {
                        // If it is not the playerType passed into the function, it is an opponent's die and can be captured.
                        if (!board.isDiePlayerType(dieRow, i-1, playerType)) return true;
                            // Otherwise, this is one of the player's own dice and thus cannot be captured or moved to.
                        else return false;
                    }
                }
                // If there is not, decrement spacesToMove.
                spacesToMove--;
            }
            // We can move this way.
            return true;
        }
    }

    /**
     * Determines if a die can move to a space frontally.
     * @param board the board to check for a move on
     * @param dieRow the row coordinate of the die to check
     * @param dieColumn the column coordinate of the die to check
     * @param spaceRow the row coordinate of the space to move to
     * @param spacesToMove the amount of spaces the die will move to reach the column
     * @param playerType the player type of the die to check
     * @return boolean determining if a move if possible or not
     */
    protected boolean canMoveFrontally(Board board, int dieRow, int dieColumn, int spaceRow, int spacesToMove, char playerType)
    {
        // First, if spacesToMove is 0, we can't move anywhere.
        if (spacesToMove == 0) return false;
        // If dieRow and spaceRow are the same, we can only move laterally.
        if (dieRow == spaceRow) return false;
        // We'll move up if the spaceRow is greater than the dieRow. Otherwise, we move down.
        if (spaceRow > dieRow)
        {
            // A for loop to check all of the spaces that we want to visit.
            for (int i = dieRow; i < spaceRow; i++)
            {
                // Check if there is a die on the space.
                if (board.isDieOn(i + 1, dieColumn))
                {
                    // If spacesToMove is not 1, you cannot place the die there regardless of whether or not the die on it
                    // is yours or the opponent's.
                    if (spacesToMove != 1) return false;
                        // If it is 1, check the playerType of the die on it.
                    else
                    {
                        // If it is not the playerType passed into the function, it is an opponent's die and can be captured.
                        if (!board.isDiePlayerType(i+1, dieColumn, playerType)) return true;
                            // Otherwise, this is one of the player's own dice and thus cannot be captured or moved to.
                        else return false;
                    }
                }
                // If there is not, decrement spacesToMove.
                spacesToMove--;
            }
            // We can move this way.
            return true;
        }
        // Move down if this is not the case.
        else
        {
            // A for loop to check all of the spaces that we want to visit.
            for (int i = dieRow; i > spaceRow; i--)
            {
                // Check if there is a die on the space.
                if (board.isDieOn(i - 1, dieColumn))
                {
                    // If spacesToMove is not 1, you cannot place the die there regardless of whether or not the die on it
                    // is yours or the opponent's.
                    if (spacesToMove != 1) return false;
                        // If it is 1, check the playerType of the die on it.
                    else
                    {
                        // If it is not the playerType passed into the function, it is an opponent's die and can be captured.
                        if (!board.isDiePlayerType(i-1, dieColumn, playerType)) return true;
                            // Otherwise, this is one of the player's own dice and thus cannot be captured or moved to.
                        else return false;
                    }
                }
                // If there is not, decrement spacesToMove.
                spacesToMove--;
            }
            // We can move this way.
            return true;
        }
    }

    /**
     * Used to get the coordinates of a key die on the board
     * @param board the board to search on
     * @param playerType the player type of the key die to look for
     * @return dieCoords an array containing the coordinates of the key die
     */
    protected int[] findKeyDie(Board board, char playerType)
    {
        // First initialize an array with two elements.
        int[] dieCoords = new int[2];
        // Use a for loop to iterate through the board.
        for (int i = 8; i > 0; i--)
        {
            for (int j = 1; j < 10; j++)
            {
                // If there is a key die on the space, check the playerType.
                if (board.isKeyDie(i, j))
                {
                    // If it is of the playerType passed into the function, we have found the key die. Register its coordinates.
                    if (board.isDiePlayerType(i, j, playerType))
                    {
                        dieCoords[0] = i;
                        dieCoords[1] = j;
                        return dieCoords;
                    }
                }
            }
        }
        // If not found, return 0 in both positions.
        dieCoords[0] = 0;
        dieCoords[1] = 0;
        return dieCoords;
    }

    /**
     * Determines if a die can be moved to a space on the board
     * @param board the board to make a move on
     * @param dieRow the row coordinate of the die to move
     * @param dieColumn the column coordinate of the die to move
     * @param spaceRow the row coordinate of the space to move to
     * @param spaceColumn the column coordinate of the space to move to
     * @param playerType the player type of the die to move
     * @return a boolean that determines of a move is possible or not
     */
    protected boolean canMoveToSpace(Board board, int dieRow, int dieColumn, int spaceRow, int spaceColumn, char playerType)
    {
        // Integer for the top number of a die.
        int topNumber = board.getDieTopNum(dieRow, dieColumn);
        // Integers for the number of rolls needed to go to a space.
        int rowRolls = abs(spaceRow - dieRow);
        int columnRolls = abs(spaceColumn - dieColumn);
        // Boolean values of whether or not you can move frontally or laterally.
        boolean frontalMove;
        boolean lateralMove;
        // Boolean values of whether or not you can move frontally or laterally after already moving in that direction.
        boolean secondFrontalMove;
        boolean secondLateralMove;

        // First, check to see if the number on the top of the die is able to move enough spaces to travel to the coordinates.
        if (topNumber != rowRolls + columnRolls) return false;

        // Now check to see if the coordinates the die wants to move to are valid positions.
        if ((spaceRow < 1 || spaceRow > 8) || (spaceColumn < 1 || spaceColumn > 9)) return false;

        // Now check if the die can move there without any problems.
        frontalMove = canMoveFrontally(board, dieRow, dieColumn, spaceRow, topNumber, playerType);
        lateralMove = canMoveLaterally(board, dieRow, dieColumn, spaceColumn, topNumber, playerType);
        // If it cannot move either way at first, it cannot move to the space.
        if (!frontalMove && !lateralMove) return false;
        // If it can move frontally, check to see if it can move laterally in a 90 degree turn.
        if (frontalMove)
        {
            // If you can move frontally (at first) but you cannot move laterally afterwards and
            // the remaining number of spaces to travel is not 0, you cannot travel to that space.
            if (!canMoveLaterally(board, spaceRow, dieColumn, spaceColumn, columnRolls, playerType)
                    && columnRolls != 0)
                secondLateralMove = false;
                // Otherwise, a move is possible.
            else return true;
        }
        // If it can move laterally, check to see if it can move frontally in a 90 degree turn.
        if (lateralMove)
        {
            // If you can move laterally (at first) but you cannot move frontally afterwards and
            // the remaining number of spaces to travel is not 0, you cannot travel to that space.
            if (!canMoveFrontally(board, dieRow, spaceColumn, spaceRow, rowRolls, playerType) && rowRolls != 0)
                secondFrontalMove = false;
                // Otherwise, a move is possible.
            else return true;
        }

        // Otherwise, there is no possible move.
        return false;
    }

    /**
     * Determines if a key die can be captured or not.
     * @param board the board being played on
     * @param playerType the player type of the player "calling" the function
     * @return dieCoords, the coordinates of dice to move and where to move to. If a move is not possible, 0 is returned for both coordinates
     */
    protected int[] captureKeyDieScore(Board board, char playerType)
    {
        // The coordinates to move to for the capture.
        int[] dieCoords = new int[4];
        // First, find the coordinates of the key die.
        int[] keyCoords;
        if (playerType == 'H') keyCoords = findKeyDie(board, 'C');
        else keyCoords = findKeyDie(board, 'H');
        // Put the keyCoords into spaceRow and spaceColumn.
        int spaceRow = keyCoords[0];
        int spaceColumn = keyCoords[1];
        // Now that we have found the opponent's key die, scan the board for your dice positions.
        for (int i = 8; i > 0; i--)
        {
            for (int j = 1; j < 10; j++)
            {
                // See if there a player's die occupying the space.
                if (board.isDieOn(i, j) && board.isDiePlayerType(i, j, playerType))
                {
                    // Check if it can move there without any problems. Return if we can.
                    if (canMoveToSpace(board, i, j, spaceRow, spaceColumn, playerType))
                    {
                        dieCoords[0] = i;
                        dieCoords[1] = j;
                        dieCoords[2] = keyCoords[0];
                        dieCoords[3] = keyCoords[1];
                        return dieCoords;
                    }
                }
            }
        }
        // Capturing the key die is not feasible.
        dieCoords[0] = 0;
        dieCoords[1] = 0;
        dieCoords[2] = 0;
        dieCoords[3] = 0;
        return dieCoords;
    }

    /**
     * Determines if a key space can be captured or not
     * @param board the board being played on
     * @param playerType the player type of the player "calling" the function
     * @return dieCoords, the coordinates of what die to move. If a move is not possible, 0 is returned for both coordinates
     */
    protected int[] captureKeySpaceScore(Board board, char playerType)
    {
        // The dieCoords to return.
        int[] dieCoords = new int[2];

        // Get the coordinates of the key space:
        int spaceRow;
        int spaceColumn;
        if (playerType == 'H')
        {
            spaceRow = 8;
            spaceColumn = 5;
        }
        else
        {
            spaceRow = 1;
            spaceColumn = 5;
        }

        // The coordinates of the key space are passed into the function. All we need to do is scan the board for the
        // player's dice to see if any of them can occupy it.
        for (int i = 8; i > 0; i--)
        {
            for (int j = 1; j < 10; j++)
            {
                // See if a player's die is occupying the space.
                if (board.isDieOn(i, j) && board.isDiePlayerType(i, j, playerType))
                {
                    // Check if it can move there without any problems. Return if we can.
                    if (canMoveToSpace(board, i, j, spaceRow, spaceColumn, playerType))
                    {
                        dieCoords[0] = i;
                        dieCoords[1] = j;
                        return dieCoords;
                    }
                }
            }
        }
        // Capturing the key space is not feasible.
        dieCoords[0] = 0;
        dieCoords[1] = 0;
        return dieCoords;
    }

    /**
     * Determines if a key die needs to be blocked or not
     * @param board the board being played on
     * @param playerType the player type of the player "calling" the function
     * @return dieCoords, the coordinates of a die to move and where to go. If a move is not possible, 0 is returned for all coordinates
     */
    protected int[] blockKeyDieScore(Board board, char playerType)
    {
        // The array to return containing the coordinates of spaces:
        int[] dieCoords = new int[4];
        // Array containing the coordinates of the player's key die.
        int[] keyDieCoords = findKeyDie(board, playerType);
        // Integers to store key die coordinates.
        int keyDieRow;
        int keyDieColumn;
        // Character to store the opponent's playerType.
        char opponentType;
        if (playerType == 'H') opponentType = 'C';
        else opponentType = 'H';

        // Get the player's key die coordinates.
        keyDieRow = keyDieCoords[0];
        keyDieColumn = keyDieCoords[1];

        // Now scan the board for the opponent's dice.
        for (int i = 8; i > 0; i--)
        {
            for (int j = 1; j < 10; j++)
            {
                // See if an opponents's die is occupying the space.
                if (board.isDieOn(i, j) && board.isDiePlayerType(i, j, opponentType))
                {
                    // Check if it can move there without any problems.
                    if (canMoveToSpace(board, i, j, keyDieRow, keyDieColumn, opponentType))
                    {
                        // We need to create a problem/capture the offending die.
                        // Scan the board for the player's dice.
                        for (int playerRow = 8; playerRow > 0; playerRow--)
                        {
                            for (int playerCol = 1; playerCol < 10; playerCol++)
                            {
                                // See if the player's die is occupying the space.
                                if (board.isDieOn(playerRow, playerCol) && board.isDiePlayerType(playerRow,
                                        playerCol, playerType))
                                {
                                    // Check if it can move to the offending die without any problems.
                                    if (canMoveToSpace(board, playerRow, playerCol, i, j, playerType))
                                    {
                                        // Return 1 if it can, as the die can be captured with this move.
                                        dieCoords[0] = playerRow;
                                        dieCoords[1] = playerCol;
                                        dieCoords[2] = i;
                                        dieCoords[3] = j;
                                        return dieCoords;
                                    }
                                    // Otherwise, try to block the die's path.
                                    // See if the offending die and the key die are in the same row.
                                    if (keyDieRow == i)
                                    {
                                        // If key die is to the left of the offending die, only search between those
                                        // spaces:
                                        if (keyDieColumn < j)
                                        {
                                            for (int a = keyDieColumn; a < j; a++)
                                            {
                                                // Check if the die can move there.
                                                if (canMoveToSpace(board, playerRow, playerCol, keyDieRow, a,
                                                        playerType))
                                                {
                                                    // The die can be blocked. Move there.
                                                    dieCoords[0] = playerRow;
                                                    dieCoords[1] = playerCol;
                                                    dieCoords[2] = keyDieRow;
                                                    dieCoords[3] = a;
                                                    return dieCoords;
                                                }
                                            }
                                        }
                                        // Otherwise, the key die is to the right:
                                        else
                                        {
                                            for (int b = j; b > keyDieColumn; b--)
                                            {
                                                // Check if the die can move there.
                                                if (canMoveToSpace(board, playerRow, playerCol, keyDieRow, b,
                                                        playerType))
                                                {
                                                    // The die can be blocked. Move there.
                                                    dieCoords[0] = playerRow;
                                                    dieCoords[1] = playerCol;
                                                    dieCoords[2] = keyDieRow;
                                                    dieCoords[3] = b;
                                                    return dieCoords;
                                                }
                                            }
                                        }
                                    }
                                    // See if the offending die and the key die are in the same column.
                                    if (keyDieColumn == j)
                                    {
                                        // If the key die is above the offending die, only search between those spaces:
                                        if (keyDieRow > j)
                                        {
                                            for (int c = i; c < keyDieRow; c++)
                                            {
                                                // Check if the die can move there.
                                                if (canMoveToSpace(board, playerRow, playerCol, c, keyDieColumn,
                                                        playerType))
                                                {
                                                    // The die can be blocked. Move there.
                                                    dieCoords[0] = playerRow;
                                                    dieCoords[1] = playerCol;
                                                    dieCoords[2] = c;
                                                    dieCoords[3] = keyDieColumn;
                                                    return dieCoords;
                                                }
                                            }
                                        }
                                        // Otherwise, it's below the key die:
                                        else
                                        {
                                            for (int d = keyDieRow; d > i; d--)
                                            {
                                                // Check if the die can move there.
                                                if (canMoveToSpace(board, playerRow, playerCol, d, keyDieColumn,
                                                        playerType))
                                                {
                                                    // The die can be blocked. Move there.
                                                    dieCoords[0] = playerRow;
                                                    dieCoords[1] = playerCol;
                                                    dieCoords[2] = d;
                                                    dieCoords[3] = keyDieColumn;
                                                    return dieCoords;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        // When all else fails, try moving the key die away.
                        // If they're in the same row, move to a different row:
                        if (keyDieRow == i)
                        {
                            // Try moving the the row below.
                            if (canMoveToSpace(board, keyDieRow, keyDieColumn, keyDieRow - 1, keyDieColumn,
                                    playerType))
                            {
                                // Move there.
                                dieCoords[0] = keyDieRow;
                                dieCoords[1] = keyDieColumn;
                                dieCoords[2] = keyDieRow-1;
                                dieCoords[3] = keyDieColumn;
                                return dieCoords;
                            }
                            // Try moving the row above.
                            if (canMoveToSpace(board, keyDieRow, keyDieColumn, keyDieRow + 1, keyDieColumn,
                                    playerType))
                            {
                                // Move there.
                                dieCoords[0] = keyDieRow;
                                dieCoords[1] = keyDieColumn;
                                dieCoords[2] = keyDieRow+1;
                                dieCoords[3] = keyDieColumn;
                                return dieCoords;
                            }
                        }
                        // If they're in the same column move to a different column:
                        if (keyDieColumn == j)
                        {
                            // Try moving to the column to the right.
                            if (canMoveToSpace(board, keyDieRow, keyDieColumn, keyDieRow, keyDieColumn + 1,
                                    playerType))
                            {
                                // Move there.
                                dieCoords[0] = keyDieRow;
                                dieCoords[1] = keyDieColumn;
                                dieCoords[2] = keyDieRow;
                                dieCoords[3] = keyDieColumn+1;
                                return dieCoords;
                            }
                            // Try moving to the column to the left.
                            if (canMoveToSpace(board, keyDieRow, keyDieColumn, keyDieRow, keyDieColumn - 1,
                                    playerType))
                            {
                                // Move there.
                                dieCoords[0] = keyDieRow;
                                dieCoords[1] = keyDieColumn;
                                dieCoords[2] = keyDieRow;
                                dieCoords[3] = keyDieColumn-1;
                                return dieCoords;
                            }
                        }
                    }
                }
            }
        }
        // Blocking the key die from being captured is not necessary or feasible.
        dieCoords[0] = 0;
        dieCoords[1] = 0;
        dieCoords[2] = 0;
        dieCoords[3] = 0;
        return dieCoords;
    }

    /**
     * Determines if a key space capture needs to be blocked or not.
     * @param board the board being played on
     * @param playerType the player type of the player "calling" the function
     * @return dieCoords, an array containing coordinates of a die to move and where to go. if no move is possible, 0 for all coords is returned
     */
    protected int[] blockKeySpaceScore(Board board, char playerType)
    {
        // Array of coordinates to return.
        int[] dieCoords = new int[4];
        // Integers to store the key space row and column, as they may be overwritten with new coordinates to move to.
        int keySpaceRow;
        int keySpaceColumn;
        if (playerType == 'H')
        {
            keySpaceRow = 1;
            keySpaceColumn = 5;
        }
        else
        {
            keySpaceRow = 8;
            keySpaceColumn = 5;
        }
        // Opponent player type.
        char opponentType;
        if (playerType == 'H') opponentType = 'C';
        else opponentType = 'H';

        // The coordinates of the key space are passed into the function. Scan the board for the opponent's dice to see if
        // any of them can occupy it.
        for (int i = 8; i > 0; i--)
        {
            for (int j = 1; j < 10; j++)
            {
                // See if an opponents's die is occupying the space.
                if (board.isDieOn(i, j) && board.isDiePlayerType(i, j, opponentType))
                {
                    // See if the die can move to the key space.
                    if (canMoveToSpace(board, i, j, keySpaceRow, keySpaceColumn, opponentType))
                    {
                        // We need to create a problem/capture the offending die. Scan the board for the player's dice.
                        for (int playerRow = 8; playerRow > 0; playerRow--)
                        {
                            for (int playerCol = 1; playerCol < 10; playerCol++)
                            {
                                // See if the player's die is occupying the space.
                                if (board.isDieOn(playerRow, playerCol) && board.isDiePlayerType(playerRow,
                                        playerCol, playerType))
                                {
                                    // See if the die can move to the offending die's position.
                                    if (canMoveToSpace(board, playerRow, playerCol, i, j, playerType))
                                    {
                                        // We can capture the die.
                                        dieCoords[0] = playerRow;
                                        dieCoords[1] = playerCol;
                                        dieCoords[2] = i;
                                        dieCoords[3] = j;
                                        return dieCoords;
                                    }
                                    // Otherwise, see if the die can block the path to the key space.
                                    // See if the offending die and key space are in the same row:
                                    if (keySpaceRow == i)
                                    {
                                        // If spaceColumn is less than the row of the offending die, it's left of the die:
                                        if (keySpaceColumn < j)
                                        {
                                            for (int a = keySpaceColumn; a < j; a++)
                                            {
                                                // If we can move to a spot between them, move there.
                                                if (canMoveToSpace(board, playerRow, playerCol, keySpaceRow, a, playerType))
                                                {
                                                    dieCoords[0] = playerRow;
                                                    dieCoords[1] = playerCol;
                                                    dieCoords[2] = keySpaceRow;
                                                    dieCoords[3] = a;
                                                    return dieCoords;
                                                }
                                            }
                                        }
                                        // Otherwise, it's right of the die.
                                        else
                                        {
                                            for (int b = j; b > keySpaceRow; b--)
                                            {
                                                // If we can move to a spot between them, move there.
                                                if (canMoveToSpace(board, playerRow, playerCol, keySpaceRow, b, playerType))
                                                {
                                                    dieCoords[0] = playerRow;
                                                    dieCoords[1] = playerCol;
                                                    dieCoords[2] = keySpaceRow;
                                                    dieCoords[3] = b;
                                                    return dieCoords;
                                                }
                                            }
                                        }
                                    }
                                    // See if the offending die and key space are in the same column.
                                    if (keySpaceColumn == j)
                                    {
                                        // If the keySpaceRow is less than i, it's below the die:
                                        if (keySpaceRow < i)
                                        {
                                            for (int c = keySpaceRow; c < i; c++)
                                            {
                                                // If we can move to a spot between them, move there.
                                                if (canMoveToSpace(board, playerRow, playerCol, c, keySpaceColumn, playerType))
                                                {
                                                    dieCoords[0] = playerRow;
                                                    dieCoords[1] = playerCol;
                                                    dieCoords[2] = c;
                                                    dieCoords[3] = keySpaceColumn;
                                                    return dieCoords;
                                                }
                                            }
                                        }
                                        // Otherwise, it's above the die:
                                        else
                                        {
                                            for (int d = i; d < keySpaceRow; d++)
                                            {
                                                // If we can move to a spot between them, move there.
                                                if (canMoveToSpace(board, playerRow, playerCol, d, keySpaceColumn, playerType))
                                                {
                                                    dieCoords[0] = playerRow;
                                                    dieCoords[1] = playerCol;
                                                    dieCoords[2] = d;
                                                    dieCoords[3] = keySpaceColumn;
                                                    return dieCoords;
                                                }
                                            }
                                        }
                                    }
                                    // Otherwise, try moving within the column of the key space.
                                    // Iteration is different depending on the row of the key space.
                                    if (keySpaceRow == 8)
                                    {
                                        for (int e = 1; e < keySpaceRow; e++)
                                        {
                                            // Try moving to the space.
                                            if (canMoveToSpace(board, playerRow, playerCol, e, keySpaceColumn, playerType))
                                            {
                                                dieCoords[0] = playerRow;
                                                dieCoords[1] = playerCol;
                                                dieCoords[2] = e;
                                                dieCoords[3] = keySpaceColumn;
                                                return dieCoords;
                                            }
                                        }
                                    }
                                    else
                                    {
                                        for (int f = keySpaceRow; f > 1; f--)
                                        {
                                            // Try moving to the space.
                                            if (canMoveToSpace(board, playerRow, playerCol, f, keySpaceColumn, playerType))
                                            {
                                                dieCoords[0] = playerRow;
                                                dieCoords[1] = playerCol;
                                                dieCoords[2] = f;
                                                dieCoords[3] = keySpaceColumn;
                                                return dieCoords;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        // Blocking the key space from being captured is not necessary or feasible.
        dieCoords[0] = 0;
        dieCoords[1] = 0;
        dieCoords[2] = 0;
        dieCoords[3] = 0;
        return dieCoords;
    }

    /**
     * Determines if a die can be captured or not
     * @param board the board being played on
     * @param playerType the player type of the player "calling" the function
     * @return dieCoords, an array containing coordinates of a die to move and where to go. if no move is possible, 0 for all coords is returned
     */
    protected int[] captureDieScore(Board board, char playerType)
    {
        // Array to contain coordinates.
        int[] dieCoords = new int[4];
        // Character to store opposing player type.
        char opponentType;
        if (playerType == 'H') opponentType = 'C';
        else opponentType = 'H';
        // Scan the board for opponent's dice.
        for (int i = 8; i > 0; i--)
        {
            for (int j = 1; j < 10; j++)
            {
                // See if an opponents's die is occupying the space.
                if (board.isDieOn(i, j) && board.isDiePlayerType(i, j, opponentType))
                {
                    // Scan the board for dice of the player's type.
                    for (int playerRow = 8; playerRow > 0; playerRow--)
                    {
                        for (int playerCol = 1; playerCol < 10; playerCol++)
                        {
                            // See if a player's die is occupying the space.
                            if (board.isDieOn(playerRow, playerCol) && board.isDiePlayerType(playerRow, playerCol, playerType))
                            {
                                // See if that die can move to the space occupied by the enemy die. If we can, return true.
                                if (canMoveToSpace(board, playerRow, playerCol, i, j, playerType))
                                {
                                    dieCoords[0] = playerRow;
                                    dieCoords[1] = playerCol;
                                    dieCoords[2] = i;
                                    dieCoords[3] = j;
                                    return dieCoords;
                                }
                            }
                        }
                    }
                }
            }
        }
        // Moving to this space is not feasible.
        dieCoords[0] = 0;
        dieCoords[1] = 0;
        dieCoords[2] = 0;
        dieCoords[3] = 0;
        return dieCoords;
    }

    /**
     * Makes a random move on the board
     * @param board the board being played on
     * @param playerType the player type of the player "calling" the function
     * @return dieCoords, an array that contains coordinates of a die to move and where to go
     */
    protected int[] randomMove(Board board, char playerType)
    {
        // Array to return with coordinates.
        int[] dieCoords = new int[4];
        // Array of boolean values to look at whether or not that row has already been checked.
        boolean[] alreadyChecked = {false, false, false, false, false, false, false, false};
        // Integer that will store a random row value.
        int randomRow;
        // Integer that will store the row rolls and column rolls necessary to make a move.
        int rowRolls = 0, columnRolls = 0;
        // The top number of the die.
        int topNum = 0;
        // Random seed.
        Random rand = new Random();
        // We will enter a semi-permanent while loop to let this perform to the best of its ability. The function
        // returns once a move has been successfully made.
        while (true)
        {
            // Get a random number between 1 and 8.
            randomRow = rand.nextInt(8) + 1;
            // Look in the alreadyChecked array to see if the randomly generated number's row has been checked. If not,
            // search the row.
            if (!alreadyChecked[randomRow - 1])
            {
                // Search the row to see if there are dice of the computer's in it.
                for (int i = 1; i < 10; i++)
                {
                    // If there is a die on the space of the playerType, attempt to make a random move.
                    if (board.isDieOn(randomRow, i) && board.isDiePlayerType(randomRow, i, playerType))
                    {
                        // Initialize possible moves with row and column traversal.
                        topNum = board.getDieTopNum(randomRow, i);
                        rowRolls = topNum;
                        columnRolls = 0;
                        while (rowRolls > 0)
                        {
                            // See if a move to the coordinates of the die plus/minus (to the left/right of) the row and column
                            // rolls needed to travel is possible. There will be four different ways to move.
                            if (canMoveToSpace(board, randomRow, i, randomRow + rowRolls, i + columnRolls, playerType))
                            {
                                // A move is possible. Return the coordinates of the possible move.
                                dieCoords[0] = randomRow;
                                dieCoords[1] = i;
                                dieCoords[2] = randomRow + rowRolls;
                                dieCoords[3] = i + columnRolls;
                                return dieCoords;
                            }
                            // Try randomRow - rowRolls.
                            if (canMoveToSpace(board, randomRow, i, randomRow - rowRolls, i + columnRolls, playerType))
                            {
                                // A move is possible. Return the coordinates of the possible move.
                                dieCoords[0] = randomRow;
                                dieCoords[1] = i;
                                dieCoords[2] = randomRow - rowRolls;
                                dieCoords[3] = i + columnRolls;
                                return dieCoords;
                            }
                            // Try i - columnRolls.
                            if (canMoveToSpace(board, randomRow, i, randomRow + rowRolls, i - columnRolls, playerType))
                            {
                                // A move is possible. Return the coordinates of the possible move.
                                dieCoords[0] = randomRow;
                                dieCoords[1] = i;
                                dieCoords[2] = randomRow + rowRolls;
                                dieCoords[3] = i - columnRolls;
                                return dieCoords;
                            }
                            // Try randomRow - rowRolls and i - columnRolls.
                            if (canMoveToSpace(board, randomRow, i, randomRow - rowRolls, i - columnRolls, playerType))
                            {
                                // A move is possible. Return the coordinates of the possible move.
                                dieCoords[0] = randomRow;
                                dieCoords[1] = i;
                                dieCoords[2] = randomRow - rowRolls;
                                dieCoords[3] = i - columnRolls;
                                return dieCoords;
                            }
                            // Otherwise, a move is not possible. Try different columns and rows to move by.
                            rowRolls--;
                            columnRolls++;
                        }
                    }
                }
                // Check off the row in the array, as it's now been checked.
                alreadyChecked[randomRow - 1] = true;
            }
        }
    }
}
