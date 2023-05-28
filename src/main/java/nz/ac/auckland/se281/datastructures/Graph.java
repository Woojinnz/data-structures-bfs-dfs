package nz.ac.auckland.se281.datastructures;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

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
   * Find the set of root verticies in the graph. Creates a hashmap.
   *
   * @return The set of root verticies in the graph.
   */
  public Set<T> getRoots() {
    Map<T, Integer> Map = new HashMap<>();
    for (T vertex : verticies) {
      Map.put(vertex, 0);
    }
    for (Edge<T> edge : edges) {
      T destination = edge.getDestination();
      Map.put(destination, Map.get(destination) + 1);
    }

    Set<T> roots = new HashSet<>();
    for (Map.Entry<T, Integer> entry : Map.entrySet()) {
      if (entry.getValue() == 0) {
        roots.add(entry.getKey());
      }
    }

    for (T vertex : verticies) {
      Set<T> equivalenceClass = getEquivalenceClass(vertex);
      if (equivalenceClass.size() > 0) {
        T minVertex = Collections.min(equivalenceClass);
        roots.add(minVertex);
      }
    }

    return roots;
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
    for (Edge<T> edge1 : edges) {
      for (Edge<T> edge2 : edges) {
        if (edge1.getDestination().equals(edge2.getSource())) {
          T source = edge1.getSource();
          T destination = edge2.getDestination();

          boolean hasDirectEdge = false;

          for (Edge<T> edge3 : edges) {
            if (edge3.getSource().equals(source) && edge3.getDestination().equals(destination)) {
              hasDirectEdge = true;
              break;
            }
          }
          if (!hasDirectEdge) {
            return false;
          }
        }
      }
    }
    return true;
  }

  /**
   * Calculates if the entire graph is AntiSymmetric
   *
   * @return True if entire graph is AntiSymmetric, false otherwise
   */
  public boolean isAntiSymmetric() {
    for (Edge<T> edge : edges) {
      T source = edge.getSource();
      T destination = edge.getDestination();

      for (Edge<T> edge1 : edges) {
        if (edge1.getSource().equals(destination) && edge1.getDestination().equals(source)) {
          return false;
        }
      }
    }
    return true;
  }

  /**
   * Calculates if the entire graph is an Equivalence Relation.
   *
   * @return True if entire graph is an Equivalence Relation, false otherwise
   */
  public boolean isEquivalence() {
    return isReflexive() && isSymmetric() && isTransitive();
  }

  /**
   * Calculates vertex is an equivalance and if so returns a set of all the other verticies that are
   * in the same equivalence class.
   *
   * @param vertex The vertex to check for the Equivalence class
   * @return A set of all the other verticies that are in the same equivalence class.
   */
  public Set<T> getEquivalenceClass(T vertex) {
    if (!isEquivalence()) {
      Set<T> emptySet = new HashSet<T>();
      return emptySet;
    }
    Set<T> equivalenceClass = new HashSet<T>();
    List<T> depthFirstSearch = depthFirstSearchForEquiv(vertex);
    equivalenceClass.addAll(depthFirstSearch);
    return equivalenceClass;
  }

  /**
   * Performs a depth first search on the graph. This is done by a Stack to ensure that O(1) is kept
   *
   * @param vertex The vertex to start the search from
   * @return List containing the visited verticies in the order they were visited.
   */
  public List<T> depthFirstSearchForEquiv(T vertex) {
    List<T> visited = new ArrayList<T>();
    Stack<T> stack = new Stack<T>();
    stack.add(vertex);

    while (!stack.isEmpty()) {
      T currentVertex = stack.pop();
      if (!visited.contains(currentVertex)) {
        visited.add(currentVertex);

        for (Edge<T> edge : edges) {
          if (edge.getSource().equals(currentVertex)) {
            stack.add(edge.getDestination());
          }
        }
      }
    }
    return visited;
  }

  /**
   * Performs a breadth first search on the graph. This is done by a Queue to ensure that O(1) is We
   * must use a priority queue to ensure that the order of the verticies is kept in order. This is
   * done by the Comparable interface.
   *
   * @return List containing the visited verticies in the order they were visited.
   */
  public List<T> iterativeBreadthFirstSearch() {
    Set<T> roots = getRoots();
    if (roots.isEmpty()) {
      throw new IllegalStateException("There are no root vertices in the graph");
    }

    List<T> visited = new ArrayList<T>();
    PriorityQueue<T> queue = new PriorityQueue<T>();

    for (T root : roots) {
      if (!visited.contains(root)) {
        queue.add(root);

        while (!queue.isEmpty()) {
          T currentVertex = queue.poll();
          if (!visited.contains(currentVertex)) {
            visited.add(currentVertex);

            List<T> neighbors = new ArrayList<T>();
            for (Edge<T> edge : edges) {
              if (edge.getSource().equals(currentVertex)) {
                neighbors.add(edge.getDestination());
              }
            }

            Collections.sort(neighbors);
            queue.addAll(neighbors);
          }
        }
      }
    }
    return visited;
  }

  /**
   * It calcualtes the starting roots based off getRoots() and then performs an iterative depth
   * first search on the graph. This is done by a Stack to ensure that O(1) is kept
   *
   * @return List containing the visited verticies in the order they were visited.
   */
  public List<T> iterativeDepthFirstSearch() {
    Set<T> roots = getRoots();
    if (roots.isEmpty()) {
      System.out.println("There are no roots in this graph");
    }
    List<T> visited = new ArrayList<T>();
    Deque<T> stack = new ArrayDeque<T>();

    for (T root : roots) {
      if (!visited.contains(root)) {
        stack.push(root);

        while (!stack.isEmpty()) {
          T currentVertex = stack.pop();
          if (!visited.contains(currentVertex)) {
            visited.add(currentVertex);

            List<T> neighbors = new ArrayList<T>();
            for (Edge<T> edge : edges) {
              if (edge.getSource().equals(currentVertex)) {
                neighbors.add(edge.getDestination());
              }
            }

            Collections.sort(neighbors, Collections.reverseOrder());
            for (T neighbor : neighbors) {
              stack.push(neighbor);
            }
          }
        }
      }
    }

    return visited;
  }

  /**
   * It calcualtes the starting roots based off getRoots() and then performs a recruisve breadth
   * first search on the graph. This is done by a Queue to ensure that O(1) is kept. This method
   * will keep track of the queue and the visited list and the helper function recursiveBFS is used
   * to run the recursive function
   *
   * @return List containing the visited verticies in the order they were visited.
   */
  public List<T> recursiveBreadthFirstSearch() {
    Set<T> roots = getRoots();
    if (roots.isEmpty()) {
      System.out.println("There are no root vertices in the graph");
    }

    List<T> visited = new ArrayList<>();
    Queue<T> queue = new LinkedList<>();

    for (T root : roots) {
      if (!visited.contains(root)) {
        queue.add(root);
        visited.add(root);
        recursiveBFS(queue, visited);
      }
    }

    return visited;
  }

  /**
   * I require a private helper for the recursive Breadth First Search This helper method will allow
   * me to call the recursive function for BFS While the other method keeps track of the queue
   *
   * @param queue The queue that will be used to keep track of the vertices
   * @param visited The list that will be used to keep track of the visited vertices
   */
  private void recursiveBFS(Queue<T> queue, List<T> visited) {
    if (!queue.isEmpty()) {
      T currentVertex = queue.poll();

      List<T> neighbors = new ArrayList<>();
      for (Edge<T> edge : edges) {
        if (edge.getSource().equals(currentVertex)) {
          neighbors.add(edge.getDestination());
        }
      }

      Collections.sort(neighbors);
      for (T neighbor : neighbors) {
        if (!visited.contains(neighbor)) {
          queue.add(neighbor);
          visited.add(neighbor);
        }
      }

      recursiveBFS(queue, visited);
    }
  }

  /**
   * It calcualtes the starting roots based off getRoots() and then performs a recruisve depth first
   * As the method cannot have any parameters this method will simply only be used to store a list
   * of the visited vertices. The recursiveDFS is a helper method which will be called to perform
   * the recursive function
   *
   * @return List containing the visited verticies in the order they were visited.
   */
  public List<T> recursiveDepthFirstSearch() {
    Set<T> roots = getRoots();
    if (roots.isEmpty()) {
      System.out.println("There are no root vertices in the graph");
    }

    List<T> visited = new ArrayList<>();
    for (T root : roots) {
      if (!visited.contains(root)) {
        recursiveDFS(root, visited);
      }
    }
    return visited;
  }

  /**
   * The helper method for the recursive depth first search. This method will be called recursively
   * to perform the recursive function
   *
   * @param currentVertex The current vertex that is being visited
   * @param visited The list that will be used to keep track of the visited vertices
   */
  private void recursiveDFS(T currentVertex, List<T> visited) {
    visited.add(currentVertex);

    List<T> neighbors = new ArrayList<>();
    for (Edge<T> edge : edges) {
      if (edge.getSource().equals(currentVertex)) {
        neighbors.add(edge.getDestination());
      }
    }

    Collections.sort(neighbors);
    for (T neighbor : neighbors) {
      if (!visited.contains(neighbor)) {
        recursiveDFS(neighbor, visited);
      }
    }
  }
}
