package com.vaggs.Servlets;

import static com.vaggs.Utils.OfyService.ofy;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.channel.ChannelService;
import com.google.appengine.api.channel.ChannelServiceFactory;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONWriter;
import com.vaggs.Route.Transponder;
import com.vaggs.Utils.Constants;
import com.vaggs.Utils.Constants.CHANNEL_MODE;

@SuppressWarnings("serial")
public class ChannelRegistration extends HttpServlet {	
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {
		resp.setContentType("application/json");
		resp.setCharacterEncoding("UTF-8");
		
		JSONWriter writer = new JSONWriter(resp.getWriter());
		
		CHANNEL_MODE mode = CHANNEL_MODE.getMode(req.getParameter("mode"));
		if(mode != null) {
			String clientId = "";
			switch (mode) {
			case COCKPIT:
				String transponderQuery = req.getParameter("transponder");
				if(null == transponderQuery) {
					resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
					writeError(writer, "Invalid request. Must query for a valid transponder");
					return;
				}
				Long transponderQueryLong = Long.parseLong(req.getParameter("transponder"));
				Transponder transponder = ofy().load().type(Transponder.class).id(transponderQueryLong).get();
				if(transponder == null) {
					resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		            writeError(writer, "No transponder code: " + transponderQueryLong);
					return;
				}
				clientId = transponderQuery;
				ChannelService service = ChannelServiceFactory.getChannelService();
				
				
				break;
			case TOWER:
				User user = UserServiceFactory.getUserService().getCurrentUser();
				if(user == null) {
					resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		            writeError(writer, "Must be logged in to " + Constants.PROJECT_ACRONYM);
		            return;
				} else {
					clientId = user.getNickname();
				}
				
				break;
			}
			ChannelService channelService = ChannelServiceFactory.getChannelService();
			String token = channelService.createChannel(clientId);
			
			try {
				writer.object();
					writer.key("token");
				    writer.value(token);
				writer.endObject();
			} catch (JSONException e) { e.printStackTrace(); }
		} else {
			System.out.println("mode was null");
		}
		
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
