package com.project.img.projectimage;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;

/**
 * Created by fleur on 05/02/2017.
 */

public class Filter_DialogFragment extends DialogFragment {


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
       Resources res = getResources();
       AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
       builder.setTitle(getString(R.string.filter))
                .setItems(res.getStringArray(R.array.filter_name), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int option) {
                        dialog.dismiss();
                    }
                });
        return builder.create();
    }
}
