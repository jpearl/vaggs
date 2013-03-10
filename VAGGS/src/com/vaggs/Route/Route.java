package com.vaggs.Route;

import java.util.Iterator;
import java.util.List;

import com.google.appengine.labs.repackaged.com.google.common.collect.Lists;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

/**
 * A route for a plane to follow
 * @author Josh Pearl
 *
 */
@Entity
public class Route implements Iterable<Waypoint>{
	@Id Long id; 
	private List<Waypoint> route = null;
	
	public Route() {
		route = Lists.newArrayList();
	}
	
	public Route(List<Waypoint> route) {
		this.route = route;
	}
	
	/**
	 * Add a waypoint at the end of the current route
	 * @param point the waypoint to add to the end of the route
	 */
	public void addWaypoint(Waypoint point) {
		route.add(point);
	}

	/**
	 * Remove the specified point from the current route
	 * such that forall indices i > {@code point}'s index j,
	 * i = i - 1 
	 * @param point the point to remove from the route
	 */
	public void removeWaypoint(Waypoint point) {
		route.remove(point);
	}
	
	/**
	 * Get an iterator to the route in order of waypoints
	 * @return the iterator
	 */
	@Override
	public Iterator<Waypoint> iterator() {
		if (null != route) {
			return route.iterator();
		}
		return null;
	}
	
	


}
