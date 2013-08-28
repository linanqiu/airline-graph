/**
 * Abstract class with static methods used in calculations for the GPS program
 * 
 * @author linanqiu
 * @file_name Calculation.java
 */
public abstract class Calculation {

	public static final double EARTH_RADIUS = 6371000; // Meters. Because who
														// the hell uses miles.

	/**
	 * This method calculates the haversine of a given radian
	 * 
	 * haversine is adopted in favor of vincenty due to simplicity of
	 * calculation, and the fact that a few meters don't really matter for
	 * flights.
	 * 
	 * @return double haversine(double rad)
	 */
	public static double haversine(double rad) {
		return Math.pow(Math.sin(rad / 2), 2);
	}

	/**
	 * Returns the distance between two pairs of lats and lons
	 * 
	 * @param lat1
	 *            the latitude of city 1 in degrees
	 * @param lat2
	 *            the latitude of city 2 in degrees
	 * @param lon1
	 *            the longitude of city 1 in degrees
	 * @param lon2
	 *            the longitude of city 2 in degrees
	 * 
	 * @return the metric distance between the two pairs
	 */
	private static double distance(double lat1, double lat2, double lon1,
			double lon2) {
		double rlat1 = Math.toRadians(lat1);
		double rlat2 = Math.toRadians(lat2);
		double rlon1 = Math.toRadians(lon1);
		double rlon2 = Math.toRadians(lon2);

		return EARTH_RADIUS * centralAngle(rlat1, rlat2, rlon1, rlon2);
	}

	/**
	 * Calculates the central angle between two pairs of lats and lons
	 * 
	 * @param rlat1
	 *            the latitude of city 1 in rad
	 * @param rlat2
	 *            the latitude of city 2 in rad
	 * @param rlon1
	 *            the longitude of city 1 in rad
	 * @param rlon2
	 *            the longitude of city 2 in rad
	 * 
	 * @return the central angle between these two pairs
	 */
	private static double centralAngle(double rlat1, double rlat2,
			double rlon1, double rlon2) {
		double ca = 2 * Math
				.asin(Math.sqrt(haversine(rlat2 - rlat1) + Math.cos(rlat1)
						* Math.cos(rlat2) * haversine(rlon2 - rlon1)));

		return ca;
	}

	/**
	 * Returns the distance between the two nodes using the great circle
	 * distance formula
	 * 
	 * @param citya
	 *            first city
	 * @param cityb
	 *            second city
	 * @return the distance between the first city and the second
	 */
	public static double distance(Node citya, Node cityb) {
		return distance(citya.getLat(), cityb.getLat(), citya.getLon(),
				cityb.getLon());
	}

	/**
	 * Finds destination coordinates in degrees given the starting coordinates,
	 * distance, and bearing. Using the great circle formula.
	 * 
	 * @param r
	 *            distance
	 * @param theta
	 *            bearing
	 * @param lon1
	 *            starting longitude
	 * @param lat1
	 *            starting latitude
	 * @return ending coordinates
	 */
	public static double[] destination(double r, double theta, double lon1,
			double lat1) {
		lon1 = Math.toRadians(lon1);
		lat1 = Math.toRadians(lat1);

		double lat2 = Math
				.asin(Math.sin(lat1) * Math.cos(r / EARTH_RADIUS)
						+ Math.cos(lat1) * Math.sin(r / EARTH_RADIUS)
						* Math.cos(theta));
		double lon2 = lon1
				+ Math.atan2(
						Math.sin(theta) * Math.sin(r / EARTH_RADIUS)
								* Math.cos(lat1),
						Math.cos(r / EARTH_RADIUS) - Math.sin(lat1)
								* Math.sin(lat2));

		lon2 = Math.toDegrees(lon2);
		lat2 = Math.toDegrees(lat2);
		double[] answer = { lon2, lat2 };
		return answer;
	}

	/**
	 * Calculates the hypotenuse of a right angled triangle
	 * 
	 * @param x
	 *            length of x
	 * @param y
	 *            length of y
	 * @return length of hypotenuse
	 */
	public static double hypotenuse(double x, double y) {
		double h = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
		return h;
	}
}
