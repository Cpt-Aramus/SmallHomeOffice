package com.example.smallhomeoffice;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

class FileLibary_ShoppingList {
    private static final String FILENAME = "ShoppingList.dat";

    public static void save_List(ArrayList items, Context context) {

        try {
            FileOutputStream get_Input =context.openFileOutput(FILENAME, Context.MODE_PRIVATE);

            // Source: https://www.youtube.com/watch?v=YmRPIGFftp0
            ObjectOutputStream save_Stream = new ObjectOutputStream(get_Input);
            save_Stream.writeObject(items);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<String> load_List (Context context) {

        ArrayList<String> items_List = null;
        try {
            FileInputStream transfer_Input = context.openFileInput(FILENAME);

            // Source: https://www.youtube.com/watch?v=YmRPIGFftp0
            ObjectInputStream load_Stream = new ObjectInputStream(transfer_Input);
            items_List = (ArrayList<String>) load_Stream.readObject();
        } catch (FileNotFoundException e) {

            items_List = new ArrayList<>();

            e.printStackTrace();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return items_List;
    }
}

