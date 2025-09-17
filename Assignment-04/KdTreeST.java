import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import java.util.ArrayList;
import edu.princeton.cs.algs4.RectHV;

public class KdTreeST<Value> {
    private Node root;

    private class Node {
        private Point2D key; // key is a 2D point
        private Value val;
        private Node left;  //pointer to the left subtree.
        private Node right; //pointer to the rigth subtree.
        private int N;      //number of points in the subtree.
        private int lvl;    //stores the level of the node.

        public Node(Point2D key, Value val, int N, int lvl) {
            this.key = key;
            this.val = val;
            this.N = N;
            this.lvl = lvl;
        }
    }

    // construct an empty symbol table of points
    public KdTreeST() {
        root = null;
    }

    // is the symbol table empty?
    public boolean isEmpty() {
        return root == null;
    }

    // number of points
    public int size() {
        return size(root);
    }

    //returns the number of points in the substree.
    private int size(Node x){
        if (x == null) return 0;
        else return x.N;
    }

    //returns the level of the point.
    private int level(Node x) {
        if (x == null) return 0;
        else return x.lvl;
    }

    // associate the value val with point p
    public void put(Point2D p, Value val) {
        root = put(root, p, val, 0);
    }

    private Node put(Node x, Point2D p, Value val, int level) {
        //return new node if doesnot exist,else updates the value
        if (x == null) return new Node(p, val, 1, level);
        //check if point are equal if yes updates the value.
        if (p.equals(x.key)) {
            x.val = val;
            return x;
        }
        if (x.lvl % 2 == 0) {
            int cmp = Double.compare(p.x(), x.key.x());
            if (cmp < 0) { // compare x-coordinates
                x.left = put(x.left, p, val, level + 1);
            } else {
                x.right = put(x.right, p, val, level + 1);
            }
        }
        else { // compare y-coordinates
            int cmp = Double.compare(p.y(), x.key.y());
            if (cmp < 0) {
                x.left = put(x.left, p, val, level + 1);
            }
            else {
                x.right = put(x.right, p, val, level + 1);
            }
        }
        x.N = size(x.left) + size(x.right) + 1;
        return x;
    }
    // value associated with point p
    public Value get(Point2D p) {
        return get(root, p);
    }

    //return the value associated with point p in the subtree rooted at x,
    //else return's null.
    private Value get(Node x, Point2D p) {
        //base case.
        if (x == null) return null;

        // Check if the current node's point is equal to the point we are looking for.
        if (p.equals(x.key)) {
            return x.val;
        }

        if (x.lvl % 2 == 0) { // compare x-coordinates
            int cmp = Double.compare(p.x(), x.key.x());
            if (cmp < 0) {
                return get(x.left, p);
            } else {
                return get(x.right, p);
            }
        } else { // compare y-coordinates
            int cmp = Double.compare(p.y(), x.key.y());
            if (cmp < 0) {
                return get(x.left, p);
            } else {
                return get(x.right, p);
            }
        }
    }

    // does the symbol table contain point p?
    public boolean contains(Point2D p) {
        if (get(p) == null) return false;
        else return true;
    }

    // all points in the symbol table
    public Iterable<Point2D> points() {
        //base case.
        if (root == null) {
            return new ArrayList<>();
        }
        //contains the points that are been visited.
        ArrayList<Point2D> points = new ArrayList<>();

        //stores the adjacent nodes of the given node.
        Queue<Node> queue = new Queue<>();

        //startpoint of the level order traversal.
        queue.enqueue(root);
        while (!queue.isEmpty()) {
            Node currentNode = queue.dequeue();
            points.add(currentNode.key);

            if (currentNode.left != null) {
                queue.enqueue(currentNode.left);
            }
            if (currentNode.right != null) {
                queue.enqueue(currentNode.right);
            }
        }
        return points;
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        Queue<Point2D> queue = new Queue<>();//stores all the points inside the reactangle.
        range(root, rect, new RectHV(0.0, 0.0, 1.0, 1.0), queue);
        return queue;
    }

    private void range(Node x, RectHV queryrect, RectHV noderect, Queue<Point2D> queue) {
        //base case
        if (x == null) return;

        //checks if the query rectange intersect with the node rectangle.
        if (!queryrect.intersects(noderect)) return;

        if (queryrect.contains(x.key)) {
            queue.enqueue(x.key);
        }

        RectHV leftRect, rightRect;
        if (x.lvl % 2 == 0) {
            leftRect = new RectHV(noderect.xmin(), noderect.ymin(), x.key.x(), noderect.ymax());
            rightRect = new RectHV(x.key.x(), noderect.ymin(), noderect.xmax(), noderect.ymax());
        } else {
            leftRect = new RectHV(noderect.xmin(), noderect.ymin(), noderect.xmax(), x.key.y());
            rightRect = new RectHV(noderect.xmin(), x.key.y(), noderect.xmax(), noderect.ymax());
        }

        // Recursion
        range(x.left, queryrect, leftRect, queue);
        range(x.right, queryrect, rightRect, queue);
    }

    // a nearest neighbor of point p; null if the symbol table is empty
    public Point2D nearest(Point2D p) {
        if (isEmpty()) {
            return null;
        }

        return nearest(root, p, root.key, new RectHV(0.0, 0.0, 1.0, 1.0));
    }

    private Point2D nearest(Node x, Point2D queryPoint, Point2D currentNearest, RectHV nodeRect) {
        if (x == null) return currentNearest;

        double currentDistSquared = queryPoint.distanceSquaredTo(x.key);
        if (currentDistSquared < queryPoint.distanceSquaredTo(currentNearest)) {
            currentNearest = x.key;
        }

        Node firstChild, secondChild;
        RectHV firstRect, secondRect;

        // Create the rectangles for the children
        if (x.lvl % 2 == 0) {
            RectHV leftRect = new RectHV(nodeRect.xmin(), nodeRect.ymin(), x.key.x(), nodeRect.ymax());
            RectHV rightRect = new RectHV(x.key.x(), nodeRect.ymin(), nodeRect.xmax(), nodeRect.ymax());

            if (queryPoint.x() < x.key.x()) {
                firstChild = x.left;
                firstRect = leftRect;
                secondChild = x.right;
                secondRect = rightRect;
            } else {
                firstChild = x.right;
                firstRect = rightRect;
                secondChild = x.left;
                secondRect = leftRect;
            }
        } else {
            RectHV bottomRect = new RectHV(nodeRect.xmin(), nodeRect.ymin(), nodeRect.xmax(), x.key.y());
            RectHV topRect = new RectHV(nodeRect.xmin(), x.key.y(), nodeRect.xmax(), nodeRect.ymax());

            if (queryPoint.y() < x.key.y()) {
                firstChild = x.left;
                firstRect = bottomRect;
                secondChild = x.right;
                secondRect = topRect;
            } else {
                firstChild = x.right;
                firstRect = topRect;
                secondChild = x.left;
                secondRect = bottomRect;
            }
        }

        // Recursively search the first subtree
        currentNearest = nearest(firstChild, queryPoint, currentNearest, firstRect);
        // Recursively search the second subtree
        currentNearest = nearest(secondChild, queryPoint, currentNearest, secondRect);

        double closestDistSquared = queryPoint.distanceSquaredTo(currentNearest);
        if (secondRect.distanceSquaredTo(queryPoint) < closestDistSquared) {
            currentNearest = nearest(secondChild, queryPoint, currentNearest, secondRect);
        }

        return currentNearest;
    }

    // unit testing (required)
    public static void main(String[] args) {
        KdTreeST<Integer> tree = new KdTreeST<>();

        System.out.println("Is tree empty? " + tree.isEmpty());
        System.out.println("Size: " + tree.size());

        tree.put(new Point2D(1, 1), 1);
        tree.put(new Point2D(2, 1), 4);
        tree.put(new Point2D(0.5, 1), 1);
        tree.put(new Point2D(1, 3), 1);
        tree.put(new Point2D(1, 3), 3);
        tree.put(new Point2D(0.1, 2),6);

        Point2D testpoint = new Point2D(1, 3);
        System.out.println("get value of testpoint "+testpoint+" : " +tree.get(testpoint));

        System.out.println("contain's testpoint "+testpoint+"? : "+tree.contains(testpoint));

        Iterable<Point2D> points = tree.points();
        for (Point2D p : points) {
            System.out.println(p);
        }

        RectHV rect = new RectHV(1,1,3,3);
        Iterable<Point2D> range = tree.range(rect);
        for (Point2D p : range) {
            System.out.println("points within the range :"+p);
        }

        Point2D testpoint2 = new Point2D(0.6,1);
        System.out.println("nearest to the : "+tree.nearest(testpoint2));
    }
}
