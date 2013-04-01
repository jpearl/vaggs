package com.vaggs.Route;

import static com.vaggs.Utils.OfyService.ofy;

import java.util.Iterator;
import java.util.List;

import com.google.appengine.labs.repackaged.com.google.common.collect.Lists;
import com.googlecode.objectify.annotation.Embed;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.cmd.LoadType;

/**
 * A route for a plane to follow
 * @author Hawkwood
 *
 */
@Embed
@Entity
public class Route implements Iterable<Waypoint>{
	@Id Long id; 
	private List<Waypoint> route = null;
	
	private Route() {
		route = Lists.newArrayList();
	}
	
	public static Route ParseRoute(Waypoint start, String str) {
		if(start == null || str == null || str.isEmpty())
			return new Route();
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
	 * TODO: change to private
	 * Adds all waypoints in a list to the route
	 */
	public void addWaypoints(List<Waypoint> pts) {
		for(Waypoint pt : pts)
			route.add(pt);
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
