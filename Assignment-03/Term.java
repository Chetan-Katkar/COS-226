import java.util.Arrays;
import java.util.Comparator;

public class Term implements Comparable<Term>{
    // instance variables.
    private final String query;
    private final long   weight;

    // Initializes a term with the given query string and weight.
    public Term(String query, long weight){
        // Throws an IllegalArgumentException if either query is null or weight is negative.
        if ((query == null) || (weight < 0)) {
            throw new IllegalArgumentException("query cannot be null or weight cannot bre negative.");
        }

        // initializing the values of the instance variables.
        this.query  = query ;
        this.weight = weight;
    }

    private static class ReverseWeightOrder implements Comparator<Term> {
        public int compare(Term v, Term w) {
            if (v.weight < w.weight) {
                return 1;// w will be swapped by v .
            }
            if (v.weight > w.weight) {
                return -1;// no swapping.
            }
            return 0;// no swapping.
        }
    }

    // Compares the two terms in descending order by weight.
    public static Comparator<Term> byReverseWeightOrder(){
        return new ReverseWeightOrder();
    }

    public static class PrefixOrder implements Comparator<Term>{
        private final int r;//number of characters from to string to be compared.
        public PrefixOrder(int r){
            this.r = r;
        }
        // calls the instance compareTo() method with substrings cut from 0 to r(excluded) index.
        public int compare(Term v, Term w){
            String string1 = r < v.query.length() ? v.query.substring(0, r) : v.query;
            String string2 = r < w.query.length() ? w.query.substring(0, r) : w.query;
            return string1.compareTo(string2);
        }

    }
    // Compares the two terms in lexicographic order,
    // but using only the first r characters of each query.
    public static Comparator<Term> byPrefixOrder(int r){
        return new PrefixOrder(r);
    }

    // Compares the two terms in lexicographic order by query.
    public int compareTo(Term that){
            int leng1 = this.query.length();
            int leng2 = that.query.length();
            int minLeng = Math.min(leng1, leng2);
            int k = 0;// stores the index of first different character from strings.
            //finds the first different character from the string having equal lengths.
             for (int i = 0; i < minLeng; i++) {
                if (this.query.charAt(i) == that.query.charAt(i)) {
                    k++;
                }
                else {
                    break;
                }
            }
            if (leng1 == leng2) {
                // checks if the length is less than lenght of the string
                if (k != (leng1 - 1)) {
                    if (this.query.charAt(k) < that.query.charAt(k)) {
                        return  -1;
                    }
                    if (this.query.charAt(k) > that.query.charAt(k)) {
                        return  1;
                    }
                }
            }
            else {// compares strings of unequal lengths.
                if (k != minLeng) {
                    if (this.query.charAt(k) < that.query.charAt(k)) {
                        return -1;
                    }
                    if (this.query.charAt(k) > that.query.charAt(k)) {
                        return 1;
                    }
                }
                else {
                    if (leng1 < leng2) return -1;
                    return 1;
                }
            }
            return 0; // strings are equal
    }

    // Returns a string representation of this term in the following format:
    // the weight, followed by a tab, followed by the query.
    public String toString(){
        return weight + "\t" + query ;
    }

    // unit testing (required)
    public static void main(String[] args){
        Term[] terms = {
                new Term("c", 2),
                new Term("bat", 1),
                new Term("man", 3),
                new Term("can", 4),
                new Term("wan", 9),
        };

        System.out.println("array of terms before sorting : ");
        for (Term term : terms) {
            System.out.println(term);
        }

        Arrays.sort(terms,byReverseWeightOrder());

        System.out.println("array of terms after sorting (reverse weight order) : ");
        for (Term term : terms) {
            System.out.println(term);
        }

        Arrays.sort(terms);
        System.out.println("sorting array by compareTo() method : ");
        for (Term term : terms) {
            System.out.println(term);
        }

        Arrays.sort(terms,byPrefixOrder(2));
        System.out.println("array of terms after sorting by query (byPrefixOrder) : ");
        for (Term term : terms) {
            System.out.println(term);
        }
    }
}
