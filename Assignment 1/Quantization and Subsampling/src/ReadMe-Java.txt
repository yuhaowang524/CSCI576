//CSCI576 HW1
//Name: Yuhao Wang
//Student_ID: 5779881695

A sample program to read and display an image in JavaFX panels.
By default, this program will read in the first frame of a given .rgb video file.


Unzip the folder to where you want.
To run the code from command line, first compile with:

>> javac ImageDisplay.java

and then, you can run it to take in 6 parameters:
<pathToRGBfile>, <sub-sampling y ratio>, <sub-sampling u ratio>, <sub-sampling v ratio>,
<scale factor Sw>, <scale factor Sh>, and <antialiasing factor>

>> java ImageDisplay <path>, <y>, <u>, <v>, <sw>, <sh>, <aa>


### Note: this program will generate two images according to your input prompt, the modified image will be on top of
the original image