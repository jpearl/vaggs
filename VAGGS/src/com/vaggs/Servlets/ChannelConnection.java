package com.vaggs.Servlets;

import static com.vaggs.Utils.OfyService.ofy;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.channel.ChannelPresence;
import com.google.appengine.api.channel.ChannelService;
import com.google.appengine.api.channel.ChannelServiceFactory;
import com.vaggs.AirportDiagram.Airport;


/**
 * TODO: use this to parse presense. Currently not in use
 * @author Josh Pearl
 *
 */

@SuppressWarnings("serial")
public class ChannelConnection extends HttpServlet {
		public void doPost(HttpServletRequest req, HttpServletResponse resp)
				throws IOException {
			
			ChannelService cs = ChannelServiceFactory.getChannelService();
			ChannelPresence presense = cs.parsePresence(req);
			
			String clientId = presense.clientId();
			/*Airport airport = ofy().load().type(Airport.class).id("kpvd").get();
			
			if(req.getRequestURI().contains("disconnected")) {
				airport.removeConnectedUser(clientId);
				System.out.println("removing client: " + clientId);
			} else if(req.getRequestURI().contains("connected")) {
				airport.addConnectedUser(clientId);
				System.out.println("adding client: " + clientId);
			} */
			
		}
}
