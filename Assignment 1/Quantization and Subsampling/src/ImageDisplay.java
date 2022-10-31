
import java.awt.*;
import java.awt.image.*;
import java.io.*;
import javax.swing.*;


public class ImageDisplay {

	JFrame frame1;
	JFrame frame2;
	JLabel lbIm1;
	JLabel lbIm2;
	BufferedImage imgOne;
	BufferedImage imgTwo;
	int width = 1920; // default image width and height
	int height = 1080;

	/** Read Image RGB
	 *  Reads the image of given width and height at the given imgPath into the provided BufferedImage.
	 */
	private void readImageRGB(int width, int height, String imgPath, BufferedImage img, RGBData imgRGBData)
	{
		try
		{
			int frameLength = width*height*3;

			File file = new File(imgPath);
			RandomAccessFile raf = new RandomAccessFile(file, "r");
			raf.seek(0);

			long len = frameLength;
			byte[] bytes = new byte[(int) len];

			raf.read(bytes);

			int ind = 0;
			for(int y = 0; y < height; y++)
			{
				for(int x = 0; x < width; x++)
				{
					byte a = 0;
					byte r = bytes[ind];
					byte g = bytes[ind+height*width];
					byte b = bytes[ind+height*width*2]; 

					int pix = 0xff000000 | ((r & 0xff) << 16) | ((g & 0xff) << 8) | (b & 0xff);
					//int pix = ((a << 24) + (r << 16) + (g << 8) + b);
					img.setRGB(x,y,pix);
					imgRGBData.setRGBInt(y, x, pix);
					ind++;
				}
			}
		}
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}

	public void showIms(String[] args){

		// Read a parameter from command line
		String param1 = args[1];
		System.out.println("The subsampling ratio of Y was: " + param1);
		int subsamplingYRatio = Integer.parseInt(args[1]);
		String param2 = args[2];
		System.out.println("The subsampling ratio of U was: " + param2);
		int subsamplingURatio = Integer.parseInt(args[2]);
		String param3 = args[3];
		System.out.println("The subsampling ratio of V was: " + param3);
		int subsamplingVRatio = Integer.parseInt(args[3]);
		String param4 = args[4];
		System.out.println("The scale factor of Sw was: " + param4);
		float scaleW = Float.parseFloat(args[4]);
		String param5 = args[5];
		System.out.println("The scale factor of Sh was: " + param5);
		float scaleH = Float.parseFloat(args[5]);
		String param6;
		int aa = Integer.parseInt(args[6]);
		if (aa == 1) {
			param6 = "on";
		} else {
			param6 = "off";
		}
		System.out.println("Antialiasing: " + param6);


		// Create am empty RGBData type to store all RGB data for the input image
		RGBData imgOneRGB = new RGBData(height, width);

		// Read in the specified image
		imgOne = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		readImageRGB(width, height, args[0], imgOne, imgOneRGB);

		imgTwo = imgOneRGB.convertToYUV().subsampleAdjust(subsamplingYRatio, subsamplingURatio, subsamplingVRatio).
				convertToRGB().scaleRGB(scaleW, scaleH, aa).convertToBufferedImage();

		// Use label to display the image
		frame1 = new JFrame();
		GridBagLayout gLayout1 = new GridBagLayout();
		frame1.getContentPane().setLayout(gLayout1);

		frame2 = new JFrame();
		GridBagLayout gLayout2 = new GridBagLayout();
		frame2.getContentPane().setLayout(gLayout2);

		JLabel lbText1 = new JLabel("Original image");
		JLabel lbText2 = new JLabel("Modified image");
		lbIm1 = new JLabel(new ImageIcon(imgOne));
		lbIm2 = new JLabel(new ImageIcon(imgTwo));

		GridBagConstraints c1 = new GridBagConstraints();
		c1.fill = GridBagConstraints.HORIZONTAL;
		c1.anchor = GridBagConstraints.CENTER;
		c1.weightx = 0.5;
		c1.gridx = 0;
		c1.gridy = 0;
		frame1.getContentPane().add(lbText1, c1);

		c1.fill = GridBagConstraints.HORIZONTAL;
		c1.gridx = 0;
		c1.gridy = 1;
		frame1.getContentPane().add(lbIm1, c1);

		frame1.pack();
		frame1.setVisible(true);

		GridBagConstraints c2 = new GridBagConstraints();
		c2.fill = GridBagConstraints.HORIZONTAL;
		c2.anchor = GridBagConstraints.CENTER;
		c2.weightx = 0.5;
		c2.gridx = 0;
		c2.gridy = 0;
		frame2.getContentPane().add(lbText2, c2);

		c2.fill = GridBagConstraints.HORIZONTAL;
		c2.gridx = 0;
		c2.gridy = 1;
		frame2.getContentPane().add(lbIm2, c2);

		frame2.pack();
		frame2.setVisible(true);
	}

	public static void main(String[] args) {
		ImageDisplay ren = new ImageDisplay();
		ren.showIms(args);
	}

}
