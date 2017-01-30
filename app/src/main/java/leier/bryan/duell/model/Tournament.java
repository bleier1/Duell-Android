package leier.bryan.duell.model;

/**
 * Created by Bryan on 11/15/2016.
 */

public class Tournament {
    // Integers containing the amount of wins for each player.
    private int humanWins;
    private int computerWins;

    // GUI components... later.

    /**
     * Default constructor, which initializes the amount of times each player won.
     */
    public Tournament()
    {
        this.humanWins = 0;
        this.computerWins = 0;
    }

    /**
     * Adds a point for the human
     */
    public void addHumanPoint()
    {
        humanWins++;
    }

    /**
     * Adds a point for the computer
     */
    public void addComputerPoint()
    {
        computerWins++;
    }

    /**
     * Prints the amount of wins for each player to the terminal.
     */
    public void printWins()
    {
        // Print out how many times the computer has won the game.
        System.out.println("Computer Wins: " + computerWins);
        System.out.println();
        // Print out how many times the human has won the game.
        System.out.println("Human Wins: " + humanWins);
    }

    /**
     * Gets the amount of wins for the human
     */
    public int getHumanWins()
    {
        return humanWins;
    }

    /**
     * Gets the amount of wins for the computer
     */
    public int getComputerWins()
    {
        return computerWins;
    }
}
