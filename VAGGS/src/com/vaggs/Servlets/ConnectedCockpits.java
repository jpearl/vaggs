package com.vaggs.Servlets;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.vaggs.Route.Transponder;
import com.vaggs.Utils.VAGGSJsonWriter;

@SuppressWarnings("serial")
public class ConnectedCockpits extends HttpServlet {
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		resp.setContentType("application/json");
		resp.setCharacterEncoding("UTF-8");
		
		VAGGSJsonWriter writer = new VAGGSJsonWriter(resp.getWriter());
		String query = req.getParameter("airport");
		if(null == query) {
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			writer.writeError("Invalid request. Must query for a valid airport");
			return;
		}
		
		if(!query.equals("kpvd")) {
			resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            writer.writeError("Currently only supports KPVD");
			return;
		}
	
		writer.writeCockpits(Transponder.getAllActive());
	}
}
