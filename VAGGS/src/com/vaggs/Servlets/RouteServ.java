package com.vaggs.Servlets;

import static com.vaggs.Utils.OfyService.ofy;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.channel.ChannelMessage;
import com.google.appengine.api.channel.ChannelService;
import com.google.appengine.api.channel.ChannelServiceFactory;
import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONWriter;
import com.vaggs.Route.Route;
import com.vaggs.Route.Transponder;
import com.vaggs.Utils.JsonRouteWriter;

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
		JsonRouteWriter.writeRoute(writer, route);
		
		//debugging: send via channel
		System.out.println("sending to " + transponderQuery.toString());
		ChannelService channelService = ChannelServiceFactory.getChannelService();
	    channelService.sendMessage(new ChannelMessage(transponderQuery.toString(), JsonRouteWriter.writeRoute(route)));
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
