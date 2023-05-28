package nz.ac.auckland.se281.datastructures;

/**
 * An edge in a graph that connects two verticies.
 *
 * <p>You must NOT change the signature of the constructor of this class.
 *
 * @param <T> The type of each vertex.
 */
public class Edge<T> {
  private T source;
  private T destination;

  public Edge(T source, T destination) {
    this.source = source;
    this.destination = destination;
  }

  /**
   * Getter which gets the node that the edge starts from.
   *
   * @return The node that the edge starts from.
   */
  public T getSource() {
    return source;
  }

  /**
   * Getter which gets the node that the edge ends at.
   *
   * @return The node that the edge ends at.
   */
  public T getDestination() {
    return destination;
  }
}
