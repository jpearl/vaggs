package com.vaggs.AirportDiagram;

import com.vaggs.Utils.LatLng;

/**
 * An Airport Diagram Information
 * @author Josh Pearl
 *
 */
public class Airport {
	private LatLng centerLatLng;
	private LatLng swBound;
	private LatLng neBound;
	private String diagramImage;
	
	public Airport(LatLng center, LatLng sw, LatLng ne, String image) {
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
