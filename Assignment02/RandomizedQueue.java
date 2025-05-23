import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    //instance variables
    private Item[] queue;    //array for storing the items.
    private int    size ;    //stores the size of the array.
    private int    capacity; //stores the maximum size of array.

    // construct an empty randomized queue
    public RandomizedQueue(){
        size = 0;     //initially size to be zero.
        capacity = 10;// setting initial cpacity to 10.
        queue = (Item[]) new Object[capacity];
    }

    // is the randomized queue empty?
    public boolean isEmpty(){
        return size == 0;
    }

    // return the number of items on the randomized queue
    public int size(){
        return size;
    }

    // add the item
    public void enqueue(Item item){
        //throws IllegalArgumentException if the client calls enqueue() with a null argument.
        if(item == null){
            throw new IllegalArgumentException("argument cannot be null.");
        }
        //checks if capcity of array is full, if so call resize() : doubles the capacity.
        if(size == capacity){
            resize();
        }
        //else adds item to end of the queue and increments the size.
        queue[size++] = item;
    }

    // Resize the array when it's full
    private void resize() {
        capacity *= 2;
        Item[] newQueue = (Item[]) new Object[capacity];
        //copies the old array to new array.
        for (int i = 0; i < size; i++) {
            newQueue[i] = queue[i];
        }
        queue = newQueue;
    }

    // remove and return a random item
    public Item dequeue(){
        //throws error if the client calls dequeue() when the deque is empty.
        if(isEmpty()){
            throw new NoSuchElementException("queue is empty.");
        }
        //calculate the random index number to be removed.
        int randomIndex = (int)(Math.random() * size);
        //stores the item at the random index
        Item item = queue[randomIndex];
        //swaps the positions.
        queue[randomIndex] = queue[size-1];
        queue[size-1] = queue[randomIndex];
        //removes the last element.
        queue[size - 1] = null;
        size--;
        //return removed random item.
        return item;
    }

    // return a random item (but do not remove it)
    public Item sample(){
        //throws error if the client calls sample() when the queue is empty.
        if(isEmpty()){
            throw new NoSuchElementException("queue is empty.");
        }
        //calculate the random index number to be removed.
        int randomIndex = (int)(Math.random() * size);
        //return random item.
        return queue[randomIndex];
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator(){
        return new RandomizedIterator();
    }

    private class RandomizedIterator implements Iterator<Item>{
        private final Item[] shuffledQueue;
        private int index;

        public RandomizedIterator() {
            //Copy all items into an array
            shuffledQueue = (Item[]) new Object[size];
            for (int i = 0; i < size; i++) {
                shuffledQueue[i] = queue[i];
            }

            //Shuffle the array
            for (int j = 0; j < size; j++) {
                int rand = j + (int) (Math.random() * (size - j));
                Item temp = shuffledQueue[j];
                shuffledQueue[j] = shuffledQueue[rand];
                shuffledQueue[rand] = temp;
            }

            index = 0;
        }

        public boolean hasNext() {
            return index < shuffledQueue.length;
        }

        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            return shuffledQueue[index++];
        }
    }
    // unit testing (required)
    public static void main(String[] args){
        RandomizedQueue<Integer> rq = new RandomizedQueue<>();

        rq.enqueue(10);
        rq.enqueue(20);
        rq.enqueue(30);
        rq.enqueue(40);
        rq.enqueue(50);

        System.out.println("isempty? : " +rq.isEmpty());//false
        System.out.println("Size : " +rq.size());//5

        System.out.println("sample : " +rq.sample());
        System.out.println("Size after calling sample() : " +rq.size());

        System.out.println("remove item : " +rq.dequeue());
        System.out.println("Size after calling dequeue() : " +rq.size());

        System.out.println("remove item : " +rq.dequeue());
        System.out.println("Size after calling dequeue() : " +rq.size());

        Iterator<Integer> it = rq.iterator();
        while (it.hasNext()) {
            System.out.println(it.next());
        }
    }
}
