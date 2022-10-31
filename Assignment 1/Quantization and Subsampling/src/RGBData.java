//CSCI576 HW1
//Name: Yuhao Wang
//Student_ID: 5779881695

import java.awt.image.*;

public class RGBData {
    private static final double[][] transferMatrix = {{0.299, 0.587, 0.114}, {0.596, -0.274, -0.322}, {0.211, -0.523, 0.312}};
    private final pixel[][] data;
    private final int height;
    private final int width;

    public RGBData(int height, int width) {
        this.height = height;
        this.width = width;
        this.data = new pixel[height][width];
    }

    public void setRGBArray(int h, int w, double[] RGBArray) {
        this.data[h][w] = new pixel(RGBArray[0], RGBArray[1], RGBArray[2]);
    }

    public void setRGBInt(int h, int w, int colorVal) {
        this.data[h][w] = new pixel(colorVal);
    }

    public YUVData convertToYUV() {
        YUVData ret = new YUVData(height, width);
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                double[] RGBArray = data[i][j].getColorArray();
                double[] dotProduct = new double[3];
                for (int x = 0; x < 3; x++) {
                    for (int y = 0; y < 3; y++) {
                        dotProduct[x] += transferMatrix[x][y] * RGBArray[y];
                    }
                }
                ret.setYUV(i, j, dotProduct);
            }
        }
        return ret;
    }

    public RGBData scaleRGB(float precisionSw, float precisionSh, int antialiasing) {
        float scaledHeight = height * precisionSh;
        float scaledWidth = width * precisionSw;
        RGBData ret = new RGBData((int) scaledHeight, (int) scaledWidth);
        for (int i = 0; i < (int) scaledHeight; i++) {
            for (int j = 0; j < (int) scaledWidth; j++) {
                ret.data[i][j] = new pixel();
                if (antialiasing == 0) {
                    float originX = i / precisionSh;
                    float originY = j / precisionSw;
                    if ((int) originX > height) {
                        originX = height;
                    }
                    if ((int) originY > width) {
                        originY = width;
                    }
                    ret.data[i][j] = data[(int) originX][(int) originY];
                } else {
                    if (i == 0 && j == 0) {
                        ret.data[i][j].colorOne = (data[i][j].colorOne + data[i][j + 1].colorOne +
                                data[i + 1][j].colorOne + data[i + 1][j + 1].colorOne) / 4;
                        ret.data[i][j].colorTwo = (data[i][j].colorTwo + data[i][j + 1].colorTwo +
                                data[i + 1][j].colorTwo + data[i + 1][j + 1].colorTwo) / 4;
                        ret.data[i][j].colorThree = (data[i][j].colorThree + data[i][j + 1].colorThree +
                                data[i + 1][j].colorThree + data[i + 1][j + 1].colorThree) / 4;
                    }
                    if (i == 0 && j > 0 && j < (int) scaledWidth - 1) {
                        int originY = (int) (j / precisionSw);
                        ret.data[i][j].colorOne = (data[i][originY - 1].colorOne + data[i][originY].colorOne +
                                data[i][originY + 1].colorOne + data[i + 1][originY - 1].colorOne +
                                data[i + 1][originY].colorOne + data[i + 1][originY + 1].colorOne) / 6;
                        ret.data[i][j].colorTwo = (data[i][originY - 1].colorTwo + data[i][originY].colorTwo +
                                data[i][originY + 1].colorTwo + data[i + 1][originY - 1].colorTwo +
                                data[i + 1][originY].colorTwo + data[i + 1][originY + 1].colorTwo) / 6;
                        ret.data[i][j].colorThree = (data[i][originY - 1].colorThree + data[i][originY].colorThree +
                                data[i][originY + 1].colorThree + data[i + 1][originY - 1].colorThree +
                                data[i + 1][originY].colorThree + data[i + 1][originY + 1].colorThree) / 6;
                    }
                    if (i == 0 && j == (int) scaledWidth - 1) {
                        int originY = (int) (j / precisionSw);
                        ret.data[i][j].colorOne = (data[i][originY].colorOne + data[i][originY - 1].colorOne +
                                data[i + 1][originY].colorOne + data[i + 1][originY - 1].colorOne) / 4;
                        ret.data[i][j].colorTwo = (data[i][originY].colorTwo + data[i][originY - 1].colorTwo +
                                data[i + 1][originY].colorTwo + data[i + 1][originY - 1].colorTwo) / 4;
                        ret.data[i][j].colorThree = (data[i][originY].colorThree + data[i][originY - 1].colorThree +
                                data[i + 1][originY].colorThree + data[i + 1][originY - 1].colorThree) / 4;
                    }
                    if (i > 0 && i < (int) scaledHeight - 1 && j == (int) scaledWidth - 1) {
                        int originX = (int) (i / precisionSh);
                        int originY = (int) (j / precisionSw);
                        ret.data[i][j].colorOne = (data[originX][originY - 1].colorOne + data[originX][originY].colorOne +
                                data[originX - 1][originY - 1].colorOne + data[originX - 1][originY].colorOne +
                                data[originX + 1][originY - 1].colorOne + data[originX + 1][originY].colorOne) / 6;
                        ret.data[i][j].colorTwo = (data[originX][originY - 1].colorTwo + data[originX][originY].colorTwo +
                                data[originX - 1][originY - 1].colorTwo + data[originX - 1][originY].colorTwo +
                                data[originX + 1][originY - 1].colorTwo + data[originX + 1][originY].colorTwo) / 6;
                        ret.data[i][j].colorThree = (data[originX][originY - 1].colorThree + data[originX][originY].colorThree +
                                data[originX - 1][originY - 1].colorThree + data[originX + 1][originY].colorThree +
                                data[originX + 1][originY - 1].colorThree + data[originX + 1][originY].colorThree) / 6;
                    }
                    if (i == (int) scaledHeight - 1 && j == (int) scaledWidth - 1) {
                        int originX = (int) (i / precisionSh);
                        int originY = (int) (j / precisionSw);
                        ret.data[i][j].colorOne = (data[originX][originY].colorOne + data[originX][originY - 1].colorOne +
                                data[originX - 1][originY].colorOne + data[originX - 1][originY - 1].colorOne) / 4;
                        ret.data[i][j].colorTwo = (data[originX][originY].colorTwo + data[originX][originY - 1].colorTwo +
                                data[originX - 1][originY].colorTwo + data[originX - 1][originY - 1].colorTwo) / 4;
                        ret.data[i][j].colorThree = (data[originX][originY].colorThree + data[originX][originY - 1].colorThree +
                                data[originX - 1][originY].colorThree + data[originX - 1][originY - 1].colorThree) / 4;
                    }
                    if (i == (int) scaledHeight - 1 && j > 0 && j < (int) scaledWidth - 1) {
                        int originX = (int) (i / precisionSh);
                        int originY = (int) (j / precisionSw);
                        ret.data[i][j].colorOne = (data[originX][originY - 1].colorOne + data[originX][originY].colorOne +
                                data[originX][originY + 1].colorOne + data[originX - 1][originY - 1].colorOne +
                                data[originX - 1][originY].colorOne + data[originX - 1][originY + 1].colorOne) / 6;
                        ret.data[i][j].colorTwo = (data[originX][originY - 1].colorTwo + data[originX][originY].colorTwo +
                                data[originX][originY + 1].colorTwo + data[originX - 1][originY - 1].colorTwo +
                                data[originX - 1][originY].colorTwo + data[originX - 1][originY + 1].colorTwo) / 6;
                        ret.data[i][j].colorThree = (data[originX][originY - 1].colorThree + data[originX][originY].colorThree +
                                data[originX][originY + 1].colorThree + data[originX - 1][originY - 1].colorThree +
                                data[originX - 1][originY].colorThree + data[originX - 1][originY + 1].colorThree) / 6;
                    }
                    if (i == (int) scaledHeight - 1 && j == 0) {
                        int originX = (int) (i / precisionSh);
                        ret.data[i][j].colorOne = (data[originX][j].colorOne + data[originX][j + 1].colorOne +
                                data[originX - 1][j].colorOne + data[originX - 1][j + 1].colorOne) / 4;
                        ret.data[i][j].colorTwo = (data[originX][j].colorTwo + data[originX][j + 1].colorTwo +
                                data[originX - 1][j].colorTwo + data[originX - 1][j + 1].colorTwo) / 4;
                        ret.data[i][j].colorThree = (data[originX][j].colorThree + data[originX][j + 1].colorThree +
                                data[originX - 1][j].colorThree + data[originX - 1][j + 1].colorThree) / 4;
                    }
                    if (i > 0 && i < (int) scaledHeight - 1 && j == 0) {
                        int originX = (int) (i / precisionSh);
                        ret.data[i][j].colorOne = (data[originX][j].colorOne + data[originX][j + 1].colorOne +
                                data[originX - 1][j].colorOne + data[originX - 1][j + 1].colorOne +
                                data[originX + 1][j].colorOne + data[originX + 1][j + 1].colorOne) / 6;
                        ret.data[i][j].colorTwo = (data[originX][j].colorTwo + data[originX][j + 1].colorTwo +
                                data[originX - 1][j].colorTwo + data[originX - 1][j + 1].colorTwo +
                                data[originX + 1][j].colorTwo + data[originX + 1][j + 1].colorTwo) / 6;
                        ret.data[i][j].colorThree = (data[originX][j].colorThree + data[originX][j + 1].colorThree +
                                data[originX - 1][j].colorThree + data[originX - 1][j + 1].colorThree +
                                data[originX + 1][j].colorThree + data[originX + 1][j + 1].colorThree) / 6;
                    } else {
                        int originX = (int) (i / precisionSh);
                        int originY = (int) (j / precisionSw);
                        if (originX <= 0) {
                            originX = 1;
                        }
                        if (originY <= 0) {
                            originY = 1;
                        }
                        if (originX >= height - 1) {
                            originX = height - 2;
                        }
                        if (originY >= width - 1) {
                            originY = width - 2;
                        }
                        ret.data[i][j].colorOne = (data[originX][originY].colorOne + data[originX][originY + 1].colorOne +
                                data[originX][originY - 1].colorOne + data[originX - 1][originY].colorOne +
                                data[originX - 1][originY + 1].colorOne + data[originX - 1][originY - 1].colorOne +
                                data[originX + 1][originY].colorOne + data[originX + 1][originY - 1].colorOne +
                                data[originX + 1][originY + 1].colorOne) / 9;
                        ret.data[i][j].colorTwo = (data[originX][originY].colorTwo + data[originX][originY + 1].colorTwo +
                                data[originX][originY - 1].colorTwo + data[originX - 1][originY].colorTwo +
                                data[originX - 1][originY + 1].colorTwo + data[originX - 1][originY - 1].colorTwo +
                                data[originX + 1][originY].colorTwo + data[originX + 1][originY - 1].colorTwo +
                                data[originX + 1][originY + 1].colorTwo) / 9;
                        ret.data[i][j].colorThree = (data[originX][originY].colorThree + data[originX][originY + 1].colorThree +
                                data[originX][originY - 1].colorThree + data[originX - 1][originY].colorThree +
                                data[originX - 1][originY + 1].colorThree + data[originX - 1][originY - 1].colorThree +
                                data[originX + 1][originY].colorThree + data[originX + 1][originY - 1].colorThree +
                                data[originX + 1][originY + 1].colorThree) / 9;
                    }
                }
            }
        }
        return ret;
    }


    public BufferedImage convertToBufferedImage() {
        BufferedImage ret = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int red = (int) data[i][j].colorOne;
                int green = (int) data[i][j].colorTwo;
                int blue = (int) data[i][j].colorThree;
                int rgbInt = 0xff000000 | ((red & 0xff) << 16) | ((green & 0xff) << 8) | (blue & 0xff);
                ret.setRGB(j, i, rgbInt);
            }
        }
        return ret;
    }
}
