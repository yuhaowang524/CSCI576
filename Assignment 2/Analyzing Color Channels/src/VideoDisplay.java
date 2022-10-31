//CSCI 576 Assignment 2
//Name: Yuhao Wang
//Student_ID: 5779881695


import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;


public class VideoDisplay {
    private JFrame frame1;
    private JLabel lbImg1;
    int width = 640; // default video width
    int height = 480; // default video height
    private static final float fps = 26.0f; // todo: need to change to 24

    /**
     * Read Image RGB
     * Reads the image of given width and height at the given imgPath into the provided BufferedImage.
     */
    private void readImageRGB(int width, int height, String rgbPath, BufferedImage img, rgbData rgb) {
        try {
            int frameLength = width * height * 3;

            File file = new File(rgbPath);
            RandomAccessFile raf = new RandomAccessFile(file, "r");
            raf.seek(0);

            long len = frameLength;
            byte[] bytes = new byte[(int) len];

            raf.read(bytes);

            int ind = 0;
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    byte r = bytes[ind];
                    byte g = bytes[ind + height * width];
                    byte b = bytes[ind + height * width * 2];

                    int pix = 0xff000000 | ((r & 0xff) << 16) | ((g & 0xff) << 8) | (b & 0xff);
                    //int pix = ((a << 24) + (r << 16) + (g << 8) + b);
                    img.setRGB(x, y, pix);
                    rgb.setIntRGB(x, y, pix);
                    ind++;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * read all rgb files in target folder path
     *
     * @param folder target folder path
     * @return <String> String list of rgb files </String>
     */
    private ArrayList<String> rgbPathReader(File folder) {
        ArrayList<String> rgbPath = new ArrayList<>();
        for (File f : Objects.requireNonNull(folder.listFiles())) {
            rgbPath.add(f.getPath());
        }
        Collections.sort(rgbPath);
        return rgbPath;
    }


    private void videoPlayer(VideoDisplay video, ArrayList<String> rgbPaths1, ArrayList<String> rgbPaths2,
                             rgbData rgb1, rgbData rgb2, int mode) {
        ArrayList<BufferedImage> videos = new ArrayList<>();
        long initialTime, endTime;

        if (mode == 1) {
            for (int i = 0; i < rgbPaths1.size(); i++) {
                BufferedImage image1 = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
                BufferedImage image2 = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
                readImageRGB(width, height, rgbPaths1.get(i), image1, rgb1);
                readImageRGB(width, height, rgbPaths2.get(i), image2, rgb2);
                // add processed frame to BufferedImage List
                videos.add(rgb1.replaceGreenToWhite().reduceGreenFurther().replaceWhiteToBack(rgb2).toBufferedImage());
            }
        } else {
            //todo:foreground subtraction
            for (int i = 0; i < rgbPaths1.size(); i++) {
                // read one frame from the file
                BufferedImage image1 = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
                readImageRGB(width, height, rgbPaths1.get(i), image1, rgb1);
                // read the next frame from the file
                rgbData rgb1Nxt = new rgbData(width, height);
                BufferedImage image1Nxt = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
                if (i + 1 == rgbPaths1.size()) {
                    readImageRGB(width, height, rgbPaths1.get(i - 1), image1Nxt, rgb1Nxt);
                } else {
                    readImageRGB(width, height, rgbPaths1.get(i + 1), image1Nxt, rgb1Nxt);
                }
                // read the background frame
                BufferedImage image2 = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
                readImageRGB(width, height, rgbPaths2.get(i), image2, rgb2);
                // add processed frame to BufferedImage List
                videos.add(rgb1.compareStaticPixel(rgb1Nxt).replaceGreenToWhite().reduceGreenFurther().
                        replaceWhiteToBack(rgb2).toBufferedImage());
            }
        }

        // initiate video play starting time
        initialTime = System.currentTimeMillis();
        long sleep = (long) Math.floor((double) 1000 / fps);

        // play the list of BufferedImages at given fps
        for (BufferedImage frame : videos) {
            video.videoPanel(video.frame1, video.lbImg1, frame, sleep);
        }

        endTime = System.currentTimeMillis();
        System.out.println("Render Complete...\n" + "Total Time took to display " + videos.size() +
                " frames is " + (endTime - (initialTime + (videos.size() * sleep) / 1000)) / 1000 + " sec.");
    }


    /**
     * creates a JFrame panel to display all rendered RGB images
     *
     * @param videoFrame an instance of JFrame within VideoDisplay class
     * @param videoLabel an instance of JLabel within VideoDisplay class
     * @param image      an instance of BufferedImage from rgb files
     */
    private void videoPanel(JFrame videoFrame, JLabel videoLabel, BufferedImage image, long sleep) {
        // display RGB in JPanel
        try {
            GridBagLayout gbl = new GridBagLayout();
            videoFrame.setTitle("Rendered Video");
            videoFrame.getContentPane().setLayout(gbl);

            videoLabel.setIcon(new ImageIcon(image));

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.gridx = 0;
            gbc.gridy = 0;
            videoFrame.getContentPane().add(videoLabel, gbc);

            videoFrame.pack();
            videoFrame.setVisible(true);
            Thread.sleep(sleep);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        // initiate variables
        VideoDisplay video = new VideoDisplay();
        video.frame1 = new JFrame();
        video.lbImg1 = new JLabel();
        rgbData fRGB = new rgbData(video.width, video.height);
        rgbData bRGB = new rgbData(video.width, video.height);
        // parse parameters from input line
        File fg = new File(args[0]);
        File bg = new File(args[1]);
        int mode = Integer.parseInt(args[2]);

        ArrayList<String> fRGBPaths = video.rgbPathReader(fg);
        ArrayList<String> bRGBPaths = video.rgbPathReader(bg);

        System.out.println("Mode " + mode + ": RGB file rendering begins...");
        video.videoPlayer(video, fRGBPaths, bRGBPaths, fRGB, bRGB, mode);
    }
}
