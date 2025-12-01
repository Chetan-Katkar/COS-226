import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.ST;

import java.util.ArrayList;
import java.util.Scanner;

public class WordNet {
    private ST<String, ArrayList<Integer>> nounToIds;
    private ArrayList<String> idToSynset;
    private Digraph G;
    private ShortestCommonAncestor SCA;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        if ((synsets == null) || (hypernyms == null)) { // checks if arguments passed are not null.
             throw new IllegalArgumentException("null parameter detected");
        }
        // intialise the datastructures.
        idToSynset = new ArrayList<>();
        nounToIds = new ST<>();
        In syn = new In(synsets);
        In hyp = new In(hypernyms);
        // mapping the ids to nouns and nouns to ids.
        while (syn.hasNextLine()) {
            String line = syn.readLine();// read the line.
            String elements[] = line.split(",");// separates the line by commas.
            idToSynset.add(elements[1]);// add the synset to the index as id .
            int id = Integer.parseInt(elements[0]);
            String nouns[] = elements[1].split(" ");// splits the synsets.

            for (String noun : nouns) {
                if (nounToIds.contains(noun)) { // add the id to the pre-existing arraylist.
                    nounToIds.get(noun).add(id);
                }
                else {// if not creats new arraylist for that id.
                    ArrayList<Integer> ids = new ArrayList<>();
                    ids.add(id);
                    nounToIds.put(noun, ids);
                }
            }
        }
        // intialising the graph with the number of syncsets.
        G = new Digraph(idToSynset.size());
        //
        while (hyp.hasNextLine()) {
            String line = hyp.readLine();
            String elements[] = line.split(",");// seperates the numbers.
            int id = Integer.parseInt(elements[0]); // parse and store the id.

            for (int i = 1; i < elements.length; i++) {
                int hypernym = Integer.parseInt(elements[i]);// parse and stores the hypernym.
                G.addEdge(id, hypernym);// add the egde in the diagraph.
            }
         }

        SCA = new ShortestCommonAncestor(G);// intialise the ShortestCommonAncestor class.
    }

    // the set of all WordNet nouns
    public Iterable<String> nouns() {
        return nounToIds.keys();// returns all the keys (nouns) from the ST.
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        return nounToIds.contains(word);//
    }

    // a synset (second field of synsets.txt) that is a shortest common ancestor
    // of noun1 and noun2 (defined below)
    public String sca(String noun1, String noun2) {
        if (!isNoun(noun1) || !isNoun(noun2)) {
            throw new IllegalArgumentException("arguments passed are not nouns!!");
        }
        int ancestor = SCA.ancestorSubset(nounToIds.get(noun1), nounToIds.get(noun2));// gets the shortest common ancestor.
        return idToSynset.get(ancestor);//id of the ancestor.
    }

    // distance between noun1 and noun2 (defined below)
    public int distance(String noun1, String noun2) {
        if (!isNoun(noun1) || !isNoun(noun2)) {
            throw new IllegalArgumentException("arguments passed are not nouns!!");
        }
        return SCA.lengthSubset(nounToIds.get(noun1), nounToIds.get(noun2));// return the length of the shortest common ancestor.
    }

    // unit testing (required)
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: java WordNet <synsets file> <hypernyms file>");
            return;
        }

        WordNet wn = new WordNet(args[0], args[1]);

        // Test nouns() and isNoun()
        System.out.println("All nouns:");
        for (String noun : wn.nouns()) {
            System.out.println(noun);
        }
        System.out.println("\n");

        System.out.println("Is 'cat' a noun? " + wn.isNoun("cat"));
        System.out.println("Is 'dog' a noun? " + wn.isNoun("dog"));
        System.out.println("Is 'unicorn' a noun? " + wn.isNoun("unicorn"));

        // Test distance and sca
        String noun1 = "cat";
        String noun2 = "feline";
        System.out.println("\nDistance between '" + noun1 + "' and '" + noun2 + "': " + wn.distance(noun1, noun2));
        System.out.println("Shortest common ancestor of '" + noun1 + "' and '" + noun2 + "': " + wn.sca(noun1, noun2));
    }
}
