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
    // Create a hashmap with T for the verticies and Integer for the number of edges.
    Map<T, Integer> mapForVertice = new HashMap<>();
    for (T vertex : verticies) {
      mapForVertice.put(vertex, 0);
    }
    // Check the number of edges for each vertex.
    for (Edge<T> edge : edges) {
      T destination = edge.getDestination();
      mapForVertice.put(destination, mapForVertice.get(destination) + 1);
    }
    // Add the verticies with no edges to the set of roots.
    Set<T> roots = new HashSet<>();
    // Iterate through the hashmap and add the verticies with no edges to the set of roots.
    for (Map.Entry<T, Integer> entry : mapForVertice.entrySet()) {
      if (entry.getValue() == 0) {
        roots.add(entry.getKey());
      }
    }
    // See if the node is an equivalance class and if it is return the minimum vertex.
    for (T vertex : verticies) {
      Set<T> equivalenceClass = getEquivalenceClass(vertex);
      if (equivalenceClass.size() > 0) {
        T minVertex = Collections.min(equivalenceClass);
        roots.add(minVertex);
      }
    }
    // Return set which contains all the verticies that are considered a root
    return roots;
  }

  /**
   * Calculates if the entire graph is reflexive.
   *
   * @return True if a graph is reflexive, false otherwise.
   */
  public boolean isReflexive() {
    // Loop through the verticies
    for (T vertex : verticies) {
      boolean hasSelfLoop = false;
      // Loop through the edges and check if there is a self loop.
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
    // Return true if there is a self loop.
    return true;
  }

  /**
   * Calculates if the entire graph is symmetric.
   *
   * @return True if the entire graph is symmetric, false otherwise.
   */
  public boolean isSymmetric() {
    // Iterate through the edges and check if there is a reverse edge.
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
   * Calculates if the entire graph is Transitive.
   *
   * @return True if entire graph is transitive, false otherwise.
   */
  public boolean isTransitive() {
    // Iterate through the edges edge 1
    for (Edge<T> edge1 : edges) {
      // Iterate through the edges edge 2
      for (Edge<T> edge2 : edges) {
        // Check if the destination of edge 1 is the same as the source of edge 2
        if (edge1.getDestination().equals(edge2.getSource())) {
          T source = edge1.getSource();
          T destination = edge2.getDestination();

          boolean hasDirectEdge = false;
          // If it is check if there is a direct edge between the source and destination
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
    // Return true if there is a direct edge.
    return true;
  }

  /**
   * Calculates if the entire graph is AntiSymmetric.
   *
   * @return True if entire graph is AntiSymmetric, false otherwise.
   */
  public boolean isAntiSymmetric() {
    // Iterate through the edges and check if there is a reverse edge.
    for (Edge<T> edge : edges) {
      // Create variables that store the edges source and destination
      T source = edge.getSource();
      T destination = edge.getDestination();
      // Check if the source and destination are the same
      for (Edge<T> edge1 : edges) {
        if (edge1.getSource().equals(destination) && edge1.getDestination().equals(source)) {
          // If they are the same return false
          return false;
        }
      }
    }
    // Return true if there is no reverse edge.
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
    // If the graph is not an equivalence relation return an empty set.
    if (!isEquivalence()) {
      Set<T> emptySet = new HashSet<T>();
      return emptySet;
    }
    // Create a new set and add the vertex to it.
    Set<T> equivalenceClass = new HashSet<T>();
    // Perform a depth first search on the vertex.
    List<T> depthFirstSearch = depthFirstSearchForEquiv(vertex);
    // Add all the verticies that were visited in the depth first search to the set.
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
    // Create a new list to store the visited verticies.
    List<T> visited = new ArrayList<T>();
    Stack<T> stack = new Stack<T>();
    stack.add(vertex);
    // While the stack is not empty
    while (!stack.isEmpty()) {
      // Pop the top vertex off the stack and set it as the currentVertex
      T currentVertex = stack.pop();
      if (!visited.contains(currentVertex)) {
        // Add the vertex to the visited list
        visited.add(currentVertex);
        // Add all the verticies that are adjacent to the currentVertex to the stack.
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
    // If the graph has no roots then return an empty list.
    if (roots.isEmpty()) {
      System.err.println("Graph has no roots");
    }
    // Create a new list to store the visited verticies.
    List<T> visited = new ArrayList<T>();
    PriorityQueue<T> queue = new PriorityQueue<T>();
    // Loop through each root
    for (T root : roots) {
      if (!visited.contains(root)) {
        queue.add(root);
        // Check if the queue is empty
        while (!queue.isEmpty()) {
          T currentVertex = queue.poll();
          if (!visited.contains(currentVertex)) {
            visited.add(currentVertex);
            // Get all the neighbors of the current vertex
            List<T> neighbors = new ArrayList<T>();
            for (Edge<T> edge : edges) {
              if (edge.getSource().equals(currentVertex)) {
                neighbors.add(edge.getDestination());
              }
            }
            // Sort the neighbors to ensure that the order is kept.
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
    // Get the roots of the graph
    Set<T> roots = getRoots();
    // If the graph has no roots then return "There are no roots in this graph"
    if (roots.isEmpty()) {
      System.out.println("There are no roots in this graph");
    }
    // Create a new list to store the visited verticies.
    List<T> visited = new ArrayList<T>();
    // Create a new stack to store the verticies that are to be visited.
    Deque<T> stack = new ArrayDeque<T>();
    // Loop through each root
    for (T root : roots) {
      if (!visited.contains(root)) {
        // Add the root to the stack
        stack.push(root);

        while (!stack.isEmpty()) {
          // Pop the top vertex off the stack and set it as the currentVertex
          T currentVertex = stack.pop();
          if (!visited.contains(currentVertex)) {
            visited.add(currentVertex);
            // Add all the verticies that are adjacent to the currentVertex to the stack.
            List<T> neighbors = new ArrayList<T>();
            for (Edge<T> edge : edges) {
              if (edge.getSource().equals(currentVertex)) {
                neighbors.add(edge.getDestination());
              }
            }
            // Sort the neighbors to ensure that the order is kept.
            Collections.sort(neighbors, Collections.reverseOrder());
            // Add all the neighbors to the stack
            for (T neighbor : neighbors) {
              stack.push(neighbor);
            }
          }
        }
      }
    }
    // Return the visited list
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
    // Get the roots of the graph
    Set<T> roots = getRoots();
    // If there are no roots then return that there are no root vertices in the graph
    if (roots.isEmpty()) {
      System.out.println("There are no root vertices in the graph");
    }
    // Create a new list to store the visited verticies. and queue to keep track of the vertices
    List<T> visited = new ArrayList<>();
    Queue<T> queue = new LinkedList<>();
    // Loop through each root
    for (T root : roots) {
      // If the root has not been visited then add it to the queue and the visited list
      if (!visited.contains(root)) {
        queue.add(root);
        visited.add(root);
        recursiveBreadthFirstSearch(queue, visited);
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
  private void recursiveBreadthFirstSearch(Queue<T> queue, List<T> visited) {
    // If the queue is not empty then poll the queue and set the current vertex to the polled
    if (!queue.isEmpty()) {
      T currentVertex = queue.poll();
      // Get all the neighbors of the current vertex
      List<T> neighbors = new ArrayList<>();
      // Loop through all the edges and add the neighbors to the list
      for (Edge<T> edge : edges) {
        if (edge.getSource().equals(currentVertex)) {
          neighbors.add(edge.getDestination());
        }
      }
      // Sort the neighbors to ensure that the order is kept.
      Collections.sort(neighbors);
      for (T neighbor : neighbors) {
        if (!visited.contains(neighbor)) {
          queue.add(neighbor);
          visited.add(neighbor);
        }
      }
      // Call the recursive function again
      recursiveBreadthFirstSearch(queue, visited);
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
    // Create a new list that contains the visited vertices
    List<T> visited = new ArrayList<>();
    for (T root : roots) {
      if (!visited.contains(root)) {
        // Perfrom the recursive DFS
        recursiveDepthFirstSearch(root, visited);
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
  private void recursiveDepthFirstSearch(T currentVertex, List<T> visited) {
    // Add the current vertex to the visited list
    visited.add(currentVertex);
    // Create a list to stroe neighbors of the current vertex
    List<T> neighbors = new ArrayList<>();
    for (Edge<T> edge : edges) {
      // If the edge is an outgoing edge from the current vertex
      if (edge.getSource().equals(currentVertex)) {
        // Add the neighbors to the list
        neighbors.add(edge.getDestination());
      }
    }
    // Sort the neighbors to ensure that the order is kept.
    Collections.sort(neighbors);
    for (T neighbor : neighbors) {
      if (!visited.contains(neighbor)) {
        recursiveDepthFirstSearch(neighbor, visited);
      }
    }
  }
}
