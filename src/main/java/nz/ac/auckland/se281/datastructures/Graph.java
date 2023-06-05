package nz.ac.auckland.se281.datastructures;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
// import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
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
  private AdjacencyListGraph<T> adjacencyListGraph;

  public Graph(Set<T> verticies, Set<Edge<T>> edges) {
    this.verticies = verticies;
    this.edges = edges;
    this.adjacencyListGraph = new AdjacencyListGraph<T>();
    // populate adjacencyListGraph with edges
    for (Edge<T> edge : edges) {
      this.adjacencyListGraph.addEdge(edge.getSource(), edge.getDestination());
    }
  }

  public List<T> getNeighbors(T vertex) {
    return adjacencyListGraph.getNeighbors(vertex);
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

    List<T> orderedRoots = new ArrayList<>(roots);
    sortList(orderedRoots);

    Set<T> rootsOrdered = new LinkedHashSet<>(orderedRoots);

    // Return set which contains all the verticies that are considered a root
    return rootsOrdered;
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
      // If the loop has a self-edge then we should count this as an anti-symmetric edge.
      if (source.equals(destination)) {
        continue;
      }
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

    // Create a new list to store the visited vertices.
    List<T> visited = new ArrayList<T>();
    // Create a new set to quickly check visited vertices
    Set<T> visitedSet = new HashSet<T>();
    // Create a new stack to store the vertices that are to be visited.
    QuickStack<T> stack = new QuickStack<T>();
    // Add the root to the stack
    stack.push(vertex);

    while (!stack.isEmpty()) {
      // Pop the top vertex off the stack and set it as the currentVertex

      T currentVertex = stack.pop();
      if (!visitedSet.contains(currentVertex)) {
        visited.add(currentVertex);
        visitedSet.add(currentVertex);
        // Add all the vertices that are adjacent to the currentVertex to the stack.

        List<T> neighbors = getNeighbors(currentVertex);
        sortList(neighbors);
        Collections.reverse(neighbors);

        // Add all the neighbors to the stack
        for (T neighbor : neighbors) {
          stack.push(neighbor);
        }
      }
    }

    // Return the visited list
    return visited;
  }

  /**
   * Performs a breadth first search on the graph.
   *
   * @return List containing the visited verticies in the order they were visited.
   */
  public List<T> iterativeBreadthFirstSearch() {
    // Retrieve the roots of the graph
    Set<T> roots = getRoots();

    // Check if there are any roots in the graph
    if (roots.isEmpty()) {
      System.err.println("Graph has no roots");
    }

    // Initialize the visited vertices list, visited set, and the queue
    List<T> visited = new ArrayList<T>();
    Set<T> visitedSet = new HashSet<T>();
    QuickQueue<T> queue = new QuickQueue<T>();

    // Process each root in the graph
    for (T root : roots) {
      // Check if the root has been visited before
      if (!visitedSet.contains(root)) {
        // Add the root to the queue
        queue.enqueue(root);

        // Perform breadth-first search
        while (!queue.isEmpty()) {
          // Dequeue the current vertex from the queue
          T currentVertex = queue.dequeue();

          // Check if the current vertex has not been visited before
          if (!visitedSet.contains(currentVertex)) {
            // Add the current vertex to the visited list and set
            visited.add(currentVertex);
            visitedSet.add(currentVertex);

            // Get the neighbors of the current vertex
            List<T> neighbors = getNeighbors(currentVertex);

            // By using my sortList helper function I am going to sort the list. while the list has
            // class T I know it is either a string or an integer.
            sortList(neighbors);

            // Visit each neighbor and enqueue it if it hasn't been visited before
            for (T neighbor : neighbors) {
              if (!visitedSet.contains(neighbor) && !queue.contains(neighbor)) {
                queue.enqueue(neighbor);
              }
            }
          }
        }
      }
    }

    // Return the visited vertices in the order they were visited
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
    // Create a new list to store the visited vertices.
    List<T> visited = new ArrayList<T>();
    // Create a new set to quickly check visited vertices
    Set<T> visitedSet = new HashSet<T>();
    // Create a new stack to store the vertices that are to be visited.
    QuickStack<T> stack = new QuickStack<T>();
    // Loop through each root
    for (T root : roots) {
      if (!visitedSet.contains(root)) {
        // Add the root to the stack
        stack.push(root);

        while (!stack.isEmpty()) {
          // Pop the top vertex off the stack and set it as the currentVertex

          T currentVertex = stack.pop();
          if (!visitedSet.contains(currentVertex)) {
            visited.add(currentVertex);
            visitedSet.add(currentVertex);
            // Add all the vertices that are adjacent to the currentVertex to the stack.

            List<T> neighbors = getNeighbors(currentVertex);
            sortList(neighbors);
            Collections.reverse(neighbors);

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
   * Sorts the elements in the given list in ascending order. The elements are compared based on
   * their types: - If both elements are strings, they are parsed into integers and compared. - If
   * both elements are integers, they are compared directly.
   *
   * @param list the list of elements to be sorted
   * @throws IllegalArgumentException if the elements are not of type String or Integer
   */
  public void sortList(List<T> list) {
    Collections.sort(
        list,
        new Comparator<T>() {
          @Override
          public int compare(T obj1, T obj2) {
            // Compare elements based on their types
            if (obj1 instanceof String && obj2 instanceof String) {
              // If both elements are strings, parse them into integers and compare
              String str1 = (String) obj1;
              String str2 = (String) obj2;
              Integer int1 = Integer.parseInt(str1);
              Integer int2 = Integer.parseInt(str2);
              return int1.compareTo(int2);
            } else if (obj1 instanceof Integer && obj2 instanceof Integer) {
              // If both elements are integers, compare them directly
              Integer int1 = (Integer) obj1;
              Integer int2 = (Integer) obj2;
              return int1.compareTo(int2);
            } else {
              // Throw an exception if the elements are not of type String or Integer
              throw new IllegalArgumentException(
                  "Invalid type comparison: " + obj1.getClass() + " and " + obj2.getClass());
            }
          }
        });
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
    // Create a new list to store the visited vertices, and a set for quick lookup.
    List<T> visited = new ArrayList<>();
    Set<T> visitedSet = new HashSet<>();
    QuickQueue<T> queue = new QuickQueue<>();
    // Loop through each root
    for (T root : roots) {
      // If the root has not been visited then add it to the queue and the visited list
      if (!visitedSet.contains(root)) {
        queue.enqueue(root);
        visited.add(root);
        visitedSet.add(root);
        recursiveBreadthFirstSearch(queue, visited, visitedSet);
      }
    }
    return visited;
  }

  /**
   * I require a private helper for the recursive Breadth First Search This helper method will allow
   * me to call the recursive function for BFS While the other method keeps track of the queue.
   *
   * @param queue The queue that will be used to keep track of the vertices.
   * @param visited The list that will be used to keep track of the visited vertices.
   */
  public void recursiveBreadthFirstSearch(QuickQueue<T> queue, List<T> visited, Set<T> visitedSet) {
    // Base case: if the queue is not empty, continue the search
    if (!queue.isEmpty()) {
      // Dequeue the vertex from the queue
      T vertex = queue.dequeue();

      // Using the getNeighbors method, get the neighbors of the vertex. The code for is implement
      // in AdjacenyListGraph
      List<T> neighbors = getNeighbors(vertex);

      // Using my sortList method, sort the neighbors in ascending order this is important as I need
      // to go through the neighbours in an ascending order
      sortList(neighbors);

      // Visit each neighbor and enqueue it if it hasn't been visited before
      for (T neighbor : neighbors) {
        if (!visitedSet.contains(neighbor)) {
          visited.add(neighbor);
          visitedSet.add(neighbor);
          queue.enqueue(neighbor);
        }
      }

      // Recursive call to continue the breadth-first search
      recursiveBreadthFirstSearch(queue, visited, visitedSet);
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
    Set<T> visitedSet = new HashSet<>();
    for (T root : roots) {
      if (!visitedSet.contains(root)) {
        // Perform the recursive DFS
        recursiveDepthFirstSearch(root, visited, visitedSet);
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
  private void recursiveDepthFirstSearch(T root, List<T> visited, Set<T> visitedSet) {
    visited.add(root);
    visitedSet.add(root);
    // Assume getNeighbors is a method that returns a list of neighbors
    List<T> neighbors = getNeighbors(root);
    sortList(neighbors);
    for (T neighbor : neighbors) {
      if (!visitedSet.contains(neighbor)) {
        recursiveDepthFirstSearch(neighbor, visited, visitedSet);
      }
    }
  }
}
