// CSCI576 HW3
// Name: Yuhao Wang
// Student ID: 5779881695

This is a java program designed to render rgb file through discrete wavelet transformation
To run this program, please put ImgDisplay.java and DWT.java into the same folder and compile
ImgDisplay.java

javac ImgDisplay.java

After compiling, ImgDisplay.java requires two args input" <filename>, <low pass level>

java ImgDisplay <filename> <low pass level>

Please note that low pass level varies from 0 to 9, which allows 2**lpl low pass coefficients
in rows and 2**lpl low pass coefficients in column. Also, -1 can be entered to show progressive
decoding