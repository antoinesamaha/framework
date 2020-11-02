package com.foc.web.microservice.entity;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.foc.desc.FocDesc;
import com.foc.desc.FocObject;
import com.foc.web.microservice.FocServletRequest;

public class FocSimpleMicroServlet extends FocEntityServlet<FocObject> {

	@Override
	public FocDesc getFocDesc(FocServletRequest request) {
		return null;
	}

	@Override
	public String getNameInPlural() {
		return null;
	}

	@Override
	public void fillFocObjectFromJson(FocObject focObj, JSONObject jsonObj) throws Exception {
	}
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	}
}
