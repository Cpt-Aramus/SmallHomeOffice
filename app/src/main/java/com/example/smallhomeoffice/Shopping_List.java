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
 * This class creates and manages the shopping list.
 */
public class Shopping_List extends AppCompatActivity implements View.OnClickListener, CorrectingListItemDialog.DialogListener {

    EditText input_item;
    Button add_button;
    ListView display;

    //needed for replacing a listitem after editing
    int position;

    ArrayList<String> goods;
    ArrayAdapter<String> goods_adapter;

    /**
     *
     * @param savedInstanceState = instant startstatus
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do_new_list);

        // Source: https://stackoverflow.com/questions/2496901/android-on-screen-keyboard-auto-popping-up
        // prevent edit text from popping up
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        input_item = findViewById(R.id.input);
        add_button = findViewById(R.id.add_btn);
        display = findViewById(R.id.display);

        goods = FileLibary_ShoppingList.load_List(this);

        goods_adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, goods);
        display.setAdapter(goods_adapter);

        add_button.setOnClickListener(this);

        registerForContextMenu(display);

    }

    // Huge Credit to https://www.youtube.com/watch?v=YmRPIGFftp0
    // If Add-Button gets clicked, Item will be added

    /**
     *
     * @param v = view
     */
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.add_btn) {
            String written_item = input_item.getText().toString();
            if (!written_item.equals("")) {
                goods_adapter.add(written_item);
                input_item.setText("");

                FileLibary_ShoppingList.save_List(goods, this);

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
        menu.add("edit");
        menu.add("move");
    }

    // Bugfixing: https://www.youtube.com/watch?v=Pq9YQl0nfEk

    /**
     *
     * @param item = item in the popup-menu
     * @return true, if everything worked correctly
     */
    @Override
    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo listItem_info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        position = listItem_info.position;

        switch (item.getTitle().toString()) {
            case "Delete":
            goods.remove(listItem_info.position);
            goods_adapter.notifyDataSetChanged();
            FileLibary_ShoppingList.save_List(goods, this);
            Toast.makeText(this, "Deleted", Toast.LENGTH_SHORT).show();
            break;

            case "edit":
                // Creates the dialog window, saving the text to our list is also controlled in the belonging class
                openDialog();
                break;
            case "move":
                goods.remove(listItem_info.position);
                goods_adapter.notifyDataSetChanged();
                FileLibary_ToDoList.save_List(goods, this);

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

        goods.remove(position);
        goods.add(position, testString );
        goods_adapter.notifyDataSetChanged();
        FileLibary_ToDoList.save_List(goods, this);
        Toast.makeText(this, "Edited", Toast.LENGTH_SHORT).show();
    }
}
