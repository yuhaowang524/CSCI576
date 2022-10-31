//CSCI576 HW2
//Name: Yuhao Wang
//Student_ID: 5779881695

A video render program to read and display continuous RGB images in JavaFX panels.
By default, this program will read a foreground rgb file folder and a background rgb file folder
and display a final video based on mode a user chose (0 or 1)


To run the code from command line, first compile with:

>> javac VideoDisplay.java

and then, type in 3 parameters required by the program in the terminal:
<foreground RGB folder>, <background RGB folder>, and <mode>

>> java VideoDisplay <foreground RGB folder> <background RGB folder> <mode>


### Note: this program will take absolute RGB folder address to run. Mode 0 compares static pixel data between
2 frames and automatically generate green screen if two pixels from two frames are identical. Mode 1, on the other
hand, takes a green screen video rgb file and crop the green screen to fit in background rgb pixels