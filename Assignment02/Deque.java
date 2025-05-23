import java.util.Iterator;
import java.util.NoSuchElementException;
//Adapted from:Robert Sedgewick, Kevin Wayne - A ... on-Wesley Professional (2011).pdf

public class Deque<Item> implements Iterable<Item> {
    //instance variables
    private Node front; // points to the most recently added node or removed node from the beginning of list.
    private Node back;  // points to the most recently added node or removed node from the ending of list.
    private int N;      // number of items on the queue

    // nested class to define nodes
    private  class Node
    {
        Item item;//stores the data
        Node next;//pointer to point the next node.
        Node prev;//pointer to point the previous node.
    }

    // construct an empty deque
    public Deque(){
        //assigns front and back initially to be null.
        front = null;
        back  = null;
        //initially list is empty hence  N = 0.
        N = 0;
    }

    // is the deque empty?
    public boolean isEmpty(){
        //if N = 0 ; then list is empty returns true
        //otherwise ,  false.
        return N == 0;
    }

    // return the number of items on the deque
    public int size(){
        return N;
    }

    // add the item to the front
    public void addFirst(Item item){
        //throws IllegalArgumentException if the client calls addFirst() with a null argument.
        if(item == null){
            throw new IllegalArgumentException("argument cannot be null.");
        }
        // Add item to the start of the list.
        Node oldfirst = front;//reference to current first node(before adding).
        front = new Node();
        front.item = item;
        front.next = oldfirst;
        front.prev = null;//NO node before the first node.
        if (isEmpty()) {
            back = front;
        }
        else{
            oldfirst.prev = front;//sets the oldfirst previous pointer from null to front node.
        }
        N++;
    }

    // add the item to the back
    public void addLast(Item item){
        //throws IllegalArgumentException if the client calls addLast() with a null argument.
        if(item == null){
            throw new IllegalArgumentException("argument cannot be null.");
        }
        // Add item to the end of the list.
        Node oldlast = back;//reference to current last node(before adding).
        back = new Node();
        back.item = item;
        back.next = null;
        back.prev = oldlast;
        if (isEmpty()){
            front = back;
        }
        else {
            oldlast.next = back;
        }
        N++;//onc node add increments the counter
    }

    // remove and return the item from the front
    public Item removeFirst(){
        //throws error if the client calls  removeFirst() when the deque is empty.
        if(isEmpty()){
            throw new NoSuchElementException("queue is empty.");
        }
        // Remove item from the beginning of the list.
        Item item = front.item;
        front = front.next;
        N--;
        if (isEmpty()) {
            back = null;
        }
        if(front != null){
            front.prev = null;//sets the previous pointer of new first node as null
        }
        return item;
    }

    // remove and return the item from the back
    public Item removeLast(){
        //throws error if the client calls removeLast() when the deque is empty.
        if(isEmpty()){
            throw new NoSuchElementException("queue is empty.");
        }
        // Remove item from the ending of the list.
        Item item = back.item;
        if(front == back){//only one node in the list
            front = null;
            back = null;
        }
        else {
            back = back.prev;//will point to the second last back node in the list.
            back.next = null;//sets the next pointer of new back node to null.
        }
        N--;
        return item;
    }


    private class DequeIterator implements Iterator<Item> {
        private Node current = front;

        public boolean hasNext(){
            return current != null;
        }

        public Item next()
        {
            if(current == null){
                throw new NoSuchElementException("no item to return");
            }
            Item item = current.item;
            current = current.next;
            return item;
        }
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator(){
        return new DequeIterator();
    }

    // unit testing (required)
    public static void main(String[] args){
        Deque<Integer> d = new Deque<>();
        System.out.println("isempty? : " +d.isEmpty());
        d.addFirst(10);
        d.addLast(20);
        d.addFirst(5);
        System.out.println("isempty? : " +d.isEmpty());
        System.out.println("Size : " +d.size());
        Iterator<Integer> it = d.iterator();
        while(it.hasNext()) {
            System.out.println(it.next());
        }

        System.out.println("removed  element " +d.removeFirst());
        System.out.println("removed  element " +d.removeLast());

        Iterator<Integer> it2 = d.iterator();
        while(it2.hasNext()) {
            System.out.println(it2.next());
        }


    }

}
