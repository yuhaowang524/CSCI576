//CSCI 576 Assignment 2
//Name: Yuhao Wang
//Student_ID: 5779881695


import java.awt.*;
import java.awt.image.BufferedImage;


public class rgbData {
    private final int height;
    private final int width;
    private rgbPixel[][] data;


    public class rgbPixel {
        private double red;
        private double green;
        private double blue;


        public rgbPixel(double red, double green, double blue) {
            this.red = red;
            this.green = green;
            this.blue = blue;
        }


        public rgbPixel(int pix) {
            this.red = (pix >> 16) & 0xff;
            this.green = (pix >> 8) & 0xff;
            this.blue = (pix & 255);
        }


        public double[] getRGBArray() {
            double[] rgbArray = new double[3];
            rgbArray[0] = this.red;
            rgbArray[1] = this.green;
            rgbArray[2] = this.blue;
            return rgbArray;
        }
    }


    public rgbData(int width, int height) {
        this.width = width;
        this.height = height;
        this.data = new rgbPixel[width][height];
    }


    public void setRGB(int width, int height, double[] rgb) {
        this.data[width][height] = new rgbPixel(rgb[0], rgb[1], rgb[2]);
    }


    public void setIntRGB(int width, int height, int pix) {
        this.data[width][height] = new rgbPixel(pix);
    }


    public rgbData replaceGreenToWhite() {
        rgbData ret = new rgbData(width, height);
        float[] hsbVals;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int red = (int) data[j][i].red;
                int green = (int) data[j][i].green;
                int blue = (int) data[j][i].blue;
                hsbVals = Color.RGBtoHSB(red, green, blue, null);

                if (hsbVals[0] * 360 > 66 && hsbVals[0] * 360 < 168 && hsbVals[1] >= 0.4 && hsbVals[2] >= 0.3) {
                    data[j][i].red = 127;
                    data[j][i].green = 127;
                    data[j][i].blue = 127;
                }
                double[] rgbArray = data[j][i].getRGBArray();
                ret.setRGB(j, i, rgbArray);
            }
        }
        return ret;
    }


    public rgbData reduceGreenFurther() {
        rgbData ret = new rgbData(width, height);
        float[] hsbVals;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int red = (int) data[j][i].red;
                int green = (int) data[j][i].green;
                int blue = (int) data[j][i].blue;
                hsbVals = Color.RGBtoHSB(red, green, blue, null);

                if (hsbVals[0] * 360 > 66 && hsbVals[0] * 360 < 168 && hsbVals[1] >= 0.11 && hsbVals[2] >= 0.15) {
                    if (red * blue != 0 && (float) (green * green) / (red * blue) > 1.5) {
                        data[j][i].red = data[j][i].red * 1.4;
                        data[j][i].blue = data[j][i].blue * 1.4;
                    } else {
                        data[j][i].red = data[j][i].red * 1.2;
                        data[j][i].blue = data[j][i].blue * 1.2;
                    }
                }
                double[] rgbArray = data[j][i].getRGBArray();
                ret.setRGB(j, i, rgbArray);
            }
        }
        return ret;
    }


    public rgbData replaceWhiteToBack(rgbData background) {
        rgbData ret = new rgbData(width, height);
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int red = (int) data[j][i].red;
                int green = (int) data[j][i].green;
                int blue = (int) data[j][i].blue;
                if (red == 127 && green == 127 && blue == 127) {
                    data[j][i].red = background.data[j][i].red;
                    data[j][i].green = background.data[j][i].green;
                    data[j][i].blue = background.data[j][i].blue;
                }
                double[] rgbArray = data[j][i].getRGBArray();
                ret.setRGB(j, i, rgbArray);
            }
        }
        return ret;
    }


    public rgbData compareStaticPixel(rgbData frame2) {
        rgbData ret = new rgbData(width, height);
        float[] frame1HsbVals;
        float[] frame2HsbVals;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int frame1R = (int) data[j][i].red;
                int frame1G = (int) data[j][i].green;
                int frame1B = (int) data[j][i].blue;
                frame1HsbVals = Color.RGBtoHSB(frame1R, frame1G, frame1B, null);

                int frame2R = (int) frame2.data[j][i].red;
                int frame2G = (int) frame2.data[j][i].green;
                int frame2B = (int) frame2.data[j][i].blue;
                frame2HsbVals = Color.RGBtoHSB(frame2R, frame2G, frame2B, null);

                // set frame1 position (j, i) pixel to green
                if (frame1HsbVals[0] == frame2HsbVals[0] && frame1HsbVals[1] == frame2HsbVals[1] &&
                        frame1HsbVals[2] == frame2HsbVals[2]) {
                    data[j][i].red = 0;
                    data[j][i].green = 255;
                    data[j][i].blue = 0;
                }
                double[] rgbArray = data[j][i].getRGBArray();
                ret.setRGB(j, i, rgbArray);
            }
        }
        return ret;
    }


    public BufferedImage toBufferedImage() {
        BufferedImage ret = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int red = (int) data[j][i].red;
                int green = (int) data[j][i].green;
                int blue = (int) data[j][i].blue;
                int pix = 0xff000000 | ((red & 0xff) << 16) | ((green & 0xff) << 8) | (blue & 0xff);
                ret.setRGB(j, i, pix);
            }
        }
        return ret;
    }
}
