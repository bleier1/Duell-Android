//     ************************************************************
//     * Name:  Bryan Leier                                       *
//     * Project:  Duell Java/Android                             *
//     * Class:  CMPS 366 - Organization of Programming Languages *
//     * Date:  12/2/2016                                         *
//     ************************************************************

package leier.bryan.duell.view;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import leier.bryan.duell.R;

public class MainActivity extends AppCompatActivity {

    // Boolean that determines if a new game will be started or not.
    public final static String EXTRA_NEWGAME = "leier.bryan.duell.newGame";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        int permResponse = 0;

        // check if we have read and write permissions
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, permResponse);
        }
    }

    /**
     * Start a new game after clicking on the new game button
     * @param view the activity being started
     */
    public void startNewGame(View view)
    {
        Intent intent = new Intent(this, StartGameActivity.class);
        intent.putExtra(EXTRA_NEWGAME, true);
        startActivity(intent);
    }

    /**
     * Load a game after clicking on the load game button
     * @param view
     */
    public void loadGame(View view)
    {
        Intent intent = new Intent(this, StartGameActivity.class);
        intent.putExtra(EXTRA_NEWGAME, false);
        startActivity(intent);
    }

    /**
     * permissions stuff
     * @param requestCode the request code for permissions
     * @param permissions the permission being granted
     * @param grantResults the results of the request
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 0: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}
