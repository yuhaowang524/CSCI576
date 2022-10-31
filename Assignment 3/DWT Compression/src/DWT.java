// CSCI576 HW3
// Name: Yuhao Wang
// Student ID: 5779881695


import java.util.Arrays;

public class DWT {
    private int height;
    private int width;
    // DWT matrices


    public DWT(int height, int width) {
        this.height = height;
        this.width = width;
    }


    public double[][] DWTDecompose(int[][] matrix, int n) {
        // creat a matrix copy
        double[][] matrixCopy = new double[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                matrixCopy[i][j] = matrix[i][j];
            }
        }

        for (int i = 0; i < width; i++) {
            matrixCopy[i] = decompose(matrixCopy[i]);
        }
        // transpose the matrix copy and decompose column
        matrixCopy = transpose(matrixCopy);
        for (int i = 0; i < height; i++) {
            matrixCopy[i] = decompose(matrixCopy[i]);
        }
        matrixCopy = transpose(matrixCopy);
        matrixCopy = zigzagTraverse(matrixCopy, n);
        return matrixCopy;
    }


    public int[][] IDWTCompose(double[][] matrix) {
        // create a matrix copy
        int[][] matInverse = new int[height][width];

        matrix = transpose(matrix);
        for (int i = 0; i < width; i++) {
            matrix[i] = compose(matrix[i]);
        }
        matrix = transpose(matrix);
        for (int i = 0; i < height; i++) {
            matrix[i] = compose(matrix[i]);
        }

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                matInverse[i][j] = (int) Math.round(matrix[i][j]);
                if (matInverse[i][j] < 0) {
                    matInverse[i][j] = 0;
                }
                if (matInverse[i][j] > 255) {
                    matInverse[i][j] = 255;
                }
            }
        }
        return matInverse;
    }


    /**
     * a helper function to transpose given matrix
     *
     * @param mat matrix needs to be transposed
     * @return transposed matrix
     */
    private double[][] transpose(double[][] mat) {
        double[][] ret = new double[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                ret[i][j] = mat[j][i];
            }
        }
        return ret;
    }


    private double[] decompose(double[] array) {
        int len = array.length;
        while (len > 0) {
            double[] arrayCopy = Arrays.copyOf(array, array.length);
            int ind = 0;
            while (ind < len / 2) {
                arrayCopy[ind] = (array[2 * ind] + array[2 * ind + 1]) / 2;
                arrayCopy[len / 2 + ind] = (array[2 * ind] - array[2 * ind + 1]) / 2;
                ind++;
            }
            array = arrayCopy;
            len = len / 2;
        }
        return array;
    }


    private double[] compose(double[] array) {
        int len = 1;
        while (len <= array.length) {
            double[] arrayCopy = Arrays.copyOf(array, array.length);
            int ind = 0;
            while (ind < len / 2) {
                arrayCopy[2 * ind] = array[ind] + array[len / 2 + ind];
                arrayCopy[2 * ind + 1] = array[ind] - array[len / 2 + ind];
                ind++;
            }
            array = arrayCopy;
            len = len * 2;
        }
        return array;
    }


    /**
     * filter only selected coefficient in the zigzag
     * fashion in a target matrix and replace others with 0
     *
     * @param matrix         target matrix
     * @param numCoefficient number of desired coefficient
     * @return filtered matrix
     */
    private double[][] zigzagTraverse(double[][] matrix, int numCoefficient) {
        int i = 0;
        int j = 0;
        int length = matrix.length - 1;
        int count = 1;

        if (count > numCoefficient) {
            matrix[i][j] = 0;
        }
        count++;

        while (true) {
            j++;
            if (count > numCoefficient) {
                matrix[i][j] = 0;
            }
            count++;

            while (j != 0) {
                i++;
                j--;
                if (count > numCoefficient) {
                    matrix[i][j] = 0;
                }
                count++;
            }

            i++;
            if (i > length) {
                i--;
                break;
            }

            if (count > numCoefficient) {
                matrix[i][j] = 0;
            }
            count++;

            while (i != 0) {
                i--;
                j++;
                if (count > numCoefficient) {
                    matrix[i][j] = 0;
                }
                count++;
            }
        }

        while (true) {
            j++;
            if (count > numCoefficient) {
                matrix[i][j] = 0;
            }
            count++;

            while (j != length) {
                j++;
                i--;
                if (count > numCoefficient) {
                    matrix[i][j] = 0;
                }
                count++;
            }

            i++;
            if (i > length) {
                i--;
                break;
            }

            if (count > numCoefficient) {
                matrix[i][j] = 0;
            }
            count++;

            while (i != length) {
                i++;
                j--;
                if (count > numCoefficient) {
                    matrix[i][j] = 0;
                }
                count++;
            }
        }
        return matrix;
    }

}
