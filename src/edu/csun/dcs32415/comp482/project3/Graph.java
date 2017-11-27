// Daniel Schetritt
package edu.csun.dcs32415.comp482.project3;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Graph { //------------------------------------------------------
    public static final int INFINITY = Integer.MAX_VALUE;
    private ArrayList<EdgeNode>[] adjList;
    private int nVertices;
    private int nEdges;
    // Traditional definition of a graph:
    private Set<Integer> vertexSet;
    private Set<EdgeNode> edgeSet;
    /**
     * Calculated by isStronglyConnected.
     *
     * @var A set of maximal strongly connected components, sorted in descending order by cardinality.
     */
    private SortedSet<Set<Integer>> stronglyConnectedComponents;
    private String fileName;

    /******************  Constructor**********************/
    /**
     * Construct the graph by specification from a file whose name is contained in the given string, inputFileName.
     *
     * @param inputFileName
     */
    public Graph(String inputFileName) {
        try {
            init(new Scanner(new File(inputFileName)));
            fileName = inputFileName;
        } catch (FileNotFoundException e) {
            System.out.println("Error reading input file. Reading from standard input...\n" +
                    "Please enter the contents of the input file all at once, followed by a blank line: ");
            init();
        }
    }

    /**
     * Constructor using Standard Input instead of a file name. Useful for testing.
     */
    private Graph() {
        init();
    }

    /************Main Program*******************************/
    public static void main(String[] args) {
        String[] inputs = {""};
        if (args.length > 0) {
            inputs = args;
        }
        for (String input : inputs) {
            Graph g = new Graph(input);
            System.out.printf("Input: %s%n");
            g.printGraph();
            System.out.printf("Is strongly connected: %s.%n", g.isStronglyConnected());
            System.out.println("Strongly Connected components:");
            System.out.println(g.getStronglyConnectedComponents());
            for (int i = 0; i < g.countVertices(); i++) {
                System.out.println("BFS: ");
                g.printShortestPaths(g.bfsShortestPaths(i));
                System.out.println("Dijkstra: ");
                g.printShortestPaths(g.dijkstraShortestPaths(i));
                System.out.println("Bellman Ford: ");
                g.printShortestPaths(g.bellmanFordShortestPaths(i));
                System.out.println("-----------------------------------");
            }
        }


    }

    /**
     * Private constructor-helper to initialize the graph from any input stream of integers.
     * The program will crash if the input stream is malformed.
     *
     * @param scanner The scanner of the input stream containing the graph in the following format:
     *                - The first integer should be the number of vertices.
     *                - The remaining integers should represent the edges.
     *                - An edge should be an three consecutive integers: sourceVertexId, destinationVertexId, and weight.
     *                - Vertex IDs should be unique and non-negative, and the highest-numbered ID should be nVertices - 1.
     */
    private void init(Scanner scanner) {
        //Regex for ignoring characters that do not make signed integers.
        // This is just for skipping over comments. If you try something like: "-023++44-1", the scanner will wrongly
        // consider it an integer.
        scanner.useDelimiter("[^-+0-9]+");

        // Initialize vertices.
        nVertices = Integer.parseInt(scanner.next());
        adjList = new ArrayList[nVertices];
        vertexSet = new HashSet<>();
        for (int vertex = 0; vertex < nVertices; vertex++) {
            adjList[vertex] = new ArrayList<>();
            vertexSet.add(vertex);
        }
        // Initialize Edges
        edgeSet = new HashSet<>();
        int[] rawEdge = new int[3];
        while (scanner.hasNext()) {
            for (int i = 0; i < rawEdge.length; i++) {
                rawEdge[i] = Integer.parseInt(scanner.next());
            }
            EdgeNode edge = new EdgeNode(rawEdge);
            adjList[rawEdge[0]].add(edge);
            edgeSet.add(edge);
        }
        nEdges = edgeSet.size();

        // SCCs
        computeStronglyConnectedComponents();
    }

    /**
     * A private constructor-helper to initialize the graph from Standard Input, terminated by a blank line.
     */
    private void init() {
        Scanner scanner = new Scanner(System.in);

        // Essentially, this takes the entire input as one string
        scanner.useDelimiter("\n\n");
        String input = scanner.next();

        // Reset the scanner now point to the input which we just read.
        scanner = new Scanner(input);

        //Parse the input.
        init(scanner);
    }

    /******************Print graph method***************/
    public void printGraph() {
        System.out.println(this);
    }

    /******************* BFS Shortest paths  ******************/
    public SPPacket bfsShortestPaths(int start) {
        boolean[] exploredVertex = new boolean[nVertices];
        Set<EdgeNode> exploredEdges = new HashSet<>(nEdges);
        List<Set<Integer>> levels = new ArrayList<>(nVertices);
        int[] dist = new int[nVertices];
        int[] parent = new int[nVertices];
        for (int i = 0; i < nVertices; i++) {
            exploredVertex[i] = false;
            dist[i] = INFINITY;
            parent[i] = -1;
        }
        // Explore the start vertex.
        levels.add(new HashSet<>());
        levels.get(0).add(start);
        exploredVertex[start] = true;
        // Explore the rest.
        int i = 0;
        while (!levels.get(i).isEmpty()) {
            levels.add(new HashSet<>());
            for (int vertex : levels.get(i)) {
                dist[vertex] = i;
                for (EdgeNode edge : adjList[vertex]) {
                    if (!exploredVertex[edge.toVertex]) {
                        parent[edge.toVertex] = edge.fromVertex;
                        levels.get(i + 1).add(edge.toVertex);
                    }
                    exploredVertex[edge.toVertex] = true;
                    exploredEdges.add(edge);
                }
            }
            i++;
        }
        return new SPPacket(start, dist, parent);
    }

    private void relaxEdge(EdgeNode edge, int[] dist, int[] parent) {
        // The cost of the path to edge.toVertex using this edge, treating infinity mathematically correctly.
        int edgeCost = dist[edge.fromVertex] == INFINITY ? INFINITY : dist[edge.fromVertex] + edge.weight;
        if (edgeCost < dist[edge.toVertex]) {
            parent[edge.toVertex] = edge.fromVertex;
            dist[edge.toVertex] = edgeCost;
        }
    }

    /********************Dijkstra's Shortest Path Algorithm*** */
    public SPPacket dijkstraShortestPaths(int start) {
        // Detect negative weights.
        for (EdgeNode edge : edgeSet) {
            if (edge.weight < 0) {
                return null;
            }
        }

        //No negative weights. Do Dijkstra's SPA.

        // Initialization.
        Set<Integer> unexploredVertices = new HashSet<>(vertexSet);
        int[] dist = new int[nVertices];
        int[] parent = new int[nVertices];
        for (int i = 0; i < nVertices; i++) {
            dist[i] = INFINITY;
            parent[i] = -1;
        }
        dist[start] = 0;

        // While we still have some vertices left to explore.
        while (!unexploredVertices.isEmpty()) {
            // Greedily choose the nearest vertex.
            int nearestVertex = -1;
            int proximity = INFINITY;
            for (Integer unexploredVertex : unexploredVertices) {
                if (dist[unexploredVertex] <= proximity) {
                    proximity = dist[unexploredVertex];
                    nearestVertex = unexploredVertex;
                }
            }
            // Explore the nearest vertex.
            unexploredVertices.remove(nearestVertex);
            // Check all its outgoing edges.
            for (EdgeNode edge : adjList[nearestVertex]) {
                // If a neighbor is unexplored, relax the edge.
                if (unexploredVertices.contains(edge.toVertex)) {
                    relaxEdge(edge, dist, parent);
                }
            }
        }
        return new SPPacket(start, dist, parent);
    }

    /********************Bellman Ford Shortest Paths ***************/
    public SPPacket bellmanFordShortestPaths(int start) {
        int[] dist = new int[nVertices];
        int[] parent = new int[nVertices];
        for (int i = 0; i < nVertices; i++) {
            dist[i] = INFINITY;
            parent[i] = -1;
        }
        dist[start] = 0;
        for (int i = 0; i < nVertices - 1; i++) {
            //Relax all Edges
            for (EdgeNode edge : edgeSet) {
                relaxEdge(edge, dist, parent);
            }
        }
        // Finally, Try to relax all edges again to detect if there are negative-weight cycles.
        for (EdgeNode edge : edgeSet) {
            // The cost of the path to edge.toVertex using this edge, treating infinity mathematically correctly.
            int edgeCost = dist[edge.fromVertex] == INFINITY ? INFINITY : dist[edge.fromVertex] + edge.weight;
            if (edgeCost < dist[edge.toVertex]) {
                // There is a negative-weight cycle.
                return null;
            }
        }

        return new SPPacket(start, dist, parent);
    }

    /***********************Prints shortest paths*************************/
    public void printShortestPaths(SPPacket spp) {
        System.out.println(spp);
    }

    /*****************isStronglyConnected***************************/
    public boolean isStronglyConnected() {
        if (stronglyConnectedComponents == null || stronglyConnectedComponents.isEmpty()) {
            computeStronglyConnectedComponents();
        }
        // By definition, a strongly connected component C of graph G=(V,E) is a a subset of G in which there exist paths
        // to all vertices in C from each vertex in C.
        // Trivially, this implies that if G has N vertices and 0 edges, then there are exactly N unique SCCs in G,
        // each containing exactly one vertex.

        // We just computed all a set S containing all SCCs of G.
        // Therefore, we can conclude that G, itself, is an SCC if S contains V.
        return stronglyConnectedComponents.contains(vertexSet);
    }

    /**
     * Compute a set of Strongly connected components.
     * Represented as a set of sets of vertices.
     * Result is stored in an instance variable by the doSccSearch function.
     * https://en.wikipedia.org/wiki/Path-based_strong_component_algorithm
     */
    private void computeStronglyConnectedComponents() {
        int visitedVertexCount = 0;
        int[] preorderNumber = new int[nVertices];
        Stack<Integer> unassignedVertices = new Stack<>(); // unassigned to a SCC
        Stack<Integer> undistinguishedVertices = new Stack<>(); //undistinguished from other SCCs.
        for (int vertex = 0; vertex < nVertices; vertex++) {
            preorderNumber[vertex] = -1;
        }
        // Initialize a set of Strongly connected components sorted by the number of vertices, descending.
        stronglyConnectedComponents = new TreeSet<>(Comparator.comparingInt(set -> -set.size()));
        for (int vertex = 0; vertex < nVertices; vertex++) {
            doSccSearch(vertex, visitedVertexCount, preorderNumber, unassignedVertices, undistinguishedVertices);
        }

        /* Keep only  maximal SCCs. */

        SortedSet<Set<Integer>> maximalStronglyConnectedComponents = new TreeSet<>(
                Comparator.comparingInt(set -> -set.size())
        );

        /*This greedily determines the maximal SCCs because stronglyConnectedComponents is sorted by biggest set first.*/

        Set<Integer> uncheckedVertices = new HashSet<Integer>(vertexSet);
        Iterator<Set<Integer>> sccIt = stronglyConnectedComponents.iterator();

        // Find the maximal SCCs containing each vertex.
        while (!uncheckedVertices.isEmpty()) {
            // Get the next largest SCC.
            Set<Integer> scc = sccIt.next();
            // Mark all vertices in this Maximal SCC as checked.
            uncheckedVertices.removeAll(scc);
            // Move the SCC from our set of SCCs to our set of **maximal** SCCs.
            sccIt.remove();
            maximalStronglyConnectedComponents.add(scc);
        }
        stronglyConnectedComponents = maximalStronglyConnectedComponents;
    }

    /**
     * Searches for strongly connected components in a depth-first manner.
     *
     * @param vertex The source vertex
     */
    private void doSccSearch(int vertex, int visitedVertexCount, int[] preorderNumber,
                             Stack<Integer> unassignedVertices, Stack<Integer> undistinguishedVertices) {
        preorderNumber[vertex] = visitedVertexCount++;
        unassignedVertices.push(vertex);
        undistinguishedVertices.push(vertex);
        for (EdgeNode edge : adjList[vertex]) {
            // If the preorder number of the opposite vertex has not yet been set.
            if (preorderNumber[edge.toVertex] < 0) {
                //Recurse through the search.
                doSccSearch(edge.toVertex, visitedVertexCount, preorderNumber,
                        unassignedVertices, undistinguishedVertices);
                // Else, if that vertex has not been assigned to an SCC yet.
            } else if (undistinguishedVertices.contains(edge.toVertex)) {
                //Repeatedly pop vertices until the top element of the stack has a
                // preorder number less than or equal to the preorder number of the vertex opposite this edge..
                while (preorderNumber[undistinguishedVertices.peek()] > preorderNumber[edge.toVertex]) {
                    undistinguishedVertices.pop();
                }
            }
        }
        // If the source vertex is at the top of the undistinguishedVertices stack
        if (undistinguishedVertices.peek().equals(vertex)) {
            Set<Integer> component = new HashSet<>();
            // Pop vertices from the unassignedVertices stack until this vertex has been popped,
            // and assign the popped vertices to a new component.
            while (!component.contains(vertex)) {
                component.add(unassignedVertices.pop());
            }
            stronglyConnectedComponents.add(component);
            // Pop this vertex from undistinguishedVertices
            undistinguishedVertices.pop();
        }
    }

    /**
     * Accessor for getting the set of Strongly Connected Components.
     *
     * @return
     */
    protected Set<Set<Integer>> getStronglyConnectedComponents() {
        return stronglyConnectedComponents;
    }

    /**
     * Accessor for getting the numbner of vertices.
     *
     * @return
     */
    protected int countVertices() {
        return nVertices;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("Graph:");
        builder = builder.append(" nVertices = ")
                .append(nVertices)
                .append(" nEdges = ")
                .append(nEdges)
                .append("\nAdjacency Lists");
        for (int vertex = 0; vertex < nVertices; vertex++) {
            builder = builder.append("\nv= ")
                    .append(vertex)
                    .append("\t")
                    .append(adjList[vertex]);
        }
        return builder.toString();
    }
} //end Graph class
//place the EdgeNode class and the SPPacket class inside the Graph.java file

/*******************************************/
class EdgeNode {
    int fromVertex;
    int toVertex;
    int weight;

    public EdgeNode(int fromVertex, int toVertex, int weight) {
        this.fromVertex = fromVertex;
        this.toVertex = toVertex;
        this.weight = weight;
    }

    public EdgeNode(int[] orderedTriple) {
        this(orderedTriple[0], orderedTriple[1], orderedTriple[2]);
    }

    @Override
    public String toString() {
        return String.format("(%d,%d,%d)", fromVertex, toVertex, weight);
    }

    @Override
    public int hashCode() {
        return fromVertex ^ toVertex ^ weight;
    }

    @Override
    public boolean equals(Object o) {
        EdgeNode e = (EdgeNode) o;
        return fromVertex == e.fromVertex && toVertex == e.toVertex && weight == e.weight;
    }
}

/***********************************************/
class SPPacket {
    int[] d; //distance array
    int[] parent; //parent path array
    int source; //source vertex
    private ArrayList<Integer>[] paths; //use dynamic programming to list the paths.

    public SPPacket(int start, int[] dist, int[] pp) {
        source = start;
        d = dist;
        parent = pp;
        paths = new ArrayList[parent.length];
        for (int destination = 0; destination < paths.length; destination++) {
            paths[destination] = getPathTo(destination);
        }
    }

    public int[] getDistance() {
        return d;
    }

    public int[] getParent() {
        return parent;
    }

    public int getSource() {
        return source;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(String.format("Shortest Paths from vertex %d to vertex", source));
        // For each destination.
        for (int destination = 0; destination < paths.length; destination++) {
            builder = builder.append("\n")
                    .append(destination)
                    .append(":\t")
                    .append(paths[destination])
                    .append("\t\t Path weight = ")
                    // Treat the unrealistically large INFINITY as Infinity.
                    .append(d[destination] == Graph.INFINITY ? "Infinity" : d[destination]);
        }
        return builder.toString();
    }

    /**
     * This uses dynamic programming to recursively trace the path from source to destination.
     *
     * @param destination The vertex to trace the path to.
     * @return The path to the destination.
     */
    private ArrayList<Integer> getPathTo(int destination) {
        if (paths[destination] == null) {
            ArrayList<Integer> path = new ArrayList<>();
            if (source == destination) {
                path.add(source);
            }
            // If there is a parent in the path to this destination.
            else if (parent[destination] >= 0) {
                path.addAll(getPathTo(parent[destination]));
                path.add(destination);
            }

            paths[destination] = path;
        }
        return paths[destination];
    }
}