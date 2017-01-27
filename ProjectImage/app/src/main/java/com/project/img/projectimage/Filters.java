package com.project.img.projectimage;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.widget.ImageView;

import static android.graphics.Color.HSVToColor;
import static android.graphics.Color.RGBToHSV;

/**
 * Created by Maxime on 27/01/2017.
 */

public class Filters {

    private Bitmap bmp, bmp_initial, bmp_cop;
    private ImageView img;
    public void toNormal() {
        bmp = bmp_initial.copy(bmp_initial.getConfig(), true);
        img.setImageBitmap(bmp_initial);
    }

    public void toGray() {
        int min = 255;
        int max = 0;
        int[] LUT = new int[256];
        for (int i = 0; i < bmp.getWidth(); i++) {
            for (int j = 0; j < bmp.getHeight(); j++) {
                int p = bmp.getPixel(i, j);
                int r = Color.red(p);
                int g = Color.green(p);
                int b = Color.blue(p);

                int newColor = (r * 30 / 100) + (g * 59 / 100) + (b * 11 / 100);
                if(min > newColor)
                    min = newColor;
                if(max < newColor)
                    max = newColor;
                int val = Color.rgb(newColor, newColor, newColor);

                bmp.setPixel(i, j, val);
            }
        }

        //Augmentation du contraste par extension de dynamique
        for(int grayLevel = 0; grayLevel < 256; grayLevel++) {
            LUT[grayLevel] = ((255 * (grayLevel - min)) / (max - min));
        }
        for (int i = 0; i < bmp.getWidth(); i++) {
            for (int j = 0; j < bmp.getHeight(); j++) {
                int p = bmp.getPixel(i, j);
                int val = LUT[Color.red(p)];
                if(val > 255)
                    val = 255;
                int val_2 = Color.rgb(val, val, val);
                bmp.setPixel(i, j, val_2);
            }
        }

        //Augmentation du contraste par égalisation d'histogramme
        int[] h = new int[256];
        int[] C = new int[256];
        for(int i = 0; i < h.length; i++) {
            h[i] = 0;
            C[i] = 0;
        }
        for(int i = 0; i < bmp.getWidth(); i++) {
            for(int j = 0; j < bmp.getHeight(); j++) {
                int p = bmp.getPixel(i, j);
                int val = Color.red(p);
                h[val]++;
            }
        }
        for(int i = 0; i < C.length; i++) {
            for(int j = 0; j < i; j++) {
                C[i] += h[j];
            }
        }
        for (int i = 0; i < bmp.getWidth(); i++) {
            for (int j = 0; j < bmp.getHeight(); j++) {
                int p = bmp.getPixel(i, j);
                int val = (C[Color.red(p)]*255) / (bmp.getWidth() * bmp.getHeight());
                if(val > 255)
                    val = 255;
                int val_2 = Color.rgb(val, val, val);
                bmp.setPixel(i, j, val_2);
            }
        }

        img.setImageBitmap(bmp);
    }

    public void toGray2() {
        int min = 255;
        int max = 0;
        int[] LUT = new int[256];
        int[] pixels = new int[bmp.getWidth() * bmp.getHeight()];
        bmp.getPixels(pixels, 0, bmp.getWidth(), 0, 0, bmp.getWidth(), bmp.getHeight());
        for (int i = 0; i < pixels.length; i++) {
            int o = pixels[i];
            int blue = Color.blue(o);
            int red = Color.red(o);
            int green = Color.green(o);

            int newColor = (red * 30 / 100) + (green * 59 / 100) + (blue * 11 / 100);
            if(min > newColor)
                min = newColor;
            if(max < newColor)
                max = newColor;

            pixels[i] = Color.rgb(newColor, newColor, newColor);

        }

        //Augmentation du contraste par extension de dynamique
        for(int ng = 0; ng < 256; ng++) {
            LUT[ng] = ((255 * (ng - min)) / (max - min));
        }
        for(int i = 0; i < pixels.length; i++) {
            int o = pixels[i];
            int val = LUT[Color.red(o)];
            if(val > 255)
                val = 255;
            int val_2 = Color.rgb(val, val, val);
            pixels[i] = val_2;
        }

        //Augmentation du contraste par égalisation d'histogramme
        int[] h = new int[256];
        int[] C = new int[256];
        for(int i = 0; i < h.length; i++) {
            h[i] = 0;
            C[i] = 0;
        }
        for(int i = 0; i < pixels.length; i++) {
            int o = pixels[i];
            int val = Color.red(o);
            h[val]++;
        }
        for(int i = 0; i < C.length; i++) {
            for(int j = 0; j < i; j++) {
                C[i] += h[j];
            }
        }
        for(int i = 0; i < pixels.length; i++) {
            int o = pixels[i];
            int val = (C[Color.red(o)]*255) / pixels.length;
            if(val > 255)
                val = 255;
            int val_2 = Color.rgb(val, val, val);
            pixels[i] = val_2;
        }


        bmp.setPixels(pixels, 0, bmp.getWidth(), 0, 0, bmp.getWidth(), bmp.getHeight());
        img.setImageBitmap(bmp);
    }

    public void toSepia() {
        int[] pixels = new int[bmp.getWidth() * bmp.getHeight()];
        bmp.getPixels(pixels, 0, bmp.getWidth(), 0, 0, bmp.getWidth(), bmp.getHeight());
        for (int i = 0; i < pixels.length; i++) {
            int o = pixels[i];
            int blue = Color.blue(o);
            int red = Color.red(o);
            int green = Color.green(o);

            int tmp_red=(red*393/1000)+(green*769/1000)+(blue*189/1000);
            int tmp_blue=(red*272/1000)+(green*534/1000)+(blue*131/1000);
            int tmp_green=(red*349/1000)+(green*686/1000)+(blue*168/1000);
            if(tmp_red>255)
                tmp_red=255;
            if(tmp_green>255)
                tmp_green=255;
            if(tmp_blue>255)
                tmp_blue=255;

            pixels[i] = Color.rgb(tmp_red, tmp_green, tmp_blue);
        }
        bmp.setPixels(pixels, 0, bmp.getWidth(), 0, 0, bmp.getWidth(), bmp.getHeight());
        img.setImageBitmap(bmp);
    }

    public void toTeinteAlea() {
        double rm=(Math.random())*360;
        int[] pixels = new int[bmp.getWidth() * bmp.getHeight()];
        bmp.getPixels(pixels, 0, bmp.getWidth(), 0, 0, bmp.getWidth(), bmp.getHeight());
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
        bmp.setPixels(pixels, 0, bmp.getWidth(), 0, 0, bmp.getWidth(), bmp.getHeight());
        img.setImageBitmap(bmp);
    }

    public void toFusion() {
        int[] pixels_image = new int[bmp.getWidth() * bmp.getHeight()];
        int[] pixels_cop = new int[bmp_cop.getWidth() * bmp_cop.getHeight()];
        bmp.getPixels(pixels_image, 0, bmp.getWidth(), 0, 0, bmp.getWidth(), bmp.getHeight());
        bmp_cop.getPixels(pixels_cop, 0, bmp_cop.getWidth(), 0, 0, bmp_cop.getWidth(), bmp_cop.getHeight());
        for(int i = 0; i < pixels_cop.length; i++) {
            if(Color.red(pixels_cop[i]) <20  && Color.green(pixels_cop[i]) < 20 && Color.blue(pixels_cop[i]) < 20) {
                int o = pixels_image[i];
                int blue = (Color.blue(o) / 3);
                int red = (Color.red(o) / 3);
                int green = (Color.green(o) / 3);

                int val = Color.rgb(red, green, blue);
                pixels_image[i] = val;
            }
        }
        bmp.setPixels(pixels_image, 0, bmp.getWidth(), 0, 0, bmp.getWidth(), bmp.getHeight());
        img.setImageBitmap(bmp);
    }
}
