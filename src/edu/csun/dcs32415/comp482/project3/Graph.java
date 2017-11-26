package edu.csun.dcs32415.comp482.project3;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;

public class Graph { //------------------------------------------------------
    private ArrayList< EdgeNode >[] adjList;
    private int nVertices;
    private int nEdges;

    /* Calculated by isStronglyConnected. */
    private Set<Set<Integer>> stronglyConnectedComponents;

    private String fileName;
    /******************  Constructor**********************/
    /**
     * Construct the graph by specification from a file whose name is contained in the given string, inputFileName.
     * @param inputFileName
     */
    public Graph(String inputFileName) {
        try {
            init(new FileInputStream(inputFileName));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("Error reading input file. Now reading from Standard Input...");
            init(System.in);
        }
    }

    /**
     * Constructor using Standard Input instead of a file name. Useful for testing.
     */
    private Graph() {
        init(System.in);
    }

    /**
     * Private constructor-helper to initialize the graph from any input stream of integers.
     * The program will crash if the input stream is malformed.
     * @param input The input stream containing the graph in the following format:
     *              - The first integer should be the number of vertices.
     *              - The remaining integers should represent the edges.
     *              - An edge should be an three consecutive integers: sourceVertexId, destinationVertexId, and weight.
     *              - Vertex IDs should be unique and non-negative, and the highest-numbered ID should be nVertices - 1.
     */
    private void init(InputStream input) {
        Scanner scanner = new Scanner(input);
        nVertices = scanner.nextInt();
        nEdges = 0;
        adjList = new ArrayList[nVertices];
        int[] rawEdge = new int[3];
        while(scanner.hasNext()) {
            for (int i = 0; i < rawEdge.length; i++) {
                rawEdge[i] = scanner.nextInt();
            }
            adjList[rawEdge[0]].add(new EdgeNode(rawEdge));
            nEdges++;
        }

        computeStronglyConnectedComponents();
    }

    /******************Print graph method***************/
    public void printGraph() {}
    /******************* BFS Shortest paths  ******************/
    public SPPacket bfsShortestPaths(int start) {
        return null;
    }
    /********************Dijkstra's Shortest Path Algorithm*** */
    public SPPacket dijkstraShortestPaths(int start) {
        return null;
    }
    /********************Bellman Ford Shortest Paths ***************/
    public SPPacket bellmanFordShortestPaths(int start) {
        return null;
    }
    /***********************Prints shortest paths*************************/
    public void printShortestPaths(SPPacket spp) {
        System.out.println(spp.toString());
    }
    /*****************isStronglyConnected***************************/
    public boolean isStronglyConnected() {
        if (stronglyConnectedComponents == null || stronglyConnectedComponents.isEmpty()) {
            computeStronglyConnectedComponents();
        }
        return stronglyConnectedComponents.size() == 1;
    }

    /**
     *  Compute a set of Strongly connected components.
     *  Represented as a set of sets of vertices.
     *  Result is stored in an instance variable by the doSccSearch function.
     *  https://en.wikipedia.org/wiki/Path-based_strong_component_algorithm
     */
    private void computeStronglyConnectedComponents() {
        int visitedVertexCount = 0;
        int[] preorderNumber = new int[nVertices];
        Stack<Integer> unassignedVertices = new Stack<>(); // unassigned to a SCC
        Stack<Integer> undistinguishedVertices = new Stack<>(); //undistinguished from other SCCs.
        for (int vertex = 0; vertex < nVertices; vertex++) {
            preorderNumber[vertex] = -1;
        }
        stronglyConnectedComponents = new HashSet<>();
        for (int vertex = 0; vertex < nVertices; vertex++) {
            doSccSearch(vertex, visitedVertexCount, preorderNumber, unassignedVertices, undistinguishedVertices);
        }
    }

    /**
     * Searches for strongly connected components in a depth-first manner.
     * @param vertex The source vertex
     */
    private void doSccSearch (int vertex, int visitedVertexCount, int[] preorderNumber,
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
            } else if (unassignedVertices.contains(edge.toVertex)) {
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
     * @return
     */
    protected Set<Set<Integer>> getStronglyConnectedComponents() {
        return stronglyConnectedComponents;
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
    public String toString() {
        return String.format("(%d,%d,%d)", fromVertex, toVertex, weight);
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
        for( int destination = 0; destination < paths.length; destination++) {
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
    public String toString() {
        StringBuilder builder = new StringBuilder(String.format("Shortest Paths from vertex %d to vertex",source));
        for (int destination = 0; destination < paths.length; destination++) {
            builder = builder.append("\n")
                    .append(destination)
                    .append(":\t")
                    .append(paths[destination].toString())
                    .append("\t Path weight = ")
                    .append(d[destination]);
        }
        return builder.toString();
    }

    /**
     * This uses dynamic programming to recursively trace the path from source to destination.
     * @param destination The vertex to trace the path to.
     * @return The path to the destination.
     */
    private ArrayList<Integer> getPathTo(int destination) {
        if (paths[destination] == null) {
            ArrayList<Integer> path = new ArrayList<>();
            if (parent[destination] >= 0) {
                path.add(destination);
                path.addAll(getPathTo(parent[destination]));
                paths[destination] = path;
            }
            paths[destination] = path;
        }
        return paths[destination];
    }
}