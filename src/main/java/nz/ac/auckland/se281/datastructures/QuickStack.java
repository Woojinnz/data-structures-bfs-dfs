package nz.ac.auckland.se281.datastructures;

/**
 * A QuickStack data structure which is a singly-linked list, implemented off my ListLinked class.
 *
 * @param <T> The type of the data that this list holds.
 */
public class QuickStack<T> {
  private ListLinked<T> list = new ListLinked<T>();

  // Pushes an element to the top of the stack in O(1)
  public void push(T elem) {
    if (!contains(elem)) {
      list.addFirst(elem);
    }
  }

  // Removes an element from the top of the stack in O(1)
  public T pop() {
    return list.removeFirst();
  }

  // Checks if an element exists in the stack in O(n)
  public boolean contains(T elem) {
    return list.contains(elem);
  }

  // Checks if the stack is empty in O(1)
  public boolean isEmpty() {
    return list.isEmpty();
  }
}
