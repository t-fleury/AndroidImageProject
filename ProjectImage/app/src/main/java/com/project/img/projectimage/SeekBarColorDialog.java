package com.project.img.projectimage;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.SeekBar;

/**
 * Created by fleur on 20/10/2016.
 */

public class SeekBarColorDialog extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater layout = getActivity().getLayoutInflater();
        builder.setTitle(R.string.color);
        builder.setView(layout.inflate(R.layout.seekbar_spectrum_dialog,null));

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
                SeekBar value = (SeekBar) ((AlertDialog) dialog).findViewById(R.id.choix);
                ImageActivity imageActivity = (ImageActivity) getActivity();
                if (imageActivity.isChoice()){
                    imageActivity.getmCustomImageView().setImageBitmap(Filter.toNotRandomColor(imageActivity.getmCustomImageView().getBitmap(),value.getProgress()));
                }else{
                    imageActivity.getmCustomImageView().setImageBitmap(Filter.ColorFilter(imageActivity.getmCustomImageView().getBitmap(),value.getProgress()));
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        return builder.create();
    }

}