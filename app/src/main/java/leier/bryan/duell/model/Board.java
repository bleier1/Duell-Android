package leier.bryan.duell.model;

/**
 * Created by Bryan on 11/3/2016.
 */

public class Board {
    // A container (2D array) of Spaces that make up the board.
    private Space[][] boardModel = new Space[8][9];

    // GUI components... later.

    /**
     * Default constructor.
     */
    public Board() {
        // Initialize all the Spaces.
        for (int i = 8; i > 0; i--)
        {
            for (int j = 1; j < 10; j++)
            {
                boardModel[i-1][j-1] = new Space();
            }
        }
    }

    /**
     * Clears the board.
     */
    public void clearBoard() {
        for (int i = 8; i > 0; i--) {
            for (int j = 1; j < 10; j++) {
                boardModel[i - 1][j - 1].clearSpace();
            }
        }
    }

    /**
     * Sets up dice in their starting positions, as if a new round of Duell was just started.
     */
    public void newGameSetUp() {
        // Place all the human player's dice.
        boardModel[0][0].placeDie(new Die(5, 6, 'H'));
        boardModel[0][1].placeDie(new Die(1, 5, 'H'));
        boardModel[0][2].placeDie(new Die(2, 1, 'H'));
        boardModel[0][3].placeDie(new Die(6, 2, 'H'));
        boardModel[0][4].placeDie(new Die(1, 1, 'H'));
        boardModel[0][5].placeDie(new Die(6, 2, 'H'));
        boardModel[0][6].placeDie(new Die(2, 1, 'H'));
        boardModel[0][7].placeDie(new Die(1, 5, 'H'));
        boardModel[0][8].placeDie(new Die(5, 6, 'H'));
        // Place all the computer player's dice.
        boardModel[7][0].placeDie(new Die(5, 6, 'C'));
        boardModel[7][1].placeDie(new Die(1, 5, 'C'));
        boardModel[7][2].placeDie(new Die(2, 1, 'C'));
        boardModel[7][3].placeDie(new Die(6, 2, 'C'));
        boardModel[7][4].placeDie(new Die(1, 1, 'C'));
        boardModel[7][5].placeDie(new Die(6, 2, 'C'));
        boardModel[7][6].placeDie(new Die(2, 1, 'C'));
        boardModel[7][7].placeDie(new Die(1, 5, 'C'));
        boardModel[7][8].placeDie(new Die(5, 6, 'C'));
    }

    /**
     * Places a die on the given coordinates.
     *
     * @param die    the die that will be placed on the board
     * @param row    the row coordinate to place the die on
     * @param column the column coordinate to place the die on
     */
    public void placeDie(Die die, int row, int column) {
        boardModel[row - 1][column - 1].placeDie(die);
    }

    /**
     * To perform a roll in the specified direction to another space on the board.
     *
     * @param row       the row coordinate to place the die on
     * @param column    the column coordinate to place the die on
     * @param direction the direction to roll the die in
     * @return a boolean that determines if the roll is successful or not
     */
    public boolean performRoll(int row, int column, String direction) {
        if (direction.equals("up")) {
            boardModel[row - 1][column - 1].moveDie(boardModel[row][column - 1], "up");
            return true;
        }
        if (direction.equals("down")) {
            boardModel[row - 1][column - 1].moveDie(boardModel[row - 2][column - 1], "down");
            return true;
        }
        if (direction.equals("left")) {
            boardModel[row - 1][column - 1].moveDie(boardModel[row - 1][column - 2], "left");
            return true;
        }
        if (direction.equals("right")) {
            boardModel[row - 1][column - 1].moveDie(boardModel[row - 1][column], "right");
            return true;
        }
        return false;
    }

    /**
     * Determines if there is a die on the coordinates in the parameters
     *
     * @param row    the row coordinate to check if there is a die on
     * @param column the column coordinate to check if there is a die on
     * @return a boolean that determines if there is a die on the space given by the coordinates
     */
    public boolean isDieOn(int row, int column) {
        if (boardModel[row - 1][column - 1].isSpaceOccupied()) return true;
        return false;
    }

    /**
     * Determines if the die on the space is of the player type in the parameters or not
     *
     * @param row        the row coordinate to check if the die is of a player type
     * @param column     the column coordinate to check if the die is of a player type
     * @param playerType the player type to check if the die on the space is
     * @return a boolean that determines if the die on the space is of playerType or not
     */
    public boolean isDiePlayerType(int row, int column, char playerType) {
        if (boardModel[row - 1][column - 1].getDiePlayerType() == playerType) return true;
        return false;
    }

    /**
     * Determines if the die on the space is a key die
     *
     * @param row    the row coordinate to check if the die is a key die
     * @param column the column coordinate to check if the die is a key die
     * @return a boolean that determines if the die on the space is a key die or not
     */
    public boolean isKeyDie(int row, int column) {
        // Check if there is even a die on the space. If not, return false.
        if (!boardModel[row - 1][column - 1].isSpaceOccupied()) return false;
        // Get the top and right numbers of the die occupying the space.
        int topNum = boardModel[row - 1][column - 1].getDieTopNum();
        int rightNum = boardModel[row - 1][column - 1].getDieRightNum();
        // If they are both 1, this is a key die.
        if (topNum == 1 && rightNum == 1) return true;
        // Otherwise, it isn't.
        return false;
    }

    /**
     * To return the name of the die in the coordinates in the parameters
     * @param row    the row coordinate to get the die name of
     * @param column the column coordinate to get the die name of
     * @return the name of the die on the space
     */
    public String getDieName(int row, int column) {
        // Get the top and right numbers of the die on the space.
        int topNum = boardModel[row - 1][column - 1].getDieTopNum();
        int rightNum = 0;
        char playerType = boardModel[row - 1][column - 1].getDiePlayerType();
        // If the playerType is H (human), get the right number. Otherwise, it's the computer. Get the left number.
        if (playerType == 'H') rightNum = boardModel[row - 1][column - 1].getDieRightNum();
        else rightNum = boardModel[row - 1][column - 1].getDieLeftNum();
        // Convert to strings, concatenate together, and return the concatenated string.
        return Character.toString(playerType) + Integer.toString(topNum) + Integer.toString(rightNum);
    }

    /**
     * To get the top number of the die on the coordinates in the parameters
     * @param row the row coordinate to get the top number of
     * @param column the column coordinate to get the top number of
     * @return the top number of the die on the space
     */
    public int getDieTopNum(int row, int column)
    {
        return boardModel[row-1][column-1].getDieTopNum();
    }

    public static void main(String[] args) {
        Board model = new Board();
        model.newGameSetUp();
        System.out.println(model.getDieName(1, 5));
    }
}
