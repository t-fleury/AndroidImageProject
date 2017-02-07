package com.project.img.projectimage;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class ImageActivity extends AppCompatActivity {

    ImageView initialImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        initialImage = (ImageView) findViewById(R.id.initial_image);

        Intent intent = getIntent();
        String mCurrentPhotoPath = intent.getStringExtra("Picture path");
        Bitmap picture_bm = BitmapFactory.decodeFile(mCurrentPhotoPath);
        initialImage.setImageBitmap(picture_bm);

        Button filter_button = (Button) findViewById(R.id.filter_button);
        filter_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Filter_DialogFragment filter_dialogFragment = new Filter_DialogFragment();
                filter_dialogFragment.show(getFragmentManager(), "filters");
            }
        });
    }
}
