// CSCI576 HW3
// Name: Yuhao Wang
// Student ID: 5779881695


import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

public class ImgDisplay {
    // input file pixel height and width are set to 512
    private static final int height = 512;
    private static final int width = 512;
    private final int[][] redMatrix = new int[height][width];
    private final int[][] greenMatrix = new int[height][width];
    private final int[][] blueMatrix = new int[height][width];
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
        if (n < -1 || n > 9) {
            System.out.println("Error: low pass level varies from 0 to 9\nEnter -1 to show progressive decoding");
            return;
        }
        int numCoefficient = (int) Math.pow(Math.pow(2, n), 2);
        if (n != -1) {
            dwt.dwtRedMat = dwt.DWTDecompose(redMatrix, numCoefficient);
            dwt.dwtGreenMat = dwt.DWTDecompose(greenMatrix, numCoefficient);
            dwt.dwtBlueMat = dwt.DWTDecompose(blueMatrix, numCoefficient);

            dwt.idwtRedMat = dwt.IDWTCompose(dwt.dwtRedMat);
            dwt.idwtGreenMat = dwt.IDWTCompose(dwt.dwtGreenMat);
            dwt.idwtBlueMat = dwt.IDWTCompose(dwt.dwtBlueMat);

            displayImg(dwt);
            displayPanel(n);
        } else {
            int step = 0;
            int maxStep = 9;

            while (step <= maxStep) {
                int currNum = (int) Math.pow(Math.pow(2, step), 2);
                dwt.dwtRedMat = dwt.DWTDecompose(redMatrix, currNum);
                dwt.dwtBlueMat = dwt.DWTDecompose(blueMatrix, currNum);
                dwt.dwtGreenMat = dwt.DWTDecompose(greenMatrix, currNum);

                dwt.idwtRedMat = dwt.IDWTCompose(dwt.dwtRedMat);
                dwt.idwtGreenMat = dwt.IDWTCompose(dwt.dwtGreenMat);
                dwt.idwtBlueMat = dwt.IDWTCompose(dwt.dwtBlueMat);

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                displayImg(dwt);
                displayPanel(step);
                step++;
            }
        }
    }


    private void displayImg(DWT dwt) {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int red = dwt.idwtRedMat[i][j];
                int green = dwt.idwtGreenMat[i][j];
                int blue = dwt.idwtBlueMat[i][j];
                int pix = 0xff000000 | ((red & 0xff) << 16) | ((green & 0xff) << 8) | (blue & 0xff);
                dwtImg.setRGB(j, i, pix);
            }
        }
    }


    private void displayPanel(int lowpass) {
        JFrame jFrame = new JFrame();
        GridBagLayout gbg = new GridBagLayout();
        JLabel jLabel1 = new JLabel();
        JLabel jImage = new JLabel();

        jFrame.getContentPane().setLayout(gbg);
        jFrame.setTitle("DWT Rendered Image");
        jLabel1.setText("Applying " + (int) Math.pow(2, lowpass) + " low pass coefficients in rows and " +
                (int) Math.pow(2, lowpass) + " low pass coefficients in columns");
        jLabel1.setHorizontalAlignment(SwingConstants.CENTER);
        jImage.setIcon(new ImageIcon(dwtImg));
        jImage.setHorizontalAlignment(SwingConstants.CENTER);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.weightx = 1;
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
            InputStream is = new FileInputStream(args[0]);
            byte[] bytes = new byte[(int) new File(args[0]).length()];
            int offset = 0;
            int numRead;
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
