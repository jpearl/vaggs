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
import com.vaggs.Route.Route;
import com.vaggs.Route.Waypoint;

@SuppressWarnings("serial")
public class PostRouteServ extends HttpServlet {
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
              throws IOException {
        UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();

        if (user != null) {
			StringBuffer sb = new StringBuffer();
			String line = null;
			try {
				BufferedReader reader = req.getReader();
			    while ((line = reader.readLine()) != null)
			    	sb.append(line);
			} catch (Exception e) { e.printStackTrace(); }
			String routeStr = sb.toString();
			System.err.println("got: " + routeStr);
			
            if(routeStr == null || routeStr.equals("[]")) {
            	resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Bad route");
            	return;
            }

            try {
            	Route route = Route.ParseRouteByWaypoints(routeStr);
            	System.err.println("parsed");
            	for(Waypoint p : route) {
            		System.err.println(p.getPoint().toString());
            	}
            } catch (JSONException e) {
            	resp.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
            	return;
            	
            }
            resp.setStatus(HttpServletResponse.SC_OK);
            
            
        } else {
            resp.sendRedirect(userService.createLoginURL(req.getRequestURI()));
        }
    }
    
    public void doGet(HttpServletRequest req, HttpServletResponse resp) {
    	System.out.println("in get");
    }
}