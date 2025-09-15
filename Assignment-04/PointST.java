import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RedBlackBST;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.Stack;

public class PointST<Value> {

    //declaring an object of RedBlackBST.
    private RedBlackBST<Point2D, Value> rbbst;

    // construct an empty symbol table of points
    public PointST() {
        //initialising RedBlackBST.
        rbbst = new RedBlackBST<>();
    }

    // is the symbol table empty?
    public boolean isEmpty() {
        //return if RedBlackBST is empty.
        return rbbst.isEmpty();
    }

    // number of points
    public int size() {
        //returns the size of RedBlackBST.
        return rbbst.size();
    }

    // associate the value val with point p
    public void put(Point2D p, Value val) {
        //panic's if any one of the arugment is null.
        if (p == null || val == null) {
            throw new IllegalArgumentException("");
        }

        //add the key-value pair to the Symbol Table.
        rbbst.put(p, val);
    }

    // value associated with point p
    public Value get(Point2D p) {
        return rbbst.get(p);
    }

    // does the symbol table contain point p?
    public boolean contains(Point2D p) {
        return rbbst.contains(p);
    }

    // all points in the symbol table
    public Iterable<Point2D> points() {
        //returns all the keys from the symbol table.
        return rbbst.keys();
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        Stack<Point2D> points = new Stack<>();//stores all the points inside the reactangle.
        for (Point2D p : rbbst.keys()) {
            if (rect.contains(p)) {
                points.push(p);
            }
        }
        return points;
    }

    // a nearest neighbor of point p; null if the symbol table is empty
    public Point2D nearest(Point2D p) {
        //Stores the min distance between point p and the points from the RedBlackBST's symbol table.
        //initially marked with max value.
        double minDist = Double.MAX_VALUE;
        //key intialize to null,return null if doesn't get updated(symbol table is empty).
        Point2D nearestPoint = null;

        //iterate over all the point and find the min distance point.
        for (Point2D pt : rbbst.keys()) {
            if (pt.distanceSquaredTo(p) < minDist) {
                nearestPoint = pt;//updates the point
                minDist = pt.distanceSquaredTo(p);//updates the distance.
            }
        }

        return nearestPoint;
    }

    // unit testing (required)
    public static void main(String[] args) {
        PointST<Integer> st = new PointST<>();

        //testing isEmpty() fun.
        System.out.println("Is empty? :" + st.isEmpty());

        //testing put() fun
        st.put(new Point2D(0.2, 0.3), 1);
        st.put(new Point2D(0.4, 0.7), 2);
        st.put(new Point2D(0.1, 0.9), 3);
        st.put(new Point2D(0.1, 0.8), 3);
        st.put(new Point2D(0.1, 0.7), 6);
        st.put(new Point2D(0.1, 0.6), 3);
        st.put(new Point2D(0.1, 0.4), 3);
        //testing size() function.
        System.out.println("Size: " + st.size());
        System.out.println("Is empty? : " + st.isEmpty());

        //coantains() and get() function.
        Point2D testPoint = new Point2D(0.4, 0.7);
        System.out.println("Contains " + testPoint + "? " + st.contains(testPoint));
        System.out.println("Value at " + testPoint + ": " + st.get(testPoint));

        //ierator() function.
        System.out.println("All points in order:");
        for (Point2D p : st.points()) {
            System.out.println(p + " : " + st.get(p));
        }

        //testing nearest() fun.
        Point2D testpoint2 = new Point2D(0.3, 0.6);
        Point2D nearest = st.nearest(testpoint2);
        System.out.println("Nearest to " + testpoint2 + ": " + nearest);

        //testing range()
        RectHV rect = new RectHV(0.1,0.1,0.5,0.5);
        Iterable<Point2D> points = st.range(rect);
        for (Point2D p : points) {
            System.out.println("points within the range :"+p + " : " + st.get(p));
        }
    }
}
