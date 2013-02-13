package com.vaggs.Utils;

public class LatLng {
	private float lat;
	private float lng;
	
	public LatLng(float lat, float lng) {
		this.lat = lat;
		this.lng = lng;
	}
	
	public float getLat() {
		return lat;
	}
	
	public float getLng() {
		return lng;
	}
	
	public String toString() {
		return "(" + lat + ", " + lng + ")";
	}
}
