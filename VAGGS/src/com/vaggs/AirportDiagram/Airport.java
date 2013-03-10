package com.vaggs.AirportDiagram;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.vaggs.Utils.LatLng;

/**
 * An Airport Diagram Information
 * @author Josh Pearl
 *
 */
@Entity
public class Airport {
	@Id Long id;
	String airportName;
	private LatLng centerLatLng;
	private LatLng swBound;
	private LatLng neBound;
	private String diagramImage;
	
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
}
