package leier.bryan.duell.model;

/**
 * Created by Bryan on 11/3/2016.
 */

public class Space {
    // The die currently occupying the space.
    private Die dieOnSpace;
    // Boolean value that determines whether or not a die is currently occupying the space.
    private boolean hasDie;

    // GUI components... later.

    /**
     * Default constructor.
     */
    public Space()
    {
        this.dieOnSpace = new Die();
        this.hasDie = false;
    }

    /**
     * Constructor for a space that lets a die be placed on it.
     * @param dieToPlace the die that will be placed on the space
     */
    public Space(Die dieToPlace)
    {
        this.dieOnSpace = dieToPlace;
        this.hasDie = true;
    }

    /**
     * Places a die on the space
     * @param dieToPlace the die that will be placed on the space
     */
    public void placeDie(Die dieToPlace)
    {
        dieOnSpace = dieToPlace;
        hasDie = true;
    }

    /**
     * Moves a die from one space to another
     * @param spaceToMoveTo the space that will receive the die currently on this space
     * @param direction the direction the die needs to roll in to reach the space in the parameters
     */
    public void moveDie(Space spaceToMoveTo, String direction)
    {
        // Adjust the die in the direction that it will roll.
        dieOnSpace.rollDie(direction);
        // Call placeDie() on spaceToMoveTo to transfer the die from "this" space to it.
        spaceToMoveTo.placeDie(dieOnSpace);
        // This space no longer has a die.
        hasDie = false;
    }

    /**
     * Determines if there is currently a die occupying the space
     * @return a boolean that determines if a die is on the space or not
     */
    public boolean isSpaceOccupied()
    {
        if (hasDie) return true;
        return false;
    }

    /**
     * "Clears" the space on the board
     */
    public void clearSpace()
    {
        hasDie = false;
    }

    /**
     * Gets the top number of the die on the space.
     * @return the top number of the die on the space
     */
    public int getDieTopNum()
    {
        return dieOnSpace.getTopNum();
    }

    /**
     * Gets the right number of the die on the space.
     * @return the right number of the die on the space
     */
    public int getDieRightNum()
    {
        return dieOnSpace.getRightNum();
    }

    /**
     * Gets the left number of the die on the space.
     * @return the left number of the die on the space
     */
    public int getDieLeftNum()
    {
        return dieOnSpace.getLeftNum();
    }

    /**
     * Gets the player type of the die on the space.
     * @return the player type of the die on the space
     */
    public char getDiePlayerType()
    {
        return dieOnSpace.getPlayerType();
    }
}
