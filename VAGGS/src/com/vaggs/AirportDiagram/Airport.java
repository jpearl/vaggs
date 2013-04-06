package com.vaggs.AirportDiagram;

import java.util.List;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
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
	private List<Taxiway> taxiways;
	private List<Waypoint> routeStartingPoints;

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
		return taxiways;
	}
	
	public void setTaxiways(List<Taxiway> taxiways) {
		this.taxiways = taxiways;
	}

	/**
	 * @return the routeStartingPoints
	 */
	public List<Waypoint> getRouteStartingPoints() {
		return routeStartingPoints;
	}
	
	public void setRouteStartingPoints(List<Waypoint> pts) {
		routeStartingPoints = pts;
	}
}
