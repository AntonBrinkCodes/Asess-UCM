package com.example.asessucm.uiutils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.asessucm.R;

public class NoticeDialogFragment extends DialogFragment {

    String titleString, bodyString, negativeBtnString, positiveBtnString;
public NoticeDialogFragment(String[] strings){
    titleString = strings[0];
    bodyString = strings[1];
    negativeBtnString = strings[2];
    positiveBtnString = strings[3];
}
    /* The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.*/
    public interface NoticeDialogListener {
        public void onDialogPositiveClick();
        public void onDialogNegativeClick();
    }

    // Use this instance of the interface to deliver action events
    NoticeDialogListener listener;

    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            listener = (NoticeDialogListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException("must implement NoticeDialogListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());

                builder.setTitle(titleString);
                builder.setMessage(bodyString);
                builder.setNegativeButton(negativeBtnString,(dialog, which)->
                        listener.onDialogNegativeClick());
                builder.setPositiveButton(positiveBtnString,(dialog, which)->
                        listener.onDialogPositiveClick());
        return builder.create();
    }

}
