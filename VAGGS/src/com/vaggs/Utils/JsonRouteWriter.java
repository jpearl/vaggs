package com.vaggs.Utils;

import java.io.StringWriter;

import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONWriter;
import com.vaggs.Route.Route;
import com.vaggs.Route.Waypoint;

public class JsonRouteWriter {

	public static String writeRoute(Route route) {
		StringWriter sw = new StringWriter();
		JSONWriter writer = new JSONWriter(sw);
		writeRoute(writer, route);
		return sw.toString();
	}
	
	public static void writeRoute(JSONWriter writer, Route route) {
		try {
			writer.object();
			writer.key("pts");
			writer.array();
				for (Waypoint point : route) {
					writePoint(writer, point);
				}
			writer.endArray();				
			writer.endObject();
		} catch (JSONException e) { e.printStackTrace(); }
	}
	
	private static void writePoint(JSONWriter writer, Waypoint pt) throws JSONException {
		writer.object();
		writer.key("Lat");
		writer.value(pt.getPoint().getLat());
		writer.key("Lng");
		writer.value(pt.getPoint().getLng());
		writer.key("Holdshort");
		writer.value(pt.isHoldShort());
		writer.endObject();
	}

}
