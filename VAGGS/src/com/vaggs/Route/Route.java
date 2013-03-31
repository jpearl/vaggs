package com.vaggs.Route;

import java.util.Iterator;
import java.util.List;

import com.google.appengine.labs.repackaged.com.google.common.collect.Lists;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.cmd.LoadType;

import static com.vaggs.Utils.OfyService.ofy;

/**
 * A route for a plane to follow
 * @author Hawkwood
 *
 */
@Entity
public class Route implements Iterable<Waypoint>{
	@Id Long id; 
	private List<Waypoint> route = null;
	
	private Route() {
		route = Lists.newArrayList();
	}
	
	static Route ParseRoute(Waypoint start, String str) {
		Route route = new Route();
		char[] chars = str.toCharArray();
		LoadType<Taxiway> q = ofy().load().type(Taxiway.class);
		Taxiway prevTaxiway = q.id(chars[0]).get();
		for(int i = 1; i < chars.length; i++) {
			Taxiway taxiway = q.id(chars[i]).get();
			Waypoint pt = prevTaxiway.intersection(taxiway);
			route.addWaypoints(prevTaxiway.PtsBetween(start, pt));
			prevTaxiway = taxiway;
			start = pt;
		}
		route.addWaypoints(prevTaxiway.PtsBetween(start, null));
		return route;
	}
	
	/**
	 * Adds all waypoints in a list to the route
	 */
	void addWaypoints(List<Waypoint> pts) {
		for(Waypoint pt : pts)
			route.add(pt);
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
