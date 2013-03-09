package com.vaggs.Servlets;

import java.io.*;
import javax.servlet.http.*;
import com.google.appengine.labs.repackaged.org.json.JSONException;
import com.google.appengine.labs.repackaged.org.json.JSONWriter;
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
					writeLatLng(writer, new LatLng(41.7258, -71.4368));
					writeLatLng(writer, new LatLng(41.7087976, -71.44134));
					writeLatLng(writer, new LatLng(41.73783, -71.41615));
					writeLatLng(writer, new LatLng(41.725, -71.433333));
				writer.endArray();
			writer.endObject();
		} catch (JSONException e) { e.printStackTrace(); }
	}
	
	void writeLatLng(JSONWriter writer, LatLng pt) throws JSONException {
		writer.object();
		writer.key("Lat");
		writer.value(pt.getLat());
		writer.key("Lng");
		writer.value(pt.getLng());
		writer.endObject();
	}
}
