package nz.ac.auckland.se281.datastructures;

/**
 * A class that represents a single node in a linked list.
 *
 * @param <T> The type of the data that this node holds.
 */
public class Node<T> {
  protected T data;
  protected Node<T> next;

  /**
   * Constructs a new Node with the given data.
   *
   * @param data The data to be stored in this Node.
   */
  public Node(T data) {
    this.data = data;
    this.next = null;
  }
}
