package leier.bryan.duell.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

import leier.bryan.duell.R;

public class winnerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_winner);

        // Get the wins from StartGameActivity:
        Bundle extras = getIntent().getExtras();
        int humanWins = extras.getInt("humanWins");
        int computerWins = extras.getInt("computerWins");

        TextView text = (TextView) findViewById(R.id.finalCompWins);
        String winText = "Computer Wins: " + Integer.toString(computerWins);
        text.setText(winText);
        winText = "Human Wins: " + Integer.toString(humanWins);
        text = (TextView) findViewById(R.id.finalHumanWins);
        text.setText(winText);

        // Determine who won, or if there was a draw:
        if (humanWins == computerWins)
        {
            // draw
            winText = "Draw!";
        }
        if (humanWins > computerWins)
        {
            // human win
            winText = "Human wins!";
        }
        if (humanWins < computerWins)
        {
            // computer win
            winText = "Computer wins!";
        }
        text = (TextView) findViewById(R.id.winner);
        text.setText(winText);
    }
}
