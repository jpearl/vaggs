package com.vaggs.AirportDiagram;

import static com.vaggs.Utils.OfyService.ofy;

import java.util.List;
import java.util.Set;

import com.google.appengine.labs.repackaged.com.google.common.collect.Lists;
import com.google.appengine.labs.repackaged.com.google.common.collect.Sets;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Load;
import com.vaggs.Route.Taxiway;
import com.vaggs.Route.Waypoint;
import com.vaggs.Utils.LatLng;

/**
 * An Airport Diagram Information
 * @author Josh Pearl
 *
 */
@Entity
public class Airport {
	@Id String airportName;
	private LatLng centerLatLng;
	private LatLng swBound;
	private LatLng neBound;
	private String diagramImage;
	@Load private List<Ref<Taxiway>> taxiways = Lists.newArrayList();
	private List<Waypoint> routeStartingPoints;
	private Set<String> connectedUsers = Sets.newHashSet();

	@SuppressWarnings("unused")
    private Airport() {
		
	}
	
	public Airport(String airportName, LatLng center, LatLng sw, LatLng ne, String image) {
		this.airportName = airportName;
		centerLatLng = center;
		swBound = sw;
		neBound = ne;
		diagramImage = image;
	}
	
	public void addConnectedUser(String user) {
		connectedUsers.add(user);
	}
	
	public void removeConnectedUser(String user) {
		connectedUsers.remove(user);
	}
	
	public List<String> getConnectedUsers() {
		return Lists.newArrayList(connectedUsers);
	}
	
	public LatLng getCenter() {
		return centerLatLng;
	}
	
	public LatLng getSWBound() {
		return swBound;
	}
	
	public LatLng getNEBound() {
		return neBound;
	}
	
	public String getImage() {
		return diagramImage;
	}
	
	/**
	 * @return the taxiways
	 */
	public List<Taxiway> getTaxiways() {
		List<Taxiway> taxis = Lists.newArrayList();
		for(Ref<Taxiway> ref : taxiways) {
			taxis.add(ref.get());
		}
		return taxis;
	}
	
	public void setTaxiways(List<Taxiway> taxis) {
		for(Taxiway taxi1 : taxis) {
			for(Waypoint pt : taxi1.getWaypoints()) {
				boolean isIntersection = false;
				for(Taxiway taxi2 : taxis) {
					if(taxi1 != taxi2 && taxi2.contains(pt)) {
						isIntersection = true;
						break;
					}
				}
				pt.setIntersection(isIntersection);
			}
		}
		
		taxiways.clear();
		for(Taxiway taxi : taxis)
			taxiways.add(Ref.create(ofy().save().entity(taxi).now()));
	}

	/**
	 * @return the routeStartingPoints
	 */
	public List<Waypoint> getRouteStartingPoints() {
		return routeStartingPoints;
	}
	
	public void setRouteStartingPoints(List<Waypoint> pts) {
		for(Waypoint pt : pts)
			pt.setIntersection(true);
		routeStartingPoints = pts;
	}
}
