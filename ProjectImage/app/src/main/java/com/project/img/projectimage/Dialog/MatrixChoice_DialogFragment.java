package com.project.img.projectimage.Dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;

import com.project.img.projectimage.Filter.*;
import com.project.img.projectimage.IHM.ImageActivity;
import com.project.img.projectimage.R;


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
                            case 1 :
                                imageActivity.setPicture(Filter.meanConvolution((2*(which+1)+1), imageActivity.getPicture()));
                                break;
                            case 2 :
                                imageActivity.setPicture(Filter.medianConvolution((2*(which+1)+1), imageActivity.getPicture()));
                                break;
                            case 3 :
                                //TODO Gaussien
                                //imageActivity.setPicture(Filter.gaussianConvolution((2*(which+1)+1), 1,imageActivity.getPicture()));
                                break;
                        }
                    }
                });
        return builder.create();
    }

}
