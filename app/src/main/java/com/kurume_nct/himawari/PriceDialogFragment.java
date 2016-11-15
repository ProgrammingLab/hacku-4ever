package com.kurume_nct.himawari;


import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.EditText;

public class PriceDialogFragment extends BaseDialogFragment {

    public static PriceDialogFragment newInstance(Fragment fragment, int requestCode, String label) {
        PriceDialogFragment dialog = new PriceDialogFragment();
        Bundle args = new Bundle();
        args.putString("TITLE", label);
        dialog.setArguments(args);
        dialog.setTargetFragment(fragment, requestCode);
        return dialog;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final View v = getActivity().getLayoutInflater().inflate(R.layout.fragment_dialog_input, null);
        String title = getArguments().getString("TITLE");

        return new AlertDialog.Builder(getActivity()).setTitle(title).setView(v).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                EditText text = (EditText) v.findViewById(R.id.editText);
                getListener().onValueSet(getTargetRequestCode(), text.getText().toString().trim());
            }
        }).create();
    }
}