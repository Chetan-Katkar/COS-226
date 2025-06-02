import edu.princeton.cs.algs4.StdIn;
public class Permutation {
    public static void main(String[] args) {
        //reads the command line argument k.
        int k = Integer.parseInt(args[0]);

        RandomizedQueue<String> queue = new RandomizedQueue<>();

        //enqueues the data from the file
        while(!StdIn.isEmpty()){
           queue.enqueue(StdIn.readString());
        }

        //prints random data from the queue
        for(int i = 0; i < k ; i++){
            System.out.println(queue.sample());
        }
    }
}
