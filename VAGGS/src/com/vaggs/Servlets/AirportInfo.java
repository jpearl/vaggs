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
import com.vaggs.Utils.VAGGSJsonWriter;

@SuppressWarnings("serial")
public class AirportInfo extends HttpServlet {
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		resp.setContentType("application/json");
		resp.setCharacterEncoding("UTF-8");
		
		VAGGSJsonWriter writer = new VAGGSJsonWriter(resp.getWriter());
		String query = req.getParameter("airport");
		if(null == query) {
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			writer.writeError("Invalid request. Must query for a valid airport");
			return;
		}
		
		if(!query.equals("kpvd")) {
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            writer.writeError("Currently only supports KPVD");
			return;
		}
		
		Airport airport = ofy().load().type(Airport.class).id(query).get();
		
		if (null == airport) {
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            writer.writeError("No airport: " + query);
			return;
		}
		
		writer.writeAirportTaxiways(airport);
		
	}
}
