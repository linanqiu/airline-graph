import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.Stack;
import java.util.TreeMap;
import java.util.concurrent.ArrayBlockingQueue;

public class Cerebro {

	private HashMap<String, ArrayList<Node>> stateLookup;
	private HashMap<String, Node> cityList;
	private HashMap<Integer, Node> cityLookup;

	private HashMap<Integer, ArrayList<Node>> lonLookup;
	private HashMap<Integer, ArrayList<Node>> latLookup;

	private Node currentCity;

	public static final double THETA_DEGREE_INCREMENT = 1;
	public static final double ARCHIMEDEAN_CONSTANT = 10000;
	public static final int MINIMUM_EDGE = 2;
	public static final int UNCHANGED = 360;

	public Cerebro() {
		stateLookup = new HashMap<String, ArrayList<Node>>();
		cityLookup = new HashMap<Integer, Node>();
		cityList = new HashMap<String, Node>();

		lonLookup = new HashMap<Integer, ArrayList<Node>>();
		latLookup = new HashMap<Integer, ArrayList<Node>>();
	}

	/**
	 * Loads a new file completely
	 * 
	 * @param path
	 *            the path of the text file
	 * @throws FileNotFoundException
	 */
	public void loadNew(String path) throws FileNotFoundException {

		File txt = new File(path);
		Scanner scan = new Scanner(txt);
		int size = Integer.valueOf(scan.nextLine());
		System.out.print(".");

		while (scan.hasNext()) {
			String[] names = scan.nextLine().split(", ");
			String city;
			String state;
			try {
				city = names[0];
				state = names[1];
			} catch (ArrayIndexOutOfBoundsException e) {
				city = names[0];
				state = names[0];
			}
			Node newCity = new Node(city, state);
			newCity.setLocation(Double.valueOf(scan.nextLine()),
					Double.valueOf(scan.nextLine()));

			// add to stateLookup
			if (stateLookup.containsKey(state.toLowerCase())) {
				stateLookup.get(state.toLowerCase()).add(newCity);
			} else {
				stateLookup.put(state.toLowerCase(), new ArrayList<Node>());
				stateLookup.get(state.toLowerCase()).add(newCity);
			}

			// add to cityLookup
			cityLookup.put(newCity.hashCode(), newCity);

			// add to cityList
			cityList.put(newCity.getName().toLowerCase(), newCity);

			// add to lonLookup and latLookup
			if (lonLookup.containsKey((int) newCity.getLon())) {
				lonLookup.get((int) newCity.getLon()).add(newCity);
			} else {
				lonLookup.put((int) newCity.getLon(), new ArrayList<Node>());
				lonLookup.get((int) newCity.getLon()).add(newCity);
			}

			if (latLookup.containsKey((int) newCity.getLat())) {
				latLookup.get((int) newCity.getLat()).add(newCity);
			} else {
				latLookup.put((int) newCity.getLat(), new ArrayList<Node>());
				latLookup.get((int) newCity.getLat()).add(newCity);
			}
		}

		if (size == cityLookup.size() && size == lonLookup.size()
				&& size == latLookup.size()) {

		}
		System.out.print(".");
		addEdges();
		System.out.print(".");
		System.out.println();
		scan.close();
	}

	/**
	 * Adds randomized edges to all nodes
	 */
	private void addEdges() {

		Iterator<Node> ite = cityLookup.values().iterator();
		while (ite.hasNext()) {
			Node aCity = ite.next();

			int edgesToAdd = (int) (MINIMUM_EDGE + Math.random() * 5);
			for (int i = 0; i < edgesToAdd; i++) {
				int weight = (int) (200 + Math.random() * 801);
				aCity.addEdge(new Edge(aCity, getRandomCityNotSelf(aCity),
						weight));
			}
		}
	}

	/**
	 * Searches for a state's cities given the state's name
	 * 
	 * @param state
	 *            the name of the state
	 * @return the ArrayList<Node> of cities in that state
	 */
	public ArrayList<Node> searchState(String state) {
		state = state.toLowerCase();
		if (stateLookup.containsKey(state)) {
			return stateLookup.get(state);
		} else {
			throw new NullPointerException();
		}
	}

	/**
	 * Gets a random city. Not the most efficient method, but suffices for
	 * testing.
	 * 
	 * @return a random Node
	 */
	public Node getRandomCity() {

		Node[] allCities = new Node[cityLookup.size()];
		cityLookup.values().toArray(allCities);
		int random = (int) (Math.random() * cityLookup.size());

		return allCities[random];
	}

	/**
	 * Gets a random city that is not itself
	 * 
	 * @param self
	 *            the city currently used
	 * @return a random city in the map that is not self
	 */
	private Node getRandomCityNotSelf(Node self) {
		Node[] allCities = new Node[cityLookup.size()];
		cityLookup.values().toArray(allCities);
		int random = (int) (Math.random() * cityLookup.size());

		while (allCities[random].hashCode() == self.hashCode()) {
			random = (int) (Math.random() * cityLookup.size());
		}

		return allCities[random];
	}

	/**
	 * Searches for a city given its name
	 * 
	 * @param name
	 *            the name of the city
	 * @return the city with the name
	 */
	public int searchCity(String name) {

		name = name.toLowerCase();

		if (cityList.containsKey(name)) {
			return cityList.get(name).hashCode();
		} else {
			throw new NullPointerException();
		}
	}

	/**
	 * Searches for a city given its ID
	 * 
	 * @param ID
	 *            the hashcode of the city
	 * @return the city with the hashcode
	 */
	public Node searchCityID(int ID) {

		if (cityLookup.containsKey(ID)) {
			return cityLookup.get(ID);
		} else {
			throw new NullPointerException();
		}
	}

	/**
	 * Sets the currentCity
	 * 
	 * @param ID
	 *            the hashcode of the city to be made currentCity
	 */
	public void setCurrentCity(int ID) {
		if (cityLookup.containsKey(ID)) {
			currentCity = cityLookup.get(ID);
		} else {
			throw new NullPointerException();
		}
	}

	/**
	 * Gets the currentCity
	 * 
	 * @return the currentCity
	 */
	public Node getCurrentCity() {
		return currentCity;
	}

	/**
	 * Sets distance of all cities to max value, set found false, and set from
	 * to null
	 */
	private void preDijkstraReset() {
		Iterator<Node> ite = cityLookup.values().iterator();
		while (ite.hasNext()) {
			Node aCity = ite.next();

			aCity.setDistance(Integer.MAX_VALUE);
			aCity.setFound(false);
			aCity.setFrom(null);
		}
	}

	/**
	 * the public method for finding shorting path using currentCity as the
	 * starting and a given endCity
	 * 
	 * @param n
	 *            the hashcode of the target city. If invalid, will be set to a
	 *            random city
	 * @return a Stack which when popped will show the pah
	 */
	public Stack<Node> findShortestPath(int n) {

		if (!cityLookup.containsKey(n)) {
			n = getRandomCity().hashCode();
			System.out
					.println("Invalid target city ID. Replacing with random city instead.");
		}

		Node endCity = searchCityID(n);

		if (currentCity == null) {
			currentCity = getRandomCity();
		}

		return dijkstra(currentCity, endCity);
	}

	/**
	 * the internal method for finding shortest path with a given startCity and
	 * an endCity using an improved dijkstra's algorithm implemented with
	 * priority queues. Throws NodeNotFoundException if a path doesn't exist.
	 * 
	 * @param startCity
	 *            the starting city
	 * @param endCity
	 *            the ending city
	 * @return a Stack which when popped shows the shortest path
	 */
	private Stack<Node> dijkstra(Node startCity, Node endCity) {
		preDijkstraReset();

		Stack<Node> answer = new Stack<Node>();

		PriorityQueue<Node> nodeQueue = new PriorityQueue<Node>(
				cityLookup.size(), new DijkstraComparator());
		Iterator<Node> ite = cityLookup.values().iterator();

		while (ite.hasNext()) {
			Node aCity = ite.next();

			nodeQueue.add(aCity);
		}

		int notFound = nodeQueue.size();

		nodeQueue.remove(startCity);
		startCity.setDistance(0);

		nodeQueue.add(startCity);

		while (notFound > 0) {
			Node smallest = nodeQueue.remove();

			smallest.setFound(true);
			notFound--;

			for (Edge edge : smallest.getEdges()) {
				if (!edge.getTarget().isFound()) {
					int cost = edge.getWeight();

					if (edge.getTarget().getDistance() > smallest.getDistance()
							+ cost) {
						nodeQueue.remove(edge.getTarget());
						edge.getTarget().setDistance(
								smallest.getDistance() + cost);
						edge.getTarget().setFrom(smallest);
						nodeQueue.add(edge.getTarget());
					}
				}
			}
		}

		Node pointer = endCity;
		answer.push(pointer);

		while (pointer.hashCode() != startCity.hashCode()) {
			if (pointer.getFrom() != null) {
				pointer = pointer.getFrom();
				answer.push(pointer);
			} else {
				throw new NodeNotFoundException();
			}
		}

		return answer;
	}

	/**
	 * public method for getting the k-nearest neighbors. selects either hte
	 * brute force algorithm or the spiral algorithm. The spiral algorithm is
	 * less useful for when n / total number of cities is more than 10%. In that
	 * case, the brute force algorithm is used. However, since most of the time,
	 * searches are much less than 1% of cities, then the spiral method takes
	 * only around 1% the time of the brute force method.
	 * 
	 * @param n
	 *            the number of closest neighbors to find
	 * @return an ArrayList<Node> of the closest neighbors
	 */
	public ArrayList<Node> getKNN(int n) {

		if (currentCity == null) {
			currentCity = getRandomCity();
		}

		if ((double) n / cityLookup.size() > 0.01) {
			return getBruteKNN(n);
		} else {
			return getSpiralKNN(n);
		}
	}

	/**
	 * A KNN algorithm that spirals outwards in an archimedian spiral to find
	 * the nearest neighbors.
	 * 
	 * @param n
	 *            the number of closest neighbors to find
	 * @return an ArrayList<Node> of the closest neighbors
	 */
	private ArrayList<Node> getSpiralKNN(int n) {

		TreeMap<Node, String> neighbors = new TreeMap<Node, String>(
				new DistanceComparator());

		double startx = currentCity.getLon();
		double starty = currentCity.getLat();

		double currentx = startx;
		double currenty = starty;

		double r = 0;
		double startThetaDegree = 0;

		ArrayList<Node> initialNeighbors = getCities((int) startx, (int) starty);
		for (Node foundCity : initialNeighbors) {
			if (!neighbors.containsKey(foundCity)) {
				neighbors.put(foundCity, "");
			}
		}

		int unchanged = 0;

		while (unchanged < UNCHANGED) {

			double startThetaRadian = Math.toRadians(startThetaDegree);
			r = ARCHIMEDEAN_CONSTANT * startThetaRadian;
			double[] cartesian = Calculation.destination(r, startThetaRadian,
					startx, starty);

			if ((int) (cartesian[0]) == (int) currentx
					&& (int) (cartesian[1]) == (int) currenty) {

			} else {

				currentx = cartesian[0];
				currenty = cartesian[1];

				ArrayList<Node> intersection = getCities((int) currentx,
						(int) currenty);

				boolean addUnchanged = true;

				if (intersection != null && intersection.size() > 0) {
					for (Node foundCity : intersection) {
						if (!neighbors.containsKey(foundCity)) {
							if (neighbors.size() < n + 1) {
								neighbors.put(foundCity, "");
								unchanged = 0;
								addUnchanged = false;
							} else if (neighbors.higherEntry(foundCity) != null) {
								neighbors.remove(neighbors.lastKey());
								neighbors.put(foundCity, "");
								unchanged = 0;
								addUnchanged = false;
							}
						}
					}
				}

				if (addUnchanged && neighbors.size() > n) {
					unchanged++;
				} else {
					unchanged = 0;
				}
			}

			startThetaDegree += THETA_DEGREE_INCREMENT;

		}

		neighbors.remove(currentCity);

		ArrayList<Node> KNN = new ArrayList<Node>(neighbors.keySet());
		Collections.sort(KNN, new DistanceComparator());
		ArrayList<Node> answer = new ArrayList<Node>();
		for (int i = 0; i < n; i++) {
			answer.add(KNN.get(i));
		}

		KNN = answer;

		return KNN;
	}

	/**
	 * Gets the bucket of cities in a given longitude and latitude (integer)
	 * 
	 * @param lon
	 *            the integer longitude
	 * @param lat
	 *            the integer latitude
	 * @return the cities with the given longitude and latitude
	 */
	private ArrayList<Node> getCities(int lon, int lat) {
		ArrayList<Node> intersection = new ArrayList<Node>();

		ArrayList<Node> lonCities = lonLookup.get(lon);

		HashMap<Node, String> getLookup = new HashMap<Node, String>();

		if (lonCities != null && lonCities.size() > 0) {

			for (Node lonCity : lonCities) {
				getLookup.put(lonCity, "");
			}

			ArrayList<Node> latCities = latLookup.get(lat);

			if (latCities != null && latCities.size() > 0) {
				for (Node latCity : latCities) {
					if (getLookup.containsKey(latCity)) {
						intersection.add(latCity);
					}
				}
				return intersection;
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	/**
	 * A KNN algorithm that calculates distance from currentCity of every single
	 * city, then finds the n shortest ones
	 * 
	 * @param n
	 *            the number of closest neighbors to find
	 * @return an ArrayList<Node> of closest neighbors
	 */
	private ArrayList<Node> getBruteKNN(int n) {

		ArrayList<Node> allCities = new ArrayList<Node>(cityLookup.values());

		Collections.sort(allCities, new DistanceComparator());
		allCities.remove(currentCity);

		ArrayList<Node> KNNTrimmed = new ArrayList<Node>();

		for (int i = 0; i < n; i++) {
			KNNTrimmed.add(allCities.get(i));
		}

		return KNNTrimmed;
	}

	/**
	 * Public method for conducting a BFS
	 * 
	 * @param n
	 *            number of closest neighbors to find
	 * @return an ArrayList<Node> containing the knearest connected neighbors
	 */
	public ArrayList<Node> BFS(int n) {

		if (currentCity == null) {
			return BFS(getRandomCity(), n);
		}
		return BFS(currentCity, n);
	}

	/**
	 * Internal method for conducting a breadth first search, thereby
	 * identifying all connected nodes. Then it will search for the k nearest
	 * neighbors in that sublist.
	 * 
	 * @param startCity
	 *            the starting city
	 * @param n
	 *            the number of closest neighbors to find
	 * @return an ArrayList<Node> containing the k nearest connected neighbors
	 */
	private ArrayList<Node> BFS(Node startCity, int n) {

		preDijkstraReset();

		ArrayBlockingQueue<Node> nodeQueue = new ArrayBlockingQueue<Node>(
				cityLookup.size());
		ArrayList<Node> answer = new ArrayList<Node>();

		nodeQueue.add(startCity);
		startCity.setFound(true);

		while (!nodeQueue.isEmpty()) {
			Node temp = nodeQueue.remove();
			answer.add(temp);

			for (Edge edge : temp.getEdges()) {
				Node otherTemp = edge.getTarget();

				if (!otherTemp.isFound()) {
					otherTemp.setFound(true);
					nodeQueue.add(otherTemp);
				}
			}
		}

		answer.remove(0);

		Collections.sort(answer, new DistanceComparator());

		ArrayList<Node> answerReturn = new ArrayList<Node>();

		for (int i = 0; i < n; i++) {
			answerReturn.add(answer.get(i));
		}

		return answerReturn;
	}

	/**
	 * The comparator used to compare the distance of two nodes, used in
	 * Dijkstra
	 * 
	 * @author linanqiu
	 * @file_name Cerebro.java
	 */
	public class DijkstraComparator implements Comparator<Node> {

		@Override
		public int compare(Node o1, Node o2) {
			if (o1.getDistance() > o2.getDistance()) {
				return 1;
			} else if (o1.getDistance() < o2.getDistance()) {
				return -1;
			} else {
				return 0;
			}
		}
	}

	/**
	 * The comparator used to compare the weights of two edges, used in kruskal
	 * 
	 * @author linanqiu
	 * @file_name Cerebro.java
	 */
	public class EdgeComparator implements Comparator<Edge> {

		@Override
		public int compare(Edge o1, Edge o2) {
			if (o1.getWeight() > o2.getWeight()) {
				return 1;
			} else if (o1.getWeight() < o2.getWeight()) {
				return -1;
			} else {
				return 0;
			}
		}

	}

	/**
	 * A comparator that compares the distance of two cities from currentCity
	 * 
	 * @author linanqiu
	 * @file_name Cerebro.java
	 */
	public class DistanceComparator implements Comparator<Node> {

		@Override
		public int compare(Node o1, Node o2) {
			double distance1 = Calculation.distance(currentCity, o1);
			double distance2 = Calculation.distance(currentCity, o2);

			if (distance1 > distance2) {
				return 1;
			} else if (distance2 > distance1) {
				return -1;
			} else {
				return 0;
			}
		}
	}
}
