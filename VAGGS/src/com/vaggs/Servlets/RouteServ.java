package com.vaggs.Servlets;

import java.io.*;
import javax.servlet.http.*;
import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONWriter;
import com.vaggs.Route.Waypoint;
import com.vaggs.Utils.LatLng;

@SuppressWarnings("serial")
public class RouteServ extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		resp.setContentType("application/json");
		resp.setCharacterEncoding("UTF-8");
		
		int transponder = Integer.parseInt(req.getParameter("transponder"));

		JSONWriter writer = new JSONWriter(resp.getWriter());
		try {
			writer.object();
				writer.key("pts");
				writer.array();
					writePoint(writer, new Waypoint(new LatLng(41.7258, -71.4368), true));
					writePoint(writer, new Waypoint(new LatLng(41.7087976, -71.44134), false));
					writePoint(writer, new Waypoint(new LatLng(41.73783, -71.41615), false));
					writePoint(writer, new Waypoint(new LatLng(41.725, -71.433333), true));
				writer.endArray();
			writer.endObject();
		} catch (JSONException e) { e.printStackTrace(); }
	}
	
	void writePoint(JSONWriter writer, Waypoint pt) throws JSONException {
		writer.object();
		writer.key("Lat");
		writer.value(pt.getPoint().getLat());
		writer.key("Lng");
		writer.value(pt.getPoint().getLng());
		writer.key("Holdshort");
		writer.value(pt.isHoldShort());
		writer.endObject();
	}
}
