package com.vaggs.AirportDiagram;

import com.vaggs.Utils.LatLng;

/**
 * Parser for Airport Diagram Information
 * @author Josh Pearl
 *
 */
public class AirportXMLParser {
	
	public static Airport parse(String file) {
		//TODO: fill this in
		
		//for now default airport is PVD
		return new Airport(new LatLng(41.7258, -71.4368), new LatLng(41.7087976, -71.44134), new LatLng(41.73783, -71.41615), "airports/pvd2.png");
	}
}
