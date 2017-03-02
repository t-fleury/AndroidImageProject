package com.project.img.projectimage;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.res.Resources;;
import android.graphics.Bitmap;
import android.os.Bundle;

public class Filter_DialogFragment extends DialogFragment {

    private Bitmap bitmap;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
       Resources res = getResources();
       final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
       builder.setTitle(getString(R.string.filter))
                .setItems(res.getStringArray(R.array.filter_name), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int option) {
                        dialog.dismiss();
                        ImageActivity imageActivity = (ImageActivity) getActivity();
                        bitmap = imageActivity.getPicture();
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
                                imageActivity.setPicture(Filter.egalizationConstrast(imageActivity.getPicture()));
                                break;
                            case 3 :
                                imageActivity.setPicture(Filter.toGray(imageActivity.getPicture()));
                                break;
                            case 4 :
                                imageActivity.setPicture(Filter.toSepia(imageActivity.getPicture()));
                                break;
                            case 5 :
                                imageActivity.setChoice(true);
                                seekBarColorDialog.show(getFragmentManager(), "notRandom");
                                break;
                            case 6 :
                                imageActivity.setPicture(Filter.toRandomColor(imageActivity.getPicture()));
                                break;
                            case 7 :
                                imageActivity.setChoice(false);
                                seekBarColorDialog.show(getFragmentManager(), "keep");
                                break;
                            case 8 :
                                imageActivity.setPicture(Filter.laplcianConvolution(1, imageActivity.getPicture()));
                                break;
                            case 9 :
                                imageActivity.setPicture(Filter.laplcianConvolution(2, imageActivity.getPicture()));
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
                                //TODO
                                break;
                            case 13:
                                //TODO
                                break;
                        }
                    }
                });
        return builder.create();
    }
}
