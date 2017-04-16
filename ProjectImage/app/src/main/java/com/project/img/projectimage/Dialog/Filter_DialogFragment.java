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

public class Filter_DialogFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
       Resources res = getResources();
       final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
       builder.setTitle(getString(R.string.filter))
                .setItems(res.getStringArray(R.array.filter_name), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int option) {
                        dialog.dismiss();
                        ImageActivity imageActivity = (ImageActivity) getActivity();
                        SeekBarDialog seekBarDialog = new SeekBarDialog();
                        SeekBarColorDialog seekBarColorDialog = new SeekBarColorDialog();
                        MatrixChoice_DialogFragment matrixChoise_dialogFragment = new MatrixChoice_DialogFragment();
                        switch (option){
                            case 0 :
                                imageActivity.setChoice(true);
                                seekBarDialog.show(getFragmentManager(), "luminosity");
                                break;
                            case 1 :
                                imageActivity.setChoice(false);
                                seekBarDialog.show(getFragmentManager(), "contrast");
                                break;
                            case 2 :
                                imageActivity.setPicture(Filter.egalizationHistogram(imageActivity.getPicture()));
                                break;
                            case 3 :
                                imageActivity.setPicture(Filter.toShadeofGray(imageActivity.getPicture()));
                                break;
                            case 4 :
                                imageActivity.setPicture(Filter.toSepia(imageActivity.getPicture()));
                                break;
                            case 5 :
                                imageActivity.setChoice(true);
                                seekBarColorDialog.show(getFragmentManager(), "notRandom");
                                break;
                            case 6 :
                                double rm = (Math.random())*360;
                                imageActivity.setPicture(Filter.toColor(imageActivity.getPicture(), (int)rm));
                                break;
                            case 7 :
                                imageActivity.setChoice(false);
                                seekBarColorDialog.show(getFragmentManager(), "keep");
                                break;
                            case 8 :
                                imageActivity.setPicture(Filter.laplacianConvolution(1, imageActivity.getPicture()));
                                break;
                            case 9 :
                                imageActivity.setPicture(Filter.laplacianConvolution(2, imageActivity.getPicture()));
                                break;
                            case 10:
                                imageActivity.setConvolutionChoice(1);
                                matrixChoise_dialogFragment.show(getFragmentManager(), "mean");
                                break;
                            case 11:
                                imageActivity.setConvolutionChoice(2);
                                matrixChoise_dialogFragment.show(getFragmentManager(),"median");
                                break;
                            case 12:
                                imageActivity.setConvolutionChoice(3);
                                matrixChoise_dialogFragment.show(getFragmentManager(),"gaussian");
                                break;
                            case 13:
                                imageActivity.setPicture(Filter.sobelConvolution(imageActivity.getPicture()));
                                break;
                            case 14 :
                                imageActivity.setPicture(Filter.pencilEffect(imageActivity.getPicture()));
                                break;
                        }
                    }
                });
        return builder.create();
    }
}
