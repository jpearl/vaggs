package com.vaggs.Route;

import com.googlecode.objectify.annotation.Embed;
import com.vaggs.Utils.LatLng;

/**
 * Represents a Waypoint along a Taxi Route
 * @author Josh Pearl
 *
 */
@Embed
public class Waypoint {
	private LatLng point; /* the physical location of the waypoint */
	private boolean holdShort; /* if the pilot must hold short of this point */
	private boolean intersection;
	
	private Waypoint() {
		point = null;
		holdShort = false;
		intersection = false;
	}
	
	public Waypoint(LatLng point, boolean holdShort, boolean intersection) {
		this.point = point;
		this.holdShort = holdShort;
		this.intersection = intersection;
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
	
	public boolean isIntersection() {
		return intersection;
	}
	
	public void setIntersection(boolean value) {
		intersection = value;
	}
	
	public boolean equals(Waypoint p) {
		return this.point.equals(p.point);
	}
}
