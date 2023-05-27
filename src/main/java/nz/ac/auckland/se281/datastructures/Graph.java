package nz.ac.auckland.se281.datastructures;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A graph that is composed of a set of verticies and edges.
 *
 * <p>You must NOT change the signature of the existing methods or constructor of this class.
 *
 * @param <T> The type of each vertex, that have a total ordering.
 */
public class Graph<T extends Comparable<T>> {

  private Set<T> verticies;
  private Set<Edge<T>> edges;

  public Graph(Set<T> verticies, Set<Edge<T>> edges) {
    this.verticies = verticies;
    this.edges = edges;
  }

  /**
   * Find the set of root verticies in the graph.
   *
   * @return The set of root verticies in the graph.
   */
  public Set<T> getRoots() {
    Set<T> roots = new HashSet<T>();

    return null;
  }

  /**
   * Calculates if the entire graph is reflexive
   *
   * @return True if a graph is reflexive, false otherwise.
   */
  public boolean isReflexive() {
    for (T vertex : verticies) {
      boolean hasSelfLoop = false;
      for (Edge<T> edge : edges) {
        if (edge.getSource().equals(vertex) && edge.getDestination().equals(vertex)) {
          hasSelfLoop = true;
          break;
        }
      }
      if (!hasSelfLoop) {
        return false;
      }
    }
    return true;
  }

  /**
   * Calculates if the entire graph is symmetric.
   *
   * @return True if the entire graph is symmetric, false otherwise.
   */
  public boolean isSymmetric() {
    for (Edge<T> edge : edges) {
      boolean hasReverseEdge = false;
      T source = edge.getSource();
      T destination = edge.getDestination();

      for (Edge<T> otherEdge : edges) {
        if (otherEdge.getSource().equals(destination)
            && otherEdge.getDestination().equals(source)) {
          hasReverseEdge = true;
          break;
        }
      }
      if (!hasReverseEdge) {
        return false;
      }
    }
    return true;
  }

  /**
   * Calculates if the entire graph is Transitive
   *
   * @return True if entire graph is transitive, false otherwise
   */
  public boolean isTransitive() {
    for (Edge<T> edge1 : edges) {}
  }

  public boolean isAntiSymmetric() {
    // TODO: Task 1.
    throw new UnsupportedOperationException();
  }

  public boolean isEquivalence() {
    // TODO: Task 1.
    throw new UnsupportedOperationException();
  }

  public Set<T> getEquivalenceClass(T vertex) {
    // TODO: Task 1.
    throw new UnsupportedOperationException();
  }

  public List<T> iterativeBreadthFirstSearch() {
    // TODO: Task 2.
    throw new UnsupportedOperationException();
  }

  public List<T> iterativeDepthFirstSearch() {
    // TODO: Task 2.
    throw new UnsupportedOperationException();
  }

  public List<T> recursiveBreadthFirstSearch() {
    // TODO: Task 3.
    throw new UnsupportedOperationException();
  }

  public List<T> recursiveDepthFirstSearch() {
    // TODO: Task 3.
    throw new UnsupportedOperationException();
  }
}
