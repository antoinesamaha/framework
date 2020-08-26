package com.foc.web.microservice.entity;

import org.json.JSONObject;

import com.foc.desc.FocDesc;
import com.foc.desc.FocObject;
import com.foc.web.microservice.FocServletRequest;

public class FocSimpleMicroServlet extends FocEntityServlet<FocObject, FocObject> {

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
}
