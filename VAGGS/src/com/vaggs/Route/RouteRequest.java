package com.vaggs.Route;

/**
 * A request object for a route from a pilot
 * @author Josh Pearl
 *
 * TODO: determine if we need methods for "change request"
 */
public class RouteRequest {
	private Route route = null;
	private final String transponderCode;
	
	public RouteRequest(String transponderCode) {
		this.transponderCode = transponderCode;
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
	public String getTransponderCode() {
		return transponderCode;
	}

	
}
