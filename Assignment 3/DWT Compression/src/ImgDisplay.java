// CSCI576 HW3
// Name: Yuhao Wang
// Student ID: 5779881695


import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

public class ImgDisplay {
    // input file pixel height and width are set to 512
    private static int height = 512;
    private static int width = 512;
    private static int blockSize = 8;
    private int[][] redMatrix = new int[height][width];
    private int[][] greenMatrix = new int[height][width];
    private int[][] blueMatrix = new int[height][width];
    private double[][] dwtRedMat = new double[height][width];
    private double[][] dwtGreenMat = new double[height][width];
    private double[][] dwtBlueMat = new double[height][width];
    private int[][] idwtRedMat = new int[height][width];
    private int[][] idwtGreenMat = new int[height][width];
    private int[][] idwtBlueMat = new int[height][width];
    BufferedImage InputImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    BufferedImage dwtImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

    private void readPixel(byte[] bytes) {
        int ind = 0;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                byte red = bytes[ind];
                byte green = bytes[ind + height * width];
                byte blue = bytes[ind + height * width * 2];

                redMatrix[i][j] = red & 0xff;
                greenMatrix[i][j] = green & 0xff;
                blueMatrix[i][j] = blue & 0xff;

                int pix = 0xff000000 | ((red & 0xff) << 16) | ((green & 0xff) << 8) | (blue & 0xff);
                InputImg.setRGB(j, i, pix);
                ind++;
            }
        }
    }


    private void performDWT(DWT dwt, String[] args) {
        int n = Integer.parseInt(args[1]);
        int numCoefficient = (int) Math.pow(Math.pow(2, n), 2);
        if (n != -1) {
            dwtRedMat = dwt.DWTDecompose(redMatrix, numCoefficient);
            dwtGreenMat = dwt.DWTDecompose(greenMatrix, numCoefficient);
            dwtBlueMat = dwt.DWTDecompose(blueMatrix, numCoefficient);

            idwtRedMat = dwt.IDWTCompose(dwtRedMat);
            idwtGreenMat = dwt.IDWTCompose(dwtGreenMat);
            idwtBlueMat = dwt.IDWTCompose(dwtBlueMat);

            //todo: add display function
            displayImg();
            displayPanel();
        } else {
            int step = 0;
            int maxStep = 9;

            while (step < maxStep) {
                int currNum = (int) Math.pow(Math.pow(2, step), 2);
                dwtRedMat = dwt.DWTDecompose(redMatrix, currNum);
                dwtBlueMat = dwt.DWTDecompose(blueMatrix, currNum);
                dwtGreenMat = dwt.DWTDecompose(greenMatrix, currNum);

                idwtRedMat = dwt.IDWTCompose(dwtRedMat);
                idwtGreenMat = dwt.IDWTCompose(dwtGreenMat);
                idwtBlueMat = dwt.IDWTCompose(dwtBlueMat);

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                //todo: add display function
                displayImg();
                displayPanel();
                step++;
            }
        }
    }


    private void displayImg() {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int red = idwtRedMat[i][j];
                int green = idwtGreenMat[i][j];
                int blue = idwtBlueMat[i][j];
                int pix = 0xff000000 | ((red & 0xff) << 16) | ((green & 0xff) << 8) | (blue & 0xff);
                dwtImg.setRGB(j, i, pix);
            }
        }
    }


    private void displayPanel() {
        JFrame jFrame = new JFrame();
        GridBagLayout gbg = new GridBagLayout();
        JLabel jLabel1 = new JLabel();
        JLabel jImage = new JLabel();

        jFrame.getContentPane().setLayout(gbg);
        jLabel1.setText("Title");
        jLabel1.setHorizontalAlignment(SwingConstants.CENTER);
        jImage.setIcon(new ImageIcon(dwtImg));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.weightx = 0.5;
        gbc.gridx = 0;
        gbc.gridy = 0;
        jFrame.getContentPane().add(jLabel1, gbc);

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 1;
        jFrame.getContentPane().add(jImage, gbc);

        jFrame.pack();
        jFrame.setVisible(true);
    }


    public void showImage(String[] args) {
        DWT dwt = new DWT(height, width);
        try {
            InputStream is = new FileInputStream(new File(args[0]));
            byte[] bytes = new byte[(int) new File(args[0]).length()];
            int offset = 0;
            int numRead = 0;
            while (offset < bytes.length
                    && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
                offset += numRead;
            }
            readPixel(bytes);
            performDWT(dwt, args);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        ImgDisplay ren = new ImgDisplay();
        ren.showImage(args);
    }
}
