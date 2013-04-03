package com.vaggs.Servlets;

import static com.vaggs.Utils.OfyService.ofy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.channel.ChannelService;
import com.google.appengine.api.channel.ChannelServiceFactory;
import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONWriter;
import com.vaggs.Route.Transponder;

@SuppressWarnings("serial")
public class ChannelRegistration extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {
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
		
		ChannelService channelService = ChannelServiceFactory.getChannelService();
		String token = channelService.createChannel(String.valueOf(transponder.getTransponderCode()));
		
		try {
			writer.object();
				writer.key("token");
			    writer.value(token);
			writer.endObject();
		} catch (JSONException e) { e.printStackTrace(); }
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
