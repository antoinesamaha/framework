package com.foc.web.unitTesting;

public class FocUnitTestLevel {
	private FocUnitTest test = null;
	private int commandIndex = -1;
	
	public FocUnitTestLevel(FocUnitTest test, int commandIndex){
		this.test = test;
		this.commandIndex = commandIndex;
	}
	
	public void dispose(){
		test = null;
		commandIndex = -1;
	}

	public int getCommandIndex() {
		return commandIndex;
	}

	public void setCommandIndex(int commandIndex) {
		this.commandIndex = commandIndex;
	}

	public FocUnitTest getTest() {
		return test;
	}
}
