import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;

public class ProfessorX {

	/**
	 * Main method that allows menu choice and, catches exceptions and processes
	 * output data from Cerebro. Quite trivial.
	 * 
	 * @param args
	 * @throws FileNotFoundException
	 */
	public static void main(String[] args) throws FileNotFoundException {

		Cerebro cerebro = new Cerebro();
		Scanner scan = new Scanner(System.in);
		System.out.println("Cerebro started");

		System.out.println("Enter the file name");
		System.out.print("> ");
		String input = scan.nextLine();

		long start = System.currentTimeMillis();
		cerebro.loadNew(input);
		long end = System.currentTimeMillis();
		long length = end - start;
		System.out.println("Completed in " + length + "ms");

		int option = 1;
		while (option < 9 && option > 0) {
			try {
				System.out.println();
				System.out.println("Enter integer for menu choice");
				System.out.println("1 Load new data file");
				System.out.println("2 Search for state");
				System.out.println("3 Search for city");
				System.out.println("4 Set current city");
				System.out.println("5 Show current city");
				System.out.println("6 Find n closest cities");
				System.out.println("7 Find n closest connected cities");

				System.out
						.println("8 Find shortest path between current and target city");
				System.out.println("9 Quit");
				System.out.print("> ");

				option = Integer.valueOf(scan.nextLine());

				switch (option) {
				case 1:
					System.out.println("Enter the file name");
					System.out.print("> ");
					input = scan.nextLine();

					start = System.currentTimeMillis();
					cerebro.loadNew(input);
					end = System.currentTimeMillis();
					length = end - start;
					System.out.println("Completed in " + length + "ms");
					break;
				case 2:
					System.out.println("Enter the name of the state");
					System.out.print("> ");
					input = scan.nextLine();

					start = System.currentTimeMillis();
					ArrayList<Node> cities = cerebro.searchState(input);
					end = System.currentTimeMillis();
					length = end - start;
					System.out.println("Completed in " + length + "ms");
					System.out.println("Cities belonging to " + input);
					System.out.println("\t" + "City ID" + "\t\t" + "City Name");
					for (Node city : cities) {
						System.out.println("\t" + city.hashCode() + "\t"
								+ city.getName());
					}
					break;
				case 3:
					System.out.println("Enter the name of the city");
					System.out.print("> ");
					input = scan.nextLine();

					start = System.currentTimeMillis();
					int hash = cerebro.searchCity(input);
					end = System.currentTimeMillis();
					length = end - start;
					System.out.println("Completed in " + length + "ms");
					System.out.println(input + "'s hashcode is " + hash);
					break;
				case 4:
					System.out
							.println("Enter the hashcode of the city to set as current city");
					System.out.print("> ");
					int hashcode = Integer.valueOf(scan.nextLine());

					start = System.currentTimeMillis();
					cerebro.setCurrentCity(hashcode);
					end = System.currentTimeMillis();
					length = end - start;
					System.out.println("Completed in " + length + "ms");
					System.out.println("Current city set to "
							+ cerebro.getCurrentCity().getName());
					break;
				case 5:
					start = System.currentTimeMillis();
					Node currentCity = cerebro.getCurrentCity();
					end = System.currentTimeMillis();
					length = end - start;
					System.out.println("Completed in " + length + "ms");
					System.out.println("Current city is "
							+ currentCity.getName());
					break;
				case 6:
					System.out.println("How many closest neighbors?");
					System.out.print("> ");

					int n = Integer.valueOf(scan.nextLine());

					start = System.currentTimeMillis();
					ArrayList<Node> answers = cerebro.getKNN(n);
					end = System.currentTimeMillis();
					length = end - start;
					System.out.println("Completed in " + length + "ms");

					System.out.println("Closest cities of "
							+ cerebro.getCurrentCity().getName() + "("
							+ cerebro.getCurrentCity().getLon() + " "
							+ cerebro.getCurrentCity().getLat() + ")");
					System.out.println("\tName\t\tCoordinates");
					for (Node answer : answers) {

						if (answer.getName().length() >= 8) {
							System.out.println("-\t" + answer.getName() + "\t"
									+ answer.getLon() + "\t" + answer.getLat());
						} else {
							System.out.println("-\t" + answer.getName()
									+ "\t\t" + answer.getLon() + "\t"
									+ answer.getLat());
						}

					}
					break;
				case 7:
					System.out.println("How many closest connected neighbors?");
					System.out.print("> ");

					n = Integer.valueOf(scan.nextLine());

					start = System.currentTimeMillis();
					answers = cerebro.BFS(n);
					end = System.currentTimeMillis();
					length = end - start;
					System.out.println("Completed in " + length + "ms");

					System.out.println("Closest connected cities of "
							+ cerebro.getCurrentCity().getName() + "("
							+ cerebro.getCurrentCity().getLon() + " "
							+ cerebro.getCurrentCity().getLat() + ")");
					System.out.println("\tName\t\tCoordinates");
					for (Node answer : answers) {

						if (answer.getName().length() >= 8) {
							System.out.println("-\t" + answer.getName() + "\t"
									+ answer.getLon() + "\t" + answer.getLat());
						} else {
							System.out.println("-\t" + answer.getName()
									+ "\t\t" + answer.getLon() + "\t"
									+ answer.getLat());
						}

					}
					break;

				case 8:
					System.out.println("Enter ID of target city.");
					System.out.print("> ");

					n = Integer.valueOf(scan.nextLine());

					start = System.currentTimeMillis();
					Stack<Node> answerStack = cerebro.findShortestPath(n);
					end = System.currentTimeMillis();
					length = end - start;
					System.out.println("Completed in " + length + "ms");

					System.out.println("\tName\t\tDistance From Current City");

					while (!answerStack.isEmpty()) {
						Node answer = answerStack.pop();

						if (answer.getName().length() >= 8) {
							System.out.println("-\t" + answer.getName() + "\t"
									+ answer.getDistance());
						} else {
							System.out.println("-\t" + answer.getName()
									+ "\t\t" + answer.getDistance());
						}
					}
					break;
				}
			} catch (NodeNotFoundException e) {
				System.out
						.println("No path connects these two cities. Run the command again with some other target.");
			} catch (NumberFormatException e) {
				System.out.println("Invalid input. Run the command again.");
			} catch (FileNotFoundException e) {
				System.out.println("File not found. Run the command again.");
			} catch (NullPointerException e) {
				System.out.println("Invalid input. Run the command again.");
			}
		}

		System.out.println("Thanks for being a great TA!");

	}
}
