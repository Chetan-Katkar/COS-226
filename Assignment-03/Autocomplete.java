import edu.princeton.cs.algs4.*;
import java.util.Arrays;
import java.util.Comparator;

public class Autocomplete {
    private Term[] terms;
    // Initializes the data structure from the given array of terms.
    public Autocomplete(Term[] terms){
        if (terms == null) {
            throw new IllegalArgumentException();
        }

        this.terms = terms;
        Arrays.sort(this.terms);
    }

    // Returns all terms that start with the given prefix, in descending order of weight.
    public Term[] allMatches(String prefix){
        if (prefix == null) {
            throw new IllegalArgumentException();
        }

        Comparator<Term> comparator = new Term.PrefixOrder(prefix.length());
        Term prefixTerm = new Term(prefix, 0); // converting string as same type as array.
        int firstIndex = BinarySearchDeluxe.firstIndexOf(terms, prefixTerm, comparator);
        int lastIndex  = BinarySearchDeluxe.lastIndexOf(terms, prefixTerm, comparator);

        if (firstIndex == -1 || lastIndex == -1) return new Term[0]; // return empty array

        Term[] copyArray = Arrays.copyOfRange(terms, firstIndex, lastIndex + 1);
        Arrays.sort(copyArray,Term.byReverseWeightOrder());

        return copyArray;
    }

    // Returns the number of terms that start with the given prefix.
    public int numberOfMatches(String prefix){
        if (prefix == null) {
            throw new IllegalArgumentException();
        }

        Comparator<Term> comparator = new Term.PrefixOrder(prefix.length());
        Term prefixTerm = new Term(prefix, 0);

        int firstIndex = BinarySearchDeluxe.firstIndexOf(terms, prefixTerm, comparator);
        int lastIndex  = BinarySearchDeluxe.lastIndexOf(terms, prefixTerm, comparator);

        if (firstIndex == -1 || lastIndex == -1) return 0;// no matches.
        return lastIndex - firstIndex + 1;
    }

    // unit testing (required)
    public static void main(String[] args){
        // read in the terms from a file
        String filename = args[0];
        In in = new In(filename);
        int n = in.readInt();
        Term[] terms = new Term[n];
        for (int i = 0; i < n; i++) {
            long weight = in.readLong();           // read the next weight
            in.readChar();                         // scan past the tab
            String query = in.readLine();          // read the next query
            terms[i] = new Term(query, weight);    // construct the term
        }

        // read in queries from standard input and print the top k matching terms
        int k = Integer.parseInt(args[1]);
        Autocomplete autocomplete = new Autocomplete(terms);
        while (StdIn.hasNextLine()) {
            String prefix = StdIn.readLine();
            Term[] results = autocomplete.allMatches(prefix);
            System.out.printf("%d matches\n", autocomplete.numberOfMatches(prefix));
            for (int i = 0; i < Math.min(k, results.length); i++)
                System.out.println(results[i]);
        }
    }

}
