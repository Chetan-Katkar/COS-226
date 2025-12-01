import java.awt.Color;
import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.StdOut;

public class SeamCarver {
    private Picture picture;
    private int width;
    private int height;
    private double energy[][];

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        // not referncing.Hence we are not mutating the original.
        this.picture = new Picture(picture);
        width = picture.width();
        height = picture.height();
        energy = new double[width][height];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                energy[x][y] = -1;
            }
        }
    }

    // current picture
    public Picture picture() {
        return picture;
    }

    // width of current picture
    public int width() {
        return width;
    }

    // height of current picture
    public int height() {
        return height;
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            throw new IllegalArgumentException("Coordinates out of bounds");
        }
        //if energy calculated will returned that else will compute the energy.
        if (energy[x][y] != -1) {
            return energy[x][y];
        }

        // x1 and x2 stores the column number of the pixels required for the calculation of energy.
        // y1 and y2 stores the row number of the pixels required to calculate the energy.
        int x1;
        int y1;
        int x2;
        int y2;

        // calculating x gradient.
        // (x1, y1) will have the column and row value of the left pixel.
        // (x2, y2) will have the column and row value of the right pixel.
        x1 = (x - 1 + width()) % width();
        y1 = y;
        x2 = (x + 1) % width();
        y2 = y;
        double xGradient = gradient(x1, y1, x2, y2);

        // calculating y gradient.
        // (x1, y1) will have the column and row value of the top pixel.
        // (x2, y2) will have the column and row value of the down pixel.
        x1 = x;
        y1 = (y - 1 + height()) % height();
        x2 = x;
        y2 = (y + 1) % height();
        double yGradient = gradient(x1, y1, x2, y2);

        double e = Math.sqrt(xGradient + yGradient);
        energy[x][y] = e;  // store the calculated energy
        return e;
    }

    // helper method to compute the gradient.
    private double gradient(int x1, int y1, int x2, int y2) {
        double gradient;

        Color color1 = picture.get(x1, y1);
        Color color2 = picture.get(x2, y2);
        double rx = color2.getRed() - color1.getRed();
        double gx = color2.getGreen() - color1.getGreen();
        double bx = color2.getBlue() - color1.getBlue();

        gradient = rx * rx + gx * gx + bx * bx;
        return gradient;
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        double[][] dp = new double[width()][height()];
        // initialize first column
        for (int y = 0; y < height(); y++) {
            dp[0][y] = energy(0, y);
        }
        // fill dp
        for (int x = 1; x < width(); x++) {
            for (int y = 0; y < height(); y++) {
                double best = dp[x - 1][y];
                if (y - 1 >= 0) {
                    best = Math.min(best, dp[x - 1][y - 1]);
                }
                if (y + 1 < height()) {
                    best = Math.min(best, dp[x - 1][y + 1]);
                }
                dp[x][y] = best + energy(x, y);
            }
        }
        // find best in last column
        int endRow = 0;
        double min = Double.MAX_VALUE;
        for (int y = 0; y < height(); y++) {
            if (dp[width() - 1][y] < min) {
                min = dp[width() - 1][y];
                endRow = y;
            }
        }
        // backtrack
        int[] seam = new int[width()];
        seam[width() - 1] = endRow;
        for (int x = width() - 2; x >= 0; x--) {
            int prevY = seam[x + 1];
            int bestY = prevY;
            double bestEnergy = dp[x][prevY];
            if (prevY - 1 >= 0 && dp[x][prevY - 1] < bestEnergy) {
                bestEnergy = dp[x][prevY - 1];
                bestY = prevY - 1;
            }
            if (prevY + 1 < height() && dp[x][prevY + 1] < bestEnergy) {
                bestEnergy = dp[x][prevY + 1];
                bestY = prevY + 1;
            }
            seam[x] = bestY;
        }
        return seam;
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        double[][] dp = new double[width()][height()];
        // initialize first row
        for (int x = 0; x < width(); x++) {
            dp[x][0] = energy(x, 0);
        }
        // fill dp
        for (int y = 1; y < height(); y++) {
            for (int x = 0; x < width(); x++) {
                double best = dp[x][y - 1];

                if (x - 1 >= 0) {
                    best = Math.min(best, dp[x - 1][y - 1]);
                }
                if (x + 1 < width()) {
                    best = Math.min(best, dp[x + 1][y - 1]);
                }
                dp[x][y] = best + energy(x, y);
            }
        }
        // find best in last row
        int endX = 0;
        double min = Double.MAX_VALUE;
        for (int x = 0; x < width(); x++) {
            if (dp[x][height() - 1] < min) {
                min = dp[x][height() - 1];
                endX = x;
            }
        }
        // backtrack
        int[] seam = new int[height()];
        seam[height() - 1] = endX;
        for (int y = height() - 2; y >= 0; y--) {
            int prevX = seam[y + 1];     // previous column index
            int bestX = prevX;
            double bestEnergy = dp[prevX][y];
            // check x-1
            if (prevX - 1 >= 0 && dp[prevX - 1][y] < bestEnergy) {
                bestEnergy = dp[prevX - 1][y];
                bestX = prevX - 1;
            }
            // check x+1
            if (prevX + 1 < width() && dp[prevX + 1][y] < bestEnergy) {
                bestEnergy = dp[prevX + 1][y];
                bestX = prevX + 1;
            }
            seam[y] = bestX;
        }
        return seam;
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        if (width() == 1) {
            throw new IllegalArgumentException("cannot be called when width is 1.");
        }
        if (seam == null) {
            throw new IllegalArgumentException("seam cannot be null!!");
        }
        if (seam.length != width()) {
            throw new IllegalArgumentException("Invalid array length.");
        }
        for (int x = 0; x < width(); x++) {
            if (seam[x] < 0 || seam[x] >= height()) {
                throw new IllegalArgumentException("Entries out of bound.");
            }
        }
        for (int x = 0; x < width() - 1; x++) {
            if (Math.abs(seam[x] - seam[x + 1]) > 1) {
                throw new IllegalArgumentException("Entries should differ by 1.");
            }
        }

        Picture newPic = new Picture(width, height - 1);
        double[][] newEnergy = new double[width][height - 1];

        for (int x = 0; x < width; x++) {
            int removedY = seam[x];// row to removed.
            // adding above pixels to the new pic.
            for (int y = 0; y < removedY; y++) {
                newPic.set(x, y, picture.get(x, y));
                newEnergy[x][y] = -1;
            }
            // adding below pixels to the new-pic
            for (int y = removedY + 1; y < height; y++) {
                newPic.set(x, y - 1, picture.get(x, y));
                newEnergy[x][y - 1] = -1;
            }
        }
        this.picture = newPic;
        this.height = height - 1;
        this.energy = newEnergy;

    }

       // remove vertical seam from current picture
       public void removeVerticalSeam(int[] seam) {
           if (height() == 1) {
               throw new IllegalArgumentException("cannot be called when heigth is 1.");
           }
           if (seam == null) {
               throw new IllegalArgumentException("seam cannot be null!!");
           }
           if (seam.length != height()) {
               throw new IllegalArgumentException("Invalid array length.");
           }
           for (int y = 0; y < height(); y++) {
               if (seam[y] < 0 || seam[y] >= width()) {
                   throw new IllegalArgumentException("Entries out of bound.");
               }
           }
           for (int y = 0; y < height() - 1; y++) {
               if (Math.abs(seam[y] - seam[y + 1]) > 1) {
                   throw new IllegalArgumentException("Entries should differ by 1.");
               }
           }

           Picture newPic = new Picture(width - 1, height);
           double[][] newEnergy = new double[width - 1][height];

           for (int y = 0; y < height; y++) {
               int removedX = seam[y]; // col to be removed.
               // adding left pixels to the new pic.
               for (int x = 0; x < removedX; x++) {
                   newPic.set(x, y, picture.get(x, y));
                   newEnergy[x][y] = -1;
               }
               // adding rigth pixels to the new-pic
               for (int x = removedX + 1; x < width; x++) {
                   newPic.set(x - 1, y, picture.get(x, y));
                   newEnergy[x - 1][y] = -1;
               }
           }
           this.picture = newPic; // updating picture.
           this.width = width - 1;// updating width
           this.energy = newEnergy;// updating energy.
       }

    //  unit testing (required)
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java SeamCarver <input-image>");
            return;
        }

        String filename = args[0];
        Picture picture = new Picture(filename);
        System.out.println("Width  = " + picture.width());
        System.out.println("Height = " + picture.height());
        SeamCarver sc = new SeamCarver(picture);

        int[] vertical = sc.findVerticalSeam();
        System.out.print("Vertical seam: ");
        for (int x : vertical) System.out.print(x + " ");
        System.out.println();
        System.out.println("Removing 1 vertical seam");
        sc.removeVerticalSeam(vertical);
        System.out.println("New Width  = " + sc.picture.width());
        System.out.println("New Height = " + sc.picture.height());

        int[] horizontal = sc.findHorizontalSeam();
        System.out.print("Horizontal seam: ");
        for (int y : horizontal) System.out.print(y + " ");
        System.out.println();
        System.out.println("Removing 1 horizontal seam");
        sc.removeHorizontalSeam(horizontal);
        System.out.println("New Width  = " + sc.picture.width());
        System.out.println("New Height = " + sc.picture.height());
    }
}
