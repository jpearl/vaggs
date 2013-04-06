package com.vaggs.Servlets;

import static com.vaggs.Utils.OfyService.ofy;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONWriter;
import com.vaggs.AirportDiagram.Airport;
import com.vaggs.Route.Taxiway;
import com.vaggs.Route.Waypoint;

@SuppressWarnings("serial")
public class AirportInfo extends HttpServlet {
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		resp.setContentType("application/json");
		resp.setCharacterEncoding("UTF-8");
		
		JSONWriter writer = new JSONWriter(resp.getWriter());
		String query = req.getParameter("airport");
		if(null == query) {
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			writeError(writer, "Invalid request. Must query for a valid airport");
			return;
		}
		
		if(!query.equals("kpvd")) {
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            writeError(writer, "Currently only supports KPVD");
			return;
		}
		
		Airport airport = ofy().load().type(Airport.class).id(query).get();
		if (null == airport) {
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            writeError(writer, "No airport: " + query);
			return;
		}
		
		try {
			writer.array();
				writer.array();
				for(Waypoint w : airport.getRouteStartingPoints()) {
					writeWaypoint(writer, w);
				}
				writer.endArray();
				for(Taxiway t : airport.getTaxiways()) {
					writer.array();
					for(Waypoint w : t.getWaypoints()) {
						writeWaypoint(writer, w);
					}
					writer.endArray();
				}
			writer.endArray();
			
		} catch (JSONException e) { e.printStackTrace(); }
		
	}
	
	private void writeError(JSONWriter writer, String error) {
		try {
	        writer.object();
		    	writer.key("error");
		    	writer.object();
		    		writer.key("description");
		    		writer.value(error);
		    	writer.endObject();
	    	writer.endObject();
		} catch (JSONException e) {
	        e.printStackTrace();
        }
	}
	
	private void writeWaypoint(JSONWriter writer, Waypoint w) throws JSONException {
		writer.key("Lat");
		writer.value(w.getPoint().getLat());
		writer.key("Lng");
		writer.value(w.getPoint().getLng());
		writer.key("isHoldshort");
		writer.value(w.isHoldShort());
	}
}
