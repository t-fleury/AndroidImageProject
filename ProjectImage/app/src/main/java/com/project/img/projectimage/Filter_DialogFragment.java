package com.project.img.projectimage;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Bitmap;
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
                        ImageActivity imageActivity = (ImageActivity) getActivity();
                        SeekBarDialog seekBarDialog = new SeekBarDialog();
                        SeekBarColorDialog seekBarColorDialog = new SeekBarColorDialog();
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
                                //TODO
                                break;
                            case 3 :
                                imageActivity.getmCustomImageView().setImageBitmap(Filter.toGray(imageActivity.getmCustomImageView().getBitmap()));
                                break;
                            case 4 :
                                imageActivity.getmCustomImageView().setImageBitmap(Filter.toSepia(imageActivity.getmCustomImageView().getBitmap()));
                                break;
                            case 5 :
                                imageActivity.setChoice(true);
                                seekBarColorDialog.show(getFragmentManager(), "notRandom");
                                break;
                            case 6 :
                                imageActivity.getmCustomImageView().setImageBitmap(Filter.toRandomColor(imageActivity.getmCustomImageView().getBitmap()));
                                break;
                            case 7 :
                                imageActivity.setChoice(false);
                                seekBarColorDialog.show(getFragmentManager(), "keep");
                                break;
                            case 8 :
                                imageActivity.getmCustomImageView().setImageBitmap(Filter.laplcianConvolution(1, imageActivity.getmCustomImageView().getBitmap()));
                                break;
                            case 9 :
                                imageActivity.getmCustomImageView().setImageBitmap(Filter.laplcianConvolution(2, imageActivity.getmCustomImageView().getBitmap()));
                                break;
                            case 10:
                                //TODO
                                break;
                            case 11:
                                //TODO
                                break;
                        }
                    }
                });
        return builder.create();
    }
}
