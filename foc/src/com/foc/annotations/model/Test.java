package com.foc.annotations.model;

import com.foc.desc.FocConstructor;
import com.foc.desc.FocObject;

@FocEntity()
public class Test extends FocObject {

	public static final String Nickname = "Nickname";
	
	public Test(FocConstructor constr) {
		super(constr);
	}

//	@FocField()
//	public String getNickname(){
//		return getPropertyString(Nickname);
//	}
}
