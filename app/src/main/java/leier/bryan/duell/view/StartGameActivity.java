package leier.bryan.duell.view;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

import leier.bryan.duell.R;
import leier.bryan.duell.model.Game;
import leier.bryan.duell.view.MainActivity;

public class StartGameActivity extends AppCompatActivity {

    // The game to be played.
    private Game duellGame = new Game();
    // A boolean that determines if the player has clicked on a die to move.
    boolean clickedDie = false;
    // An array containing the coordinates of spaces to change on the board for the human.
    int[] humanMoves = new int[4];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Remove the title bar. It doesn't look that nice!
        this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_game);

        // Check if the user started a new game.
        Intent mainIntent = getIntent();
        boolean newGame = mainIntent.getExtras().getBoolean(MainActivity.EXTRA_NEWGAME);
        // If true, start a new game by determining who goes first.
        if (newGame) {
            duellGame.setUpGame();
            determineFirstMove();
        }
        else
        {
            askForFilename();
        }
    }

    /**
     * lets the player make the move they want to make on the board
     * @param view the space that the player clicks on
     */
    public void makeMove(View view)
    {
        // First check to see if clickedDie is false. If so, this is the die the player wants to move.
        if (!clickedDie)
        {
            // Get the coordinates.
            int[] startCoords = getSpaceCoordinates(view);
            // Before doing anything, perform a check to see if a move from this space is even possible for the human.
            // Get the name of the die on them.
            String nameCheck = duellGame.getDieNameOnSpace(startCoords[0], startCoords[1]);
            // If the first character is not H, then don't bother doing anything. The player can't do anything on this space.
            if (nameCheck.isEmpty() || nameCheck.charAt(0) == 'C')
            {
                Toast.makeText(getApplicationContext(), "You cannot move from this space.", Toast.LENGTH_SHORT).show();
                return;
            }
            // Set clickedDie to true.
            clickedDie = true;
            humanMoves[0] = startCoords[0];
            humanMoves[1] = startCoords[1];

            // Highlight the space to indicate it is being selected.
            view.setBackgroundResource(R.drawable.highlightedspace);
            // Highlight the spaces that this die can move to.
            highlightPossibleMoves();
        }
        // If clickedDie is true, the player either wants to stop moving this die or is choosing a space to move it to.
        else
        {
            // Check if the player is clicking on the same die to stop making their move selection.
            int[] endCoords = getSpaceCoordinates(view);
            if (endCoords[0] == humanMoves[0] && endCoords[1] == humanMoves[1])
            {
                // it's the same space, deselect it and remove it from the array
                humanMoves[0] = 0;
                humanMoves[1] = 0;
                // unhighlight all spaces
                unhighlightAll();
                // set clickedDie to false
                clickedDie = false;
            }
            else
            {
                // they're different coordinates, but before recording these as the end coordinates, check if a move can be
                // made to them:
                if (duellGame.humanCanMoveToSpace(humanMoves[0], humanMoves[1], endCoords[0], endCoords[1]))
                {
                    // a move is possible, record these coordinates
                    humanMoves[2] = endCoords[0];
                    humanMoves[3] = endCoords[1];
                    // make a move
                    humanMakesMove();
                }
                else
                {
                    // the die cannot be moved to this space, report as such
                    Toast.makeText(getApplicationContext(), "The die cannot be moved to this space.", Toast.LENGTH_SHORT).show();
                    humanMoves[2] = 0;
                    humanMoves[3] = 0;
                }
            }
        }
    }

    /**
     * gets a recommendation from the computer when requested (button is clicked)
     */
    public void getHelp(View view)
    {
        String recommendation = duellGame.getHelp();
        // Build a dialog box that contains the recommendation.
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle("Recommendation");
        dialogBuilder.setMessage(recommendation);
        dialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // nothing
            }
        });
        AlertDialog recommendationBox = dialogBuilder.create();
        recommendationBox.show();
    }

    /**
     * asks for the name of the file to restore
     */
    private void askForFilename()
    {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        final EditText fileInput = new EditText(StartGameActivity.this);
        dialogBuilder.setTitle("Filename");
        dialogBuilder.setMessage("Please enter the name of the text file you want to restore (do not include .txt).");
        dialogBuilder.setView(fileInput);
        dialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener(){
           @Override
            public void onClick(DialogInterface dialog, int which)
           {
               String editValue = fileInput.getText().toString();
               loadGame(editValue);
           }
        });
        AlertDialog inputBox = dialogBuilder.create();
        inputBox.setCanceledOnTouchOutside(false);
        inputBox.show();
    }

    /**
     * loads the game from the saved text file
     * @param filename the name of the text file
     */
    private void loadGame(String filename)
    {
        if (duellGame.resumeGame(filename))
        {
            // update the display
            updateDisplay();
            if (duellGame.getCurrentPlayer().equals("Computer")) computerMakesMove();
        }
        else
        {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
            dialogBuilder.setTitle("Error");
            dialogBuilder.setMessage("The file was unable to be read. The application will now exit.");
            dialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener(){
               @Override
                public void onClick(DialogInterface dialog, int which)
               {
                   // exit the program
                   finishAffinity();
                   Intent intent = new Intent(Intent.ACTION_MAIN);
                   intent.addCategory(Intent.CATEGORY_HOME);
                   intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                   startActivity(intent);
               }
            });
            AlertDialog errorMessage = dialogBuilder.create();
            errorMessage.setCanceledOnTouchOutside(false);
            errorMessage.show();
        }
    }

    /**
     * lets the human make their move on the board
     */
    private void humanMakesMove()
    {
        // decide the direction
        decideDirection(humanMoves[0], humanMoves[1], humanMoves[2], humanMoves[3]);
    }

    private void computerMakesMove()
    {
        String moveMade = duellGame.doComputerTurn();
        // update the board
        updateDisplay();
        // display the move just made
        printComputerMove(moveMade);
    }

    /**
     * Gets the coordinates of the space that was clicked on
     * @param view the view to get the id of
     * @return an array containing the coordinates of the space that was clicked on
     */
    private int[] getSpaceCoordinates(View view)
    {
        // Initialize the array:
        int[] coords = new int[2];
        // Get the name of the ID:
        String idName = getResources().getResourceEntryName(view.getId());
        // Get the row and column:
        int spaceCoordinates = Integer.parseInt(idName.substring(idName.length() - 2));
        int row = spaceCoordinates / 10;
        int column = spaceCoordinates % 10;
        coords[0] = row;
        coords[1] = column;
        return coords;
    }

    /**
     * Determines who goes first in the game
     */
    private void determineFirstMove()
    {
        // Start building a dialog box that will tell the player who goes first:
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle("Who Goes First?");
        dialogBuilder.setMessage("The round will begin with a die toss. The player with the highest number goes first!");
        dialogBuilder.setPositiveButton("Let's play!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Determine the first player:
                int[] results = duellGame.determineFirstMove();
                while (results[0] == results[1])
                {
                    Toast.makeText(getApplicationContext(), "Both players got " + Integer.toString(results[0]) + ". Rolling again...", Toast.LENGTH_SHORT).show();
                    results = duellGame.determineFirstMove();
                }
                // Display the numbers of each player.
                String result = "You rolled a "+Integer.toString(results[0])+" and the Computer rolled a "+Integer.toString(results[1])+".";
                if (results[0] > results[1])
                {
                    result += " You go first!";
                }
                else
                {
                    result += " Computer goes first!";
                }
                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
                updateDisplay();
                // computer needs to make their move first if they won the die toss
                if (duellGame.getCurrentPlayer().equals("Computer"))
                {
                    computerMakesMove();
                }
            }
        });
        AlertDialog gameStartDialog = dialogBuilder.create();
        gameStartDialog.setCanceledOnTouchOutside(false);
        gameStartDialog.show();
    }

    /**
     * updates the board display
     */
    private void updateDisplay()
    {
        // update the board display
        for (int i = 8; i > 0; i--)
        {
            for (int j = 1; j < 10; j++)
            {
                // Convert i and j to integers and append to "space"
                int viewID = getResources().getIdentifier("space"+Integer.toString(i)+Integer.toString(j), "id", getPackageName());
                TextView text = (TextView) findViewById(viewID);
                text.setText(duellGame.getDieNameOnSpace(i, j));
            }
        }

        // update the amount of wins for each player
        TextView text = (TextView) findViewById(R.id.compWins);
        String compWinText = "Computer Wins: " + Integer.toString(duellGame.getComputerWins());
        text.setText(compWinText);
        text = (TextView) findViewById(R.id.humanWins);
        String humanWinText = "Human Wins: " + Integer.toString(duellGame.getHumanWins());
        text.setText(humanWinText);

        // update the current player
        text = (TextView) findViewById(R.id.currentPlayer);
        String currentText = "Next Player: " + duellGame.getCurrentPlayer();
        text.setText(currentText);
    }

    /**
     * highlights all the squares that the die selected can move to
     */
    private void highlightPossibleMoves()
    {
        for (int i = 8; i > 0; i--)
        {
            for (int j = 1; j < 10; j++)
            {
                // Check if a move is possible to this space:
                if (duellGame.humanCanMoveToSpace(humanMoves[0], humanMoves[1], i, j))
                {
                    // highlight this square
                    // Convert i and j to integers and append to "space"
                    int viewID = getResources().getIdentifier("space"+Integer.toString(i)+Integer.toString(j), "id", getPackageName());
                    TextView text = (TextView) findViewById(viewID);
                    text.setBackgroundResource(R.drawable.highlightedspace);
                }
                // otherwise, don't do anything
            }
        }
    }

    /**
     * unhighlights all the spaces on the board
     */
    private void unhighlightAll()
    {
        for (int i = 8; i > 0; i--)
        {
            for (int j = 1; j < 10; j++)
            {
                // Convert i and j to integers and append to "space"
                int viewID = getResources().getIdentifier("space"+Integer.toString(i)+Integer.toString(j), "id", getPackageName());
                TextView text = (TextView) findViewById(viewID);
                text.setBackgroundResource(R.drawable.boardspace);
            }
        }
    }

    /**
     * decides which direction the player wants to move in first and then continues the flow of the game
     * @param dieRow row of die to move
     * @param dieColumn column of die to move
     * @param spaceRow row of space to move to
     * @param spaceColumn column of space to move to
     */
    private void decideDirection(final int dieRow, final int dieColumn, final int spaceRow, final int spaceColumn)
    {
        // Start building a dialog box for the player to determine which direction to move in:
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle("Direction");
        dialogBuilder.setMessage("Which direction would you like the die to first move in?");
        dialogBuilder.setPositiveButton("Frontally", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (duellGame.determineHumanDirection(dieRow, dieColumn, spaceRow, spaceColumn) == 1)
                {
                    // only a lateral move is possible
                    Toast.makeText(getApplicationContext(), "You cannot roll in that direction.", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    duellGame.doHumanTurn(dieRow, dieColumn, spaceRow, spaceColumn, "frontally");
                    updateDisplay();
                    // clean up
                    humanMoves[0] = 0;
                    humanMoves[1] = 0;
                    humanMoves[2] = 0;
                    humanMoves[3] = 0;
                    clickedDie = false;
                    unhighlightAll();
                    // don't need to print the move for the human... it's quite apparent that they know where they moved,
                    // given the highlights on the board
                    // check for win condition
                    int winCondition = duellGame.checkWinCondition();
                    if (winCondition > 0)
                    {
                        // there is a win condition on the board, determine the winner
                        determineWinner(winCondition);
                    }
                    // otherwise, ask to save the game
                    else
                    {
                        // switch players
                        duellGame.switchPlayers();
                        // update display
                        updateDisplay();
                        // ask to save
                        askToSave();
                    }
                }
            }
        });
        dialogBuilder.setNegativeButton("Laterally", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (duellGame.determineHumanDirection(dieRow, dieColumn, spaceRow, spaceColumn) == 2)
                {
                    // only a frontal move is possible
                    Toast.makeText(getApplicationContext(), "You cannot roll in that direction.", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    duellGame.doHumanTurn(dieRow, dieColumn, spaceRow, spaceColumn, "laterally");
                    updateDisplay();
                    // clean up
                    humanMoves[0] = 0;
                    humanMoves[1] = 0;
                    humanMoves[2] = 0;
                    humanMoves[3] = 0;
                    clickedDie = false;
                    unhighlightAll();
                    // don't need to print the move for the human... it's quite apparent that they know where they moved,
                    // given the highlights on the board
                    // check for win condition
                    int winCondition = duellGame.checkWinCondition();
                    if (winCondition > 0)
                    {
                        // there is a win condition on the board, determine the winner
                        determineWinner(winCondition);
                    }
                    // otherwise, ask to save the game
                    else
                    {
                        // switch players
                        duellGame.switchPlayers();
                        // update display
                        updateDisplay();
                        // ask to save
                        askToSave();
                    }
                }
            }
        });
        AlertDialog directionDialog = dialogBuilder.create();
        directionDialog.setCanceledOnTouchOutside(false);
        directionDialog.show();
    }

    private void printComputerMove(String moveMade)
    {
        // Start building a dialog box for the player to determine which direction to move in:
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle("Computer");
        dialogBuilder.setMessage(moveMade);
        dialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                // check for win condition
                int winCondition = duellGame.checkWinCondition();
                if (winCondition > 0)
                {
                    // there is a win condition on the board, determine the winner
                    determineWinner(winCondition);
                }
                // otherwise, ask to save the game
                else
                {
                    // switch players
                    duellGame.switchPlayers();
                    // update the display
                    updateDisplay();
                    // ask to save
                    askToSave();
                }
            }
        });
        AlertDialog computerMove = dialogBuilder.create();
        computerMove.setCanceledOnTouchOutside(false);
        computerMove.show();
    }

    /**
     * determines the winner of the game
     * @param winScore the score returned by the Game's checkWinCondition function
     */
    private void determineWinner(final int winScore)
    {
        // The message that will be printed to the screen.
        String winMessage;
        if (winScore == 1)
        {
            winMessage = "The human has landed on the computer's key space. The human wins!";
        }
        else if (winScore == 2)
        {
            winMessage = "The computer has landed on the human's key space. The computer wins!";
        }
        else if (winScore == 3)
        {
            winMessage = "The human has captured the computer's key die. The human wins!";
        }
        else if (winScore == 4)
        {
            winMessage = "The computer has captured the human's key die. The computer wins!";
        }
        else
        {
            winMessage = "";
        }
        // build a box to display the winner
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle("Game Over");
        dialogBuilder.setMessage(winMessage);
        dialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                // add to the score and ask to play again
                if (winScore == 1 || winScore == 3)
                {
                    tallyAndPlayAgain(1);
                }
                if (winScore == 2 || winScore == 4)
                {
                    tallyAndPlayAgain(2);
                }
            }
        });
        AlertDialog winner = dialogBuilder.create();
        winner.setCanceledOnTouchOutside(false);
        winner.show();
    }

    /**
     * records the winner of the game and asks the player if they want to play another game
     * @param winner the winner of the game
     */
    private void tallyAndPlayAgain(int winner)
    {
        // register the winner
        duellGame.registerWinner(winner);
        // ask to play again
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle("Game Over");
        dialogBuilder.setMessage("Would you like to play another game?");
        dialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                // start a new activity
                Intent intent = new Intent(getApplicationContext(), winnerActivity.class);
                intent.putExtra("humanWins", duellGame.getHumanWins());
                intent.putExtra("computerWins", duellGame.getComputerWins());
                startActivity(intent);
            }
        });
        dialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                // start a new game
                duellGame.setUpGame();
                determineFirstMove();
            }
        });
        AlertDialog playAgain = dialogBuilder.create();
        playAgain.setCanceledOnTouchOutside(false);
        playAgain.show();
    }

    private void askToSave()
    {
        // build a box to ask for a save
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle("Save Game");
        dialogBuilder.setMessage("Would you like to save the game?");
        dialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                askForSaveName();
            }
        });
        dialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                // do next turn
                if (duellGame.getCurrentPlayer().equals("Computer"))
                {
                    computerMakesMove();
                }
            }
        });
        AlertDialog saver = dialogBuilder.create();
        saver.setCanceledOnTouchOutside(false);
        saver.show();
    }

    /**
     * asks for the name of the text file the user will save to
     */
    private void askForSaveName()
    {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        final EditText fileOutput = new EditText(StartGameActivity.this);
        dialogBuilder.setTitle("Filename");
        dialogBuilder.setMessage("Please enter the name of the text file you want to save (do not include .txt).");
        dialogBuilder.setView(fileOutput);
        dialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener(){
           @Override
            public void onClick(DialogInterface dialog, int which)
           {
               String filename = fileOutput.getText().toString();
               saveGame(filename);
           }
        });
        AlertDialog saveInput = dialogBuilder.create();
        saveInput.setCanceledOnTouchOutside(false);
        saveInput.show();
    }

    /**
     * saves the game
     * @param filename the name of the text file to create
     */
    private void saveGame(String filename)
    {
        if (duellGame.saveFile(filename))
        {
            Toast.makeText(getApplicationContext(), "File successfully saved! Quitting...", Toast.LENGTH_SHORT).show();
            // exit the program
            finishAffinity();
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        else
        {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
            dialogBuilder.setTitle("Error");
            dialogBuilder.setMessage("The file was not successfully saved. The program will now exit.");
            dialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    // exit the program
                    finishAffinity();
                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_HOME);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            });
            AlertDialog saveError = dialogBuilder.create();
            saveError.setCanceledOnTouchOutside(false);
            saveError.show();
        }
    }
}
