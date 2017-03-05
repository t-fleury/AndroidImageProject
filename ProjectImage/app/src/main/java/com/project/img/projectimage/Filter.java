package com.project.img.projectimage;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

import java.util.Arrays;
import static android.graphics.Color.HSVToColor;
import static android.graphics.Color.RGBToHSV;

abstract class Filter{

    final static private int[][] LAPLACE_FILTER1 = {{0, 1, 0},
                                                    {1,-4, 1},
                                                    {0, 1, 0}};
    final static private int[][] LAPLACE_FILTER2 = {{1, 1, 1},
                                                    {1,-8, 1},
                                                    {1, 1, 1}};

    final static private int[][] SOBEL_X_FILTER = {{-1,0,1},
                                                   {-2,0,2},
                                                   {-1,0,1}};

    final static private int[][] SOBEL_Y_FILTER = {{-1,-2,-1},
                                                   { 0, 0, 0},
                                                   {-1,-2,-1}};

    private static Bitmap checkMutable(Bitmap bmp){
        if(!bmp.isMutable()){
            bmp = bmp.copy(bmp.getConfig(),true);
        }
        return bmp;
    }

    static Bitmap changeLuminosity(Bitmap bmp, int percentage){
        bmp = checkMutable(bmp);
        int width = bmp.getWidth();
        int height = bmp.getHeight();
        int[] pixels = new int[width * height];
        bmp.getPixels(pixels, 0, width, 0, 0, width, height);
        for (int i = 0; i < pixels.length; i++) {
            int o = pixels[i];
            int blue = Color.blue(o) + percentage;
            if(blue>255)
                blue=255;
            if(blue < 0)
                blue = 0;

            int red = Color.red(o) + percentage;
            if(red>255)
                red=255;
            if(red < 0)
                red = 0;

            int green = Color.green(o) + percentage;
            if(green>255)
                green=255;
            if(green < 0)
                green = 0;

            pixels[i] = Color.rgb(red, green, blue);
        }
        bmp.setPixels(pixels, 0, width, 0, 0, width, height);
        return bmp;
    }

    static Bitmap changeContrast(Bitmap bmp, int percentage){
        bmp = checkMutable(bmp);
        int width = bmp.getWidth();
        int height = bmp.getHeight();
        int[] pixels = new int[width * height];
        bmp.getPixels(pixels, 0, width, 0, 0, width, height);
        for (int i = 0; i < pixels.length; i++) {
            //newRed   = Truncate(factor * (Red(colour)   - 128) + 128);
            //newGreen = Truncate(factor * (Green(colour) - 128) + 128);
            //newBlue  = Truncate(factor * (Blue(colour)  - 128) + 128);
            int o = pixels[i];
            int blue = Color.blue(o) + percentage;
            if(blue>255)
                blue=255;
            if(blue < 0)
                blue = 0;

            int red = Color.red(o) + percentage;
            if(red>255)
                red=255;
            if(red < 0)
                red = 0;

            int green = Color.green(o) + percentage;
            if(green>255)
                green=255;
            if(green < 0)
                green = 0;

            pixels[i] = Color.rgb(red, green, blue);
        }
        bmp.setPixels(pixels, 0, width, 0, 0, width, height);
        return bmp;
    }

    static Bitmap toGray(Bitmap bmp) {
        bmp = checkMutable(bmp);
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
        bmp = checkMutable(bmp);
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
        bmp = checkMutable(bmp);
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
        bmp = checkMutable(bmp);
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

    static Bitmap colorFilter(Bitmap bmp, int option) {
        bmp = checkMutable(bmp);
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

    static Bitmap egalizationHistogram(Bitmap bmp) {
        int size = bmp.getWidth() * bmp.getHeight();
        int pixelsColor[] = new int[size];
        int pixelsColorRGB[][] = new int[size][3];
        int pixelsColorNew[] = new int[size];
        int pixelsShadesOfGrey[] = new int[size];
        int histogramShadesOfGrey[] = new int[256];
        int cumulateHistogramShadesOfGrey[] = new int[256];
        int tempColor;
        int red, green, blue;

        bmp.getPixels(pixelsColor, 0, bmp.getWidth(), 0, 0, bmp.getWidth(), bmp.getHeight());

        for (int i = 0; i < size; i++) {
            pixelsColorRGB[i][0] = Color.red(pixelsColor[i]);
            pixelsColorRGB[i][1] = Color.green(pixelsColor[i]);
            pixelsColorRGB[i][2] = Color.blue(pixelsColor[i]);
        }

        Bitmap greyImage = toGray(bmp);
        greyImage.getPixels(pixelsShadesOfGrey, 0, greyImage.getWidth(), 0, 0, greyImage.getWidth(), greyImage.getHeight());

        for (int i = 0; i <= 255; i++) {
            histogramShadesOfGrey[i] = 0;
        }

        for (int i = 0; i < size; i++) {
            tempColor = Color.red(pixelsShadesOfGrey[i]);
            histogramShadesOfGrey[tempColor]++;
        }

        cumulateHistogramShadesOfGrey[0] = histogramShadesOfGrey[0];

        for (int i = 1; i <= 255; i++) {
            cumulateHistogramShadesOfGrey[i] = cumulateHistogramShadesOfGrey[i - 1] + histogramShadesOfGrey[i];
        }

        for (int i = 0; i < size; i++) {
            red = (cumulateHistogramShadesOfGrey[pixelsColorRGB[i][0]] * 255) / size;
            green = (cumulateHistogramShadesOfGrey[pixelsColorRGB[i][1]] * 255) / size;
            blue = (cumulateHistogramShadesOfGrey[pixelsColorRGB[i][2]] * 255) / size;
            pixelsColorNew[i] = Color.rgb(red, green, blue);
        }

        bmp.setPixels(pixelsColorNew, 0, bmp.getWidth(), 0, 0, bmp.getWidth(), bmp.getHeight());
        return bmp;
    }

    static Bitmap meanConvolution(int sizeMatrix, Bitmap bmp){
        bmp = checkMutable(bmp);
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

    static Bitmap medianConvolution(int sizeMatrix, Bitmap bmp){
        bmp = checkMutable(bmp);
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

    static Bitmap laplacianConvolution(int choiceMatrix, Bitmap bmp){
        bmp = checkMutable(bmp);
        int[][] matrix;
        if(choiceMatrix == 1){
            matrix = LAPLACE_FILTER1;
        }else{
            matrix = LAPLACE_FILTER2;
        }
        int width = bmp.getWidth();
        int height = bmp.getHeight();
        int[] pixels = new int[width * height];
        int[] laplacePixels = new int[width * height];
        bmp.getPixels(pixels, 0, width, 0, 0, width, height);
        int red = 0,blue = 0, green = 0;
        int x_pixelMatrix, y_pixelMatrix;
        for(int i = 0; i < laplacePixels.length; i++)
        {
            x_pixelMatrix=i%width;
            y_pixelMatrix=i/width;

            if(i <= width || i >= width * (height-(3/2)) || i % width < 3/2  || i % width >= width-(3/2))
            {
                red = Color.red(pixels[i]);
                green = Color.green(pixels[i]);
                blue = Color.blue(pixels[i]);
            }
            else {
                int laplaceX = 0, laplaceY = 0;
                for (int x = x_pixelMatrix -(3 / 2); x <= x_pixelMatrix +(3 / 2); x++) {
                    for (int y = y_pixelMatrix - (3 / 2); y <= y_pixelMatrix + (3 / 2); y++) {
                        red += Color.red(pixels[x+y*width])*matrix[laplaceX][laplaceY];
                        green += Color.green(pixels[x+y*width])*matrix[laplaceX][laplaceY];
                        blue += Color.blue(pixels[x+y*width])*matrix[laplaceX][laplaceY];
                        laplaceY++;
                        if(laplaceY == 3){
                            laplaceX++;
                            laplaceY = 0;
                        }
                    }
                }
            }

            laplacePixels[i] = Color.rgb(red,green,blue);
        }

        bmp.setPixels(laplacePixels, 0, width, 0, 0, width, height);
        return bmp;
    }

    private static double[][] generateGaussianMatrix(int matrixSize, double sigma){
        double[][] gaussianMatrix = new double[matrixSize][matrixSize];

        double sum = 0.0;
        int shift = matrixSize / 2;
        for (int x = -shift; x <= shift; x++) {
            for (int y = -shift; y <= shift; y++) {
                gaussianMatrix[x + shift][y + shift] = (Math.exp((-(Math.pow(x, 2) + Math.pow(y, 2))) / (2 * Math.pow(sigma,2)))) / (2*Math.PI*Math.pow(sigma,2));
                sum += gaussianMatrix[x + shift][y + shift];
            }
        }
        for (int i = 0; i < matrixSize; ++i) {
            for (int j = 0; j < matrixSize; ++j) {
                gaussianMatrix[i][j] /= sum;
                System.out.println(gaussianMatrix[i][j]);
            }
        }
        return gaussianMatrix;
    }

    static Bitmap gaussianConvolution(int sizeMatrix, Bitmap bmp){
        bmp = checkMutable(bmp);
        int width = bmp.getWidth();
        int height = bmp.getHeight();
        double[][] matrix = generateGaussianMatrix(sizeMatrix, 1.0);
        int[] pixels = new int[width * height];
        int[] gaussianPixels = new int[width * height];
        bmp.getPixels(pixels, 0, width, 0, 0, width, height);
        int red = 0, blue = 0, green = 0;
        int x_pixelMatrix, y_pixelMatrix;
        for(int i = 0; i < gaussianPixels.length; i++) {
            x_pixelMatrix=i%width;
            y_pixelMatrix=i/width;

            if(i <= width || i >= width * (height-(3/2)) || i % width < 3/2  || i % width >= width-(3/2)) {
                red = Color.red(pixels[i]);
                green = Color.green(pixels[i]);
                blue = Color.blue(pixels[i]);
            }
            else {
                int gaussianX = 0, gaussianY = 0;
                for (int x = x_pixelMatrix - (3 / 2); x <= x_pixelMatrix + (3 / 2); x++) {
                    for (int y = y_pixelMatrix - (3 / 2); y <= y_pixelMatrix + (3 / 2); y++) {
                        red += Color.red(pixels[x + y * width]) * matrix[gaussianX][gaussianY];
                        green += Color.green(pixels[x + y * width]) * matrix[gaussianX][gaussianY];
                        blue += Color.blue(pixels[x + y * width]) * matrix[gaussianX][gaussianY];
                        gaussianY++;
                        if (gaussianY == 3) {
                            gaussianX++;
                            gaussianY = 0;
                        }
                    }
                }
            }

            gaussianPixels[i] = Color.rgb(red,green,blue);
        }

        bmp.setPixels(gaussianPixels, 0, width, 0, 0, width, height);
        return bmp;
    }

    static Bitmap sobelConvolution(Bitmap bmp){
        bmp = checkMutable(bmp);
        int width = bmp.getWidth();
        int height = bmp.getHeight();
        int[] pixels = new int[width * height];
        int[] sobelPixels = new int[width * height];
        bmp.getPixels(pixels, 0, width, 0, 0, width, height);
        int red = 0, blue = 0, green = 0;
        int x_pixelMatrix, y_pixelMatrix;
        for(int i = 0; i < sobelPixels.length; i++) {
            x_pixelMatrix=i%width;
            y_pixelMatrix=i/width;

            if(i <= width || i >= width * (height-(3/2)) || i % width < 3/2  || i % width >= width-(3/2)) {
                red = Color.red(pixels[i]);
                green = Color.green(pixels[i]);
                blue = Color.blue(pixels[i]);
            }
            else {
                int sobelI = 0;
                int[][] matrix = SOBEL_X_FILTER;
                while (sobelI < 2) {
                    int sobelX = 0, sobelY = 0;
                    for (int x = x_pixelMatrix - (3 / 2); x <= x_pixelMatrix + (3 / 2); x++) {
                        for (int y = y_pixelMatrix - (3 / 2); y <= y_pixelMatrix + (3 / 2); y++) {
                            red += Color.red(pixels[x + y * width]) * matrix[sobelX][sobelY];
                            green += Color.green(pixels[x + y * width]) * matrix[sobelX][sobelY];
                            blue += Color.blue(pixels[x + y * width]) * matrix[sobelX][sobelY];
                            sobelY++;
                            if (sobelY == 3) {
                                sobelX++;
                                sobelY = 0;
                            }
                        }
                    }
                    sobelI++;
                    matrix = SOBEL_Y_FILTER;
                }
            }
            sobelPixels[i] = Color.rgb(red,green,blue);
        }

        bmp.setPixels(sobelPixels, 0, width, 0, 0, width, height);
        return bmp;
    }
}
