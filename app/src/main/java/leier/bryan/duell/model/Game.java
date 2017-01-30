package leier.bryan.duell.model;

import android.os.Environment;
import android.support.v4.app.ActivityCompat;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

import leier.bryan.duell.view.StartGameActivity;

/**
 * Created by Bryan on 11/15/2016.
 */

public class Game {
    // The tournament for the game.
    private Tournament gameTournament;
    // The board the game will be played on.
    private Board gameBoard;
    // The current player.
    private String currentPlayer;
    // A Human player.
    private Human humanPlayer;
    // A Computer player.
    private Computer cpuPlayer;

    // GUI components... later.

    /**
     * Default constructor.
     */
    public Game()
    {
        gameTournament = new Tournament();
        gameBoard = new Board();
        humanPlayer = new Human();
        cpuPlayer = new Computer();
        currentPlayer = "";
    }

    /**
     * Sets up a new game of Duell
     */
    public void setUpGame()
    {
        // Clear the dice on the board.
        gameBoard.clearBoard();
        // Set up the dice on the board.
        gameBoard.newGameSetUp();
    }

    /**
     * resumes a saved game from a text file
     * @param filename the name of the file containing the game to restore
     * @return a boolean determining whether or not restoration was a success
     */
    public boolean resumeGame(String filename)
    {
        // First check to see if the storage is available to read from.
        if (!isExternalStorageReadable())
        {
            return false;
        }
        // Fix up the name of the file.
        String fixedName = "/"+filename+".txt";
        // The file to read from.
        File savedGame = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), fixedName);
        // A string that references one line at a time.
        String line = "";

        // Start reading from the file.
        try
        {
            // File reader reads text, which is what we want to do when resuming games.
            FileReader fileReader = new FileReader(savedGame);
            // Wrap FileReader in BufferedReader.
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            // Start reading the lines in the file:
            line = bufferedReader.readLine();
            // The very first line should be "Board:", if not, it is invalid and cannot be resumed.
            if (!line.equals("Board:"))
            {
                bufferedReader.close();
                return false;
            }

            // Continue reading from the file. The next 8 lines should be spaces on the board.
            for (int i = 8; i > 0; i--)
            {
                line = bufferedReader.readLine();
                // If the board is unable to be restored, return false for an error.
                if (!restoreBoard(line, i)) return false;
            }

            // Get the next couple lines. Skip one because it's blank.
            line = bufferedReader.readLine();
            line = bufferedReader.readLine();
            // Restore the amount of wins for the computer player.
            if (!restorePlayerWins(line)) return false;
            // Get the next couple lines. Skip one because it's blank.
            line = bufferedReader.readLine();
            line = bufferedReader.readLine();
            // Restore the amount of wins for the human player.
            if (!restorePlayerWins(line)) return false;
            // Get the next couple lines. Skip one because it's blank.
            line = bufferedReader.readLine();
            line = bufferedReader.readLine();
            // Assign the next player to be the current player.
            if (!restorePlayer(line)) return false;
            // Seems like restoration was a success! Close the file.
            bufferedReader.close();
        }
        catch(FileNotFoundException exception)
        {
            // The file was not found.
            return false;
        }
        catch(IOException exception)
        {
            // There was an error reading a line in the file.
            return false;
        }

        return true;
    }

    /**
     * saves a game to be restored for later
     * @param filename the name of the text file containing the game that was saved
     * @return a boolean determining whether or not saving the file was a success
     */
    public boolean saveFile(String filename)
    {
        // First check to see if the storage is available to read from.
        if (!isExternalStorageWritable())
        {
            return false;
        }
        // Fix up the name of the file.
        String fixedName = "/"+filename+".txt";
        // The file to read from.
        File output = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), fixedName);

        try
        {
            // Need to write to a file:
            FileWriter fileWriter = new FileWriter(output);
            // Wrap it in a BufferedWriter:
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            // Start writing the proper format for file output:
            bufferedWriter.write("Board:");
            bufferedWriter.newLine();
            bufferedWriter.write(" \t");
            // Scan the board for dice:
            for (int i = 8; i > 0; i--)
            {
                for (int j = 1; j < 10; j++)
                {
                    // If there is a die on the space, output its name to the text file.
                    if (gameBoard.isDieOn(i, j))
                    {
                        bufferedWriter.write(gameBoard.getDieName(i, j)+" \t");
                    }
                    // Otherwise, it's an empty space.
                    else
                    {
                        bufferedWriter.write("0 \t");
                    }
                }
                bufferedWriter.newLine();
                bufferedWriter.write("\t");
            }

            // Output the number of wins the computer and human have:
            bufferedWriter.newLine();
            bufferedWriter.write("Computer Wins: "+gameTournament.getComputerWins());
            bufferedWriter.newLine();
            bufferedWriter.newLine();
            bufferedWriter.write("Human Wins: "+gameTournament.getHumanWins());
            // Get the next player.
            bufferedWriter.newLine();
            bufferedWriter.newLine();
            bufferedWriter.write("Next Player: "+getCurrentPlayer());
            // We're done! Close the file.
            bufferedWriter.close();
        }
        catch(IOException exception)
        {
            return false;
        }

        return true;
    }

    /**
     * Determines the current play in the game
     * @return the name of the current player
     */
    public String getCurrentPlayer()
    {
        return currentPlayer;
    }

    /**
     * Gets the name of the die on the board's space
     * @param row the row coordinate to check
     * @param column the column coordinate to check
     * @return a string that signifies the die currently occupying the space
     */
    public String getDieNameOnSpace(int row, int column)
    {
        // Check if there is a die on the space. If it is not, return an empty string.
        if (!gameBoard.isDieOn(row, column)) return "";
        else return gameBoard.getDieName(row, column);
    }

    /**
     * Gets the amount of wins that the computer has in the tournament
     * @return the amount of computer wins
     */
    public int getComputerWins()
    {
        return gameTournament.getComputerWins();
    }

    /**
     * Gets the amount of wins that the human has in the tournament
     * @return the amount of human wins
     */
    public int getHumanWins()
    {
        return gameTournament.getHumanWins();
    }

    /**
     * performs the computer's turn
     */
    public String doComputerTurn()
    {
        return cpuPlayer.play(gameBoard);
    }

    /**
     * performs the human's turn
     * @param dieRow the row of the die to move
     * @param dieColumn the column of the die to move
     * @param spaceRow the row of the space to move to
     * @param spaceColumn the column of the space to move to
     * @param direction the direction to first move in
     */
    public void doHumanTurn(int dieRow, int dieColumn, int spaceRow, int spaceColumn, String direction)
    {
        humanPlayer.play(gameBoard, dieRow, dieColumn, spaceRow, spaceColumn, direction);
    }

    /**
     * Determines if there is a win condition that is met in the game
     * @return a number that corresponds to who won and what type of victory it is
     */
    public int checkWinCondition()
    {
        // First, check the key spaces. If the die playerType does not match the space of the player it should belong to,
        // the player with their die on that space wins the game.

        // Check the player's key space.
        if (gameBoard.isDieOn(1, 5))
        {
            // If the die on it is not a human type, the human loses and the game is over.
            if (gameBoard.isDiePlayerType(1, 5, 'C')) return 2;
        }
        // Now check the computer's key space.
        if (gameBoard.isDieOn(8, 5))
        {
            // If the die on it is not a computer type, the computer loses and the game is over.
            if (gameBoard.isDiePlayerType(8, 5, 'H')) return 1;
        }

        // Now look for the key dies for each player.
        int keyDieResult = isKeyDieOnBoard();
        // If the result is 1, the computer's key die is captured and the computer loses the game.
        if (keyDieResult == 1) return 3;
        // If the result is 2, the human's key die is captured and the human loses the game.
        if (keyDieResult == 2) return 4;
        // Otherwise, 0 was found, which means both key dice are on the board and the key spaces are not occupied. Nobody
        // has won yet.
        return 0;
    }

    /**
     * Determines if a die is on the board's space
     * @param row the row to check on the board
     * @param column the column to check on the board
     * @return a boolean determining if a die is on the space or not
     */
    public boolean isDieOnSpace(int row, int column)
    {
        if (gameBoard.isDieOn(row, column)) return true;
        return false;
    }

    /**
     * switches the players so they can take turns
     */
    public void switchPlayers()
    {
        if (currentPlayer.equals("Human")) currentPlayer = "Computer";
        else currentPlayer = "Human";
    }

    /**
     * checks if the spaces that the player clicked on the board makes for a valid move
     * @param dieRow the row of the die the player wants to move
     * @param dieColumn the column of the die the player wants to move
     * @param spaceRow the row of the space the player wants to move to
     * @param spaceColumn the column of the space the player wants to move to
     * @return a boolean signifying whether or not the move is possible
     */
    public boolean humanCanMoveToSpace(int dieRow, int dieColumn, int spaceRow, int spaceColumn)
    {
        if (humanPlayer.canMakeMove(gameBoard, dieRow, dieColumn, spaceRow, spaceColumn)) return true;
        else return false;
    }

    /**
     * determines who goes first in a new game
     * @return an array containing the results of the die throws
     */
    public int[] determineFirstMove()
    {
        // The results of each player's die toss.
        int[] results = new int[2];
        // Random seed.
        Random rand = new Random();
        // The die toss result of the first player.
        int player1DieToss = rand.nextInt(6) + 1;
        // The die toss result of the second player.
        int player2DieToss = rand.nextInt(6) + 1;
        // If it's the same roll, return the results anyway. This function will be called again if this is the case.
        if (player1DieToss == player2DieToss)
        {
            results[0] = player1DieToss;
            results[1] = player2DieToss;
            return results;
        }
        // Otherwise, determine who threw higher.
        if (player1DieToss > player2DieToss)
        {
            // the human goes first
            currentPlayer = "Human";
        }
        else
        {
            // the computer goes first
            currentPlayer = "Computer";
        }
        // Return the results.
        results[0] = player1DieToss;
        results[1] = player2DieToss;
        return results;
    }

    /**
     * determines which direction the human should move in when making their move
     * @param dieRow the row of the die to move
     * @param dieColumn the column of the die to move
     * @param spaceRow the row of the space to move to
     * @param spaceColumn the column of the space to move to
     * @return 0 if both directions are possible, 1 if frontal only, 2 if lateral only
     */
    public int determineHumanDirection(int dieRow, int dieColumn, int spaceRow, int spaceColumn)
    {
        return humanPlayer.determineDirection(gameBoard, dieRow, dieColumn, spaceRow, spaceColumn);
    }

    /**
     * gets help from the computer on a move to make
     * @return a string containing the recommendation
     */
    public String getHelp()
    {
        return humanPlayer.getHelp(gameBoard);
    }

    /**
     * registers the winner of the game into the tournament
     * @param winner an integer signifying who won the game
     */
    public void registerWinner(int winner)
    {
        if (winner == 1) gameTournament.addHumanPoint();
        else gameTournament.addComputerPoint();
    }

    public static void main(String[] args)
    {
        Game test = new Game();
        test.setUpGame();
        test.doComputerTurn();
    }

    /**
     * determines if a key die is on the board or not
     * @return a number that corresponds to whose key die is missing or if none are missing at all
     */
    private int isKeyDieOnBoard()
    {
        // Initialize boolean values for the key dies of each player and whether or not they're on the board.
        boolean humanKeyDie = false;
        boolean computerKeyDie = false;
        // Search the board for the key die. Use a for loop to iterate through the board.
        for (int i = 8; i > 0; i--)
        {
            for (int j = 1; j < 10; j++)
            {
                // If there is a key die on the space, check the playerType.
                if (gameBoard.isKeyDie(i, j))
                {
                    // If it is a human type, humanKeyDie is true.
                    if (gameBoard.isDiePlayerType(i, j, 'H')) humanKeyDie = true;
                    // If it is a computer type, computerKeyDie is true.
                    if (gameBoard.isDiePlayerType(i, j, 'C')) computerKeyDie = true;
                }
            }
        }
        // If the human's key die is present but not the computer's, return 1.
        if (humanKeyDie && !computerKeyDie) return 1;
        // If the computer's key die is present but not the human's, return 2.
        if (!humanKeyDie && computerKeyDie) return 2;

        // Otherwise, both are true. Return 0.
        return 0;
    }

    /**
     * determines if external storage is writable
     * @return a boolean determining if we can write to the device
     */
    private boolean isExternalStorageWritable()
    {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) return true;
        return false;
    }

    /**
     * determines if external storage is readable
     * @return a boolean determining if we can read from the device
     */
    private boolean isExternalStorageReadable()
    {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) return true;
        return false;
    }

    /**
     * restores the board model to what is represented in the text file
     * @param line the line that contains the spaces
     * @param row the row to be stored
     * @return a boolean determining whether or not the restoration was a success
     */
    private boolean restoreBoard(String line, int row)
    {
        // Set up an array of strings that will help to parse the line.
        String[] dice = line.split("\\s+");
        // Integers that hold the top and right number of the die.
        int topNum;
        int rightNum;
        // Go through the array:
        for (int i = 1; i < 10; i++)
        {
            // If it is a die name, initialize the die on the board:
            if (dice[i].charAt(0) == 'H' || dice[i].charAt(0) == 'C')
            {
                // Get the top and right numbers of the die.
                topNum = Character.getNumericValue(dice[i].charAt(1));
                rightNum = Character.getNumericValue(dice[i].charAt(2));
                // Place the die on the board.
                gameBoard.placeDie(new Die(topNum, rightNum, dice[i].charAt(0)), row, i);
            }
            // If it is a 0, it is an empty space. Leave it be.
            else if (dice[i].equals("0")) {}
            // Otherwise, it is not recognized by the board. Return false for an error.
            else
            {
                return false;
            }
        }
        // Everything seems to have gone well, return true.
        return true;
    }

    /**
     * restores the amount of wins the player has in the game
     * @param line the line containing the player and their win count
     * @return a boolean determining whether or not restoration was a success
     */
    private boolean restorePlayerWins(String line)
    {
        // Set up an array of strings that will help to parse the line.
        String delims = "[ ]+";
        String[] winLine = line.split(delims);
        // String that stores the player's name.
        String playerName = "";
        // Integer that stores how many times the player has won.
        int winCount;

        // If the player's name is "Human" or "Computer", store it in the name. Otherwise, return an error.
        if (winLine[0].equals("Human") || winLine[0].equals("Computer"))
        {
            playerName = winLine[0];
        }
        else
        {
            return false;
        }
        // Store the amount of times the player has won into winCount.
        winCount = Integer.parseInt(winLine[2]);
        // Restore the amount of wins for that player:
        for (int i = 0; i < winCount; i++)
        {
            if (playerName.equals("Human")) gameTournament.addHumanPoint();
            else gameTournament.addComputerPoint();
        }

        // Everything seems to have went well. Return true.
        return true;
    }

    /**
     * restores the player who will take their turn next in the game
     * @param line the line containing the next player
     * @return a boolean determining whether restoration was successful or not
     */
    private boolean restorePlayer(String line)
    {
        // Set up an array of strings that will help to parse the line.
        String delims = "[ ]+";
        String[] playerLine = line.split(delims);

        // If the third element in playerLine is not "Human" or "Computer", the file is invalid.
        if (playerLine[2].equals("Human"))
        {
            // Assign currentPlayer to human.
            currentPlayer = "Human";
        }
        else if (playerLine[2].equals("Computer"))
        {
            // Assign currentPlayer to computer.
            currentPlayer = "Computer";
        }
        else
        {
            return false;
        }

        // Everything went well! Return true.
        return true;
    }
}
