import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Topological;

import java.util.LinkedList;
import java.util.List;

public class ShortestCommonAncestor {
    private Digraph G;

    // constructor takes a rooted DAG as argument
    public ShortestCommonAncestor(Digraph G) {
        Topological topo = new Topological(G);
        // return if G is not a rooted DAG.
        if (!topo.isDAG()) {
            throw new IllegalArgumentException("G is not a rooted DAG!!");
        }
        // Intialises the Digraph.
        this.G = G;
    }

    // length of shortest ancestral path between v and w
    public int length(int v, int w) {
        if (v < 0 || v >= G.V() || w < 0 || w >= G.V()) {
            throw new IllegalArgumentException("vertice v is outside its prescribed range");
        }

        // gets the common closest ancestor to both v and w.
        int ancestor = ancestor(v, w);
        // intances to calculate the distance to the shortest common ancestor.
        BreadthFirstDirectedPaths vSource = new BreadthFirstDirectedPaths(G, v);
        BreadthFirstDirectedPaths wSource = new BreadthFirstDirectedPaths(G, w);

        return vSource.distTo(ancestor) + wSource.distTo(ancestor);
    }

    // a shortest common ancestor of vertices v and w
    public int ancestor(int v, int w) {
        if (v < 0 || v >= G.V() || w < 0 || w >= G.V()) {
            throw new IllegalArgumentException("vertice v is outside its prescribed range");
        }

        // stores the minDistance from the common ancestor of v and w.
        int minDistance = Integer.MAX_VALUE;
        // intances to calculate the shortest common ancestor.
        BreadthFirstDirectedPaths vSource = new BreadthFirstDirectedPaths(G, v);
        BreadthFirstDirectedPaths wSource = new BreadthFirstDirectedPaths(G, w);
        // the common ancestor to all vertices will be 0.
        int ancestor = 0;
        for (int x = 0; x < G.V(); x++) {
            if (vSource.hasPathTo(x) && wSource.hasPathTo(x)) {
                int totalDist = vSource.distTo(x) + wSource.distTo(x);
                // update the mindistance and the shortest common ancestor.
                if (totalDist < minDistance) {
                    minDistance = totalDist;
                    ancestor = x;
                }
            }
        }

        return ancestor;
    }

    // length of shortest ancestral path of vertex subsets A and B
    public int lengthSubset(Iterable<Integer> subsetA, Iterable<Integer> subsetB) {
        // check any of the argument is null.
        if (subsetA == null || subsetB == null) {
            throw new IllegalArgumentException("Iterable argument cannot be null");
        }

        // variables to count the elements in the sets.
        int elementsInA = 0;
        int elementsInB = 0;
        // checks if any element of any of subset is null.
        for (Integer v : subsetA) {
            if (v == null) {
                throw new IllegalArgumentException("Iterable A contains a null item");
            }
            else {
                elementsInA++;
            }
        }
        for (Integer w : subsetB) {
            if (w == null) {
                throw new IllegalArgumentException("Iterable B contains a null item");
            }
            else {
                elementsInB++;
            }
        }
        // check id the sets are empty.
        if (elementsInB == 0 || elementsInA == 0) {
            throw new IllegalArgumentException("set cannot be empty");
        }

        int minDist = Integer.MAX_VALUE;
        for (Integer v : subsetA) {
            for (Integer w : subsetB) {
                int len = length(v, w);
                if (len < minDist) {
                    minDist = len;
                }
            }
        }

        return minDist;
    }

    // a shortest common ancestor of vertex subsets A and B
    public int ancestorSubset(Iterable<Integer> subsetA, Iterable<Integer> subsetB){
        // check any of the argument is null.
        if (subsetA == null || subsetB == null) {
            throw new IllegalArgumentException("Iterable argument cannot be null");
        }
        // variables to count the elements in the sets.
        int elementsInA = 0;
        int elementsInB = 0;
        // checks if any element of any of subset is null.
        for (Integer v : subsetA) {
            if (v == null) {
                throw new IllegalArgumentException("Iterable A contains a null item");
            }
            else {
                elementsInA++;
            }
        }
        for (Integer w : subsetB) {
            if (w == null) {
                throw new IllegalArgumentException("Iterable B contains a null item");
            }
            else {
                elementsInB++;
            }
        }
        // check id the sets are empty.
        if (elementsInB == 0 || elementsInA == 0) {
            throw new IllegalArgumentException("set cannot be empty");
        }

        int minDist = Integer.MAX_VALUE;
        int ancestor = 0;
        for (Integer v : subsetA) {
            for (Integer w : subsetB) {
                int len = length(v, w);
                if (len < minDist) {
                    minDist = len;               // updates the new minimum distance.
                    ancestor = ancestor(v, w);
                }
            }
        }

         return ancestor;
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        ShortestCommonAncestor sca = new ShortestCommonAncestor(G);
        // while (!StdIn.isEmpty()) {
        //     int v = StdIn.readInt();
        //     int w = StdIn.readInt();
        //     int length   = sca.length(v, w);
        //     int ancestor = sca.ancestor(v, w);
        //     StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        // }

        Stack<Integer> setA = new Stack<>();
        Stack<Integer> setB = new Stack<>();

        setA.push(13);
        setA.push(23);
        setA.push(24);

        setB.push(6);
        setB.push(16);
        setB.push(17);

        StdOut.printf("length = %d, ancestor = %d\n", sca.lengthSubset(setA, setB), sca.ancestorSubset(setA, setB));

    }
}
