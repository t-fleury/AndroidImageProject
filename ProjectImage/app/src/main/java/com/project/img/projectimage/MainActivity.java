package com.project.img.projectimage;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import java.util.Arrays;


public class MainActivity extends AppCompatActivity {

    private Bitmap bitmap;
    private ImageView imageView;
    final private int SIZE_MATRIX = 5;
    final private int LAPLACE_SIZE_MATRIX = 3;
    final private int[][] LAPLACE_FILTER1 = {{0, 1, 0},
                                             {1,-4, 1},
                                             {0, 1, 0}};
    final private int[][] LAPLACE_FILTER2 = {{1, 1, 1},
                                             {1,-8, 1},
                                             {1, 1, 1}};
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

    //Moyenneur - Bugé sur mon tel(thomas)
    private void convulationMoyenneur(){
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int[] pixels = new int[width * height];
        int[] pixelMoyenneur = new int[width * height];
        int red=0, blue=0, green=0, x_pixelMatrix, y_pixelMatrix;

        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);

        for(int i = 0; i < pixelMoyenneur.length; i++)
        {
            x_pixelMatrix=i%width;
            y_pixelMatrix=i/width;
            if(i <= width*(SIZE_MATRIX/2) || i >= width * (height-(SIZE_MATRIX/2)) || i % width < SIZE_MATRIX/2  || i % width >= width-(SIZE_MATRIX/2)) {
                red = Color.red(pixels[i]);
                green = Color.green(pixels[i]);
                blue = Color.blue(pixels[i]);
            }else {
                for (int x = x_pixelMatrix -(SIZE_MATRIX / 2); x <= x_pixelMatrix +(SIZE_MATRIX / 2); x++) {
                    for (int y = y_pixelMatrix - (SIZE_MATRIX / 2); y <= y_pixelMatrix + (SIZE_MATRIX / 2); y++) {
                        red+=Color.red(pixels[x+y*width]);
                        green+=Color.green(pixels[x+y*width]);
                        blue+=Color.blue(pixels[x+y*width]);
                    }
                }
            }

            red/=(SIZE_MATRIX*SIZE_MATRIX);
            green/=(SIZE_MATRIX*SIZE_MATRIX);
            blue/=(SIZE_MATRIX*SIZE_MATRIX);
            pixelMoyenneur[i] = Color.rgb(red, green, blue);
        }

        bitmap.setPixels(pixelMoyenneur, 0, width, 0, 0, width, height);
        imageView.setImageBitmap(bitmap);
    }

    //Median
    private void convulationMedian(){
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int[] pixels = new int[width * height];
        int[] pixelMoyenneur = new int[width * height];
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
        int[] red= new int[SIZE_MATRIX*SIZE_MATRIX];
        int[] blue= new int[SIZE_MATRIX*SIZE_MATRIX];
        int[] green= new int[SIZE_MATRIX*SIZE_MATRIX];
        int x_pixelMatrix, y_pixelMatrix;
        for(int i = 0; i < pixelMoyenneur.length; i++)
        {
            x_pixelMatrix=i%width;
            y_pixelMatrix=i/width;

            if(i <= width*(SIZE_MATRIX/2) || i >= width * (height-(SIZE_MATRIX/2)) || i % width < SIZE_MATRIX/2  || i % width >= width-(SIZE_MATRIX/2))
            {
                red[0] = Color.red(pixels[i]);
                green[0] = Color.green(pixels[i]);
                blue[0] = Color.blue(pixels[i]);
            }
            else {
                int index = 0;
                for (int x = x_pixelMatrix -(SIZE_MATRIX / 2); x <= x_pixelMatrix +(SIZE_MATRIX / 2); x++) {
                    for (int y = y_pixelMatrix - (SIZE_MATRIX / 2); y <= y_pixelMatrix + (SIZE_MATRIX / 2); y++) {
                        red[index] = Color.red(pixels[x+y*width]);
                        green[index] = Color.green(pixels[x+y*width]);
                        blue[index] = Color.blue(pixels[x+y*width]);
                        index++;
                    }
                }
            }
            Arrays.sort(red);
            Arrays.sort(green);
            Arrays.sort(blue);
            pixelMoyenneur[i] = Color.rgb(red[(SIZE_MATRIX/2 + 1)], green[(SIZE_MATRIX/2 + 1)], blue[(SIZE_MATRIX/2 + 1)]);
        }

        bitmap.setPixels(pixelMoyenneur, 0, width, 0, 0, width, height);
        imageView.setImageBitmap(bitmap);
    }

    private void convulationLaplace(int[][] matrix){
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int[] pixels = new int[width * height];
        int[] pixelMoyenneur = new int[width * height];
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
        int[] red= new int[LAPLACE_SIZE_MATRIX*LAPLACE_SIZE_MATRIX];
        int[] blue= new int[LAPLACE_SIZE_MATRIX*LAPLACE_SIZE_MATRIX];
        int[] green= new int[LAPLACE_SIZE_MATRIX*LAPLACE_SIZE_MATRIX];
        int x_pixelMatrix, y_pixelMatrix;
        for(int i = 0; i < pixelMoyenneur.length; i++)
        {
            x_pixelMatrix=i%width;
            y_pixelMatrix=i/width;

            if(i <= width*(LAPLACE_SIZE_MATRIX/2) || i >= width * (height-(LAPLACE_SIZE_MATRIX/2)) || i % width < LAPLACE_SIZE_MATRIX/2  || i % width >= width-(LAPLACE_SIZE_MATRIX/2))
            {
                red[0] = Color.red(pixels[i]);
                green[0] = Color.green(pixels[i]);
                blue[0] = Color.blue(pixels[i]);
            }
            else {
                int index = 0;
                int laplaceX = 0, laplaceY = 0;
                for (int x = x_pixelMatrix -(LAPLACE_SIZE_MATRIX / 2); x <= x_pixelMatrix +(LAPLACE_SIZE_MATRIX / 2); x++) {
                    for (int y = y_pixelMatrix - (LAPLACE_SIZE_MATRIX / 2); y <= y_pixelMatrix + (LAPLACE_SIZE_MATRIX / 2); y++) {
                        red[index] = Color.red(pixels[x+y*width])*matrix[laplaceX][laplaceY];
                        green[index] = Color.green(pixels[x+y*width])*matrix[laplaceX][laplaceY];
                        blue[index] = Color.blue(pixels[x+y*width])*matrix[laplaceX][laplaceY];
                        index++;
                        laplaceY++;
                        if(laplaceY == 3){
                            laplaceX++;
                            laplaceY = 0;
                        }
                    }
                }
            }
            int finalRed = 0, finalGreen = 0, finalBlue = 0;
            for (int color : red) finalRed += color;
            for (int color : green) finalGreen += color;
            for (int color : blue) finalBlue += color;

            pixelMoyenneur[i] = Color.rgb(finalRed,finalGreen,finalBlue);
        }

        bitmap.setPixels(pixelMoyenneur, 0, width, 0, 0, width, height);
        imageView.setImageBitmap(bitmap);
    }

    @Override
    protected void onStart() {
        super.onStart();
        bitmap = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.tmp);
        bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);

        imageView = (ImageView) findViewById(R.id.imageView);
        imageView.setImageBitmap(bitmap);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button moyenneur = (Button) findViewById(R.id.moyenneur);
        moyenneur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                convulationMoyenneur();
            }
        });

        Button median = (Button) findViewById(R.id.median);
        median.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                convulationMedian();
            }
        });
        Button lp1 = (Button) findViewById(R.id.LP1);
        lp1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                convulationLaplace(LAPLACE_FILTER1);
            }
        });
        Button lp2 = (Button) findViewById(R.id.LP2);
        lp2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                convulationLaplace(LAPLACE_FILTER2);
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
