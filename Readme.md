# GPS Program
lq2137
Linan Qiu

## Dependencies
- Java 1.7

## Files Included
- Calculation.java
- Cerebro.java
- Edge.java
- Node.java
- NodeNotFoundException.java
- ProfessorX.java

## Compilation
Compile all three files by placing them in the same directory, and running 

    javac *.java

Run the main class by running, in the same directory as the compiled files

    java ProfessorX

## Notes
These are some notes that you should know before running the program

### IDs
Each ID for each city is likely to be unique. It is generated from `name + state + longitude + latitude`, then calling the `String.hashCode()` function provided by Java on the entire string. It is likely to be long, but will be unique for each city.

### Spherical Earth
All distances are calculated taking into account the spherical nature of the earth, hence the `Calculation.java` class.

I practically learnt geodesics for this assignment. That took me hours. >< Well, at least this solves the problem of inaccurate distances.

### K Nearest Neighbor Search
Due to the ambiguity of the question, and the fact that I attempted the homework early, I implemented the KNN search based on geographical distance only.

> I have approached Professor Hershkop, and he said it is fine that I implemented this even though he would have preferred a BFS search. Just in case, I implemented a BFS search that searches for all connected neighbors, then ranks them by distance. However, please grade this homework based on the geographical distance search (ie. option 6) only. 

For a small search (ie. K < size of list / 100) which is usually used by GPS programs, I implemented a spiral search that takes into account the spherical nature of the earth. It spirals outwards using an archimedian spiral, and finds the nearest neighbors. It then continues one more revolution just to be sure. 

This is implemented using two HashMaps that index the longitude and latitude down to the nearest integer of every city. 

This algorithm is around 100 times faster than brute force.

For larger searches (ie. K > size of list / 100), a brute force NKK is used since the spiral search tends to be inaccurate and slower. ie. the distance from current city to every city is calculated, then ranked. 

A public method automatically switches between these two based on the queries.

Alternatively, a BFS search that indexes all connected neighbors, then ranks them by distance is provided. It works, but please do not base your grading on this.

## Quickstart
1. First, when prompted, type in the full file name of the cities data list. It must be in the same directory.
2. Each of the functions and their runtimes are explained later.

## Functions
Each menu choice will be explained here with their runtimes.

### Load new data file
Loads an entirely new data file into the program. Most time consuming portion is adding edges to vertices. Runtime is O(V), since each vertice will be visited, and edges will be added to each of them. (Between 2 to 6, on average 4, edges are added to each vertice.)

### Search for state
Searching for state looks it up from a HashMap<String, ArrayList<Node>> `stateLookup`, where the String is the name of the state and the ArrayList<Node> represents all cities associated with that state. 

Runtime is O(1) assuming a good hash. 

### Search for city
Searches for a city given its name. Searches from HashMap<String, Node> `cityList` (note `cityList` is different from `cityLookup`. `cityLookup` is a HashMap<Integer, Node> that maps cities to their IDs.)

Runtime is O(1) assuming a good hash (which I have provided via the hashcode concatenation)

### Set current city
Sets current city as a hash. Constant time.

### Show current city
Shows current city. Constant time.

### Find n closest cities
Finds n closest cities (no matter whether they are connected or not). Two different algorithms are used depending on query size. For more information, look at the notes section above.

The spiral search will do the search in O(n), where n is the size of the query, since the time taken to spiral merely depends how many revolutions it has to go, which is directly correlated with the query size. It is much faster because it does not have to visit every vertice.

The brute force search will do the search in O(VlogV), since it has to search through the entire list O(V), calculating the distance of each vertice the current city, then sorts them using TimSort(O(VlogV)), hence O(VlogV).

> Test cases show that the spiral search is usually 100 times faster than the brute force search.

### Find n closest connected cities  
Does a breadth first search on all cities connected to the current city, then ranks them by distance. BFS takes O(E + V), then calculating distance takes O(V), and finally ranking takes O(VlogV).  In total, this takes O(E + V + VlogV), and since this is a sparse graph, this takes O(VlogV).

### Find shortest path between current and target city
Performs a dijkstra between current city and target city, replacing them with random cities if they are not set or the target city ID is nonexistent. Throws a NodeNotFoundException if no path exists (checked by popping out the cities along the way, and not arriving at the original city). Takes O(E + VlogV) for dijkstra, and since this is a sparse graph, takes O(VlogV). 
