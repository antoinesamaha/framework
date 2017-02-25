package com.foc.menu;

public class FMenuSeparator extends FMenuItem{

	public FMenuSeparator(){
		super("", ' ', null);
	}
	
	@Override
	public boolean isSeparator() {
		return true;
	}
}
