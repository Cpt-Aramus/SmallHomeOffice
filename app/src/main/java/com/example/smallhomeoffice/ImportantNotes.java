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
 * This class creates and manages the list of important notes.
 */
public class ImportantNotes extends AppCompatActivity implements  View.OnClickListener, CorrectingListItemDialog.DialogListener {

    EditText input;
    Button add_button;
    ListView display;

    //needed for replacing a listitem after editing
    int position;

    ArrayList<String> notes;
    ArrayAdapter<String> notes_adapter;

    /**
     *
     * @param savedInstanceState = instant startstatus
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_important_notes);

        // Source: https://stackoverflow.com/questions/2496901/android-on-screen-keyboard-auto-popping-up
        // prevent edit text from popping up
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        input = findViewById(R.id.input);
        add_button = findViewById(R.id.add_btn);
        display = findViewById(R.id.display);

        notes = FileLibaryNotes.load_List(this);

        notes_adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, notes);
        display.setAdapter(notes_adapter);

        add_button.setOnClickListener(this);

        registerForContextMenu(display);

    }

    // Huge Credit to https://www.youtube.com/watch?v=YmRPIGFftp0

    /**
     *
     * @param v = View
     */
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.add_btn) {
            String written_item = input.getText().toString();
            if (!written_item.equals("")) {
                notes_adapter.add(written_item);
                input.setText("");

                FileLibaryNotes.save_List(notes, this);

                Toast.makeText(this, "Item added to List!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Source: https://www.youtube.com/watch?v=IVDKyIOVrBU

    /**
     *
     * @param menu = context menu
     * @param v = view (listview)
     * @param menuInfo = info about context menu
     */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        menu.add("Delete");
        menu.add("Edit");
        menu.add("Move");
    }

    // Bugfixing: https://www.youtube.com/watch?v=Pq9YQl0nfEk

    /**
     *
     * @param item = item in popup-menu
     * @return true, if everything worked correctly
     */
    @Override
    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo listItem_info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        position = listItem_info.position;

        switch (item.getTitle().toString()) {
            case "Delete":
            notes.remove(listItem_info.position);
            notes_adapter.notifyDataSetChanged();
            FileLibaryNotes.save_List(notes, this);

            Toast.makeText(this, "Deleted", Toast.LENGTH_SHORT).show();
            break;
            case "Edit":
                // Creates the dialog window, saving the text to our list is also controlled in the belonging class
                openDialog();
                break;
            case "Move":
                notes.remove(listItem_info.position);
                notes_adapter.notifyDataSetChanged();
                FileLibary_ToDoList.save_List(notes, this);

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

        notes.remove(position);
        notes.add(position, testString );
        notes_adapter.notifyDataSetChanged();
        FileLibaryNotes.save_List(notes, this);
        Toast.makeText(this, "Edited", Toast.LENGTH_SHORT).show();
    }
}