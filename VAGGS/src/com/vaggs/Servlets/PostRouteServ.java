package com.vaggs.Servlets;

import static com.vaggs.Utils.OfyService.ofy;

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
import com.vaggs.Route.FavoriteRoutes;
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
        	boolean saveMode = req.getParameter("saveRoute") != null;
        	String routeStr = req.getParameter("data");
            
            try {
        		JSONObject obj = new JSONObject(new JSONTokener(routeStr));
            	if(!saveMode) {
            		Route route = Route.ParseRouteByWaypoints(obj.getJSONArray("route"), "");
            		Transponder transponder = Transponder.Parse(obj.getLong("transponder"));
            		if(route == null) {
            			resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid Route");
            		} else if(transponder == null) {
            			resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Transponder Code out of range");
            			return;
            		} else {
            			transponder.setRoute(route);
                        resp.setStatus(HttpServletResponse.SC_OK);            		
                	}
            	} else {
            		String routeName = obj.getString("routeName");
            		String airportName = obj.getString("airport");
            		Route route = Route.ParseRouteByWaypoints(obj.getJSONArray("route"), routeName);
            		if(route == null) {
            			resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid Route");
            		} else if(routeName == null || routeName.isEmpty()) {
            			resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid Route Name");
            		} else if(airportName == null || airportName.isEmpty()) {
            			resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid Airport Name");
            		} else {
            			FavoriteRoutes favs = ofy().load().type(FavoriteRoutes.class).id(airportName).get();
            			if(favs == null) {
            				favs = new FavoriteRoutes(airportName);
            			}
            			favs.addRoute(route);
            			ofy().save().entity(favs).now();
            		}
            	}
            } catch (JSONException e) {
            	resp.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
            }
        } else {
        	resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
        }
    }
    
}