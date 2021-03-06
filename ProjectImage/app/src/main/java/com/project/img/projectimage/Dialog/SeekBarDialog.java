package com.project.img.projectimage.Dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.SeekBar;

import com.project.img.projectimage.Filter.*;
import com.project.img.projectimage.IHM.ImageActivity;
import com.project.img.projectimage.R;

public class SeekBarDialog extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater layout = getActivity().getLayoutInflater();
        builder.setTitle(R.string.value);
        builder.setView(layout.inflate(R.layout.seekbar_dialog,null));

        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
                SeekBar value = (SeekBar) ((AlertDialog) dialog).findViewById(R.id.choix);
                int percentage = value.getProgress();
                percentage -= 50;
                ImageActivity imageActivity = (ImageActivity) getActivity();
                if (imageActivity.isChoice()){
                    imageActivity.setPicture(Filter.changeLuminosity(imageActivity.getPicture(), percentage));
                }else{
                    imageActivity.setPicture(Filter.changeContrast(imageActivity.getPicture(), percentage));
                }
            }
        });

        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        return builder.create();
    }

}