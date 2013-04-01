package com.vaggs.Route;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

/**
 * An object that represents a transponder code
 */
@Entity
public class Transponder {
	private Route route = null;
	@Id private long code;
	
	public static Transponder Parse(String code) {
		try {
			return new Transponder(Integer.parseInt(code));
		} catch(Exception e) {
			return null;
		}
	}
	
	private Transponder() {
		code = -1;
	}
	
	public Transponder(int code) {
		if(!CheckTransponderCode(code)) 
			throw new IllegalArgumentException("Invalid Transponder Code: " + code);
		this.code = code;
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
		return code == 0;
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
	public long getTransponderCode() {
		return code;
	}
}
