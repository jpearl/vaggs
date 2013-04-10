package com.vaggs.Servlets;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONObject;
import com.google.appengine.labs.repackaged.org.json.JSONTokener;
import com.vaggs.Route.Route;
import com.vaggs.Route.Transponder;
import com.vaggs.Utils.AtcUser;

@SuppressWarnings("serial")
public class PostRouteServ extends HttpServlet {
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
              throws IOException {
        UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();
        
        if (user != null && AtcUser.GetUser(user.getNickname()) != null) {
			StringBuffer sb = new StringBuffer();
			String line = null;
			try {
				BufferedReader reader = req.getReader();
			    while ((line = reader.readLine()) != null)
			    	sb.append(line);
			} catch (Exception e) { e.printStackTrace(); }
			String routeStr = sb.toString();
            
            try {
        		JSONObject obj = new JSONObject(new JSONTokener(routeStr));
            	Route route = Route.ParseRouteByWaypoints(obj.getJSONArray("route"));
            	Transponder transponder = Transponder.Parse(obj.getLong("transponder"));
            	if(route == null) {
            		resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid Route");
            	} else if(transponder == null) {
	            	resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Transponder Code out of range");
            	} else {
        			transponder.setRoute(route);
                    resp.setStatus(HttpServletResponse.SC_OK);            		
            	}
            } catch (JSONException e) {
            	resp.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
            }
        } else {
        	resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
        }
    }
    
    public void doGet(HttpServletRequest req, HttpServletResponse resp) {
    	System.out.println("in get");
    }
}