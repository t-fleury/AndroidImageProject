package com.project.img.projectimage;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.ImageView;

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
                        Bitmap bitmap = imageActivity.getmCustomImageView().getBitmap();
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

                                break;
                            case 3 :
                                Filter.toGray(bitmap);
                                break;
                            case 4 :
                                Filter.toSepia(bitmap);
                                break;
                            case 5 :
                                imageActivity.setChoice(true);
                                seekBarColorDialog.show(getFragmentManager(), "contrast");
                                break;
                            case 6 :
                                Filter.toRandomColor(bitmap);
                                break;
                            case 7 :
                                imageActivity.setChoice(false);
                                seekBarColorDialog.show(getFragmentManager(), "contrast");
                                break;
                            case 8 :

                                break;
                        }
                    }
                });
        return builder.create();
    }
}
