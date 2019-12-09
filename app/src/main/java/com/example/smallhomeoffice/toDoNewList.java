package com.example.smallhomeoffice;

import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

/**
 * This Class creates and manages the ToDoList and got called up by the MainMenu-Activity.
 */
public class toDoNewList extends AppCompatActivity implements View.OnClickListener, CorrectingListItemDialog.DialogListener {

    EditText edit;

    //needed for replacing a listitem after editing
    int position;

   Button add_button;
    ListView display;

    ArrayList<String> tasks;
    ArrayAdapter<String> tasks_adapter;

    /**
     *
     * @param savedInstanceState = instant Startstatus
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do_new_list);

        // Source: https://stackoverflow.com/questions/2496901/android-on-screen-keyboard-auto-popping-up
        // prevent edit text from popping up
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        // connect views
        edit = findViewById(R.id.input);
        add_button = findViewById(R.id.add_btn);
        display = findViewById(R.id.display);

        // First Step: Load the current To-Do-List
        tasks = FileLibary_ToDoList.load_List(this);

        // define the ListView-Adapter
        tasks_adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, tasks);
        display.setAdapter(tasks_adapter);

        // Clickable Button
        add_button.setOnClickListener(this);

        // Popup-Menu connecting with Listview
        registerForContextMenu(display);

    }

    // Huge Credit to https://www.youtube.com/watch?v=YmRPIGFftp0

    /**
     *
     * @param v = View
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_btn:
                String written_item = edit.getText().toString();
                if (!written_item.equals("")) {
                    tasks_adapter.add(written_item);
                    // Field gets empty after saving
                    edit.setText("");

                    // Saving To-Do-List
                    FileLibary_ToDoList.save_List(tasks, this);

                    Toast.makeText(this, "Item added to List!", Toast.LENGTH_SHORT).show();
                    break;
                }
        }
    }


    /**
     *
     * @param menu = Context menu
     * @param v = Current View
     * @param menuInfo = Current menuInfo
     */

    // Source: https://www.youtube.com/watch?v=IVDKyIOVrBU
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        menu.add("delete");
        menu.add("edit");
        menu.add("move");
    }


    // Bugfixing: https://www.youtube.com/watch?v=Pq9YQl0nfEk
    /**
     *
     * @param item = pressed item in the listview
     * @return true
     */

    //TODO https://www.youtube.com/watch?v=eX-TdY6bLdg
    //TODO https://www.youtube.com/watch?v=cT9w4T9FCSQ



    @Override
    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo listItem_info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

      position = listItem_info.position;

        switch (item.getTitle().toString()) {
            case "delete":
                tasks.remove(listItem_info.position);
                tasks_adapter.notifyDataSetChanged();
                FileLibary_ToDoList.save_List(tasks, this);

                Toast.makeText(this, "Deleted!", Toast.LENGTH_SHORT).show();
                break;
            case "edit":
                // Creates the dialog window, saving the text to our list is also controlled in the belonging class
                openDialog();
               break;
            case "move":
                tasks.remove(listItem_info.position);
                tasks_adapter.notifyDataSetChanged();
                FileLibary_ToDoList.save_List(tasks, this);

                Toast.makeText(this, "Moved!", Toast.LENGTH_SHORT).show();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + item.getTitle());
        }

        return true;
    }

    private void openDialog() {

    CorrectingListItemDialog dialog = new CorrectingListItemDialog();
    dialog.show(getSupportFragmentManager(), "example dialog");

    }


    @Override
    public void applyText(String testString) {

        tasks.remove(position);
        tasks.add(position, testString );
        tasks_adapter.notifyDataSetChanged();
        FileLibary_ToDoList.save_List(tasks, this);
        Toast.makeText(this, "Edited", Toast.LENGTH_SHORT).show();
    }
}

