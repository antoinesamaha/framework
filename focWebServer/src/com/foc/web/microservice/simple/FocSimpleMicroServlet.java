package com.foc.web.microservice.simple;

import org.json.JSONObject;

import com.foc.desc.FocDesc;
import com.foc.desc.FocObject;

public class FocSimpleMicroServlet extends FocSimpleObjectServlet<FocObject> {

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
