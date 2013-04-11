package com.vaggs.Route;

import java.util.ArrayList;
import java.util.List;

import com.google.appengine.labs.repackaged.com.google.common.collect.Lists;
import com.googlecode.objectify.annotation.Embed;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

/**
 * Represents a Taxiway
 * @author Hawkwood
 *
 */
@Entity
@Embed
public class Taxiway {
	@Id String name;
	private List<Waypoint> pts;
	
	private Taxiway() {
		this.pts = Lists.newArrayList();
		this.name = "";
	}
	
	public Taxiway(String name) {
		this.pts = Lists.newArrayList();
		this.name = name;
	}
	
	/**
	 * Finds the intersection between
	 * this taxiway and the passed taxiway
	 * @return 
	 */
	public Waypoint intersection(Taxiway taxiway) {
		for(Waypoint pt : pts){
			if(taxiway.contains(pt))
				return pt;
		}
		return null;
	}
	
	public boolean contains(Waypoint pt) {
		for(Waypoint p : pts)
			if(p.equals(pt)) return true;
		return false;
	}
	
	public ArrayList<Waypoint> PtsBetween(Waypoint start, Waypoint end) {
		ArrayList<Waypoint> ans = Lists.newArrayList();
		int endI, startI = pts.indexOf(start);
		if(end == null)
			endI = pts.size();
		else endI = pts.indexOf(end);
		int diff = endI - startI;
		if(diff == 0) {
			ans.add(start);
		} else if (diff > 0) {
			for(int i = startI; i < endI; i++)
				ans.add(pts.get(i));
		} else if (diff < 0) {
			for(int i = startI; i > endI; i--)
				ans.add(pts.get(i));
		}
		return ans;
	}

	/**
	 * @return the pts
	 */
	public List<Waypoint> getWaypoints() {
		return pts;
	}
	
	public void setWaypoints(List<Waypoint> pts) {
		this.pts = pts;
	}
}
