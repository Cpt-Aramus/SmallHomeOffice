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
public class Shopping_List extends AppCompatActivity implements View.OnClickListener {

    EditText input_item;
    Button add_button;
    ListView display;

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
        switch (v.getId()) {
            case R.id.add_btn:
                String written_item = input_item.getText().toString();
                if(!written_item.equals("")){
                    goods_adapter.add(written_item);
                    input_item.setText("");

                    FileLibary_ShoppingList.save_List(goods, this);

                    Toast.makeText(this, "Item added to List!", Toast.LENGTH_SHORT).show();
                    break;
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

        if(item.getTitle() == "Delete") {
            goods.remove(listItem_info.position);
            goods_adapter.notifyDataSetChanged();
            FileLibary_ShoppingList.save_List(goods, this);
            Toast.makeText(this, "Deleted", Toast.LENGTH_SHORT).show();
        }
        return true;
    }
}
