package com.vaggs.Route;

import java.util.Iterator;
import java.util.List;

import com.google.appengine.labs.repackaged.com.google.common.collect.Lists;

public class FavoriteRoutes {
	private String airport = "";
	private List<Route>	routes = null;
	
	public FavoriteRoutes(String airport) {
		this.airport = airport;
		routes = Lists.newArrayList();
	}
	
	public FavoriteRoutes(String airport, List<Route> routes) {
		this.routes = routes;
		this.airport = airport;
	}
	
	public void addRoute(Route route) {
		routes.add(route);
	}
	
	public void removeRoute(Route route) {
		routes.remove(route);
	}
	
	public Iterator<Route> iterator() {
		if(routes != null)
			return routes.iterator();
		else
			return null;
	}
	
	public String getAirport() {
		return airport;
	}
}
