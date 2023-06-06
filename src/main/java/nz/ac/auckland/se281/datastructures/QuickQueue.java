package nz.ac.auckland.se281.datastructures;

/**
 * A QuickQueue data structure which is a singly-linked list, implemented off my ListLinked class.
 *
 * @param <T> The type of the data that this list holds.
 */
public class QuickQueue<T> {
  private ListLinked<T> list = new ListLinked<T>();

  /**
   * Enqueues an element to the end of the queue in O(1).
   *
   * @param elem The element to be added to the queue.
   */
  public void enqueue(T elem) {
    // If the element is not already in the queue, add it to the end.
    if (!list.contains(elem)) {
      list.addLast(elem);
    }
  }

  // Removes an element from the front of the queue in O(1)
  public T dequeue() {
    T elem = list.removeFirst();
    return elem;
  }

  // Checks if an element exists in the queue in O(n)
  public boolean contains(T elem) {
    return list.contains(elem);
  }

  // Checks if the queue is empty in O(1)
  public boolean isEmpty() {
    return list.isEmpty();
  }
}
