package com.vaggs.Servlets;

import static com.vaggs.Utils.OfyService.ofy;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONWriter;
import com.vaggs.Route.Route;
import com.vaggs.Route.Transponder;
import com.vaggs.Route.Waypoint;

@SuppressWarnings("serial")
public class RouteServ extends HttpServlet {
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		resp.setContentType("application/json");
		resp.setCharacterEncoding("UTF-8");
		
		JSONWriter writer = new JSONWriter(resp.getWriter());
		
		String query = req.getParameter("transponder");
		if(null == query) {
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			writeError(writer, "Invalid request. Must query for a valid transponder");
			return;
		}
		Long transponderQuery = Long.parseLong(req.getParameter("transponder"));
		Transponder transponder = ofy().load().type(Transponder.class).id(transponderQuery).get();
		if(transponder == null) {
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            writeError(writer, "No transponder code: " + transponderQuery);
			return;
		}
		
		Route route = transponder.getRoute();

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
	
	void writePoint(JSONWriter writer, Waypoint pt) throws JSONException {
		writer.object();
		writer.key("Lat");
		writer.value(pt.getPoint().getLat());
		writer.key("Lng");
		writer.value(pt.getPoint().getLng());
		writer.key("Holdshort");
		writer.value(pt.isHoldShort());
		writer.endObject();
	}
	
	void writeError(JSONWriter writer, String error) {
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
}
