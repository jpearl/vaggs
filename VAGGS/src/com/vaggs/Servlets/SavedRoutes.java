package com.vaggs.Servlets;

import static com.vaggs.Utils.OfyService.ofy;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONWriter;
import com.vaggs.Route.FavoriteRoutes;
import com.vaggs.Route.Route;
import com.vaggs.Route.Waypoint;

@SuppressWarnings("serial")
public class SavedRoutes extends HttpServlet {
	
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
		
		FavoriteRoutes favRoutes = ofy().load().type(FavoriteRoutes.class).id(query).get();
		
		if (null == favRoutes) {
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            writeError(writer, "No favorite routes for airport: " + query);
			return;
		}
		
		try {
			writer.array();
				for(Route r : favRoutes.getRoutes()) {
						writer.object();
							writer.key("routeName");
							writer.value(r.getName());
							writer.key("route");
							writer.array();
								for(Waypoint w : r) {
									writeWaypoint(writer, w);
								}
							writer.endArray();
						writer.endObject();
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
		writer.object();
			writer.key("Lat");
			writer.value(w.getPoint().getLat());
			writer.key("Lng");
			writer.value(w.getPoint().getLng());
			writer.key("Holdshort");
			writer.value(w.isHoldShort());
			writer.key("Intersection");
			writer.value(w.isIntersection());
		writer.endObject();
	}
}
