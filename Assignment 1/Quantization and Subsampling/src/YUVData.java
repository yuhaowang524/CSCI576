//CSCI576 HW1
//Name: Yuhao Wang
//Student_ID: 5779881695

public class YUVData {
    private static final double[][] transferMaxtrix = {{1.000, 0.956, 0.621}, {1.000, -0.272, -0.647}, {1.000, -1.106, 1.703}};
    private final pixel[][] data;
    private final int height;
    private final int width;

    public YUVData(int height, int width) {
        this.height = height;
        this.width = width;
        this.data = new pixel[height][width];
    }

    public void setYUV(int height, int width, double[] yuv) {
        this.data[height][width] = new pixel(yuv[0], yuv[1], yuv[2]);
    }

    public YUVData subsampleAdjust(int Yscale, int Uscale, int Vscale) {
        YUVData ret = new YUVData(height, width);
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int Yoffset = j % Yscale;
                int Uoffset = j % Uscale;
                int Voffset = j % Vscale;
                ret.data[i][j] = new pixel();
                if (Yoffset == 0) {
                    ret.data[i][j].colorOne = data[i][j].colorOne;
                } else {
                    double neighborPixel1Y = data[i][j - Yoffset].colorOne;
                    double neighborPixel2Y;
                    if (j + Yoffset > width - 1) {
                        neighborPixel2Y = data[i][width - 1].colorOne;
                    } else {
                        neighborPixel2Y = data[i][j + Yoffset].colorOne;
                    }
                    ret.data[i][j].colorOne = (neighborPixel1Y + neighborPixel2Y) / 2;
                }
                if (Uoffset == 0) {
                    ret.data[i][j].colorTwo = data[i][j].colorTwo;
                } else {
                    double neighborPixel1U = data[i][j - Uoffset].colorTwo;
                    double neighborPixel2U;
                    if (j + Uoffset > width - 1) {
                        neighborPixel2U = data[i][width - 1].colorTwo;
                    } else {
                        neighborPixel2U = data[i][j + Uoffset].colorTwo;
                    }
                    ret.data[i][j].colorTwo = (neighborPixel1U + neighborPixel2U) / 2;
                }
                if (Voffset == 0) {
                    ret.data[i][j].colorThree = data[i][j].colorThree;
                } else {
                    double neighborPixel1V = data[i][j - Voffset].colorThree;
                    double neighborPixel2V;
                    if (j + Voffset > width - 1) {
                        neighborPixel2V = data[i][width - 1].colorThree;
                    } else {
                        neighborPixel2V = data[i][j + Voffset].colorThree;
                    }
                    ret.data[i][j].colorThree = (neighborPixel1V + neighborPixel2V) / 2;
                }
            }
        }
        return ret;
    }

    public RGBData convertToRGB() {
        RGBData ret = new RGBData(height, width);
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                double[] dotProduct = new double[3];
                double[] colorArray = data[i][j].getColorArray();
                for (int x = 0; x < 3; x++) {
                    for (int y = 0; y < 3; y++) {
                        dotProduct[x] += transferMaxtrix[x][y] * colorArray[y];
                    }
                    if (dotProduct[x] > 255) {
                        dotProduct[x] = 255;
                    }
                    if (dotProduct[x] < 0) {
                        dotProduct[x] = 0;
                    }
                }
                ret.setRGBArray(i, j, dotProduct);
            }
        }
        return ret;
    }
}
