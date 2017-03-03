package com.project.img.projectimage;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.Vector;

public class ImageActivity extends AppCompatActivity {

    private CustomImageView mCustomImageView;
    private ImageView scaledDrawable;
    private boolean choice;
    private int convolutionChoice;
    private Button filter_button;
    private Button display_scaled_button;
    private Button display_fs_button;
    private Bitmap picture;
    private Vector<Bitmap> saved_pictures;
    private ContentResolver contentResolver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        Intent intent = getIntent();

        String mCurrentPhotoPath = intent.getStringExtra("Picture path");

        contentResolver = this.getContentResolver();

        picture = BitmapFactory.decodeFile(mCurrentPhotoPath);

        saved_pictures = new Vector<Bitmap>();
        Log.d("V size", Integer.toString(saved_pictures.size()));

        scaledDrawable = (ImageView) findViewById(R.id.scaledDrawable);
        display_fs_button = (Button) findViewById(R.id.display_fs_button);
        display_scaled_button = (Button) findViewById(R.id.display_scaled_button);
        mCustomImageView = new CustomImageView(this.getBaseContext(), picture);

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);

        layoutParams.addRule(RelativeLayout.ABOVE, R.id.filter_button);

        mCustomImageView.setLayoutParams(layoutParams);

        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.mainLayout);

        relativeLayout.addView(mCustomImageView);
        scaledDrawable.setImageBitmap(picture);

        mCustomImageView.setVisibility(View.INVISIBLE);
        display_scaled_button.setVisibility(View.INVISIBLE);

        display_fs_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scaledDrawable.setVisibility(View.INVISIBLE);
                mCustomImageView.setVisibility(View.VISIBLE);
                display_scaled_button.setVisibility(View.VISIBLE);
                display_fs_button.setVisibility(View.INVISIBLE);
            }
        });

        display_scaled_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scaledDrawable.setVisibility(View.VISIBLE);
                mCustomImageView.setVisibility(View.INVISIBLE);
                display_fs_button.setVisibility(View.VISIBLE);
                display_scaled_button.setVisibility(View.INVISIBLE);
            }
        });

        filter_button = (Button) findViewById(R.id.filter_button);

        filter_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Filter_DialogFragment filter_dialogFragment = new Filter_DialogFragment();
                filter_dialogFragment.show(getFragmentManager(), "filters");
            }
        });
    }

    public Bitmap getPicture() {
        return picture.copy(picture.getConfig(), true);
    }

    public void setPicture(Bitmap pic) {
        saved_pictures.add(picture.copy(picture.getConfig(), true));
        this.picture = pic;
        mCustomImageView.setBitmap(picture);
        scaledDrawable.setImageBitmap(picture);
        invalidateOptionsMenu();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.undo_option);
        if (saved_pictures.size() >= 1){
            menuItem.setVisible(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_option:
                String res = MediaStore.Images.Media.insertImage(contentResolver, picture.copy(picture.getConfig(), true), "", "");
                return true;
            case R.id.undo_option:
                Bitmap bitmap = saved_pictures.lastElement();
                this.picture = bitmap;
                mCustomImageView.setBitmap(picture);
                scaledDrawable.setImageBitmap(picture);
                int last_index = saved_pictures.size() - 1;
                saved_pictures.remove(last_index);
                invalidateOptionsMenu();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public boolean isChoice() {
        return choice;
    }

    public void setChoice(boolean choice) {
        this.choice = choice;
    }

    public int getConvolutionChoice() {
        return convolutionChoice;
    }

    public void setConvolutionChoice(int convolutionChoice) {
        this.convolutionChoice = convolutionChoice;
    }
}
