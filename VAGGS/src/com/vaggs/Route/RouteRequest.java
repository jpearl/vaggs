package com.vaggs.Route;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

/**
 * A request object for a route from a pilot
 * @author Josh Pearl
 *
 * TODO: determine if we need methods for "change request"
 */
@Entity
public class RouteRequest {
	@Id long Id;
	@Index private Transponder transponder;
	
	private RouteRequest() {
		transponder = null;
	}
	
	public RouteRequest(int tCode) {
		this.transponder = new Transponder(tCode);
	}
	
	/**
	 * 
	 * @return true if the RouteRequest has been given a route by the ATC,
	 * false otherwise
	 */
	public boolean hasRoute() {
		return transponder != null && transponder.hasRoute();
	}

	/**
	 * @return the route
	 */
	public Route getRoute() {
		return transponder == null ? null : transponder.getRoute();
	}
	
	/**
	 * @return the transponder object
	 */
	public Transponder getTransponder() {
		return transponder;	
	}
}
