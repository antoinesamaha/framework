package com.foc.gui.progressBar;

public interface IProgressClass {
	public String  getRuntimeMessage();
	public String  getSuccessMessage();
	public String  getInterruptedMessage();
	public void    setRequestInterruption();
	public boolean isInterrupted();
	public boolean isSuccessful();
}
