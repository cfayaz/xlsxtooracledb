package com.fayaz.xcel.servlets;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fayaz.xcel.logicallayer.DBDetailServletHelper;
import com.google.gson.Gson;

@WebServlet(name = "DBDetailHelper", value = "/DBDetailHelper")
public class DBDetailHelperServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4338393352748294323L;

	static Logger logger = LoggerFactory.getLogger(DBDetailHelperServlet.class);

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		DBDetailServletHelper helper = new DBDetailServletHelper();
		
		String mode = (String) req.getParameter("mode");
		String json = "";
		if (mode.equals("0")) {
			List<String> list = helper.getConnectionNames();
			json = new Gson().toJson(list);

		} else {
			String connId = (String) req.getParameter("id");
			HashMap<String, String> connMap = helper.getConnDetails(connId);
			json = new Gson().toJson(connMap);
		}
		resp.setContentType("application/json");
		resp.setCharacterEncoding("UTF-8");
		resp.getWriter().write(json);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		DBDetailServletHelper helper = new DBDetailServletHelper();
		HashMap<String,String> connMap = new HashMap<String, String>();
		connMap.put("host", (String) req.getParameter("host"));
		connMap.put("name", (String) req.getParameter("name"));
		connMap.put("sid", (String) req.getParameter("sid"));
		connMap.put("port", (String) req.getParameter("port"));
		connMap.put("user", (String) req.getParameter("user"));
		connMap.put("pwd", (String) req.getParameter("pwd"));
		helper.saveConnection(connMap, "ORCL");
	}

}
