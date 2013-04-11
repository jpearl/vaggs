package com.vaggs.Servlets;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONWriter;
import com.vaggs.Route.Route;
import com.vaggs.Route.Transponder;
import com.vaggs.Utils.JsonRouteWriter;

@SuppressWarnings("serial")
public class RouteQueryServ extends HttpServlet {
	
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
		
		Transponder transponder = Transponder.Parse(Long.parseLong(req.getParameter("transponder")));
		if(transponder == null) {
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Transponder Code out of range");
		} else if (transponder.hasRoute()){
			Route route = transponder.getRoute();
			JsonRouteWriter.writeRoute(writer, route);
		}
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
}
