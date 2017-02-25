package com.project.img.projectimage;

import android.graphics.Bitmap;
import android.graphics.Color;
import java.util.Arrays;
import static android.graphics.Color.HSVToColor;
import static android.graphics.Color.RGBToHSV;

/**
 * Created by fleur on 25/02/2017.
 */

abstract class Filter{

    static Bitmap toNormal(Bitmap bmp) {
        return bmp.copy(bmp.getConfig(), true);
    }

    static Bitmap toGray(Bitmap bmp) {
        int width = bmp.getWidth();
        int height = bmp.getHeight();
        int[] pixels = new int[width * height];
        bmp.getPixels(pixels, 0, width, 0, 0, width, height);

        for (int i = 0; i < pixels.length; i++) {
            int r = ((Color.red(pixels[i])*3)/10);
            int g = ((Color.green(pixels[i])*59)/100);
            int b = ((Color.blue(pixels[i])*11)/100);
            int total = b + r + g;

            pixels[i] = Color.rgb(total, total, total);
        }
        bmp.setPixels(pixels, 0, width, 0, 0, width, height);
        return bmp;
    }

    static Bitmap toSepia(Bitmap bmp) {
        int width = bmp.getWidth();
        int height = bmp.getHeight();
        int[] pixels = new int[width * height];
        bmp.getPixels(pixels, 0, width, 0, 0, width, height);

        for (int i = 0; i < pixels.length; i++) {
            int blue = Color.blue(pixels[i]);
            int red = Color.red(pixels[i]);
            int green = Color.green(pixels[i]);

            int finalRed=(red*393/1000)+(green*769/1000)+(blue*189/1000);
            int finalGreen=(red*349/1000)+(green*686/1000)+(blue*168/1000);
            int finalBlue=(red*272/1000)+(green*534/1000)+(blue*131/1000);
            if(finalRed>255)
                finalRed=255;
            if(finalGreen>255)
                finalGreen=255;
            if(finalBlue>255)
                finalBlue=255;

            pixels[i] = Color.rgb(finalRed, finalGreen, finalBlue);
        }
        bmp.setPixels(pixels, 0, width, 0, 0, width, height);
        return bmp;
    }

    static Bitmap toRandomColor(Bitmap bmp) {
        double rm=(Math.random())*360;
        int width = bmp.getWidth();
        int height = bmp.getHeight();
        int[] pixels = new int[width * height];
        bmp.getPixels(pixels, 0, width, 0, 0, width, height);
        float [] hsv=new float[3];
        for (int i = 0; i < pixels.length; i++) {
            int o = pixels[i];
            int blue = Color.blue(o);
            int red = Color.red(o);
            int green = Color.green(o);

            RGBToHSV(red,green,(blue),hsv);
            hsv[0]=(float)rm;

            pixels[i] = HSVToColor(hsv);
        }
        bmp.setPixels(pixels, 0, width, 0, 0, width, height);
        return bmp;
    }

    static Bitmap toNotRandomColor(Bitmap bmp, int color) {
        int width = bmp.getWidth();
        int height = bmp.getHeight();
        float[] HSV = new float[width * height];
        int[] pixels = new int[width * height];
        bmp.getPixels(pixels, 0, width, 0, 0, width, height);
        for (int i = 0; i < pixels.length; i++) {
            int blue = Color.blue(pixels[i]);
            int red = Color.red(pixels[i]);
            int green = Color.green(pixels[i]);
            Color.RGBToHSV(red, green, blue, HSV);
            HSV[0] = color;
            pixels[i] = Color.HSVToColor(HSV);
        }
        bmp.setPixels(pixels, 0, width, 0, 0, width, height);
        return bmp;
    }

    static Bitmap ColorFilter(Bitmap bmp, int option) {
        int width = bmp.getWidth();
        int height = bmp.getHeight();
        float[] HSV = new float[width * height];
        int[] pixels = new int[width * height];
        bmp.getPixels(pixels, 0, width, 0, 0, width, height);

        for (int i = 0; i < pixels.length; i++) {
            int blue = Color.blue(pixels[i]);
            int red = Color.red(pixels[i]);
            int green = Color.green(pixels[i]);
            Color.RGBToHSV(red, green, blue, HSV);
            if(HSV[0] < option - 30 || HSV[0] > option + 30){
                int total = blue + red + green;
                pixels[i] = Color.rgb(total, total, total);
            }
        }
        bmp.setPixels(pixels, 0, width, 0, 0, width, height);
        return bmp;
    }

    static Bitmap changeLuminosity(Bitmap bmp, int percentage){
        int width = bmp.getWidth();
        int height = bmp.getHeight();
        int[] pixels = new int[width * height];
        bmp.getPixels(pixels, 0, width, 0, 0, width, height);
        for (int i = 0; i < pixels.length; i++) {
            int o = pixels[i];
            int blue = Color.blue(o) + Color.blue(o) * (percentage/100);
            if(blue>255)
                blue=255;
            if(blue < 0)
                blue = 0;

            int red = Color.red(o) + Color.red(o) * (percentage/100);
            if(red>255)
                red=255;
            if(red < 0)
                red = 0;

            int green = Color.green(o) + Color.green(o) * (percentage/100);
            if(green>255)
                green=255;
            if(green < 0)
                green = 0;

            pixels[i] = Color.rgb(red, green, blue);

        }
        bmp.setPixels(pixels, 0, width, 0, 0, width, height);
        return bmp;
    }

    static Bitmap meanConvulation(int sizeMatrix, Bitmap bmp){
        int width = bmp.getWidth();
        int height = bmp.getHeight();
        int[] pixels = new int[width * height];
        int[] pixelMoyenneur = new int[width * height];
        int red=0, blue=0, green=0, x_pixelMatrix, y_pixelMatrix;

        bmp.getPixels(pixels, 0, width, 0, 0, width, height);

        for(int i = 0; i < pixelMoyenneur.length; i++)
        {
            x_pixelMatrix=i%width;
            y_pixelMatrix=i/width;
            if(i <= width*(sizeMatrix/2) || i >= width * (height-(sizeMatrix/2)) || i % width < sizeMatrix/2  || i % width >= width-(sizeMatrix/2)) {
                red = Color.red(pixels[i]);
                green = Color.green(pixels[i]);
                blue = Color.blue(pixels[i]);
            }else {
                for (int x = x_pixelMatrix -(sizeMatrix / 2); x <= x_pixelMatrix +(sizeMatrix / 2); x++) {
                    for (int y = y_pixelMatrix - (sizeMatrix / 2); y <= y_pixelMatrix + (sizeMatrix / 2); y++) {
                        red+=Color.red(pixels[x+y*width]);
                        green+=Color.green(pixels[x+y*width]);
                        blue+=Color.blue(pixels[x+y*width]);
                    }
                }
            }

            red/=(sizeMatrix*sizeMatrix);
            green/=(sizeMatrix*sizeMatrix);
            blue/=(sizeMatrix*sizeMatrix);
            pixelMoyenneur[i] = Color.rgb(red, green, blue);
        }

        bmp.setPixels(pixelMoyenneur, 0, width, 0, 0, width, height);
        return bmp;
    }

    static Bitmap medianConvulation(int sizeMatrix, Bitmap bmp){
        int width = bmp.getWidth();
        int height = bmp.getHeight();
        int[] pixels = new int[width * height];
        int[] pixelMoyenneur = new int[width * height];
        bmp.getPixels(pixels, 0, width, 0, 0, width, height);
        int[] red= new int[sizeMatrix*sizeMatrix];
        int[] blue= new int[sizeMatrix*sizeMatrix];
        int[] green= new int[sizeMatrix*sizeMatrix];
        int x_pixelMatrix, y_pixelMatrix;
        for(int i = 0; i < pixelMoyenneur.length; i++)
        {
            x_pixelMatrix=i%width;
            y_pixelMatrix=i/width;

            if(i <= width*(sizeMatrix/2) || i >= width * (height-(sizeMatrix/2)) || i % width < sizeMatrix/2  || i % width >= width-(sizeMatrix/2))
            {
                red[0] = Color.red(pixels[i]);
                green[0] = Color.green(pixels[i]);
                blue[0] = Color.blue(pixels[i]);
            }
            else {
                int index = 0;
                for (int x = x_pixelMatrix -(sizeMatrix / 2); x <= x_pixelMatrix +(sizeMatrix / 2); x++) {
                    for (int y = y_pixelMatrix - (sizeMatrix / 2); y <= y_pixelMatrix + (sizeMatrix / 2); y++) {
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
            pixelMoyenneur[i] = Color.rgb(red[(sizeMatrix/2 + 1)], green[(sizeMatrix/2 + 1)], blue[(sizeMatrix/2 + 1)]);
        }

        bmp.setPixels(pixelMoyenneur, 0, width, 0, 0, width, height);
        return bmp;
    }

    static Bitmap laplcianConvolution(int[][] matrix, int sizeMatrix, Bitmap bmp){
        int width = bmp.getWidth();
        int height = bmp.getHeight();
        int[] pixels = new int[width * height];
        int[] laplacePixels = new int[width * height];
        bmp.getPixels(pixels, 0, width, 0, 0, width, height);
        int[] red= new int[sizeMatrix*sizeMatrix];
        int[] blue= new int[sizeMatrix*sizeMatrix];
        int[] green= new int[sizeMatrix*sizeMatrix];
        int x_pixelMatrix, y_pixelMatrix;
        for(int i = 0; i < laplacePixels.length; i++)
        {
            x_pixelMatrix=i%width;
            y_pixelMatrix=i/width;

            if(i <= width*(sizeMatrix/2) || i >= width * (height-(sizeMatrix/2)) || i % width < sizeMatrix/2  || i % width >= width-(sizeMatrix/2))
            {
                red[0] = Color.red(pixels[i]);
                green[0] = Color.green(pixels[i]);
                blue[0] = Color.blue(pixels[i]);
            }
            else {
                int index = 0;
                int laplaceX = 0, laplaceY = 0;
                for (int x = x_pixelMatrix -(sizeMatrix / 2); x <= x_pixelMatrix +(sizeMatrix / 2); x++) {
                    for (int y = y_pixelMatrix - (sizeMatrix / 2); y <= y_pixelMatrix + (sizeMatrix / 2); y++) {
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

            laplacePixels[i] = Color.rgb(finalRed,finalGreen,finalBlue);
        }

        bmp.setPixels(laplacePixels, 0, width, 0, 0, width, height);
        return bmp;
    }
}
