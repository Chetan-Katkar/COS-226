import java.util.Comparator;

public class BinarySearchDeluxe {

    // Returns the index of the first key in the sorted array a[]
    // that is equal to the search key, or -1 if no such key.
    public static <Key> int firstIndexOf(Key[] a, Key key, Comparator<Key> comparator){
        if (a == null || key == null || comparator == null) {
            throw new IllegalArgumentException("Argument cannot be null value.");
        }

        int lo = 0;           // stores the first index of an array.
        int hi = a.length - 1;// stores the last index of an array.
        int result = -1;
        // find the key on the mid else shrinks the array by n/2.
        while (lo <= hi) {
            int mid = lo + (hi - lo)/2;// middle index of an array
            int compare = comparator.compare(key, a[mid]);

            if (compare < 0) hi = mid - 1;
            else if (compare > 0) lo = mid + 1;
            else {
                // Search for key in left half of an array.
                result = mid;
                hi = mid - 1;
            }
        }
        return result;
    }

    // Returns the index of the last key in the sorted array a[]
    // that is equal to the search key, or -1 if no such key.
    public static <Key> int lastIndexOf(Key[] a, Key key, Comparator<Key> comparator){
        if (a == null || key == null || comparator == null) {
            throw new IllegalArgumentException("Argument cannot be null value.");
        }

        int lo = 0;           // initially stores the first index of array.
        int hi = a.length - 1;// initially stores the last index of the array.
        int result = -1;
        // find the key on the mid else shrinks the array by n/2.
        while (lo <= hi) {
            int mid = lo + (hi - lo)/2;// middle index of an array
            int compare = comparator.compare(key, a[mid]);

            if (compare < 0) hi = mid - 1;
            else if (compare > 0) lo = mid + 1;
            else {
                // Search for key in right half of an array.
                result = mid;
                lo = mid + 1;
            }
        }
        return result;
    }

    // unit testing (required)
    public static void main(String[] args) {
        Integer[] a = {10, 20, 20, 20, 30, 40, 50, 60, 80, 90 , 90};

        Comparator<Integer> comparator = Integer::compareTo;

        int firstIndex = firstIndexOf(a, 90, comparator);
        int lastIndex = lastIndexOf(a, 20, comparator);

        System.out.println("First index of 90: " + firstIndex);
        System.out.println("Last index of 20: " + lastIndex);

        firstIndex = firstIndexOf(a, 90, comparator);
        if (firstIndex == -1) {
            System.out.println("no such key found");
        }
    }
}
