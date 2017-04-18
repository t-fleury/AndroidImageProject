package com.project.img.projectimage.Filter;

import android.graphics.Bitmap;
import android.graphics.Color;

import java.util.Arrays;

public abstract class Filter {

    final static private double[][] LAPLACE_FILTER1 = {{0, 1, 0},{1, -4, 1},{0, 1, 0}};
    final static private double[][] LAPLACE_FILTER2 = {{1, 1, 1},{1, -8, 1},{1, 1, 1}};

    final static private double[][] SOBEL_X_FILTER = {{-1, 0, 1}, {-2, 0, 2}, {-1, 0, 1}};
    final static private double[][] SOBEL_Y_FILTER = {{-1, -2, -1},{0, 0, 0},{-1, -2, -1}};

    private static Bitmap checkMutable(Bitmap bmp) {
        if (!bmp.isMutable()) {
            bmp = bmp.copy(bmp.getConfig(), true);
        }
        return bmp;
    }

    private static double standardization(double val, double coeffMin, double coeffMax) {
        return 255 * ((val - coeffMin) / (coeffMax - coeffMin));
    }

    private static double inColor(double value) {
        if (value > 255) {
            return 255;
        } else if (value < 0) {
            return 0;
        } else {
            return value;
        }
    }

    public static Bitmap changeLuminosity(Bitmap bmp, int percentage) {
        bmp = checkMutable(bmp);
        int width = bmp.getWidth();
        int height = bmp.getHeight();
        int[] pixels = new int[width * height];
        bmp.getPixels(pixels, 0, width, 0, 0, width, height);
        for (int i = 0; i < pixels.length; i++) {
            int o = pixels[i];
            int blue = (int) inColor(Color.blue(o) + percentage);
            int red = (int) inColor(Color.red(o) + percentage);
            int green = (int) inColor(Color.green(o) + percentage);

            pixels[i] = Color.rgb(red, green, blue);
        }
        bmp.setPixels(pixels, 0, width, 0, 0, width, height);
        return bmp;
    }

    public static Bitmap changeContrast(Bitmap bmp, int percentage) {
        return bmp;
    }

    public static Bitmap toShadeofGray(Bitmap bmp) {
        bmp = checkMutable(bmp);
        int width = bmp.getWidth();
        int height = bmp.getHeight();
        int[] pixels = new int[width * height];
        bmp.getPixels(pixels, 0, width, 0, 0, width, height);

        for (int i = 0; i < pixels.length; i++) {
            int r = ((Color.red(pixels[i]) * 3) / 10);
            int g = ((Color.green(pixels[i]) * 59) / 100);
            int b = ((Color.blue(pixels[i]) * 11) / 100);
            int total = b + r + g;

            pixels[i] = Color.rgb(total, total, total);
        }
        bmp.setPixels(pixels, 0, width, 0, 0, width, height);
        return bmp;
    }

    public static Bitmap toSepia(Bitmap bmp) {
        bmp = checkMutable(bmp);
        int width = bmp.getWidth();
        int height = bmp.getHeight();
        int[] pixels = new int[width * height];
        bmp.getPixels(pixels, 0, width, 0, 0, width, height);

        for (int i = 0; i < pixels.length; i++) {
            int blue = Color.blue(pixels[i]);
            int red = Color.red(pixels[i]);
            int green = Color.green(pixels[i]);

            int finalRed = (int) inColor((red * 393 / 1000) + (green * 769 / 1000) + (blue * 189 / 1000));
            int finalGreen = (int) inColor((red * 349 / 1000) + (green * 686 / 1000) + (blue * 168 / 1000));
            int finalBlue = (int) inColor((red * 272 / 1000) + (green * 534 / 1000) + (blue * 131 / 1000));

            pixels[i] = Color.rgb(finalRed, finalGreen, finalBlue);
        }
        bmp.setPixels(pixels, 0, width, 0, 0, width, height);
        return bmp;
    }

    public static Bitmap toColor(Bitmap bmp, int color) {
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

    public static Bitmap colorFilter(Bitmap bmp, int option) {
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
            if (HSV[0] < option - 30 || HSV[0] > option + 30) {
                int total = blue + red + green;
                pixels[i] = Color.rgb(total, total, total);
            }
        }
        bmp.setPixels(pixels, 0, width, 0, 0, width, height);
        return bmp;
    }

    public static Bitmap egalizationHistogram(Bitmap bmp) {
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

        Bitmap greyImage = toShadeofGray(bmp);
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

    public static Bitmap meanConvolution(int sizeMatrix, Bitmap bmp) {
        int width = bmp.getWidth();
        int height = bmp.getHeight();
        double[][] meanMatrix = new double[sizeMatrix][sizeMatrix];
        for (int i = 0; i < sizeMatrix; i++) {
            for (int j = 0; j < sizeMatrix; j++) {
                meanMatrix[i][j] = 1 / (Math.pow(sizeMatrix, 2));
            }
        }
        bmp.setPixels(convolution(meanMatrix, sizeMatrix, bmp), 0, width, 0, 0, width, height);
        return bmp;
    }

    public static Bitmap laplacianConvolution(int choiceMatrix, Bitmap bmp) {
        int width = bmp.getWidth();
        int height = bmp.getHeight();
        int[] finalLaplacian;
        int limit4CX = 4*255;
        int limit8CX = 8*255;
        if (choiceMatrix == 1) {
            finalLaplacian = convolutionV2(LAPLACE_FILTER1, 3, bmp, -limit4CX, limit4CX);
        } else {
            finalLaplacian = convolutionV2(LAPLACE_FILTER2, 3, bmp, -limit8CX, limit8CX);
        }
        bmp.setPixels(finalLaplacian, 0, width, 0, 0, width, height);
        return bmp;
    }

    public static Bitmap gaussianConvolution(int matrixSize, double sigma, Bitmap bmp) {
        double[][] gaussianMatrix = new double[matrixSize][matrixSize];
        double sum = 0.0;
        int shift = matrixSize / 2;
        for (int x = -shift; x <= shift; x++) {
            for (int y = -shift; y <= shift; y++) {
                gaussianMatrix[x + shift][y + shift] = (Math.exp((-(Math.pow(x, 2) + Math.pow(y, 2))) / (2 * Math.pow(sigma, 2)))) / (2 * Math.PI * Math.pow(sigma, 2));
                sum += gaussianMatrix[x + shift][y + shift];
            }
        }
        for (int i = 0; i < matrixSize; ++i) {
            for (int j = 0; j < matrixSize; ++j) {
                gaussianMatrix[i][j] /= sum;
            }
        }
        int width = bmp.getWidth();
        int height = bmp.getHeight();
        bmp.setPixels(convolution(gaussianMatrix, matrixSize, bmp), 0, width, 0, 0, width, height);
        return bmp;
    }

    public static Bitmap sobelConvolution(Bitmap bmp) {
        int width = bmp.getWidth();
        int height = bmp.getHeight();
        int limitSobel = 4*255;
        int[] sobelX = convolutionV2(SOBEL_X_FILTER, 3, bmp, -limitSobel, limitSobel);
        int[] sobelY = convolutionV2(SOBEL_Y_FILTER, 3, bmp, -limitSobel, limitSobel);
        int[] finalSobel = new int[width * height];
        for (int i = 0; i < width * height; i++) {
            finalSobel[i] = (int) Math.sqrt(sobelX[i] * sobelX[i] + sobelY[i] * sobelY[i]);
        }
        bmp.setPixels(finalSobel, 0, width, 0, 0, width, height);
        return bmp;
    }

    private static int[] convolution(double[][] matrix, int length, Bitmap bmp) {
        int width = bmp.getWidth();
        int height = bmp.getHeight();
        int[] pixels = new int[width * height];
        bmp.getPixels(pixels, 0, width, 0, 0, width, height);
        int[] newPixels = new int[width * height];
        int red, blue, green, x_pixelMatrix, y_pixelMatrix;
        double redF, blueF, greenF;
        for (int i = 0; i < pixels.length; i++) {
            redF = blueF = greenF = 0;
            x_pixelMatrix = i % width;
            y_pixelMatrix = i / width;

            if (i <= width * (length / 2) || i >= width * (height - (length / 2)) || i % width < length / 2 || i % width >= width - (length / 2)) {
                redF = Color.red(pixels[i]);
                greenF = Color.green(pixels[i]);
                blueF = Color.blue(pixels[i]);
            } else {
                int cptI = 0;
                int cptJ = 0;
                for (int x = x_pixelMatrix - (length / 2); x <= x_pixelMatrix + (length / 2); x++) {
                    for (int y = y_pixelMatrix - (length / 2); y <= y_pixelMatrix + (length / 2); y++) {
                        redF += Color.red(pixels[x + y * width]) * matrix[cptI][cptJ];
                        greenF += Color.green(pixels[x + y * width]) * matrix[cptI][cptJ];
                        blueF += Color.blue(pixels[x + y * width]) * matrix[cptI][cptJ];

                        cptJ++;
                    }
                    cptI++;
                    cptJ = 0;
                }
            }
            red = (int) inColor(redF);
            green = (int) inColor(greenF);
            blue = (int) inColor(blueF);
            newPixels[i] = Color.rgb(red, green, blue);
        }
        return newPixels;
    }

    private static int[] convolutionV2(double[][] matrix, int length, Bitmap bmp, double coeffMin, double coeffMax) {
        int width = bmp.getWidth();
        int height = bmp.getHeight();
        int[] pixels = new int[width * height];
        bmp.getPixels(pixels, 0, width, 0, 0, width, height);
        int[] newPixels = new int[width * height];
        int red, blue, green, x_pixelMatrix, y_pixelMatrix;
        double redF, blueF, greenF;
        for (int i = 0; i < pixels.length; i++) {
            redF = blueF = greenF = 0;
            x_pixelMatrix = i % width;
            y_pixelMatrix = i / width;

            if (i <= width * (length / 2) || i >= width * (height - (length / 2)) || i % width < length / 2 || i % width >= width - (length / 2)) {
                redF = Color.red(pixels[i]);
                greenF = Color.green(pixels[i]);
                blueF = Color.blue(pixels[i]);
            } else {
                int cptI = 0;
                int cptJ = 0;
                for (int x = x_pixelMatrix - (length / 2); x <= x_pixelMatrix + (length / 2); x++) {
                    for (int y = y_pixelMatrix - (length / 2); y <= y_pixelMatrix + (length / 2); y++) {
                        redF += Color.red(pixels[x + y * width]) * matrix[cptI][cptJ];
                        greenF += Color.green(pixels[x + y * width]) * matrix[cptI][cptJ];
                        blueF += Color.blue(pixels[x + y * width]) * matrix[cptI][cptJ];

                        cptJ++;
                    }
                    cptI++;
                    cptJ = 0;
                }
            }
            red = (int) standardization(redF, coeffMin, coeffMax);
            green = (int) standardization(greenF, coeffMin, coeffMax);
            blue = (int) standardization(blueF, coeffMin, coeffMax);
            newPixels[i] = Color.rgb(red, green, blue);
        }
        return newPixels;
    }

    public static Bitmap medianConvolution(int sizeMatrix, Bitmap bmp) {
        bmp = checkMutable(bmp);
        int width = bmp.getWidth();
        int height = bmp.getHeight();
        int[] pixels = new int[width * height];
        int[] pixelMoyenneur = new int[width * height];
        bmp.getPixels(pixels, 0, width, 0, 0, width, height);
        int[] red = new int[sizeMatrix * sizeMatrix];
        int[] blue = new int[sizeMatrix * sizeMatrix];
        int[] green = new int[sizeMatrix * sizeMatrix];
        int x_pixelMatrix, y_pixelMatrix;
        for (int i = 0; i < pixelMoyenneur.length; i++) {
            x_pixelMatrix = i % width;
            y_pixelMatrix = i / width;

            if (i <= width * (sizeMatrix / 2) || i >= width * (height - (sizeMatrix / 2)) || i % width < sizeMatrix / 2 || i % width >= width - (sizeMatrix / 2)) {
                red[0] = Color.red(pixels[i]);
                green[0] = Color.green(pixels[i]);
                blue[0] = Color.blue(pixels[i]);
            } else {
                int index = 0;
                for (int x = x_pixelMatrix - (sizeMatrix / 2); x <= x_pixelMatrix + (sizeMatrix / 2); x++) {
                    for (int y = y_pixelMatrix - (sizeMatrix / 2); y <= y_pixelMatrix + (sizeMatrix / 2); y++) {
                        red[index] = Color.red(pixels[x + y * width]);
                        green[index] = Color.green(pixels[x + y * width]);
                        blue[index] = Color.blue(pixels[x + y * width]);
                        index++;
                    }
                }
            }
            Arrays.sort(red);
            Arrays.sort(green);
            Arrays.sort(blue);
            pixelMoyenneur[i] = Color.rgb(red[(sizeMatrix / 2 + 1)], green[(sizeMatrix / 2 + 1)], blue[(sizeMatrix / 2 + 1)]);
        }

        bmp.setPixels(pixelMoyenneur, 0, width, 0, 0, width, height);
        return bmp;
    }

    private static Bitmap inverseColor(Bitmap bmp) {
        int[] pixels = new int[bmp.getWidth() * bmp.getHeight()];
        bmp.getPixels(pixels, 0, bmp.getWidth(), 0, 0, bmp.getWidth(), bmp.getHeight());
        for (int i = 0; i < pixels.length; i++) {
            int o = pixels[i];
            int blue = (int) inColor(254 - Color.blue(o));
            int red = (int) inColor(254 - Color.red(o));
            int green = (int) inColor(254 - Color.green(o));

            pixels[i] = Color.rgb(red, green, blue);
        }
        bmp.setPixels(pixels, 0, bmp.getWidth(), 0, 0, bmp.getWidth(), bmp.getHeight());
        return bmp;
    }

    public static Bitmap pencilEffect(Bitmap bmp) {
        bmp = sobelConvolution(bmp);
        bmp = inverseColor(bmp);
        bmp = gaussianConvolution(3, 1, bmp);
        return bmp;
    }
}
