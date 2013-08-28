import java.util.ArrayList;

/**
 * Node class that represents a city
 * 
 * @author linanqiu
 * @file_name Node.java
 */
public class Node {

	private String name;
	private String state;
	private int ID;
	private double lon;
	private double lat;

	private boolean found;
	private Node from;
	private int distance;

	// adj list implementation of graphs
	private ArrayList<Edge> edgeList;

	/**
	 * Construcs a Node. The ID of a node is given by concatenating its name,
	 * state, longitude and latitude, then hashing it. This reduces the chance
	 * of cities having the same ID.
	 * 
	 * @param name
	 * @param state
	 */
	public Node(String name, String state) {
		this.name = name;
		this.state = state;
		ID = (name + state + lon + "" + lat).hashCode();

		edgeList = new ArrayList<Edge>();
	}
	
	// the rest of these methods are trivial
	public void setLocation(double lon, double lat) {
		this.lon = lon;
		this.lat = lat;
	}

	public double getLon() {
		return lon;
	}

	public double getLat() {
		return lat;
	}

	public int hashCode() {
		return ID;
	}

	public String getName() {
		return name;
	}

	public String getState() {
		return state;
	}

	public void addEdge(Edge newEdge) {
		edgeList.add(newEdge);
	}

	public ArrayList<Edge> getEdges() {
		return edgeList;
	}

	public boolean isFound() {
		return found;
	}

	public void setFound(boolean found) {
		this.found = found;
	}

	public Node getFrom() {
		return from;
	}

	public void setFrom(Node from) {
		this.from = from;
	}

	public int getDistance() {
		return distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}
}
