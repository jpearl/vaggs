package com.vaggs.Route;

import com.vaggs.Utils.LatLng;

/**
 * Represents a Waypoint along a Taxi Route
 * @author Josh Pearl
 *
 */
public class Waypoint {
	private LatLng point; /* the physical location of the waypoint */
	private boolean holdShort; /* if the pilot must hold short of this point */
	
	public Waypoint(LatLng point, boolean holdShort) {
		this.point = point;
		this.holdShort = holdShort;
	}

	/**
	 * @return the point
	 */
	public LatLng getPoint() {
		return point;
	}


	/**
	 * @return if the pilot must hold short of this point
	 */
	public boolean isHoldShort() {
		return holdShort;
	}
	
	
}
