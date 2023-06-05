package nz.ac.auckland.se281.datastructures;

/**
 * A class that represents a singly-linked list.
 *
 * @param <T> The type of the data that this list holds.
 */
public class ListLinked<T> {
  private Node<T> head;
  private Node<T> tail;
  private int size;

  /** Constructs a new, empty ListLinked. */
  public ListLinked() {
    this.head = null;
    this.tail = null;
    this.size = 0;
  }

  /**
   * Adds a new element at the beginning of the list.
   *
   * @param elem The element to be added to the list.
   */
  public void addFirst(T elem) {
    Node<T> node = new Node<T>(elem);
    if (isEmpty()) {
      head = tail = node;
    } else {
      node.next = head;
      head = node;
    }
    size++;
  }

  /**
   * Checks if the list contains the given element.
   *
   * @param elem The element to be checked for its presence in the list.
   * @return true if the element is found in the list, false otherwise.
   */
  public boolean contains(T elem) {
    Node<T> current = head;
    while (current != null) {
      if (current.data.equals(elem)) {
        return true;
      }
      current = current.next;
    }
    return false;
  }

  /**
   * Adds a new element at the end of the list.
   *
   * @param elem The element to be added to the list.
   */
  public void addLast(T elem) {
    Node<T> node = new Node<T>(elem);
    if (isEmpty()) {
      head = tail = node;
    } else {
      tail.next = node;
      tail = node;
    }
    size++;
  }

  /**
   * Removes and returns the first element in the list.
   *
   * @return The first element in the list, or null if the list is empty.
   */
  public T removeFirst() {
    if (isEmpty()) {
      return null;
    }

    T data = head.data;
    head = head.next;
    size--;

    if (isEmpty()) {
      tail = null;
    }

    return data;
  }

  /**
   * Removes and returns the last element in the list.
   *
   * @return The last element in the list, or null if the list is empty.
   */
  public T removeLast() {
    if (isEmpty()) {
      return null;
    }

    T data;
    if (head == tail) {
      data = head.data;
      head = tail = null;
    } else {
      Node<T> current = head;
      while (current.next != tail) {
        current = current.next;
      }

      data = tail.data;
      tail = current;
      tail.next = null;
    }
    size--;

    return data;
  }

  /**
   * Checks if the list is empty.
   *
   * @return true if the list is empty, false otherwise.
   */
  public boolean isEmpty() {
    return size == 0;
  }

  /**
   * Returns the number of elements in the list.
   *
   * @return The number of elements in the list.
   */
  public int getSize() {
    return size;
  }
}
