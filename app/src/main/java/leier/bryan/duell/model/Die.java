package leier.bryan.duell.model;

/**
 * Created by Bryan on 11/2/2016.
 */

public class Die {

    // The numbers of each side of a die.
    private int topNum, playerFacingNum, bottomNum, awayFacingNum, leftNum, rightNum;
    // The player that controls the die.
    private char playerType;

    // GUI components... later.

    /**
     * Default constructor. Top number is assumed to be 1 and right number assumed to be 5.
     */
    public Die()
    {
        this.topNum = 1;
        this.rightNum = 5;
        this.playerFacingNum = 3;
        this.bottomNum = 7 - topNum;
        this.awayFacingNum = 7 - playerFacingNum;
        this.leftNum = 7 - rightNum;
        // Player type is assumed 'N', for no player.
        this.playerType = 'N';
    }

    /**
     * Constructor that allows specification for the top and right numbers on the die, in addition to the player who controls it.
     * @param topInput the top number on the die
     * @param rightInput the right number on the die
     * @param player the player who controls the die
     */
    public Die(int topInput, int rightInput, char player)
    {
        // Dice can be controlled by either a human or computer (H or C). It is not valid otherwise.
        if (player == 'H' || player == 'C') this.playerType = player;
        else this.playerType = 'N';

        // If both topInput and rightInput is 1, this is a key die. Each side needs to be 1.
        if (topInput == 1 && rightInput == 1)
        {
            this.topNum = topInput;
            this.rightNum = rightInput;
            this.bottomNum = 1;
            this.leftNum = 1;
            this.playerFacingNum = 1;
            this.awayFacingNum = 1;
            return;
        }

        // Otherwise, initialize each side to the same numbers as the defaul constructor.
        this.topNum = 1;
        this.rightNum = 5;
        this.playerFacingNum = 3;
        this.bottomNum = 7 - topNum;
        this.leftNum = 7 - rightNum;
        this.awayFacingNum = 7 - playerFacingNum;

        // Now call the rotate functions so each side matches up with the parameters in the constructor.
        // First check to see if the sides are valid. If they are not, then return.
        if ((topInput < 1 || topInput > 6) || (rightInput < 1 || rightInput > 6))
            return;
        // Otherwise, they are! Call the functions.
        else
        {
            this.rotateToTopNum(topInput);
            this.rotateToRightNum(rightInput);
            // If this is a Computer die then the die needs to be rotated left twice so that the point of view
            // of the computer player has 3 facing it.
            if (playerType == 'C')
            {
                this.rotateLeft();
                this.rotateLeft();
            }
        }
    }

    /**
     * Rolls the die in the direction specified.
     * @param direction the direction that the die will move in
     */
    public void rollDie(String direction)
    {
        if (direction.equals("up"))
            this.moveUp();
        if (direction.equals("down"))
            this.moveDown();
        if (direction.equals("left"))
            this.moveLeft();
        if (direction.equals("right"))
            this.moveRight();
    }

    /**
     * Get the number on the top of the die.
     * @return topNum of the Die
     */
    public int getTopNum() {
        return topNum;
    }

    /**
     * Get the number on the right of the die.
     * @return rightNum of the Die
     */
    public int getRightNum() {
        return rightNum;
    }

    /**
     * Get the number on the left of the die.
     * @return leftNum of the Die
     */
    public int getLeftNum() {
        return leftNum;
    }

    /**
     * Get the player who controls the die.
     * @return playerType of the Die
     */
    public char getPlayerType() {
        return playerType;
    }

    // main for testing
    public static void main(String[] args)
    {
        Die test = new Die(5, 6, 'H');
    }

    /**
     * "Rolls" the die upwards and updates the sides of the die appropriately.
     */
    private void moveUp()
    {
        // Initialize a temporary int to store the value of a number on the die.
        int temp = topNum;
        topNum = playerFacingNum;
        playerFacingNum = bottomNum;
        bottomNum = awayFacingNum;
        awayFacingNum = temp;
    }

    /**
     * "Rolls" the die downwards and updates the sides of the die appropriately.
     */
    private void moveDown()
    {
        // Initialize a temporary int to store the value of a number on the die.
        int temp = topNum;
        topNum = awayFacingNum;
        awayFacingNum = bottomNum;
        bottomNum = playerFacingNum;
        playerFacingNum = temp;
    }

    /**
     * "Rolls" the die to the left and updates the sides of the die appropriately.
     */
    private void moveLeft()
    {
        // Initialize a temporary int to store the value of a number on the die.
        int temp = topNum;
        topNum = rightNum;
        rightNum = bottomNum;
        bottomNum = leftNum;
        leftNum = temp;
    }

    /**
     * "Rolls" the die to the right and updates the sides of the die appropriately.
     */
    private void moveRight()
    {
        // Initialize a temporary int to store the value of a number on the die.
        int temp = topNum;
        topNum = leftNum;
        leftNum = bottomNum;
        bottomNum = rightNum;
        rightNum = temp;
    }

    /**
     * "Rotates" the die to the left and updates each side appropriately.
     */
    private void rotateLeft()
    {
        // Initialize a temporary int to store the value of a number on the die.
        int temp = playerFacingNum;
        playerFacingNum = leftNum;
        leftNum = awayFacingNum;
        awayFacingNum = rightNum;
        rightNum = temp;
    }

    /**
     * "Rotates" the die to so the number in the parameter is on the top of the die.
     * @param topInput the number that will end up on the top of the die
     */
    private void rotateToTopNum(int topInput)
    {
        // If topInput is 1, nothing needs to be done.
        if (topInput == 1) return;
        // If topInput is 2, it needs to be moved to the right.
        if (topInput == 2)
        {
            this.moveRight();
            return;
        }
        // If topInput is 3, it needs to be moved upwards.
        if (topInput == 3)
        {
            this.moveUp();
            return;
        }
        // If topInput is 4, it needs to be moved downwards.
        if (topInput == 4)
        {
            this.moveDown();
            return;
        }
        // If topInput is 5, it needs to be moved to the left.
        if (topInput == 5)
        {
            this.moveLeft();
            return;
        }
        // If topInput is 6, it needs to be moved upwards twice.
        if (topInput == 6)
        {
            this.moveUp();
            this.moveUp();
        }
    }

    /**
     * "Rotates" the die so that the number in the parameter ends up on the right side of the die.
     * @param rightInput the number that will end up on the right of the die
     */
    private void rotateToRightNum(int rightInput)
    {
        // Rotate until rightNum equals rightInput.
        while (rightNum != rightInput)
        {
            this.rotateLeft();
        }
    }
}
