package com.project.img.projectimage;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import static com.project.img.projectimage.Filters.*;


public class MainActivity extends AppCompatActivity {

    private Bitmap bitmap;
    private ImageView imageView;
    final private int sizeMatrix = 3;
    /*private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };
    private static final int SELECT_PICTURE = 2;
    static final int REQUEST_TAKE_PHOTO = 3;
    private Bitmap picture;
    private String mCurrentPhotoPath;

    private void takePicture(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                System.out.println(ex);
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }*/

    //NOT FINISHED
    private void convulation(){
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int[] pixels = new int[width * height];
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
        int averageR = 0, averageG = 0, averageB = 0;
        for (int i = 0; i < width - sizeMatrix; i++) {
            for (int j = 0; j < height - sizeMatrix; j++) {
                for (int k = i; k < sizeMatrix; k++) {
                    for (int l = j; l < sizeMatrix; l++) {
                        averageR += Color.red(bitmap.getPixel(k,l));
                        averageG += Color.green(bitmap.getPixel(k,l));
                        averageB += Color.blue(bitmap.getPixel(k,l));
                    }
                }
               pixels[i+(sizeMatrix/2) + (j+(sizeMatrix/2)) * width] =
                       Color.rgb((averageR / (sizeMatrix*sizeMatrix))%256,
                                 (averageG / (sizeMatrix*sizeMatrix))%256,
                                 (averageB / (sizeMatrix*sizeMatrix))%256);
               averageR = 0;
               averageG = 0;
               averageB = 0;
            }
        }
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
    }

    @Override
    protected void onStart() {
        super.onStart();
        bitmap = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.pandaria);
        bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);

        imageView = (ImageView) findViewById(R.id.imageView);
        imageView.setImageBitmap(bitmap);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button testFilter = (Button) findViewById(R.id.testFilter);
        testFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                convulation();
            }
        });

       /* //Buttons
        Button take = (Button) findViewById(R.id.Take);
        Button getImg = (Button) findViewById(R.id.getImg);
        Button getSaved = (Button) findViewById(R.id.getSaved);

        take.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicture();
            }
        });

        getImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,
                        "Select Picture"), SELECT_PICTURE);
            }
        });

        getSaved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        //Check permissions for get picture and camera
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)!= PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CONTACTS)) {
            } else {
                ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, 0);
            }
        }*/
    }
    /*
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri uri = data.getData();
                File img = new File(uri.getPath());
                BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                picture = BitmapFactory.decodeFile(img.getPath(),bmOptions);
            }else if (requestCode == REQUEST_IMAGE_CAPTURE){
                Bundle extras = data.getExtras();
                picture = (Bitmap) extras.get("data");
                ((ImageView) findViewById(R.id.imageView)).setImageBitmap(picture);
            }
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    public Bitmap getpicture() {
        return picture;
    }*/
}
