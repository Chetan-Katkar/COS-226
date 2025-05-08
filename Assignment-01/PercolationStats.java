import edu.princeton.cs.algs4.*;
public class PercolationStats {
    private int n;
    private final int T;
    private Percolation p;
    private double x_thresholds[];
    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials){
        T = trials;
        this.n = n;
        x_thresholds = new double[T];
        if(n <= 0 || trials <= 0) {//throws exception if n <= 0 or trials <= 0
            throw new IllegalArgumentException("");
        }

        for (int t = 0; t < T; t++) {
            Percolation p = new Percolation(n);

            for (int i = 0; i < n * n; i++) {
                int row = StdRandom.uniformInt(n);
                int col = StdRandom.uniformInt(n);

                if (!p.isOpen(row, col)) {
                    p.open(row, col);

                    if (p.percolates()) {
                        break;
                    }
                }
            }

            x_thresholds[t] = (double) p.numberOfOpenSites() / (n * n);
        }
    }

    // sample mean of percolation threshold
    public double mean(){
        return StdStats.mean(x_thresholds);
    }

    // sample standard deviation of percolation threshold
    public double stddev(){
        return StdStats.stddev(x_thresholds);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLow() {
        return mean() - (1.96 * stddev() / Math.sqrt(T));
    }

    // high endpoint of 95% confidence interval
    public double confidenceHigh() {
        return mean() + (1.96 * stddev() / Math.sqrt(T));
    }

    // test client (see below)
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int T = Integer.parseInt(args[1]);
        PercolationStats ps = new PercolationStats(n, T);
        Stopwatch stopwatch = new Stopwatch();
        StdOut.println("mean()           = " + ps.mean());
        StdOut.println("stddev()         = " + ps.stddev());
        StdOut.println("confidenceLow()  = " + ps.confidenceLow());
        StdOut.println("confidenceHigh() = " + ps.confidenceHigh());
        StdOut.println("elapsed time     = " + stopwatch.elapsedTime());
    }
}
