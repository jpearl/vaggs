package com.vaggs.Utils;

public class LatLng {
	private double lat;
	private double lng;
	
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
