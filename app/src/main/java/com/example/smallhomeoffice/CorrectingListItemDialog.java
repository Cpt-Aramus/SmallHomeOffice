package com.example.smallhomeoffice;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatDialogFragment;

/**
 * This class creates our custom dialog window for
 * editing an item in our listview
 * https://www.youtube.com/watch?v=ARezg1D9Zd0
 *
 */
public class CorrectingListItemDialog extends AppCompatDialogFragment {

    private EditText changedText;
    private DialogListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.editing_listitem_dialog, null);

        builder.setView(view);
        builder.setTitle("Edit:");

        //TODO Polishing buttons
        // https://www.youtube.com/watch?v=plnLs6aST1M

        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            //does nothing really, you just jump back to the List itself
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        builder.setPositiveButton("edit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {


                String oldListItem = changedText.getText().toString();
                listener.applyText(oldListItem);

            }
        });

        changedText = view.findViewById(R.id.inputTextChange);

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (DialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement DialogListener");
        }
    }

    public interface DialogListener {

        void applyText(String testString);

    }
}
