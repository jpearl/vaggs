package com.vaggs.Route;

import java.util.List;

import com.google.appengine.labs.repackaged.com.google.common.collect.Lists;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Load;

import static com.vaggs.Utils.OfyService.ofy;

@Entity
public class FavoriteRoutes {
	@Id private String airport;
	@Load private List<Ref<Route>> routes = Lists.newArrayList();
	
	@SuppressWarnings("unused")
    private FavoriteRoutes() {
		airport = "";
	}
	
	public FavoriteRoutes(String airport) {
		this.airport = airport;
		routes = Lists.newArrayList();
	}
	
	public FavoriteRoutes(String airport, List<Route> routes) {
		this.airport = airport;
		for(Route r : routes) {
			addRoute(r);
		}
	}
	
	public void addRoute(Route route) {
		routes.add(Ref.create(ofy().save().entity(route).now()));
	}
	
	/*public void removeRoute(Route route) {
		routes.remove(route);
	}*/
	
	public List<Route> getRoutes() {
		List<Route> routeList = Lists.newArrayList();
		for(Ref<Route> ref : routes) {
			routeList.add(ref.get());
		}
		return routeList;
	}
	
	public String getAirport() {
		return airport;
	}
}
