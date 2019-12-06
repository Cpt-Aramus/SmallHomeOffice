package com.example.smallhomeoffice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

/**
 * This is the first app screen to be launched.
 */
public class MainMenu extends AppCompatActivity {

    /**
     * @param savedInstanceState = start status
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
    }

    // starts the To-Do-Main-Activity

    /**
     * This method starts our toDoList-Screen.
     * @param view = view
     */
    public void startToDoList(View view) {

      startActivity(new Intent(this, toDoNewList.class));
    }

    // starts the Buying-Main-Activity

    /**
     * This method starts our shoppinglist.
     * @param view = view
     */
    public void startBuying(View view) {

        startActivity(new Intent(this, Shopping_List.class));

    }

    // starts the Important-Main-Activity

    /**
     * This method starts our list for important notes.
     * @param view = v
     */
    public void startImportant(View view) {

        startActivity(new Intent(this, ImportantNotes.class));

    }

    // starts the HabitTracker-App
    // Source: https://stackoverflow.com/questions/3872063/launch-an-application-from-another-application-on-android
    // Credits HabitTracker: https://github.com/iSoron/uhabits

    /**
     * This method starts the HabitTracker-App.
     * @param view = view
     */
    public void startHabitTracker(View view) {

        Intent launchIntent = getPackageManager().getLaunchIntentForPackage("org.isoron.uhabits");

        //null pointer check in case package name was not found
        if (launchIntent != null) {
            startActivity(launchIntent);
        } else {
            Toast.makeText(this, "Cannot launch activity.", Toast.LENGTH_SHORT).show();
        }
    }

}