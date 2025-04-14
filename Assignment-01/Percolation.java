import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    //instance variables.
    public static boolean grid[][];
    public static int count ;
    private int n ;//Stores the size of grid.
    public static WeightedQuickUnionUF UF;
    public static WeightedQuickUnionUF UF1;//for fullness of the site
    private int virtual_top    ;
    private int virtual_bottom ;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n){
        this.n = n;
        count  = 0;//count the number of open sites intially zero.
        UF = new WeightedQuickUnionUF((n  * n) + 2);
        UF1 = new WeightedQuickUnionUF((n  * n) + 1);
        if(n <= 0){//throws error when n less than or equal to 0.
            throw new IllegalArgumentException("n should be positive");
        }
        else {
            grid = new boolean[n][n];
            //initializing all sites as blocked in grid
            for (int i = 0; i < grid.length; i++) {
                for (int j = 0; j < grid[0].length; j++) {
                    grid[i][j] = false;
                }
            }
            virtual_bottom = (n * n) + 1;
            virtual_top    = n * n ;
        }
    }

    //private method for validating indices.
    private void check(int row, int col){
        if(row < 0 || row > (n - 1) || col < 0 || col > (n-1)){
            //throws error when any one of argument is outside prescribed range.
            throw new IllegalArgumentException("index out of prescribed range");
        }
    }

    //tranform 2D indices to 1D index.
    private int transform(int row, int col){
        return (row * n) + col ;
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col){
        check(row, col);//checks if the indices are valid or not.
        int index1,index2;
        index1 = transform(row, col);//storing index of current site.
        if (!grid[row][col]) {//opens the site if not open already.
            grid[row][col] = true;
            count++;
            // If it's in the top row, connect to virtual_top
            if (row == 0) {
                UF.union(index1, virtual_top);
                UF1.union(index1, virtual_top);
            }

            // If it's in the bottom row, connect to virtual_bottom
            if (row == n - 1) {
                UF.union(index1, virtual_bottom);
            }

            //4 connectivity
            // Connect to adjacent open sites
            if (row < n - 1 && isOpen(row + 1, col)) {
                UF.union(index1, transform(row + 1, col));
            }
            if (row > 0 && isOpen(row - 1, col)) {
                UF.union(index1, transform(row - 1, col));
            }
            if (col > 0 && isOpen(row, col - 1)) {
                UF.union(index1, transform(row, col - 1));
            }
            if (col < n - 1 && isOpen(row, col + 1)) {
                UF.union(index1, transform(row, col + 1));
            }
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col){
        check(row, col);//checks if the indices are valid or not.

        return grid[row][col] ;
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col){
        if(!isOpen(row, col)){
            return false; // returns false if site is blocked.
        }
        check(row, col);//checks if the indices are valid or not.
        int index = transform(row, col);//stores the 1d index of 2d grid.

        return UF1.connected(virtual_top,index) ;
    }

    // returns the number of open sites
    public int numberOfOpenSites(){
        return count;
    }

    // does the system percolate?
    public boolean percolates(){
        return UF.connected(virtual_top, virtual_bottom);
    }

    // unit testing (required)
    public static void main(String[] args){
        // // #test case 1 (sytem percolates).
        // int n = 2;
        // Percolation p = new Percolation(n);
        // p.open(0, 1);
        // p.open(1, 1);
        // StdOut.println(p.percolates());//true
        // StdOut.println(p.isFull(1, 1));//true
        // StdOut.println(p.isFull(0, 0));//false
        // StdOut.println(p.numberOfOpenSites());

        // // #test case 2 (sytem does not percolates).
        // int n = 2;
        // Percolation p = new Percolation(n);
        // p.open(0, 1);
        // p.open(1, 0);
        // StdOut.println(p.percolates());//false
        // StdOut.println(p.isFull(1, 1));//false
        // StdOut.println(p.isFull(0, 1));//true

        //#test case 3 (bask wash removal check)
        Percolation p1 = new Percolation(3);
        p1.open(0,0);
        p1.open(2,0);
        p1.open(3,0);
        p1.open(3,2);
        StdOut.println(p.percolates());
        StdOut.println(p.isFull(3, 2));//false
    }

}
