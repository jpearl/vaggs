package com.vaggs.Route;

/**
 * A request object for a route from a pilot
 * @author Josh Pearl
 *
 * TODO: determine if we need methods for "change request"
 */
public class RouteRequest {
	private Route route = null;
	private final int transponderCode;
	
	public RouteRequest(int transponderCode) {
		if(!CheckTransponderCode(transponderCode)) 
			throw new IllegalArgumentException("Invalid Transponder Code");
		this.transponderCode = transponderCode;
	}
	
	/**
	 * Private method for checking validity of Transponder Codes
	 * @return true if the argument is between the octal numbers 0000 and 7777
	 */
	boolean CheckTransponderCode(int code) {
		for(int i = 0; i < 3; i++) {
			if(code % 10 > 7) return false;
			code /= 10;
		}
		return code != 0;
	}
	
	/**
	 * 
	 * @return true if the RouteRequest has been given a route by the ATC,
	 * false otherwise
	 */
	public boolean hasRoute() {
		return null != route;
	}

	/**
	 * @return the route
	 */
	public Route getRoute() {
		return route;
	}

	/**
	 * @param route the route to set
	 */
	public void setRoute(Route route) {
		this.route = route;
	}

	/**
	 * @return the transponderCode
	 */
	public int getTransponderCode() {
		return transponderCode;
	}

	
}
