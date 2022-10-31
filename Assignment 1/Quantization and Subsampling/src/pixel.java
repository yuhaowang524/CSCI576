//CSCI576 HW1
//Name: Yuhao Wang
//Student_ID: 5779881695

public class pixel {
    // an arraylist storing pixel color value
    public double[] retArray;
    // parameters such that colorOne, colorTwo, and colorThree are used to represent
    // r, g, b in RGB data and y, u, v in YUV data
    double colorOne;
    double colorTwo;
    double colorThree;

    public pixel() {

    }

    //constructor pixel takes in double rgb value
    public pixel(double colorOne, double colorTwo, double colorThree) {
        this.colorOne = colorOne;
        this.colorTwo = colorTwo;
        this.colorThree = colorThree;
    }

    //constructor pixel takes in int rgb value
    public pixel(int colorVal) {
        this.colorOne = (colorVal >> 16) & 0xFF;
        this.colorTwo = (colorVal >> 8) & 0xFF;
        this.colorThree = (colorVal) & 0xFF;
    }

    public double[] getColorArray() {
        retArray = new double[3];
        retArray[0] = colorOne;
        retArray[1] = colorTwo;
        retArray[2] = colorThree;
        return retArray;
    }
}
