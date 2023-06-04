package nz.ac.auckland.se281.datastructures;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * A graph implementation using an adjacency list representation.
 *
 * @param <T> the type of elements in the graph
 */
public class AdjacencyListGraph<T extends Comparable<T>> {

  private Map<T, List<T>> adjacencyList;

  /** Constructs an empty AdjacencyListGraph. */
  public AdjacencyListGraph() {
    this.adjacencyList = new TreeMap<>();
  }

  /**
   * Adds an edge between two vertices in the graph.
   *
   * @param source the source vertex
   * @param destination the destination vertex
   */
  public void addEdge(T source, T destination) {
    // Add destination to source's adjacency list
    List<T> sourceNeighbors = adjacencyList.computeIfAbsent(source, k -> new ArrayList<>());
    sourceNeighbors.add(destination);
  }

  /**
   * Retrieves the neighbors (outgoing edges) of a vertex in the graph.
   *
   * @param vertex the vertex for which to retrieve the neighbors
   * @return a list of neighbors (outgoing edges) of the vertex
   */
  public List<T> getNeighbors(T vertex) {
    // Retrieve the adjacency list of the vertex
    List<T> neighbors = adjacencyList.get(vertex);

    // If the adjacency list is null, return an empty list
    if (neighbors == null) {
      return new ArrayList<>();
    }

    // Return the neighbors as outgoing edges
    return new ArrayList<>(neighbors);
  }
}
