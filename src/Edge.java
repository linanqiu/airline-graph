/**
 * An edge in the graph
 * 
 * @author linanqiu
 * @file_name Edge.java
 */
public class Edge {

	private int weight;
	private Node target;
	private Node source;

	/**
	 * Constructs an edge
	 * 
	 * @param source
	 *            the source node
	 * @param target
	 *            the target node
	 * @param weight
	 *            the weight assigned to the node
	 */
	public Edge(Node source, Node target, int weight) {
		this.source = source;
		this.target = target;
		this.weight = weight;
	}

	// rest of these methods are trivial
	public void setWeight(int weight) {
		this.weight = weight;
	}

	public void setTarget(Node target) {
		this.target = target;
	}

	public int getWeight() {
		return weight;
	}

	public Node getTarget() {
		return target;
	}

	public Node getSource() {
		return source;
	}

	public void setSource(Node source) {
		this.source = source;
	}
}
