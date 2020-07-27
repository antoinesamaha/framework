package com.foc.web.microservice.entity;

import org.json.JSONObject;

import com.foc.desc.FocDesc;
import com.foc.desc.FocObject;

public class FocSimpleMicroServlet extends FocEntityServlet<FocObject> {

	@Override
	public FocDesc getFocDesc() {
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
