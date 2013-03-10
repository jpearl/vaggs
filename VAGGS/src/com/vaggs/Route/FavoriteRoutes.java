package com.vaggs.Route;

import java.util.Iterator;
import java.util.List;

import com.google.appengine.labs.repackaged.com.google.common.collect.Lists;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

@Entity
public class FavoriteRoutes {
	@Id private String airport;
	private List<Route>	routes;
	
	@SuppressWarnings("unused")
    private FavoriteRoutes() {
		airport = "";
		routes = null;
	}
	
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
