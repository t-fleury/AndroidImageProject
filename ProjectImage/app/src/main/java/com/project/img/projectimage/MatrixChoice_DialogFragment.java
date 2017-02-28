package com.project.img.projectimage;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;


public class MatrixChoice_DialogFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Resources res = getResources();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.matrix))
                .setItems(res.getStringArray(R.array.matrix_size), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ImageActivity imageActivity = (ImageActivity) getActivity();
                        switch (imageActivity.getConvolutionChoice()){
                            case 0 :
                                imageActivity.setPicture(Filter.meanConvulation((2*(which+1)+1), imageActivity.getPicture()));
                                break;
                            case 1 :
                                imageActivity.setPicture(Filter.medianConvulation((2*(which+1)+1), imageActivity.getPicture()));
                                break;
                            case 2 :
                                //TODO Gaussian filter
                                break;
                            case 3 :
                                //TODO Sobel filter
                                break;
                        }
                    }
                });
        return builder.create();
    }

}
