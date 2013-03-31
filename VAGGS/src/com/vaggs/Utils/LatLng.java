package com.vaggs.Utils;

import com.googlecode.objectify.annotation.Embed;

@Embed
public class LatLng {
	private double lat;
	private double lng;
	
	private LatLng() {
		lat = -1;
		lng = -1;
	}
	
	public LatLng(double lat, double lng) {
		this.lat = lat;
		this.lng = lng;
	}
	
	public double getLat() {
		return lat;
	}
	
	public double getLng() {
		return lng;
	}
	
	public String toString() {
		return "(" + lat + ", " + lng + ")";
	}
	
	public boolean equals(LatLng o) {
		return this.lat == o.lat && this.lng == o.lng;
	}
}
