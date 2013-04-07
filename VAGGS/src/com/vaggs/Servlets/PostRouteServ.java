package com.vaggs.Servlets;

import java.io.IOException;
import javax.servlet.http.*;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.vaggs.Route.Route;

@SuppressWarnings("serial")
public class PostRouteServ extends HttpServlet {
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
              throws IOException {
        UserService userService = UserServiceFactory.getUserService();
        User user = userService.getCurrentUser();

        if (user != null) {
            String routeParam = req.getParameter("route");
            if(routeParam == null || routeParam.isEmpty()) {
            	resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Bad route");
            }
            
            try {
            	Route.ParseRouteByWaypoints(routeParam);
            } catch (JSONException e) {
            	resp.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
            	
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